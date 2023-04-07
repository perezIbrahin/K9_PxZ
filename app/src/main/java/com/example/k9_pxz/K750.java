package com.example.k9_pxz;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Alert.K9Alert;
import Bluetooth.ActionGatt;
import Bluetooth.BLE_Format;
import Bluetooth.BleCharacteristics;
import Bluetooth.Ble_Protocol;
import Bluetooth.UUID;
import Interface.InterfaceSetupInfo;
import Util.Beep;
import Util.ConcatDataWriteBle;
import Util.ControlGUI;
import Util.Default_values;
import Util.IntToArray;
import Util.Key_Util;
import Util.RecyclerLocations;
import Util.SetPoints;
import Util.TagRefrence;
import Util.TextSize;
import Util.Util_Dialog;
import Util.Util_timer;

public class K750 extends AppCompatActivity implements InterfaceSetupInfo {
    private static final String TAG = "K750";

    /*
     * Project : Percussion Vibration
     * Desc: Percussion /Vibration
     * Rev:3.0.1
     * Prog:Ibrahim
     * Date:09/28/22
     * */

    private TextView tvK750Rev1;
    private TextView tvCon;

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


    //
    private boolean isLockMode = false;
    private int mode = 0;
    private int mProtocol = 0;
    private int mSp = 0;
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
    //
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
    //Bluetooth
    private String myBleAdd = "00:00:00:00:00";//Address used to connect BLE
    private boolean bleIsScanner = false;

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

    //SendData BleKz=new SendData();

    AlertDialog.Builder builder;
    private boolean isEnableGui = false;
    private boolean isFlagSetConnection = false;
    //
    private boolean flagConnSuc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.activity_k750);
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
        //

    }

    //loading dialog
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //bluetooth scan
        launchBleScan();
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
        /*btnSelectPer = findViewById(R.id.btnSelPerc);
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


        tvPresStart = findViewById(R.id.tvReady);
        //
        tvTextFrq = findViewById(R.id.tvTextFreq);
        tvTextInt = findViewById(R.id.tvtextInt);
        tvTextTime = findViewById(R.id.tvTextTime);
        //
        tvTitle = findViewById(R.id.tvTextPvTitile);
        tvCurrent = findViewById(R.id.tvCurrent);*/
        tvCon = findViewById(R.id.tvCon);
        tvK750Rev1 = findViewById(R.id.tvK750Rev);
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

    @Override
    public void onItemSetupInfo(String name, String description) {
        Util.Message message = new Util.Message();
        Log.d(TAG, "onItemSetupInfo: " + description);
        if (description.equalsIgnoreCase(utilDialog.LOCATION_EXIT_THERAPY)) {
            //isFlagIsSr = true;
            //updateSideRail(controlGUI.POS0);
            //displayOperation(message.MESSAGE_SYSTEM_READY);
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_CONN_FAILED)) {
            //goHome();
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_SIDERAIL)) {//
            //isFlagIsSr = true;
            //updateSideRail(controlGUI.POS0);
            //condStartTherapy(flagIsFreq, flagIsInt, flagIsTim, flagIsTRA, flagIsTRB, isFlagIsSr);

            //changed 10/19/22
            // displayOperation(displayOperations.DISPLAY_OPE_READY);//Ready
            updateBtnReady(controlGUI.POS1);
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_EMERGENCY_STOP_CONFIRM)) {
            Log.d(TAG, "onItemSetupInfo: emergency stop");
            //goHome();
        } else if (description.equalsIgnoreCase(utilDialog.THERAPY_DONE)) {
            Log.d(TAG, "onItemSetupInfo: THERAPY_DONE");

        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_ACK_CON_FAIL)) {
            //counterFail=0;
        }
    }

    @Override
    public void onItemSetupAlarm(String name, String description, String location) {

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
            tvK750Rev1.setText(revision);
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

                //d();
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
            //displayRssiValue(rssi);
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
               // modelBtnFreqArrayList.clear();
               // updateFbFreq(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.INTENSITY) {
               // modelBtnIntArrayList.clear();
                //updateFbInt(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.TIME) {
                //modelBtnTimeArrayList.clear();
               // updateFbTime(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.OPERATION) {
               // updateFbCommands(mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.RBA) {
                Log.d(TAG, "getParamUpdateValueGui3: RBA");
                // modelBtnArrayListA.clear();
                //modelIconArrayListA.clear();
               // resetCheckBockA();
                //check mode
                //updateFbRBA(mode, mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.RBB) {
               // resetCheckBockB();
                //modelBtnArrayListB.clear();
                //modelIconArrayListB.clear();
                //updateFbRBb(mode, mSp);
                beepSound();
            } else if (mProtocol == bleProtocol.COOLING) {
                Log.d(TAG, "getParamUpdateValueGui3:cooling");
               // controlIconCoolingTransd(mSp);
            }
            //alarm
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // checkStatusAlarms(mValue2);
                }
            });

            //power sensor
            //displayCurrent(mValue3);
            //wacht dog
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //watchDogTimerCom();
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
               // Serial_Number_Product = bundle.getString("serial_number");
                //Log.d(TAG, "getExtrasFromAct: serial_number:" + Serial_Number_Product);

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


    //indication therapy is ready
    private void updateBtnReady(int value) {
        try {
            Default_values default_values = new Default_values();
            AnimationDrawable rocketAnimation;
            switch (value) {
                case 0:
                    //stop animation
                    //btnReady.setVisibility(View.INVISIBLE);
                    //displaytextStart(false);
                    //lockMode(false);

                    break;
                case 1: //alert
                    //btnReady.setVisibility(View.INVISIBLE);
                    //btnReady.setVisibility(View.VISIBLE);
                    //displaytextStart(true);
                    //displaytextStart(false);
                    //lockMode(true);
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

    //Toast message
    private void toastMessageInt(int message) {
        if (message > 0) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /*---------------display------------------------*/
    //updateConnectionState
    private void updateConnectionState(int input) {
        Log.d(TAG, "updateConnectionState: " + input);
        //Resources resources = getResourcesLanguage(language);


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

    //-----------Enable---------------------//
    private void enableGui(boolean input) {
        Log.d(TAG, "enableGui: " + input);

        if (input) {
            if (!isEnableGui) {
                //load view
                // updateButtonsFrequencyF(setPointsBluetooth.INT_BLE_SP_FREQ_NONE);//frequency
                //updateButtonsIntensity(setPointsBluetooth.INT_BLE_SP_INT_NONE);//intensity
                //updateButtonsTime(setPointsBluetooth.INT_BLE_SP_TIME_NONE);//time
                //updateCommand(setPointsBluetooth.INT_BLE_CMD_NONE);
                isEnableGui = true;
            }

        } else {
            // updateButtonsFrequencyF(setPointsBluetooth.INT_BLE_SP_CLEAN);
            isEnableGui = false;
        }
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


}