package com.evenfinancial.sbt.secrets.util

import java.nio.ByteBuffer
import java.util.Base64
import com.amazonaws.services.kms.AWSKMSClient
import com.amazonaws.services.kms.model.DecryptRequest

object KmsUtil {

  lazy val client = new AWSKMSClient()

  def decryptDataKey(encryptedDataKey: String): String = {
    val byteBuffer = ByteBuffer.wrap(Base64.getDecoder.decode(encryptedDataKey.trim))
    val decryptRequest = new DecryptRequest().withCiphertextBlob(byteBuffer)
    val decryptResult = client.decrypt(decryptRequest)
    new String(decryptResult.getPlaintext.array, "utf-8")
  }

}
