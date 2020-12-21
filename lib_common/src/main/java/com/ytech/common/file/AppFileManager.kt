package com.ytech.common.file

import android.os.Build
import android.os.Environment

import com.ytech.common.text.isNotEmptyStr

import java.io.File

object AppFileManager {
    val DIR = "/storage/emulated/0"
    /**
     * 音频缓存的目录名
     */
    val CACHE_AUDIO_UNIQUE_NAME = "cache/audio"
    /**
     * 文件缓存的目录名
     */
    val CACHE_FILE_UNIQUE_NAME = "cache/file"
    /**
     * 图片缓存的目录名
     */
    val CACHE_IMAGE_UNIQUE_NAME = "cache/image"
    /**
     * 视频缓存的目录名
     */
    val CACHE_VIDEO_UNIQUE_NAME = "cache/video"
    /**
     * 视频缓存的目录名
     */
    val CACHE_GIF_UNIQUE_NAME = "cache/gif"

    val DOWNLOAD_PICTURE_UNIQUE_NAME = "download/picture"

    fun getImageDir(): File {
        val file = File(DIR + File.separator + "com.haohaoyang/" + CACHE_IMAGE_UNIQUE_NAME)
        file.createOrExistsDir()
        return file
    }

    fun getImage(name: String): File {
        return File(getImageDir(), name)
    }

    fun getGifDir(): File {
        val file = File(DIR + File.separator + "com.haohaoyang/" + CACHE_GIF_UNIQUE_NAME)
        file.createOrExistsDir()
        return file
    }

    fun getGif(name: String): File = File(getGifDir(), name)

    fun getVideoDir(): File {
        val file = File(DIR + File.separator + "com.haohaoyang/" + CACHE_VIDEO_UNIQUE_NAME)
        file.createOrExistsDir()
        return file
    }

    fun getVideo(name: String) = File(getVideoDir(), name)

    fun getDownloadPictureDir(): File {
        val file = File(DIR + File.separator + "com.haohaoyang/" + DOWNLOAD_PICTURE_UNIQUE_NAME)
        file.createOrExistsDir()
        return file
    }

    fun getDownloadPicture(name: String): File = File(getDownloadPictureDir(), name)

    fun getDCIMDir(): File {
        val brand = Build.BRAND
        val dir = Environment.getExternalStorageDirectory().absolutePath
        return File(
            when {
                brand == "xiaomi" -> if (dir.isNotEmptyStr()) "${dir}/DCIM/Camera" else "/storage/emulated/0/DCIM/Camera"
                brand.equals("Huawei", ignoreCase = true) ->
                    if (dir.isNotEmptyStr()) "${dir}/DCIM/Camera" else "/storage/emulated/0/DCIM/Camera"
                else -> if (dir.isNotEmptyStr()) "${dir}/DCIM" else "/storage/emulated/0/DCIM"
            }
        )
    }

    fun getDCIMFile(name: String): File = File(getDCIMDir(), name)

    fun clearImageCache() {
        getImageDir().deleteDir()
    }

    fun clearGifCache() {
        getGifDir().deleteDir()
    }

    fun clearVideoCache() {
        getVideoDir().deleteDir()
    }

    fun clearCache() {
        clearImageCache()
        clearVideoCache()
        clearGifCache()
    }
}