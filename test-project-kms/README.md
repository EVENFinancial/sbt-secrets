# test-project-kms

This is a project for testing the `KmsSecrets` plugin. To do any testing,
you'll need to do the following:

* Make sure that your credentials are configure in the AWS CLI: http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html
* Set up KMS in AWS: http://docs.aws.amazon.com/kms/latest/developerguide/overview.html.
* Generate a KMS data key, and add the `CiphertextBlob` to
`encrypted-kms-data-key.txt`: http://docs.aws.amazon.com/kms/latest/APIReference/API_GenerateDataKey.html

Then test the `encryptSecretFiles` and `decryptSecretFiles` commands:

1. Run `encryptSecretFiles`. Ensure that `secret.sbt.encrypted` is generated
and that it contains seemingly encrypted text.
2. Delete `secret.sbt` and run `decryptSecretFiles`. Ensure that `secret.sbt`
is re-generated, and that it contains the same text as before.

For testing purposes, `secret.sbt` is checked into the VCS, so it should be
easy to verify that the contents did not change after encryption / decryption.
Under normal circumstances, `secret.sbt` should be ignored by the VCS.
