package com.evenfinancial.sbt

import sbt._

object KmsSecrets extends AutoPlugin {

  override def requires = Secrets

  import Secrets.autoImport._

  override def projectSettings = Seq(

    encryptSecretFiles := {
      secretFiles.value.map { file =>
        val encryptedData = IO.read(file)
        val encryptedFile = file.getParentFile / (file.getName + ".encrypted")
        IO.write(encryptedFile, encryptedData)
        encryptedFile
      }
    },

    decryptSecretFiles := {
      secretFiles.value.map { file =>
        val encryptedFile = file.getParentFile / (file.getName + ".encrypted")
        val decryptedData = IO.read(encryptedFile)
        IO.write(file, decryptedData)
        file
      }
    }

  )

}
