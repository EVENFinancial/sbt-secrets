package com.evenfinancial.sbt.secrets

import sbt._

object KmsSecrets extends AutoPlugin {

  override def requires = Secrets

  object autoImport {

    val encryptedKmsDataKeyFile = settingKey[File](
      "The file containing the encrypted KMS data key."
    )

    val kmsDataKey = taskKey[String](
      "The data key used to encrypt / decrypt files."
    )

  }

  import Secrets.autoImport._
  import autoImport._

  override def projectSettings = Seq(

    encryptedKmsDataKeyFile := file("./encrypted-kms-data-key.txt"),

    kmsDataKey := KmsUtil.decryptDataKey(IO.read(encryptedKmsDataKeyFile.value)),

    encryptSecretFiles := {
      val dataKey = kmsDataKey.value

      secretFiles.value.map { file =>
        val encryptedData = AesUtil.encrypt(IO.read(file), dataKey)
        val _encryptedFile = Secrets.encryptedFile(file)
        IO.write(_encryptedFile, encryptedData)
        _encryptedFile
      }
    },

    decryptSecretFiles := {
      val dataKey = kmsDataKey.value

      secretFiles.value.map { file =>
        if (SbtUtil.canOverwriteFile(file)) {
          val _encryptedFile = Secrets.encryptedFile(file)
          val decryptedData = AesUtil.decrypt(IO.read(_encryptedFile), dataKey)
          IO.write(file, decryptedData)
        }

        file
      }
    }

  )

}
