package com.slc.morse.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.slc.morse.ui.lantern.LanternActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, LanternActivity::class.java))
    }

}