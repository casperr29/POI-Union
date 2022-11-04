package com.poi.union.models

import com.google.firebase.database.Exclude
import java.util.*

class mensaje(
    var messageId: String = "",
    var senderId: String = "",
    var senderName: String = "",
    var receiverId : String = "",
    var message: String = "",
    var timestamp: String = "",
    var dateObject: Date? = null,
    var imagen:String? = null
) {
    @Exclude
    var esMio: Boolean = false
}