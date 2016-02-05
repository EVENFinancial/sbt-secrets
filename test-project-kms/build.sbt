name := "test-project-kms"

enablePlugins(KmsSecrets)

secretFiles := Seq(file("secret.sbt"))
