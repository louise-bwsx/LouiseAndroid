package com.louise.singlewebview

import android.util.Log
import android.webkit.JavascriptInterface
import com.louise.singlewebview.manager.AppManager
import org.json.JSONObject

class JSBridge {
    @JavascriptInterface
    fun urlHandler(message: String) {
        Log.i("JSBridge", "收到網址: $message")
        try {
            val json = JSONObject(message)
            val toPath = json.optString("to")
            AppManager.inst.updatePathFromWeb(toPath)
        } catch (e: Exception) {
            Log.e("JSBridge", "解析 JSON 失敗", e)
        }
    }
}
