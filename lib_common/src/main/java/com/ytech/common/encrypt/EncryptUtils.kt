package com.ytech.common.encrypt

import android.annotation.SuppressLint
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class EncryptUtils private constructor() {
    private lateinit var key: String

    /**
     * 必须要16位密钥匙
     */
    fun key(key: String) {
        this.key = key
    }

    private object Holder {
        val INSTANCE = EncryptUtils()
    }

    companion object {
        fun getInstance(): EncryptUtils = Holder.INSTANCE
    }

    /**
     * AES128加密
     * @param plainText 明文
     * @return
     */
    @SuppressLint("GetInstance")
    fun encrypt(plainText: String): String {
        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val keyspec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keyspec)
            val encrypted = cipher.doFinal(plainText.toByteArray())
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * AES128解密
     * @param cipherText 密文
     * @return
     */
    @SuppressLint("GetInstance")
    fun decrypt(cipherText: String): String {
        return try {
            val encrypted1 = Base64.decode(cipherText, Base64.NO_WRAP)
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val original = cipher.doFinal(encrypted1)
            String(original)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun sha1(info: String): String {
        var digesta: ByteArray? = null
        try {
            // 得到一个SHA-1的消息摘要
            val alga = MessageDigest.getInstance("SHA-1")
            // 添加要进行计算摘要的信息
            alga.update(info.toByteArray())
            // 得到该摘要
            digesta = alga.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        // 将摘要转为字符串
        return byte2hex(digesta!!)
    }

    private fun byte2hex(byteArray: ByteArray): String {
        // 转成16进制后是32字节
        return with(StringBuilder()) {
            byteArray.forEach {
                val hex = it.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                if (hexStr.length == 1) {
                    append("0").append(hexStr)
                } else {
                    append(hexStr)
                }
            }
            toString()
        }
    }


}