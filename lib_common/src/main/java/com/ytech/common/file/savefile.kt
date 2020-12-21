package com.ytech.common.file

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.util.*

import io.reactivex.Observable
import java.io.IOException


fun File.saveGifToAlbum(context: Context, gifFile: File): Observable<Boolean> {
    return Observable.create { emitter ->
        gifFile.createOrExistsFile()
        val result = copyOrMoveFile(gifFile)
        if (result) {
            // MediaStore.Images.Media.insertImage(context.contentResolver, gifFile.absolutePath, gifFile.name, "")
            MediaScannerConnection.scanFile(
                context, arrayOf(gifFile.absolutePath),
                arrayOf("image/gif")
            ) { _, _ ->
                emitter.onNext(true)
                emitter.onComplete()
            }
        } else {
            emitter.onNext(false)
            emitter.onComplete()
        }
    }
}

fun File.saveVideoToAlbum(context: Context, videoFile: File): Observable<Boolean> {
    return Observable.create { emitter ->
        videoFile.createOrExistsFile()
        val result = copyOrMoveFile(videoFile)
        if (result) {
            MediaScannerConnection.scanFile(
                context, arrayOf(videoFile.absolutePath),
                arrayOf("video/mp4")
            ) { _, _ ->
                emitter.onNext(true)
                emitter.onComplete()
            }
        } else {
            emitter.onNext(false)
            emitter.onComplete()
        }
    }
}

fun Bitmap.saveToAlbum(
    context: Context,
    target: File,
    quality: Int = 100
): Observable<Boolean> {
    return Observable.create<Boolean> { emitter ->
        target.createOrExistsFile()
        compress(Bitmap.CompressFormat.PNG, quality, FileOutputStream(target))
        // MediaStore.Images.Media.insertImage(context.contentResolver, target.absolutePath, target.name, "")
        MediaScannerConnection.scanFile(
            context, arrayOf(target.absolutePath),
            arrayOf("image/png")
        ) { _, _ ->
            emitter.onNext(true)
            emitter.onComplete()
        }
    }
}


fun Context.getThumbnailFileSync(videoPath: String): String {
    val file = File(videoPath)
    val thumbPath = if (file.exists()) {
        val bitmap =
            ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND)
        val thumbFile = File.createTempFile("video_thumb", ".jpg", externalCacheDir)
        bitmap2File(bitmap, thumbFile)
    } else null
    return thumbPath ?: ""
}

/**
 * Bitmap保存成File
 *
 * @param bitmap input bitmap
 * @param f      output file's name
 * @return String output file's path
 */
private fun bitmap2File(bitmap: Bitmap?, f: File?): String? {
    if (bitmap == null || f == null) return null
    if (f.exists()) f.delete()
    val fOut: FileOutputStream?
    try {
        fOut = FileOutputStream(f)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        fOut.flush()
        fOut.close()
    } catch (e: IOException) {
        return null
    }

    return f.absolutePath
}

fun getVideoSize(videoPath: String): IntArray {
    var retriever: MediaMetadataRetriever? = null
    val videoWidth: Int
    val videoHeight: Int
    try {
        retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)
        videoWidth =
            Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))//获取视频的宽度
        videoHeight =
            Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))//获取视频的高度
        val videoRotation =
            Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION))
        return if (videoRotation == 90 || videoRotation == 270) {
            intArrayOf(videoHeight, videoWidth)
        } else {
            intArrayOf(videoWidth, videoHeight)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return intArrayOf(0, 0)
    } finally {
        retriever?.release()
    }
}