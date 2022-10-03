package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import Util.LocaleHelper;
import Util.Rev;

public class SleepActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SleepActivity";
    private Button btnExitSleep;
    private TextView tvSleepRev;

    //revision
    private Rev rev = new Rev();
    //Language
    private String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.layout_sleep);
        //remove menu bar
        removeMenuBar();
        //remove action bar from top
        removeActionBar();
        //init all components
        initGUI();
        //init others stuff
        initOther();
        //init language
        initLang();
        //buttons events
        eventsBtn();

        //
        displaySoftRev(rev.APP_REV_PAGE_12);
    }

    private void initGUI() {
        btnExitSleep = findViewById(R.id.btnSleepDisable);
        tvSleepRev = findViewById(R.id.tvSleepRev);
    }

    //init other stuff
    private void initOther() {
        language = getExtrasFromAct();
    }

    //init language
    private void initLang() {
        loadContentByLanguage(getResourcesLanguage(language));
    }

    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(SleepActivity.this, language);
        resources = context.getResources();
        return resources;
    }

    //load all the text according to the language
    private void loadContentByLanguage(Resources resources) {
        btnExitSleep.setText(resources.getString(R.string.string_text_btn_sleep));
    }

    private void eventsBtn() {
        btnExitSleep.setOnClickListener(this);
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnExitSleep) {
            launchActivity(MainActivity.class);
        }
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
            if (tvSleepRev != null) {
                tvSleepRev.setText(revision);

            }

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
}