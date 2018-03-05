# sbt-secrets

An SBT plugin for securely storing secrets in a VCS.

## Usage

In `plugins.sbt`, add `addSbtPlugin("com.evenfinancial" % "sbt-secrets" % "0.1.1")`.

## Motivation

From a security standpoint, committing things like passwords or API keys to a VCS is a pretty terrible idea, regardless of whether the repo is public or private. On the other hand, it's really convenient to keep all of a project's data in one place. One resolution to this conflict is to encrypt the secret data before committing, and decrypt it after pulling new changes. The goal of **sbt-secrets** is to make that process easy.

## API

The generic API consists of three keys:

* `secretFiles` - a list of files that contain secrets, and thus need to be managed by sbt-secrets. **These files _must_ be ignored by the VCS**. These files should be minimal and not include any non-secret data. In practice, most repos will have only secret file.
* `encryptSecretFiles` - encrypts the files specified by `secretFiles`, storing the results in files alongside the originals, with suffix `.encrypted`.
* `decryptSecretFiles` - decrypts `.encrypted` files, storing the results back in the original files specified by `secretFiles`.

## Backends

The API is implemented by plugins with different encryption backends.

### `KeybaseSecrets`

The `KeybaseSecrets` plugin is a simple wrapper around the **Keybase** CLI. To use, you must do the following:

1. Sign up for a Keybase account: https://keybase.io/
2. Sign in to Keybase via the CLI: https://keybase.io/docs/command_line
3. Add the `authorizedKeybaseUsernames` key to the build. This should be a `Seq` of the Keybase usernames who should have access to the secret files.
4. Add `enablePlugins(KeybaseSecrets)` to the build.

The plugin delegates to the `keybase pgp encrypt` and `keybase pgp decrypt` commands. Keybase is free and simple to use, although the secret files need to be re-encrypted whenever a user is added or removed from `authorizedKeybaseUsernames`.

### `KmsSecrets`

The `KmsSecrets` plugin leverages the **AWS Key Management Service** (KMS). In order to use this plugin, you must do the following:

1. Sign up for AWS and/or set up KMS: http://docs.aws.amazon.com/kms/latest/developerguide/overview.html
2. Create a new encryption key in IAM.
3. Sign in to AWS via the CLI: http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html
4. Generate a KMS data key via this command: `aws kms generate-data-key --key-id=YOUR_KEY_ID_HERE --key-spec=AES_256` and add the `CiphertextBlob` to `encrypted-kms-data-key.txt`: http://docs.aws.amazon.com/kms/latest/APIReference/API_GenerateDataKey.html
5. Add `enablePlugins(KmsSecrets)` to the build.

The plugin uses the specified data key to AES encrypt the secret files. The KMS console allows you to dynamically control who has access to the data key without making changes to the repository, making it a very scalable approach (if you don't mind paying to use KMS).

## Testing

There are two test projects `test-project-keybase` and `test-project-kms`, for testing the corresponding plugins. For each, first follow the associated setup instructions. Then test the `encryptSecretFiles` and `decryptSecretFiles` commands:

1. Run `encryptSecretFiles`. Ensure that `secret.sbt.encrypted` is generated and that it contains seemingly encrypted text.
2. Delete `secret.sbt` and run `decryptSecretFiles`. Ensure that `secret.sbt` is re-generated, and that it contains the same text as before.
3. Run `decryptSecretFiles` again and verify that the interactive prompt for handling the existing file works as expected.

For testing purposes, `secret.sbt` is checked into the VCS in each project, so it should be easy to verify that the contents did not change after encryption / decryption. **Under normal circumstances, `secret.sbt` should be ignored by the VCS**.

## Miscellanea

Initially implemented during an [Even Financial](https://github.com/EVENFinancial) hack-a-thon on 5 Feb, 2016.
