package com.poi.union.models
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream

class Constantes {
    companion object {
        const val KEY_COLLECTION_USERS:String = "users"


        const val KEY_NAME  = "nombre"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
        const val KEY_ROL = "rol"
        const val KEY_CARRERA = "carrera"
        const val KEY_IMAGE = "image"




        @RequiresApi(Build.VERSION_CODES.O)
        fun decodeImage(encodedImage:String): Bitmap {

            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)

            return BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            )
        }

        fun encodeImage(bitmap: Bitmap): String{
            val previewWidth = 150
            val previewHeight = bitmap.height * previewWidth / bitmap.width
            val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
            val byteArrayOutputStream = ByteArrayOutputStream()
            previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }
}