package com.slc.morse.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.slc.morse.ui.chat.ChatActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, ChatActivity::class.java))
    }

}