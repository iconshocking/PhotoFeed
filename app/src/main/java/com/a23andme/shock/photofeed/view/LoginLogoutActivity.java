package com.a23andme.shock.photofeed.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.a23andme.shock.photofeed.R;

import static com.a23andme.shock.photofeed.model.network.NetworkApi.AMPERSAND;
import static com.a23andme.shock.photofeed.model.network.NetworkApi.EQUALS;
import static com.a23andme.shock.photofeed.model.network.NetworkApi.ID_TOKEN_PREFIX;
import static com.a23andme.shock.photofeed.model.network.NetworkApi.LOGIN_AND_LOGOUT_REDIRECT_URI;
import static com.a23andme.shock.photofeed.model.network.NetworkApi.LOGIN_URL;
import static com.a23andme.shock.photofeed.model.network.NetworkApi.LOGOUT_URL;
import static com.a23andme.shock.photofeed.view.FeedActivity.AUTH_TOKEN_EXTRA;

public class LoginLogoutActivity extends AppCompatActivity {
    public static final String IS_LOGOUT_EXTRA = "is_logout";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        webView = findViewById(R.id.login_webview);
        setupWebView();

        if (getIntent().getBooleanExtra(IS_LOGOUT_EXTRA, false)) {
            webView.loadUrl(LOGOUT_URL);
        } else {
            webView.loadUrl(LOGIN_URL);
        }
    }

    private void setupWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (url.contains(ID_TOKEN_PREFIX)) {
                    // redirected after successful login
                    int startIndexOfAuthorizationToken =
                            url.indexOf(ID_TOKEN_PREFIX) + ID_TOKEN_PREFIX.length() + EQUALS.length();
                    int endIndexOfAuthorizationToken = url.indexOf(AMPERSAND, startIndexOfAuthorizationToken);
                    String authToken = url.substring(startIndexOfAuthorizationToken, endIndexOfAuthorizationToken);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(AUTH_TOKEN_EXTRA, authToken);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (url.startsWith(LOGIN_AND_LOGOUT_REDIRECT_URI)) {
                    // catches redirect after successful logout
                    webView.loadUrl(LOGIN_URL);
                }
            }
        });
    }
}
