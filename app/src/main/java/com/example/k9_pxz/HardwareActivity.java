package com.example.k9_pxz;

import static java.lang.Long.MAX_VALUE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import Util.Key_Util;
import Util.LocaleHelper;
import Util.Navigation;
import Util.Rev;
import Util.Safety;

public class HardwareActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "HardwareActivity";
    private Key_Util keyUtil = new Key_Util();
    private String SERIAL_NUMBER = "0";
    private String DEVICE_ID = "123456";
    //working time
    private long year = 1;
    private long day = 1;
    private long hours = 1;
    private long minutes = 1;
    private long seconds = 1;
    private long totalSecs = 1;
    private String timeString = "00:00:00";
    //GUI
    private TextView tvSN;
    private TextView tvLife;

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
        //fix orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
        //events
        events();
        //load preferences
        loadPreferences2();
        loadPreferences();
        //
        displaySerialNumber(SERIAL_NUMBER, DEVICE_ID);
        //working time
        displayWorkingTime(getWorkingTime());

    }

    private void events(){
        btnHome.setOnClickListener(this);
    }

    //launch home activity
    private void goHome() {
        launchActivity(MainActivity.class);
    }


    //change activity
    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    //init GUI
    private void  initGui(){
        tvTitle=findViewById(R.id.tvHardTitle);
        tvRev=findViewById(R.id.tvConBurn);
        btnHome=findViewById(R.id.btnHardHome);

        tvSN = findViewById(R.id.tvSN);
        tvLife = findViewById(R.id.tvSystemLife);
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

    private void loadPreferences2() {
        SharedPreferences sharedPref = getSharedPreferences(keyUtil.KEY_SETTINGS2, MODE_PRIVATE);
        SERIAL_NUMBER = sharedPref.getString(keyUtil.KEY_SERIAL_NUMBER, "0");
        DEVICE_ID = sharedPref.getString(keyUtil.KEY_ID, "12345");
        Log.d(TAG, "loadPreferences:KEY_SERIAL_NUMBER:" + SERIAL_NUMBER);
    }

    private void loadPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(keyUtil.KEY_SETTINGS, MODE_PRIVATE);
        totalSecs = sharedPref.getLong(keyUtil.KEY_LIFE_TIME, 1);
    }

    private String getWorkingTime() {
        try {
            if (true) {
                if (totalSecs > MAX_VALUE) {/**/
                    totalSecs = MAX_VALUE;
                }

                year = day / 365;
                day = hours / 24;
                hours = totalSecs / 3600;
                minutes = (totalSecs % 3600) / 60;
                seconds = totalSecs % 60;
                //timeString = String.format("%02d:%02d:%02d:%02d:%02d", year, day, hours, minutes, seconds);
                timeString = String.format("%02d:%02d:%02d:%02d", year, day, hours,minutes);
            }
        } catch (Exception e) {
            Log.d(TAG, "getWorkingTime: ex:" + e.getMessage());
        }
        return timeString;
    }

    //display working time
    private void displayWorkingTime(String life) {
        try {
            if (life != null) {
                tvLife.setText("TOTAL RUNNING TIME: \n" + life +"\n"+"(Year:Day:Hour)" );
            }
        } catch (Exception e) {
            Log.d(TAG, "displaySerialNumber: ex:" + e.getMessage());
        }

    }

    //display serial number
    private void displaySerialNumber(String sn, String id) {
        try {
            if (sn != null && id != null) {
                tvSN.setText("S/N:" + sn + "\n" + "ID:" + id);
            }
        } catch (Exception e) {
            Log.d(TAG, "displaySerialNumber: ex:" + e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        if(v==btnHome){
            goHome();
        }
    }
}