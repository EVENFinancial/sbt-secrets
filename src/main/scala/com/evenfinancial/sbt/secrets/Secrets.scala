package com.evenfinancial.sbt.secrets

import sbt._

object Secrets extends AutoPlugin {

  object autoImport {

    val secretFiles = settingKey[Seq[File]](
      "Files containing secrets that cannot be commited unencrypted to SCM."
    )

    val encryptSecretFiles = taskKey[Seq[File]](
      "Encrypt all files specified by `secretFiles`."
    )

    val decryptSecretFiles = taskKey[Seq[File]](
      "Decrypt all encrypted files corresponding to those specified by `secretFiles`."
    )

  }

  import autoImport._

  override def projectSettings = Seq(

    secretFiles := Seq.empty

  )

}
