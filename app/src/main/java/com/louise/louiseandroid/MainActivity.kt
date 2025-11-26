package com.louise.louiseandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.louise.louiseandroid.ui.theme.SingleWebViewTheme
import com.louise.louiseandroid.ui.view.WebView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SingleWebViewTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),      // 預設的下方Bar
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding),
                        ) {
                        WebView(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
