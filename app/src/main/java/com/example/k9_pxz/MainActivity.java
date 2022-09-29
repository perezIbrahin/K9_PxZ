package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

//import Alert.AlertCustomDialog;
import Alert.CustomAlert;
import Alert.K9Alert;
import Interface.InterfaceSetupInfo;
import Interface.RecyclerViewClickInterface;
import Util.Rev;
import Util.Util_Dialog;
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
    private Button btnMainSleep;
    private TextView tvRev;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.layout_main_page);
        //remove menu bar
        removeMenuBar();
        //remove action bar from top
        removeActionBar();
        //init
        initGUI();
        //init other stuff
        initApp();
        //events btns
        eventsBtn();
        //get info from others pages
        getExtrasFromAct();//get extras from other activity
        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_10);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData();
    }

    private void initGUI() {
        btnMainK9 = findViewById(R.id.btnMainK9);
        btnMainSett = findViewById(R.id.btnMainSet);
        btnMainLock = findViewById(R.id.btnMainLock);
        btnMainSleep = findViewById(R.id.btnMainSllep);
        tvRev = findViewById(R.id.tvRev);
    }

    private boolean initApp() {
        k9Alert = new K9Alert(this, this);
        //init lock
        btnMainLock.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.drawable.ic_baseline_lock_open_24), null, null);
        btnMainLock.setText("Unlocked");
        isLockScreen = false;

        return true;
    }

    private void eventsBtn() {
        btnMainK9.setOnClickListener(this);
        btnMainSett.setOnClickListener(this);
        btnMainLock.setOnClickListener(this);
        btnMainSleep.setOnClickListener(this);
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnMainK9) {
            if (!isLockScreen) {
                passBleAddToVib(myBleAdd);
            }else{
                toastMessage();
            }
        } else if (v == btnMainSett) {
            if (!isLockScreen) {
                launchActivity(SettActivity.class);
            }else{
                toastMessage();
            }

        } /*else if (v == btnMainInfo) {
            CustomAlert customAlert = new CustomAlert(this, this);
            customAlert.showAlertInfo(mySerialAdd);

            //launchActivity(InfoActivity.class);
        } */ else if (v == btnMainSleep) {
            launchActivity(SleepActivity.class);
        } else if (v == btnMainLock) {

            try {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        k9Alert.alertDialogLock(utilDialog.LOCATION_CONFIRM_LOCK);
                    }
                });

            } catch (Exception e) {
                Log.d(TAG, "alertDialogLock: ex" + e.getMessage());
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

                    if (add.equalsIgnoreCase(myBleAdd)) {
                        Log.d(TAG, "getExtrasFromAct: same address");
                    } else {
                        Log.d(TAG, "getExtrasFromAct: address change");
                        myBleAdd = add;
                        mySerialAdd = serial;
                        saveData();
                    }
                }
                if (DATA_SYSTEM_SERIAL != null) {
                    serial = bundle.getString(DATA_SYSTEM_SERIAL);
                    Log.d(TAG, "getExtrasFromAct: " + add + ".serial:" + serial);
                }


            } catch (Exception e) {
                Log.d(TAG, "getExtrasFromAct: " + e.getMessage());
            }


        }
    }

    //pass bluettoth address to vibration activity
    private void passBleAddToVib(String add) {
        try {
            if (add != null) {
                Bundle bundle = new Bundle();
                Log.d(TAG, "passBleAddToVib " + add);
                bundle.putString("vibAdd", add);
                Intent intent = new Intent(MainActivity.this, VibrationPercussionActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d(TAG, "passBleAddToVib: exception" + e.getMessage());
        }
    }

    //Method. to take care of save data
    public void saveData() {
        //shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);//mode private means no other app can change it
        SharedPreferences.Editor editor = sharedPreferences.edit();//enable to change the value
        editor.putString(BLE_ADD_K9, myBleAdd);//get the text from the editText
        editor.putString(SERIAL_K9, mySerialAdd);//get the text from the editText
        editor.apply();
        //Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
    }

    //Method to load the data
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);//mode private means no other app can change it
        myBleAdd = sharedPreferences.getString(BLE_ADD, "");
        mySerialAdd = sharedPreferences.getString(SERIAL_K9, "");
    }

    @Override
    public void onItemPostSelect(int position, String value) {

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

    //remove menu bar
    private void removeMenuBar() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);


        } catch (Exception e) {
            Log.d(TAG, "removeMenuBar: ex:" + e.getMessage());
        }
    }

    @Override
    public void onItemSetupInfo(String name, String description) {
        if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_LOCK)) {
            setLockScreen();
        }
    }

    @Override
    public void onItemSetupAlarm(String name, String description, String location) {

    }

    //lock/unlock
    private void setLockScreen(){
        try {
            if (!isLockScreen) {
                Log.d(TAG, "setLockScreen: lock screen" + isLockScreen);
                btnMainLock.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.drawable.ic_baseline_lock_24), null, null);
                btnMainLock.setText("Locked");
                isLockScreen = true;
                return;
            } else if (isLockScreen) {
                Log.d(TAG, "setLockScreen: unlock screen" + isLockScreen);
                btnMainLock.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.drawable.ic_baseline_lock_open_24), null, null);
                btnMainLock.setText("Unlocked");
                isLockScreen = false;
                return;
            }

        }catch (Exception e){
            Log.d(TAG, "setLockScreen: ex:"+e.getMessage());
        }
    }

    //
    private void toastMessage(){
        try {
            Toast.makeText(getApplicationContext(), "Unlock the screen", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d(TAG, "toastMessage: ex:"+e.getMessage());
        }

    }
}