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
        const val KEY_COLLECTION_GROUPS:String = "groups"
        const val KEY_COLLECTION_GROUP_MEMBERS:String = "members"
        const val KEY_COLLECTION_GROUP_TASKS:String = "groupTasks"
        const val KEY_COLLECTION_GROUP_NFTS:String = "groupNFTs"
        const val KEY_COLLECTION_BOUGHT_NFTS:String = "boughtNFTs"

        const val KEY_NAME  = "nombre"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
        const val KEY_ROL = "rol"
        const val KEY_CARRERA = "carrera"
        const val KEY_IMAGE = "image"
        const val KEY_PREFERENCE_NAME = "chatAppPreference"
        const val KEY_USER = "user"
        const val KEY_ACTIVE="online"

        const val KEY_COLLECTION_CHAT = "chat"
        const val KEY_SENDER_ID = "senderId"
        const val KEY_SENDER_NAME = "senderName"
        const val KEY_RECEIVER_ID = "receiverId"
        const val KEY_MESSAGE = "message"

        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_TMSP_DAY = "dayOfMonth"
        const val KEY_TMSP_MONTH = "monthValue"
        const val KEY_TMSP_YEAR = "year"
        const val KEY_TMSP_HOUR = "hour"
        const val KEY_TMSP_MINUTE = "minute"

        const val KEY_GROUP = "group"
        const val KEY_GROUP_ID = "groupId"
        const val KEY_GROUP_NAME  = "groupName"
        const val KEY_GROUP_IMAGE = "groupImage"
        const val KEY_GROUP_ADMIN_ID = "groupAdminId"
        const val KEY_GROUP_ADMIN_NAME = "groupAdminId"
        const val KEY_GROUP_TIMESTAMP = "timestamp"

        const val KEY_GROUP_MEMBER_ID = "memberId"
        const val KEY_GROUP_MEMBER_NAME  = "memberName"
        const val KEY_GROUP_MEMBER_ROLE = "memberRole"

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

        const val CIPHER_KEY = "BEWAREOBLIVIONISATHAND"
        const val IS_CIPHER_ACTIVATED = "isCipherActivated"
        const val BASE64_REGEX = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"

        fun isBase64(text:String): Boolean{
            return text.matches(Regex(BASE64_REGEX)) && text.length > 3
        }
    }
}