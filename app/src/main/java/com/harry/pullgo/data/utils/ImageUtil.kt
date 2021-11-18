package com.harry.pullgo.data.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImageUtil {
    companion object{
        fun BitmapToString(bitmap: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
            val bytes = baos.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }
}