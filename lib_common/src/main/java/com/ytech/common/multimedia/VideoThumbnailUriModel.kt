package com.ytech.common.multimedia

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.text.TextUtils
import me.panpf.sketch.uri.AbsBitmapDiskCacheUriModel
import me.panpf.sketch.util.SketchUtils

class VideoThumbnailUriModel : AbsBitmapDiskCacheUriModel() {
    companion object {
        const val SCHEME = "video.thumbnail://"

        fun makeUri(filePath: String) = SCHEME + filePath
    }

    override fun getContent(context: Context, uri: String): Bitmap {
        return ThumbnailUtils.createVideoThumbnail(
            getUriContent(uri),
            MediaStore.Images.Thumbnails.MINI_KIND
        )!!
    }

    override fun match(uri: String): Boolean {
        return !TextUtils.isEmpty(uri) && uri.startsWith(SCHEME)
    }

    /**
     * 获取 uri 所真正包含的内容部分，例如 "video.thumbnail:///sdcard/test.mp4"，就会返回 "/sdcard/test.mp4"
     *
     * @param uri 图片 uri
     * @return uri 所真正包含的内容部分，例如 "video.thumbnail:///sdcard/test.mp4"，就会返回 "/sdcard/test.mp4"
     */
    override fun getUriContent(uri: String): String {
        return if (match(uri)) uri.substring(SCHEME.length) else uri
    }

    override fun getDiskCacheKey(uri: String): String {
        return SketchUtils.createFileUriDiskCacheKey(uri, getUriContent(uri))
    }
}