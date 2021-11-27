sbtPlugin := true

scalaVersion := "2.12.4"

organization := "com.evenfinancial"
name := "sbt-secrets"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-kms" % "1.11.285",
  "org.scalatest" %% "scalatest" % "3.0.9" % Test
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayRepository := "sbt-plugins"
bintrayOrganization := Some("evenfinancial")
publishMavenStyle := false
