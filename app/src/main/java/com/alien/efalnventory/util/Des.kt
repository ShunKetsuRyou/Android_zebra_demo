package com.alien.efaInventory.interfaces

import com.alien.efaInventory.util.Base64
import java.lang.Exception
import javax.crypto.Cipher.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object DES {
    @Throws(Exception::class)
    fun encryptDES(encryptString: String, encryptKey: String): String {
        val zeroIv = IvParameterSpec("81439001".toByteArray())
        val key = SecretKeySpec(encryptKey.toByteArray(), "DES")
        val cipher = getInstance("DES/CBC/PKCS5Padding")
        cipher.init(ENCRYPT_MODE, key, zeroIv)
        val encryptedData = cipher.doFinal(encryptString.toByteArray())
        return Base64.encode(encryptedData)
    }

    @Throws(Exception::class)
    fun decryptDES(decryptString: String?, decryptKey: String): String {
        val byteMi: ByteArray = Base64.decode(decryptString.toString())
        val zeroIv = IvParameterSpec("81439001".toByteArray())
        //      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        val key = SecretKeySpec(decryptKey.toByteArray(), "DES")
        val cipher = getInstance("DES/CBC/PKCS5Padding")
        cipher.init(DECRYPT_MODE, key, zeroIv)
        val decryptedData = cipher.doFinal(byteMi)
        return String(decryptedData)
    }
}
