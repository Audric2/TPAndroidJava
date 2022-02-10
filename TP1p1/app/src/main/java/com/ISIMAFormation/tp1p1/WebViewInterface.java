package com.ISIMAFormation.tp1p1;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WebViewInterface {
    Context context;

    WebViewInterface(Context context){
        this.context = context;
    }

    // 1 : JS vers JAVA
    @JavascriptInterface
    public void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    // 2 : Java vers JS
    @JavascriptInterface
    public String getJSON() throws InterruptedException {
        JsonDownloader jd = new JsonDownloader("https://perso.isima.fr/~aucatinon/jsonformation.json");
        Thread t = new Thread(jd);
        t.start();
        t.join();
        return jd.getJson();
    }

    private class JsonDownloader implements Runnable {
        private String json;
        private String urlStr = "";

        JsonDownloader(String url){
            this.urlStr = url;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000000);
                conn.setConnectTimeout(5000000);
                conn.setRequestMethod("GET");
                conn.connect();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                Scanner s = new Scanner(in).useDelimiter("\\a");
                json = s.hasNext() ? s.next() : "";
            } catch (IOException e) {}
            finally { if(conn!=null)  conn.disconnect(); }
        }

        public String getJson() {
            return json;
        }
    }
}
