package com.ich.pullgo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

class AddressActivity : AppCompatActivity() {

    companion object {
        const val ADDRESS_REQUEST_CODE = 1234
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        val webView = findViewById<WebView>(R.id.webViewAddress)

        webView.settings.javaScriptEnabled = true

        webView.addJavascriptInterface(KaKaoJavaScriptInterface(),"Android")
        webView.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        webView.loadUrl("http://api.pullgo.kr/v1/daum.html")
    }

    inner class KaKaoJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(address: String?) {
            Intent().apply {
                putExtra("address", address)
                setResult(RESULT_OK, this)
            }
            finish()
        }
    }
}