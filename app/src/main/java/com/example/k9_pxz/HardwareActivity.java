package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import Util.LocaleHelper;
import Util.Navigation;
import Util.Rev;
import Util.Safety;

public class HardwareActivity extends AppCompatActivity {
    private static final String TAG = "HardwareActivity";

    Safety safety = new Safety();
    Navigation navigation = new Navigation();
    private Rev rev = new Rev();

    //Language
    private String language = "en";

    //Gui
    private TextView tvRev;
    private TextView tvTitle;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.activity_hardware);
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

        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_15);
    }

    //init GUI
    private void  initGui(){
        tvTitle=findViewById(R.id.tvHardTitle);
        tvRev=findViewById(R.id.tvSetHardRev);

        btnHome=findViewById(R.id.btnHardHome);
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
        context = LocaleHelper.setLocale(HardwareActivity.this, language);
        resources = context.getResources();
        return resources;
    }

    //load all the text according to the language
    private void loadContentByLanguage(Resources resources) {
        btnHome.setText(resources.getString(R.string.string_text_pv__btn_main));
        tvTitle.setText(resources.getString(R.string.string_sett_hard));
    }

    //load layout
    private void loadLayout(int layout) {
        try {
            setContentView(layout);
        } catch (Exception e) {
            Log.d(TAG, "loadLayout: ex:" + e.getMessage());
        }
    }

    //display software revision
    private void displaySoftRev(String revision) {
        if (revision != null) {
            try {
                tvRev.setText(revision);
            }catch (Exception e){
                Log.d(TAG, "displaySoftRev: ex:"+e.getMessage());
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