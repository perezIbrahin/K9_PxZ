package com.example.k9_pxz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import Adapter.RecyclerViewAdaptBtnF;
import Adapter.RecyclerViewAdaptBtnI;
import Adapter.RecyclerViewAdaptBtnT;
import Adapter.RecyclerViewAdaptRbA;
import Adapter.RecyclerViewAdaptRbB;
import Adapter.RecyclerViewAdapticonA;
import Adapter.RecyclerViewAdapticonB;
import Alert.Alarm;
import Alert.K9Alert;
import Bluetooth.BleCharacteristics;
import Bluetooth.Ble_Protocol;
import Configuration.Config;
import Eth.MessageEth;
import Eth.TcpClient;
import Eth.TcpEvent;
import Interface.InterfaceSetupInfo;
import Interface.RecyclerViewClickInterface;
import Model.ModelBtn;
import Setpoints.SpEth;
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

public class K9PvzEth extends AppCompatActivity implements InterfaceSetupInfo, RecyclerViewClickInterface, View.OnClickListener, Observer, View.OnLongClickListener, View.OnTouchListener {
    private static final String TAG = "K9PvzEth";
    /*GUI*/
    //modes
    private Button btnSelectPer;
    private Button btnSelectVib;
    private Button btnSelectTotalPer;
    private Button btnSelectTotalVib;
    private Button btnLockOp;
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
    private TextView tvDate;
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
    //Declaration of instances
    private SetPoints setPoints = new SetPoints();
    private TextSize textSize = new TextSize();
    private ControlGUI controlGUI = new ControlGUI();
    private Beep beep = new Beep();
    //private SetPointsBluetooth setPointsBluetooth = new SetPointsBluetooth();
    private Default_values default_values = new Default_values();
    private Key_Util keyUtil = new Key_Util();
    //private ActionGatt actionGatt = new ActionGatt();
    //private UUID uuid = new UUID();
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
    private Config config = new Config();
    private Status status = new Status();
    private Rev rev = new Rev();  //revision
    //
    SpEth spEth = new SpEth();//ethernet setpoint
    //TCP ip
    private TcpClient client;


    //countdown timers
    CountDownTimer timerCheckNetwork = null;
    private CountDownTimer timerTherapy = null;
    CountDownTimer timerForceSop = null;
    CountDownTimer timerLockColdDown = null;

    //others
    private K9Alert k9Alert;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    AlertDialog.Builder builder;
    //ethernet
    public static final String EXTRA_MESSAGE = "space.mzero.tcpz.MESSAGE";
    public static String msg = "default";
    public static String resp = "No Response Yet\nTry Sending Some Data.";
    //network
    public static Boolean Abort = false;
    // public static LongOperation lo = null;//need to be enabled
    public static Socket socket = null;
    //update display
    private final static int UPDATE_DEVICE = 0;
    private final static int UPDATE_VALUE = 1;
    private final static int UPDATE_CLOUD = 2;
    private final static int UPDATE_MCU = 3;
    String value = null;
    //therapy
    private boolean isTherapyOn = false;
    //get conditions to start therapy
    private boolean flagIsFreq = false;
    private boolean flagIsInt = false;
    private boolean flagIsTim = false;
    private boolean flagIsTRA = false;
    private boolean flagIsTRB = false;
    private boolean isFlagIsSr = false;
    private boolean isFlagMcuReady = false;
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
    private String dialogUnderCurrent = "0";
    private String dialogOverCurrent = "0";

    //serial number
    private String Serial_Number_Product = "12PV123456";
    //get type of therapy
    private int typeOfTherapy = 0;
    private Validation validation = new Validation();
    private String serialNumber = "0";
    //
    private boolean isLockMode = false;
    private int mode = 0;//get mode
    private int mProtocol = 0;
    private int mSp = 0;
    //alarm
    private boolean isAlarm = true;
    private long TIMER_WACHT_DOG = 3000;
    private long TIMER_LOOP_STATUS = 3000;//
    //watchDog
    private CountDownTimer watchDogTimer;
    private CountDownTimer loopGetStatusTimer;

    //Display operations
    private DisplayOperations displayOperations = new DisplayOperations();
    private boolean isEnableGui = false;
    //ethernet TCp
    private boolean isConnectedTCP = false;

    /*Ethernet*/
    private PrintWriter output;
    private BufferedReader input;
    private String SERVER_IP = "192.168.6.203";//by default
    private int SERVER_PORT = 1200;
    Thread Thread1 = null;
    Socket socket1;
    private String socketStatus = null;
    //
    private int countWatchDog = 0;
    private int MAXWatchDog = 3;
    /**/
    private long down, up;//get how long key was pressed
    private boolean isLockScreen = false;
    private Handler handler;
    //preferences
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    //hide navigation bar
    private int currentApiVersion;
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    //
    private boolean isFlagAlarmUc = false;//alarm under current
    private boolean isFlagAlarmOc = false;//alarm under current
    //
    private int counterUc = 0;//avoid bad flags
    private int counterOc = 0;

    //Memory of transducers
    private int memoryTransdA = -1;
    private int memoryTransdB = -1;

    //added
    private int oldCurrentModule = 0;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //screen full size
        //screenFullSize();
        //load layout
        loadLayout(R.layout.layout_percussion_vibration);

        //remove menu bar
        hideNavigationBar();

        //remove action bar from top
        // removeActionBar();
        // notified of system UI visibility changes
        //systemUiChanges();
        //init all components
        initGUI();
        //init bluetooth and broadcast
        initOther();
        //init parameters for app
        initApp();
        //init language
        initLang();
        //buttons events
        eventBtn();
        //loading alert dialog
        alertDialogLoading(true);
        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_11);
        //disable wifi
        disableWIFI();
        //request every 3 sec status of the host
        requestStatusFromHostTimer();
        //display current date
        displayDate();
        //orientation of the screen landscape
        setOrientationLandscape();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //cancel timer to request status from host
        cancelRequestStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**********************************************
     *Init
     */
    //init GUI
    private void initGUI() {
        btnSelectPer = findViewById(R.id.btnSelPerc);
        btnSelectVib = findViewById(R.id.btnSelVib);
        btnSelectTotalPer = findViewById(R.id.btnTotalPerc);
        btnSelectTotalVib = findViewById(R.id.btnTotalVib);
        btnLockOp = findViewById(R.id.btnLockOp);
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
        tvDate = findViewById(R.id.tvDate);
        //
        tvTitle = findViewById(R.id.tvTextPvTitile);
        tvCurrent = findViewById(R.id.tvCurrent);
    }

    //init system
    private boolean initApp() {
        k9Alert = new K9Alert(this, this);
        isAlarm = false;//just for test

        //myBleAdd = getExtrasFromAct(myBleAdd);
        //updateBtnReady(controlGUI.POS0);

        //Invisible button ready. Used in older revision
        updateBtnReady(controlGUI.POS0);


        return true;
    }

    //Init all
    private boolean initOther() {
        //get data from Main
        if (getExtraIntent()) {
            Log.d(TAG, "Extras from Main: ");
            initEthernet();
        }
        return true;
    }

    //init Ethernet
    private void initEthernet() {
        //need to be initialized
       /* SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String nserver_address = prefs.getString("server", null);

        //
        if (nserver_address != null) {

        }
        Integer nserver_port = prefs.getInt("port", 1200);

        if (nserver_port != 1200) {
            Log.d("load port", nserver_port.toString());
            //TextView port_text = (TextView) findViewById(R.id.portText);
            // port_text.setText(nserver_port.toString());
        }
        //test communication
        sendMessageEth(spEth.k9_op_1);*/
        initTCP_IP();
    }

    //init array spinner language
    private void initLang() {
        Log.d(TAG, "initLang: language:" + language);
        //Context context = LocaleHelper.setLocale(VibrationPercussionActivity.this, language);
        //resources = context.getResources();
        loadContentByLanguage(getResourcesLanguage(language));

    }

    /**********************************************
     *Interface
     */
    @Override
    public void onItemSetupInfo(String name, String description) {
        Util.Message message = new Util.Message();
        Log.d(TAG, "onItemSetupInfo: " + description);
        if (description.equalsIgnoreCase(utilDialog.LOCATION_EXIT_THERAPY)) {
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_CONN_FAILED)) {
            goHome();
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_CONFIRM_SIDERAIL)) {//
            isFlagIsSr = true;
            updateSideRail(controlGUI.POS0);
            //
            boolean isTRAB = false;
            if (flagIsTRA || flagIsTRB) {
                isTRAB = true;
            } else {
                isTRAB = false;
            }

            condStartTherapy(flagIsFreq, flagIsInt, flagIsTim, isTRAB, isTRAB, isFlagIsSr, isFlagMcuReady);//   condStartTherapy(flagIsFreq, flagIsInt, flagIsTim, flagIsTRA, flagIsTRB, isFlagIsSr,isFlagMcuReady);

            //changed 10/19/22
            // displayOperation(displayOperations.DISPLAY_OPE_READY);//Ready
            updateBtnReady(controlGUI.POS1);
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_EMERGENCY_STOP_CONFIRM)) {
            Log.d(TAG, "onItemSetupInfo: emergency stop");
            goHome();
        } else if (description.equalsIgnoreCase(utilDialog.THERAPY_DONE)) {
            Log.d(TAG, "onItemSetupInfo: THERAPY_DONE");
        } else if (description.equalsIgnoreCase(utilDialog.LOCATION_ACK_CON_FAIL)) {
            //counterFail=0;
        } else if (description.equalsIgnoreCase(utilDialog.UNDER_CURRENT_CONFIRM)) {
            Log.d(TAG, "onItemSetupInfo: confirm UNDER_CURRENT_CONFIRM");
            isFlagAlarmUc = false;
        } else if (description.equalsIgnoreCase(utilDialog.OVER_CURRENT_CONFIRM)) {
            Log.d(TAG, "onItemSetupInfo: OVER_CURRENT_CONFIRM");
            isFlagAlarmOc = false;
        }
    }

    @Override
    public void onItemSetupAlarm(String name, String description, String location) {

    }

    @Override
    public void onItemPostSelect(int position, String value) {
        Log.d(TAG, "onItemPostSelect: pos" + position + "val:" + value);
        //conditions to disable the use of setpoints
        boolean conditions = isTherapyOn || isLockScreen;

        if (value.equalsIgnoreCase(recyclerLocations.LOCATION_VIB_FREQ)) {
            //send data of freq
            sendSpFreq(position, conditions);//isTherapyOn
        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_VIB_INT)) {
            Log.d(TAG, "onItemPostSelect: int");
            //send data of Int
            sendSpInt(position, conditions);
        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_VIB_TIM)) {
            Log.d(TAG, "onItemPostSelect: time");
            //send data of Time
            sendSpTime(position, conditions);
        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_RB_A)) {
            if (!isTotalBody(mode)) {
                Log.d(TAG, "onItemPostSelect: mem a:memoryTransdA:" + memoryTransdA + ".position:" + position);
                //new
                if (memoryTransdA != position) {
                    memoryTransdA = position;
                    //do not accept commands manualy is total body
                    sendSpRBA(position, conditions);
                } else {
                    //clean flag
                    cleanSelectedTrandA(conditions);
                }
            }
        } else if (value.equalsIgnoreCase(recyclerLocations.LOCATION_RB_B)) {
            Log.d(TAG, "onItemPostSelect: " + ".position:" + position);
            if (!isTotalBody(mode)) {
                //do not accept commands manualy is total body

                //new
                if (memoryTransdB != position) {
                    memoryTransdB = position;
                    //do not accept commands manualy is total body
                    sendSpRBB(position, conditions);
                } else {
                    //clean flag
                    cleanSelectedTrandB(conditions);
                }
                //sendSpRBB(position, conditions);//working
            }
        }
    }

    private void cleanSelectedTrandA(boolean conditions) {
        if (!conditions) {
            Log.d(TAG, "cleanSelectedTrandA: ");
            sendSpRBA(5, conditions); //clean
        }
    }

    private void cleanSelectedTrandB(boolean conditions) {
        if (!conditions) {
            Log.d(TAG, "cleanSelectedTrandB: ");
            sendSpRBB(5, conditions); //clean
        }
    }

    private void cleanFlagsAfterResetTransA() {
        flagIsTRA = false;
        memoryTransdA = -1;

    }

    private void cleanFlagsAfterResetTransB() {

        flagIsTRB = false;
        memoryTransdB = -1;
    }

    /**********************************************
     *utility
     */
    //type of therapy
    /*This instance will get the serial number and check if is for Percussion or percussion and vibration*/
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

    //check installed system
    private int checkingInstalledSystem(String value) {
        if (value != null) {
            return validation.validateIsPV(value);
        }
        return 0;
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

    //hide system bar
    private void hideSystemBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.

        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());


    }

    //remove menu bar
    private void hideNavigationBar() {
        try {
            currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(flags);
                final View decorView = getWindow().getDecorView();
                decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
            }



           /* View decorView = getWindow().getDecorView();
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);*/


        } catch (Exception e) {
            Log.d(TAG, "removeMenuBar: ex:" + e.getMessage());
        }
    }

    //events on UI
    /*private void systemUiChanges() {
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        Log.d(TAG, "onSystemUiVisibilityChange: " + visibility);
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            //hideNavigationBar();
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });
    }*/

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: ");
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            Log.d(TAG, "onWindowFocusChanged: yes");
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**********************************************
     *Navigation between activities
     */
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

    /**********************************************
     * Get extras
     */
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
                    if (config.isNetworkBle) {
                        /*BLE_BED = str;
                        if (!BLE_BED.equalsIgnoreCase(keyUtil.DEFAULT_ADDRESS)) {
                            BLE_BED = dataIntent.getStringExtra(keyUtil.KEY_ADDRESS_BED);
                            Log.d(TAG, "getExtraIntent: BLE_BED: " + BLE_BED);
                        }*/
                    } else if (config.isNetworkEth) {

                    }
                }
            }
        }
        return true;
    }

    /**********************************************
     * Hardware
     */
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

    /**********************************************
     * Language
     */
    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(K9PvzEth.this, language);
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
        //error code
        dialogEmergStop = resources.getString(R.string.string_alarm_emergency_stop);
        dialogHardwareFail = resources.getString(R.string.string_alarm_hardware_fail);
        dialogUnderCurrent = resources.getString(R.string.string_alarm_under_current);
        dialogOverCurrent = resources.getString(R.string.string_alarm_over_current);
        //
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


    /**********************************************
     * Models/AdapterView
     */
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(K9PvzEth.this);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(K9PvzEth.this);
        recyclerViewI.setLayoutManager(linearLayoutManager);//error
        recyclerViewI.setHasFixedSize(true);
        recyclerViewAdaptBtnI = new RecyclerViewAdaptBtnI(this, modelBtnIntArrayList);//modelDevicesArrayList
        recyclerViewI.setAdapter(recyclerViewAdaptBtnI);
    }

    //update Recycler view Time
    private void updateGUIRecyclerViewT() {
        recyclerViewT = findViewById(R.id.RecyclerViewTime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(K9PvzEth.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewT.setLayoutManager(linearLayoutManager);//error
        recyclerViewT.setHasFixedSize(true);
        recyclerViewAdaptBtnT = new RecyclerViewAdaptBtnT(this, modelBtnTimeArrayList);//modelDevicesArrayList
        recyclerViewT.setAdapter(recyclerViewAdaptBtnT);
    }

    //update Recycler view RadioButton -A
    private void updateGUIRecyclerViewRbA() {
        recyclerViewRbA = findViewById(R.id.RecyclerViewRbA);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(K9PvzEth.this);
        recyclerViewRbA.setLayoutManager(linearLayoutManager);//error
        recyclerViewRbA.setHasFixedSize(true);
        recyclerViewAdaptRbA = new RecyclerViewAdaptRbA(this, modelBtnArrayListA);//modelDevicesArrayList
        recyclerViewRbA.setAdapter(recyclerViewAdaptRbA);
    }

    //update Recycler view RadioButton -B
    private void updateGUIRecyclerViewRbB() {
        recyclerViewRbB = findViewById(R.id.RecyclerViewRbB);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(K9PvzEth.this);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(K9PvzEth.this);
        recyclerViewIconA.setLayoutManager(linearLayoutManager);//error
        recyclerViewIconA.setHasFixedSize(true);
        recyclerViewAdaptIconA = new RecyclerViewAdapticonA(modelIconArrayListA);//modelDevicesArrayList
        recyclerViewIconA.setAdapter(recyclerViewAdaptIconA);
    }

    //update Recycler view  icon B
    private void updateGUIRecyclerViewIconB() {
        recyclerViewIconB = findViewById(R.id.RecyclerViewIconB);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(K9PvzEth.this);
        recyclerViewIconB.setLayoutManager(linearLayoutManager);//error
        recyclerViewIconB.setHasFixedSize(true);
        recyclerViewAdaptIconB = new RecyclerViewAdapticonB(modelIconArrayListB);//modelDevicesArrayList
        recyclerViewIconB.setAdapter(recyclerViewAdaptIconB);
    }

    /**********************************************
     * Feedback from host/update GUI
     */
    // get return from bluetooth and update GUI  frequency
    private boolean updateButtonsFrequencyF(int value) {
        Log.d(TAG, "updateButtonsFrequencyF: ");
        if (value > 0) {
            try {
                if (value == setPoints.INT_BLE_SP_FREQ_NONE) {
                    //none
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_FREQ1) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);

                } else if (value == setPoints.INT_BLE_SP_FREQ2) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);

                } else if (value == setPoints.INT_BLE_SP_FREQ3) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_FREQ4) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_FREQ5) {
                    updateRecyclerViewF(0, default_values.DEF_FREQ1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(1, default_values.DEF_FREQ2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(2, default_values.DEF_FREQ3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(3, default_values.DEF_FREQ4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewF(4, default_values.DEF_FREQ5, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewF(5, default_values.DEF_FREQ_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_FREQ_MAX) {
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
                if (value == setPoints.INT_BLE_SP_INT_NONE) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_INT1) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_INT2) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_INT3) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_INT4) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_INT5) {
                    updateRecyclerViewI(0, default_values.DEF_INT1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(1, default_values.DEF_INT2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(2, default_values.DEF_INT3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(3, default_values.DEF_INT4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewI(4, default_values.DEF_INT5, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewI(5, default_values.DEF_INT_MAX, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_INT_MAX) {
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
                if (value == setPoints.INT_BLE_SP_TIME_NONE) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_TIME1) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_TIME2) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_TIME3) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_TIME4) {
                    updateRecyclerViewT(0, default_values.DEF_TIM1, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(1, default_values.DEF_TIM2, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(2, default_values.DEF_TIM3, default_values.DEF_STATUS_UNCHECKED, null);
                    updateRecyclerViewT(3, default_values.DEF_TIM4, default_values.DEF_STATUS_CHECKED, getDrawable(R.drawable.ic_baseline_circle_32));
                    updateRecyclerViewT(4, default_values.DEF_TIM5, default_values.DEF_STATUS_UNCHECKED, null);
                } else if (value == setPoints.INT_BLE_SP_TIME5) {
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
        Log.d(TAG, "updateButtonsRbAPercussion: "+value);
        if (value > 0) {
            try {
                if (value == setPoints.INT_BLE_SP_TRA_NONE) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    //added 02/10/23
                    controlIconTransdA(controlGUI.POS0);


                } else if (value == setPoints.INT_BLE_SP_TRA1) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS1);

                } else if (value == setPoints.INT_BLE_SP_TRA2) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA

                    controlIconTransdA(controlGUI.POS2);

                } else if (value == setPoints.INT_BLE_SP_TRA6) {//clean
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    cleanFlagsAfterResetTransA();
                    //added 02/10/23
                    controlIconTransdA(controlGUI.POS0);

                } else if (value == setPoints.INT_BLE_CMD_TOTAL_PERC) {
                    Log.d(TAG, "updateButtonsRbAPercussion:setPoints.INT_BLE_CMD_TOTAL_PERC ");
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));

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
        if (value > 0) {/**/
            try {
                if (value == setPoints.INT_BLE_SP_TRA_NONE) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS0);

                } else if (value == setPoints.INT_BLE_SP_TRA1) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS1);
                } else if (value == setPoints.INT_BLE_SP_TRA2) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS2);
                } else if (value == setPoints.INT_BLE_SP_TRA3) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS3);
                } else if (value == setPoints.INT_BLE_SP_TRA4) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS4);
                } else if (value == setPoints.INT_BLE_SP_TRA5) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconA
                    controlIconTransdA(controlGUI.POS5);
                } else if (value == setPoints.INT_BLE_SP_TRA6) {//clean
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    cleanFlagsAfterResetTransA();
                    //update iconA
                    controlIconTransdA(controlGUI.POS0);


                } else if (value == setPoints.INT_BLE_CMD_TOTAL_VIB) {
                    updateRecyclerViewRbA(0, default_values.DEF_RBA1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(1, default_values.DEF_RBA2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(2, default_values.DEF_RBA3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(3, default_values.DEF_RBA4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbA(4, default_values.DEF_RBA5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconA
                    if (mode != status.SELECT_MODE_TOTAL_VIBRATION) {
                        controlIconTransdA(controlGUI.POS_ALL_VIB);
                    }

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
                if (value == setPoints.INT_BLE_SP_TRB_NONE) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS0);
                } else if (value == setPoints.INT_BLE_SP_TRB1) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    // updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS1);
                } else if (value == setPoints.INT_BLE_SP_TRB2) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS2);
                } else if (value == setPoints.INT_BLE_SP_TRB6) {//clean
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //
                    cleanFlagsAfterResetTransB();
                    //update iconB
                    controlIconTransdB(controlGUI.POS0);
                } else if (value == setPoints.INT_BLE_CMD_TOTAL_PERC) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
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
                if (value == setPoints.INT_BLE_SP_TRB_NONE) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS0);
                } else if (value == setPoints.INT_BLE_SP_TRB1) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS1);
                } else if (value == setPoints.INT_BLE_SP_TRB2) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS2);
                } else if (value == setPoints.INT_BLE_SP_TRB3) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS3);
                } else if (value == setPoints.INT_BLE_SP_TRB4) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS4);
                } else if (value == setPoints.INT_BLE_SP_TRB5) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconB
                    controlIconTransdB(controlGUI.POS5);
                } else if (value == setPoints.INT_BLE_SP_TRB6) {//clean
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_radio_button_unchecked_48));
                    //
                    cleanFlagsAfterResetTransB();
                    //update iconB
                    controlIconTransdB(controlGUI.POS0);
                } else if (value == setPoints.INT_BLE_CMD_TOTAL_VIB) {
                    updateRecyclerViewRbB(0, default_values.DEF_RBB1, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));//DEF_RBA1
                    updateRecyclerViewRbB(1, default_values.DEF_RBB2, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(2, default_values.DEF_RBB3, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(3, default_values.DEF_RBB4, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    updateRecyclerViewRbB(4, default_values.DEF_RBB5, default_values.DEF_STATUS_UNCHECKED, getDrawable(R.drawable.ic_baseline_circle_48));
                    //update iconB
                    if (mode != status.SELECT_MODE_TOTAL_VIBRATION) {
                        controlIconTransdB(controlGUI.POS_ALL_VIB);
                    }

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
                if (value == setPoints.INT_BLE_CMD_NONE) {
                    // btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //added 10/19/22
                    btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_wh));
                    btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));
                    return setPoints.INT_BLE_CMD_NONE;
                } else if ((value == setPoints.INT_BLE_CMD_START)) {
                    //btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48));
                    //btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //added 10/19/22
                    btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_wh));
                    btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));
                    displayOperation(displayOperations.DISPLAY_OPE_RUNNING);//running
                    return setPoints.INT_BLE_CMD_START;
                } else if ((value == setPoints.INT_BLE_CMD_STOP)) {
                    //btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    // btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));
                    //added 10/19/22
                    btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_wh));
                    btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));
                    displayOperation(displayOperations.DISPLAY_OPE_STOPPED);//stopped
                    return setPoints.INT_BLE_CMD_STOP;
                }
                // return true;

            } catch (Exception e) {
                Log.d(TAG, "uupdateCommand: " + e.getMessage());
            }
        }
        return setPoints.INT_BLE_CMD_NONE;
    }

    //get return from bluetooth and update GUI  modes
    private int updateModes(int value) {
        if (value > 0) {
            try {
                if (value == setPoints.INT_BLE_SP_MODE1) {//added 10/19/22
                    mode = selectMode(status.SELECT_MODE_PERCUSSION);
                    return setPoints.INT_BLE_SP_MODE1;
                } else if (value == setPoints.INT_BLE_SP_MODE2) {//added 10/19/22
                    mode = selectMode(status.SELECT_MODE_VIBRATION);
                    return setPoints.INT_BLE_SP_MODE2;
                } else if (value == setPoints.INT_BLE_SP_MODE3) {//added 10/19/22
                    mode = selectMode(status.SELECT_MODE_TOTAL_PERCUSSION);
                    return setPoints.INT_BLE_SP_MODE3;
                } else if (value == setPoints.INT_BLE_SP_MODE4) {//added 10/19/22
                    mode = selectMode(status.SELECT_MODE_TOTAL_VIBRATION);
                    return setPoints.INT_BLE_SP_MODE4;
                }

            } catch (Exception e) {
            }
        }
        return setPoints.INT_BLE_SP_MODE9;
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
        return setPoints.INT_BLE_CMD_NONE;
    }

    //update lock screen
    private int updateLockScreen(int value) {
        try {
            if (value == status.SELECT_SCREEN_UNLOCK) {
                btnLockOp.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_baseline_lock_open_32), null);
                btnLockOp.setTextColor(getResources().getColor(R.color.black));
                btnLockOp.setTextSize(25);
                btnLockOp.setText(getResources().getString(R.string.string_text_btn_lock));
            } else if (value == status.SELECT_SCREEN_LOCK) {
                btnLockOp.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_circle_32), null, getDrawable(R.drawable.ic_baseline_lock_32), null);
                btnLockOp.setTextColor(getResources().getColor(R.color.red));
                btnLockOp.setTextSize(25);
                btnLockOp.setText(getResources().getString(R.string.string_text_action_locked));
            }
        } catch (Exception e) {
            Log.d(TAG, "updateLockScreen: ex:" + e.getMessage());
        }
        return value;
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

    /**********************************************
     * GUI Display utilities
     */
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

    //control icon transducers A
    private void controlIconTransdA(int value) {
        try {
            Log.d(TAG, "controlIconTransdA: " + value);
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
                updateRecyclerViewIconA(position, default_values.DEF_STATUS_ICON, default_values.DEF_STATUS_ICON_RUNNING, getDrawable(R.drawable.ic_baseline_surround_sound_32));
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

    /**********************************************
     * GUI Display
     */
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

    //Display data
    private void displayData(String data) {
        Log.d(TAG, "instance initializer display data:" + data);
        //Toast.makeText(this, "Btn pressd", Toast.LENGTH_SHORT).show();
        //Date currentTime = Calendar.getInstance().getTime();
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String event = "Pressed:" + currentTime.toString();
        //btnTherm.setText(event);
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

    //quality of the signal
    private void displayRssiValue(int value) {
        String unit = "dBm";
        Log.d(TAG, "displayRssiValue: " + value + "dBi");/**/
        //tvRssi.setText(String.valueOf(value) + unit);
    }

    //display feedback
    private void displayFeedBackStatus(int value, int code) {
        try {
            if (value > 0) {
                String str = (String) String.valueOf(value);
                if (str != null) {
                    tvCurrent.setText("stat:" + str + "\r\n+code:" + code);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "displayFeedBackStatus: ex:" + e.getMessage());
        }
    }

    //dispay date
    private void displayDate() {
        try {
            if (tvDate != null) {
                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());
                if (date != null) {
                    tvDate.setText(date);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "displayDate: ex:" + e.getMessage());
        }
    }

    /**********************************************
     * Feedback from Host
     */
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
        Log.d(TAG, "updateFbRBA: mode:"+mode+".value:"+value);
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
                if (ret == setPoints.INT_BLE_CMD_NONE) {
                } else if (ret == setPoints.INT_BLE_CMD_START) {
                    updateBtnReady(controlGUI.POS0);
                    launchRunTherapy(valueTimerTherapy, countInterval);
                    //update display with language
                    displayStartCommand(resources.getString(R.string.string_text_btn_running));
                } else if (ret == setPoints.INT_BLE_CMD_STOP) {
                    displayStartCommand(resources.getString(R.string.string_text_btn_start));
                    //displayStartCommand("Start");
                    if (isFlagTimerElapsed) {
                        notificationTimerElapsed();
                    } else {
                        forceStopTimerTherapy();
                    }
                }
            }
        });
    }

    //update feedback modes
    private void updateFbModes(int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int ret = updateModes(value);
                Log.d(TAG, "updateFbModes: ");
                if (ret == setPoints.INT_BLE_SP_MODE1) {//Added 10/19/22
                    Log.d(TAG, "updateFbCommands: setPointsBluetooth.PERC");
                    mode = status.SELECT_MODE_PERCUSSION;
                    //
                    memoryTransdA = -1;
                    memoryTransdB = -1;
                    Log.d(TAG, "updateFbModes:modes: "+ mode);
                } else if (ret == setPoints.INT_BLE_SP_MODE2) {//Added 10/19/22
                    mode = status.SELECT_MODE_VIBRATION;
                    Log.d(TAG, "updateFbCommands: setPointsBluetooth.vib");
                    //
                    memoryTransdA = -1;
                    memoryTransdB = -1;

                    Log.d(TAG, "updateFbModes:modes: "+ mode);
                } else if (ret == setPoints.INT_BLE_SP_MODE3) {//Added 10/19/22
                    Log.d(TAG, "updateFbCommands: setPointsBluetooth.INT_BLE_CMD_TOTAL_PERC");
                    //
                    memoryTransdA = 2;
                    memoryTransdB = 2;

                    resetCheckBockA();
                    resetCheckBockB();
                    //check mode
                    Log.d(TAG, "updateFbCommands: total perc");
                    mode = status.SELECT_MODE_TOTAL_PERCUSSION;
                    updateFbRBA(mode, setPoints.INT_BLE_CMD_TOTAL_PERC);
                    updateFbRBb(mode, setPoints.INT_BLE_CMD_TOTAL_PERC);
                    Log.d(TAG, "updateFbModes:modes: "+ mode);

                } else if (ret == setPoints.INT_BLE_SP_MODE4) {//Added 10/19/22
                    Log.d(TAG, "updateFbCommands: total vib");
                    //
                    memoryTransdA = 4;
                    memoryTransdB = 4;

                    resetCheckBockA();
                    resetCheckBockB();
                    //check mode
                    mode = status.SELECT_MODE_TOTAL_VIBRATION;
                    Log.d(TAG, "updateFbModes:modes: "+ mode);
                    //
                    updateFbRBA(mode, setPoints.INT_BLE_CMD_TOTAL_VIB);
                    updateFbRBb(mode, setPoints.INT_BLE_CMD_TOTAL_VIB);
                    //
                    controlIconTransdA(controlGUI.POS5);
                    controlIconTransdB(controlGUI.POS5);
                }
            }
        });
    }

    //update feedback status
    private void updateFbStatus(int value, int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateFbStatus: " + value);
                //display info on screen
                displayFeedBackStatus(value, code);

                if (value == setPoints.INT_BLE_STATUS_READY) {
                    cleanFlagsOcUc();
                } else if (value == setPoints.INT_BLE_STATUS_WORKING) {

                } else if (value == setPoints.INT_BLE_STATUS_ALARM1) {
                    notificationAlarmDummy();
                } else if (value == setPoints.INT_BLE_STATUS_ALARM2) {//over current
                    notificationOverCurrent();
                } else if (value == setPoints.INT_BLE_STATUS_ALARM3) {//under current
                    notificationUnderCurrent();
                }
                displayDate();

                /*// Stuff that updates the UI
                int ret = updateCommand(value);
                //get language resources
                Resources resources = getResourcesLanguage(language);
                //
                if (ret == setPoints.INT_BLE_CMD_NONE) {
                } else if (ret == setPoints.INT_BLE_CMD_START) {
                    updateBtnReady(controlGUI.POS0);
                    launchRunTherapy(valueTimerTherapy, countInterval);
                    //update display with language
                    displayStartCommand(resources.getString(R.string.string_text_btn_running));
                } else if (ret == setPoints.INT_BLE_CMD_STOP) {
                    displayStartCommand(resources.getString(R.string.string_text_btn_start));
                    //displayStartCommand("Start");
                    if (isFlagTimerElapsed) {
                        notificationTimerElapsed();
                    } else {
                        forceStopTimerTherapy();
                    }
                }*/
            }
        });
    }

    /**********************************************
     * Sound alerts
     */
    //beep
    private void beepSound() {
        Log.d(TAG, "beepSound: ");
        /*if (!isFlagSetConnection) {
            setConnetion();
        }*/

        beep.beep_key();
    }

    //beep alarm
    private void beepAlarm() {
        beep.beep1();
    }

    /**********************************************
     * Operations:mode/transducers
     */
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
            updateButtonsRbAPercussion(setPoints.INT_BLE_SP_TRA_NONE);
            updateButtonsRbBPercussion(setPoints.INT_BLE_SP_TRB_NONE);
            updateMode(mode);
            return status.SELECT_MODE_PERCUSSION;

        } else if (mode == status.SELECT_MODE_VIBRATION) {
            //reset flag selected transd
            flagIsTRA = false;
            flagIsTRB = false;
            resetCheckBockA();
            resetCheckBockB();
            updateButtonsRbA(setPoints.INT_BLE_SP_TRA_NONE);//selection transducer position A
            updateButtonsRbB(setPoints.INT_BLE_SP_TRB_NONE);//selection transducer position A

            updateMode(mode);
            return status.SELECT_MODE_VIBRATION;
        } else if (mode == status.SELECT_MODE_TOTAL_VIBRATION) {
            //reset flag selected transd
            flagIsTRA = false;
            flagIsTRB = false;
            resetCheckBockA();
            resetCheckBockB();
            updateButtonsRbA(setPoints.INT_BLE_SP_TRA_NONE);//selection transducer position A
            updateButtonsRbB(setPoints.INT_BLE_SP_TRB_NONE);//selection transducer position A
            updateMode(mode);
            //totalVibration();
            return status.SELECT_MODE_TOTAL_VIBRATION;
        } else if (mode == status.SELECT_MODE_TOTAL_PERCUSSION) {
            //reset flag selected transd
            flagIsTRA = false;
            flagIsTRB = false;
            resetCheckBockA();
            resetCheckBockB();
            updateButtonsRbAPercussion(setPoints.INT_BLE_SP_TRA_NONE);
            updateButtonsRbBPercussion(setPoints.INT_BLE_SP_TRB_NONE);
            updateMode(mode);
            //totalPercussion();
            return status.SELECT_MODE_TOTAL_PERCUSSION;
        }


        return 0;
    }

    //lock mode
    private void lockMode(boolean input) {
        isLockMode = input;
    }

    //check is Full body or manual
    private boolean isTotalBody(int mode) {
        if ((mode == status.SELECT_MODE_TOTAL_VIBRATION) || (mode == status.SELECT_MODE_TOTAL_PERCUSSION)) {
            return true;
        }
        return false;
    }

    /**********************************************
     * Operations:connection
     */
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

    private void enableGui(boolean input) {
        Log.d(TAG, "enableGui: " + input);
        if (input) {
            if (!isEnableGui) {
                //load view
                updateButtonsFrequencyF(setPoints.INT_BLE_SP_FREQ_NONE);//frequency
                updateButtonsIntensity(setPoints.INT_BLE_SP_INT_NONE);//intensity
                updateButtonsTime(setPoints.INT_BLE_SP_TIME_NONE);//time
                updateCommand(setPoints.INT_BLE_CMD_NONE);
                isEnableGui = true;
            }
        } else {
            updateButtonsFrequencyF(setPoints.INT_BLE_SP_CLEAN);
            isEnableGui = false;
        }
    }

    //conditions necesary to start therapy
    private void condStartTherapy(boolean flagIsFreq, boolean flagIsInt, boolean flagIsTim,
                                  boolean flagIsTRA, boolean flagIsTRB, boolean flagIsSR, boolean isFlagMcuReady) {
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
                } else if (flagIsFreq && flagIsInt && flagIsTim && flagIsTRA && flagIsSR && isFlagMcuReady) {
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
    private int checkMissingParam(boolean frq, boolean intens, boolean time, boolean rba,
                                  boolean rbb, boolean siderail) {
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

        //do not clean flag if screen is locked
        if (!isLockScreen || isTherapyOn) {
            cancelReady();
        }
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

    //cancel ready
    private void cancelReady() {
        lockMode(false);
        isFlagIsSr = false;
        updateBtnReady(controlGUI.POS0);//invisible ready
    }

    //operation lock screen
    private boolean lockScreen(boolean input) {
        Log.d(TAG, "lockScreen: " + input);
        //if therapy on not do nothing
       /* if (isTherapyOn) {
            return false;
        }*/

        if (!isLockScreen) {
            isLockScreen = true;
            updateLockScreen(status.SELECT_SCREEN_LOCK);
            lockMode(isLockScreen);
            beepSound();
            return isLockScreen;
        }
        isLockScreen = false;
        updateLockScreen(status.SELECT_SCREEN_UNLOCK);
        lockMode(isLockScreen);
        beepSound();
        return isLockScreen;
    }

    /**********************************************
     * Events buttons
     */
    //events btns
    private void eventBtn() {
        btnSelectPer.setOnClickListener(this);
        btnSelectVib.setOnClickListener(this);
        btnSelectTotalPer.setOnClickListener(this);
        btnSelectTotalVib.setOnClickListener(this);
        // btnLockOp.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSr1.setOnClickListener(this);
        btnSr2.setOnClickListener(this);
        btnSr3.setOnClickListener(this);
        btnSr4.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        ivBle.setOnClickListener(this);
        //button lock will activate if after 3 seconds pressed
        btnLockOp.setOnClickListener(this);
        //btnLockOp.setOnLongClickListener(this);
        btnLockOp.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v) {
        if (btnStart == v) {
            if (!isAlarm) {
                if (!isLockScreen) {
                    if (memoryTransdA == -1 && memoryTransdB == -1) {
                        flagIsTRA = false;
                        flagIsTRB = false;
                    } else {
                        flagIsTRA = true;
                        flagIsTRB = true;
                    }
                    condStartTherapy(flagIsFreq, flagIsInt, flagIsTim, (flagIsTRA || flagIsTRB), (flagIsTRA || flagIsTRB), isFlagIsSr, isFlagMcuReady);
                }
            } else {
                Log.d(TAG, "onClick: alarm enable");
            }
            //beep.beep_key();
        } else if (btnStop == v) {
            stopTherapy();
        } else if (btnMenu == v) {
            if (!isLockScreen) {
                goHome();
            }
        } else if (ivBle == v) {
            Log.d(TAG, "onClick: ble manual connection");
            if (!isFlagSetConnection) {
                setConnetion();
            }
        } else if (btnSelectPer == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    if (!isLockScreen) {
                        mode = selectMode(status.SELECT_MODE_PERCUSSION);
                        Log.d(TAG, "onClick: mode percussion");
                        sendSpPercussion();
                    }
                }
            }
        } else if (btnSelectVib == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    if (!isLockScreen) {
                        mode = selectMode(status.SELECT_MODE_VIBRATION);
                        Log.d(TAG, "onClick: mode vibration");
                        sendSpVibration();
                    }
                }
            }
        } else if (btnSelectTotalPer == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    if (!isLockScreen) {
                        sendSpTotalPercussion();
                        //mode = selectMode(status.SELECT_MODE_TOTAL_PERCUSSION);
                    }
                }
            }
        } else if (btnSelectTotalVib == v) {
            if (isTherapyOn == false) {
                Log.d(TAG, "onClick: isLockMode" + isLockMode);
                if (isLockMode == false) {
                    if (!isLockScreen) {
                        sendSpTotalVibration();
                        //mode = selectMode(status.SELECT_MODE_TOTAL_VIBRATION);
                    }
                }
            }
        }

    }

    //long touch events
    @Override
    public boolean onLongClick(View v) {

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler = new Handler();
                handler.postDelayed(runDelayButton, 3000);
                Log.d(TAG, "onTouch: down");
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(runDelayButton);
                Log.d(TAG, "onTouch: remove");
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: key:" + keyCode + " event:" + event);
       /* if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "onKeyDown: KEYCODE_BACK");
            //super.onKeyDown(keyCode, event);
            return true;
        }*/
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.d(TAG, "onKeyDown: KEYCODE_HOME");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //delay of 3 sec to lock or unlock screen
    Runnable runDelayButton = new Runnable() {
        @Override
        public void run() {
            //Toast.makeText(MainActivity.this, "delayed msg", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "run: runDelayButton");
            lockScreen(true);
        }
    };

    /**********************************************
     * Events from adapters-Get all setpoints
     *
     */
    //send data of frequency
    private void sendSpFreq(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendTCP(spEth.k9_fr_1);
                    break;
                case 1:
                    sendTCP(spEth.k9_fr_2);
                    break;
                case 2:
                    sendTCP(spEth.k9_fr_3);
                    break;
                case 3:
                    sendTCP(spEth.k9_fr_4);
                    break;
                case 4:
                    sendTCP(spEth.k9_fr_5);//clean all freq
                    break;
                case 5:
                    //sendTCP(spEth.k9_fr_6);//max frecuency
                    initTCP_IP();
                    break;
            }
        }

    }

    //send data of Int
    private void sendSpInt(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendTCP(spEth.k9_in_1);
                    break;
                case 1:
                    sendTCP(spEth.k9_in_2);
                    break;
                case 2:
                    sendTCP(spEth.k9_in_3);
                    break;
                case 3:
                    sendTCP(spEth.k9_in_4);
                    break;
                case 4:
                    sendTCP(spEth.k9_in_5);
                    break;
                case 5:
                    //max
                    //sendTCP(spEth.k9_in_7);
                    disconnectTCP();

                    break;
            }
        }
    }

    //send data of Time
    private void sendSpTime(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendTCP(spEth.k9_tm_1);
                    break;
                case 1:
                    sendTCP(spEth.k9_tm_2);
                    break;
                case 2:
                    sendTCP(spEth.k9_tm_3);
                    break;
                case 3:
                    sendTCP(spEth.k9_tm_4);
                    break;
                case 4:
                    sendTCP(spEth.k9_tm_5);
                    break;
            }
        }
    }

    //send data of RBA
    private void sendSpRBA(int pos, boolean enable) {
        if (!enable) {
            switch (pos) {
                case 0:
                    sendTCP(spEth.k9_ta_1);
                    break;
                case 1:
                    sendTCP(spEth.k9_ta_2);
                    break;
                case 2:
                    sendTCP(spEth.k9_ta_3);
                    break;
                case 3:
                    sendTCP(spEth.k9_ta_4);
                    break;
                case 4:
                    sendTCP(spEth.k9_ta_5);
                    break;
                case 5:
                    sendTCP(spEth.k9_ta_6);//clean
                    break;
            }
        }

    }

    //send data of RBB
    private void sendSpRBB(int pos, boolean enable) {
        if (!enable) {

            switch (pos) {
                case 0:
                    sendTCP(spEth.k9_tb_1);
                    break;
                case 1:
                    sendTCP(spEth.k9_tb_2);
                    break;
                case 2:
                    sendTCP(spEth.k9_tb_3);
                    break;
                case 3:
                    sendTCP(spEth.k9_tb_4);
                    break;
                case 4:
                    sendTCP(spEth.k9_tb_5);
                    break;
                case 5:
                    Log.d(TAG, "sendSpRBB: spEth.k9_tb_6");
                    sendTCP(spEth.k9_tb_6);//clean
                    break;
            }
        }
    }

    //send data of command
    private void sendSpCommand(int pos, boolean enable) {
        switch (pos) {
            case 0:

                break;
            case 1:
                if (!enable) {
                    sendTCP(spEth.k9_op_2);//start
                }
                break;
            case 2:
                if (enable) {
                    sendTCP(spEth.k9_op_3);//stop
                }
                break;
        }
    }

    //send cooing
    private void sendSpCooling(int pos, boolean enable) {
        if (!enable) {
            //sendCmdBle(concatDataWriteBle.concatDataToWrite(bleProtocol.COOLING, pos));
            Log.d(TAG, "sendSpCooling: value:" + pos);

        }
    }

    //send mode percussion
    private void sendSpPercussion() {
        sendTCP(spEth.k9_md_1);//mode percussion
    }

    //send mode vibration
    private void sendSpVibration() {
        sendTCP(spEth.k9_md_2);//mode vibration
    }


    //send mode Total percussion
    private void sendSpTotalPercussion() {
        sendTCP(spEth.k9_md_3);//mode full percussion
    }

    //send mode Total percussion
    private void sendSpTotalVibration() {
        sendTCP(spEth.k9_md_4);//mode total vibration
    }


    /**********************************************
     * Timer therapy
     */

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
                if (value == setPoints.INT_BLE_SP_TIME_NONE) {
                    intTime = mTagReference.SELECTED_INT_TIME_0;
                } else if (value == setPoints.INT_BLE_SP_TIME1) {
                    intTime = mTagReference.SELECTED_INT_TIME_1;
                } else if (value == setPoints.INT_BLE_SP_TIME2) {
                    intTime = mTagReference.SELECTED_INT_TIME_2;
                } else if (value == setPoints.INT_BLE_SP_TIME3) {
                    intTime = mTagReference.SELECTED_INT_TIME_3;
                } else if (value == setPoints.INT_BLE_SP_TIME4) {
                    intTime = mTagReference.SELECTED_INT_TIME_4;
                } else if (value == setPoints.INT_BLE_SP_TIME5) {
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

    /**********************************************
     * storage data
     */
    //save data
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

    /**********************************************
     * Notifications
     */
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

    //notification under current
    private void notificationUnderCurrent() {
        if (counterUc > 3) {
            if (!isFlagAlarmUc) {
                k9Alert.alertDialogUnderCurrent(dialogUnderCurrent, dialogConfirmLang);
                isFlagAlarmUc = true;
                beepAlarm();
                counterUc = 0;
            }
        } else {
            counterUc++;
        }


    }

    //notification overCurrent
    private void notificationOverCurrent() {

        if (counterOc > 3) {
            if (!isFlagAlarmOc) {
                k9Alert.alertDialogOverCurrent(dialogOverCurrent, dialogConfirmLang);
                isFlagAlarmOc = true;
                beepAlarm();
            }
        } else {
            counterOc++;
        }
    }

    //clean all counter andflas
    private void cleanFlagsOcUc() {
        counterOc = 0;
        counterUc = 0;
    }

    //notification alarmDummy
    private void notificationAlarmDummy() {

    }


    /**********************************************
     * Alert dialog
     */
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


    /**********************************************
     * watch dog
     */

    private void reloadRequestStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestStatusFromHostTimer();
                Log.d(TAG, "reloadRequestStatus: ");
                //rest counter watchdog
                countWatchDog = MAXWatchDog;
            }
        });

    }

    //cancel request status
    private void cancelRequestStatus() {
        if (loopGetStatusTimer != null) {
            loopGetStatusTimer.cancel();
            loopGetStatusTimer = null;
        }
        //clean timer watchdog
        if (watchDogTimer != null) {
            watchDogTimer.cancel();
            watchDogTimer = null;
        }
    }

    //*request status from host
    private void requestStatusFromHostTimer() {
        if (loopGetStatusTimer != null) {
            loopGetStatusTimer.cancel();
            loopGetStatusTimer = null;
        }

        //clean timer watchdog
        if (watchDogTimer != null) {
            watchDogTimer.cancel();
            watchDogTimer = null;
        }


        loopGetStatusTimer = new CountDownTimer(TIMER_LOOP_STATUS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: request status:" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                setWatchDogCounter();
            }
        }.start();
    }

    //
    private void setWatchDogCounter() {
        Log.d(TAG, "setWatchDogCounter: ");
        //timer delay
        watchDogTimerCom();
        //request status
        sendTCP(spEth.k9_st_1);
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
                reconnectTCP_IP();//just for testing
                Log.d(TAG, "watchDogTimerCom onFinish: Reconnecting tcp ip!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                //watchDogTimerElapsed();//working
            }
        }.start();
    }

    //watchdog
    private void watchDogTimerElapsed() {
        Log.d(TAG, "watchDogTimerElapsed: ");

        //private int countWatchDog=0;
        //private int MAXWatchDog=3;
        if (countWatchDog == 0) {
            //alarm watch dog
            isTherapyOn = false;
            cleanDisplayTimer();
            stopByEmergency();
            notificationHardwareFail();
        } else {
            countWatchDog--;
            setWatchDogCounter();
        }
    }

    /**********************************************
     * Ethernet
     */

    //Select characteristics to send command to the host
    private boolean sendCmdNetwork(int command) {
        //AsyncTaskExample asyncTask = new AsyncTaskExample();
        //AsyncTaskSend asyncTaskSend = new AsyncTaskSend();
        boolean ret = false;
        if (command != 0) {
           /* Log.d(TAG, "sendCmdBle: " + command);
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
            }*/
        }
        return ret;
    }

    //init socket TCp
    private void initTCP_IP() {
        client = new TcpClient("192.168.6.203", 1200);
        client.addObserver(this);
        //establish connection
        connectionTCP();

    }

    //reconnect with device
    private void reconnectTCP_IP() {

        try { //new

            Log.d(TAG, "reconnectTCP_IP: do nothing ");
        } catch (Exception e) {
            Log.d(TAG, "sendTCP: ex:" + e.getMessage());
        }

    }

    //send message using tcp ip
    private void sendTCP(String message) {
        if (message != null) {


            try { //new

                client.sendMessage(message);
                Log.d(TAG, "eth connection status:send :" + message);
                isFlagMcuReady = false;
               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });*/
            } catch (Exception e) {
                Log.d(TAG, "sendTCP: ex:" + e.getMessage());
            }

        }
    }

    //establish connection tcp
    private void connectionTCP() {
        try {
            if (true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnectedTCP = true;
                        client.connect();
                        Log.d(TAG, "eth connection status: connectedTCP");
                    }
                });
            }
        } catch (Exception e) {
            Log.d(TAG, "connectionTCP: ex:" + e.getMessage());
        }
    }

    //disconnect connection tcp
    private void disconnectTCP() {
        try {
            if (true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "eth connection status: disconnectTCP");
                        isConnectedTCP = false;
                        client.disconnect();

                    }
                });
            }


        } catch (Exception e) {
            Log.d(TAG, "connectionTCP: ex:" + e.getMessage());
        }
    }

    //get status from ethernet
    private void feedbackFromEthernet(String message) {
        MessageEth messageEth = new MessageEth();
        Log.d(TAG, "statusEthernet:" + message);


        /*
         * Payload example:
         * receive:k9_op_##_##
         *
         * |123456|78| |1011|
         * |k9_op_|##|_##
         *
         * */

        if (message != null) {
            if (message.length() < 6) {
                return;
            }
            //filter the message with the len
            int startIndex = 0;//0
            int endIndex = 6;//0
            //substring for the message received. Know from where
            String substrPayload = message.substring(startIndex, endIndex);
            Log.d(TAG, "statusEthernet:extracted payload ref: " + substrPayload);

            /*get first code */
            //substring to get number
            int startIndexPos = 6;
            int endIndexPos = 8;
            String substrPos = message.substring(startIndexPos, endIndexPos);
            Log.d(TAG, "statusEthernet:extracted payload pos:" + substrPos);
            //check if substring is number
            if (!substrPos.matches("\\d+(?:\\.\\d+)?")) {
                return;
            }

            int myNum = Integer.parseInt(substrPos);
            Log.d(TAG, "statusEthernet: number:" + myNum);

            /*get second code */
            //substring to get number
            int startIndexCode = 9;
            int endIndexCode = 11;
            String substrCode = message.substring(startIndexCode, endIndexCode);
            Log.d(TAG, "statusEthernet:extracted payload code:" + substrCode);
            //check if substring is number
            if (!substrCode.matches("\\d+(?:\\.\\d+)?")) {
                return;
            }

            int myCode = Integer.parseInt(substrCode);
            Log.d(TAG, "statusEthernet: myCode:" + myCode);

            updateFbStatus(myNum, myCode);
            //
            reloadRequestStatus();


            try {
                if (substrPayload.contains(messageEth.PAYLOAD_ETH_CON)) {
                    //finish load dialog
                    Log.d(TAG, "statusEthernet: PAYLOAD_CONNECTED_ETH");

                    //update icon
                    if (!isFlagSetConnection) {
                        Log.d(TAG, "statusEthernet: set connection");
                        //enable element on the gui
                        setConnetion();
                        //beep top acknowledge is connected
                        beep.beep_key();
                        //Dismisses the load dialog
                        systemConnected();
                        return;
                    }
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_FRQ)) {//freq
                    isFlagMcuReady = true;
                    modelBtnFreqArrayList.clear();
                    updateFbFreq(myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_INT)) {//int
                    isFlagMcuReady = true;
                    modelBtnIntArrayList.clear();
                    updateFbInt(myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_TIM)) {//tim
                    isFlagMcuReady = true;
                    modelBtnTimeArrayList.clear();
                    updateFbTime(myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_TA)) {//Transd-A
                    isFlagMcuReady = true;
                    resetCheckBockA();
                    updateFbRBA(mode, myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_TB)) {//Transd-B
                    isFlagMcuReady = true;
                    resetCheckBockB();

                    updateFbRBb(mode, myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_MD)) {//mode
                    isFlagMcuReady = true;
                    Log.d(TAG, "statusEthernet: num:"  + myNum);
                    updateFbModes(myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_OP)) {//operations
                    updateFbCommands(myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_ST)) {//status from host
                    Log.d(TAG, "feedbackFromEthernet:oldCurrentModule " + oldCurrentModule);
                    Log.d(TAG, "feedbackFromEthernet:myCode " + myCode);

                    //enable MCU
                    if (!isFlagMcuReady) {
                        if (myNum == 91) {
                            Log.d(TAG, "feedbackFromEthernet: enable star from mcu");
                            isFlagMcuReady = true;
                        }
                    }


                    if (oldCurrentModule != myCode && oldCurrentModule != 99) {
                        Log.d(TAG, "feedbackFromEthernet: " + myCode);
                        if (myCode == messageEth.CURRENT_MODULE_A1B1) {
                            oldCurrentModule = myCode;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    controlIconTransdA(controlGUI.POS1);
                                    controlIconTransdB(controlGUI.POS1);
                                }
                            });

                            return;
                        } else if (myCode == messageEth.CURRENT_MODULE_A2B2) {
                            oldCurrentModule = myCode;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    controlIconTransdA(controlGUI.POS2);
                                    controlIconTransdB(controlGUI.POS2);
                                }
                            });

                            return;
                        } else if (myCode == messageEth.CURRENT_MODULE_A3B3) {
                            oldCurrentModule = myCode;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    controlIconTransdA(controlGUI.POS3);
                                    controlIconTransdB(controlGUI.POS3);
                                }
                            });

                            return;
                        } else if (myCode == messageEth.CURRENT_MODULE_A4B4) {
                            oldCurrentModule = myCode;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    controlIconTransdA(controlGUI.POS4);
                                    controlIconTransdB(controlGUI.POS4);
                                }
                            });

                            return;
                        } else if (myCode == messageEth.CURRENT_MODULE_A5B5) {
                            oldCurrentModule = myCode;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    controlIconTransdA(controlGUI.POS5);
                                    controlIconTransdB(controlGUI.POS5);
                                }
                            });

                            return;
                        }
                    }


                    //updateFbStatus(myNum, myCode);
                    //
                    //reloadRequestStatus();
                    return;
                }

                isAlarm = false;
            } catch (Exception e) {
                Log.d(TAG, "statusEthernet: ex:" + e.getMessage());
            }
        }
    }

    //get status from ethernet
    private int feedbackFromEthernetStatus(int myCode) {
        MessageEth messageEth = new MessageEth();

        if (myCode == messageEth.CURRENT_MODULE_A1B1) {
            oldCurrentModule = myCode;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controlIconTransdA(controlGUI.POS1);
                    controlIconTransdB(controlGUI.POS1);
                }
            });

            return myCode;
        } else if (myCode == messageEth.CURRENT_MODULE_A2B2) {
            oldCurrentModule = myCode;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controlIconTransdA(controlGUI.POS2);
                    controlIconTransdB(controlGUI.POS2);
                }
            });

            return myCode;
        } else if (myCode == messageEth.CURRENT_MODULE_A3B3) {
            oldCurrentModule = myCode;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controlIconTransdA(controlGUI.POS3);
                    controlIconTransdB(controlGUI.POS3);
                }
            });

            return myCode;
        } else if (myCode == messageEth.CURRENT_MODULE_A4B4) {
            oldCurrentModule = myCode;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controlIconTransdA(controlGUI.POS4);
                    controlIconTransdB(controlGUI.POS4);
                }
            });

            return myCode;
        } else if (myCode == messageEth.CURRENT_MODULE_A5B5) {
            oldCurrentModule = myCode;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controlIconTransdA(controlGUI.POS5);
                    controlIconTransdB(controlGUI.POS5);
                }
            });

            return myCode;
        }
        return myCode;

    }


    /*update from tcp ip-Data from the communication*/
    @Override
    public void update(Observable o, Object arg) {
        TcpEvent event = (TcpEvent) arg;

        switch (event.getTcpEventType()) {
            case MESSAGE_RECEIVED:
                //Do something
                Log.d(TAG, "update: MESSAGE_RECEIVED new");
                Log.d(TAG, "update: payload:" + event.getPayload());
                feedbackFromEthernet(event.getPayload().toString());

                break;
            case CONNECTION_ESTABLISHED:
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "update:CONNECTION_ESTABLISHED new");
                        //Update ui
                        client.sendMessage("Ready");
                    }
                });
                break;
            case MESSAGE_SENT:
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "update: MESSAGE_SENT");
                    }
                });
                break;
            case DISCONNECTED:
                Log.d(TAG, "update: DISCONNECTED");
                break;
            case MESSAGE_FLUSH:
                Log.d(TAG, "update: FLUSH");
                break;
        }
    }

    /**********************************************
     * Alarms
     */


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
                //stopTimerEnableStart();
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


    /**********************************************
     *screen orientation
     */
    //set orientation
    private void setOrientationLandscape() {
        try {
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } catch (Exception e) {
            Log.d(TAG, "setOrientationLandscape: ex:" + e.getMessage());
        }
    }

    /**********************************************
     *
     */

    /**********************************************
     *
     */

    /**********************************************
     *
     */


}
