package com.evenfinancial.sbt.secrets

import java.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}

// Basically copied from the Play framework's Crypto library.
// @see https://github.com/playframework/playframework/blob/2.4.x/framework/src/play/src/main/scala/play/api/libs/Crypto.scala
object AesUtil {

  def encrypt(data: String, dataKey: String): String = {
    val secretKey = buildSecretKey(dataKey)
    val cipher = buildCipher()
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encrypted = cipher.doFinal(data.getBytes("UTF-8"))
    val result = cipher.getIV() ++ encrypted
    Base64.getEncoder.encodeToString(result)
  }

  def decrypt(data: String, dataKey: String): String = {
    val secretKey = buildSecretKey(dataKey)
    val bytes = Base64.getDecoder.decode(data)
    val cipher = buildCipher()
    val iv = bytes.slice(0, cipher.getBlockSize)
    val payload = bytes.slice(cipher.getBlockSize, bytes.size)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv))
    new String(cipher.doFinal(payload), "utf-8")
  }

  private def buildCipher() = Cipher.getInstance("AES/CTR/NoPadding")

  private def buildSecretKey(dataKey: String) = {
    val algorithm = "AES"
    val messageDigest = MessageDigest.getInstance("SHA-256")
    messageDigest.update(dataKey.getBytes("utf-8"))
    // max allowed length in bits / (8 bits to a byte)
    val maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength(algorithm) / 8
    val raw = messageDigest.digest().slice(0, maxAllowedKeyLength)
    new SecretKeySpec(raw, algorithm)
  }

}
