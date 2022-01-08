sbtPlugin := true

scalaVersion := "2.12.15"

organization := "com.evenfinancial"
name := "sbt-secrets"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-kms" % "1.12.136",
  "org.scalatest" %% "scalatest" % "3.2.10" % Test
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

publishTo := {
  if (isSnapshot.value)
    Some("EVEN Private Snapshots" at "s3://evenfinancial/maven/private/snapshots")
  else
    Some("EVEN Private Releases" at "s3://evenfinancial/maven/private/releases")
}
