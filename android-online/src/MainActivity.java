package br.com.lp.html.editor.online;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.net.URLEncoder;

public class MainActivity extends Activity {

    private WebView webview1;
    private ValueCallback<Uri[]> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    private WebView hiddenEngine;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable timeoutTask;

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
        
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webview1.addJavascriptInterface(new WebAppInterface(this), "Android");

        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("file:///android_asset/")) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception e) {}
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                if (!url.startsWith("file:///android_asset/")) {
                    try {
                        String extractScript = "javascript:if(window.onBackgroundExtract){ window.onBackgroundExtract('<html>'+document.documentElement.innerHTML+'</html>'); }";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            view.evaluateJavascript(extractScript, null);
                        } else {
                            view.loadUrl(extractScript);
                        }
                    } catch (Exception e) {}
                }
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
                    if (mCustomView != null) { callback.onCustomViewHidden(); return; }
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
                } catch (Exception e) { mUploadMessage = null; }
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
        public void fetchUrl(final String urlString) {
            iniciarExtratorOculto(urlString);
        }
    }

    private void iniciarExtratorOculto(final String alvoUrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    limparMotorOculto();

                    hiddenEngine = new WebView(MainActivity.this);
                    WebSettings ws = hiddenEngine.getSettings();
                    ws.setJavaScriptEnabled(true);
                    ws.setDomStorageEnabled(true);
                    ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    }
                    
                    ws.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

                    hiddenEngine.setWebChromeClient(new WebChromeClient());

                    hiddenEngine.addJavascriptInterface(new Object() {
                        @JavascriptInterface
                        public void enviarDOM(final String html) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cancelarTimeout();
                                    enviarParaJs(html);
                                    limparMotorOculto();
                                }
                            });
                        }
                    }, "LPExtratorJS");

                    hiddenEngine.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return false;
                        }

                        @Override
                        public void onPageFinished(final WebView view, String url) {
                            mainHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (hiddenEngine != null) {
                                            String jsInjetado = "javascript:window.LPExtratorJS.enviarDOM('<html>'+document.documentElement.innerHTML+'</html>');";
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                view.evaluateJavascript(jsInjetado, null);
                                            } else {
                                                view.loadUrl(jsInjetado);
                                            }
                                        }
                                    } catch (Exception e) {}
                                }
                            }, 2500);
                        }
                    });

                    timeoutTask = new Runnable() {
                        @Override
                        public void run() {
                            if (hiddenEngine != null) {
                                enviarParaJs("ERRO_CHAMADA");
                                limparMotorOculto();
                            }
                        }
                    };
                    mainHandler.postDelayed(timeoutTask, 15000);

                    hiddenEngine.loadUrl(alvoUrl);

                } catch (Exception e) {
                    enviarParaJs("ERRO_CHAMADA");
                    limparMotorOculto();
                }
            }
        });
    }

    private void cancelarTimeout() {
        try {
            if (timeoutTask != null) {
                mainHandler.removeCallbacks(timeoutTask);
                timeoutTask = null;
            }
        } catch (Exception e) {}
    }

    private void limparMotorOculto() {
        try {
            if (hiddenEngine != null) {
                hiddenEngine.stopLoading();
                hiddenEngine.clearHistory();
                hiddenEngine.clearCache(true);
                hiddenEngine.removeAllViews();
                hiddenEngine.destroy();
                hiddenEngine = null;
            }
        } catch (Exception e) {}
    }

    private void enviarParaJs(String dados) {
        try {
            String encoded = URLEncoder.encode(dados, "UTF-8").replace("+", "%20");
            final String js = "javascript:if(window.onUrlFetched){ window.onUrlFetched(decodeURIComponent('" + encoded + "')); }";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            webview1.evaluateJavascript(js, null);
                        } else {
                            webview1.loadUrl(js);
                        }
                    } catch (Exception e) {}
                }
            });
        } catch (Exception e) {}
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

    @Override
    protected void onDestroy() {
        try {
            limparMotorOculto();
            cancelarTimeout();
            super.onDestroy();
        } catch (Exception e) {}
    }
}
