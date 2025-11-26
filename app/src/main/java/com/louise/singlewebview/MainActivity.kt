package com.louise.singlewebview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.louise.singlewebview.manager.AppManager
import com.louise.singlewebview.ui.components.BottomBar
import com.louise.singlewebview.ui.theme.SingleWebViewTheme
import com.louise.singlewebview.ui.view.HomeView
import com.louise.singlewebview.ui.view.WebView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SingleWebViewTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),      // 預設的下方Bar
                    bottomBar = { BottomBar() }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        WebView(modifier = Modifier.fillMaxSize())

                        if (AppManager.inst.path.value == "/") {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent) // 透明背景
                                    // 攔截點擊 避免點擊到被HomeView覆蓋的WebView
                                    .clickable(enabled = true) { }
                            )
                        }

                        // HomeView 覆蓋在上面 在HomeView裡面設定背景顏色
                        if (AppManager.inst.path.value == "/") {
                            HomeView(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}
