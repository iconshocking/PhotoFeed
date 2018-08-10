package com.a23andme.shock.photofeed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.a23andme.shock.photofeed.FeedActivity.AUTH_TOKEN_EXTRA;
import static com.a23andme.shock.photofeed.Network.NetworkApi.AMPERSAND;
import static com.a23andme.shock.photofeed.Network.NetworkApi.EQUALS;
import static com.a23andme.shock.photofeed.Network.NetworkApi.ID_TOKEN_PREFIX;
import static com.a23andme.shock.photofeed.Network.NetworkApi.LOGIN_URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupLoginWebView();
    }

    private void setupLoginWebView() {
        final WebView myWebView = findViewById(R.id.login_webview);
        myWebView.loadUrl(LOGIN_URL);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains(ID_TOKEN_PREFIX)) {
                    int startIndexOfAuthorizationToken =
                            url.indexOf(ID_TOKEN_PREFIX) + ID_TOKEN_PREFIX.length() + EQUALS.length();
                    int endIndexOfAuthorizationToken = url.indexOf(AMPERSAND, startIndexOfAuthorizationToken);
                    String authToken = url.substring(startIndexOfAuthorizationToken, endIndexOfAuthorizationToken);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(AUTH_TOKEN_EXTRA, authToken);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}
