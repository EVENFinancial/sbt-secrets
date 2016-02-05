# sbt-secrets

An SBT plugin for securely storing secrets in a VCS.

## Motivation

From a security standpoint, committing things like passwords or API keys to a VCS is a pretty terrible idea, regardless of whether the repo is public or private. On the other hand, it's really convenient to keep all of a project's data in one place. One resolution to this conflict is to encrypt the secret data before committing, and decrypt it after pulling new changes. The goal of **sbt-secrets** is to make that process easy.

## API

The generic API consists of three keys:

* `secretFiles` - a list of files that contain secrets, and thus need to be managed by sbt-secrets. **These files _must_ be ignored by the VCS**. These files should be minimal and not include any non-secret data. In practice, most repos will have only secret file.
* `encryptSecretFiles` - encrypts the files specified by `secretFiles`, storing the results in files alongside the originals, with suffix `.encrypted`.
* `decryptSecretFiles` - decrypts `.encrypted` files, storing the results back in the original files specified by `secretFiles`.

## Backends

The API is implemented by plugins with different encryption backends.

### `KmsSecrets`

The `KmsSecrets` plugin leverages the **AWS Key Management Service** (KMS). In order to use this plugin, you must do the following:

1. Sign up for AWS and/or set up KMS: http://docs.aws.amazon.com/kms/latest/developerguide/overview.html
2. Sign in to AWS via the CLI: http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html
3. Generate a KMS data key, and add the `CiphertextBlob` to `encrypted-kms-data-key.txt`: http://docs.aws.amazon.com/kms/latest/APIReference/API_GenerateDataKey.html
4. Add `enablePlugins(KmsSecrets)` to `build.sbt`

The plugin uses the specified data key to AES encrypt the secret files. The KMS console allows you to dynamically control who has access to the data key without making changes to the repository, making it a very scalable approach (if you don't mind paying to use KMS).

## Miscellanea

Initially implemented during an [Even Financial](https://github.com/EVENFinancial) hack-a-thon on 5 Feb, 2016.
