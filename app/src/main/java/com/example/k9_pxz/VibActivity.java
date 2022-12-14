package com.example.k9_pxz;

import static com.example.k9_pxz.K9Activity.ACTION_GATT_SERVICES_DISCOVERED;
import static Bluetooth.UUID.UUID_CHARACTERISTIC_COMMANDS;
import static Bluetooth.UUID.UUID_CHARACTERISTIC_FREQUENCY;
import static Bluetooth.UUID.UUID_CHARACTERISTIC_INTENSITY;
import static Bluetooth.UUID.UUID_CHARACTERISTIC_PROCESS_VALUE;
import static Bluetooth.UUID.UUID_CHARACTERISTIC_TIME;
import static Bluetooth.UUID.UUID_CHARACTERISTIC_TRANSDUCERS;
import static Bluetooth.UUID.UUID_SERVICE_COMMANDS;
import static Bluetooth.UUID.UUID_SERVICE_FREQUENCY;
import static Bluetooth.UUID.UUID_SERVICE_INTENSITY;
import static Bluetooth.UUID.UUID_SERVICE_PROCESS_VALUE;
import static Bluetooth.UUID.UUID_SERVICE_TIME;
import static Bluetooth.UUID.UUID_SERVICE_TRANSDUCERS;
import static Util.ColdDown.MODULE1;
import static Util.ColdDown.MODULE2;
import static Util.ColdDown.MODULE3;
import static Util.ColdDown.MODULE4;
import static Util.ColdDown.MODULE5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Alert.CustomAlert;
import Bluetooth.BluetoothCharacter;
import Interface.RecyclerViewClickInterface;
import Util.BluetoothStatus;
import Util.ColdDown;
import Util.DayOfWeek;
import Util.Feedback;
import Util.Flags;
import Util.GenerateSound;
import Util.SendReceive;
import Util.SetPoints;
import Util.Setpoint;
import Util.SleepMode;
import Util.Status;
import Util.TagRefrence;
import Util.TherapyMessage;
import Util.ToastMessage;

public class VibActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickInterface {
    private static final String TAG = "VibActivity";
    //private static final BluetoothGattCharacteristic TODO = ;
    //GUI
    ImageView ivIcon;

    //Old GUI
    //buttons
    private Button btnMaxInflate = null;
    private Button btnPercussion = null;
    private Button btnVibration = null;
    private Button btnStart = null;
    private Button btnStop = null;
    //frequency
    private Button btnFreq1 = null;
    private Button btnFreq2 = null;
    private Button btnFreq3 = null;
    private Button btnFreq4 = null;
    private Button btnFreq5 = null;
    //intensity
    private Button btnInt1 = null;
    private Button btnInt2 = null;
    private Button btnInt3 = null;
    private Button btnInt4 = null;
    private Button btnInt5 = null;
    //timer
    private Button btnTimer1 = null;
    private Button btnTimer2 = null;
    private Button btnTimer3 = null;
    private Button btnTimer4 = null;
    private Button btnTimer5 = null;
    //wake-up/sleep
    private Button btnSync = null;//no
    //siderail
    private Button srLeft = null;
    private Button srRigh = null;

    private Button btnWakeUpMode = null;
    private Button btnSleepMode = null;

    private Button btnGoHome;

    //radio buttons
    private RadioButton rbA1 = null;
    private RadioButton rbA2 = null;
    private RadioButton rbA3 = null;
    private RadioButton rbA4 = null;
    private RadioButton rbA5 = null;
    private RadioButton rbB1 = null;
    private RadioButton rbB2 = null;
    private RadioButton rbB3 = null;
    private RadioButton rbB4 = null;
    private RadioButton rbB5 = null;
    //image buttons
    private ImageView ivA1 = null;
    private ImageView ivA2 = null;
    private ImageView ivA3 = null;
    private ImageView ivA4 = null;
    private ImageView ivA5 = null;
    private ImageView ivB1 = null;
    private ImageView ivB2 = null;
    private ImageView ivB3 = null;
    private ImageView ivB4 = null;
    private ImageView ivB5 = null;
    private ImageView ivBody = null;

    //textview
    private TextView tvTitlePage = null;
    private TextView tvFBMode = null;
    private TextView tvFBZone = null;
    private TextView tvFBFreq = null;
    private TextView tvFBInt = null;
    private TextView tvFBTime = null;
    private TextView tvFBStatus = null;
    private TextView tvSleepMode = null;
    private TextView tvConnection = null;
    private TextView tvClient = null;
    private TextView tvInfoConnection = null;
    private TextView tvLabelFreq = null;
    private TextView tvLabelInt = null;
    private TextView tvLabelTime = null;
    private TextView tvLabelProg = null;
    private TextView tvWriteCmd = null;
    private TextView tvSensors = null;

    private ImageView ivMCU = null;
    ImageView ivNotification = null;

    //ProgressDialog
    private ProgressDialog progressDialog;

    //Alert Dialog
    AlertDialog alertDialogForceStop = null;
    private boolean isAlertDialogForceStopShowing = false;

    //variables
    private String myBleAdd2 = "90:FD:9F:0A:F5:F6";
    private String myBleAdd = "90:FD:9F:0A:F5:F6";//Address used to connect BLE
    private boolean bleIsScanner = false;
    // setup UI handler
    private final static int UPDATE_DEVICE = 0;
    private final static int UPDATE_VALUE = 1;
    String value = null;
    //
    long varTimerTherapy = 1000;//time for the therapy
    long varCheckNetwork = 5000;//time to check network
    //get value ofTimer
    private int memMin = 9;
    private int min = 9;
    private int sec = 59;
    //
    private int spFrequencyMem = 0;
    //mode
    public int MODE_PERCUSSION = 1;
    public int MODE_PERCUSSION_VIBRATION = 2;
    //storage trsnaducer operation
    private int currentTransdA = 0;
    private int currentTransdB = 0;
    private int savedTransdA = 0;
    private int savedTransdB = 0;
    //feedback
    private boolean isDisableFeedback = false;
    //request data
    private int counterRequestPV = 0;
    private int MAX_CounterRequestPV = 5;
    //variables operation
    private boolean isTherapyOn = false;
    private boolean isDone = false;
    private int visTransdA = 0;//visible icons with the location A
    private int visTransdB = 0;//visible icons with the location B
    private boolean isSleep = false;//status of the mode of the table (Sleep/Wake)
    //Flags
    private boolean isNewFeq = false;
    private boolean isNewInt = false;
    private boolean isNewTime = false;
    private boolean isNewStart = false;
    private boolean isNewStop = false;
    private boolean isNewRB = false;
    private boolean isCloudEnable = false;// enable to upload to the cloud
    //progress bar
    private int progressBarStatus = 0;
    private long fileSize = 0;
    //cooling
    private boolean isCoolingBox1 = false;
    private boolean isCoolingBox2 = false;
    private boolean isCoolingBox3 = false;
    private boolean isCoolingBox4 = false;
    private boolean isCoolingBox5 = false;
    private boolean isCoolingStart = false;
    //send data
    String s = "go";
    //
    int pulse = 0;
    AnimationDrawable animationNotificationDraw = null;
    AnimationDrawable animationNotificationDrawB = null;
    private ImageView ivTrA = null;
    private ImageView ivTrB = null;


    //Bluetooth
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mGatt;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothLeScanner mBLEScanner;
    public LeDeviceListAdapter mLeDeviceListAdapter;

    //Bluetooth characterics
    BluetoothGattCharacteristic CHARACTERISTIC_FREQUENCY;
    BluetoothGattCharacteristic CHARACTERISTIC_INTENSITY;
    BluetoothGattCharacteristic CHARACTERISTIC_TIME;
    BluetoothGattCharacteristic CHARACTERISTIC_TRANSDUCERS;
    BluetoothGattCharacteristic CHARACTERISTIC_COMMANDS;
    BluetoothGattCharacteristic CHARACTERISTIC_PROCESS_VALUE;

    //countdown
    CountDownTimer timerCheckNetwork = null;
    CountDownTimer timerTherapy = null;
    CountDownTimer timerForceSop = null;
    CountDownTimer timerLockColdDown = null;

    /*Class*/
    private Feedback feedback = new Feedback();
    private Flags flags = new Flags();
    private BluetoothStatus bluetoothStatus = new BluetoothStatus();
    // private CloudStatus cloudStatus = new CloudStatus();
    private DayOfWeek dayOfWeek = new DayOfWeek();
    private TherapyMessage therapyMessage = new TherapyMessage();
    private ToastMessage toastMessage = new ToastMessage();
    private TagRefrence mTagReference = new TagRefrence();
    private SleepMode sleepMode = new SleepMode();
    private SetPoints setPointsBluetooth = new SetPoints();
    private GenerateSound generateSound = new GenerateSound();
    private Handler progressBarbHandler = new Handler();
    private SendReceive sendReceive = new SendReceive();//list of value
    private Flags mFlags = new Flags();
    private Setpoint setpoint = new Setpoint();// get list of setpoints
    private CustomAlert customAlert;

    //Bluetooth extras
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";


    /*
     * get timer selected for transducers
     * */
    //get in memory the time that transducer work to use it for cooling down
    private int TIMER_MODULE_1 = 0;
    private int TIMER_MODULE_2 = 0;
    private int TIMER_MODULE_3 = 0;
    private int TIMER_MODULE_4 = 0;
    private int TIMER_MODULE_5 = 0;
    private int TRANSD_SELECTED_OLD = 0;
    private int TRANSD_SELECTED_NEW = 0;


    //Class
    public BluetoothCharacter bluetoothCharacter = new BluetoothCharacter();
    public Status statusSystem = new Status();//status of the system

    AlertDialog alt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vib);
        initGUI();//Init GUI
        initBLE();// Init Bluetooth

        /*
         * enable  GUI stuff
         * */
        //Permissions
        checkPermissions();
        // Boadcast
        broadCastReceiver();

        //from old design
        firstTime();//init method just for the first time
        eventControl();//events
        getExtrasFromAct();//get extras
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: ");
        try {
            //Enable Scan
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //Enable Bluetooth
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    Log.d(TAG, "onPostResume: ActivityCompat#requestPermissions");
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                   // return;
                }
                startActivityForResult(enableBtIntent, bluetoothCharacter.REQUEST_ENABLE_BLE);
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    Log.d(TAG, "initBLE:SDK_INT>=21");
                    mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                    settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .build();
                    filters = new ArrayList<ScanFilter>();
                    mBLEScanner.startScan(mScanCallback);
                    bleIsScanner = true;
                    // Initializes list view adapter.
                    mLeDeviceListAdapter = new LeDeviceListAdapter();
                    //setListAdapter(mLeDeviceListAdapter);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "onPostResume: " + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                mLeDeviceListAdapter.clear();
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

    @Override
    protected void onStop() {
        super.onStop();
        close();
    }

    //events btn
    private void eventControl() {
        if (true) {
            //btn
            btnMaxInflate.setOnClickListener(this);
            btnGoHome.setOnClickListener(this);
            btnPercussion.setOnClickListener(this);
            btnVibration.setOnClickListener(this);
            btnStart.setOnClickListener(this);
            btnStop.setOnClickListener(this);
            //set point of frequency
            btnFreq1.setOnClickListener(this);
            btnFreq2.setOnClickListener(this);
            btnFreq3.setOnClickListener(this);
            btnFreq4.setOnClickListener(this);
            btnFreq5.setOnClickListener(this);
            //set point of intensity
            btnInt1.setOnClickListener(this);
            btnInt2.setOnClickListener(this);
            btnInt3.setOnClickListener(this);
            btnInt4.setOnClickListener(this);
            btnInt5.setOnClickListener(this);
            //set point of time
            btnTimer1.setOnClickListener(this);
            btnTimer2.setOnClickListener(this);
            btnTimer3.setOnClickListener(this);
            btnTimer4.setOnClickListener(this);
            btnTimer5.setOnClickListener(this);
            //btn sleep
            //btnSync.setOnClickListener(this);
            btnSleepMode.setOnClickListener(this);
            btnWakeUpMode.setOnClickListener(this);

            //radio buttons
            rbA1.setOnClickListener(this);
            rbA2.setOnClickListener(this);
            rbA3.setOnClickListener(this);
            rbA4.setOnClickListener(this);
            rbA5.setOnClickListener(this);
            rbB1.setOnClickListener(this);
            rbB2.setOnClickListener(this);
            rbB3.setOnClickListener(this);
            rbB4.setOnClickListener(this);
            rbB5.setOnClickListener(this);
            //get Location
            //ivLocation.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        try {
            if (view == btnMaxInflate) {

            } else if (view == btnPercussion) {//mode percussion
                selectedMode(MODE_PERCUSSION);
            } else if (view == btnVibration) {//mode vibration
                selectedMode(MODE_PERCUSSION_VIBRATION);
            } else if (view == btnStart) {//start therapy
                if (checkTransducersSelected(currentTransdA, currentTransdB)) {
                    beforeOnTherapy(setPointsBluetooth.INT_BLE_CMD_START);
                } else {
                    alertDialogCheckTransd();
                }
            } else if (view == btnStop) {//stop therapy
                forceStop();
            } else if (view == rbA1) {
                sendDataTransdABLE(setPointsBluetooth.INT_BLE_SP_TRA1);
            } else if (view == rbA2) {
                sendDataTransdABLE(setPointsBluetooth.INT_BLE_SP_TRA2);
            } else if (view == rbA3) {
                sendDataTransdABLE(setPointsBluetooth.INT_BLE_SP_TRA3);
            } else if (view == rbA4) {
                sendDataTransdABLE(setPointsBluetooth.INT_BLE_SP_TRA4);
            } else if (view == rbA5) {
                sendDataTransdABLE(setPointsBluetooth.INT_BLE_SP_TRA5);
            } else if (view == rbB1) {
                sendDataTransdBBLE(setPointsBluetooth.INT_BLE_SP_TRB1);
            } else if (view == rbB2) {
                sendDataTransdBBLE(setPointsBluetooth.INT_BLE_SP_TRB2);
            } else if (view == rbB3) {
                sendDataTransdBBLE(setPointsBluetooth.INT_BLE_SP_TRB3);
            } else if (view == rbB4) {
                sendDataTransdBBLE(setPointsBluetooth.INT_BLE_SP_TRB4);
            } else if (view == rbB5) {
                sendDataTransdBBLE(setPointsBluetooth.INT_BLE_SP_TRB5);
            } else if (view == btnFreq1) {/* Set point   frequency */
                spFrequencyMem = setPointsBluetooth.INT_BLE_SP_FREQ1;
                Log.d(TAG, "onClick:btnFreq1 " + spFrequencyMem);
                sendDataFreqBLE(setPointsBluetooth.INT_BLE_SP_FREQ1);
            } else if (view == btnFreq2) {
                spFrequencyMem = setPointsBluetooth.INT_BLE_SP_FREQ2;
                Log.d(TAG, "onClick:btnFreq2 " + spFrequencyMem);
                sendDataFreqBLE(spFrequencyMem);
            } else if (view == btnFreq3) {
                spFrequencyMem = setPointsBluetooth.INT_BLE_SP_FREQ3;
                Log.d(TAG, "onClick:btnFreq3 " + spFrequencyMem);
                sendDataFreqBLE(spFrequencyMem);
            } else if (view == btnFreq4) {
                Log.d(TAG, "onClick:btnFreq4 ");
                spFrequencyMem = setPointsBluetooth.INT_BLE_SP_FREQ4;
                sendDataFreqBLE(spFrequencyMem);
            } else if (view == btnFreq5) {
                Log.d(TAG, "onClick:btnFreq5 ");
                spFrequencyMem = setPointsBluetooth.INT_BLE_SP_FREQ5;
                sendDataFreqBLE(spFrequencyMem);
            } else if (view == btnInt1) {
                sendDataIntBLE(setPointsBluetooth.INT_BLE_SP_INT1);
            } else if (view == btnInt2) {
                sendDataIntBLE(setPointsBluetooth.INT_BLE_SP_INT2);
            } else if (view == btnInt3) {
                sendDataIntBLE(setPointsBluetooth.INT_BLE_SP_INT3);
            } else if (view == btnInt4) {
                sendDataIntBLE(setPointsBluetooth.INT_BLE_SP_INT4);
            } else if (view == btnInt5) {
                sendDataIntBLE(setPointsBluetooth.INT_BLE_SP_INT5);
            } else if (view == btnTimer1) {
                sendDataTimerBLE(setPointsBluetooth.INT_BLE_SP_TIME1);
            } else if (view == btnTimer2) {
                sendDataTimerBLE(setPointsBluetooth.INT_BLE_SP_TIME2);
            } else if (view == btnTimer3) {
                sendDataTimerBLE(setPointsBluetooth.INT_BLE_SP_TIME3);
            } else if (view == btnTimer4) {
                sendDataTimerBLE(setPointsBluetooth.INT_BLE_SP_TIME4);
            } else if (view == btnTimer5) {
                sendDataTimerBLE(setPointsBluetooth.INT_BLE_SP_TIME5);
            } else if (view == btnSleepMode) {//sleep mode
                Toast.makeText(this, "Disable function!", Toast.LENGTH_SHORT).show();
                //wakeUp(false);
            } else if (view == btnWakeUpMode) {//wake up
               wakeUp(true);
            } else if (view == btnGoHome) {
                goHome();
            }

        } catch (Exception e) {
            Log.d(TAG, "onClick: exception" + e.getMessage());
        }
    }

    //-------------init--------------
    //  first time
    private void firstTime() {
        //
        customAlert = new CustomAlert(this, VibActivity.this);


        initVisGUI();
        displayMode(mTagReference.SELECTED_ZONE_UNKNOWN);
        displayTimerMinSec(mTagReference.TIME_ZERO);
        runLoadingSystem(true);//loading dialog
    }

    //init GUI
    private void initGUI() {
        ivIcon = findViewById(R.id.ivConnection);
        initOldGui();
    }

    //private void old gui
    private void initOldGui() {
        //buttons
        btnMaxInflate = findViewById(R.id.btnMaxInflate);
        btnGoHome = findViewById(R.id.btnHome);
        btnPercussion = (Button) findViewById(R.id.btnPercussion);
        btnVibration = (Button) findViewById(R.id.btnVibration);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        //btn frequency
        btnFreq1 = (Button) findViewById(R.id.btn_freq_sp1);
        btnFreq2 = (Button) findViewById(R.id.btn_freq_sp2);
        btnFreq3 = (Button) findViewById(R.id.btn_freq_sp3);
        btnFreq4 = (Button) findViewById(R.id.btn_freq_sp4);
        btnFreq5 = (Button) findViewById(R.id.btn_freq_sp5);
        //btn intensity
        btnInt1 = (Button) findViewById(R.id.btn_int_sp1);
        btnInt2 = (Button) findViewById(R.id.btn_int_sp2);
        btnInt3 = (Button) findViewById(R.id.btn_int_sp3);
        btnInt4 = (Button) findViewById(R.id.btn_int_sp4);
        btnInt5 = (Button) findViewById(R.id.btn_int_sp5);
        //btn timer
        btnTimer1 = (Button) findViewById(R.id.btn_time_sp1);
        btnTimer2 = (Button) findViewById(R.id.btn_time_sp2);
        btnTimer3 = (Button) findViewById(R.id.btn_time_sp3);
        btnTimer4 = (Button) findViewById(R.id.btn_time_sp4);
        btnTimer5 = (Button) findViewById(R.id.btn_time_sp5);
        //btn wakeUp/Sleep
        //btnSync = (Button) findViewById(R.id.btnSleep1);//no
        btnWakeUpMode = (Button) findViewById(R.id.btnWakeUpMode1);
        btnSleepMode = (Button) findViewById(R.id.btnScanDev);
        //
        //side rail
        srLeft = (Button) findViewById(R.id.btnSideRailLeft);
        srRigh = (Button) findViewById(R.id.btnSideRailRight);


        //radio buttons
        rbA1 = (RadioButton) findViewById(R.id.rbA1);
        rbA2 = (RadioButton) findViewById(R.id.rbA2);
        rbA3 = (RadioButton) findViewById(R.id.rbA3);
        rbA4 = (RadioButton) findViewById(R.id.rbA4);
        rbA5 = (RadioButton) findViewById(R.id.rbA5);
        //
        rbB1 = (RadioButton) findViewById(R.id.rb1B);
        rbB2 = (RadioButton) findViewById(R.id.rb2B);
        rbB3 = (RadioButton) findViewById(R.id.rb3B);
        rbB4 = (RadioButton) findViewById(R.id.rb4B);
        rbB5 = (RadioButton) findViewById(R.id.rb5B);
        //image button
        ivA1 = (ImageView) findViewById(R.id.ivA1);
        ivA2 = (ImageView) findViewById(R.id.ivA2);
        ivA3 = (ImageView) findViewById(R.id.ivA3);
        ivA4 = (ImageView) findViewById(R.id.ivA4);
        ivA5 = (ImageView) findViewById(R.id.ivA5);
        //
        ivB1 = (ImageView) findViewById(R.id.ivB1);
        ivB2 = (ImageView) findViewById(R.id.ivB2);
        ivB3 = (ImageView) findViewById(R.id.ivB3);
        ivB4 = (ImageView) findViewById(R.id.ivB4);
        ivB5 = (ImageView) findViewById(R.id.ivB5);
        ivBody = findViewById(R.id.ivBody);
        //
        //
        tvFBTime = (TextView) findViewById(R.id.tvFbTimer);
        tvFBStatus = (TextView) findViewById(R.id.tvFBStatus);

        tvLabelFreq = (TextView) findViewById(R.id.tvLabelFreq);
        tvLabelInt = (TextView) findViewById(R.id.tvLabelInt);
        tvLabelTime = (TextView) findViewById(R.id.tvLabelTime);

        tvWriteCmd = (TextView) findViewById(R.id.tvCmd);
        tvSensors = (TextView) findViewById(R.id.tvFeedback);
        //
        //imageview
        ivMCU = (ImageView) findViewById(R.id.ivMcu);
        ivNotification = (ImageView) findViewById(R.id.ivNotifications);
    }

    //------------------Mode---------------//
    //mode of therapy
    private int selectedMode(int mode) {
        if (mode == MODE_PERCUSSION) {
            cleanColorBtnMode();  //clean
            refreshRBByPercussion();//refresh
            invisibleAllIconB();
            sendDataTransdABLE(setPointsBluetooth.INT_BLE_SP_TRA1);//set by default
            setGraphicButtonModeConfirmed(btnPercussion);
            displayMode(mTagReference.SELECTED_ZONE_PERCUSSION);
            animationNotification(true);
            return MODE_PERCUSSION;
        } else if (mode == MODE_PERCUSSION_VIBRATION) {
            cleanColorBtnMode(); //clean
            refreshRBByPercussionVibration(); //refresh
            setGraphicButtonModeConfirmed(btnVibration);
            displayMode(mTagReference.SELECTED_ZONE_VIBRATION);
            animationNotification(false);
            return MODE_PERCUSSION_VIBRATION;
        }
        return 0;
    }

    //------------------therapy---------------
    //*Condition before to start therapy
    private void beforeOnTherapy(int input) {
        //check transducers!!
        if (input == setPointsBluetooth.INT_BLE_CMD_START) {
            invisibleSleepModeBtn(); //Invisible sleep mode buttons
            invisibleWakeUpModeBtn();
            invisibleHomeKey();
            isTherapyOn = true; //set flag
            alertDialogBeforeStart();  //alert dialog first
            displayMode(mTagReference.SELECTED_THERAPY_RUNNING); // display test
        }
    }

    //*refreshRadioButtons by Percussion
    private void refreshRBByPercussion() {
        invisibleAllRbVibration();//radio buttons vibration invisible
        visibleAllRbPercussion(); //radio buttons percussion visible
    }

    //*refreshRadioButtons by Percussion vibration
    private void refreshRBByPercussionVibration() {
        invisibleAllRbVibration();//radio buttons vibration invisible
        visibleAllRbVibration(); //radio buttons percussion visible
    }

    //------------------send data Bluetooth---------------
    //send data Transductor A  to the bluetooth
    //*send data set therapy ON  to the bluetooth
    private void sendDataOnTherapyBLE() {
        setValueCharact(CHARACTERISTIC_COMMANDS, setPointsBluetooth.INT_BLE_CMD_START);
        runProgressDialog();
        startTherapy();
        setDisableFeedback(true);
    }

    //*send data frequency to the bluetooth
    private void sendDataFreqBLE(int input) {
        int textSize = 40;
        int valueSendBLE = 0;
        if ((input >= setPointsBluetooth.INT_BLE_SP_FREQ1) && (input <= setPointsBluetooth.INT_BLE_SP_FREQ5)) {
            // send BLE
            setValueCharact(CHARACTERISTIC_FREQUENCY, input);
            //clean frequency icon before to updated
            cleanColorSPFreq();
            //disable feedback when another command is pressed
            setDisableFeedback(true);
        }
    }

    //*send data intensity to the bluetooth
    private void sendDataIntBLE(int input) {
        int textSize = 40;
        int valueSendBLE = 0;
        if ((input >= setPointsBluetooth.INT_BLE_SP_INT1) && (input <= setPointsBluetooth.INT_BLE_SP_INT5)) {
            //clean icon before to updated
            cleanColorSPIntensity();
            //disable feedback when another command is pressed
            setDisableFeedback(true);
            // send BLE
            setValueCharact(CHARACTERISTIC_INTENSITY, input);
        }
    }

    //*send data Timer to the bluetooth
    private void sendDataTimerBLE(int input) {
        int textSize = 40;
        int valueSendBLE = 0;
        if ((input >= setPointsBluetooth.INT_BLE_SP_TIME1) && (input <= setPointsBluetooth.INT_BLE_SP_TIME5)) {
            //clean icon before to updated
            cleanColorSPTimer();
            //disable feedback when another command is pressed
            setDisableFeedback(true);
            // send BLE
            setValueCharact(CHARACTERISTIC_TIME, input);
        }
    }

    //*send data Transductor A  to the bluetooth
    private void sendDataTransdABLE(int input) {
        int textSize = 40;
        int valueSendBLE = 0;
        if ((input >= setPointsBluetooth.INT_BLE_SP_TRA1) && (input <= setPointsBluetooth.INT_BLE_SP_TRA5)) {
            //clean icon before to updated
            invisibleAllIconA();
            //disable feedback when another command is pressed
            setDisableFeedback(true);
            // send BLE
            setValueCharact(CHARACTERISTIC_TRANSDUCERS, input);
            //storage current transd-A
            currentTransdA = input;
        }
    }

    //*send data Transductor B  to the bluetooth
    private void sendDataTransdBBLE(int input) {
        int textSize = 40;
        int valueSendBLE = 0;
        if ((input >= setPointsBluetooth.INT_BLE_SP_TRB1) && (input <= setPointsBluetooth.INT_BLE_SP_TRB5)) {

            //clean icon before to updated
            invisibleAllIconB();
            //disable feedback when another command is pressed
            setDisableFeedback(true);
            // send BLE
            setValueCharact(CHARACTERISTIC_TRANSDUCERS, input);
            //storage current transd-B
            currentTransdB = input;
        }
    }

    //disable to request feedback
    private void setDisableFeedback(boolean input) {
        if (input) {
            isDisableFeedback = true;
            counterRequestPV = 0;
        } else {
            isDisableFeedback = false;
        }
    }

    @Override
    public void onItemPostSelect(int position, String value) {

    }

    //start therapy
    private class StartTherapyThread1 implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "run: StartTherapyThread1 ");
            Thread thread2 = new Thread(new StartTherapyThread2());
            thread2.start();
        }
    }

    // run therapy
    private class StartTherapyThread2 implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "run: StartTherapyThread2 thread");
            sendCmdStart(sendReceive.getSTART().toString());
        }
    }

    //
    private void sendCmdStart(String input) {
        if (true) {
            String string = input;
            sentData(string); //MCu as Server
            Log.d(TAG, "sendCmdStart: ******!!!");
            displayTherapyStatus(therapyMessage.getTHERAPY_MESSAGE_RUNNING());
            isNewStart = false;
        }
    }

    //?
    private void sentData(String input) {
       /* if (input.equalsIgnoreCase("")) {
            //do nothing
        } else {
            //trasmit
            s = input.toString();
            writeTask writeTask = new writeTask();
            writeTask.execute();
            //update screen
            String inputText = "T-> " + s + "---Date:" + new Date();
            // connectionText(inputText.toString());
            displayTherapyStatus(therapyMessage.getTHERAPY_MESSAGE_RUNNING());
        }*/
    }

    //?
    private void startTherapy() {
        if (isTherapyOn == true) {
            Log.d(TAG, "startTherapy: ");
            disableAllSetPoints();
            min = memMin;
            runTimerTherapy();//start timer therapy
            Thread thread1 = new Thread(new StartTherapyThread1());
            thread1.start();
        }
    }

    //force to stop
    private void forceStop() {
        beforeStopTherapy(setPointsBluetooth.INT_BLE_CMD_STOP);
        //

        TRANSD_SELECTED_OLD = currentTransdA;//save value of cyrrent transd

        timerForceSop = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Thread threadCoolingStop = new Thread(new coolingThreadStop());
                threadCoolingStop.start();
                conditionAfterTherapyFinished();
            }
        }.start();
    }

    //Condition before to stop therapy
    private void conditionAfterTherapyFinished() {
        enableAllSetPoints();
        btnStopDoVisible();//Visible stuff
        invisibleSideRailIcon();
        visibleHomeKey();
    }

    //Condition before to stoptherapy
    private void beforeStopTherapy(int input) {
        if (input == setPointsBluetooth.INT_BLE_CMD_STOP) {
            isTherapyOn = false; //set flag
            setValueCharact(CHARACTERISTIC_COMMANDS, setPointsBluetooth.INT_BLE_CMD_STOP); //send data BLE
            enableWhenTheraDone();//enable stuff
            stopTimerTherapy();
            cleanDisplayTimer();
            setDisableFeedback(true); //disable feedback when another command is pressed
            isCoolingStart = false;
            pulse = 0;
            runProgressDialogStop();
            displayMode(mTagReference.SELECTED_STOPPED);// display test
            lockAllOperationColdDown(); //lock module .
        }
    }

    //stop timer therapy
    private void stopTimerTherapy() {
        try {
            stopCount();
            if (isTherapyOn) {

            }
        } catch (Exception e) {

        }
    }

    //
    //clean display timer
    private void cleanDisplayTimer() {
        displayTimerMinSec(mTagReference.TIME_ZERO);
        sec = 59;
    }

    //---------------Timers--------------------
    //timer therapy
    private void runTimerTherapy() {
        if (timerTherapy != null) {
            timerTherapy.cancel();
            timerTherapy = null;
            disableWhenTheraStart();
        }
        timerTherapy = new CountDownTimer(varTimerTherapy, 1000) {
            @Override
            public void onTick(long l) {
                if (sec == 0) {
                    //
                    min--;
                    if (min < 0) {
                        min = 0;
                    }
                    sec = 59;
                    displayTimerMinSec(String.valueOf(min + ":" + sec));
                } else {
                    sec--;
                    if (sec >= 0 && sec <= 9) {
                        displayTimerMinSec(String.valueOf(min + ":" + "0" + sec));
                    } else {
                        displayTimerMinSec(String.valueOf(min + ":" + sec));
                    }
                }
                int value = (int) l / 60000;
                int value2 = (int) l / 1000;
                Log.d(TAG, "timer therapy: " + value2);
                pulse++;
                Log.d(TAG, "onTick: pulse" + pulse);
                float time = value2 / 60;
                if (!isCoolingStart) {
                    if (pulse > 10) {
                        Thread threadCoolingStart = new Thread(new coolingThreadStart());
                        threadCoolingStart.start();
                        Log.d(TAG, "onTick: pulse!!!" + pulse);
                        isCoolingStart = true;
                    }
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: therapy done!");
                timerTherapyElapsed();
            }
        }.start();
    }

    //disable stuff when therapy start
    private void disableWhenTheraStart() {
        disableSPTimer();
        disableBtnStart();
    }

    // therapy done
    private void timerTherapyElapsed() {
        //stopCount();
        forceStop();
        //sendCmdStop(sendReceive.getSTOP().toString());
        // stopTimerTherapy();
        isNewStop = true;
        Toast.makeText(this, "Therapy Done!!!", Toast.LENGTH_LONG).show();
        cleanDisplayTimer();
    }

    //check to have transducerA and Transducer B selected
    private boolean checkTransducersSelected(int trandA, int trandB) {
        Log.d(TAG, "checkTransducersSelected: A:" + trandA + "B:" + trandB);
        try {
            if (trandA > 0 && trandB > 0) {
                Log.d(TAG, "checkTransducersSelected: A:" + trandA + "B:" + trandB);
                if (trandA + 16 == trandB) {
                    return true;
                } else {
                    Log.d(TAG, "checkTransducersSelected: A:" + trandA + "B:" + trandB);
                    return false;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "checkTransducersSelected: " + e.getMessage());
        }
        return false;
    }

    //wake up mode
    private boolean wakeUp(boolean input) {
        final String WAKE_UP_MODE = "WAKE UP";
        final String SLEEP_MODE = "SLEEP";
        String field = "0";
        boolean ret = false;
        if (input) {
            //wake up mode
            mFlags.FLAG_SLEEP_MODE = false;
            mFlags.FLAG_WAKE_UP_MODE = true;
            //Do
            turnOnScreen();
            visibleAllSleep();
            visibleBody();
            //
            visibleSleepModeBtn();
            invisibleWakeUpModeBtn();
            //
            field = WAKE_UP_MODE;
            ret = true;

        } else {
            //sleep mode
            mFlags.FLAG_SLEEP_MODE = true;
            mFlags.FLAG_WAKE_UP_MODE = false;
            Log.d(TAG, "onClick: Flags Wake up->sleep:" + mFlags.FLAG_SLEEP_MODE + "Wake->" + mFlags.FLAG_WAKE_UP_MODE);
            //Do
            turnOffScreen();
            invisibleAllSleep();
            //switching mode
            invisibleSleepModeBtn();
            visibleWakeUpModeBtn();
            invisibleBody();
            //
            field = SLEEP_MODE;
            ret = false;
        }
        generateSound.generateToneAlert();
        return ret;
    }

    //turn on screen
    public void turnOnScreen() {
        // setSleepNever();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // btnSync.setBackgroundResource(R.drawable.ic_brightness_3_black_24dp);
        Toast.makeText(this, "Wake up!", Toast.LENGTH_SHORT).show();
        displaySleepMode(sleepMode.getSLEEP_MODE_OFF());
    }

    //turn off screen
    public void turnOffScreen() {
        // setScreenTimeOut(5000);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setScreenTimeOut(5000);
        //btnSync.setBackgroundResource(R.drawable.ic_brightness_5_black_24dp);
        Toast.makeText(this, "Sleep in 5 sec!", Toast.LENGTH_SHORT).show();
        displaySleepMode(sleepMode.getSLEEP_MODE_ON());
    }

    //set time out for screen
    public void setScreenTimeOut(int millesensoScreenTimeOut) {
        boolean value = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            value = Settings.System.canWrite(getApplicationContext());
            if (value) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, millesensoScreenTimeOut);
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        }
    }

    //cancel all timers
    private void stopCount() {
        Log.d(TAG, "stopCount: ");
        if (timerTherapy != null) {
            timerTherapy.cancel();
            timerTherapy = null;
        }
    }

    //convert timer to setpoint
    private long timerCalc(int input) {
        long ret = 0;
        long offset = 60000;
        if (input > 0) {
            ret = (long) input * offset;
        }
        return ret;
    }

    //--------------Cold down operations--------------------//
    //get old transducer selected


    private int setMemoryColdDown(int module, int getTimer) {
        ColdDown coldDown = new ColdDown();
        try {
            if (module > 0) {
                switch (module) {
                    case MODULE1:
                        TIMER_MODULE_1 = coldDown.setWorkingTimeTransd(getTimer);
                        break;
                    case MODULE2:
                        TIMER_MODULE_2 = coldDown.setWorkingTimeTransd(getTimer);
                        break;
                    case MODULE3:
                        TIMER_MODULE_3 = coldDown.setWorkingTimeTransd(getTimer);
                        break;
                    case MODULE4:
                        TIMER_MODULE_4 = coldDown.setWorkingTimeTransd(getTimer);
                        break;
                    case MODULE5:
                        TIMER_MODULE_5 = coldDown.setWorkingTimeTransd(getTimer);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + module);
                }
                return 0;
            }
        } catch (Exception e) {
            Log.d(TAG, "setMemoryColdDown: Exception" + e.getMessage());
        }
        return -1;
    }


    //----------------Display-------------------//

    //*update animation icon timer
    private void animationNotification(boolean input) {
        if (ivNotification != null) {
            if (input) {
                ivNotification.setBackgroundResource(R.drawable.animation_vibration2);
                animationNotificationDraw = (AnimationDrawable) ivNotification.getBackground();
                animationNotificationDraw.start();
            } else {
                animationNotificationDraw.stop();
            }
        }
    }

    //image view
    //* update
    private void animationIconTransd(boolean input, ImageView ivTrA, ImageView ivTrB) {
        if (input) {
            if (ivTrA != null && ivTrB != null) {
                //ivTrA.setBackgroundResource(R.drawable.);
                ivTrA.setImageResource(R.drawable.ic_remove_black_24dp);
                ivTrA.setBackgroundResource(R.drawable.animation_vibration2);
                animationNotificationDraw = (AnimationDrawable) ivTrA.getBackground();
                animationNotificationDraw.start();
                //
                ivTrB.setImageResource(R.drawable.ic_remove_black_24dp);
                ivTrB.setBackgroundResource(R.drawable.animation_vibration2);
                animationNotificationDrawB = (AnimationDrawable) ivTrB.getBackground();
                animationNotificationDrawB.start();
            }
        } else {

            //Start anaimation A
            animationNotificationDraw.stop();
            ivTrA.setImageResource(R.drawable.ic_radio_button_checked_green_24dp);
            //stop animationB
            animationNotificationDrawB.stop();
            ivTrB.setImageResource(R.drawable.ic_radio_button_checked_green_24dp);
        }
    }

    //launch home activity
    private void goHome() {
        if (isTherapyOn) {
            alertDialogLivePage();
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

    //display set point of intensity
    private void displaySPIntensity(String input) {
        int textSize = 40;
        if (input.equalsIgnoreCase("")) {
            //empty
        } else {
            if (input.equalsIgnoreCase(mTagReference.SELECTED_STRING_INT_1)) {
                cleanColorSPIntensity();
                btnInt1.setTextColor(getResources().getColor(R.color.black));
                btnInt1.setTextSize(textSize);
                btnInt1.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
                btnInt1.setElegantTextHeight(true);

                //btnInt1.setTextColor(getResources().getColor(R.color.greenDark));
            } else if (input.equalsIgnoreCase(mTagReference.SELECTED_STRING_INT_2)) {
                cleanColorSPIntensity();
                btnInt2.setTextColor(getResources().getColor(R.color.black));
                btnInt2.setTextSize(textSize);
                btnInt2.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
                btnInt2.setElegantTextHeight(true);
                // btnInt2.setTextColor(getResources().getColor(R.color.greenDark));
            } else if (input.equalsIgnoreCase(mTagReference.SELECTED_STRING_INT_3)) {
                cleanColorSPIntensity();
                btnInt3.setTextColor(getResources().getColor(R.color.black));
                btnInt3.setTextSize(textSize);
                btnInt3.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
                btnInt3.setElegantTextHeight(true);
                // btnInt3.setTextColor(getResources().getColor(R.color.greenDark));
            } else if (input.equalsIgnoreCase(mTagReference.SELECTED_STRING_INT_4)) {
                cleanColorSPIntensity();
                btnInt4.setTextColor(getResources().getColor(R.color.black));
                btnInt4.setTextSize(textSize);
                btnInt4.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
                btnInt4.setElegantTextHeight(true);
                // btnInt4.setTextColor(getResources().getColor(R.color.greenDark));
            } else if (input.equalsIgnoreCase(mTagReference.SELECTED_STRING_INT_5)) {
                cleanColorSPIntensity();
                btnInt5.setTextColor(getResources().getColor(R.color.black));
                btnInt5.setTextSize(textSize);
                btnInt5.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
                btnInt5.setElegantTextHeight(true);
                //btnInt5.setTextColor(getResources().getColor(R.color.greenDark));
            }


            if (tvFBInt != null) {
                tvFBInt.setText(input + mTagReference.INT_UNIT);
            }
        }

    }

    //display sleep mode
    private void displaySleepMode(String message) {
        if (message != null && tvSleepMode != null) {
            tvSleepMode.setText(message);
        }
    }

    //display therapy status
    private void displayTherapyStatus(String message) {
        if (message != null && tvFBStatus != null) {
            Log.d(TAG, "displayTherapyStatus: " + message);
            //tvFBStatus.setText(message);
        }
    }

    //display textView Timer
    //get timer in min sec
    private void displayTimerMinSec(String time) {
        if (time != null) {
            tvFBTime.setText(time);
        }
    }

    //update ui from ble
    private boolean updateUIfromBle(int value) {
        if (value > 0) {
            try {


                //!String strValue = String.valueOf(value);
                Log.d(TAG, "checkAllSpUpdates: value" + value);
                if (value >= setPointsBluetooth.getBLE_SP_FREQ1() && value <= setPointsBluetooth.getBLE_SP_FREQ5()) {
                    //send it frequency
                    mFlags.FLAG_SP_FREQ = true;
                    isFullSetUp(mFlags.FLAG_SP_FREQ, mFlags.FLAG_SP_INT, mFlags.FLAG_SP_TIME, mFlags.FLAG_SP_TRAND);
                    Log.d(TAG, "checkAllSpUpdates: frequency");
                    return true;
                } else if (value >= setPointsBluetooth.getBLE_SP_INT1() && value <= setPointsBluetooth.getBLE_SP_INT5()) {
                    // isUpdatedSPInt(message);
                    mFlags.FLAG_SP_INT = true;
                    isFullSetUp(mFlags.FLAG_SP_FREQ, mFlags.FLAG_SP_INT, mFlags.FLAG_SP_TIME, mFlags.FLAG_SP_TRAND);
                    Log.d(TAG, "checkAllSpUpdates: Intensity");
                    return true;
                } else if (value >= setPointsBluetooth.getBLE_SP_TIME1() && value <= setPointsBluetooth.getBLE_SP_TIME5()) {
                    mFlags.FLAG_SP_TIME = true;
                    isFullSetUp(mFlags.FLAG_SP_FREQ, mFlags.FLAG_SP_INT, mFlags.FLAG_SP_TIME, mFlags.FLAG_SP_TRAND);
                    Log.d(TAG, "checkAllSpUpdates: time");
                    return true;
                } else if (value >= setPointsBluetooth.getBLE_SP_TRA1() && value <= setPointsBluetooth.getBLE_SP_TRB5()) {
                    mFlags.FLAG_SP_TRAND = true;
                    isFullSetUp(mFlags.FLAG_SP_FREQ, mFlags.FLAG_SP_INT, mFlags.FLAG_SP_TIME, mFlags.FLAG_SP_TRAND);
                    Log.d(TAG, "checkAllSpUpdates: transducers");
                    return true;
                }

            } catch (Exception e) {
                Log.d(TAG, "updateUIfromBle: exception" + e.getMessage());
            }
        }
        return false;
    }

    //check if setup is full
    private boolean isFullSetUp(boolean isFlagFreq, boolean isFlagInt, boolean isFlagTime, boolean isFlagTransd) {
        boolean ret = false;
        Log.d(TAG, "isFullSetUp:isFlagFreq: " + isFlagFreq + "./ isFlagInt:" + isFlagInt + "/ .isFlagTime" + isFlagTime + ". /isFlagTransd:" + isFlagTransd);
        if (isFlagFreq && isFlagInt && isFlagTime && isFlagTransd) {
            //visi
            visibleBtnCommands();
            Log.d(TAG, "isFullSetUp: visible");
            ret = true;
        } else {
            ret = false;
            invisibleBtnCommands();
            Log.d(TAG, "isFullSetUp: invisible");
        }
        return ret;
    }

    //*get the received value after send data using bluetooth. Main
    private String updateFeedbackValue(int intValue) {
        String strValue = "0";
        try {
            if (intValue > 0) {
                strValue = String.valueOf(value);
                if (intValue >= setPointsBluetooth.INT_BLE_SP_FREQ1 && intValue <= setPointsBluetooth.INT_BLE_SP_FREQ5) {
                    updateDisplayIconFreq(strValue);
                } else if (intValue >= setPointsBluetooth.INT_BLE_SP_INT1 && intValue <= setPointsBluetooth.INT_BLE_SP_INT5) {
                    //intensity
                    updateDisplayIconInt(strValue);
                } else if (intValue >= setPointsBluetooth.INT_BLE_SP_TIME1 && intValue <= setPointsBluetooth.INT_BLE_SP_TIME5) {
                    //timer
                    updateDisplayIconTimer(strValue);
                } else if (intValue >= setPointsBluetooth.INT_BLE_SP_TRA1 && intValue <= setPointsBluetooth.INT_BLE_SP_TRA5) {
                    //transducer A
                    updateDisplayIconTransdA(strValue);
                } else if (intValue >= setPointsBluetooth.INT_BLE_SP_TRB1 && intValue <= setPointsBluetooth.INT_BLE_SP_TRB5) {
                    //transducer B
                    updateDisplayIconTransdB(strValue);
                } else if (intValue >= setPointsBluetooth.INT_BLE_CMD_START && intValue <= setPointsBluetooth.INT_BLE_SP_COOL_ALL_INIT) {
                    //commands
                    updateDisplayIconCommands(strValue);
                }
                //beep
                generateSound.generateToneRing();
                return strValue;
            } else {
                return "0";
            }
        } catch (Exception e) {
            Log.d(TAG, "updateFeedbackValue:  Exception" + e.getMessage());
        }
        return "0";
    }

    //*update icon frequency using feedback from BLE
    private String updateDisplayIconFreq(String value) {
        String ret = "";
        String field = "";
        String fieldValue = "";
        //
        String FREQUENCY = "Frequency";
        Log.d(TAG, "updateDisplayIconFreq: " + value);

        if (true) {
            cleanColorSPFreq();
            if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_FREQ1)) {

                fieldValue = mTagReference.SELECTED_STRING_FREQ_1;
                Log.d(TAG, "updateDisplayIconFreq: btnFreq1");
                setGraphicButtonConfirmed(btnFreq1);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_FREQ2)) {

                fieldValue = mTagReference.SELECTED_STRING_FREQ_2;
                setGraphicButtonConfirmed(btnFreq2);
                Log.d(TAG, "updateDisplayIconFreq: btnFreq2");
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_FREQ3)) {

                fieldValue = mTagReference.SELECTED_STRING_FREQ_3;
                Log.d(TAG, "updateDisplayIconFreq: btnFreq3");
                setGraphicButtonConfirmed(btnFreq3);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_FREQ4)) {

                fieldValue = mTagReference.SELECTED_STRING_FREQ_4;
                setGraphicButtonConfirmed(btnFreq4);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_FREQ5)) {

                fieldValue = mTagReference.SELECTED_STRING_FREQ_5;
                setGraphicButtonConfirmed(btnFreq5);
            }
        }
        field = FREQUENCY;
        ret = field + "/" + fieldValue;
        return ret;
    }

    //*update icon Intensity using feedback from BLE
    private String updateDisplayIconInt(String value) {
        String ret = "";
        String field = "";
        String fieldValue = "";
        //
        String INTENSITY = "Intensity";
        if (true) {
            cleanColorSPIntensity();
            // Log.d(TAG, "updateDisplayIconFreq: setPointsBluetooth.SP_BLE_SP_FREQ1"+setPointsBluetooth.SP_BLE_SP_FREQ1);
            if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_INT1)) {
                fieldValue = mTagReference.SELECTED_STRING_INT_1;
                setGraphicButtonConfirmed(btnInt1);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_INT2)) {
                fieldValue = mTagReference.SELECTED_STRING_INT_2;
                setGraphicButtonConfirmed(btnInt2);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_INT3)) {
                fieldValue = mTagReference.SELECTED_STRING_INT_3;
                setGraphicButtonConfirmed(btnInt3);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_INT4)) {
                fieldValue = mTagReference.SELECTED_STRING_INT_4;
                setGraphicButtonConfirmed(btnInt4);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_INT5)) {
                fieldValue = mTagReference.SELECTED_STRING_INT_5;
                setGraphicButtonConfirmed(btnInt5);
            }
        }
        field = INTENSITY;
        ret = field + "/" + fieldValue;
        return ret;
    }

    //*update icon Timer using feedback from BLE
    private String updateDisplayIconTimer(String value) {
        String ret = "";
        String field = "";
        String fieldValue = "";
        int timerToCalc = 0;
        String TIMER = "Timer";
        //
        if (true) {
            cleanColorSPTimer();
            if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TIME1)) {
                fieldValue = mTagReference.SELECTED_STRING_TIME_1;
                setGraphicButtonConfirmed(btnTimer1);
                timerToCalc = setpoint.TIME1;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TIME2)) {
                fieldValue = mTagReference.SELECTED_STRING_TIME_2;
                setGraphicButtonConfirmed(btnTimer2);
                timerToCalc = setpoint.TIME2;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TIME3)) {
                fieldValue = mTagReference.SELECTED_STRING_TIME_3;
                setGraphicButtonConfirmed(btnTimer3);
                timerToCalc = setpoint.TIME3;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TIME4)) {
                fieldValue = mTagReference.SELECTED_STRING_TIME_4;
                setGraphicButtonConfirmed(btnTimer4);
                timerToCalc = setpoint.TIME4;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TIME5)) {
                fieldValue = mTagReference.SELECTED_STRING_TIME_5;
                setGraphicButtonConfirmed(btnTimer5);
                timerToCalc = setpoint.TIME5;
            }
        }
        field = TIMER;
        ret = field + "/" + fieldValue;
        //calculate timer
        varTimerTherapy = timerCalc(timerToCalc);
        memMin = timerToCalc - 1;
        return ret;
    }

    //*update icon Transductors A using feedback from BLE
    private String updateDisplayIconTransdA(String value) {
        String ret = "";
        String field = "";
        String fieldValue = "";
        //
        String TRANSDUCER_A = "Transducer_A";
        String TRANSDUCER_B = "Transducer_B";
        Log.d(TAG, "updateDisplayIconFreq: " + value);
        //
        ImageView imageView = null;
        RadioButton radioButton = null;

        if (true) {
            invisibleAllIconA();
            if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRA1)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_A1;
                radioButton = rbA1;
                imageView = ivA1;
                // visibleIconTransdA(ivA1);
                //setRadioButtonVisible(rbA1);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRA2)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_A2;
                radioButton = rbA2;
                imageView = ivA2;

                //setRadioButtonVisible(rbA2);
                //visibleIconTransdA(ivA2);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRA3)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_A3;
                radioButton = rbA3;
                imageView = ivA3;
                //setRadioButtonVisible(rbA3);
                //visibleIconTransdA(ivA3);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRA4)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_A4;
                radioButton = rbA4;
                imageView = ivA4;
                //setRadioButtonVisible(rbA4);
                //visibleIconTransdA(ivA4);
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRA5)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_A5;
                radioButton = rbA5;
                imageView = ivA5;
                //setRadioButtonVisible(rbA5);
                //visibleIconTransdA(ivA5);
            }
        }
        //update icon
        visibleIconTransdA(imageView);
        //update radio button
        setRadioButtonVisible(radioButton);

        ivTrA = imageView;
        field = TRANSDUCER_A;
        ret = field + "/" + fieldValue;
        return ret;
    }

    //*update icon Transductors B using feedback from BLE
    private String updateDisplayIconTransdB(String value) {
        String ret = "";
        String field = "";
        String fieldValue = "";
        //
        String TRANSDUCER_A = "Transducer_A";
        String TRANSDUCER_B = "Transducer_B";
        Log.d(TAG, "updateDisplayIconFreq: " + value);
        //
        ImageView imageView = null;
        RadioButton radioButton = null;
        if (true) {
            invisibleAllIconB();
            if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRB1)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_B1;
                imageView = ivB1;
                radioButton = rbB1;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRB2)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_B2;
                imageView = ivB2;
                radioButton = rbB2;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRB3)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_B3;
                imageView = ivB3;
                radioButton = rbB3;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRB4)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_B4;
                imageView = ivB4;
                radioButton = rbB4;
            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_SP_TRB5)) {
                fieldValue = mTagReference.SELECTED_STRING_RB_B5;
                imageView = ivB5;
                radioButton = rbB5;
            }
        }
        //
        setRadioButtonVisible(radioButton);
        visibleIconTransdB(imageView);
        //
        ivTrB = imageView;

        //
        field = TRANSDUCER_B;
        ret = field + "/" + fieldValue;
        return ret;
    }

    //*update icon button commands using feedback from BLE
    private String updateDisplayIconCommands(String value) {
        String ret = "";
        String field = "";
        String fieldValue = "";
        String COMMANDS = "Commands";
        //
        if (true) {
            if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_CMD_START)) {
                cleanColorBtnCmd();
                fieldValue = mTagReference.ST_SELECTED_THERAPY_RUNNING;
                setGraphicButtonCmdConfirmed(btnStart);
                disableBtnStart();
                //anaimation transducer start
                animationIconTransd(true, ivTrA, ivTrB);

            } else if (value.equalsIgnoreCase(setPointsBluetooth.SP_BLE_CMD_STOP)) {
                cleanColorBtnCmd();
                setGraphicButtonCmdConfirmed(btnStop);
                fieldValue = mTagReference.ST_SELECTED_STOPPED;
                enableBtnStart();
                //animation transducer  stop
                animationIconTransd(false, ivTrA, ivTrB);
            }
        }
        field = COMMANDS;
        ret = field + "/" + fieldValue;
        return ret;
    }


    //------------------Clean flags---------------//

    //change color on the set point frequency
    private void cleanColorSPFreq() {
        int textSizeDefault = 25;
        //btnFreq1.setTextColor(getResources().getColor(R.color.colorAccent));
        //btnFreq1.setTextColor(getResources().getColor(R.color.colorGray));
        // btnFreq2.setTextColor(getResources().getColor(R.color.colorGray));
        // btnFreq3.setTextColor(getResources().getColor(R.color.colorGray));
        // btnFreq4.setTextColor(getResources().getColor(R.color.colorGray));
        // btnFreq5.setTextColor(getResources().getColor(R.color.colorGray));
        //
        btnFreq1.setTextSize(textSizeDefault);
        btnFreq2.setTextSize(textSizeDefault);
        btnFreq3.setTextSize(textSizeDefault);
        btnFreq4.setTextSize(textSizeDefault);
        btnFreq5.setTextSize(textSizeDefault);
        //
        btnFreq1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnFreq2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnFreq3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnFreq4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnFreq5.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        //
        //btnFreq1.setElegantTextHeight(false);
        // btnFreq1.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
       /* btnFreq2.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnFreq3.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnFreq4.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnFreq5.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));*/
    }

    //change color on the set point intensity
    private void cleanColorSPIntensity() {
        int textSizeDefault = 25;
       /* btnInt1.setTextColor(getResources().getColor(R.color.colorGray));
        btnInt2.setTextColor(getResources().getColor(R.color.colorGray));
        btnInt3.setTextColor(getResources().getColor(R.color.colorGray));
        btnInt4.setTextColor(getResources().getColor(R.color.colorGray));
        btnInt5.setTextColor(getResources().getColor(R.color.colorGray));*/
        //
        btnInt1.setTextSize(textSizeDefault);
        btnInt2.setTextSize(textSizeDefault);
        btnInt3.setTextSize(textSizeDefault);
        btnInt4.setTextSize(textSizeDefault);
        btnInt5.setTextSize(textSizeDefault);
        //
        btnInt1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnInt2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnInt3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnInt4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnInt5.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        /*btnInt1.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnInt2.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnInt3.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnInt4.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnInt5.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));*/
    }

    //change color on the set point timer
    private void cleanColorSPTimer() {
        int textSizeDefault = 25;
       /* btnTimer1.setTextColor(getResources().getColor(R.color.colorGray));
        btnTimer2.setTextColor(getResources().getColor(R.color.colorGray));
        btnTimer3.setTextColor(getResources().getColor(R.color.colorGray));
        btnTimer4.setTextColor(getResources().getColor(R.color.colorGray));
        btnTimer5.setTextColor(getResources().getColor(R.color.colorGray));*/
        //
        btnTimer1.setTextSize(textSizeDefault);
        btnTimer2.setTextSize(textSizeDefault);
        btnTimer3.setTextSize(textSizeDefault);
        btnTimer4.setTextSize(textSizeDefault);
        btnTimer5.setTextSize(textSizeDefault);
        //
        btnTimer1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnTimer2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnTimer3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnTimer4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnTimer5.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        /*btnTimer1.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnTimer2.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnTimer3.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnTimer4.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));
        btnTimer5.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_light));*/
    }

    //change color btn modes
    private void cleanColorBtnMode() {
        btnPercussion.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnVibration.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
    }

    //change color btn commands
    private void cleanColorBtnCmd() {
        btnStart.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        btnStop.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
    }

    //------------------Confirmation---------------//


    //set icon green
    private boolean setGraphicButtonRequest(Button button) {
        int textSize = 40;
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(textSize);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_blue_24dp), null, null, null);
        button.setElegantTextHeight(true);
        return true;
    }

    //set icon blue
    private boolean setGraphicButtonConfirmed(Button button) {
        int textSize = 40;
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(textSize);
        //button.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_blue_24dp), null, null, null);
        button.setElegantTextHeight(true);
        return true;
    }

    //*set icon blue
    private boolean setGraphicButtonCmdConfirmed(Button button) {
        int textSize = 26;
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(textSize);
        //button.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_blue_24dp), null, null, null);
        button.setElegantTextHeight(true);
        return true;
    }

    //set icon blue
    private boolean setGraphicButtonModeConfirmed(Button button) {
        int textSize = 24;
        button.setTextColor(getResources().getColor(R.color.black));
        button.setTextSize(textSize);
        //button.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_green_24dp), null, null, null);
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.ic_radio_button_checked_blue_24dp), null, null, null);
        button.setElegantTextHeight(true);
        return true;
    }

    //-----------------display text----------------------
    //* display mode
    private void displayMode(int input) {
        try {
            if (input == mTagReference.SELECTED_ZONE_PERCUSSION) {
                tvFBStatus.setText("Mode:Percussion");
                //ibMode.setImageResource(R.drawable.ic_verified_user_black_24dp);
            } else if (input == mTagReference.SELECTED_ZONE_VIBRATION) {
                tvFBStatus.setText("Mode:Vibration");
                //ibMode.setImageResource(R.drawable.ic_verified_user_black_24dp);
            } else if (input == mTagReference.SELECTED_ZONE_UNKNOWN) {
                tvFBStatus.setText("Mode:Unknown");
                //ibMode.setImageResource(R.drawable.ic_info_outline_black_24dp);
            } else if (input == mTagReference.SELECTED_PROGRAM1) {
                tvFBStatus.setText("Mode:Program1");
            } else if (input == mTagReference.SELECTED_PROGRAM2) {
                tvFBMode.setText("Mode:Program2");
                tvFBStatus.setText("Mode:Program2");
            } else if (input == mTagReference.SELECTED_PROGRAM3) {
                tvFBStatus.setText("Mode:Program3");
            } else if (input == mTagReference.SELECTED_THERAPY_RUNNING) {
                tvFBStatus.setText("Mode:Therapy is running");
                //ibMode.setImageResource(R.drawable.ic_play_circle_outline_blue_24dp);
            } else if (input == mTagReference.SELECTED_STOPPED) {
                tvFBStatus.setText("Mode:Therapy stopped");
                //ibMode.setImageResource(R.drawable.ic_pause_circle_outline_blue_24dp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------Enable/Disable---------------
    // Enable btn start
    private void enableBtnStart() {
        btnStart.setEnabled(true);
    }

    //Disable btn Start
    private void disableBtnStart() {
        btnStart.setEnabled(false);
    }

    //enable setpoint timer
    private void enableSPTimer() {
        btnTimer1.setEnabled(true);
        btnTimer2.setEnabled(true);
        btnTimer3.setEnabled(true);
        btnTimer4.setEnabled(true);
        btnTimer5.setEnabled(true);
    }

    //disable setpoint timer
    private void disableSPTimer() {
        btnTimer1.setEnabled(false);
        btnTimer2.setEnabled(false);
        btnTimer3.setEnabled(false);
        btnTimer4.setEnabled(false);
        btnTimer5.setEnabled(false);
    }

    //enable sp after finish therapy
    private void enableWhenTheraDone() {
        enableSPTimer();
        enableBtnStart();
    }

    //disable
    private void disableAllSetPoints() {
        btnFreq1.setEnabled(false);
        btnFreq2.setEnabled(false);
        btnFreq3.setEnabled(false);
        btnFreq4.setEnabled(false);
        btnFreq5.setEnabled(false);
        //
        btnTimer1.setEnabled(false);
        btnTimer2.setEnabled(false);
        btnTimer3.setEnabled(false);
        btnTimer4.setEnabled(false);
        btnTimer5.setEnabled(false);
        //Intensity
        btnInt1.setEnabled(false);
        btnInt2.setEnabled(false);
        btnInt3.setEnabled(false);
        btnInt4.setEnabled(false);
        btnInt5.setEnabled(false);
        //
        btnVibration.setEnabled(false);
        btnPercussion.setEnabled(false);
        //transducers A
        rbA1.setEnabled(false);
        rbA2.setEnabled(false);
        rbA3.setEnabled(false);
        rbA4.setEnabled(false);
        rbA5.setEnabled(false);
        //transducerB
        rbB1.setEnabled(false);
        rbB2.setEnabled(false);
        rbB3.setEnabled(false);
        rbB4.setEnabled(false);
        rbB5.setEnabled(false);
    }

    //enable all setpoints
    private void enableAllSetPoints() {
        btnFreq1.setEnabled(true);
        btnFreq2.setEnabled(true);
        btnFreq3.setEnabled(true);
        btnFreq4.setEnabled(true);
        btnFreq5.setEnabled(true);
        //
        btnTimer1.setEnabled(true);
        btnTimer2.setEnabled(true);
        btnTimer3.setEnabled(true);
        btnTimer4.setEnabled(true);
        btnTimer5.setEnabled(true);
        //
        btnInt1.setEnabled(true);
        btnInt2.setEnabled(true);
        btnInt3.setEnabled(true);
        btnInt4.setEnabled(true);
        btnInt5.setEnabled(true);
        //
        btnVibration.setEnabled(true);
        btnPercussion.setEnabled(true);
        //  transducers A
        rbA1.setEnabled(true);
        rbA2.setEnabled(true);
        rbA3.setEnabled(true);
        rbA4.setEnabled(true);
        rbA5.setEnabled(true);
        //transducersB
        rbB1.setEnabled(true);
        rbB2.setEnabled(true);
        rbB3.setEnabled(true);
        rbB4.setEnabled(true);
        rbB5.setEnabled(true);
    }

    //lock operation
    private void lockAllOperationColdDown() {
        //
        //get var
        savedTransdA = currentTransdA;
        savedTransdB = currentTransdB;
        //
        // lockByColdDownTimerA(savedTransdA);
        //lockByColdDownTimerB(savedTransdB);
        //
        runLockColdDown();
    }

    //lock operation by coldon timer
    private void lockByColdDownTimerA(int transdA) {
        if (transdA == setPointsBluetooth.INT_BLE_SP_TRA1) {
            visibleInviCustomRB_IV(false, rbA1, ivA1);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA2) {
            visibleInviCustomRB_IV(false, rbA2, ivA2);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA3) {
            visibleInviCustomRB_IV(false, rbA3, ivA3);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA4) {
            visibleInviCustomRB_IV(false, rbA4, ivA4);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA5) {
            visibleInviCustomRB_IV(false, rbA5, ivA5);
        }
    }

    //un lock operation by colddon timer
    private void unlockByColdDownTimerA(int transdA) {
        if (transdA == setPointsBluetooth.INT_BLE_SP_TRA1) {
            visibleInviCustomRB_IV(true, rbA1, ivA1);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA2) {
            visibleInviCustomRB_IV(true, rbA2, ivA2);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA3) {
            visibleInviCustomRB_IV(true, rbA3, ivA3);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA4) {
            visibleInviCustomRB_IV(true, rbA4, ivA4);
        } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA5) {
            visibleInviCustomRB_IV(true, rbA5, ivA5);
        }
    }

    //lock operation by coldon timer
    private void lockByColdDownTimerB(int transdB) {
        if (transdB == setPointsBluetooth.INT_BLE_SP_TRB1) {
            visibleInviCustomRB_IV(false, rbB1, ivB1);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB2) {
            visibleInviCustomRB_IV(false, rbB2, ivB2);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB3) {
            visibleInviCustomRB_IV(false, rbB3, ivB3);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB4) {
            visibleInviCustomRB_IV(false, rbB4, ivB4);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB5) {
            visibleInviCustomRB_IV(false, rbB5, ivB5);
        }
    }

    //un lock operation by colddon timer
    private void unlockByColdDownTimerB(int transdB) {
        if (transdB == setPointsBluetooth.INT_BLE_SP_TRB1) {
            visibleInviCustomRB_IV(true, rbB1, ivB1);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB2) {
            visibleInviCustomRB_IV(true, rbB2, ivB2);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB3) {
            visibleInviCustomRB_IV(true, rbB3, ivB3);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB4) {
            visibleInviCustomRB_IV(true, rbB4, ivB4);
        } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB5) {
            visibleInviCustomRB_IV(true, rbB5, ivB5);
        }
    }

    //lock some operation on the gui after stop
    private void runLockColdDown() {
        if (timerLockColdDown != null) {
            timerLockColdDown = null;
        }
        //
        timerLockColdDown = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "runLockColdDown: " + millisUntilFinished / 1000);
                lockByColdDownTimerA(savedTransdA);
                lockByColdDownTimerB(savedTransdB);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "runLockColdDown onFinish: ");
                cancelLockDown();
            }
        }.start();
    }

    //cancell lock clodDown
    private void cancelLockDown() {
        Log.d(TAG, "cancelLockDown: ");
        unlockByColdDownTimerA(savedTransdA);
        unlockByColdDownTimerB(savedTransdB);
        //
        //savedTransdA=0;
        //savedTransdB=0;
    }


    //------------------Visible/Invisible---------------//

    //invisible home key
    private void invisibleHomeKey() {
        btnGoHome.setVisibility(View.INVISIBLE);
    }

    //visible home key
    private void visibleHomeKey() {
        btnGoHome.setVisibility(View.VISIBLE);
    }

    //init visible GUI
    private boolean initVisGUI() {
        //icon of selected transducer
        invisibleAllIconA();
        invisibleAllIconB();
        //radio buttons
        invisibleAllRb();//invisible all radio button. Need to be selected
        //hide action bar
        //hideToolbar(false);
        //invisible
        invisibleAllSleep();
        //button of slect prog
        //invisibleAllProg();
        //btn sleep mode
        invisibleSleepModeBtn();
        //
        invisibleSideRailIcon();
        //
        invisibleBody();
        return true;
    }

    //invisible Body
    private void invisibleBody() {
        ivBody.setVisibility(View.INVISIBLE);
    }

    //visible Body
    private void visibleBody() {
        ivBody.setVisibility(View.VISIBLE);
    }

    //*set radio button visible
    private boolean setRadioButtonVisible(RadioButton radioButton) {
        radioButton.setVisibility(View.VISIBLE);
        return true;
    }

    //* visible icon of transducer A
    private void visibleIconTransdA(ImageView imageView) {
        //clean display
        invisibleAllIconA();
        //visible
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    //* visible icon of transducer B
    private void visibleIconTransdB(ImageView imageView) {
        //clean display
        invisibleAllIconB();
        //visible
        if (imageView != null) {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    //*invisible all icon A
    private void invisibleAllIconA() {
        ivA1.setVisibility(View.INVISIBLE);
        ivA2.setVisibility(View.INVISIBLE);
        ivA3.setVisibility(View.INVISIBLE);
        ivA4.setVisibility(View.INVISIBLE);
        ivA5.setVisibility(View.INVISIBLE);
    }

    //*invisible all icon B
    private void invisibleAllIconB() {
        ivB1.setVisibility(View.INVISIBLE);
        ivB2.setVisibility(View.INVISIBLE);
        ivB3.setVisibility(View.INVISIBLE);
        ivB4.setVisibility(View.INVISIBLE);
        ivB5.setVisibility(View.INVISIBLE);
    }

    //invisible icons for Vibration
    private void invisibleIconVibration() {
        //A
        ivA3.setVisibility(View.INVISIBLE);
        ivA4.setVisibility(View.INVISIBLE);
        ivA5.setVisibility(View.INVISIBLE);
        //B
        ivB3.setVisibility(View.INVISIBLE);
        ivB4.setVisibility(View.INVISIBLE);
        ivB5.setVisibility(View.INVISIBLE);

    }

    //invisible All Radio buttons percussion
    private void invisibleAllRb() {
        //A
        rbA1.setVisibility(View.INVISIBLE);
        rbA2.setVisibility(View.INVISIBLE);
        rbA3.setVisibility(View.INVISIBLE);
        rbA4.setVisibility(View.INVISIBLE);
        rbA5.setVisibility(View.INVISIBLE);
        //B
        rbB1.setVisibility(View.INVISIBLE);
        rbB2.setVisibility(View.INVISIBLE);
        rbB3.setVisibility(View.INVISIBLE);
        rbB4.setVisibility(View.INVISIBLE);
        rbB5.setVisibility(View.INVISIBLE);
    }

    //invisible All radio button Vibration
    private void invisibleAllRbVibration() {
        //A
        rbA3.setVisibility(View.INVISIBLE);
        rbA4.setVisibility(View.INVISIBLE);
        rbA5.setVisibility(View.INVISIBLE);
        //B
        rbB3.setVisibility(View.INVISIBLE);
        rbB4.setVisibility(View.INVISIBLE);
        rbB5.setVisibility(View.INVISIBLE);
    }

    //visible All radio button Vibration
    private void visibleAllRbVibration() {
        //A
        rbA1.setVisibility(View.VISIBLE);
        rbA2.setVisibility(View.VISIBLE);
        rbA3.setVisibility(View.VISIBLE);
        rbA4.setVisibility(View.VISIBLE);
        rbA5.setVisibility(View.VISIBLE);
        //B
        rbB1.setVisibility(View.VISIBLE);
        rbB2.setVisibility(View.VISIBLE);
        rbB3.setVisibility(View.VISIBLE);
        rbB4.setVisibility(View.VISIBLE);
        rbB5.setVisibility(View.VISIBLE);

    }

    //visible All radio button Percussion
    private void visibleAllRbPercussion() {
        //A
        rbA1.setVisibility(View.VISIBLE);
        rbA2.setVisibility(View.VISIBLE);
        //B
        rbB1.setVisibility(View.VISIBLE);
        rbB2.setVisibility(View.VISIBLE);

    }

    //invisible
    private void invisibleTextView() {
        //tvFBFreq.setVisibility(View.INVISIBLE);
        //tvFBInt.setVisibility(View.INVISIBLE);
        //tvFBMode.setVisibility(View.INVISIBLE);
        // tvFBZone.setVisibility(View.INVISIBLE);
        //
        tvLabelFreq.setVisibility(View.INVISIBLE);
        tvLabelInt.setVisibility(View.INVISIBLE);
        tvLabelTime.setVisibility(View.INVISIBLE);
        //tvLabelProg.setVisibility(View.INVISIBLE);
    }

    //invisible
    private void visibleTextView() {
        tvLabelFreq.setVisibility(View.VISIBLE);
        tvLabelInt.setVisibility(View.VISIBLE);
        tvLabelTime.setVisibility(View.VISIBLE);
    }

    //invisible all  freq buttons
    private void invisibleAllFreqBtn() {
        btnFreq1.setVisibility(View.INVISIBLE);
        btnFreq2.setVisibility(View.INVISIBLE);
        btnFreq3.setVisibility(View.INVISIBLE);
        btnFreq4.setVisibility(View.INVISIBLE);
        btnFreq5.setVisibility(View.INVISIBLE);
    }

    //visible all freq buttons
    private void visibleAllFreqBtn() {
        btnFreq1.setVisibility(View.VISIBLE);
        btnFreq2.setVisibility(View.VISIBLE);
        btnFreq3.setVisibility(View.VISIBLE);
        btnFreq4.setVisibility(View.VISIBLE);
        btnFreq5.setVisibility(View.VISIBLE);
    }

    //invisible all  Int buttons
    private void invisibleAllIntBtn() {
        btnInt1.setVisibility(View.INVISIBLE);
        btnInt2.setVisibility(View.INVISIBLE);
        btnInt3.setVisibility(View.INVISIBLE);
        btnInt4.setVisibility(View.INVISIBLE);
        btnInt5.setVisibility(View.INVISIBLE);
        btnMaxInflate.setVisibility(View.INVISIBLE);
    }

    //visible all Int buttons
    private void visibleAllIntBtn() {
        btnInt1.setVisibility(View.VISIBLE);
        btnInt2.setVisibility(View.VISIBLE);
        btnInt3.setVisibility(View.VISIBLE);
        btnInt4.setVisibility(View.VISIBLE);
        btnInt5.setVisibility(View.VISIBLE);
        btnMaxInflate.setVisibility(View.VISIBLE);
    }

    //invisible all  Time buttons
    private void invisibleAllTimeBtn() {
        btnTimer1.setVisibility(View.INVISIBLE);
        btnTimer2.setVisibility(View.INVISIBLE);
        btnTimer3.setVisibility(View.INVISIBLE);
        btnTimer4.setVisibility(View.INVISIBLE);
        btnTimer5.setVisibility(View.INVISIBLE);
    }

    //visible all Time buttons
    private void visibleAllTimeBtn() {
        btnTimer1.setVisibility(View.VISIBLE);
        btnTimer2.setVisibility(View.VISIBLE);
        btnTimer3.setVisibility(View.VISIBLE);
        btnTimer4.setVisibility(View.VISIBLE);
        btnTimer5.setVisibility(View.VISIBLE);
    }

    //invisible all btn mode
    private void invisibleAllBtnMode() {
        btnPercussion.setVisibility(View.INVISIBLE);
        btnVibration.setVisibility(View.INVISIBLE);
    }

    //visible all btn mode
    private void visibleAllBtnMode() {
        btnPercussion.setVisibility(View.VISIBLE);
        btnVibration.setVisibility(View.VISIBLE);
    }

    //visible btn Percussion
    private void visibleBtnPercussion() {
        btnPercussion.setVisibility(View.VISIBLE);
    }

    //visible btn Vibration
    private void visibleBtnVibration() {
        btnVibration.setVisibility(View.VISIBLE);
    }

    //invisible sleepMode btn
    private void invisibleSleepModeBtn() {
        btnSleepMode.setVisibility(View.INVISIBLE);
    }

    //visible sleepMode btn
    private void visibleSleepModeBtn() {
        btnSleepMode.setVisibility(View.VISIBLE);
    }

    //invisible WakeUpMode btn
    private void invisibleWakeUpModeBtn() {
        btnWakeUpMode.setVisibility(View.INVISIBLE);
    }

    //visible WakeUpMode btn
    private void visibleWakeUpModeBtn() {
        btnWakeUpMode.setVisibility(View.VISIBLE);
    }

    //invisible btnSync
    private void invisibleBtnSync() {
        // btnSync.setVisibility(View.INVISIBLE);
    }

    //visible btnSync
    private void visibleBtnSync() {
        btnSync.setVisibility(View.VISIBLE);
    }

    //invisible btn commands
    private void invisibleBtnCommands() {
        btnStart.setVisibility(View.INVISIBLE);
        btnStop.setVisibility(View.INVISIBLE);
    }

    //visible btn commands
    private void visibleBtnCommands() {
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
    }

    //invisible btn start
    private void invisibleBtnStart() {
        btnStart.setVisibility(View.INVISIBLE);
    }

    //visible btn start
    private void visibleBtnStart() {
        btnStart.setVisibility(View.VISIBLE);
    }

    //Invisible all icons except sleep mode
    private void invisibleAllSleep() {
        invisibleAllIconA();
        invisibleAllIconB();
        invisibleAllRb();
        //
        invisibleAllFreqBtn();
        invisibleAllIntBtn();
        invisibleAllTimeBtn();
        //
        invisibleTextView();
        invisibleAllBtnMode();
        //
        invisibleBtnSync();
        //
        invisibleBtnCommands();
    }

    //Visible all icons except sleep mode
    private void visibleAllSleep() {
        //
        visibleAllFreqBtn();
        visibleAllIntBtn();
        visibleAllTimeBtn();
        //
        visibleTextView();
        //
        visibleAllBtnMode();
    }

    //visible stuff when btn stop is pressed
    private void btnStopDoVisible() {
        visibleBtnVibration();
        visibleBtnPercussion();
        visibleSleepModeBtn();
        visibleBtnStart();
    }

    //visible invisible side rail icon
    private boolean visibleSideRailIcon() {
        Log.d(TAG, "visibleSideRailIcon: ");
        if (srRigh != null && srLeft != null) {

            srLeft.setVisibility(View.VISIBLE);
            srRigh.setVisibility(View.VISIBLE);
            Log.d(TAG, "visibleSideRailIcon: visible");
            return true;

        }
        return false;
    }

    //visible invisible side rail icon
    private boolean invisibleSideRailIcon() {
        Log.d(TAG, "visibleSideRailIcon: ");
        if (srRigh != null && srLeft != null) {

            srLeft.setVisibility(View.INVISIBLE);
            srRigh.setVisibility(View.INVISIBLE);
            Log.d(TAG, "visibleSideRailIcon: invisible");
            return false;

        }
        return false;
    }

    //invisible/visible custom icon
    private void visibleInviCustomRB_IV(boolean input, RadioButton radioButton, ImageView imageView) {
        if (radioButton != null && imageView != null) {
            if (input) {
                radioButton.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_baseline_eco_24);

                // imageView.setVisibility(View.VISIBLE);
            } else {
                radioButton.setVisibility(View.INVISIBLE);
                imageView.setImageResource(R.drawable.ic_baseline_ac_unit_24);
                //imageView.setVisibility(View.INVISIBLE);
            }
        }
    }


    //-----------------------Alert Dialog-----------------------
    /*Alert Dialog:  Windows on the screen to alert than the Therapy is gonna start and take care of the Side Rail
     */
    //Before start
    private void alertDialogCheckTransd() {

        customAlert.alertDialogCheckSelectedTransd(getApplicationContext());
        //?
        /*try {
            AlertDialog.Builder builder = new AlertDialog.Builder(VibActivity.this);//, R.style.Theme_AppCompat_Light
            builder.setTitle(R.string.alert_Title);
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage("Check transducers! Need to match A# with B#.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            TextView textView = ((TextView) alert.findViewById(android.R.id.message));
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            Log.d(TAG, "alertDialogBeforeStart: Exception" + e);
        }*/
    }

    //Before start
    private void alertDialogBeforeStart() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(VibActivity.this);//, R.style.Theme_AppCompat_Light
            builder.setTitle(R.string.alert_Title);
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage(R.string.alert_message)
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sendDataOnTherapyBLE();
                            visibleSideRailIcon();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            TextView textView = ((TextView) alert.findViewById(android.R.id.message));
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            Log.d(TAG, "alertDialogBeforeStart: Exception" + e);
        }
    }

    //Before start
    private void alertDialogLivePage() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(VibActivity.this);//, R.style.Theme_AppCompat_Light
            builder.setTitle(R.string.alert_Title);
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage("Stop the therapy first! ")
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            TextView textView = ((TextView) alert.findViewById(android.R.id.message));
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            Log.d(TAG, "alertDialogBeforeStart: Exception" + e);
        }
    }

    //Progress dialog Stop
    private void runProgressDialogStop() {
        String title = "Cooling down ...";
        //increase size of the text
        SpannableString ss1 = new SpannableString(title);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length(), 0);
        // ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, ss1.length(), 0);
        //
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(ss1);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressBarStatus = 0;
        fileSize = 0;
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    //------------------Operations---------------//
    //Progress bar Preparing system
    private void runProgressDialog() {
        String title = "Preparing system ...";
        //increase size of the text
        SpannableString ss1 = new SpannableString(title);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length(), 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(ss1);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressBarStatus = 0;
        fileSize = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    //loading system
    private void runLoadingSystem(boolean enable) {
        try {
            if (enable) {

                LayoutInflater inflater = getLayoutInflater();
                View promptsView = inflater.inflate(R.layout.layout_load_system, null);
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(VibActivity.this);
                alertdialog.setView(promptsView);
                alt = alertdialog.create();
                alt.show();
            } else {
                if (alt != null) {
                    alt.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //bluetooth connected
    private void bluetoothConnected(){
        Log.d(TAG, "bluetoothConnected: visible icons");
        runLoadingSystem(false);//remove loading dialog
        try {
            visibleAllSleep();
            visibleBody();
            //
            visibleSleepModeBtn();
            invisibleWakeUpModeBtn();
        }catch (Exception e){
            Log.d(TAG, "bluetoothConnected: Exception:"+e.getMessage());

        }

        //visible all buttons
        //wakeUp(true);

       // turnOnScreen();



    }


    //------------------Bluetooth---------------//

    //BLE-Init
    private void initBLE() {
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                Log.d(TAG, "initBLE:ActivityCompat#requestPermissions ");

                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(enableBtIntent, bluetoothCharacter.REQUEST_ENABLE_BLE);
        }
    }

    // BLE- Check Permissions*/
    private void checkPermissions() {
        /*Check permisions*/
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: it is not granted");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        bluetoothCharacter.REQUEST_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //BLE-BroadCast Receiver
    private void broadCastReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

            this.registerReceiver(mBroadcastReceiver, filter);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: Exception BroadCast Receiver" + e.getMessage());
        }
    }

    //BLE-Broadcast
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Device found
                Log.d(TAG, "onReceive: Device Found");
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
                Log.d(TAG, "onReceive: Device is now connected");
                Toast.makeText(context, "Device is now connected", Toast.LENGTH_SHORT).show();
                //myInstance.dismiss();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
                Log.d(TAG, "onReceive:Done searching ");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
                Log.d(TAG, "onReceive: Device is about to disconnect ");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                Log.d(TAG, "onReceive: Device has disconnected");
                //Toast.makeText(context, "Device has disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //BLE-CallBack From scan
    private ScanCallback mScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBluetoothDevice = result.getDevice();
            }

            //String deviceNameFound = mBluetoothDevice.getName().toString();
            String deviceFound = mBluetoothDevice.getAddress().toString();
            Log.d(TAG, "*************************************************************");
            //Log.d(TAG, "Device found name:-> " + deviceNameFound);
            Log.d(TAG, "Device found Add:-> " + deviceFound);
            //Log.d(TAG, "Looking for :->" + bluetoothCharacter.MAC);
            Log.d(TAG, "*************************************************************");

            mLeDeviceListAdapter.addDevice(mBluetoothDevice);
            mLeDeviceListAdapter.notifyDataSetChanged();
            //Log.d(TAG, "onScanResult: !!!!!!!!!!!!!!");
            //Connect with the device
            if (deviceFound.equalsIgnoreCase(myBleAdd2)) {
                connectToDevice(result.getDevice());
                Log.d(TAG, "I got it: ------------" + deviceFound + "----------------");
                bleIsScanner = false;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    Log.d(TAG, "onScanResult:ActivityCompat#requestPermissions ");
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return;
                }
                mBLEScanner.stopScan(mScanCallback);
                connectToDevice(result.getDevice());
                checkListBLE();//check for the decices found it and mach with list of devices
                /*
                count = mLeDeviceListAdapter.getCount();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        if (mLeDeviceListAdapter.getDevice(i).toString().equalsIgnoreCase(bluetoothCharacter.MAC)) {
                            Toast.makeText(MainActivity.this, "I got it", Toast.LENGTH_SHORT).show();

                        }
                        Log.d(TAG, "mLeDeviceListAdapter): " + "Posit: " + i + "  value:  " + mLeDeviceListAdapter.getDevice(i));
                    }

                }*/
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, "onBatchScanResults: " + results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, "onScanFailed: " + errorCode);
        }
    };

    //check for the list of devices found on BLE
    private void checkListBLE() {
        int count = 0;
        String getDeviceName = "";
        count = mLeDeviceListAdapter.getCount();//size of the array  of the adapter
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                getDeviceName = mLeDeviceListAdapter.getDevice(i).toString();//get address of the device saved on that position
                if (getDeviceName.equalsIgnoreCase(bluetoothCharacter.MAC_K9)) { //found k9
                    //enableGoTherapy(true);
                    //dialogLoading(false);
                } else if (getDeviceName.equalsIgnoreCase(bluetoothCharacter.MAC_KZ)) { //found kz
                    //enableGoKzUnit(true);
                } else if (getDeviceName.equalsIgnoreCase(bluetoothCharacter.MAC_K1200_SYST_A)) { //found bed frame system A
                    //enableGoK7500Unit(true);

                } else if (getDeviceName.equalsIgnoreCase(bluetoothCharacter.MAC_K1200_SYST_B)) { //found  bed frame system B
                    // enableGoK7500Unit(true);

                }
            }
        }
    }

    // Connection with the bluetooth devices
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void connectToDevice(BluetoothDevice device) {
        Log.d(TAG, "connectToDevice: trying to connect");
        if (mGatt == null) {
            //this.mDevice = device;
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
            mGatt = device.connectGatt(getApplicationContext(), true, gattCallback);
            mBLEScanner.stopScan(mScanCallback);
        }
    }

    //Bluetooth gatt 
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Message msg = null;
            switch (status) {
                case BluetoothProfile.STATE_CONNECTING:
                    Log.d(TAG, "onConnectionStateChange: STATE_CONNECTING ");
                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    Log.d(TAG, "onConnectionStateChange: STATE_CONNECTED ");
                    getParamUpdateDevGui(msg, statusSystem.BLE_CONNECTED);


                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    Log.d(TAG, "onConnectionStateChange: STATE_DISCONNECTING ");
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.d(TAG, "onConnectionStateChange: STATE_DISCONNECTED");
                    getParamUpdateDevGui(msg, statusSystem.BLE_DISCONNECTED);
                    break;
            }


            Log.d(TAG, "onConnectionStateChange: newStatus " + newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    getParamUpdateDevGui(msg, statusSystem.BLE_CONNECTED);

                    bluetoothConnected();


                    //discover serices
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        //do permissions
                    }
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    Log.i("gattCallback", "reconnecting...");
                    getParamUpdateDevGui(msg, statusSystem.BLE_DISCONNECTED);

                    BluetoothDevice mDevice = gatt.getDevice();
                    mGatt = null;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            //discover all services
            Log.d(TAG, "onServicesDiscovered: ");
            mGatt = gatt;
            //get list
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                if (service.getUuid().equals(UUID_SERVICE_FREQUENCY)) { //frequency
                    CHARACTERISTIC_FREQUENCY = discoveryBleGatt(mGatt, UUID_SERVICE_FREQUENCY, UUID_CHARACTERISTIC_FREQUENCY);
                } else if (service.getUuid().equals(UUID_SERVICE_INTENSITY)) { //intensity
                    CHARACTERISTIC_INTENSITY = discoveryBleGatt(mGatt, UUID_SERVICE_INTENSITY, UUID_CHARACTERISTIC_INTENSITY);
                } else if (service.getUuid().equals(UUID_SERVICE_TIME)) { //time
                    CHARACTERISTIC_TIME = discoveryBleGatt(mGatt, UUID_SERVICE_TIME, UUID_CHARACTERISTIC_TIME);
                } else if (service.getUuid().equals(UUID_SERVICE_TRANSDUCERS)) { //transducers
                    CHARACTERISTIC_TRANSDUCERS = discoveryBleGatt(mGatt, UUID_SERVICE_TRANSDUCERS, UUID_CHARACTERISTIC_TRANSDUCERS);
                } else if (service.getUuid().equals(UUID_SERVICE_COMMANDS)) { //commands
                    CHARACTERISTIC_COMMANDS = discoveryBleGatt(mGatt, UUID_SERVICE_COMMANDS, UUID_CHARACTERISTIC_COMMANDS);
                } else if (service.getUuid().equals(UUID_SERVICE_PROCESS_VALUE)) { //process value or feedback
                    CHARACTERISTIC_PROCESS_VALUE = discoveryBleGatt(mGatt, UUID_SERVICE_PROCESS_VALUE, UUID_CHARACTERISTIC_PROCESS_VALUE);
                    Log.d(TAG, "onServicesDiscovered: CHARACTERISTIC_PROCESS_VALUE" + CHARACTERISTIC_PROCESS_VALUE.getUuid());
                    broadcastFeedback(ACTION_GATT_SERVICES_DISCOVERED, CHARACTERISTIC_PROCESS_VALUE, mGatt);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "onCharacteristicRead: ");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG, "onCharacteristicWrite: ");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d(TAG, "onCharacteristicChanged: ");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorRead: ");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorWrite: ");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }

        @Override
        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
            super.onServiceChanged(gatt);
        }
    };

    //BLE-Close Gatt
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

    //set descriptor
    private void setDescriptorEnable(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        for (BluetoothGattDescriptor descriptor : bluetoothGattCharacteristic.getDescriptors()) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
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
            bluetoothGatt.writeDescriptor(descriptor);
        }
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
    }

    //broadcast feedback
    private void broadcastFeedback(final String action,
                                   BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt) {
        try {
            if (characteristic != null) {
                Log.d(TAG, "broadcastFeedback: ");
                final Intent intent = new Intent(action);
                if (CHARACTERISTIC_PROCESS_VALUE.equals(characteristic.getUuid())) {
                    //int flag = CHARACTERISTIC_PROCESS_VALUE.getProperties();
                    //int format = -1;
                    Log.d(TAG, "broadcastFeedback: " + characteristic);

                    setDescriptorEnable(bluetoothGatt, characteristic);
                } else {
                    // For all other profiles, writes the data formatted in HEX.
                    final byte[] data = characteristic.getValue();
                    if (data != null && data.length > 0) {
                        final StringBuilder stringBuilder = new StringBuilder(data.length);
                        for (byte byteChar : data)
                            stringBuilder.append(String.format("%02X ", byteChar));
                        intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                                stringBuilder.toString());
                    }
                }
                sendBroadcast(intent);
            } else {
                Log.d(TAG, "broadcastFeedback: characteristics null");
            }
        } catch (Exception e) {
            Log.d(TAG, "broadcastFeedback: exception" + e.getMessage());
        }
    }


    //convert from hex to byte
    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        Log.d(TAG, "hexStringToByteArray: len" + len);
        Log.d(TAG, "hexStringToByteArray: String" + s);
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            Log.d(TAG, "hexStringToByteArray: i" + i);
        }

        return data;
    }

    //Write using bluetooth
    private boolean writeBluetooth(BluetoothGatt bluetoothGatt, BluetoothAdapter bluetoothAdapter, BluetoothGattCharacteristic characteristic, int valueInt) {
        try {
            int unixTime = 0;//format
            if ((bluetoothAdapter == null) || (bluetoothGatt == null) || (characteristic == null)) {
                Log.d(TAG, "writeBluetooth: adapter not initilized");
                return false;
            }
            if (valueInt > 0) {
                unixTime = valueInt;
            } else {
                unixTime = -1;
            }
            //
            String unixTimeString = Integer.toHexString(unixTime);
            byte[] byteArray = hexStringToByteArray(unixTimeString);
            characteristic.setValue(byteArray);
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
            return bluetoothGatt.writeCharacteristic(characteristic);

        } catch (Exception e) {
            Log.d(TAG, "writeBluetooth:  exception" + e.getMessage());
        }
        return false;
    }

    //set value into characteristic--setValueCharact
    private boolean setValueCharact(BluetoothGattCharacteristic characteristic, int valueInt) {
        if (valueInt > 0) {
            if (writeBluetooth(mGatt, mBluetoothAdapter, characteristic, valueInt)) {
                Message msg = null;
                getParamUpdateValueGui(msg, valueInt);
            } else {
                //Toast.makeText(this, "Failed to send data!", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    //get data from UI and send it by bluetooth
    private boolean getUISetpoints(int location, BluetoothGattCharacteristic bluetoothGattCharacteristic, int value) {
        Status status = new Status();
        switch (location) {
            case 1:
                //frequency

                break;
            case 2:
                //Intensity

                break;
            case 3:
                //timer

                break;
            case 4:
                //transd

                break;
            case 5:
                //

                break;


        }
        return true;

    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           /* ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());*/

            return view;
        }

    }

    //map all bluetooth characteristics
    private BluetoothGattCharacteristic discoveryBleGatt(BluetoothGatt bluetoothGatt, UUID uuid_service, UUID uuid_char) {
        BluetoothGattCharacteristic gattCharacteristic = null;
        if (true) {
            gattCharacteristic = bluetoothGatt.getService(uuid_service).getCharacteristic(uuid_char);
            Log.d(TAG, "discoveryBleGatt: match uuid_service:" + uuid_service + "uuid_charac:" + uuid_char);
            //discovery all descriptor
            for (BluetoothGattDescriptor bluetoothGattDescriptor : gattCharacteristic.getDescriptors()) {
                bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
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
                bluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
            }
            bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
        }
        return gattCharacteristic;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == bluetoothCharacter.REQUEST_ENABLE_BLE && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //get parameters update
    private void getParamUpdateDevGui(Message msg, String valueUpdate) {
        msg = Message.obtain();
        value = valueUpdate;
        msg.obj = value;
        msg.what = UPDATE_DEVICE;
        msg.setTarget(uiHandler);
        msg.sendToTarget();
    }

    //get parameters value
    private void getParamUpdateValueGui(Message msg, int valueUpdate) {
        try {
            String mValue = String.valueOf(valueUpdate);
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

    //Upgrade GUI
    private final Handler uiHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            final String value = (String) msg.obj;
            switch (what) {
                case UPDATE_DEVICE:
                    updateDevice(value);
                    Log.d(TAG, "handleMessage: update device");
                    break;
                case UPDATE_VALUE:
                    Log.d(TAG, "handleMessage: update value");
                    updateValue(value);
                    break;
            }
        }
    };

    //Upgrade device information
    private void updateDevice(String value) {
        Log.d(TAG, "updateDevice: value" + value);
        try {
            if (value != null) {
                if (value.equalsIgnoreCase(statusSystem.BLE_CONNECTED)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ivIcon.setImageDrawable(getDrawable(R.drawable.ic_action_connection));
                    }
                } else if (value.equalsIgnoreCase(statusSystem.BLE_DISCONNECTED)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ivIcon.setImageDrawable(getDrawable(R.drawable.ic_action_no_device));
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "updateValue: ");
        }
    }

    /*Update text value*/
    private void updateValue(String value) {
        try {
            int valueInt = Integer.parseInt(value);
            updateFeedbackValue(valueInt);
            updateUIfromBle(valueInt);
            Log.d(TAG, "updateValue: " + value);

        } catch (Exception e) {
            Log.d(TAG, "updateValue: Exception " + e.getMessage());
        }
    }

    //-------------class-----------

    class coolingThreadStart implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "run: !!!!!!!!!!!!!getBLE_SP_COOL_ALL_INIT");
            setValueCharact(CHARACTERISTIC_TRANSDUCERS, setPointsBluetooth.getBLE_SP_COOL_ALL_INIT());//   setValueCharact(CHARACTERISTIC_TRANSDUCERS, setPointsBluetooth.getBLE_SP_COOL_ALL_INIT());
            //cloudStorageSetPoints("Event:", "Therapy is running");
        }
    }

    //Thread cooling stop
    class coolingThreadStop implements Runnable {
        @Override
        public void run() {
            setValueCharact(CHARACTERISTIC_TRANSDUCERS, setPointsBluetooth.getBLE_SP_COOL_ALL_STOP());// setValueCharact(CHARACTERISTIC_TRANSDUCERS, setPointsBluetooth.getBLE_SP_COOL_ALL_STOP());
            //cloudStorageSetPoints("Event:", "Therapy stopped");
        }
    }

    //------------get extrass-----------------//
    //get extras from activity
    private void getExtrasFromAct() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String add = bundle.getString("vibAdd");
            if (add != null) {
                Log.d(TAG, "Vibration getExtrasFromAct: " + add);
                if (add.equalsIgnoreCase(myBleAdd)) {
                    Log.d(TAG, "getExtrasFromAct: same address");
                } else {
                    Log.d(TAG, "getExtrasFromAct: address change");
                    myBleAdd = add;
                }
            }
        }
    }


}