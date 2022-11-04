package com.poi.union.models

import com.poi.union.models.Users
import java.text.FieldPosition

interface UserListener {
    fun onUserClicked(user:Users)
}