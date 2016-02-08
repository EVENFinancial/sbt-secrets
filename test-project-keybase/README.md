# test-project-keybase

This is a project for testing the `KeybaseSecrets` plugin. To do any testing, first follow the setup instructions in the top-level README. Then test the `encryptSecretFiles` and `decryptSecretFiles` commands:

1. Run `encryptSecretFiles`. Ensure that `secret.sbt.encrypted` is generated and that it contains seemingly encrypted text.
2. Delete `secret.sbt` and run `decryptSecretFiles`. Ensure that `secret.sbt` is re-generated, and that it contains the same text as before.
3. Run `decryptSecretFiles` again and verify that the interactive prompt for handling the existing file works as expected.

For testing purposes, `secret.sbt` is checked into the VCS, so it should be easy to verify that the contents did not change after encryption / decryption. **Under normal circumstances, `secret.sbt` should be ignored by the VCS**.
