package com.ianleshan.doublesymmetrytask.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ianleshan.doublesymmetrytask.Greeting
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.Modifier

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = darkColors()
            ) {
                Box(Modifier
                    .background(color = MaterialTheme.colors.background)
                ) {
                    App()
                }
            }
        }
    }
}
