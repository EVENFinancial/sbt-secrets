name := "test-project-kms"

enablePlugins(KeybaseSecrets)

secretFiles := Seq(file("secret.sbt"))
