package com.ISIMAFormation.tp1p1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // Création de la webview
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.i("WebView", consoleMessage.message());
                return true;
            }
        });

        // Paramétrage webview
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.addJavascriptInterface(new WebViewInterface(this), "Java");

        // Chargement de la page
        //myWebView.loadUrl("https://www.isima.fr/");
        myWebView.loadUrl("file:///android_asset/www/index.html");

        // Masquage de la toolbar
        getSupportActionBar().hide();
    }
}