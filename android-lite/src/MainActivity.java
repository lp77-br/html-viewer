package br.com.lp.html.editor.lite;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String PREF_NAME = "app_settings";
    private static final String KEY_HTML = "html_content";
    private static final String URL_CREDITS = "https://github.com/lp77-br/html-viewer/tree/main";
    
    private EditText editor;
    private WebView preview;
    private LinearLayout layoutEditor;
    private LinearLayout layoutPreview;
    private SharedPreferences prefs;
    private boolean isPreviewVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        initInterface();
        loadData();
    }

    private void initInterface() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#0f172a"));

        layoutEditor = new LinearLayout(this);
        layoutEditor.setOrientation(LinearLayout.VERTICAL);
        layoutEditor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
        scroll.setFillViewport(true);

        editor = new EditText(this);
        editor.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editor.setBackgroundColor(Color.TRANSPARENT);
        editor.setTextColor(Color.WHITE);
        editor.setTextSize(14);
        editor.setTypeface(Typeface.MONOSPACE);
        editor.setGravity(Gravity.TOP);
        editor.setPadding(40, 40, 40, 40);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        scroll.addView(editor);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setPadding(10, 10, 10, 5);
        toolbar.setGravity(Gravity.CENTER);

        Button btnRun = makeBtn("RODAR", "#3b82f6");
        Button btnCopy = makeBtn("COPIAR", "#1e293b");
        Button btnClear = makeBtn("LIMPAR", "#ef4444");

        toolbar.addView(btnRun);
        toolbar.addView(btnCopy);
        toolbar.addView(btnClear);

        TextView footer = new TextView(this);
        footer.setText("Open Source (Clique Aqui)");
        footer.setTextColor(Color.parseColor("#64748b"));
        footer.setTextSize(11);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, 10, 0, 25);
        footer.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_CREDITS)));
            } catch (Exception e) {}
        });

        layoutEditor.addView(scroll);
        layoutEditor.addView(toolbar);
        layoutEditor.addView(footer);

        layoutPreview = new LinearLayout(this);
        layoutPreview.setOrientation(LinearLayout.VERTICAL);
        layoutPreview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layoutPreview.setVisibility(View.GONE);

        preview = new WebView(this);
        preview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layoutPreview.addView(preview);

        root.addView(layoutEditor);
        root.addView(layoutPreview);
        setContentView(root);

        btnRun.setOnClickListener(v -> toggleView());
        btnCopy.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("code_lite", editor.getText().toString()));
            Toast.makeText(this, "Copiado!", Toast.LENGTH_SHORT).show();
        });
        btnClear.setOnClickListener(v -> editor.setText(""));
    }

    private Button makeBtn(String label, String color) {
        Button b = new Button(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        p.setMargins(6, 6, 6, 6);
        b.setLayoutParams(p);
        b.setText(label);
        b.setTextColor(Color.WHITE);
        b.setBackgroundColor(Color.parseColor(color));
        b.setTextSize(12);
        return b;
    }

    private void loadData() {
        String defaultHtml = "<!DOCTYPE html>\n<html lang=\"pt-br\">\n\n<head>\n  <meta charset=\"UTF-8\">\n  <title>HTML Editor</title>\n</head>\n\n<body>\n  <center>\n    <br><br><br>\n    <hr width=\"30%\">\n    <h1>Um simples editor de HTML</h1>\n    <hr width=\"30%\">\n  </center>\n</body>\n\n</html>";
        editor.setText(prefs.getString(KEY_HTML, defaultHtml));

        WebSettings s = preview.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        preview.setWebViewClient(new WebViewClient());
        preview.setWebChromeClient(new WebChromeClient());
    }

    private void toggleView() {
        if (!isPreviewVisible) {
            String html = editor.getText().toString();
            prefs.edit().putString(KEY_HTML, html).apply();
            preview.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            layoutEditor.setVisibility(View.GONE);
            layoutPreview.setVisibility(View.VISIBLE);
        } else {
            preview.loadUrl("about:blank");
            layoutPreview.setVisibility(View.GONE);
            layoutEditor.setVisibility(View.VISIBLE);
        }
        isPreviewVisible = !isPreviewVisible;
    }

    @Override
    public void onBackPressed() {
        if (isPreviewVisible) {
            toggleView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (preview != null) {
                preview.clearCache(true);
            }
            WebStorage.getInstance().deleteAllData();
        } catch (Exception e) {}
    }

    @Override
    protected void onDestroy() {
        try {
            if (preview != null) {
                preview.clearCache(true);
                preview.clearHistory();
                preview.destroy();
            }
            WebStorage.getInstance().deleteAllData();
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {}
        super.onDestroy();
    }
}
