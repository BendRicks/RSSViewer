package ru.bendricks.rssviewer.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.bendricks.rssviewer.R

class RssWebViewActivity : AppCompatActivity() {

    private lateinit var webView : WebView
    private lateinit var closeActivityButton: FloatingActionButton

    companion object {
        const val URL = "url"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss_web_view)

        webView = findViewById(R.id.web_view)
        closeActivityButton = findViewById(R.id.close_button)

        val url = intent.getStringExtra(URL)

        if (url.isNullOrBlank()){
            Toast.makeText(this, "Incorrect url", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        closeActivityButton.setOnClickListener { finish() }

        webView.webViewClient = WebViewClient()
        webView.loadUrl(url!!)
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }

}