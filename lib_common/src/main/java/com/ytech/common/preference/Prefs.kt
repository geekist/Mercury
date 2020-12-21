package com.ytech.common.preference

import android.content.SharedPreferences
import com.ytech.common.encrypt.EncryptUtils
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private inline fun <T> SharedPreferences.delegate(
    key: String? = null,
    defaultValue: T,
    crossinline getter: SharedPreferences.(String, T) -> T,
    crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue)!!

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            edit().setter(key ?: property.name, value).apply()
    }


fun SharedPreferences.string(
    key: String? = null,
    defValue: String = "",
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, String> {
    return if (isEncrypt) {

        delegate(
            key,
            defValue,
            SharedPreferences::getEncryptString,
            SharedPreferences.Editor::putEncryptString
        )
    } else {

        delegate(
            key,
            defValue,
            SharedPreferences::getInnerString,
            SharedPreferences.Editor::putString
        )
    }
}

fun SharedPreferences.int(
    key: String? = null,
    defValue: Int = 0,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Int> {
    return if (isEncrypt) {

        delegate(
            key,
            defValue,
            SharedPreferences::getEncryptInt,
            SharedPreferences.Editor::putEncryptInt
        )
    } else {

        delegate(key, defValue, SharedPreferences::getInt, SharedPreferences.Editor::putInt)
    }
}

fun SharedPreferences.long(
    key: String? = null,
    defValue: Long = 0,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Long> {
    return if (isEncrypt) {

        delegate(
            key,
            defValue,
            SharedPreferences::getEncryptLong,
            SharedPreferences.Editor::putEncryptLong
        )
    } else {

        delegate(key, defValue, SharedPreferences::getLong, SharedPreferences.Editor::putLong)
    }
}

fun SharedPreferences.float(
    key: String? = null,
    defValue: Float = 0f,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Float> {
    return if (isEncrypt) {

        delegate(
            key,
            defValue,
            SharedPreferences::getEncryptFloat,
            SharedPreferences.Editor::putEncryptFloat
        )
    } else {

        delegate(key, defValue, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)
    }
}

fun SharedPreferences.boolean(
    key: String? = null,
    defValue: Boolean = false,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Boolean> {
    return if (isEncrypt) {

        delegate(
            key,
            defValue,
            SharedPreferences::getEncryptBoolean,
            SharedPreferences.Editor::putEncryptBoolean
        )
    } else {

        delegate(key, defValue, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)
    }
}

fun SharedPreferences.stringSet(
    key: String? = null,
    defValue: Set<String> = emptySet(),
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Set<String>> {
    return if (isEncrypt) {

        delegate(
            key,
            defValue,
            SharedPreferences::getEncryptStringSet,
            SharedPreferences.Editor::putEncryptStringSet
        )
    } else {

        delegate(
            key, defValue,
            SharedPreferences::getInnerStringSet,
            SharedPreferences.Editor::putStringSet
        )
    }
}


fun SharedPreferences.initKey(key: String) = EncryptUtils.getInstance().key(key)


fun SharedPreferences.getEncryptInt(key: String, defValue: Int): Int {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return Integer.parseInt(decryptPreference(encryptValue))
}

fun SharedPreferences.Editor.putEncryptInt(key: String, value: Int): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(Integer.toString(value)))
    return this
}


fun SharedPreferences.getEncryptLong(key: String, defValue: Long): Long {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return java.lang.Long.parseLong(decryptPreference(encryptValue))
}

fun SharedPreferences.Editor.putEncryptLong(key: String, value: Long): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(java.lang.Long.toString(value)))
    return this
}


fun SharedPreferences.getEncryptFloat(key: String, defValue: Float): Float {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return java.lang.Float.parseFloat(decryptPreference(encryptValue))
}

fun SharedPreferences.Editor.putEncryptFloat(key: String, value: Float): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(java.lang.Float.toString(value)))
    return this
}


fun SharedPreferences.getEncryptBoolean(key: String, defValue: Boolean): Boolean {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return java.lang.Boolean.parseBoolean(decryptPreference(encryptValue))
}

fun SharedPreferences.Editor.putEncryptBoolean(
    key: String,
    value: Boolean
): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(java.lang.Boolean.toString(value)))
    return this
}


fun SharedPreferences.getEncryptString(key: String, defValue: String?): String {
    val encryptValue = this.getString(encryptPreference(key), null)
    return if (encryptValue == null) defValue ?: "" else decryptPreference(encryptValue)
}

fun SharedPreferences.getInnerString(key: String, defValue: String?) =
    this.getString(key, defValue) ?: ""

fun SharedPreferences.Editor.putEncryptString(
    key: String,
    value: String
): SharedPreferences.Editor {
    this.putString(encryptPreference(key), encryptPreference(value))
    return this
}


fun SharedPreferences.getEncryptStringSet(key: String, defValues: Set<String>): Set<String> {
    val encryptSet = this.getStringSet(encryptPreference(key), null)
        ?: return defValues
    val decryptSet = HashSet<String>()
    for (encryptValue in encryptSet) {
        decryptSet.add(decryptPreference(encryptValue))
    }
    return decryptSet
}

fun SharedPreferences.getInnerStringSet(key: String, defValues: Set<String>) =
    this.getStringSet(key, defValues) ?: emptySet()

fun SharedPreferences.Editor.putEncryptStringSet(
    key: String,
    values: Set<String>
): SharedPreferences.Editor {
    val encryptSet = HashSet<String>()
    for (value in values) {
        encryptSet.add(encryptPreference(value))
    }
    this.putStringSet(encryptPreference(key), encryptSet)
    return this
}

/**
 * encrypt function
 * @return cipherText base64
 */
private fun encryptPreference(plainText: String): String {

    return EncryptUtils.getInstance().encrypt(plainText)
}

/**
 * decrypt function
 * @return plainText
 */
private fun decryptPreference(cipherText: String): String {
    return EncryptUtils.getInstance().decrypt(cipherText)
}