package com.example.wclchat

import android.app.Application
import com.example.wclchat.db.MainDb

class MainApp : Application() {
    val database by lazy { MainDb.getDatabase(this) }
}