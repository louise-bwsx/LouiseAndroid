package com.louise.singlewebview.ui.view

import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.louise.singlewebview.JSBridge
import com.louise.singlewebview.manager.AppManager

@Composable
fun WebView(modifier: Modifier = Modifier, isVisible: Boolean = true) {
    val context = LocalContext.current
    // 使Android的返回按鈕會嘗試在WebView觸發返回
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    // 創建Webview實例
    val webView = remember {
        android.webkit.WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            settings.apply {
                javaScriptEnabled = true
                // 需要加上這一行 才能從"/"跳轉到"/notifications" 不然跳轉後只會看到白畫面跟vue小工具
                domStorageEnabled = true  // 啟用 DOM Storage
            }

            addJavascriptInterface(JSBridge(), "AndroidBridge")

            webChromeClient = object : android.webkit.WebChromeClient() {}
            webViewClient = object : android.webkit.WebViewClient() {
                override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // 注入 JS 模擬 iOS messageHandlers 因為compose沒有messageHandlers
                    view?.evaluateJavascript(
                        """
            if (!window.webkit) {
                window.webkit = { messageHandlers: {} };
            }
            window.webkit.messageHandlers.urlHandler = {
                postMessage: function(message) {
                    try {
                        // 如果傳進來是物件，轉成 JSON 字串
                        if (typeof message === 'object') {
                            message = JSON.stringify(message);
                        }
                        AndroidBridge.urlHandler(message);
                    } catch (e) {
                        console.error("urlHandler error:", e);
                    }
                }
            };
            """.trimIndent(), null
                    )
                }
            }

            // Load the web view
            loadUrl(AppManager.inst.fullUrl())
        }
    }

    // 在 WebView 創建後立即註冊到 AppManager
    LaunchedEffect(webView) {
        AppManager.inst.setWebView(webView)
    }

    // 註冊返回按鈕點擊事件
    DisposableEffect(webView) {
        val callback = object : OnBackPressedCallback(true) {
            // 當點擊返回按鈕時
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                }
            }
        }

        onBackPressedDispatcher?.addCallback(callback)

        onDispose { callback.remove() }
    }

    // 使用AndroidView 渲染Webview內容
    AndroidView(factory = { webView })

    Column {
        Text("fullUrl: ${AppManager.inst.fullUrl()}")
        Text("path: ${AppManager.inst.path.value}")
    }
}
