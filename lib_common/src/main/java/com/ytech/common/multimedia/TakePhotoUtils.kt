package com.ytech.common.multimedia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File


object TakePhotoUtils {
    private var mCurrentPath = ""

    fun getTakePhotoPath() = mCurrentPath

    fun startTakePhoto(fragment: Fragment) {
        val context = fragment.context ?: return
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(context.packageManager) != null) {
            val cameraFile = File(context.getExternalFilesDir(null)!!.absolutePath + File.separator + "yuya_teacher.jpg")
            mCurrentPath = cameraFile.absolutePath
            val imageUri = generateUri(context, cameraFile)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            fragment.startActivityForResult(cameraIntent, RequestCode.TAKE_PHOTO)
        }
    }

    private fun generateUri(context: Context, cameraFile: File): Uri {
        val imageUri: Uri
        val authority: String = context.packageName.toString() + ".provider"
        imageUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //通过FileProvider创建一个content类型的Uri
                FileProvider.getUriForFile(context, authority, cameraFile)
            } else {
                Uri.fromFile(cameraFile)
            }
        return imageUri
    }

    object RequestCode {
        const val SETTING_PWD = 0
        const val PUBLISH_TASK = 1
        const val PHOTO_PICKER = 2
        const val VIDEO_PICKER = 3
        const val IMAGE_CROP = 4
        const val LOOK_HEAD = 5
        const val TAKE_PHOTO = 6
        const val ADD_GROWTH_HANDBOOK = 7
    }
}