package com.louise.louiseandroid.ui.view

import android.view.ViewGroup
import android.webkit.WebSettings
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
import com.louise.louiseandroid.JSBridge
import com.louise.louiseandroid.manager.AppManager

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

                // 字體渲染相關（非常重要）
                loadWithOverviewMode = true
                useWideViewPort = true
                loadsImagesAutomatically = true

                // 必須加：否則字重容易失效
                minimumFontSize = 1
                textZoom = 100

                // 無論如何都要啟用
                allowFileAccess = true
                allowContentAccess = true

                // 高品質渲染
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false

                // 最關鍵：避免系統干預字型
                layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            }

            // 非常重要：強制使用硬體加速，否則字重會被合併
            setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)

            addJavascriptInterface(JSBridge(), "AndroidBridge")

            webChromeClient = object : android.webkit.WebChromeClient() {}
            webViewClient = object : android.webkit.WebViewClient() {
                override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    val css = """
        <style>
        @font-face {
            font-family: 'NotoSans';
            src: url('file:///android_asset/fonts/NotoSans-Regular.ttf') format('truetype');
            font-weight: 400;
        }
        @font-face {
            font-family: 'NotoSans';
            src: url('file:///android_asset/fonts/NotoSans-Medium.ttf') format('truetype');
            font-weight: 500;
        }
        @font-face {
            font-family: 'NotoSans';
            src: url('file:///android_asset/fonts/NotoSans-SemiBold.ttf') format('truetype');
            font-weight: 600;
        }
        @font-face {
            font-family: 'NotoSans';
            src: url('file:///android_asset/fonts/NotoSans-Bold.ttf') format('truetype');
            font-weight: 700;
        }

        * {
            font-family: 'NotoSans' !important;
        }
        </style>
    """.trimIndent()

                    view?.evaluateJavascript(
                        """
        (function() {
            var style = document.createElement('style');
            style.innerHTML = `$css`;
            document.head.appendChild(style);
        })();
        """.trimIndent(),
                        null
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
