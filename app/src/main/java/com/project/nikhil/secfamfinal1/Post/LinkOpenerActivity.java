package com.project.nikhil.secfamfinal1.Post;


import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class LinkOpenerActivity extends BaseActivity {
    MaterialProgressBar progressBar;
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialToolbar toolbar;
    TextView tv_title,tv_url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_opener);

        String WEB_URL = getIntent().getExtras().getString("url");
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.bg);
        toolbar = findViewById(R.id.topAppBar);
        tv_title = findViewById(R.id.tv_title);
        tv_url = findViewById(R.id.tv_url);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isInternetAvailable()) {
                    webView.reload();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        progressBar = findViewById(R.id.download_progressbar);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setSupportMultipleWindows(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.clearCache(true);
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new ChromeClient());
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.loadUrl(WEB_URL);

        isInternetAvailable();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!webView.canGoBack()) {
            webView.stopLoading();
            super.onBackPressed();
        } else {
            webView.stopLoading();
            webView.goBack();
        }
    }

    public class ChromeClient extends WebChromeClient {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                if (title.length()>25){
                    tv_title.setText(title.substring(0,25)+"...");
                }else {
                    tv_title.setText(view.getTitle());
                }
                if (webView.getUrl().length()>50){
                    tv_url.setText(webView.getUrl().substring(0,50)+"...");
                }else {
                    tv_url.setText(webView.getUrl());
                }
            }
        }
    }

}