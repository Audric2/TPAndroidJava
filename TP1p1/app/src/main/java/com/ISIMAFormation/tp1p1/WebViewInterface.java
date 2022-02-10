package com.ISIMAFormation.tp1p1;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class WebViewInterface {
    Context context;

    WebViewInterface(Context context){
        this.context = context;
    }

    @JavascriptInterface
    public void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
