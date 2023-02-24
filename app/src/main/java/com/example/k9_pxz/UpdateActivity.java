package com.example.k9_pxz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import Util.LocaleHelper;
import Util.Navigation;
import Util.Rev;
import Util.Safety;

public class UpdateActivity extends AppCompatActivity {
    private static final String TAG = "UpdateActivity";
    Safety safety = new Safety();
    Navigation navigation = new Navigation();
    private Rev rev = new Rev();
    private String language = "en";

    private TextView tvSetRev;
    private WebView wv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.activity_update);
        //remove menu bar
        removeMenuBar();
        //remove action bar from top
        removeActionBar();
        //init all components
        initGui();
        //init others stuff
        initOther();
        //init language
        initLang();
        //buttons events
        events();
        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_14);

        //initWebView();
    }

    private void initGui() {
        tvSetRev = findViewById(R.id.tvSetRev);

        // initialising the web view
         wv = (WebView) findViewById(R.id.webview);
    }

    //init other stuff
    private void initOther() {
        language = getExtrasFromAct();
    }

    //init language
    private void initLang() {
        loadContentByLanguage(getResourcesLanguage(language));
    }

    //init web view
    private void initWebView(){
        // add your link here
        wv.loadUrl("https://drive.google.com/drive/u/2/my-drive");
        wv.setWebViewClient(new Client());
        WebSettings ws = wv.getSettings();

        // Enabling javascript
        ws.setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.clearCache(true);
        wv.clearHistory();

        // download manager is a service that can be used to handle downloads
       /* wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
                req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(req);
                Toast.makeText(UpdateActivity.this, "Downloading....", Toast.LENGTH_SHORT).show();
            }
        });*/

        wv.postDelayed(new Runnable() {
            @Override
            public void run() {
                wv.loadUrl("https://drive.google.com/drive/u/2/my-drive");
            }
        },500);
    }

    //load all the text according to the language
    private void loadContentByLanguage(Resources resources) {
        /*btnHome.setText(resources.getString(R.string.string_text_pv__btn_main));
        btnSetLink.setText(resources.getString(R.string.string_sett_link));
        btnBurn.setText(resources.getString(R.string.string_sett_burning));
        btnAbout.setText(resources.getString(R.string.string_sett_About));
        btnSystem.setText(resources.getString(R.string.string_sett_system));
        tvSetTitle.setText(resources.getString(R.string.string_sett_title));*/


    }

    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(UpdateActivity.this, language);
        resources = context.getResources();
        return resources;
    }

    private void events() {
        /*btnHome.setOnClickListener(this);
        btnSetLink.setOnClickListener(this);
        btnBurn.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnSystem.setOnClickListener(this);*/
    }

    //load layout
    private void loadLayout(int layout) {
        try {
            //setContentView(R.layout.activity_main);
            setContentView(layout);
        } catch (Exception e) {
            Log.d(TAG, "loadLayout: ex:" + e.getMessage());
        }
    }

    //display software revision
    private void displaySoftRev(String revision) {
        if (revision != null) {
           // tvSetRev.setText(revision);
        }
    }

    //remove action bar
    private void removeActionBar() {
        // Take instance of Action Bar
        // using getSupportActionBar and
        // if it is not Null
        // then call hide function
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } catch (Exception e) {
            Log.d(TAG, "removeActionBar: ex:" + e.getMessage());
        }

    }

    //remove action bar
    private void screenFullSize() {
        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            Log.d(TAG, "screenFullSize: ex:" + e.getMessage());
        }
    }

    //remove menu bar
    private void removeMenuBar() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);


        } catch (Exception e) {
            Log.d(TAG, "removeMenuBar: ex:" + e.getMessage());
        }
    }

    //get language
    private String getExtrasFromAct() {
        String lang = "en";
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                //get language
                lang = bundle.getString("language");
                if (lang != null) {
                    return lang;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getExtrasFromAct: " + e.getMessage());
        }
        return "en";
    }

    private class Client extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            // if stop loading
            try {
                webView.stopLoading();
            } catch (Exception e) {
            }

            if (webView.canGoBack()) {
                webView.goBack();
            }
            // if loaded blank then show error
            // to check internet connection using
            // alert dialog
            webView.loadUrl("about:blank");
            AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Check your internet connection and Try again.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });

            alertDialog.show();


            super.onReceivedError(webView, errorCode, description, failingUrl);
        }
    }

}