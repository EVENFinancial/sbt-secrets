sbtPlugin := true

scalaVersion := "2.10.4"

organization := "com.evenfinancial"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-kms" % "1.10.22",
  "org.scalatest" %% "scalatest" % "2.2.5" % Test
)
