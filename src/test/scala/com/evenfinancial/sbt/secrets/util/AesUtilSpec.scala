package com.evenfinancial.sbt.secrets.util

import org.scalatest.{WordSpec, MustMatchers}

class AesUtilSpec extends WordSpec with MustMatchers
{
  "AesUtil" must {
    "encrypt and then decrypt transparently" in {
      val data = "data"
      val key = "key"
      val secretKeySpec = AesUtil.buildSecretKey(key, 128)
      val encryptedData = AesUtil.encrypt(data, secretKeySpec)
      encryptedData must not be data
      val decryptedData = AesUtil.decrypt(encryptedData, secretKeySpec)
      decryptedData mustBe data
    }

    "must not decrypt data correctly if the key is incorrect" in {
      val data = "data"
      val secretKeySpec = AesUtil.buildSecretKey(data, 128)
      val encryptedData = AesUtil.encrypt(data, secretKeySpec)
      val anotherSecretKeySpec = AesUtil.buildSecretKey("data2", 128)
      val decryptedData = AesUtil.decrypt(encryptedData, anotherSecretKeySpec)
      decryptedData must not be data
    }
  }
}
