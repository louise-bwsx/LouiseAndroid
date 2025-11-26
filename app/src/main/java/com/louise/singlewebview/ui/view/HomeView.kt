package com.louise.singlewebview.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.louise.singlewebview.manager.AppManager

@Composable
fun HomeView(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("path: ${AppManager.inst.path.value}")
        TextButton(onClick = { AppManager.inst.showWebView("/notifications") }) {
            Text("Go To Notifications")
        }
        TextButton(onClick = { AppManager.inst.showWebView("/properties") }) {
            Text("Go To properties")
        }
        TextButton(onClick = { AppManager.inst.showWebView("/maintenance") }) {
            Text("Go To maintenance")
        }
        TextButton(onClick = { AppManager.inst.showWebView("/profile") }) {
            Text("Go To profile")
        }
    }
}
