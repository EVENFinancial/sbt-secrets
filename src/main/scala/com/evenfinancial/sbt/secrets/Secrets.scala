package com.evenfinancial.sbt.secrets

import sbt._

object Secrets extends AutoPlugin {

  object autoImport {
    val secretFiles = settingKey[Seq[File]]("Files containing secrets that cannot be commited unencrypted to SCM.")
    val interactiveOverwrite = settingKey[Boolean]("Whether to prompt when overwriting existing files.")
    val aesKeySize = settingKey[Int]("The AES key-size to use for encryption and decryption. Defaults to 128. 256 bits requires the Java Cryptography Extension.")
    val encryptSecretFiles = taskKey[Seq[File]]("Encrypt all files specified by `secretFiles`.")
    val decryptSecretFiles = taskKey[Seq[File]]("Decrypt all encrypted files corresponding to those specified by `secretFiles`.")
  }

  import autoImport._

  override def projectSettings = Seq(
    secretFiles := Seq.empty,
    interactiveOverwrite := true,
    aesKeySize := 128
  )
}
