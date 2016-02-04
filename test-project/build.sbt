name := "test-project"

enablePlugins(KmsSecrets)

secretFiles := Seq(file("secret.sbt"))
