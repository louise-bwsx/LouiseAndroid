package com.louise.louiseandroid.manager

import android.util.Log
import android.webkit.WebView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.louise.louiseandroid.BuildConfig
import java.lang.ref.WeakReference


object AppManager {
    @Volatile
    private var INSTANCE: AppManager? = null
    val inst: AppManager by lazy { getInstance() }

    private const val IS_DEBUG = true  // 或使用 BuildConfig.DEBUG

    // 從 BuildConfig 讀取環境變數
    val baseUrl: String = BuildConfig.BASE_URL
    val isDebug: Boolean = BuildConfig.IS_DEBUG


    private val _path = mutableStateOf("/")
    val path: State<String> = _path

    // 儲存創建好的WebView
    private var webViewRef: WeakReference<WebView>? = null

    private fun getInstance(): AppManager {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: AppManager.also { INSTANCE = it }
        }
    }

    // 設定 WebView 實例
    fun setWebView(webView: android.webkit.WebView) {
        this.webViewRef = WeakReference(webView)
        if (isDebug) {
            Log.d("AppManager", "WebView 設定完成，當前環境：DEBUG")
            Log.d("AppManager", "Base URL: $baseUrl")
        }
    }

    fun showWebView(newPath: String) {
        Log.i("AppManager", "changeWebView: $newPath")
        _path.value = newPath
        // 通知 Web 端使用router.push修改網址
        sendMessageToWeb("pathChanged", """{"path": "$newPath"}""")
    }

    // 發送訊息到 Web 的函數
    private fun sendMessageToWeb(eventName: String, data: String) {
        webViewRef?.get()?.let { webView ->
            val jsCode =
                "if (typeof receiveAppMessage === 'function') { receiveAppMessage('$eventName', $data); }"
            webView.evaluateJavascript(jsCode) { result ->
                Log.d("AppManager", "JavaScript 執行結果: $result")
            }
        } ?: Log.w("AppManager", "WebView 實例為空或已被回收，無法發送訊息")
    }

    // Web 端回傳路徑更新 當_path == "/"時 app不顯示webView
    fun updatePathFromWeb(newPath: String) {
        Log.i("AppManager", "Web 端回傳路徑: $newPath")
        _path.value = newPath
    }

    fun fullUrl(): String {
        return baseUrl + _path.value
    }
}
