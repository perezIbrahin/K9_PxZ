package com.example.k9_pxz;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import Adapter.RecyclerViewAdaptBtnF;
import Adapter.RecyclerViewAdaptBtnI;
import Adapter.RecyclerViewAdaptBtnT;
import Adapter.RecyclerViewAdaptRbA;
import Adapter.RecyclerViewAdaptRbB;
import Adapter.RecyclerViewAdapticonA;
import Adapter.RecyclerViewAdapticonB;
import Alert.Alarm;
import Alert.K9Alert;
import Bluetooth.ActionGatt;
import Bluetooth.BLE_Format;
import Bluetooth.BleCharacteristics;
import Bluetooth.Ble_Protocol;
import Bluetooth.UUID;
import Interface.InterfaceSetupInfo;
import Interface.RecyclerViewClickInterface;
import Model.ModelBtn;
import Util.Beep;
import Util.Bitwise;
import Util.ConcatDataWriteBle;
import Util.ControlGUI;
import Util.Cooling;
import Util.Default_values;
import Util.DisplayOperations;
import Util.IntToArray;
import Util.Key_Util;
import Util.LocaleHelper;
import Util.RecyclerLocations;
import Util.Rev;
import Util.SetPoints;
import Util.Status;
import Util.TagRefrence;
import Util.TextSize;
import Util.Util_Dialog;
import Util.Util_timer;
import Util.Validation;

public class VibrationPercussionActivity extends AppCompatActivity implements View.OnClickListener, InterfaceSetupInfo, RecyclerViewClickInterface {
    private static final String TAG = "VibrationPercussionActi";
    /*
     * Project : Percussion Vibration
     * Desc: Percussion /Vibration
     * Rev:3.0.1
     * Prog:Ibrahim
     * Date:09/28/22
     * */
    //Bluetooth
    private String myBleAdd = "00:00:00:00:00";//Address used to connect BLE
    private boolean bleIsScanner = false;

    private final String KZ_BLE_ADDRESS = "BC:33:AC:CB:F6:9D";
    //private static final UUID KZ_SERVICE_UUID =UUID_SERVICE_COMMANDS;
    //button command
    //modes
    private Button btnSelectPer;
    private Button btnSelectVib;
    private Button btnSelectTotalPer;
    private Button btnSelectTotalVib;
    //
    private Button btnStart;
    private Button btnStop;
    //side rail
    private Button btnSr1;
    private Button btnSr2;
    private Button btnSr3;
    private Button btnSr4;
    private Button btnReady;
    private ImageView ivBle;
    private ImageView ivIconPerc;
    private ImageView ivIconVib;
    private Button btnMenu;
    private TextView tvTitle;
    private TextView tvOperation;
    private TextView tvTimer;
    private TextView tvCon;
    private TextView tvRev;
    private TextView tvPresStart;
    private TextView tvTextFrq;
    private TextView tvTextInt;
    private TextView tvTextTime;
    private TextView tvCurrent;
    //Adapter Frequency
    private RecyclerView recyclerViewF;
    private RecyclerView recyclerViewI;
    private RecyclerView recyclerViewT;
    private RecyclerView recyclerViewRbA;
    private RecyclerView recyclerViewRbB;
    private RecyclerView recyclerViewIconA;
    private RecyclerView recyclerViewIconB;
    //Adapter
    private RecyclerViewAdaptBtnF recyclerViewAdaptBtnF = new RecyclerViewAdaptBtnF();
    private RecyclerViewAdaptBtnI recyclerViewAdaptBtnI = new RecyclerViewAdaptBtnI();
    private RecyclerViewAdaptBtnT recyclerViewAdaptBtnT = new RecyclerViewAdaptBtnT();
    private RecyclerViewAdaptRbA recyclerViewAdaptRbA = new RecyclerViewAdaptRbA();
    private RecyclerViewAdaptRbB recyclerViewAdaptRbB = new RecyclerViewAdaptRbB();
    private RecyclerViewAdapticonA recyclerViewAdaptIconA = new RecyclerViewAdapticonA();
    private RecyclerViewAdapticonB recyclerViewAdaptIconB = new RecyclerViewAdapticonB();
    //arraylist-Recycler View Adapter
    private ArrayList<ModelBtn> modelBtnFreqArrayList = new ArrayList<>();
    private ArrayList<ModelBtn> modelBtnIntArrayList = new ArrayList<>();
    private ArrayList<ModelBtn> modelBtnTimeArrayList = new ArrayList<>();
    private ArrayList<ModelBtn> modelBtnArrayListA = new ArrayList<>();
    private ArrayList<ModelBtn> modelBtnArrayListB = new ArrayList<>();
    private ArrayList<ModelBtn> modelIconArrayListA = new ArrayList<>();
    private ArrayList<ModelBtn> modelIconArrayListB = new ArrayList<>();
    //
    private TextSize textSize = new TextSize();
    private ControlGUI controlGUI = new ControlGUI();
    private Beep beep = new Beep();
    private SetPoints setPointsBluetooth = new SetPoints();
    private Default_values default_values = new Default_values();
    private Key_Util keyUtil = new Key_Util();
    private ActionGatt actionGatt = new ActionGatt();
    private UUID uuid = new UUID();
    List<java.util.UUID> characteristicUUIDsList = new ArrayList<java.util.UUID>();
    private BleCharacteristics bleChar = new BleCharacteristics();
    ConcatDataWriteBle concatDataWriteBle = new ConcatDataWriteBle();//concat register and value and send it as 16bits
    IntToArray intToArray = new IntToArray();//convert int to byte array
    RecyclerLocations recyclerLocations = new RecyclerLocations();
    private Ble_Protocol bleProtocol = new Ble_Protocol();//is a prefix to use for describe to the mcu that the value is from frequency o intensity...
    private Util_Dialog utilDialog = new Util_Dialog();
    private Util.Message message = new Util.Message();
    private TagRefrence mTagReference = new TagRefrence();
    private Util_timer utilTimer = new Util_timer();
    //Bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mGatt;
    BluetoothManager bluetoothManager;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothLeScanner mBLEScanner;
    private BluetoothDevice mDevice;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private byte[] arrayDatafromBle;
    //others
    private K9Alert k9Alert;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    //SendData BleKz=new SendData();

    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    //timers
    //countdown
    CountDownTimer timerCheckNetwork = null;
    private CountDownTimer timerTherapy = null;
    CountDownTimer timerForceSop = null;
    CountDownTimer timerLockColdDown = null;
    //
    private Status status = new Status();

    //revision
    private Rev rev = new Rev();

    //
    private int counterFail = 0;
    private int MAX_CON = 3;

    //Bluetooth actions
    public final static String ACTION_GATT_CONNECTED =
            "android.bluetooth.device.action.ACL_CONNECTED";// "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
    public final static String ACTION_GATT_DISCONNECTED =
            "android.bluetooth.device.action.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "android.bluetooth.device.action.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "android.bluetooth.device.action.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "android.bluetooth.device.action.EXTRA_DATA";
    public final static String ACTION_STATE_CLOSED =
            "android.bluetooth.device.action.ACTION_STATE_CLOSED";

    //variables
    private String BLE_BED = keyUtil.DEFAULT_ADDRESS;
    private int connectionState = actionGatt.STATE_DISCONNECTED;
    private boolean mConnected = false;
    private boolean mTimerEnabled = false;
    private boolean mTimerMainEnabled = false;
    private boolean connected;
    private boolean IsReadyToNot = false;
    private int statusBleDiscovery = 0;
    private int oldCommand = 0;
    //Pending BLE write and read
    private boolean isReadingNow = false;
    private boolean isPendingWrite = false;
    private boolean isFeedbackEnable = true;//true if wants feedback
    //update display
    private final static int UPDATE_DEVICE = 0;
    private final static int UPDATE_VALUE = 1;
    private final static int UPDATE_CLOUD = 2;
    private final static int UPDATE_MCU = 3;
    String value = null;

    //
    private boolean isLockMode = false;
    private int mode = 0;
    private int mProtocol = 0;
    private int mSp = 0;
    //therapy
    private boolean isTherapyOn = false;
    //get conditions to start therapy
    private boolean flagIsFreq = false;
    private boolean flagIsInt = false;
    private boolean flagIsTim = false;
    private boolean flagIsTRA = false;
    private boolean flagIsTRB = false;
    private boolean isFlagIsSr = false;
    private boolean flagIsStartReady = false;
    private Boolean flagIsMinZero = false;
    private boolean isFlagSetConnection = false;
    //
    private boolean flagConnSuc = false;
    //storage memory for timer
    //get value ofTimer
    private int memMin = 9;
    private int min = 9;
    private int sec = 59;
    //Timer therapy
    private String strMin = "00";
    private String strSec = "00";
    private String strDiv = ":";
    private long countInterval = 1000;
    private long valueTimerTherapy = 0;
    //get current transducer
    private int old_transdA = 0;
    private int old_transdB = 0;
    private int new_transdA = 0;
    private int new_transdB = 0;

    //
    private boolean isFlagTimerElapsed = false;
    private int memoryLastTimer = 0;

    int module = 0;//
    int moduleA = 0;
    int moduleB = 0;

    //Language
    private String language = "en";
    private Resources resources;
    //Dialog for text alert
    private String dialogSideRailLang = "0";
    private String dialogEmergStop = "0";
    private String dialogHardwareFail = "0";
    private String dialogTherapyCompleteLang = "0";
    private String dialogConfirmLang = "0";
    private String dialogCancelLang = "0";
    //dialog missing parameteres
    private String dialogMissTitle = "0";
    private String dialogMissFreq = "0";
    private String dialogMissInt = "0";
    private String dialogMissTime = "0";
    private String dialogMissTra = "0";
    private String dialogMissTrb = "0";

    //serial number
    private String Serial_Number_Product = "12PV123456";
    //get type of therapy
    private int typeOfTherapy = 0;
    private Validation validation = new Validation();
    private String serialNumber = "0";

    //alarm
    private boolean isAlarm = true;
    private long TIMER_WACHT_DOG = 20000;

    //wachtDog
    private CountDownTimer watchDogTimer;

    //Display operations
    private DisplayOperations displayOperations = new DisplayOperations();

    private boolean isEnableGui = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.layout_percussion_vibration);
        //remove menu bar
        removeMenuBar();
        //remove action bar from top
        removeActionBar();
        //init all components
        initGUI();
        //init bluetooth and broadcast
        initOther();
        //init paremetriz for app
        initApp();
        //init language
        initLang();

        //buttons events
        eventBtn();
        //loading alert dialog
        alertDialogLoading(true);
        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_11);
        //testing
        testing();
        //disable wifi
        disableWIFI();


    }

    //loading dialog
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //bluetooth scan
        launchBleScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //cancel scan
        cancelBleScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        close();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        closeNotifications();
        super.onDestroy();
        close();
        unregisterReceiver(mBroadcastReceiver);
    }

    //close all notification
    private void closeNotifications() {
        //customBluetooth.closeBluetooth(mGatt);//03/31/22
        /*
        //works great
        if (mGatt != null) {
            //mGatt.setCharacteristicNotification(bleCharacteristics.mCHARACTERISTIC_A10_ORIENTATION, false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return;
            }
            mGatt.close();
            mGatt.disconnect();
        }*/

    }

    //get Extras from Intent
    private boolean getExtraIntent() {
        Log.d(TAG, "getExtraIntent: ");
        Intent dataIntent = getIntent();
        //  Key_Util keyUtil = new Key_Util();
        if (keyUtil.KEY_MESSAGE_FROM_MAIN != null) {
            if (dataIntent.getExtras() != null) {
                String str = dataIntent.getStringExtra(keyUtil.KEY_MESSAGE_FROM_MAIN);
                Log.d(TAG, "getExtraIntent: str " + str);
                if (str != null) {
                    BLE_BED = str;
                    if (!BLE_BED.equalsIgnoreCase(keyUtil.DEFAULT_ADDRESS)) {
                        BLE_BED = dataIntent.getStringExtra(keyUtil.KEY_ADDRESS_BED);
                        Log.d(TAG, "getExtraIntent: BLE_BED: " + BLE_BED);
                    }
                }
            }
        }
        return true;
    }

    //init GUI
    private void initGUI() {
        btnSelectPer = findViewById(R.id.btnSelPerc);
        btnSelectVib = findViewById(R.id.btnSelVib);
        btnSelectTotalPer = findViewById(R.id.btnTotalPerc);
        btnSelectTotalVib = findViewById(R.id.btnTotalVib);

        btnMenu = findViewById(R.id.btnMenu);
        btnStart = findViewById(R.id.btnModeStart);
        btnStop = findViewById(R.id.btnModeStop);
        btnSr1 = findViewById(R.id.sr1);
        btnSr2 = findViewById(R.id.sr2);
        btnSr3 = findViewById(R.id.sr3);
        btnSr4 = findViewById(R.id.sr4);
        btnReady = findViewById(R.id.btnReady);
        ivBle = findViewById(R.id.ivBle);
        ivIconPerc = findViewById(R.id.ivIconPerc);
        ivIconVib = findViewById(R.id.ivIconVib);
        tvOperation = findViewById(R.id.tvOpe);
        tvTimer = findViewById(R.id.tvtimer);
        tvCon = findViewById(R.id.tvCon);
        tvRev = findViewById(R.id.tvVibRev);
        tvPresStart = findViewById(R.id.tvReady);
        //
        tvTextFrq = findViewById(R.id.tvTextFreq);
        tvTextInt = findViewById(R.id.tvtextInt);
        tvTextTime = findViewById(R.id.tvTextTime);
        //
        tvTitle = findViewById(R.id.tvTextPvTitile);
        tvCurrent = findViewById(R.id.tvCurrent);
    }

    //init system
    private boolean initApp() {
        k9Alert = new K9Alert(this, this);
        myBleAdd = getExtrasFromAct(myBleAdd);
        updateBtnReady(controlGUI.POS0);

/*
        //load view
        updateButtonsFrequencyF(setPointsBluetooth.INT_BLE_SP_FREQ1);//frequency
        updateButtonsIntensity(setPointsBluetooth.INT_BLE_SP_INT2);//intensity
        updateButtonsTime(setPointsBluetooth.INT_BLE_SP_TIME3);//time
        //
        updateButtonsRbA(setPointsBluetooth.INT_BLE_SP_TRA2);//selection transducer position A
        updateButtonsRbB(setPointsBluetooth.INT_BLE_SP_TRB4);//selection transducer position A

        //

        //
        updateCommand(setPointsBluetooth.INT_BLE_CMD_STOP);
        //
       */
        return true;
    }

    //Init all
    private boolean initOther() {
        //get data from Main
        if (getExtraIntent()) {
            Log.d(TAG, "Extras from Main: ");
            //Init BLE
            initBLE();
            //Bluetooth permissions
            checkBluetoothPermissions();
            //Bluet[ooth broadcast receiver
            broadCastReceiver();
            //
            launchBleScan();
        }
        return true;
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
    private void initLang() {
        Log.d(TAG, "initLang: language:" + language);
        //Context context = LocaleHelper.setLocale(VibrationPercussionActivity.this, language);
        //resources = context.getResources();
        loadContentByLanguage(getResourcesLanguage(language));

    }

    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(VibrationPercussionActivity.this, language);
        resources = context.getResources();
        return resources;
    }

    //load all the text according to the language
    private void loadContentByLanguage(Resources resources) {
        btnStart.setText(resources.getString(R.string.btnStart));
        btnStop.setText(resources.getString(R.string.btnStop));

        tvPresStart.setText(resources.getString(R.string.string_text_pres_start));
        tvTextFrq.setText(resources.getString(R.string.string_text_pv_tv_freq));
        tvTextInt.setText(resources.getString(R.string.string_text_pv_tv_int));
        tvTextTime.setText(resources.getString(R.string.string_text_pv_tv_tim));

        btnMenu.setText(resources.getString(R.string.string_text_pv__btn_main));
        //dialog with language
        dialogSideRailLang = resources.getString(R.string.string_dial_side_rail);
        dialogEmergStop = resources.getString(R.string.string_alarm_emergency_stop);
        dialogHardwareFail = resources.getString(R.string.string_alarm_hardware_fail);
        dialogTherapyCompleteLang = resources.getString(R.string.string_name_therapy_complete);
        dialogConfirmLang = resources.getString(R.string.string_btn_SR_confirm);
        dialogCancelLang = resources.getString(R.string.string_btn_SR_cancel);
        //dialog missing components
        dialogMissTitle = resources.getString(R.string.string_dial_configuration);
        dialogMissFreq = resources.getString(R.string.string_text_check_freq);
        dialogMissInt = resources.getString(R.string.string_text_check_int);
        dialogMissTime = resources.getString(R.string.string_text_check_tim);
        dialogMissTra = resources.getString(R.string.string_text_check_transdA);
        dialogMissTrb = resources.getString(R.string.string_text_check_transdB);

        //check if percussion or Percussion/Vibration
        typeOfTherapy = selectTypeOfTherapy(resources, Serial_Number_Product);
    }

    //type of therapy
    private int selectTypeOfTherapy(Resources resources, String value) {
        if (checkingInstalledSystem(Serial_Number_Product) == validation.IsPercussion) {
            tvTitle.setText(resources.getString(R.string.string_name_percussion_therapy));
            btnSelectPer.setText(resources.getString(R.string.string_name_select_perc));
            //
            btnSelectVib.setVisibility(View.INVISIBLE);
            ivIconVib.setVisibility(View.INVISIBLE);
            return validation.IsPercussion;
        } else if (checkingInstalledSystem(Serial_Number_Product) == validation.IsPercussionVibration) {
            btnSelectVib.setVisibility(View.VISIBLE);
            ivIconVib.setVisibility(View.VISIBLE);
            //
            tvTitle.setText(resources.getString(R.string.string_name_therapy));
            btnSelectPer.setText(resources.getString(R.string.string_name_select_perc));
            btnSelectVib.setText(resources.getString(R.string.string_name_select_vib));
            return validation.IsPercussionVibration;
        } else {

        }
        return validation.IsUnknown;
    }


    //close connection
    public void close() {
        if (mGatt == null) {
            return;
        }
        closeBroadcast();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Log.d(TAG, "close:ActivityCompat#requestPermissions ");
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        mGatt.close();
        mGatt = null;
        //stop notifications
        //customBluetooth.enableBluetoothNotifications(mGatt, bleChar.mCHARAC_K750_CMD, false);
        //close connection
        //customBluetooth.closeBluetooth(mGatt);

        /*
        if (mGatt == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        //bleChar.mCHARAC_K750_CMD
        mGatt.setCharacteristicNotification(bleChar.mCHARAC_K750_CMD, false);

        mGatt.close();
        mGatt = null;*/
    }

    //testing
    private void testing() {
        // k9Alert.alertDialogTherapyDone(utilDialog.THERAPY_DONE);

        //k9Alert.alertDialogConnectionFail("s");
    }

    //events
    private void eventBtn() {
        btnSelectPer.setOnClickListener(this);
        btnSelectVib.setOnClickListener(this);
        btnSelectTotalPer.setOnClickListener(this);
        btnSelectTotalVib.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSr1.setOnClickListener(this);
        btnSr2.setOnClickListener(this);
        btnSr3.setOnClickListener(this);
        btnSr4.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        ivBle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (btnStart == v) {
            if (!isAlarm) {
                condStartTherapy(flagIsFreq, flagIsInt, flagIsTim, flagIsTRA, flagIsTRB, isFlagIsSr);
            } else {
                Log.d(TAG, "onClick: alarm enable");
            }
            //beep.beep_key();

        } else if (btnStop == v) {
            stopTherapy();
        } else if (btnMenu == v) {
            goHome();
        } else if (ivBle == v) {
            Log.d(TAG, "onClick: ble manual connection");
            if (!isFlagSetConnection) {
                setConnetion();
            }
        } else if (btnSelectPer == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    mode = selectMode(status.SELECT_MODE_PERCUSSION);
                }
            }
        } else if (btnSelectVib == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    mode = selectMode(status.SELECT_MODE_VIBRATION);
                }

            }
        } else if (btnSelectTotalPer == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    totalPercussion();
                    //mode = selectMode(status.SELECT_MODE_TOTAL_PERCUSSION);
                }

            }
        } else if (btnSelectTotalVib == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    totalVibration();
                    //mode = selectMode(status.SELECT_MODE_TOTAL_VIBRATION);
                }

            }
        }
    }

    //event when is selected percussion
    private int selectMode(int mode) {
        if (mode == status.SELECT_MODE_PERCUSSION) {
            //reset flag selected transd
            flagIsTRA = false;
            flagIsTRB = false;
            //clean array list
            resetCheckBockA();
            resetCheckBockB();
            //
            updateButtonsRbAPercussion(setPointsBluetooth.INT_BLE_SP_TRA_NONE);
            updateButtonsRbBPercussion(setPointsBluetooth.INT_BLE_SP_TRB_NONE);
            updateMode(mode);
            return status.SELECT_MODE_PERCUSSION;

        } else if (mode == status.SELECT_MODE_VIBRATION) {
            //reset flag selected transd
            flagIsTRA = false;
            flagIsTRB = false;
            resetCheckBockA();
            resetCheckBockB();
            updateButtonsRbA(setPointsBluetooth.INT_BLE_SP_TRA_NONE);//selection transducer position A
            updateButtonsRbB(setPointsBluetooth.INT_BLE_SP_TRB_NONE);//selection transducer position A

            updateMode(mode);
            return status.SELECT_MODE_VIBRATION;
        } else if (mode == status.SELECT_MODE_TOTAL_VIBRATION) {
            //reset flag selected transd
            flagIsTRA = false;
            flagIsTRB = false;
            resetCheckBockA();
            resetCheckBockB();
            updateButtonsRbA(setPointsBluetooth.INT_BLE_SP_TRA_NONE);//selection transducer position A
            updateButtonsRbB(setPointsBluetooth.INT_BLE_SP_TRB_NONE);//selection transducer position A
            updateMode(mode);
            //totalVibration();
            return status.SELECT_MODE_TOTAL_VIBRATION;
        } else if (mode == status.SELECT_MODE_TOTAL_PERCUSSION) {
            //reset flag selected transd
            flagIsTRA = false;
            flagIsTRB = false;
            resetCheckBockA();
            resetCheckBockB();
            updateButtonsRbAPercussion(setPointsBluetooth.INT_BLE_SP_TRA_NONE);
            updateButtonsRbBPercussion(setPointsBluetooth.INT_BLE_SP_TRB_NONE);
            updateMode(mode);
            //totalPercussion();
            return status.SELECT_MODE_TOTAL_PERCUSSION;
        }


        return 0;
    }

    //mode percussion

    //mode vibration
    //clean modes-A
    private void resetCheckBockA() {
        modelBtnArrayListA.clear();
        modelIconArrayListA.clear();
        //reset flag
    }

    //clean modes-B
    private void resetCheckBockB() {
        modelBtnArrayListB.clear();
        modelIconArrayListB.clear();
        //reset flag
    }

    //check installed system
    private int checkingInstalledSystem(String value) {
        if (value != null) {
            return validation.validateIsPV(value);
        }
        return 0;
    }

    //launch home activity
    private void goHome() {
        if (isTherapyOn) {
            k9Alert.alertDialogLivePage("Alert");
        } else {
            launchActivity(MainActivity.class);
        }
    }

    //change activity
    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    //modify the top bar
    private void modifyTopBar() {
        try {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove top bar
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.blueGreyDark));
            }
        } catch (Exception e) {
            Log.d(TAG, "removeTopBar: ");
        }
    }

    //get the address to connect bluetooth and language
    private String getExtrasFromAct(String oldAddress) {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null || oldAddress == null) {
                //get bluetooth address
                String add = bundle.getString("vibAdd");
                //get language
                language = bundle.getString("language");
                //get type of therapy
                Serial_Number_Product = bundle.getString("serial_number");
                Log.d(TAG, "getExtrasFromAct: serial_number:" + Serial_Number_Product);

                if (add != null) {
                    Log.d(TAG, "Vibration getExtrasFromAct: " + add);
                    if (add.equalsIgnoreCase(oldAddress)) {
                        Log.d(TAG, "getExtrasFromAct: same address");
                        return oldAddress;
                    } else {
                        Log.d(TAG, "getExtrasFromAct: address change");
                        return add;
                    }
                }
            } else {
                return "00:00:00:00:00";
            }
        } catch (Exception e) {
            Log.d(TAG, "getExtrasFromAct: " + e.getMessage());
        }
        return "00:00:00:00:00";
    }

    /*
     *
     * Feedback from BLE
     *
     * */
    //get feedback from bluetooth
    private boolean getFeedbackFromBle(int input) {
        SetPoints setPointsBluetooth = new SetPoints();
        if (input > 0) {
            try {
                if ((input >= setPointsBluetooth.INT_BLE_SP_FREQ1) && (input <= setPointsBluetooth.INT_BLE_SP_FREQ5)) {
                    //frequency
                } else if ((input >= setPointsBluetooth.INT_BLE_SP_INT1) && (input <= setPointsBluetooth.INT_BLE_SP_INT5)) {
                    //intensity
                } else if ((input >= setPointsBluetooth.INT_BLE_SP_TIME1) && (input <= setPointsBluetooth.INT_BLE_SP_TIME5)) {
                    //time
                } else if ((input >= setPointsBluetooth.INT_BLE_SP_TRA1) && (input <= setPointsBluetooth.INT_BLE_SP_TRA5)) {
                    //transducer A
                } else if ((input >= setPointsBluetooth.INT_BLE_SP_TRB1) && (input <= setPointsBluetooth.INT_BLE_SP_TRB5)) {
                    //transducer B
                } else if ((input >= setPointsBluetooth.INT_BLE_CMD_INIT) && (input <= setPointsBluetooth.INT_BLE_CMD_END)) {
                    //commands
                }
            } catch (Exception e) {
                Log.d(TAG, "getFeedbackFromBle: " + e.getMessage());
            } finally {
                return false;
            }
        }
        return false;
    }

    /*Adapter frequency*/
    //inject data to the inject data Model Basic
    private ModelBtn injectDataModelBasic(String name, String status, Drawable drawable) {
        ModelBtn modelBtn = new ModelBtn();
        modelBtn.setBtnName(name);
        modelBtn.setBtnStatus(status);
        modelBtn.setMenuDrawable(drawable);
        return modelBtn;
    }

    //update GUI adapter -frequency
    private boolean updateRecyclerViewF(int position, String name, String status, Drawable drawable) {
        if (modelBtnFreqArrayList != null) {
            //modelBtnFreqArrayList.clear();
            modelBtnFreqArrayList.add(position, injectDataModelBasic(name, status, drawable));
            Log.d(TAG, "updateRecyclerViewF: position:" + position + ".name:" + name);
        } else {
            return false;
        }
        return true;
    }

    //update GUI adapter - intensity
    private boolean updateRecyclerViewI(int position, String name, String status, Drawable drawable) {
        if (modelBtnIntArrayList != null) {
            //modelMenuArrayList.clear();
            modelBtnIntArrayList.add(position, injectDataModelBasic(name, status, drawable));
            // Log.d(TAG, "updateRecyclerView: position:" + position + ".name:" + name);
        } else {
            return false;
        }
        return true;
    }

    //update GUI adapter - time
    private boolean updateRecyclerViewT(int position, String name, String status, Drawable drawable) {
        if (modelBtnTimeArrayList != null) {
            //modelMenuArrayList.clear();
            modelBtnTimeArrayList.add(position, injectDataModelBasic(name, status, drawable));
            // Log.d(TAG, "updateRecyclerView: position:" + position + ".name:" + name);
        } else {
            return false;
        }
        return true;
    }

    //update GUI adapter - RadioButton -A
    private boolean updateRecyclerViewRbA(int position, String name, String status, Drawable drawable) {
        if (modelBtnArrayListA != null) {
            modelBtnArrayListA.add(position, injectDataModelBasic(name, status, drawable));
        } else {
            return false;
        }
        return true;
    }

    //update GUI adapter - RadioButton -B
    private boolean updateRecyclerViewRbB(int position, String name, String status, Drawable drawable) {
        if (modelBtnArrayListB != null) {
            modelBtnArrayListB.add(position, injectDataModelBasic(name, status, drawable));
        } else {
            return false;
        }
        return true;
    }

    //update GUI adapter - Icon -A
    private boolean updateRecyclerViewIconA(int position, String name, String status, Drawable drawable) {
        if (modelIconArrayListA != null) {
            modelIconArrayListA.add(position, injectDataModelBasic(name, status, drawable));
        } else {
            return false;
        }
        return true;
    }

    //update GUI adapter - Icon -B
    private boolean updateRecyclerViewIconB(int position, String name, String status, Drawable drawable) {
        if (modelIconArrayListB != null) {
            modelIconArrayListB.add(position, injectDataModelBasic(name, status, drawable));
        } else {
            return false;
        }
        return true;
    }

    //update Recycler view
    private void updateGUIRecyclerViewF() {
        Log.d(TAG, "updateGUIRecyclerViewF: ");
        recyclerViewF = findViewById(R.id.RecyclerViewFreq);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VibrationPercussionActivity.this);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewF.setLayoutManager(linearLayoutManager);//error
        recyclerViewF.setHasFixedSize(true);
        Log.d(TAG, "updateGUIRecyclerViewF:model size: " + modelBtnFreqArrayList.size());
        recyclerViewAdaptBtnF = new RecyclerViewAdaptBtnF(this, modelBtnFreqArrayList);//modelDevicesArrayList
        recyclerViewAdaptBtnF.notifyDataSetChanged();
        recyclerViewF.setAdapter(recyclerViewAdaptBtnF);
    }

    //update Recycler view Intensity
    private void updateGUIRecyclerViewI() {
        recyclerViewI = findViewById(R.id.RecyclerViewInt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VibrationPercussionActivity.this);
        recyclerViewI.setLayoutManager(linearLayoutManager);//error
        recyclerViewI.setHasFixedSize(true);
        recyclerViewAdaptBtnI = new RecyclerViewAdaptBtnI(this, modelBtnIntArrayList);//modelDevicesArrayList
        recyclerViewI.setAdapter(recyclerViewAdaptBtnI);
    }

    //update Recycler view Time
    private void updateGUIRecyclerViewT() {
        recyclerViewT = findViewById(R.id.RecyclerViewTime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VibrationPercussionActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewT.setLayoutManager(linearLayoutManager);//error
        recyclerViewT.setHasFixedSize(true);
        recyclerViewAdaptBtnT = new RecyclerViewAdaptBtnT(this, modelBtnTimeArrayList);//modelDevicesArrayList
        recyclerViewT.setAdapter(recyclerViewAdaptBtnT);
    }

    //update Recycler view RadioButton -A
    private void updateGUIRecyclerViewRbA() {
        recyclerViewRbA = findViewById(R.id.RecyclerViewRbA);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VibrationPercussionActivity.this);
        recyclerViewRbA.setLayoutManager(linearLayoutManager);//error
        recyclerViewRbA.setHasFixedSize(true);
        recyclerViewAdaptRbA = new RecyclerViewAdaptRbA(this, modelBtnArrayListA);//modelDevicesArrayList
        recyclerViewRbA.setAdapter(recyclerViewAdaptRbA);
    }

    //update Recycler view RadioButton -B
    private void updateGUIRecyclerViewRbB() {
        recyclerViewRbB = findViewById(R.id.RecyclerViewRbB);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VibrationPercussionActivity.this);
        recyclerViewRbB.setLayoutManager(linearLayoutManager);//error
        recyclerViewRbB.setHasFixedSize(true);
        recyclerViewAdaptRbB = new RecyclerViewAdaptRbB(this, modelBtnArrayListB);//modelDevicesArrayList
        recyclerViewRbB.setAdapter(recyclerViewAdaptRbB);
    }

    //update Recycler view  icon A
    private void updateGUIRecyclerViewIconA() {
        Log.d(TAG, "updateGUIRecyclerViewIconA: ");

        Log.d(TAG, "updateGUIRecyclerViewIconA: model" + modelIconArrayListA.size());
        recyclerViewIconA = findViewById(R.id.RecyclerViewIconA);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VibrationPercussionActivity.this);
        recyclerViewIconA.setLayoutManager(linearLayoutManager);//error
        recyclerViewIconA.setHasFixedSize(true);
        recyclerViewAdaptIconA = new RecyclerViewAdapticonA(modelIconArrayListA);//modelDevicesArrayList
        recyclerViewIconA.setAdapter(recyclerViewAdaptIconA);
    }

    //update Recycler view  icon B
    private void updateGUIRecyclerViewIconB() {
        recyclerViewIconB = findViewById(R.id.RecyclerViewIconB);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VibrationPercussionActivity.this);
        recyclerViewIconB.setLayoutManager(linearLayoutManager);//error
        recyclerViewIconB.setHasFixedSize(true);
        recyclerViewAdaptIconB = new RecyclerViewAdapticonB(modelIconArrayListB);//modelDevicesArrayList
        recyclerViewIconB.setAdapter(recyclerViewAdaptIconB);
    }

    /*
    // get return from bluetooth and update GUI  frequency
    private boolean updateButtonsFrequencyF(int value) {
        Log.d(TAG, "updateButtonsFrequencyF: ");
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_FREQ_NONE) {
                    //none
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ1) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_radio_button_checked_green_24dp));
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);

                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ2) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_radio_button_checked_green_24dp));
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);

                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ3) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_radio_button_checked_green_24dp));
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ4) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_radio_button_checked_green_24dp));
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ5) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_radio_button_checked_green_24dp));
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ_MAX) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_radio_button_checked_green_24dp));
                } else {
                    Log.d(TAG, "updateButtonsFrequencyF: empty");
                }
                updateGUIRecyclerViewF();
            } catch (Exception e) {
                Log.d(TAG, "updateButtonsFrequencyF exception: " + e.getMessage());
            }
            updateGUIRecyclerViewF();
            return true;
        }
        //modelBtnFreqArrayList.clear();
        //updateGUIRecyclerViewF();
        return false;
    }*/
    // get return from bluetooth and update GUI  frequency
    private boolean updateButtonsFrequencyF(int value) {
        Log.d(TAG, "updateButtonsFrequencyF: ");
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_FREQ_NONE) {
                    //none
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ1) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);

                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ2) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);

                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ3) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ4) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ5) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_FREQ_MAX) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                } else {
                    Log.d(TAG, "updateButtonsFrequencyF: empty");
                }
                updateGUIRecyclerViewF();
            } catch (Exception e) {
                Log.d(TAG, "updateButtonsFrequencyF exception: " + e.getMessage());
            }
            updateGUIRecyclerViewF();
            return true;
        }
        //modelBtnFreqArrayList.clear();
        //updateGUIRecyclerViewF();
        return false;
    }

    // get return from bluetooth and update GUI  Intensity
    private boolean updateButtonsIntensity(int value) {
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_INT_NONE) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_INT1) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_INT2) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_INT3) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_INT4) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_INT5) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_INT_MAX) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                }
                updateGUIRecyclerViewI();
            } catch (Exception e) {
                Log.d(TAG, "updateButtonsIntensity: " + e.getMessage());
            }
            return true;
        }
        return false;
    }

    // get return from bluetooth and update GUI  time
    private boolean updateButtonsTime(int value) {
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_TIME_NONE) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME1) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME2) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME3) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME4) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME5) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                }
                updateGUIRecyclerViewT();
                return true;
            } catch (Exception e) {
                Log.d(TAG, "updateButtonsTime: " + e.getMessage());
            }
        }
        return false;
    }

    //radio buttons just for Percussion
    private boolean updateButtonsRbAPercussion(int value) {
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_TRA_NONE) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS0);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRA1) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS1);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRA2) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS2);
                } else if (value == setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS_ALL_PERC);
                }


                new_transdA = value;//save in memory
                updateGUIRecyclerViewRbA();
                return true;

            } catch (Exception e) {
                Log.d(TAG, "updateButtonsRbA: " + e.getMessage());
            }
        }
        return false;
    }

    // Radio buttons for Percussion and vibration update GUI  RAdioButton -A
    private boolean updateButtonsRbA(int value) {
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_TRA_NONE) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS0);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRA1) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS1);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRA2) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS2);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRA3) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS3);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRA4) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS4);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRA5) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS5);
                } else if (value == setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS_ALL_VIB);
                }
                new_transdA = value;//save in memory
                updateGUIRecyclerViewRbA();
                return true;

            } catch (Exception e) {
                Log.d(TAG, "updateButtonsRbA: " + e.getMessage());
            }
        }
        return false;
    }

    // get return from bluetooth and update GUI  RAdioButton -B
    private boolean updateButtonsRbBPercussion(int value) {
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_TRB_NONE) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS0);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRB1) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    // updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS1);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRB2) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS2);
                } else if (value == setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdB(controlGUI.POS_ALL_PERC);
                }
                new_transdB = value;//save in memory
                updateGUIRecyclerViewRbB();
                return true;

            } catch (Exception e) {
                Log.d(TAG, "updateButtonsRbB: " + e.getMessage());
            }
        }
        return false;
    }

    // get return from bluetooth and update GUI  RAdioButton -B
    private boolean updateButtonsRbB(int value) {
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_TRB_NONE) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS0);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRB1) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS1);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRB2) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS2);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRB3) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS3);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRB4) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS4);
                } else if (value == setPointsBluetooth.INT_BLE_SP_TRB5) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS5);
                } else if (value == setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconA
                    controlIconTransdB(controlGUI.POS_ALL_VIB);
                }
                new_transdB = value;//save in memory
                updateGUIRecyclerViewRbB();
                return true;

            } catch (Exception e) {
                Log.d(TAG, "updateButtonsRbB: " + e.getMessage());
            }
        }
        return false;
    }

    //get return from bluetooth and update GUI  commands
    private int updateCommand(int value) {
        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_CMD_NONE) {
                    // btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //added 10/19/22
                    btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_wh));
                    btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));


                    return setPointsBluetooth.INT_BLE_CMD_NONE;
                } else if ((value == setPointsBluetooth.INT_BLE_CMD_START)) {
                    //btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48));
                    //btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    //added 10/19/22
                    btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_wh));
                    btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));

                    displayOperation(displayOperations.DISPLAY_OPE_RUNNING);//running
                    return setPointsBluetooth.INT_BLE_CMD_START;
                } else if ((value == setPointsBluetooth.INT_BLE_CMD_STOP)) {
                    //btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    // btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));

                    //added 10/19/22
                    btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_wh));
                    btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));

                    displayOperation(displayOperations.DISPLAY_OPE_STOPPED);//stopped
                    return setPointsBluetooth.INT_BLE_CMD_STOP;
                } else if (value == setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC) {//added 10/19/22
                    mode = selectMode(status.SELECT_MODE_TOTAL_PERCUSSION);
                    return setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC;
                } else if (value == setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB) {//added 10/19/22
                    mode = selectMode(status.SELECT_MODE_TOTAL_VIBRATION);
                    return setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB;
                }
                // return true;

            } catch (Exception e) {
                Log.d(TAG, "uupdateCommand: " + e.getMessage());
            }
        }
        return setPointsBluetooth.INT_BLE_CMD_NONE;
    }

    //get return from bluetooth and update GUI  commands
    private int updateMode(int value) {
        if (value > 0) {
            try {
                if (value == status.SELECT_MODE_NONE) {
                    btnSelectPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else if ((value == status.SELECT_MODE_PERCUSSION)) {
                    btnSelectPer.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_circle_32), null, null, null);
                    btnSelectVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else if ((value == status.SELECT_MODE_VIBRATION)) {
                    btnSelectPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectVib.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_circle_32), null, null, null);
                    btnSelectTotalPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else if ((value == status.SELECT_MODE_TOTAL_PERCUSSION)) {
                    btnSelectPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalPer.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_circle_32), null, null, null);
                    btnSelectTotalVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else if ((value == status.SELECT_MODE_TOTAL_VIBRATION)) {
                    btnSelectPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectVib.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalPer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btnSelectTotalVib.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_circle_32), null, null, null);
                }
                return value;

            } catch (Exception e) {
                Log.d(TAG, "uupdateCommand: " + e.getMessage());
            }
        }
        return setPointsBluetooth.INT_BLE_CMD_NONE;
    }

    //indication therapy is ready
    private void updateBtnReady(int value) {
        try {
            Default_values default_values = new Default_values();
            AnimationDrawable rocketAnimation;
            switch (value) {
                case 0:
                    //stop animation
                    btnReady.setVisibility(View.INVISIBLE);
                    displaytextStart(false);
                    lockMode(false);

                    break;
                case 1: //alert
                    btnReady.setVisibility(View.INVISIBLE);
                    //btnReady.setVisibility(View.VISIBLE);
                    //displaytextStart(true);
                    displaytextStart(false);
                    lockMode(true);
                    /*if(btnReady.getVisibility()==View.INVISIBLE){
                        btnReady.setVisibility(View.VISIBLE);
                        animationSideRail(btnReady, R.drawable.btn_start_readim, true);
                    }*/
                    break;
                default:
            }
        } catch (Exception e) {
            Log.d(TAG, "updateBrnReady: " + e.getMessage());
        }


    }

    //get return from bluetooth and update GUI  commands
    private void updateSideRail(int value) {
        try {
            Default_values default_values = new Default_values();
            AnimationDrawable rocketAnimation;
            switch (value) {
                case 0:
                    //stop animation
                    btnSr1.setBackgroundResource(R.drawable.btn_siderail);
                    btnSr2.setBackgroundResource(R.drawable.btn_siderail);
                    btnSr3.setBackgroundResource(R.drawable.btn_siderail);
                    btnSr4.setBackgroundResource(R.drawable.btn_siderail);
                    break;
                case 1: //alert
                    animationSideRail(btnSr1, R.drawable.btn_siderail_alert, true);
                    animationSideRail(btnSr2, R.drawable.btn_siderail_alert, true);
                    animationSideRail(btnSr3, R.drawable.btn_siderail_alert, true);
                    animationSideRail(btnSr4, R.drawable.btn_siderail_alert, true);
                    break;
                default:
            }
        } catch (Exception e) {
            Log.d(TAG, "updateSideRail: " + e.getMessage());
        }
    }

    //animation siderail
    private void animationSideRail(Button btn, int drawable, boolean enable) {
        AnimationDrawable siderailAnimation = null;
        if (btn != null) {
            try {
                if (enable) {
                    btn.setBackgroundResource(drawable);
                    siderailAnimation = (AnimationDrawable) btn.getBackground();
                    siderailAnimation.start();
                } else {
                    siderailAnimation.stop();
                }
            } catch (Exception e) {
                Log.d(TAG, "animationSideRail: " + e.getMessage());
            }
        }
    }

    //animation to start therapy
    private void animationStartTherapy(int value) {
        if (value > 0) {
            try {
                Default_values default_values = new Default_values();
                AnimationDrawable rocketAnimation;
                switch (value) {
                    case 0:
                        //stop animation
                        btnSr1.setBackgroundResource(R.drawable.btn_siderail);
                        btnSr2.setBackgroundResource(R.drawable.btn_siderail);
                        btnSr3.setBackgroundResource(R.drawable.btn_siderail);
                        btnSr4.setBackgroundResource(R.drawable.btn_siderail);
                        break;
                    case 1: //alert
                        animationSideRail(btnSr1, R.drawable.btn_siderail_alert, true);
                        animationSideRail(btnSr2, R.drawable.btn_siderail_alert, true);
                        animationSideRail(btnSr3, R.drawable.btn_siderail_alert, true);
                        animationSideRail(btnSr4, R.drawable.btn_siderail_alert, true);
                        break;
                    default:
                }
            } catch (Exception e) {
                Log.d(TAG, "updateSideRail: " + e.getMessage());
            }
        }
    }

    //control icon transducers A
    private void controlIconTransdA(int value) {
        try {
            //controlIconSelectedTransdA(value);

            switch (value) {
                case 0:
                    //none
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 1:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 2:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);

                    break;
                case 3:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 4:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);

                    break;
                case 5:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    break;
                case 6://total perc
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    break;
                case 7://total vib
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    break;
            }
            updateGUIRecyclerViewIconA();
        } catch (Exception e) {
            Log.d(TAG, "controlIconTransdA: " + e.getMessage());
        }
    }

    //control icon transducers B
    private void controlIconTransdB(int value) {
        try {
            switch (value) {
                case 0:
                    //none
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 1:
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 2:
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);

                    break;
                case 3:
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 4:
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);

                    break;
                case 5:
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconB(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    break;
                case 6://total perc
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    break;
                case 7://total vib
                    updateRecyclerViewIconB(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconB(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    break;
            }
            updateGUIRecyclerViewIconB();
        } catch (Exception e) {
            Log.d(TAG, "controlIconTransdB: " + e.getMessage());
        }
    }

    //control icon transducers A
    private void controlIconCoolingTransd(int value) {
        Log.d(TAG, "controlIconCoolingTransd: " + value);
       /* try {


            switch (value) {
                case 0:
                    //none
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 1:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_bl));
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 2:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_bl));
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);

                    break;
                case 3:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_bl));
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    break;
                case 4:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_bl));
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);

                    break;
                case 5:
                    updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_bl));
                    break;
            }
            updateGUIRecyclerViewIconA();
        } catch (Exception e) {
            Log.d(TAG, "controlIconTransdA: " + e.getMessage());
        }*/
    }

    //Indication on transducers A
    private void coolingMod_A(int position, int input) {
        Log.d(TAG, "coolingMod_A: pos:" + position + ".input:" + input);
      /*  try {
            if(input==controlGUI.TRANSD_NONE){
                updateRecyclerViewIconA(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
            } else if(input==controlGUI.TRANSD_SELECTED){
                updateRecyclerViewIconA(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
            }else if(input==controlGUI.TRANSD_COOLING){
                updateRecyclerViewIconA(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_bl));
            }else if(input==controlGUI.TRANSD_ERROR){
                updateRecyclerViewIconA(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_rd));
            }
            else{
               // updateRecyclerViewIconA(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
            }
        }catch (Exception e){
            Log.d(TAG, "coolingMod1: ex:"+e.getMessage());
        }*/
    }

    //Indication on transducers B
    private void coolingMod_B(int position, int input) {
        try {
            if (input == controlGUI.TRANSD_NONE) {
                updateRecyclerViewIconB(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
            } else if (input == controlGUI.TRANSD_SELECTED) {
                updateRecyclerViewIconB(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
            } else if (input == controlGUI.TRANSD_COOLING) {
                updateRecyclerViewIconB(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_bl));
            } else if (input == controlGUI.TRANSD_ERROR) {
                updateRecyclerViewIconB(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_24_rd));
            } else {
                updateRecyclerViewIconB(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
            }
        } catch (Exception e) {
            Log.d(TAG, "coolingMod1: ex:" + e.getMessage());
        }
    }

    //control icon transducers A
    private void controlIconSelectedTransdA(int value) {
        Log.d(TAG, "controlIconSelectedTransdA: value:" + value);
        try {
            switch (value) {
                case 0:
                    //none
                    coolingMod_A(controlGUI.MOD1, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD2, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD3, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD4, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD5, controlGUI.TRANSD_NONE);
                    /*updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);*/
                    break;
                case 1:
                    coolingMod_A(controlGUI.MOD1, controlGUI.TRANSD_SELECTED);
                    coolingMod_A(controlGUI.MOD2, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD3, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD4, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD5, controlGUI.TRANSD_NONE);
                    /*updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);*/
                    break;
                case 2:
                    coolingMod_A(controlGUI.MOD1, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD2, controlGUI.TRANSD_SELECTED);
                    coolingMod_A(controlGUI.MOD3, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD4, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD5, controlGUI.TRANSD_NONE);
                    /*updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);*/

                    break;
                case 3:
                    coolingMod_A(controlGUI.MOD1, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD2, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD3, controlGUI.TRANSD_SELECTED);
                    coolingMod_A(controlGUI.MOD4, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD5, controlGUI.TRANSD_NONE);
                   /* updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);*/
                    break;
                case 4:
                    coolingMod_A(controlGUI.MOD1, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD2, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD3, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD4, controlGUI.TRANSD_SELECTED);
                    coolingMod_A(controlGUI.MOD5, controlGUI.TRANSD_NONE);
                   /* updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);*/

                    break;
                case 5:
                    coolingMod_A(controlGUI.MOD1, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD2, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD3, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD4, controlGUI.TRANSD_NONE);
                    coolingMod_A(controlGUI.MOD5, controlGUI.TRANSD_SELECTED);
                    /*updateRecyclerViewIconA(0, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(1, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(2, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(3, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, null);
                    updateRecyclerViewIconA(4, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_og_24));*/
                    break;
            }
            Log.d(TAG, "controlIconSelectedTransdA: ");
            updateGUIRecyclerViewIconA();
        } catch (Exception e) {
            Log.d(TAG, "controlIconTransdA: " + e.getMessage());
        }
    }

    //send command by bluetooth
    private String sendCommandBle(int value) {

        return "0";
    }

    //check setup before allow to start

    //------------------Bluetooth---------------//

    //init BLE
    private void initBLE() {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            toastMessageInt(R.string.ble_not_supported);
            finish();
        }
        // Initializes Bluetooth adapter.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                // return;
            }
            startActivityForResult(enableBtIntent, actionGatt.REQUEST_ENABLE_BT);
        }
        //Launch BLE scan
        launchBleScan();
    }

    //check bluetooth permissions
    private void checkBluetoothPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        actionGatt.REQUEST_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //Bluetooth Broadcast receiver
    private void broadCastReceiver() {
        Log.d(TAG, "broadCastReceiver: ");
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            filter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
            this.registerReceiver(mBroadcastReceiver, filter);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: Exception BroadCast Receiver" + e.getMessage());
        }
    }

    // Handles various events fired by the Service.//
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            final String action = intent.getAction();
            if (ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, "onReceive:ACTION_GATT_CONNECTED ");
                connected = true;
                setConnetion();


                invalidateOptionsMenu();
                //mConnected = true;
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, "onReceive:ACTION_GATT_DISCONNECTED ");
                connected = false;
                updateConnectionState(R.string.ble_disconnected);
                invalidateOptionsMenu();
                // clearUI();
            } else if (
                    ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the
                // user interface.
                //displayGattServices(actionGatt.getSupportedGattServices());
                Log.d(TAG, "onReceive:ACTION_GATT_SERVICES_DISCOVERED ");
                //displayGattServices(mGatt, statusBleDiscovery);
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "onReceive:ACTION_DATA_AVAILABLE ");
                displayData(intent.getStringExtra(EXTRA_DATA));
            }
        }
    };

    //launch ble Scan
    private void launchBleScan() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //Enable Bluetooth
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return;
                }
                startActivityForResult(enableBtIntent, actionGatt.REQUEST_ENABLE_BT);
            } catch (Exception e) {
                Log.d(TAG, "onCreate:" + e.getLocalizedMessage());
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                Log.d(TAG, "initBLE:SDK_INT>=21");
                mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
                //
                try {
                    mBLEScanner.startScan(mScanCallback);
                } catch (Exception e) {
                    Log.d(TAG, "onPostResume: " + e.getLocalizedMessage());
                }
            } else {
                Log.d(TAG, "initBLE:SDK_INT<21");
            }
        }
    }

    //stop ble scan
    private void cancelBleScan() {
        try {
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                //mLeDeviceListAdapter.clear();
                if (bleIsScanner) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            Log.d(TAG, "onPause:ActivityCompat#requestPermissions ");
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mBLEScanner.stopScan(mScanCallback);
                    }
                    bleIsScanner = false;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "onPause: " + e.getMessage());
        }
    }

    //Scan bluetooth
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            mBluetoothDevice = result.getDevice();
            Log.d(TAG, "onScanResult: " + result.getDevice().getAddress());
            if (myBleAdd != null) {
                if (mBluetoothDevice.getAddress().equalsIgnoreCase(myBleAdd)) {
                    connectToDevice(result.getDevice());
                }
            }
        }
    };

    //force connection
    private void setConnetion() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                enableGui(true);
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                updateConnectionState(R.string.ble_connected);
            }
        });
        isFlagSetConnection = true;
    }


    //Connection with device
    public void connectToDevice(BluetoothDevice device) {
        Log.d(TAG, "connectToDevice: trying to connect");
        if (mGatt == null) {
            this.mDevice = device;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return;
            }
            mGatt = device.connectGatt(this, true, gattCallback);
            // Stop scan
            mBLEScanner.stopScan(mScanCallback);
            connectToDevice(device);
            Log.d(TAG, "Connected to:" + device.getName());
        }
    }

    // gattCallback
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            //
            String intentAction;

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "onConnectionStateChange:newState STATE_CONNECTED ");

                systemConnected();
                intentAction = ACTION_GATT_CONNECTED;//ACTION_GATT_CONNECTED;
                connectionState = actionGatt.STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return;
                }
                Log.i(TAG, "Attempting to start service discovery:" +
                        mGatt.discoverServices());
                mConnected = true;
                flagConnSuc = true;
                // and we also want to get RSSI value to be updated periodically
                // startMonitoringCharctValue(isFeedbackEnable);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                connectionState = actionGatt.STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                mConnected = false;
                systemLostConnection(flagConnSuc);
                broadcastUpdate(intentAction);//working

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            // discoverServices(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered: ");
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                displayGattServices(gatt, status);
                statusBleDiscovery = status;
                Log.d(TAG, "onServicesDiscovered: " + status);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onCharacteristicRead:characteristic " + status);
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG, "onCharacteristicWrite:  status" + status);
            switch (status) {
                case (BluetoothGatt.GATT_FAILURE):
                    Log.d(TAG, "onCharacteristicWrite: GATT_FAILURE");
                    value = "FAILURE";
                    callUpdate(UPDATE_VALUE);
                    break;
                case (BluetoothGatt.GATT_SUCCESS):
                    Log.d(TAG, "onCharacteristicWrite!!!: GATT_SUCCESS");
                    value = "SUCCESS";
                    callUpdate(UPDATE_VALUE);

                    break;
                case (BluetoothGatt.GATT_WRITE_NOT_PERMITTED):
                    Log.d(TAG, "onCharacteristicWrite: GATT_WRITE_NOT_PERMITTED");
                    value = "WRITE_NOT_PERMITTED";
                    callUpdate(UPDATE_VALUE);
                    break;
                case (BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH):
                    Log.d(TAG, "onCharacteristicWrite: GATT_INVALID_ATTRIBUTE_LENGTH");
                    value = "INVALID_ATTRIBUTE_LENGTH";
                    callUpdate(UPDATE_VALUE);
                    break;
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            Log.d(TAG, "onCharacteristicChanged: uint32 " + characteristic.getIntValue(FORMAT_UINT32, 0));
            int value = characteristic.getIntValue(FORMAT_UINT32, 0);
            if (value > 0) {
                getNotificationsBluetooth(value);
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onDescriptorRead:characteristic " + status);

            }

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onDescriptorRead:characteristic " + status);

            }
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.d(TAG, "onReliableWriteCompleted: " + status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.d(TAG, "onReadRemoteRssi: " + rssi);
            displayRssiValue(rssi);
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    };

    //Call broadcast
    private void broadcastUpdate(final String action) {
        if (action.equalsIgnoreCase(ACTION_GATT_DISCONNECTED)) {
            //finish();
            updateConnectionState(R.string.ble_disconnected);
            //invalidateOptionsMenu();
            //initDataGui();
        }
        // sendBroadcast(intent);
    }

    //Broadcast data from read char
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        BLE_Format ble_format = new BLE_Format();

        // Log.d(TAG, "broadcastUpdate get action: " + action);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return;
        }
        mGatt.setCharacteristicNotification(characteristic, true);
        /*//Read articulation Head
        if (uuid.UUID_CHARACTERISTIC_K750_ART_HEAD.equals(characteristic.getUuid())) {//A10
            int artHead = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);//   int temperature = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, 0);
            Log.d(TAG, "broadcastUpdate: " + artHead);
            artHeadValue = getArtHead(artHead);
            //Read articulation foot
        } else if (uuid.UUID_CHARACTERISTIC_K750_ART_FOOT.equals(characteristic.getUuid())) {//A10
            int artFoot = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);//FORMAT_UINT16
            Log.d(TAG, "broadcastUpdate: " + artFoot);
            artFootValue = getArtFoot(artFoot);
        }
        //Read lift head
        else if (uuid.UUID_CHARACTERISTIC_K750_LIFT_HEAD.equals(characteristic.getUuid())) {//A10
            int liftHead = (characteristic.getIntValue(FORMAT_UINT8, 0));
            liftHeadValue = getLiftHead(liftHead);
        }
        //Read lift foot
        else if (uuid.UUID_CHARACTERISTIC_K750_LIFT_FOOT.equals(characteristic.getUuid())) {//A10
            int liftFoot = (characteristic.getIntValue(FORMAT_UINT8, 0));
            liftFootValue = getLiftFoot(liftFoot);
        }

        //Read extension
        else if (uuid.UUID_CHARACTERISTIC_K750_EXT.equals(characteristic.getUuid())) {//A10
            final int extension = (characteristic.getIntValue(FORMAT_UINT8, 0));
            Log.d(TAG, "broadcastUpdate: ext:" + extension);
            extensionValue = getExtension(extension);
            Log.d(TAG, "broadcastUpdate: ext value:" + extensionValue);
        }

        //Read brake
        else if (uuid.UUID_CHARACTERISTIC_K750_BRAKE.equals(characteristic.getUuid())) {//A10
            final int brake = (characteristic.getIntValue(FORMAT_UINT8, 0));
            brakeValue = getBrake(brake);
        }

        //Read scale
        else if (uuid.UUID_CHARACTERISTIC_K750_SCALE.equals(characteristic.getUuid())) {//A10
            final int scale = (characteristic.getIntValue(FORMAT_UINT8, 0));
            scaleValue = getScale(scale);
        }

        //Battery
        else if (uuid.UUID_CHARACTERISTIC_A10_BATTERY.equals(characteristic.getUuid())) {//A10
            final int readingIntBattery = (characteristic.getIntValue(FORMAT_UINT8, 0));
            powerSupply = getPower(readingIntBattery);

        }  */
        //COMMANDS
        if (uuid.UUID_CHARACTERISTIC_K750_CMD.equals(characteristic.getUuid())) {//
            Log.d(TAG, "broadcastUpdate:CHARACTERISTIC_K750_CMD " + (characteristic.getIntValue(FORMAT_UINT8, 0)));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));

            }
        }
        sendBroadcast(intent);
    }

    //displayGattServices
    private void displayGattServices(BluetoothGatt gatt, int status) {
        discoverServices(gatt, status);
    }

    //ble discovery services
    private void discoverServices(BluetoothGatt gatt, int status) {
        mGatt = gatt;
        List<BluetoothGattService> services = gatt.getServices();
        for (BluetoothGattService service : services) {
            //print services
            Log.d(TAG, "discoverServices scan: " + service.getUuid().toString());
            if (service.getUuid().equals(uuid.UUID_SERVICE_STM32_LED_BTN)) {
                Log.d(TAG, "discoverServices: UUID_SERVICE_STM32_LED_BTN ");
                // setStn32_Led_Btn(gatt);//led and buttons
            }
            /*
            //Battery
            if (service.getUuid().equals(uuid.UUID_SERVICE_A10_BATTERY)) {
                List<BluetoothGattCharacteristic> characteristics1 = gatt.getService(service.getUuid()).getCharacteristics();
                for (BluetoothGattCharacteristic characteristic1 : characteristics1) {
                    Log.d(TAG, "discoverServices scan Battery charact: " + characteristic1.getUuid());
                    characteristicUUIDsList.add(characteristic1.getUuid());
                }
                setK750_Power(gatt);
            }*/

            //Services UUID_SERVICE_for K750
            /*
             * Include:
             * Commands
             * SideRail            *
             * */
            if (service.getUuid().equals(uuid.UUID_SERVICE_K750_CUSTOM2)) {
                Log.d(TAG, "discoverServices:UUID_SERVICE_K750_Custom2 ");
                List<BluetoothGattCharacteristic> characteristics1 = gatt.getService(service.getUuid()).getCharacteristics();
                for (BluetoothGattCharacteristic characteristic1 : characteristics1) {
                    Log.d(TAG, "discoverServices custom2 charact: " + characteristic1.getUuid());
                    characteristicUUIDsList.add(characteristic1.getUuid());
                }
                setK750_Custom2(gatt);
            }
            //Services UUID_SERVICE_for K750
            /*
             * include:
             * articulation head
             * articulation foot
             * lift head
             * lift foot
             * extension
             * brake
             * scale
             * */
            /*
            if (service.getUuid().equals(uuid.UUID_SERVICE_K750_CUSTOM1)) {
                Log.d(TAG, "discoverServices:UUID_SERVICE_K750_Custom1 ");
                //scan char
                List<BluetoothGattCharacteristic> characteristics1 = gatt.getService(service.getUuid()).getCharacteristics();
                for (BluetoothGattCharacteristic characteristic1 : characteristics1) {
                    Log.d(TAG, "discoverServices scan char: " + characteristic1.getUuid());
                    characteristicUUIDsList.add(characteristic1.getUuid());
                }
                //
                if (setK750_Custom1(gatt)) {
                    //set enable to read characteris
                    isTempEnable = true;
                } else {

                    isTempEnable = false;
                }
            }*/
        }
    }

    //Set Motion Characte
    private boolean setK750_Custom2(BluetoothGatt gatt) {
        ///Commands
        bleChar.mCHARAC_K750_CMD = mGatt.getService(uuid.UUID_SERVICE_K750_CUSTOM2).getCharacteristic(uuid.UUID_CHARACTERISTIC_K750_CMD);
        if (bleChar.mCHARAC_K750_CMD == null) {
            Log.d(TAG, "setK750_Custom2:K750_CMD  failed ");
            return false;
        }
        for (BluetoothGattDescriptor descriptor : bleChar.mCHARAC_K750_CMD.getDescriptors()) {
            //Log.d(TAG, "onServicesDiscovered: descriptor Frequency " + descriptor);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            //descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return TODO;
            }
            mGatt.writeDescriptor(descriptor);
            // Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for Frequency ");
        }

        mGatt.setCharacteristicNotification(bleChar.mCHARAC_K750_CMD, true);
        Log.d(TAG, "K750_Custom2: K750_CMD");
        //

        //Siderail
        bleChar.mCHARAC_K750_SIDERAIL = mGatt.getService(uuid.UUID_SERVICE_K750_CUSTOM2).getCharacteristic(uuid.UUID_CHARACTERISTIC_K750_SIDERAIL);
        if (bleChar.mCHARAC_K750_SIDERAIL == null) {
            Log.d(TAG, "setK750_Custom2:K750_SIDERAIL failed ");
            return false;
        }
        Log.d(TAG, "K750_Custom2: K750_SIDERAIL ");
        //
        /*for (BluetoothGattDescriptor descriptor : mCHARACTERISTIC_A10_ACCELEROMETER.getDescriptors()) {
            //Log.d(TAG, "onServicesDiscovered: descriptor Frequency " + descriptor);
            // descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mGatt.writeDescriptor(descriptor);
            // Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for Frequency ");
        }
        mGatt.setCharacteristicNotification(mCHARACTERISTIC_A10_ACCELEROMETER, true);//mGatt.setCharacteristicNotification(mCHARACTERISTIC_A10_ORIENTATION, true);*/
        return true;
    }

    //get notification from Bluetooth
    private void getNotificationsBluetooth(int value) {
        Message msg = null;
        arrayDatafromBle = intToArray.intToByteArray(value);
        // getParamUpdateValueGui2(msg, arrayDatafromBle[0], arrayDatafromBle[1], arrayDatafromBle[2], arrayDatafromBle[3]);
        getParamUpdateValueGui3(arrayDatafromBle[0], arrayDatafromBle[1], arrayDatafromBle[2], arrayDatafromBle[3]);

    }

    //BLE-Close Broadcast-hardware connection
    public void closeBroadcast() {
        try {
            if (mBroadcastReceiver != null) {
                try {
                    this.unregisterReceiver(mBroadcastReceiver);
                } catch (Exception e) {
                    Log.d(TAG, "onCreate: Exception BroadCast Receiver" + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "closeBroadcast: Exception" + e.getMessage());
        }
    }

    //Select characteristics to send command to Bluetooth module
    private boolean sendCmdBle(int command) {
        //AsyncTaskExample asyncTask = new AsyncTaskExample();
        //AsyncTaskSend asyncTaskSend = new AsyncTaskSend();
        boolean ret = false;
        if (command != 0) {
            Log.d(TAG, "sendCmdBle: " + command);
            Log.d(TAG, "sendCmdBle: " + bleChar.mCHARAC_K750_CMD.getUuid());
            oldCommand = command;
            if (isReadingNow) {
                isPendingWrite = true;
                Log.d(TAG, "sendCmdBle: is  Reading when wants to write");
                ret = false;
            } else {
                Log.d(TAG, "sendCmdBle:  write");
                if (writeValueBleCharact(mGatt, bleChar.mCHARAC_K750_CMD, command)) {
                    ret = true;
                } else {
                    ret = false;
                }
            }
        }
        return ret;
    }

    //write  to Bluetooth
    private boolean writeValueBleCharact(BluetoothGatt gatt, BluetoothGattCharacteristic charac, int value) {
        boolean ret = false;
        Log.d(TAG, "writeCharac: Written " + value);
        try {
            int unixTime = 0;
            if (mBluetoothAdapter == null || gatt == null) {
                Log.w(TAG, "BluetoothAdapter not initialized");
                return false;
            }
            if (charac == null) {
                Log.e(TAG, "charact not found!");
            }
            if (value > 0) {
                unixTime = value;
            } else {
                unixTime = 1;
            }
            Log.d(TAG, "writeValueBleCharact: unixTime:" + unixTime);

            //convert from into to byte array
            byte[] byteArray = bigIntToByteArray(unixTime);
            Log.d(TAG, "writeValueBleCharact: byteArray " + byteArray);

            charac.setValue(byteArray);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return TODO;
            }
            boolean status = mGatt.writeCharacteristic(charac);
            if (status) {
                Log.d(TAG, "writeValueBleCharact: Written Successfully" + value);

                Message msg = null;
                //getParamUpdateValueGui(msg, value);
                ret = true;
            } else {
                ret = false;
                Log.d(TAG, "writeValueBleCharact: Error writing characteristicControl");
            }
        } catch (Exception e) {
            Log.d(TAG, "writeValueBleCharact: exception:" + e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }

    //convert int to bytearray
    private byte[] bigIntToByteArray(final int i) {
        BigInteger bigInt = BigInteger.valueOf(i);
        return bigInt.toByteArray();
    }

    //sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.INTENSITY, variables.CMD_INT_1));

    //------------Dispay---------------------//

    //get parameters value
    private void getParamUpdateValueGui2(Message msg, int value0, int value1, int value2, int value3) {
        try {
            String mValue0 = String.valueOf(value0);
            String mValue1 = String.valueOf(value1);
            String mValue2 = String.valueOf(value2);
            String mValue3 = String.valueOf(value3);
            String mValue = "[B0]:" + mValue0 + "...[B1]:" + mValue1 + "...[B2]:" + mValue2 + "...[B3]:" + mValue3;
            Log.d(TAG, "getParamUpdateValueGui:value: " + mValue);

            if (mValue != null) {
                msg = Message.obtain();
                value = mValue;
                msg.obj = value;
                msg.what = UPDATE_VALUE;
                msg.setTarget(uiHandler);
                msg.sendToTarget();
            }
        } catch (Exception e) {
            Log.d(TAG, "getParamUpdateValueGui: Exception" + e.getMessage());
        }
    }

    /*get all the feedback from the bluetooth and upgade ion the GUI*/
    //get parameters value
    private void getParamUpdateValueGui3(int value0, int value1, int value2, int value3) {
        try {
            mProtocol = value0;
            mSp = value1;

            String mValue0 = String.valueOf(value0);//protocol
            String mValue1 = String.valueOf(value1);//setpoint
            String mValue2 = String.valueOf(value2);//alarm status
            String mValue3 = String.valueOf(value3);//current mA
            String mValue = "[B0]:" + mValue0 + "...[B1]:" + mValue1 + "...[B2]:" + mValue2 + "...[B3]:" + mValue3;
            Log.d(TAG, "getParamUpdateValueGui:value: " + mValue);


            if (mProtocol == bleProtocol.FREQUENCY) {
                modelBtnFreqArrayList.clear();
                updateFbFreq(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.INTENSITY) {
                modelBtnIntArrayList.clear();
                updateFbInt(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.TIME) {
                modelBtnTimeArrayList.clear();
                updateFbTime(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.OPERATION) {
                updateFbCommands(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.RBA) {
                Log.d(TAG, "getParamUpdateValueGui3: RBA");
                // modelBtnArrayListA.clear();
                //modelIconArrayListA.clear();
                resetCheckBockA();
                //check mode
                updateFbRBA(mode, mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.RBB) {
                resetCheckBockB();
                //modelBtnArrayListB.clear();
                //modelIconArrayListB.clear();
                updateFbRBb(mode, mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.COOLING) {
                Log.d(TAG, "getParamUpdateValueGui3:cooling");
                controlIconCoolingTransd(mSp);
            }
            //alarm
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkStatusAlarms(mValue2);
                }
            });

            //power sensor
            //displayCurrent(mValue3);
            //wacht dog
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    watchDogTimerCom();
                }
            });

            if (!isFlagSetConnection) {
                Log.d(TAG, "getParamUpdateValueGui3: set connection");
                setConnetion();
                beep.beep_key();
            }

            //beep
            //beepSound();
        } catch (Exception e) {
            Log.d(TAG, "getParamUpdateValueGui: Exception" + e.getMessage());
        }
    }

    //check status alarm
    private void checkStatusAlarms(String malarm) {
        Alarm alarm1 = new Alarm();
        Log.d(TAG, "checkStatusAlarms: " + malarm);
        if (malarm.equalsIgnoreCase(alarm1.ALARM_ACK)) {

        } else if (malarm.equalsIgnoreCase(alarm1.ALARM_RESET)) {
            isAlarm = false;
        } else if (malarm.equalsIgnoreCase(alarm1.ALARM_SHORT_CIRCUIT)) {

        } else if (malarm.equalsIgnoreCase(alarm1.ALARM_SYSTEM_RESTART)) {
            if (isTherapyOn) {
                //stop system by alarm
                stopByEmergency();
                notificationSystemRestart();
                isAlarm = true;
            }

        } else if (malarm.equalsIgnoreCase(alarm1.ALARM_SYSTEM_STANDBY)) {
            if (isTherapyOn) {
                Log.d(TAG, "checkStatusAlarms: top by emergency");
                stopTimerEnableStart();
                //stopTherapy();
            }

            isAlarm = false;
        } else if (malarm.equalsIgnoreCase(alarm1.ALARM_SYSTEM_WORKING)) {
            if (!isTherapyOn) {
                //SOMETHING HAPPENS WITH THE STOP
                stopTherapy();
            }
        }


    }

    //updateConnectionState
    private void updateConnectionState(int input) {
        Log.d(TAG, "updateConnectionState: " + input);
        Resources resources = getResourcesLanguage(language);


        try {
            switch (input) {
                case R.string.ble_connected:
                    Log.d(TAG, "updateComIcon: ble_connected");
                    //Toast.makeText(k9Alert, "Connected", Toast.LENGTH_SHORT).show();
                    //ivBle.setImageResource(R.drawable.ic_baseline_bluetooth_connected_24);
                    displayConnection(resources.getString((R.string.string_com_connected)));
                    //enableGui(true);
                    break;
                case R.string.ble_disconnected:
                    Log.d(TAG, "updateComIcon: ble_disconnected");
                    // ivBle.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                    displayConnection(resources.getString((R.string.string_com_discconnected)));
                    //displayConnection("disconnected");
                    ///**/enableGui(false);
                    break;
                case R.string.ble_disconnected_requested:
                    //ivBle.setImageResource(R.drawable.ic_baseline_bluetooth_24);
                    Log.d(TAG, "updateComIcon: failure");
                    displayConnection("failure");
                    // systemFailure();
                    break;
                case R.string.ble_discovery_finished:
                    displayConnection("discovery finish");
                    Log.d(TAG, "updateComIcon: discovery finish");
                    //ivBle.setImageResource(R.drawable.ic_baseline_bluetooth_searching_24);
                    //ivDryConnection.setImageResource(R.drawable.ic_bluetooth_searching_black_24dp);
                    break;
                case R.string.ble_found:
                    Log.d(TAG, "updateComIcon: ble found");
                    displayConnection("device found");
                    //ivBle.setImageResource(R.drawable.ic_baseline_bluetooth_searching_24);
                    //ivDryConnection.setImageResource(R.drawable.ic_bluetooth_black_24dp);
                    break;
                default:
                    //ivDryConnection.setImageResource(R.drawable.ic_error_black_24dp);
                    break;
            }
        } catch (Exception e) {
            Log.d(TAG, "updateConnectionState: ex" + e.getMessage());
        }
    }

    //beep
    private void beepSound() {
        Log.d(TAG, "beepSound: ");
        /*if (!isFlagSetConnection) {
            setConnetion();
        }*/

        beep.beep_key();
    }

    //Display data
    private void displayData(String data) {
        Log.d(TAG, "instance initializer display data:" + data);
        //Toast.makeText(this, "Btn pressd", Toast.LENGTH_SHORT).show();
        //Date currentTime = Calendar.getInstance().getTime();
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String event = "Pressed:" + currentTime.toString();
        //btnTherm.setText(event);
    }

    //upgrade display
    private void callUpdate(int what) {
        /*android.os.Message msg = android.os.Message.obtain();
        msg.obj = value;//value
        msg.what = what;
        msg.setTarget(uiHandler);
        msg.sendToTarget();*/
    }

    private final Handler uiHandler = new Handler() {


        public void handleMessage(android.os.Message msg) {
            //Log.d(TAG, "handleMessage: ");
            final int what = msg.what;
            final String value = (String) msg.obj;
            switch (what) {
                case UPDATE_DEVICE:
                    updateDevice(value);
                    break;
                case UPDATE_VALUE:
                    updateValue(value);
                    break;
                case UPDATE_CLOUD:
                    // updateCloudIcon(valueCloud);
                    break;
                case UPDATE_MCU:
                    //updateMCUIcon(value);
                    break;
            }
        }
    };

    //Upgrade device information
    private void updateDevice(String value) {
        Log.d(TAG, "updateDevice: value" + value);
        try {
            if (value != null) {
                Log.d(TAG, "updateDevice: " + value);
            }
        } catch (Exception e) {
            Log.d(TAG, "updateValue: ");
        }
    }

    //Upgrade device information
    private void updateValue(String value1) {
        Log.d(TAG, "updateValue: value" + value1);
        try {
            if (value1 != null) {
                // tvResult.setText(value1);
                /*
                //tvConnection.setText(value1);
                if(value1.equalsIgnoreCase("51")){
                    tvResult.setText("Send Intensity level 1");
                }else  if(value1.equalsIgnoreCase("52")){
                    tvResult.setText("Send Intensity level 2");
                }else  if(value1.equalsIgnoreCase("53")){
                    tvResult.setText("Send Intensity level 3");
                }else  if(value1.equalsIgnoreCase("54")){
                    tvResult.setText("Send Intensity level 4");
                }else  if(value1.equalsIgnoreCase("55")){
                    tvResult.setText("Send Intensity level 5");
                }else  if(value1.equalsIgnoreCase("56")){
                    tvResult.setText("Send Turn");
                }*/

                //tvResult.setText(value1);
                Log.d(TAG, "updateValue: " + value1);

                if (value.equalsIgnoreCase("Failure")) {
                    //systemFailure();
                }


            }
        } catch (Exception e) {
            Log.d(TAG, "updateValue: error");
        }
    }

    //quality of the signal
    private void displayRssiValue(int value) {
        String unit = "dBm";
        Log.d(TAG, "displayRssiValue: " + value + "dBi");/**/
        //tvRssi.setText(String.valueOf(value) + unit);
    }

    //display operation
    private void displayOperation(String value) {
        if (value != null) {
            try {
                Resources resources = getResourcesLanguage(language);
                String text = "0";

                if (value.equalsIgnoreCase(displayOperations.DISPLAY_OPE_READY)) {
                    text = resources.getString(R.string.string_text_btn_ready);
                } else if (value.equalsIgnoreCase(displayOperations.DISPLAY_OPE_RUNNING)) {
                    text = resources.getString(R.string.string_text_btn_running);
                } else if (value.equalsIgnoreCase(displayOperations.DISPLAY_OPE_STOPPED)) {
                    text = resources.getString(R.string.string_text_btn_stopped);
                }
                if (text != null) {
                    tvOperation.setText(text);
                }


            } catch (Exception e) {
                Log.d(TAG, "displayOperation: Ex" + e.getMessage());
            }

        }

    }

    //display change color stroke btn start
    private void changeStrokeBtnStart(int value) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    if (value == controlGUI.POS0) {
                        //not ready
                        btnStart.setBackground(getDrawable(R.drawable.btn_start));
                    } else if (value == controlGUI.POS1) {
                        //ready
                        animationSideRail(btnStart, R.drawable.btn_start_ready, true);
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "changeStrokeBtnStart: ex:" + e.getMessage());
        }
    }

    //display connection status
    private void displayConnection(String value) {
        if (value != null) {
            try {
                tvCon.setText(value);
            } catch (Exception e) {
                Log.d(TAG, "displayConnection: ex:" + e.getMessage());
            }

        }

    }

    //display text press to start
    private void displaytextStart(boolean input) {
        if (tvPresStart != null) {
            if (input) {
                tvPresStart.setVisibility(View.VISIBLE);
                tvPresStart.setText(resources.getString(R.string.string_text_pres_start));
                //tvPresStart.setText("Press to start");
            } else {
                tvPresStart.setVisibility(View.INVISIBLE);
            }
        }
    }

    //display text power
    private void displayCurrent(String value) {
        try {
            if (value != null) {
                if (tvCurrent != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCurrent.setText(value + "mA");
                        }
                    });
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "displayCurrent: ex:" + e.getMessage());
        }


    }

    //-----------Enable---------------------//
    private void enableGui(boolean input) {
        Log.d(TAG, "enableGui: " + input);

        if (input) {
            if (!isEnableGui) {
                //load view
                updateButtonsFrequencyF(setPointsBluetooth.INT_BLE_SP_FREQ_NONE);//frequency
                updateButtonsIntensity(setPointsBluetooth.INT_BLE_SP_INT_NONE);//intensity
                updateButtonsTime(setPointsBluetooth.INT_BLE_SP_TIME_NONE);//time
                updateCommand(setPointsBluetooth.INT_BLE_CMD_NONE);
                isEnableGui = true;
            }

        } else {
            updateButtonsFrequencyF(setPointsBluetooth.INT_BLE_SP_CLEAN);
            isEnableGui = false;
        }
    }

    //Toast message
    private void toastMessageInt(int message) {
        if (message > 0) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSetupInfo(String name, String description) {
        Util.Message message = new Util.Message();
        Log.d(TAG, "onItemSetupInfo: " + description);
        if (description.equalsIgnoreCase(utilDialog.LOCATION_EXIT_THERAPY)) {
            //isFlagIsSr = true;
            //updateSideRail(controlGUI.POS0);
            //displayOperation(message.MESSAGE_SYSTEM_READY);
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_CONN_FAILED)) {
            goHome();
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_SIDERAIL)) {//
            isFlagIsSr = true;
            updateSideRail(controlGUI.POS0);
            condStartTherapy(flagIsFreq, flagIsInt, flagIsTim, flagIsTRA, flagIsTRB, isFlagIsSr);

            //changed 10/19/22
            // displayOperation(displayOperations.DISPLAY_OPE_READY);//Ready
            updateBtnReady(controlGUI.POS1);
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_EMERGENCY_STOP_CONFIRM)) {
            Log.d(TAG, "onItemSetupInfo: emergency stop");
            goHome();
        } else if (description.equalsIgnoreCase(utilDialog.THERAPY_DONE)) {
            Log.d(TAG, "onItemSetupInfo: THERAPY_DONE");

        }else if(description.equalsIgnoreCase(utilDialog.LOCATION_ACK_CON_FAIL)){
            //counterFail=0;
        }
    }

    @Override
    public void onItemSetupAlarm(String name, String description, String location) {

    }

    @Override
    public void onItemPostSelect(int position, String value) {
        Log.d(TAG, "onItemPostSelect: pos" + position + "val:" + value);
        if (value.equalsIgnoreCase(recyclerLocations.LOCATION_VIB_FREQ)) {
            //send data of freq
            sendSpFreq(position, isTherapyOn);
        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_VIB_INT)) {
            Log.d(TAG, "onItemPostSelect: int");
            //send data of Int
            sendSpInt(position, isTherapyOn);
        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_VIB_TIM)) {
            Log.d(TAG, "onItemPostSelect: time");
            //send data of Time
            sendSpTime(position, isTherapyOn);
        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_RB_A)) {
            if (!isTotalBody(mode)) {
                //do not accept commands manualy is total body
                sendSpRBA(position, isTherapyOn);
            }

        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_RB_B)) {
            if (!isTotalBody(mode)) {
                //do not accept commands manualy is total body
                sendSpRBB(position, isTherapyOn);
            }

        }
    }

    //check is Full body or manual
    private boolean isTotalBody(int mode) {
        if ((mode == status.SELECT_MODE_TOTAL_VIBRATION) || (mode == status.SELECT_MODE_TOTAL_PERCUSSION)) {
            return true;
        }
        return false;
    }

    //send data of frequency
    private void sendSpFreq(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.FREQUENCY, setPointsBluetooth.INT_BLE_SP_FREQ1));
                    break;
                case 1:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.FREQUENCY, setPointsBluetooth.INT_BLE_SP_FREQ2));
                    break;
                case 2:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.FREQUENCY, setPointsBluetooth.INT_BLE_SP_FREQ3));
                    break;
                case 3:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.FREQUENCY, setPointsBluetooth.INT_BLE_SP_FREQ4));
                    break;
                case 4:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.FREQUENCY, setPointsBluetooth.INT_BLE_SP_FREQ5));
                    break;
                case 5:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.FREQUENCY, setPointsBluetooth.INT_BLE_SP_FREQ_MAX));
                    break;
            }
        }

    }

    //send data of Int
    private void sendSpInt(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.INTENSITY, setPointsBluetooth.INT_BLE_SP_INT1));
                    break;
                case 1:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.INTENSITY, setPointsBluetooth.INT_BLE_SP_INT2));
                    break;
                case 2:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.INTENSITY, setPointsBluetooth.INT_BLE_SP_INT3));
                    break;
                case 3:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.INTENSITY, setPointsBluetooth.INT_BLE_SP_INT4));
                    break;
                case 4:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.INTENSITY, setPointsBluetooth.INT_BLE_SP_INT5));
                    break;
                case 5:
                    Log.d(TAG, "sendSpInt: 5");
                    //sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.INTENSITY, setPointsBluetooth.INT_BLE_SP_INT5));
                    maxIntensity();
                    break;
            }
        }
    }

    //send data of Time
    private void sendSpTime(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.TIME, setPointsBluetooth.INT_BLE_SP_TIME1));
                    break;
                case 1:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.TIME, setPointsBluetooth.INT_BLE_SP_TIME2));
                    break;
                case 2:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.TIME, setPointsBluetooth.INT_BLE_SP_TIME3));
                    break;
                case 3:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.TIME, setPointsBluetooth.INT_BLE_SP_TIME4));
                    break;
                case 4:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.TIME, setPointsBluetooth.INT_BLE_SP_TIME5));
                    break;
            }
        }
    }

    //send data of RBA
    private void sendSpRBA(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBA, setPointsBluetooth.INT_BLE_SP_TRA1));
                    break;
                case 1:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBA, setPointsBluetooth.INT_BLE_SP_TRA2));
                    break;
                case 2:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBA, setPointsBluetooth.INT_BLE_SP_TRA3));
                    break;
                case 3:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBA, setPointsBluetooth.INT_BLE_SP_TRA4));
                    break;
                case 4:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBA, setPointsBluetooth.INT_BLE_SP_TRA5));
                    break;
            }
        }

    }

    //send data of RBB
    private void sendSpRBB(int pos, boolean enable) {
        if (!enable) {

            switch (pos) {
                case 0:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBB, setPointsBluetooth.INT_BLE_SP_TRB1));
                    break;
                case 1:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBB, setPointsBluetooth.INT_BLE_SP_TRB2));
                    break;
                case 2:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBB, setPointsBluetooth.INT_BLE_SP_TRB3));
                    break;
                case 3:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBB, setPointsBluetooth.INT_BLE_SP_TRB4));
                    break;
                case 4:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.RBB, setPointsBluetooth.INT_BLE_SP_TRB5));
                    break;
            }
        }
    }

    //send data of command
    private void sendSpCommand(int pos, boolean enable) {
        switch (pos) {
            case 0:
                //sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.OPERATION, setPointsBluetooth.INT_BLE_CMD_START));
                break;
            case 1:
                if (!enable) {
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.OPERATION, setPointsBluetooth.INT_BLE_CMD_START));
                }
                break;
            case 2:
                if (enable) {
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.OPERATION, setPointsBluetooth.INT_BLE_CMD_STOP));
                }
                break;
        }
    }

    //send data of RBA
    private void sendSpCooling(int pos, boolean enable) {
        if (!enable) {
            sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.COOLING, pos));
            Log.d(TAG, "sendSpCooling: value:" + pos);
            /*
            switch (pos) {
                case 0:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.COOLING, setPointsBluetooth.INT_BLE_SP_COOL_BOX1));
                    break;
                case 1:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.COOLING, setPointsBluetooth.INT_BLE_SP_COOL_BOX2));
                    break;
                case 2:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.COOLING, setPointsBluetooth.INT_BLE_SP_COOL_BOX3));
                    break;
                case 3:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.COOLING, setPointsBluetooth.INT_BLE_SP_COOL_BOX4));
                    break;
                case 4:
                    sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.COOLING, setPointsBluetooth.INT_BLE_SP_COOL_BOX5));
                    break;
            }*/
        }
    }

    //-----------Feedback-------------//
    //update displayFreq
    private void updateFbFreq(int value) {
        Log.d(TAG, "updateFbFreq: " + value + "size:" + modelBtnFreqArrayList.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                flagIsFreq = updateButtonsFrequencyF(value);
            }
        });
    }

    //update displayInt
    private void updateFbInt(int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                flagIsInt = updateButtonsIntensity(value);
            }
        });
    }

    //update displayInt
    private void updateFbTime(int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                flagIsTim = updateButtonsTime(value);
                memoryLastTimer = value;
                loadDisplayTimerCountFirstTime(memoryLastTimer);
            }
        });
    }

    //update displayRBA
    private void updateFbRBA(int mode, int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                if (mode == status.SELECT_MODE_PERCUSSION) {
                    flagIsTRA = updateButtonsRbAPercussion(value);
                } else if (mode == status.SELECT_MODE_VIBRATION) {
                    flagIsTRA = updateButtonsRbA(value);
                } else if (mode == status.SELECT_MODE_TOTAL_PERCUSSION) {
                    flagIsTRA = updateButtonsRbAPercussion(value);
                } else if (mode == status.SELECT_MODE_TOTAL_VIBRATION) {
                    flagIsTRA = updateButtonsRbA(value);
                }

            }
        });
    }

    //update displayRBB
    private void updateFbRBb(int mode, int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mode == status.SELECT_MODE_PERCUSSION) {
                    flagIsTRB = updateButtonsRbBPercussion(value);
                } else if (mode == status.SELECT_MODE_VIBRATION) {
                    flagIsTRB = updateButtonsRbB(value);
                } else if (mode == status.SELECT_MODE_TOTAL_PERCUSSION) {
                    flagIsTRB = updateButtonsRbBPercussion(value);
                } else if (mode == status.SELECT_MODE_TOTAL_VIBRATION) {
                    flagIsTRB = updateButtonsRbB(value);
                }
            }
        });
    }

    //update feedback commands
    private void updateFbCommands(int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateFbCommands: " + value);
                // Stuff that updates the UI
                int ret = updateCommand(value);
                //get language resources
                Resources resources = getResourcesLanguage(language);
                //
                if (ret == setPointsBluetooth.INT_BLE_CMD_NONE) {
                } else if (ret == setPointsBluetooth.INT_BLE_CMD_START) {
                    updateBtnReady(controlGUI.POS0);
                    launchRunTherapy(valueTimerTherapy, countInterval);
                    //update display with language
                    displayStartCommand(resources.getString(R.string.string_text_btn_running));
                } else if (ret == setPointsBluetooth.INT_BLE_CMD_STOP) {
                    displayStartCommand(resources.getString(R.string.string_text_btn_start));
                    //displayStartCommand("Start");
                    if (isFlagTimerElapsed) {
                        notificationTimerElapsed();
                    } else {
                        forceStopTimerTherapy();
                    }
                } else if (ret == setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC) {//Added 10/19/22
                    Log.d(TAG, "updateFbCommands: setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC");

                    resetCheckBockA();
                    resetCheckBockB();
                    //check mode
                    Log.d(TAG, "updateFbCommands: total perc");
                    mode = status.SELECT_MODE_TOTAL_PERCUSSION;
                    updateFbRBA(mode, setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC);
                    updateFbRBb(mode, setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC);

                } else if (ret == setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB) {//Added 10/19/22
                    Log.d(TAG, "updateFbCommands: total vib");

                    resetCheckBockA();
                    resetCheckBockB();
                    //check mode
                    mode = status.SELECT_MODE_TOTAL_VIBRATION;
                    updateFbRBA(mode, setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB);
                    updateFbRBb(mode, setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB);


                }
            }
        });
    }

    //stop timer
    private void stopTimerEnableStart() {
        //displayStartCommand(resources.getString(R.string.string_text_btn_start));
        //forceStopTimerTherapy();
        //isTherapyOn=false;
        //cancelReady();

        //updateFbCommands(setPointsBluetooth.INT_BLE_CMD_STOP);

    }

    //display start command
    private void displayStartCommand(String input) {
        if (input != null) {
            try {
                if (btnStart != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnStart.setText(input);

                        }
                    });
                }
            } catch (Exception e) {
                Log.d(TAG, "displayStartCommand: ex:" + e.getMessage());
            }
        }
    }

    //display textView Timer
    private void displayTimerMinSec(String time) {
        if (time != null) {
            try {
                tvTimer.setText(time);
            } catch (Exception e) {
                Log.d(TAG, "displayTimerMinSec: ex" + e.getMessage());
            }
        }
    }

    //------------------Operation--------------------//
    //
    private void condStartTherapy(boolean flagIsFreq, boolean flagIsInt, boolean flagIsTim, boolean flagIsTRA, boolean flagIsTRB, boolean flagIsSR) {
        if (true) {
            try {
                // Stuff that updates the UI
                Resources resources = getResourcesLanguage(language);
                //button name with language confirm
                String nameBtnConfirm = resources.getString(R.string.string_btn_SR_confirm);
                //button name with language cancel
                String nameBtnCancel = resources.getString(R.string.string_btn_SR_cancel);

                if (flagIsFreq && flagIsInt && flagIsTim && flagIsTRA && flagIsTRB && !flagIsSR) {

                    //dialog to be display
                    String text = resources.getString(R.string.string_name_lock);


                    //check side rail
                    k9Alert.alertDialogSiderail(dialogSideRailLang, nameBtnConfirm, nameBtnCancel);
                    return;
                } else if (flagIsFreq && flagIsInt && flagIsTim && flagIsTRA && flagIsTRB && flagIsSR) {
                    startTherapy();
                } else {
                    //missing parameter
                    int status = checkMissingParam(flagIsFreq, flagIsInt, flagIsTim, flagIsTRA, flagIsTRB, flagIsSR);
                    Log.d(TAG, "condStartTherapy: status:" + status);
                    if (dialogMissTitle == null) {
                        Log.d(TAG, "condStartTherapy: dialogMissTitle null");
                    }


                    k9Alert.alertDialogMissingPara(dialogMissTitle, status, dialogMissFreq, dialogMissInt, dialogMissTime, dialogMissTra, dialogMissTrb, nameBtnConfirm);
                    return;
                }
            } catch (Exception e) {
                Log.d(TAG, "condStartTherapy: ex:" + e.getMessage());
            }


        }
    }


    //check which parameter is missing
    private int checkMissingParam(boolean frq, boolean intens, boolean time, boolean rba, boolean rbb, boolean siderail) {
        /*insert a bit in position according to the inputs*/
        Bitwise bitwise = new Bitwise();
        Log.d(TAG, "checkMissingParam: time" + time);
        return bitwise.operationBit(frq, intens, time, rba, rbb, siderail);
    }


    //control operation start
    private void startTherapy() {
        ControlGUI controlGUI = new ControlGUI();
        //updateSideRail(controlGUI.POS0);
        sendSpCommand(controlGUI.CMD_ON, isTherapyOn);
    }

    //launch therapy
    private void launchRunTherapy(long time, long countInterval) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                isTherapyOn = true;
                runTimerTherapy(time, countInterval);
            }
        });
    }

    //operation stop therapy
    private void stopTherapy() {
        sendSpCommand(controlGUI.CMD_OFF, isTherapyOn);
        //cleanFlagAfterStop();
        cancelReady();
    }

    //stop system by alarm
    private void stopByEmergency() {
        Log.d(TAG, "stopByEmergency: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                stopTherapy();
                isAlarm = true;
            }
        });


    }

    //Total percussion
    private void totalPercussion() {

        sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.OPERATION, setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC));
    }

    //Total percussion
    private void totalVibration() {
        sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.OPERATION, setPointsBluetooth.INT_BLE_CMD_TOTAL_VIB));
    }

    //clean flag after stop
    private void cleanFlagAfterStop() {
        isFlagIsSr = false;
        isTherapyOn = false;
    }

    //system connected
    private boolean systemConnected() {
        //alertDialogLoading(false);
        updateSideRail(controlGUI.POS1);
        //alertDialogBuilder=null;

        new Thread() {
            public void run() {
                alertDialog.dismiss();
            }
        }.start();
        return false;
    }

    //system lost connection
    private void systemLostConnection(boolean input) {
        if (input) {

            try {
                Log.d(TAG, "systemLostConnection: alert");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        k9Alert.alertDialogConnectionFail(utilDialog.LOCATION_CONFIRM_CONN_FAILED);
                    }
                });

            } catch (Exception e) {
                Log.d(TAG, "systemLostConnection: ex" + e.getMessage());
            }



            /*counterFail++;
            Log.d(TAG, "systemLostConnection: counter:"+ counterFail);
            if (counterFail > MAX_CON) {
                try {
                    Log.d(TAG, "systemLostConnection: alert");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            k9Alert.alertDialogConnectionFail(utilDialog.LOCATION_CONFIRM_CONN_FAILED);
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "systemLostConnection: ex" + e.getMessage());
                }
            }*/


        }
    }

    //force to stop Therapy
    private boolean forceStopTimerTherapy() {
        stopTherapy();
        cleanFlagAfterStop();
        isTherapyOn = false;
        cleanTimerTherapy();
        cleanDisplayTimer();
        //
        saveInMemTransd(new_transdA, new_transdB);
        updateSideRail(controlGUI.POS1);
        //changeStrokeBtnStart(controlGUI.POS0);
        updateBtnReady(controlGUI.POS0);

        //
        loadDisplayTimerCountFirstTime(memoryLastTimer);
        return true;
    }

    //-------------loading-------------------------//
    public void alertDialogLoading(boolean input) {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.layout_loading_system, null);
            alertDialogBuilder = new AlertDialog.Builder(this);
            //alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setView(promptsView);

            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvDialogLoading);
            //get language
            Resources resources = getResourcesLanguage(language);
            String text = resources.getString(R.string.string_text_dial_loading);
            //set text
            if (text != null) {
                tvTextDialogSR.setText(text);
            }


            // set dialog message
            alertDialogBuilder
                    .setCancelable(false);
            // create alert dialog
            alertDialog = alertDialogBuilder.create();
            // show it

            if (input) {
                alertDialog.show();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        //alertDialog.dismiss();
                    }
                });

            }
        } catch (Exception e) {
            Log.d(TAG, "alertDialogLoading ex: " + e.getMessage());
        }
    }

    public AlertDialog.Builder getDialogProgressBar() {

        if (builder == null) {
            builder = new AlertDialog.Builder(this);

            builder.setTitle("Loading...");

            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }
    //-------------Timers--------------------------//

    //clean timer
    private boolean cleanTimerTherapy() {
        try {
            if (timerTherapy != null) {
                timerTherapy.cancel();
                timerTherapy = null;
                flagIsMinZero = false;
                //  disableWhenTheraStart();
                return true;
            }
        } catch (Exception e) {
            Log.d(TAG, "cancelTimerTherapy: ex:" + e.getMessage());
        }
        return false;
    }

    //timer therapy
    private void runTimerTherapy(long time, long countInterval) {
        try {
            cleanTimerTherapy();
            Log.d(TAG, "runTimerTherapy: time:" + time + ". Interval:" + countInterval);

            timerTherapy = new CountDownTimer(time, countInterval) {
                @Override
                public void onTick(long l) {
                    //Log.d(TAG, "onTick: timer sec:"+l/1000);
                    //
                    if (sec == 0) {
                        //discount minutes between 0 and 9
                        if (min >= 0 && min <= 9) {
                            if (min > 0) {
                                min--;
                            } else {
                                min = 0;
                                flagIsMinZero = true;
                            }
                            strMin = "0" + String.valueOf(min);
                        } else if (min > 9) {
                            min--;
                            strMin = String.valueOf(min);
                        }
                        //avoid when 59 when nee to be all in zero

                        if (!flagIsMinZero) {
                            sec = 59;
                            strSec = String.valueOf(sec);
                        } else {
                            sec = 0;
                            strSec = "0" + String.valueOf(sec);
                        }
                    } else {
                        sec--;
                        if (sec >= 0 && sec <= 9) {
                            strSec = "0" + String.valueOf(sec);
                            //check minutes
                            if (min >= 0 && min <= 9) {
                                strMin = "0" + String.valueOf(min);
                            } else {
                                strMin = String.valueOf(min);
                            }
                        } else {
                            strSec = String.valueOf(sec);
                            //check minutes
                            if (min >= 0 && min <= 9) {
                                strMin = "0" + String.valueOf(min);
                            } else {
                                strMin = String.valueOf(min);
                            }
                        }
                    }
                    Log.d(TAG, "onTick:strMin: " + strMin + ":strSec" + strSec);
                    //display value
                    displayTimerMinSec(String.valueOf(strMin + strDiv + strSec));

                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "onFinish: therapy done!");
                    timerTherapyElapsed();
                }
            }.start();

        } catch (Exception e) {
            Log.d(TAG, "runTimerTherapy: ex:" + e.getMessage());
        }

    }

    // timer finish therapy done
    private void timerTherapyElapsed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                sendSpCommand(controlGUI.CMD_OFF, isTherapyOn);
                isFlagTimerElapsed = true;
            }
        });


    }

    //norification timer therapy elapsed
    private void notificationTimerElapsed() {
        k9Alert.alertDialogTherapyDone(dialogTherapyCompleteLang, dialogConfirmLang);
        isTherapyOn = false;
        beep.beep_disable();
        //cleanDisplayTimer();
        forceStopTimerTherapy();
        isFlagTimerElapsed = false;
        loadDisplayTimerCountFirstTime(memoryLastTimer);
    }

    //set display time 00:00
    private void cleanDisplayTimer() {
        displayTimerMinSec(mTagReference.TIME_ZERO);
        sec = 59;
    }

    //load display time on the screen. This is temporal
    private boolean loadDisplayTimerCountFirstTime(int value) {
        String strTime = mTagReference.TIME_ZERO;

        Log.d(TAG, "loadDisplayTimerCountFirstTime: value:" + value);
        int intTime = 0;

        if (value > 0) {
            try {
                if (value == setPointsBluetooth.INT_BLE_SP_TIME_NONE) {
                    intTime = mTagReference.SELECTED_INT_TIME_0;
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME1) {
                    intTime = mTagReference.SELECTED_INT_TIME_1;
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME2) {
                    intTime = mTagReference.SELECTED_INT_TIME_2;
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME3) {
                    intTime = mTagReference.SELECTED_INT_TIME_3;
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME4) {
                    intTime = mTagReference.SELECTED_INT_TIME_4;
                } else if (value == setPointsBluetooth.INT_BLE_SP_TIME5) {
                    intTime = mTagReference.SELECTED_INT_TIME_5;
                }

                //load timer value converter for the count down timer
                valueTimerTherapy = utilTimer.timerCalc(intTime);
                Log.d(TAG, "loadDisplayTimerCountFirstTime:intTime: " + intTime);
                Log.d(TAG, "loadDisplayTimerCountFirstTime:  valueTimerTherapy:" + valueTimerTherapy);
                //start to count value form 1 second before
                min = intTime - 1;
                Log.d(TAG, "loadDisplayTimerCountFirstTime: minutes:" + min);
                //display value on display
                displayTimerMinSec(utilTimer.convertIntTimeStr(intTime));
                return true;
            } catch (Exception e) {
                Log.d(TAG, "updateButtonsTime: " + e.getMessage());
            }
        }
        displayTimerMinSec(utilTimer.convertIntTimeStr(mTagReference.SELECTED_INT_TIME_0));
        return false;
    }

    //--------------------Cooling system--------------------//
    private String saveInMemTransd(int new_transdA, int new_transdB) {
        Cooling cooling = new Cooling();
        //save in memory
        old_transdA = new_transdA;
        old_transdB = new_transdB;
        //get module to coold down

        module = cooling.checkCoolingModules(old_transdA, old_transdB, true);
        //
        Log.d(TAG, "saveInMemTransd:old_transdA: " + old_transdA + ".old_transdB" + old_transdB);
        Log.d(TAG, "saveInMemTransd: module:" + module);

        //send module A
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //do something
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        Log.d(TAG, "run: delay 2s");
                        sendSpCooling(module, isTherapyOn);
                    }
                });
            }
        }, 2000);//time in milisecond


/*
        if(module>=0 && module<9){
            //send just 1 paq
            sendSpCooling(module, isTherapyOn);
        }else{
            //send 2 paq
            moduleA=cooling.getModA(module);
            moduleB=cooling.getModB(module);
            Log.d(TAG, "saveInMemTransd: moduleA:"+ moduleA);
            Log.d(TAG, "saveInMemTransd: moduleB:"+ moduleB);
            //send module A
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    sendSpCooling(moduleA, isTherapyOn);
                }
            });
            //send module B
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    sendSpCooling(moduleB, isTherapyOn);
                }
            });
        }*/
        return "0";
    }


    //max intensity
    private void maxIntensity() {
       /* Log.d(TAG, "maxIntensity: ");
        //BleKz.run();
        BluetoothSocket btSocket = null;
        BluetoothDevice device = null;

        BluetoothAdapter bt;

        BluetoothAdapter bleAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        Log.d(TAG, "maxIntensity: " + bleAdapter.toString());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        Set<BluetoothDevice> pairedDevices = bleAdapter.getBondedDevices();
        Log.d(TAG, "maxIntensity: paired:" + pairedDevices);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(uuid.UUID_SERVICE_COMMANDS);
            btSocket.

        } catch (Exception e) {
            Log.d(TAG, "maxIntensity: ex:" + e.getMessage());
        }
        try {
            btSocket.connect();
        } catch (IOException e) {

        }*/
    }

    /*init system*/

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

    //lock mode
    private void lockMode(boolean input) {
        isLockMode = input;
    }

    //cancel ready
    private void cancelReady() {
        lockMode(false);
        isFlagIsSr = false;
        updateBtnReady(controlGUI.POS0);//invisible ready
    }

    //notifications
    private void notificationSystemRestart() {
        Log.d(TAG, "notificationSystemRestart: ");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                k9Alert.alertDialogSystemEmergencyStop(dialogEmergStop, dialogConfirmLang);
                isAlarm = true;
            }
        });
    }

    //notifications
    private void notificationHardwareFail() {
        Log.d(TAG, "notificationSystemRestart: ");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                k9Alert.alertDialogSystemEmergencyStop(dialogHardwareFail, dialogConfirmLang);
                isAlarm = true;
            }
        });
    }


    //set alarm
    private void setAlarmEmerg(boolean input) {

    }

    //watchdog timer
    private void watchDogTimerCom() {
        Log.d(TAG, "watchDogTimerCom: ");
        if (watchDogTimer != null) {
            watchDogTimer.cancel();
            watchDogTimer = null;
        }
        //
        watchDogTimer = new CountDownTimer(TIMER_WACHT_DOG, 1000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "watchDogTimer counter: " + l / 1000);
            }

            @Override
            public void onFinish() {
                wachtDogTimerElapsed();
            }
        }.start();
    }

    //watchdog
    private void wachtDogTimerElapsed() {
        Log.d(TAG, "wachtDogTimerElapsed: ");
        isTherapyOn = false;
        cleanDisplayTimer();
        stopByEmergency();
        notificationHardwareFail();
    }
}