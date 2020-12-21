package com.ytech.common.multimedia

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import com.bigkoo.convenientbanner.utils.ScreenUtil.getScreenHeight
import com.bigkoo.convenientbanner.utils.ScreenUtil.getScreenWidth
import com.ytech.common.common.closeQuietly
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object CropUtils {
    fun decodeRegionCrop(context: Context, source: Uri, sourceBitmap: Bitmap, regionRect: Rect): Bitmap? {
        var croppedImage: Bitmap? = null
        var decoder: BitmapRegionDecoder? = null
        var inputStream: InputStream? = null

        try {
            inputStream = context.contentResolver.openInputStream(source)
            decoder = BitmapRegionDecoder.newInstance(inputStream, false)

            val exifRotation = getExifRotation(source.path ?: "")
            val width = decoder.width
            val height = decoder.height

            val newLeft: Float
            val newTop: Float
            val newRight: Float
            val newBottom: Float
            when (exifRotation) {
                90, 270 -> {
                    newLeft = (regionRect.top * 1.0f / sourceBitmap.height) * width
                    newTop = (1 - regionRect.right * 1.0f / sourceBitmap.width) * height
                    newRight = newLeft + regionRect.height() * 1.0f / sourceBitmap.height * width
                    newBottom = newTop + regionRect.width() * 1.0f / sourceBitmap.width * height
                }
                else -> {
                    newLeft = (regionRect.left * 1.0f / sourceBitmap.width) * width
                    newTop = (regionRect.top * 1.0f / sourceBitmap.height) * height
                    newRight = newLeft + regionRect.width() * 1.0f / sourceBitmap.width * width
                    newBottom = newTop + regionRect.height() * 1.0f / sourceBitmap.height * height
                }
            }

            val newRegionRect = Rect(newLeft.toInt(), newTop.toInt(), newRight.toInt(), newBottom.toInt())

            val maxSize = getMaxImageSize(context)
            var sampleSize = 1
            while (newRegionRect.width() / sampleSize > maxSize || newRegionRect.height() / sampleSize > maxSize) {
                sampleSize = sampleSize shl 1
            }
            val options = BitmapFactory.Options()
            options.inSampleSize = sampleSize
            croppedImage = decoder?.decodeRegion(newRegionRect, options)

            if (exifRotation != 0) {
                val matrix = Matrix()
                matrix.postRotate(exifRotation.toFloat())
                croppedImage = croppedImage?.let { bm ->
                    Bitmap.createBitmap(
                        bm, 0, 0, bm.width, bm.height, matrix, true
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.closeQuietly()
            decoder?.recycle()
        }
        return croppedImage
    }

    fun decodeRegionCrop(context: Context, resId: Int, destWidth: Int, destHeight: Int): Bitmap? {
        var croppedImage: Bitmap? = null
        var decoder: BitmapRegionDecoder? = null
        var inputStream: InputStream? = null
        try {
            val options = BitmapFactory.Options()
            // options.inTargetDensity = TypedValue().density
            // options.inScaled = false
            options.inPreferredConfig = Bitmap.Config.ARGB_4444

            inputStream = context.resources.openRawResource(resId)
            decoder = BitmapRegionDecoder.newInstance(inputStream, false)
            val width = decoder.width
            croppedImage =
                decoder?.decodeRegion(Rect(0, 0, width, (destHeight * 1f / destWidth * width).toInt()), options)
            val matrix = Matrix()
            val scale = destWidth * 1f / width
            matrix.postScale(scale, scale)
            croppedImage = croppedImage?.let { bitmap ->
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.closeQuietly()
            decoder?.recycle()
        }
        return croppedImage
    }

    private fun getMaxImageSize(context: Context) = Math.sqrt(
        Math.pow(getScreenWidth(context).toDouble(), 2.0) +
                Math.pow(getScreenHeight(context).toDouble(), 2.0)
    )

    fun saveOutput(context: Context, saveUri: Uri?, croppedImage: Bitmap, quality: Int): Boolean {
        if (saveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = context.contentResolver.openOutputStream(saveUri)
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                }
            } catch (e: FileNotFoundException) {
                return false
            } finally {
                outputStream.closeQuietly()
            }
            return true
        }
        return false
    }

    fun getExifRotation(imagePath: String): Int {
        return try {
            val exif = ExifInterface(imagePath)
            // We only recognize a subset of orientation tag values
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> ExifInterface.ORIENTATION_UNDEFINED
            }
        } catch (e: IOException) {
            0
        }
    }

    fun getImageSize(imagePath: String): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        val width = options.outWidth
        val height = options.outHeight

        val exifRotation = getExifRotation(imagePath)
        return when (exifRotation) {
            90, 270 -> intArrayOf(height, width)
            else -> intArrayOf(width, height)
        }
    }
}