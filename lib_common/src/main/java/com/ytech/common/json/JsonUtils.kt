package com.ytech.util.json

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.ytech.common.log.L
import java.lang.reflect.Type
import kotlin.Exception

object JsonUtils {
    private val GSON = GsonBuilder().create()
    private val PARSER = JsonParser()

    fun toJson(obj: Any): String? {
        try {
            return GSON.toJson(obj)
        } catch (e: Exception) {
            L.e("实体类转json错误 msg = ${e.message}")
        }
        return null
    }

    fun <T,V> mapToJson(map: Map<T,V>) : String {
        return GSON.toJson(map)
    }

    fun <T : Any> fromJson(json: String, clazz: Class<T>): T? {
        try {
            return GSON.fromJson(json, clazz)
        } catch (e: Exception) {
            L.e("json转实体类错误 msg = ${e.message}")
        }
        return null
    }

    fun <T : Any> fromJson(json: String, type: Type): T? {
        try {
            return GSON.fromJson(json, type)
        } catch (e: Exception) {
            L.e("json转实体类错误 msg = ${e.message}")
        }
        return null
    }

    fun <T : Any> fromJson(jsonReader: JsonReader, type: Type): T? {
        try {
            return GSON.fromJson(jsonReader, type)
        } catch (e: Exception) {
            L.e("json转实体类错误 msg = ${e.message}")
        }
        return null
    }

    fun <T : Any> fromJson(jsonReader: JsonReader, clazz: Class<T>): T? {
        try {
            return GSON.fromJson(jsonReader, clazz)
        } catch (e: Exception) {
            L.e("json转实体类错误 msg = ${e.message}")
        }
        return null
    }

    fun getString(json: String, key: String): String? {
        return try {
            getElementByKey(json, key)?.asString
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getString(jsonReader: JsonReader, key: String): String? {
        return getElementByKey(jsonReader, key)?.asString
    }

    fun getInt(json: String, key: String): Int? {
        return getElementByKey(json, key)?.asInt
    }

    fun getInt(jsonReader: JsonReader, key: String): Int? {
        return getElementByKey(jsonReader, key)?.asInt
    }

    fun getLong(json: String, key: String): Long? {
        return getElementByKey(json, key)?.asLong
    }

    fun getLong(jsonReader: JsonReader, key: String): Long? {
        return getElementByKey(jsonReader, key)?.asLong
    }

    fun getJsonArray(json: String, key: String): JsonArray? {
        return getElementByKey(json, key)?.asJsonArray
    }

    private fun getElementByKey(json: String, key: String): JsonElement? {
        return try {
            PARSER.parse(json)
            val jsonObject = PARSER.parse(json).asJsonObject
            if (jsonObject.has(key)) jsonObject.get(key) else null
        } catch (e: Exception) {
            L.e("获取json字段失败 error msg = ${e.message}")
            null
        }
    }

    private fun getElementByKey(jsonReader: JsonReader, key: String): JsonElement? {
        return try {
            val jsonObject = PARSER.parse(jsonReader).asJsonObject
            if (jsonObject.has(key)) jsonObject.get(key) else null
        } catch (e: Exception) {
            L.e("获取json字段失败 error msg = ${e.message}")
            null
        }
    }
}