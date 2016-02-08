package com.evenfinancial.sbt.secrets.util

import org.scalatest.{WordSpec, MustMatchers}

class AesUtilSpec
  extends WordSpec
  with MustMatchers
{

  "AesUtil" must {

    "encrypt and then decrypt transparently" in {
      val data = "data"
      val key = "key"
      val encryptedData = AesUtil.encrypt(data, key)
      encryptedData must not be data
      val decryptedData = AesUtil.decrypt(encryptedData, key)
      decryptedData mustBe data
    }

    "must not decrypt data correctly if the key is incorrect" in {
      val data = "data"
      val encryptedData = AesUtil.encrypt(data, "key")
      val decryptedData = AesUtil.decrypt(encryptedData, "other key")
      decryptedData must not be data
    }

  }

}
