package com.ianleshan.doublesymmetrytask.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ianleshan.doublesymmetrytask.Greeting
import android.widget.TextView
import androidx.activity.compose.setContent

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}
