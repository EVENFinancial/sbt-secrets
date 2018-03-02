package com.evenfinancial.sbt.secrets

import sbt._
import com.evenfinancial.sbt.secrets.util._

object KmsSecrets extends AutoPlugin {

  override def requires: Secrets.type = Secrets

  object autoImport {

    val encryptedKmsDataKeyFile = settingKey[File]("The file containing the encrypted KMS data key.")

    val kmsDataKey = taskKey[String]("The data key used to encrypt / decrypt files.")

    val kmsKeyId = taskKey[Option[String]]("A unique identifier for the customer master key (CMK).")
  }

  import Secrets.autoImport._
  import autoImport._

  override def projectSettings = Seq(

    encryptedKmsDataKeyFile := file("./encrypted-kms-data-key.txt"),

    kmsDataKey := KmsUtil.decryptDataKey(
      encryptedDataKey = IO.read(encryptedKmsDataKeyFile.value),
      kmsKeyId = kmsKeyId.value),

    kmsKeyId := None,

    encryptSecretFiles := {
      val dataKey = kmsDataKey.value
      val secretKeySpec = AesUtil.buildSecretKey(dataKey, aesKeySize.value)

      secretFiles.value.map { file =>
        val encryptedData = AesUtil.encrypt(IO.read(file), secretKeySpec)
        val _encryptedFile = SbtUtil.fileWithSuffix(file, ".encrypted")
        IO.write(_encryptedFile, encryptedData)
        _encryptedFile
      }
    },

    decryptSecretFiles := {
      val dataKey = kmsDataKey.value
      val secretKeySpec = AesUtil.buildSecretKey(dataKey, aesKeySize.value)

      secretFiles.value.map { file =>
        SbtUtil.promptForOverwrite(file, interactiveOverwrite.value) {
          val _encryptedFile = SbtUtil.fileWithSuffix(file, ".encrypted")
          val decryptedData = AesUtil.decrypt(IO.read(_encryptedFile), secretKeySpec)
          IO.write(file, decryptedData)
        }

        file
      }
    }

  )

}
