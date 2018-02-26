package com.evenfinancial.sbt.secrets

import sbt._
import scala.sys.process._
import com.evenfinancial.sbt.secrets.util._

object KeybaseSecrets extends AutoPlugin {

  override def requires = Secrets

  object autoImport {

    val authorizedKeybaseUsernames = settingKey[Seq[String]](
      "The Keybase usernames for whom the secret files should be encrypted."
    )

  }

  import Secrets.autoImport._
  import autoImport._

  override def projectSettings = Seq(

    authorizedKeybaseUsernames := Seq.empty,

    encryptSecretFiles := {
      secretFiles.value.map { file =>
        val encryptedFile = SbtUtil.fileWithSuffix(file, ".encrypted")
        (Process.cat(file) #| (Seq("keybase", "pgp", "encrypt") ++ authorizedKeybaseUsernames.value) #> encryptedFile).!
        encryptedFile
      }
    },

    decryptSecretFiles := {
      secretFiles.value.map { file =>
        SbtUtil.promptForOverwrite(file) {
          val encryptedFile = SbtUtil.fileWithSuffix(file, ".encrypted")
          (Process.cat(encryptedFile) #| Seq("keybase", "pgp", "decrypt") #> file).!
        }

        file
      }
    }

  )

}
