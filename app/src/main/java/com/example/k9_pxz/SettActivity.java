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

import Alert.AlertCustomDialog;
import Alert.CustomAlert;
import Alert.K9Alert;
import Interface.InterfaceSetupInfo;
import Interface.RecyclerViewClickInterface;
import Util.LocaleHelper;
import Util.Navigation;
import Util.Rev;
import Util.Safety;

public class SettActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickInterface, InterfaceSetupInfo {
    private static final String TAG = "SettActivity";
    Safety safety = new Safety();
    Navigation navigation = new Navigation();
    //GUI

    private Button btnHome;
    private Button btnSetLink;
    private Button btnBurn;
    private Button btnAbout;
    private Button btnSystem;
    private Button btnHardware;
    private TextView tvSetRev;
    private TextView tvSetTitle;
    //


    AlertCustomDialog alertCustomDialog;
    CustomAlert customAlert;
    private Rev rev = new Rev();

    //Language
    private String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.activity_sett);
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
    }

    private void initGui() {
        btnHome = findViewById(R.id.btnHome);
        btnSetLink = findViewById(R.id.btnLink);
        btnBurn = findViewById(R.id.btnBurn);
        btnAbout = findViewById(R.id.btnAbout);
        btnSystem = findViewById(R.id.btnSystem);
        btnHardware=findViewById(R.id.btnHardware);
        //
        tvSetRev = findViewById(R.id.tvSetRev);
        tvSetTitle = findViewById(R.id.tvSettTitile);
    }

    //init other stuff
    private void initOther() {
        language = getExtrasFromAct();
    }

    //init language
    private void initLang() {
        loadContentByLanguage(getResourcesLanguage(language));
    }

    //load all the text according to the language
    private void loadContentByLanguage(Resources resources) {
        btnHome.setText(resources.getString(R.string.string_text_pv__btn_main));
        btnSetLink.setText(resources.getString(R.string.string_sett_link));
        btnBurn.setText(resources.getString(R.string.string_sett_burning));
        btnAbout.setText(resources.getString(R.string.string_sett_About));
        btnHardware.setText(resources.getString(R.string.string_sett_hard));
        btnSystem.setText(resources.getString(R.string.string_sett_system));
        tvSetTitle.setText(resources.getString(R.string.string_sett_title));
        /*tvTextFrq.setText(resources.getString(R.string.string_text_pv_tv_freq));
        tvTextInt.setText(resources.getString(R.string.string_text_pv_tv_int));
        tvTextTime.setText(resources.getString(R.string.string_text_pv_tv_tim));
        tvTitle.setText(resources.getString(R.string.string_name_therapy));
        btnMenu.setText(resources.getString(R.string.string_text_pv__btn_main));
        //dialog with language
        dialogSideRailLang = resources.getString(R.string.string_dial_side_rail);
        dialogTherapyCompleteLang=resources.getString(R.string.string_name_therapy_complete);
        dialogConfirmLang = resources.getString(R.string.string_btn_SR_confirm);
        dialogCancelLang = resources.getString(R.string.string_btn_SR_cancel);*/

    }

    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(SettActivity.this, language);
        resources = context.getResources();
        return resources;
    }

    private void events() {
        btnHome.setOnClickListener(this);
        btnSetLink.setOnClickListener(this);
        btnBurn.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnSystem.setOnClickListener(this);
        btnHardware.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnSetLink) {
            //alertCustomDialog=new AlertCustomDialog(this, this);
            customAlert = new CustomAlert(this, this);
            customAlert.showDialog();
            customAlert.showDialogLink(safety.PASS_QC);
            //launchActivity(ScanActivity.class);
        } else if (v == btnHome) {
            launchActivity(MainActivity.class);
        } else if (v == btnBurn) {
            launchActivity(BurningActivityRutine.class);
        } else if (v == btnAbout) {
            // launchActivity(BurningActivityRutine.class);
            K9Alert k9Alert=new K9Alert(this,this);
            //k9Alert.alertDialogAbout("","", "");

        } else if (v == btnSystem) {
            //launchActivity(UpdateActivity.class);
            appClose();
        } else if(v==btnHardware){
            launchActivity(HardwareActivity.class);
        }
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    @Override
    public void onItemPostSelect(int position, String value) {
        Log.d(TAG, "onItemPostSelect Activity Setting: " + position + "value:" + value);
        if (value.equalsIgnoreCase(navigation.NAV_LINK)) {
            launchActivity(UpdateActivity.class);// launchActivity(ScanActivity.class);
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
            tvSetRev.setText(revision);
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

    @Override
    public void onItemSetupInfo(String name, String description) {

    }

    @Override
    public void onItemSetupAlarm(String name, String description, String location) {

    }

    private void appClose(){
        finish();
        System.exit(0);
    }
}