lazy val secretsPlugin = file("..").getAbsoluteFile.toURI

lazy val root = Project("test-project", file(".")).dependsOn(secretsPlugin)
