package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

//import Alert.AlertCustomDialog;
import java.util.Locale;

import Alert.K9Alert;
import Interface.InterfaceSetupInfo;
import Interface.RecyclerViewClickInterface;
import Util.Languages;
import Util.LocaleHelper;
import Util.Rev;
import Util.Status;
import Util.Util_Dialog;
import Util.Validation;
//import Util.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickInterface, InterfaceSetupInfo {
    /*
     * Project : Percussion Vibration
     * Desc: Main Page
     * Rev:3.0.1
     * Prog:Ibrahim
     * Date:09/28/22
     * */


    private static final String TAG = " MainActivity";
    //GUI
    private Button btnMainK9;
    private Button btnMainSett;
    private Button btnMainLock;
    private Button btnMainUnlock;
    private Button btnMainSleep;
    private Button btnMainManual;
    private TextView tvRev;
    private Spinner mLanguage;
    private TextView tvTherapyName;
    private TextView tvStatusScreen;
    ArrayAdapter<String> mAdapter;
    //shared preferences
    public static final String SHARED_PREFS1 = "sharedPrefs";
    //variables to save text
    public static final String BLE_ADD_K9 = "00:00:00:00:00";
    public static final String SERIAL_K9 = "serial";
    //variables to save text
    public static final String BLE_ADD = "text";

    //private String
    private String myBleAdd = "0";
    private String mySerialAdd = "0";
    public String DATA_BLE_ADD = "DATA_BLE_ADD";
    public String DATA_SYSTEM_SERIAL = "DATA_SYSTEM_SERIAL";
    //lock
    private boolean isLockScreen = false;
    //revision
    private Rev rev = new Rev();
    //alert
    private K9Alert k9Alert;
    private Util_Dialog utilDialog = new Util_Dialog();
    //language
    private Resources resourcesLang;
    private Languages languages = new Languages();
    private String language = languages.LANG_EN;
    private int languagePos = 0;
    //Store preferences
    private String KEY_LANGUAGE = languages.LANG_EN;
    private String KEY_LANGUAGE_POS = "0";

    //serial number of the product
    private String Serial_Number_Product = "12PV123456";
    private String KEY_SERIAL_NUMBER = Serial_Number_Product;

    //get system isntalled acording serial number
    private Validation validation = new Validation();
    private int installedSystem = validation.IsUnknown;//can be percussion or percussion/vibration
    //load manual
    // creating a variable
    // for PDF view.
    //PDFView pdfView;
    // url of our PDF file.

    //hide permanent bar
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    private int currentApiVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.layout_main_page);
        //remove menu bar
        hideStatusBar();
        //remove action bar from top
        removeActionBar();
        //init
        initGUI();
        //init other stuff
        initApp();
        //init array for language
        initArrayLang();
        //events btns
        eventsBtn();
        //events spinner language
        eventSpinnerLanguage();
        //get info from others pages
        getExtrasFromAct();//get extras from other activity
        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_10);
        //disable wifi
        disableWIFI();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //get preferences saved before
        getStorePreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Store preferences
        storePreferences();
    }

    private void initGUI() {
        btnMainK9 = findViewById(R.id.btnMainK9);
        btnMainSett = findViewById(R.id.btnMainSet);
        btnMainLock = findViewById(R.id.btnMainLock);
        btnMainUnlock = findViewById(R.id.btnMainUnLock);
        btnMainSleep = findViewById(R.id.btnMainSllep);
        btnMainManual = findViewById(R.id.btnManual);
        tvRev = findViewById(R.id.tvRev);
        mLanguage = (Spinner) findViewById(R.id.spinnerLang);
        tvTherapyName = findViewById(R.id.tvTherapyName);
        tvStatusScreen = findViewById(R.id.tvScreenStatus);
    }

    private boolean initApp() {
        k9Alert = new K9Alert(this, this);
        //init lock
        //btnMainLock.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.drawable.ic_baseline_mobile_friendly_32), null, null);
        btnMainK9.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_mobile_friendly_48));
        // btnMainLock.setText("Unlocked");
        isLockScreen = false;
        return true;
    }

    private void eventsBtn() {
        btnMainK9.setOnClickListener(this);
        btnMainSett.setOnClickListener(this);
        btnMainLock.setOnClickListener(this);
        btnMainUnlock.setOnClickListener(this);
        btnMainSleep.setOnClickListener(this);
        btnMainManual.setOnClickListener(this);
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    //disable WIFi
    private void disableWIFI() {
        try {
            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                if (wifi.isWifiEnabled()) {
                    wifi.setWifiEnabled(false);
                    Log.d(TAG, "disableWIFI: disable");
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "disableWIFI: ex:" + e.getMessage());
        }
    }

    //init array spinner language
    private void initArrayLang() {
        Context context = LocaleHelper.setLocale(MainActivity.this, "en");
        Resources resourcesLang = context.getResources();
        ArrayAdapter mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_option));
        mLanguage.setAdapter(mAdapter);

        //get language
        if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en")) {
            mLanguage.setSelection(mAdapter.getPosition("English"));
        } else if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("de")) {
            mLanguage.setSelection(mAdapter.getPosition("German"));
        } else if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("fr")) {
            mLanguage.setSelection(mAdapter.getPosition("France"));
        } else if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("it")) {
            mLanguage.setSelection(mAdapter.getPosition("Italian"));
        } else {
            mLanguage.setSelection(mAdapter.getPosition("Spanish"));
        }
    }

    // for testing
    private void langChange(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.layout_main_page);

    }

    //load all the text according to the language
    private void loadContentByLanguage(String language) {
        Log.d(TAG, "loadContentByLanguage: lang:" + language);

        Context context1 = LocaleHelper.setLocale(MainActivity.this, language);
        Resources resources1 = context1.getResources();

        if (context1 != null) {

            Log.d(TAG, "loadContentByLanguage: resource1 ok");
        } else {
            Log.d(TAG, "loadContentByLanguage: resource null");
        }


        if (resources1 != null) {

            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* Log.d(TAG, "loadContentByLanguage: " + resources1.getString(R.string.string_text_main_manual));
                        Log.d(TAG, "loadContentByLanguage: " + resources1.getString(R.string.string_text_main_lock));
                        Log.d(TAG, "loadContentByLanguage: " + resources1.getString(R.string.string_text_main_unlock));
                        Log.d(TAG, "loadContentByLanguage: " + resources1.getString(R.string.string_text_main_sleep));
                        Log.d(TAG, "loadContentByLanguage: " + resources1.getString(R.string.string_name_therapy));
                        Log.d(TAG, "loadContentByLanguage: " + resources1.getString(R.string.string_text_action_unlocked));*/
                        //
                        btnMainManual.setText(resources1.getString(R.string.string_text_main_manual));
                        btnMainLock.setText(resources1.getString(R.string.string_text_main_lock));
                        btnMainUnlock.setText(resources1.getString(R.string.string_text_main_unlock));
                        btnMainSleep.setText(resources1.getString(R.string.string_text_main_sleep));
                        //
                        tvStatusScreen.setText(resources1.getString(R.string.string_text_action_unlocked));

                        //check if percussion or Percussion/Vibration
                        if (checkingInstalledSystem(Serial_Number_Product) == validation.IsPercussion) {
                            //load percussion
                            tvTherapyName.setText(resources1.getString(R.string.string_name_percussion_therapy));
                        } else if (checkingInstalledSystem(Serial_Number_Product) == validation.IsPercussionVibration) {
                            //load stuff for percussion/vibration
                            tvTherapyName.setText(resources1.getString(R.string.string_name_therapy));
                        }
                    }
                });

            } catch (Exception e) {
                Log.d(TAG, "loadContentByLanguage: ex" + e.getMessage());
            }


        } else {
            Log.d(TAG, "loadContentByLanguage: null");
        }


    }

    //events language
    private void eventSpinnerLanguage() {
        mLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Context context;
                            Resources resources;
                            switch (i) {
                                case 0:
                                    context = LocaleHelper.setLocale(MainActivity.this, languages.LANG_EN);//english
                                    Log.d(TAG, "run: en");
                                    language = languages.LANG_EN;
                                    languagePos = 0;
                                    //resources = context.getResources();
                                    loadContentByLanguage(language);
                                    break;
                                case 1:
                                    context = LocaleHelper.setLocale(MainActivity.this, languages.LANG_DE);//germany
                                    language = languages.LANG_DE;
                                    languagePos = 1;
                                    //resources = context.getResources();
                                    Log.d(TAG, "run: lang" + language);
                                    loadContentByLanguage(language);
                                    break;
                                case 2:
                                    context = LocaleHelper.setLocale(MainActivity.this, languages.LANG_FR);//fr
                                    language = languages.LANG_FR;
                                    languagePos = 2;
                                    //resources = context.getResources();
                                    Log.d(TAG, "run: lang" + language);
                                    loadContentByLanguage(language);
                                    break;
                                case 3:
                                    context = LocaleHelper.setLocale(MainActivity.this, languages.LANG_IT);//it

                                    language = languages.LANG_IT;
                                    languagePos = 3;
                                    Log.d(TAG, "run: lang" + language);
                                    //resources = context.getResources();
                                    loadContentByLanguage(language);
                                    break;
                                case 4:
                                    context = LocaleHelper.setLocale(MainActivity.this, languages.LANG_ES);//sp
                                    language = languages.LANG_ES;
                                    languagePos = 4;
                                    Log.d(TAG, "run: lang" + language);
                                    //resources = context.getResources();
                                    loadContentByLanguage(language);
                                    break;
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "eventSpinnerLanguage: ex" + e.getMessage());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //shared preferences


    //load preferences

    @Override
    public void onClick(View v) {
        if (v == btnMainK9) {
            if (!isLockScreen) {
                //work with bluetooth
                //launchActivityWithExtras(VibrationPercussionActivity.class, language, Serial_Number_Product, myBleAdd);
                //ethernet
                launchActivityWithExtras(K9PvzEth.class, language, Serial_Number_Product, myBleAdd);
            } else {
                toastMessage();
            }
        } else if (v == btnMainSett) {
            if (!isLockScreen) {
                launchActivityWithExtras(SettActivity.class, language, Serial_Number_Product, "0");
            } else {
                toastMessage();
            }
        } else if (v == btnMainManual) {
            launchActivityWithExtras(ManualActivity.class, language, Serial_Number_Product, "0");
            //launchActivity(ManualActivity.class);
        } else if (v == btnMainSleep) {
            launchActivityWithExtras(SleepActivity.class, language, Serial_Number_Product, "0");
            //launchActivity(SleepActivity.class);
        } else if (v == btnMainLock) {
            //check status first. ned to be unlocked
            if (!isLockScreen) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            Resources resources = getResourcesLanguage(language);
                            //dialog to be display
                            String text = resources.getString(R.string.string_name_lock);
                            //button name with language confirm
                            String nameBtnConfirm = resources.getString(R.string.string_btn_SR_confirm);
                            //button name with language cancel
                            String nameBtnCancel = resources.getString(R.string.string_btn_SR_cancel);
                            //send data to dialog
                            k9Alert.alertDialogLock(text, nameBtnConfirm, nameBtnCancel);
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogLock: ex" + e.getMessage());
                }
            }


        } else if (v == btnMainUnlock) {
            //check status first. ned to be locked
            if (isLockScreen) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            Resources resources = getResourcesLanguage(language);
                            //dialog to be display
                            String text = resources.getString(R.string.string_name_lock);
                            //button name with language confirm
                            String nameBtnConfirm = resources.getString(R.string.string_btn_SR_confirm);
                            //button name with language cancel
                            String nameBtnCancel = resources.getString(R.string.string_btn_SR_cancel);
                            //send data to dialog
                            k9Alert.alertDialogLock(text, nameBtnConfirm, nameBtnCancel);
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogLock: ex" + e.getMessage());
                }
            }

        }
    }

    //get extras from activity
    private void getExtrasFromAct() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //String add = bundle.getString("myAdd");
            String add = "KZ";
            String serial = "00:00:00:00:00";

            try {
                if (DATA_BLE_ADD != null) {
                    add = bundle.getString(DATA_BLE_ADD);
                    //check if the address changed
                    if (add.equalsIgnoreCase(myBleAdd)) {
                        Log.d(TAG, "getExtrasFromAct: same address");
                    } else {
                        Log.d(TAG, "getExtrasFromAct: address change");
                        myBleAdd = add;
                        mySerialAdd = serial;
                        saveData();
                    }
                }
                //get serial number
                if (DATA_SYSTEM_SERIAL != null) {
                    Serial_Number_Product = bundle.getString(DATA_SYSTEM_SERIAL);
                    Log.d(TAG, "getExtrasFromAct: " + bundle.getString(DATA_SYSTEM_SERIAL));
                    // Log.d(TAG, "getExtrasFromAct: " + add + ".serial:" + serial);
                    storePreferences();
                    //

                }
            } catch (Exception e) {
                Log.d(TAG, "getExtrasFromAct: ex " + e.getMessage());
            }


        }
    }

    //check installed system
    private int checkingInstalledSystem(String value) {
        if (value != null) {
            return validation.validateIsPV(value);
        }
        return 0;
    }


    //pass bluettoth address to vibration activity
    private void passBleAddToVib(String add) {
        try {
            if (add != null) {
                Bundle bundle = new Bundle();
                Log.d(TAG, "passBleAddToVib " + add);
                bundle.putString("vibAdd", add);//send bluetooth address
                bundle.putString("language", language);//send bluetooth address
                bundle.putString("serial_number", Serial_Number_Product);//send language
                Intent intent = new Intent(MainActivity.this, VibrationPercussionActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d(TAG, "passBleAddToVib: exception" + e.getMessage());
        }
    }

    //launch activity with extra
    private void launchActivityWithExtras(Class myclass, String language, String serialNumber, String bluetoothAdd) {
        try {
            if (language != null) {
                Bundle bundle = new Bundle();
                //
                bundle.putString("vibAdd", bluetoothAdd);//send bluetooth address
                bundle.putString("language", language);//send language
                bundle.putString("serial_number", serialNumber);//send language
                //
                Intent intent = new Intent(MainActivity.this, myclass);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d(TAG, "launchActWithExtras: exception" + e.getMessage());
        }
    }

    //Method. to take care of save data
    public void saveData() {
        //shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);//mode private means no other app can change it
        SharedPreferences.Editor editor = sharedPreferences.edit();//enable to change the value
        editor.putString(BLE_ADD_K9, myBleAdd);//get the text from the editText
        editor.putString(SERIAL_K9, mySerialAdd);//get the text from the editText
        Log.d(TAG, "saveData: serial number" + mySerialAdd);
        editor.apply();
        //Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
    }

    //Method to load the data
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);//mode private means no other app can change it
        myBleAdd = sharedPreferences.getString(BLE_ADD, "");
        mySerialAdd = sharedPreferences.getString(SERIAL_K9, "");
        Log.d(TAG, "loadData: serialNumber:" + mySerialAdd);
    }

    @Override
    public void onItemPostSelect(int position, String value) {
        Log.d(TAG, "onItemPostSelect: pos:" + position + ".value:" + value);
        //get the serial number of the product
        Status status = new Status();
        if (position == status.STATUS_SERIAL_LINK) {
            Serial_Number_Product = value;
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
            tvRev.setText(revision);
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


    //Hidden status bar
    private void hideStatusBar() {
        try {
            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            Log.d(TAG, "onSystemUiVisibilityChange: "+visibility);
                            // Note that system bars will only be "visible" if none of the
                            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                                // TODO: The system bars are visible. Make any desired
                                // adjustments to your UI, such as showing the action bar or
                                // other navigational controls.
                            } else {
                                // TODO: The system bars are NOT visible. Make any desired
                                // adjustments to your UI, such as hiding the action bar or
                                // other navigational controls.
                            }
                        }
                    });



           /* View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            actionBar.hide();*/

            /*getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);*/


        } catch (Exception e) {
            Log.d(TAG, "removeMenuBar: ex:" + e.getMessage());
        }
    }

    @Override
    public void onItemSetupInfo(String name, String description) {
        if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_LOCK)) {

            setLockScreen(getResourcesLanguage(language));
        }
    }

    @Override
    public void onItemSetupAlarm(String name, String description, String location) {

    }

    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(MainActivity.this, language);
        resources = context.getResources();
        return resources;
    }

    //lock/unlock
    private void setLockScreen(Resources resources) {
        try {
            if (!isLockScreen) {
                Log.d(TAG, "setLockScreen: lock screen" + isLockScreen);
                // btnMainLock.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.drawable.ic_baseline_phonelink_lock_32), null, null);
                // btnMainLock.setText(resources.getString(R.string.string_text_action_locked));

                isLockScreen = true;
                displayLock(isLockScreen);
                return;
            } else if (isLockScreen) {
                Log.d(TAG, "setLockScreen: unlock screen" + isLockScreen);
                // btnMainLock.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.drawable.ic_baseline_mobile_friendly_32), null, null);
                // btnMainLock.setText(resources.getString(R.string.string_text_action_unlocked));
                isLockScreen = false;
                displayLock(isLockScreen);
                return;
            }

        } catch (Exception e) {
            Log.d(TAG, "setLockScreen: ex:" + e.getMessage());
        }
    }

    //
    private void toastMessage() {
        try {
            Toast.makeText(getApplicationContext(), "Unlock the screen", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "toastMessage: ex:" + e.getMessage());
        }

    }

    //show lock icon on the start button
    private void displayLock(boolean input) {
        Resources resources = getResourcesLanguage(language);

        if (input) {
            btnMainK9.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_phonelink_lock_48));
            displayStatusScreen(resources.getString(R.string.string_text_action_locked));
        } else {
            btnMainK9.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_mobile_friendly_48));

            displayStatusScreen(resources.getString(R.string.string_text_action_unlocked));
        }
    }

    //save   preferences
    private void storePreferences() {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from spinner language
        myEdit.putString(KEY_LANGUAGE, language);
        //Store position of the spinner
        myEdit.putInt(KEY_LANGUAGE_POS, languagePos);
        //store serial number of the product
        myEdit.putString(KEY_SERIAL_NUMBER, Serial_Number_Product);

        Log.d(TAG, "storePreferences: language" + language);
        Log.d(TAG, "storePreferences: language Position" + languagePos);
        Log.d(TAG, "storePreferences: serial number" + Serial_Number_Product);

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
    }

    //load  preferences
    private void getStorePreferences() {
        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        //get language
        String s1 = sh.getString(KEY_LANGUAGE, "en");
        //get language position on the spinner
        int a = sh.getInt(KEY_LANGUAGE_POS, 0);
        //get serial number
        String serialNumber = sh.getString(KEY_SERIAL_NUMBER, "12PV123456");

        //
        Log.d(TAG, "getStorePreferences:language " + s1);
        Log.d(TAG, "getStorePreferences:language Pos: " + a);
        //int a = sh.getInt("age", 0);

        // We can then use the data
        if (language != null) {
            language = s1;
            //update spinner language
            loadValueSpinnerLang(a);
        }
        //
        if (serialNumber != null) {
            Serial_Number_Product = serialNumber;
            Log.d(TAG, "getStorePreferences: Serial number:" + Serial_Number_Product);
        }

    }

    //load preferences
    private void loadValueSpinnerLang(int input) {
        //
        Log.d(TAG, "loadValueSpinnerLang: " + input);
        mLanguage.setSelection(input);
    }

    //display status screen
    private void displayStatusScreen(String status) {
        if (status != null) {
            tvStatusScreen.setText(status);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: ");
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}