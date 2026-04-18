package br.com.lp.html.editor;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webview1;
    private ValueCallback<Uri[]> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        webview1 = new WebView(this);
        WebSettings ws = webview1.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setAllowContentAccess(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ws.setAllowFileAccessFromFileURLs(false);
            ws.setAllowUniversalAccessFromFileURLs(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        }

        webview1.addJavascriptInterface(new WebAppInterface(this), "Android");

        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("file:///android_asset/")) return false;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {}
                return true;
            }
        });

        webview1.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                try {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    result.confirm();
                } catch (Exception e) {}
                return true;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                try {
                    if (mCustomView != null) {
                        callback.onCustomViewHidden();
                        return;
                    }
                    mCustomView = view;
                    mCustomViewCallback = callback;
                    ((ViewGroup) getWindow().getDecorView()).addView(mCustomView, new FrameLayout.LayoutParams(-1, -1));
                    webview1.setVisibility(View.GONE);
                } catch (Exception e) {}
            }

            @Override
            public void onHideCustomView() {
                try {
                    if (mCustomView == null) return;
                    webview1.setVisibility(View.VISIBLE);
                    ((ViewGroup) getWindow().getDecorView()).removeView(mCustomView);
                    mCustomViewCallback.onCustomViewHidden();
                    mCustomView = null;
                } catch (Exception e) {}
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                try {
                    if (mUploadMessage != null) mUploadMessage.onReceiveValue(null);
                    mUploadMessage = filePathCallback;
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    startActivityForResult(Intent.createChooser(intent, "File"), FILECHOOSER_RESULTCODE);
                } catch (Exception e) {
                    mUploadMessage = null;
                }
                return true;
            }
        });

        esconderBarras();
        setContentView(webview1);
        webview1.loadUrl("file:///android_asset/index.html");
    }

    public class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) { mContext = c; }

        @JavascriptInterface
        public void copyToClipboard(String text) {
            try {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText("LP77_CODE", text));
            } catch (Exception e) {}
        }

        @JavascriptInterface
        public void openUrl(String url) {
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {}
        }

        @JavascriptInterface
        public void downloadExtractor() {
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lp77-br/html-viewer")));
            } catch (Exception e) {}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (mUploadMessage == null) return;
                Uri[] result = (resultCode == Activity.RESULT_OK && data != null) ? new Uri[]{Uri.parse(data.getDataString())} : null;
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } catch (Exception e) {}
    }

    private void esconderBarras() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } catch (Exception e) {}
    }

    @Override
    public void onBackPressed() {
        try {
            if (mCustomView != null) {
                ((WebChromeClient) webview1.getWebChromeClient()).onHideCustomView();
            } else if (webview1.canGoBack()) {
                webview1.goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {}
    }
}
