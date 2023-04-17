package com.example.k9_pxz;

import static Util.LogUtils.LOGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.widget.Button;

//


//
/*
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;*/


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;


import Adapter.RecyclerViewAdapBurn;
import Alert.CustomAlert;
import Alert.K9Alert;
import Configuration.Config;
import Eth.MessageEth;
import Eth.TcpClient;
import Eth.TcpEvent;
import Interface.InterfaceSetupInfo;
import Interface.RecyclerViewClickInterface;
import Model.ModelBurn;
import Permission.PermissionsChecker;
import Setpoints.SpEth;
import Util.Beep;
import Util.ControlGUI;
import Util.ConvertTimeMin;
import Util.Default_values;
import Util.DisplayOperations;
import Util.Key_Util;
import Util.LocaleHelper;
import Util.Modes;
import Util.SetPoints;
import Util.TagRefrence;
import Util.Util_Burn;
import Util.Util_timer;

public class BurningActivityRutine extends AppCompatActivity implements InterfaceSetupInfo, RecyclerViewClickInterface, View.OnClickListener, Observer {
    private static final String TAG = "BurningActivityRutine";
    private static final int MY_PERMISSIONS_REQUEST = 1;

    /*
     *
     * https://www.youtube.com/watch?v=gpH4Zr1ffnU*/
    //GUI
    private Button btnBurnStart,btnBurnSerial;
    private Button btnBurnStop;
    private Button btnBurnClean;
    private Button btnBurnReport;
    private Button btnHome;
    private TextView tvCurrent, tvStartTime, tvStopTime, tvElapsedTime, tvUpdateFb, tvOperation, tvElapsedCycle;
    private TextView tvCon;
    //Language
    private String language = "en";
    private Resources resources;
    //private String
    private String BLE_ADD_GOT = "0";
    private String SERIAL_NUMBER = "0";
    CustomAlert customAlert;

    public String DATA_BLE_ADD = "DATA_BLE_ADD";
    public String DATA_SYSTEM_SERIAL = "DATA_SYSTEM_SERIAL";


    private Util_Burn utilBurn = new Util_Burn();

    //RecyclerView
    private RecyclerView recyclerViewBurn;
    //Adapter
    private RecyclerViewAdapBurn viewAdapBurn = new RecyclerViewAdapBurn();
    //arraylist-Recycler View Adapter
    private ArrayList<ModelBurn> modelBurns = new ArrayList<>();
    private Default_values default_values = new Default_values();
    private Key_Util keyUtil = new Key_Util();
    private Config config = new Config();
    private SetPoints setPoints = new SetPoints();
    SpEth spEth = new SpEth();//ethernet setpoint
    private Beep beep = new Beep();
    private DisplayOperations displayOperations = new DisplayOperations();
    private TagRefrence mTagReference = new TagRefrence();
    private ControlGUI controlGUI = new ControlGUI();
    private Util_timer utilTimer = new Util_timer();
    BurnSequence burnSequence = new BurnSequence();
    private CountDownTimer timerTherapy = null;
    private boolean isEnableGui = false;
    private K9Alert k9Alert;
    //TCP ip
    private TcpClient client;
    //ethernet TCp
    private boolean isConnectedTCP = false;
    private boolean flagIsTim = false;
    private boolean isFlagMcuReady = false;
    Context mContext;
    PermissionsChecker checker;
    //alarm
    private boolean isAlarm = true;
    private long TIMER_WACHT_DOG = 3000;
    private long TIMER_LOOP_STATUS = 3000;//
    //watchDog
    private CountDownTimer watchDogTimer;
    private CountDownTimer loopGetStatusTimer;
    private int countWatchDog = 0;
    private int MAXWatchDog = 3;
    private boolean isFlagSetConnection = false;

    //system ready
    private boolean isReadyOperate = true;

    //mute
    private boolean isMuteOn = false;

    //load configuration
    private int positionSample = 0;
    private boolean conditionsSample = true;
    private int iSample;

    //save setpoints
    private int SP_FREQ = 4;//35hz
    private int SP_INT = 4;//90%
    private int SP_TIM = 0;//4//25min
    private int SP_TRA = 1;
    private int SP_TRB = 1;
    private int SP_MODE = 1;


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
    private String dialogAutoSleep = "0";
    private String dialogSoppingWait = "0";
    private String dialogSoppingLoading = "0";
    //
    private boolean isTherapyOn = false;
    private boolean isLockScreen = false;
    private Boolean flagIsMinZero = false;
    //
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
    private boolean isFlagTimerElapsed = false;
    private int memoryLastTimer = 0;

    private int currentCycle = 0;//move cycles
    private int currentIndex = 0;//index to print

    private int timerBurn=0;

    String dest;

    String mPath = "0";

    //hide navigation bar
    private int currentApiVersion;
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLayout(R.layout.activity_burning_rutine);//load layout
        hideNavigationBar(); //remove menu bar

        initGUI();//init gui
        initOther(); //init Ethernet
        initLang();//init language
        initApp();//init App
        displayIconsButtons();//display icons on the buttons
        //
        requestStatusFromHostTimer(); //request every 3 sec status of the host
        eventsBtn();//events
        //load burn sequence
        loadSequences(currentCycle);
        //display time init burn process

        displayTimeElapsed("00:00");
        displayTimeElapsed("00:00:00");
    }

    /**********************************************
     * Operations:connection
     */
    //inject data to the model scan
    private ModelBurn injectDataModelBurn(String modName, String modFreq, String modInt, String modTime, String modTransA, String modTransB, String modStatus, String modStartCycles, String modEndCycles) {
        ModelBurn modelBurn = new ModelBurn();
        modelBurn.setModName(modName);
        //parameters
        modelBurn.setModFreq(modFreq);
        modelBurn.setModInt(modInt);
        modelBurn.setModTime(modTime);
        //transd
        modelBurn.setModTransdA(modTransA);
        modelBurn.setModTransdB(modTransB);
        //status
        modelBurn.setModStatus(modStatus);
        modelBurn.setModStart(modStartCycles);
        modelBurn.setModEnd(modEndCycles);
        //Log.d(TAG, "injectDataModelScan:name: " + modelScan.getDeviceName() + ".UUID:" + modelScan.getDevUIID());
        return modelBurn;
    }

    //update Recycler view
    private void updateRecyclerViewBurn() {
        recyclerViewBurn = findViewById(R.id.recyclerViewBurn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewBurn.setLayoutManager(linearLayoutManager);
        recyclerViewBurn.setHasFixedSize(true);
        viewAdapBurn = new RecyclerViewAdapBurn(this, modelBurns);//modelDevicesArrayList
        recyclerViewBurn.setAdapter(viewAdapBurn);
    }

    //update GUI adapter Burn
    private boolean updateGuiRecyclerViewBurn(String modName, String modFreq, String modInt, String modTime, String modTransA, String modTransB, String modStatus, String modStartCycles, String modEndCycles) {
        if (modelBurns != null) {
            //modelScan.clear();
            Log.d(TAG, "updateGuiRecyclerViewMainDev: ");
            //modelScan.clear();
            modelBurns.add(injectDataModelBurn(modName, modFreq, modInt, modTime, modTransA, modTransB, modStatus, modStartCycles, modEndCycles));
            Log.d(TAG, "updateGuiRecyclerViewBurn: index" + modName);
            return true;
        }
        return false;
    }

    //update GUI adapter Burn
    private boolean updateGuiRecyclerViewBurnIndex(int index, String modName, String modFreq, String modInt, String modTime, String modTransA, String modTransB, String modStatus, String modStartCycles, String modEndCycles) {
        if (modelBurns != null) {
            //modelScan.clear();
            Log.d(TAG, "updateGuiRecyclerViewMainDev: ");
            //modelScan.clear();
            modelBurns.add(index, injectDataModelBurn(modName, modFreq, modInt, modTime, modTransA, modTransB, modStatus, modStartCycles, modEndCycles));
            return true;
        }
        return false;
    }

    //clean model
    private void cleanModel() {
        modelBurns.clear();
    }

    //update position
    private void updateBurnGUI(int module, String modName, String modFreq, String modInt, String modTime, String modCycles, String modStatus) {
        if (modelBurns == null) {
            return;
        }
    }

    //loading first time
    private void firstTimeLoadGUI() {
        for (int i = 1; i < utilBurn.MAX_NUMBER_TRANS + 1; i++) {
            Log.d(TAG, "firstTimeLoadGUI:i: " + i);
            String modName = utilBurn.MOD_DEF_NAME + String.valueOf(i);
            updateBurnGUI(i, modName, utilBurn.MOD_DEF_FREQ, utilBurn.MOD_DEF_INT, utilBurn.MOD_DEF_TIME, utilBurn.MOD_DEF_CYCLES, utilBurn.MOD_DEF_STATUS);
        }
        updateRecyclerViewBurn();
    }

    //load status when stop
    private void updateSequenceStatusStop(int index, String status, String stopCycle) {

        // modelBurns.get(index).getModName();
        int mIndex = 0;
        /*
        if (index > 0) {
            mIndex = index - 1;
        } else {
            mIndex = 0;
        }*/
        mIndex = modelBurns.size() - 1;
        Log.d(TAG, "updateSequenceStatusStop: mIndex: " + mIndex);
        Log.d(TAG, "updateSequenceStatusStop:size: " + modelBurns.size());

        //parameters
        modelBurns.get(mIndex).getModFreq();
        modelBurns.get(mIndex).getModInt();
        modelBurns.get(mIndex).getModTime();
        //trans
        modelBurns.get(mIndex).getModTransdA();
        modelBurns.get(mIndex).getModTransdB();
        //cycle
        modelBurns.get(mIndex).getModStart();

        updateGuiRecyclerViewBurn(String.valueOf(mIndex), modelBurns.get(mIndex).getModFreq(), modelBurns.get(mIndex).getModInt(), modelBurns.get(mIndex).getModTime(), modelBurns.get(mIndex).getModTransdA(), modelBurns.get(mIndex).getModTransdB(), burnSequence.SEQ_STATUS_STATUS + status, burnSequence.SEQ_STATUS_START + modelBurns.get(mIndex).getModStart(), burnSequence.SEQ_STATUS_END + stopCycle);
        updateRecyclerViewBurn();
    }

    //load status when start
    private void updateSequenceStatusStart(int index, String status, String cycleStart) {

        // modelBurns.get(index).getModName();
        int mIndex = 0;
        /*
        if (index > 0) {
            mIndex = index - 1;
        } else {
            mIndex = 0;
        }*/
        mIndex = modelBurns.size() - 1;
        Log.d(TAG, "updateSequenceStatusStop: mIndex: " + mIndex);
        Log.d(TAG, "updateSequenceStatusStop:size: " + modelBurns.size());

        //parameters
        modelBurns.get(mIndex).getModFreq();
        modelBurns.get(mIndex).getModInt();
        modelBurns.get(mIndex).getModTime();
        //trans
        modelBurns.get(mIndex).getModTransdA();
        modelBurns.get(mIndex).getModTransdB();
        //cycle
        //modelBurns.get(mIndex).getModStart();

        updateGuiRecyclerViewBurn(String.valueOf(mIndex), modelBurns.get(mIndex).getModFreq(), modelBurns.get(mIndex).getModInt(), modelBurns.get(mIndex).getModTime(), modelBurns.get(mIndex).getModTransdA(), modelBurns.get(mIndex).getModTransdB(), burnSequence.SEQ_STATUS_STATUS + status, burnSequence.SEQ_STATUS_START + cycleStart, burnSequence.SEQ_STATUS_END + "-");
        updateRecyclerViewBurn();
    }

    /**********************************************
     *Init
     */

    //init GUI
    private void initGUI() {
        btnBurnSerial= findViewById(R.id.btnBurnSerial);
        btnBurnStart = findViewById(R.id.btnBurnStart);
        btnBurnStop = findViewById(R.id.btnBurnStop);
        btnBurnClean = findViewById(R.id.btnBurnClean);
        btnBurnReport = findViewById(R.id.btnBurnReport);
        btnHome = findViewById(R.id.btnHome);
        tvCurrent = findViewById(R.id.tvCurrentBurning);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvStopTime = findViewById(R.id.tvStopTime);
        tvElapsedTime = findViewById(R.id.tvElapsedTime);
        tvCon = findViewById(R.id.tvConBurn);
        tvUpdateFb = findViewById(R.id.tvUpdateFb);
        tvOperation = findViewById(R.id.tvOperation);
        tvElapsedCycle = findViewById(R.id.tvElapsedCycle);
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
        initTCP_IP();
    }

    //init socket TCP
    private void initTCP_IP() {
        client = new TcpClient("192.168.6.203", 1200);
        client.addObserver(this);
        //establish connection
        connectionTCP();
    }

    //init array spinner language
    private void initLang() {
        Log.d(TAG, "initLang: language:" + language);
        //Context context = LocaleHelper.setLocale(VibrationPercussionActivity.this, language);
        //resources = context.getResources();
        loadContentByLanguage(getResourcesLanguage(language));

    }

    //init system
    private boolean initApp() {
        k9Alert = new K9Alert(this, this);
        isAlarm = false;//just for test
        return true;
    }

    /**********************************************
     *Events
     */
    //events buttons
    private void eventsBtn() {
        btnBurnStart.setOnClickListener(this);
        btnBurnStop.setOnClickListener(this);
        btnBurnClean.setOnClickListener(this);
        btnBurnReport.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnBurnSerial.setOnClickListener(this);
    }

    @Override
    public void onItemPostSelect(int position, String value) {

    }

    @Override
    public void onClick(View v) {
        if (v == btnBurnStart) {
            startTherapy();
        } else if (v == btnBurnStop) {
            stopTherapy();
        } else if (v == btnBurnClean) {
            if (!isTherapyOn) {
                operationClean();
            }
        } else if (v == btnBurnReport) {
            //createPDF();
            if (!isTherapyOn) {
                operationPrint();
            }
        } else if (v == btnHome) {
            goHome();
        }else if (v == btnBurnSerial) {
            operationSerialNumber();
        }
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

    //go to home
    private void goHome() {
        Bundle bundle = new Bundle();
        Log.d(TAG, "onClick: get address " + BLE_ADD_GOT);
        bundle.putString(DATA_BLE_ADD, BLE_ADD_GOT);//
        bundle.putString(DATA_SYSTEM_SERIAL, SERIAL_NUMBER);
        Intent intent = new Intent(BurningActivityRutine.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // saveToExcel.writeToExcel("machines","names","reasons","treatments","times");

                //saveToExcel.writeToExcel(name,sex,phone,address);
                Log.d(TAG, "onRequestPermissionsResult: granted");
                Toast.makeText(BurningActivityRutine.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            } else {

                // Permission Denied
                Log.d(TAG, "onRequestPermissionsResult: denied");
                Toast.makeText(BurningActivityRutine.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    //write to exxcel
    public static String getExcelDir() {

        // SD
        String sdcardPath = Environment.getExternalStorageDirectory()
                .toString();
        File dir = new File(sdcardPath + File.separator + "Excel"
                + File.separator + "Person");
        //File dir = new File(sdcardPath + File.separator + "Excel");
        if (!dir.exists()) {

            return dir.toString();
        } else {

            dir.mkdirs();
            Log.e("BAG", "保存路径不存在,");
            return dir.toString();
        }
    }

    //get uuid
    public String createTransactionID() throws Exception {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
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
        if (!isMuteOn) {
            beep.beep_key();
        }
    }

    /**********************************************
     * Language
     */
    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(BurningActivityRutine.this, language);
        resources = context.getResources();
        return resources;
    }

    //load all the text according to the language
    private void loadContentByLanguage(Resources resources) {
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
        dialogAutoSleep = resources.getString(R.string.string_sleep_question);
        dialogSoppingWait = resources.getString(R.string.string_text_dial_stoping);
        dialogSoppingLoading = resources.getString(R.string.string_text_dial_loading);
        //check if percussion or Percussion/Vibration
    }

    //get current time
    private String getCurrentTime() {
        String date = "DD:MM:YYYY, HH:mm:ss";
        try {
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
            date = df.format(Calendar.getInstance().getTime());
            if (date != null) {
                return date;
            }
        } catch (Exception e) {
            Log.d(TAG, "getCurrentTime: " + e.getMessage());
        }
        return date = "DD:MM:YYYY, HH:mm:ss";
    }

    /**********************************************
     * Display
     */
    //dispay date
    private String displayDate() {
        String date = "DD:MM:YYYY, HH:mm:ss";
        try {
            if (tvUpdateFb != null) {
                // @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                // date = df.format(Calendar.getInstance().getTime());
                if (date != null) {
                    tvUpdateFb.setText(getCurrentTime());
                    return date;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "displayDate: ex:" + e.getMessage());
        }
        return date;
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
                if (btnBurnStart != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnBurnStart.setText(input);
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
                tvElapsedCycle.setText(burnSequence.SEQ_STATUS_CURRENT_CYCLE+time);
            } catch (Exception e) {
                Log.d(TAG, "displayTimerMinSec: ex" + e.getMessage());
            }
        }
    }

    //display icons buttons
    private void displayIconsButtons() {
        btnBurnSerial.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_qr_code_24));
        btnBurnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
        btnBurnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_cancel_24));
        btnBurnClean.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_delete_24));
        btnBurnReport.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_print_24));
    }

    //display start burn
    private void displayBurnProcessStart(String time) {
        try {
            tvStartTime.setText(burnSequence.SEQ_STATUS_START + time);
            Log.d(TAG, "displayBurnProcessStart: "+time);
        } catch (Exception e) {
            Log.d(TAG, "displayBurnProcessStart: ex:" + e.getMessage());
        }
    }

    //display end burn
    private void displayBurnProcessEnd(String time) {
        try {
            tvStopTime.setText(burnSequence.SEQ_STATUS_END + time);
        } catch (Exception e) {
            Log.d(TAG, "displayBurnProcessStop: ex:" + e.getMessage());
        }
    }

    //display time elapsed
    private void displayTimeElapsed(String time){
        try {
            tvElapsedTime.setText(burnSequence.SEQ_STATUS_CURRENT_BURN + time);
        } catch (Exception e) {
            Log.d(TAG, "displayBurntvElapsedTime: ex:" + e.getMessage());
        }
    }

    /**********************************************
     * Load layout
     */
    //load layout
    private void loadLayout(int layout) {
        try {
            //setContentView(R.layout.activity_main);
            setContentView(layout);
        } catch (Exception e) {
            Log.d(TAG, "loadLayout: ex:" + e.getMessage());
        }
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

    /**********************************************
     * Ethernet
     */
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

    //system connected
    private boolean systemConnected() {
        // updateSideRail(controlGUI.POS1);

        new Thread() {
            public void run() {
                //alertDialog.dismiss();
            }
        }.start();
        return false;
    }

    /**********************************************
     * Operations:update
     */
    //update feedback status
    private void updateFbStatus(int value, int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "updateFbStatus: " + value);
                //display info on screen
                displayFeedBackStatus(value, code);

                if (value == setPoints.INT_BLE_STATUS_READY) {
                    isReadyOperate = true;
                    // cleanFlagsOcUc();
                } else if (value == setPoints.INT_BLE_STATUS_WORKING) {
                    isReadyOperate = false;
                } else if (value == setPoints.INT_BLE_STATUS_ALARM1) {
                    //notificationAlarmDummy();
                } else if (value == setPoints.INT_BLE_STATUS_ALARM2) {//over current
                    //notificationOverCurrent();
                } else if (value == setPoints.INT_BLE_STATUS_ALARM3) {//under current
                    //notificationUnderCurrent();
                }
                displayDate();
            }
        });
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
                    //
                    Log.d(TAG, "run: currentCycle:" + currentCycle);
                    // updateBtnReady(controlGUI.POS0);
                    launchRunTherapy(valueTimerTherapy, countInterval);
                    //update display with language
                    displayStartCommand(resources.getString(R.string.string_text_btn_running));
                    //
                    currentIndex++;
                    Log.d(TAG, "run:currentIndex: " + currentIndex);
                    // updateSequenceStatusStop(currentIndex, burnSequence.SEQ_STATUS_WORKING, burnSequence.SEQ_STATUS_EMPTY);//UPDATE ON DISPLAY
                    updateSequenceStatusStart(currentIndex, burnSequence.SEQ_STATUS_WORKING, getCurrentTime());

                } else if (ret == setPoints.INT_BLE_CMD_STOP) {
                    displayStartCommand(resources.getString(R.string.string_text_btn_start));
                    //displayStartCommand("Start");
                    if (isFlagTimerElapsed) {
                        notificationTimerElapsed();
                        //burn process end
                        if (modelBurns.get(currentIndex - 1).getModStatus() != burnSequence.SEQ_STATUS_PASS) {
                            currentIndex++;//currentCycle
                            updateSequenceStatusStop(currentCycle, burnSequence.SEQ_STATUS_PASS, getCurrentTime());//UPDATE ON DISPLAY
                        }
                        //
                        nextCycle();
                    } else {
                        forceStopTimerTherapy();
                        //
                        if (modelBurns.get(currentIndex - 1).getModStatus() != burnSequence.SEQ_STATUS_STOPPED) {
                            currentIndex++;
                            updateSequenceStatusStop(currentIndex, burnSequence.SEQ_STATUS_STOPPED, getCurrentTime());//UPDATE ON DISPLAY
                        }
                    }
                }
            }
        });
    }

    //get return from bluetooth and update GUI  commands
    private int updateCommand(int value) {
        if (value > 0) {
            try {
                if (value == setPoints.INT_BLE_CMD_NONE) {
                    // btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //added 10/19/22
                    /*btnBurnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_bk));
                    btnBurnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_square_24_bk));
                    btnBurnClean.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_delete_24));*/
                    return setPoints.INT_BLE_CMD_NONE;
                } else if ((value == setPoints.INT_BLE_CMD_START)) {
                    //btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48));
                    //btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //added 10/19/22
                    /*btnBurnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_bk));
                    btnBurnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_square_24_bk));
                    btnBurnClean.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_delete_24));*/
                    displayOperation(displayOperations.DISPLAY_OPE_RUNNING);//running
                    return setPoints.INT_BLE_CMD_START;
                } else if ((value == setPoints.INT_BLE_CMD_STOP)) {
                    //btnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    // btnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_stop2_48));
                    //added 10/19/22
                    /*btnBurnStart.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_play_arrow_48_bk));
                    btnBurnStop.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_square_24_bk));
                    btnBurnClean.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getDrawable(R.drawable.ic_baseline_delete_24));*/
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

    //update displayInt
    private void updateFbTime(int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                //flagIsTim = updateButtonsTime(value);
                memoryLastTimer = value;
                loadDisplayTimerCountFirstTime(memoryLastTimer);
            }
        });
    }

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
     * Feedback from Ethernet
     */
    //get status from ethernet
    private void feedbackFromEthernet(String message) {
        MessageEth messageEth = new MessageEth();
        Log.d(TAG, "statusEthernet:" + message);

        //sleepMode(isTherapyOn);//auto sleep mode


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
            //
            updateFbStatus(myNum, myCode);//get current time
            //
            reloadRequestStatus();
            //
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
                    /*modelBtnFreqArrayList.clear();
                    updateFbFreq(myNum);*/
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_INT)) {//int
                    isFlagMcuReady = true;
                    /*modelBtnIntArrayList.clear();
                    updateFbInt(myNum);*/
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_TIM)) {//tim
                    isFlagMcuReady = true;
                    /*modelBtnTimeArrayList.clear();*/
                    updateFbTime(myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_TA)) {//Transd-A
                    isFlagMcuReady = true;
                    /*resetCheckBockA();
                    updateFbRBA(mode, myNum);*/
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_TB)) {//Transd-B
                    isFlagMcuReady = true;
                    /*resetCheckBockB();
                    updateFbRBb(mode, myNum);*/
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_MD)) {//mode
                    isFlagMcuReady = true;
                    Log.d(TAG, "statusEthernet: num:" + myNum);
                    /* updateFbModes(myNum);*/
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_OP)) {//operations
                    updateFbCommands(myNum);
                    beepSound();
                    return;
                } else if (substrPayload.contains(messageEth.PAYLOAD_ETH_ST)) {//status from host
                    //Log.d(TAG, "feedbackFromEthernet:oldCurrentModule " + oldCurrentModule);
                    Log.d(TAG, "feedbackFromEthernet:myCode " + myCode);

                    //enable MCU
                    if (!isFlagMcuReady) {
                        if (myNum == 91) {
                            Log.d(TAG, "feedbackFromEthernet: enable star from mcu");
                            isFlagMcuReady = true;
                        }
                    }


                    /*if (oldCurrentModule != myCode && oldCurrentModule != 99) {
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
                    }*/


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

    /**********************************************
     * Enable
     */
    private void enableGui(boolean input) {
        Log.d(TAG, "enableGui: " + input);
        if (input) {
            if (!isEnableGui) {
                //load view
                /*updateButtonsFrequencyF(setPoints.INT_BLE_SP_FREQ_NONE);//frequency
                updateButtonsIntensity(setPoints.INT_BLE_SP_INT_NONE);//intensity
                updateButtonsTime(setPoints.INT_BLE_SP_TIME_NONE);//time*/
                updateCommand(setPoints.INT_BLE_CMD_NONE);
                isEnableGui = true;
            }
        } else {
            //updateButtonsFrequencyF(setPoints.INT_BLE_SP_CLEAN);
            isEnableGui = false;
        }
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

    /**********************************************
     * Get settings
     */
    //get settings
    private void getSettings(int value, int SP_TRA, int SP_TRB) {
        Modes modes = new Modes();
        switch (value) {
            case 0:
                isMuteOn = true;
                iSample++;
                break;
            case 1:
                positionSample = SP_FREQ;
                Log.d(TAG, "run: delay freq:" + SP_FREQ);
                sendSpFreq(positionSample, false);
                iSample++;
                break;
            case 2:
                positionSample = SP_INT;
                Log.d(TAG, "run: delay int:" + SP_INT);
                sendSpInt(positionSample, false);
                iSample++;
                break;
            case 3:
                positionSample = SP_TIM;
                sendSpTime(positionSample, false);
                iSample++;
                break;
            case 4:
                positionSample = SP_MODE;
                sendModes(SP_MODE);
                iSample++;
                break;
            case 5:
                positionSample = SP_TRA;
                Log.d(TAG, "getSettings:SP_TRA " + SP_TRA);
                if (SP_MODE == modes.MODE_PERC || SP_MODE == modes.MODE_VIB) {
                    sendSpRBA(positionSample, false);
                    // flagIsTRA =true;
                }
                iSample++;
                break;
            case 6:
                positionSample = SP_TRB;
                Log.d(TAG, "getSettings:SP_TRB: " + SP_TRB);
                if (SP_MODE == modes.MODE_PERC || SP_MODE == modes.MODE_VIB) {
                    sendSpRBB(positionSample, false);
                    // flagIsTRB=true;
                }
                iSample++;
                isMuteOn = false;
                break;
        }

    }

    //load last configuration
    private void loadLastConfig(int tra, int trb) {
        notificationSystemLoading();
        for (int i = 0; i < 7; i++) {
            Handler handlerDelay = new Handler();
            handlerDelay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run:  iSample:" + iSample);
                    getSettings(iSample, tra, trb);
                }
            }, 1000 * i);
        }
    }

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
                    sendTCP(spEth.k9_fr_5);
                    break;
                case 5:
                    //sendTCP(spEth.k9_fr_6);//max frecuency
                    //initTCP_IP();
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
                    //disconnectTCP();

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
                    //text stopping wait
                    notificationSystemStoppingWait();
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

    //send modes
    private int sendModes(int modes) {

        switch (modes) {
            case 1:
                sendTCP(spEth.k9_md_1);//mode percussion
                break;
            case 2:
                sendTCP(spEth.k9_md_2);//mode vibration
                break;
            case 3:
                sendTCP(spEth.k9_md_3);//mode full percussion
                break;
            case 4:
                sendTCP(spEth.k9_md_4);//mode total vibration
                break;
        }
        return modes;
    }

    /**********************************************
     * Notifications
     */
    //notification stopping wait
    private void notificationSystemStoppingWait() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                //k9Alert.alertDialogSystemEmergencyStop(dialogEmergStop, dialogConfirmLang);
                k9Alert.alertDialogStoppingWait(dialogSoppingWait, dialogConfirmLang, dialogCancelLang);
            }
        });

    }

    //notification loading
    private void notificationSystemLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                k9Alert.alertDialogLoading(dialogSoppingLoading, dialogConfirmLang, dialogCancelLang);
            }
        });

    }

    //norification timer therapy elapsed
    private void notificationTimerElapsed() {
        //k9Alert.alertDialogTherapyDone(dialogTherapyCompleteLang, dialogConfirmLang);
        isTherapyOn = false;
        beep.beep_disable();
        //cleanDisplayTimer();
        forceStopTimerTherapy();
        isFlagTimerElapsed = false;
        // loadDisplayTimerCountFirstTime (memoryLastTimer);
    }

    //clean flag after stop
    private void cleanFlagAfterStop() {
        isTherapyOn = false;
    }

    //force to stop Therapy
    private boolean forceStopTimerTherapy() {
        stopTherapy();
        cleanFlagAfterStop();
        isTherapyOn = false;
        cleanTimerTherapy();
        cleanDisplayTimer();
        //
        //saveInMemTransd(new_transdA, new_transdB);

        //changeStrokeBtnStart(controlGUI.POS0);
        //updateBtnReady(controlGUI.POS0);
        //
        //loadDisplayTimerCountFirstTime(memoryLastTimer);
        return true;
    }

    @Override
    public void onItemSetupInfo(String name, String description) {

    }

    @Override
    public void onItemSetupAlarm(String name, String description, String location) {

    }

    //control operation start
    private void startTherapy() {
        ControlGUI controlGUI = new ControlGUI();
        Log.d(TAG, "startTherapy: "+isTherapyOn);
        sendSpCommand(controlGUI.CMD_ON, isTherapyOn);
    }

    //operation stop therapy
    private void stopTherapy() {
        ControlGUI controlGUI = new ControlGUI();
        sendSpCommand(controlGUI.CMD_OFF, isTherapyOn);
        //cleanFlagAfterStop();

        //do not clean flag if screen is locked
        if (!isLockScreen || isTherapyOn) {
            //cancelReady();
        }

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

    //timer therapy
    private void runTimerTherapy(long time, long countInterval) {
        ConvertTimeMin convertTimeMin= new ConvertTimeMin();

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
                    //
                    timerBurn++;
                    //
                    String getTime=convertTimeMin.getDurationString(timerBurn);

                    displayTimeElapsed(String.valueOf(getTime));
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

    //next cycle
    private void nextCycle() {
        Log.d(TAG, "nextCycle: ");
        displayBurnProcessStart(getCurrentTime());

        currentCycle++;
        Log.d(TAG, "nextCycle:  currentCycle:"+ currentCycle);
        loadSequences(currentCycle);
    }

    //load seq
    private void loadSequences(int currentCycle) {
        switch (currentCycle) {
            case 0:
                iSample = 0;
                sequence1();
                break;
            case 1:
                iSample = 0;
                sequence2();
                break;
            case 2:
                iSample = 0;
                sequence3();
                break;
            case 3:
                iSample = 0;
                sequence4();
                break;
            case 4:
                iSample = 0;
                sequence5();
                break;
            case 5:
                //end
                displayBurnProcessEnd(getCurrentTime());
                break;
            case 6:


                break;
        }

    }

    //sequence
    private void sequence1() {
        //load configuration into the screen
        displayBurnProcessStart(getCurrentTime());
        displayBurnProcessEnd("-");

        currentIndex++;
        updateGuiRecyclerViewBurn(String.valueOf(currentIndex - 1), burnSequence.SEQ_FREQ, burnSequence.SEQ_INT, burnSequence.SEQ_TIME, burnSequence.SEQ_TRA1, burnSequence.SEQ_TRB1, burnSequence.SEQ_STATUS_STATUS + burnSequence.SEQ_STATUS_INIT, burnSequence.SEQ_STATUS_START + getCurrentTime(), burnSequence.SEQ_STATUS_END + "-");
        updateRecyclerViewBurn();
        //send configuration to mcu
        int tra = 0;
        int trb = 0;
        loadLastConfig(tra, trb);

    }

    //sequence
    private void sequence2() {
        //load configuration into the screen
        currentIndex++;
        Log.d(TAG, "sequence2: currentIndex: " + currentIndex);
        updateGuiRecyclerViewBurn(String.valueOf(currentIndex - 1), burnSequence.SEQ_FREQ, burnSequence.SEQ_INT, burnSequence.SEQ_TIME, burnSequence.SEQ_TRA2, burnSequence.SEQ_TRB2, burnSequence.SEQ_STATUS_STATUS + burnSequence.SEQ_STATUS_INIT, burnSequence.SEQ_STATUS_START + getCurrentTime(), burnSequence.SEQ_STATUS_END + "-");
        updateRecyclerViewBurn();
        //send configuration to mcu
        int tra = 1;
        int trb = 1;
        loadLastConfig(tra, trb);

        memoryLastTimer = SP_TIM;
        loadDisplayTimerCountFirstTime(memoryLastTimer);
        //
        Handler handlerDelay = new Handler();
        handlerDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTherapy();
            }
        }, 10000);
    }

    //sequence
    private void sequence3() {
        //load configuration into the screen
        currentIndex++;
        updateGuiRecyclerViewBurn(String.valueOf(currentIndex - 1), burnSequence.SEQ_FREQ, burnSequence.SEQ_INT, burnSequence.SEQ_TIME, burnSequence.SEQ_TRA3, burnSequence.SEQ_TRB3, burnSequence.SEQ_STATUS_STATUS + burnSequence.SEQ_STATUS_INIT, burnSequence.SEQ_STATUS_START + getCurrentTime(), burnSequence.SEQ_STATUS_END + "-");
        updateRecyclerViewBurn();
        //send configuration to mcu
        int tra = 2;
        int trb = 2;
        loadLastConfig(tra, trb);

        memoryLastTimer = SP_TIM;
        loadDisplayTimerCountFirstTime(memoryLastTimer);
        //
        Handler handlerDelay1 = new Handler();
        handlerDelay1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: startTherapy3");
                startTherapy();
            }
        }, 10000);
    }

    //sequence
    private void sequence4() {
        //load configuration into the screen
        updateGuiRecyclerViewBurn(String.valueOf(currentIndex - 1), burnSequence.SEQ_FREQ, burnSequence.SEQ_INT, burnSequence.SEQ_TIME, burnSequence.SEQ_TRA4, burnSequence.SEQ_TRB4, burnSequence.SEQ_STATUS_STATUS + burnSequence.SEQ_STATUS_INIT, burnSequence.SEQ_STATUS_START + getCurrentTime(), burnSequence.SEQ_STATUS_END + "-");
        updateRecyclerViewBurn();
        //send configuration to mcu
        int tra = 3;
        int trb = 3;
        loadLastConfig(tra, trb);

        memoryLastTimer = SP_TIM;
        loadDisplayTimerCountFirstTime(memoryLastTimer);
        //
        Handler handlerDelay = new Handler();
        handlerDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTherapy();
            }
        }, 10000);
    }

    //sequence
    private void sequence5() {
        //load configuration into the screen
        updateGuiRecyclerViewBurn(String.valueOf(currentIndex - 1), burnSequence.SEQ_FREQ, burnSequence.SEQ_INT, burnSequence.SEQ_TIME, burnSequence.SEQ_TRA5, burnSequence.SEQ_TRB5, burnSequence.SEQ_STATUS_STATUS + burnSequence.SEQ_STATUS_INIT, burnSequence.SEQ_STATUS_START + getCurrentTime(), burnSequence.SEQ_STATUS_END + "-");
        updateRecyclerViewBurn();
        //send configuration to mcu
        int tra = 4;
        int trb = 4;
        loadLastConfig(tra, trb);

        memoryLastTimer = SP_TIM;
        loadDisplayTimerCountFirstTime(memoryLastTimer);
        //
        Handler handlerDelay = new Handler();
        handlerDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTherapy();
            }
        }, 10000);
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

    //set display time 00:00
    private void cleanDisplayTimer() {
        displayTimerMinSec(mTagReference.TIME_ZERO);
        sec = 59;
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

    //operation clean
    private void operationClean() {
        cleanModel();
        currentCycle = 0;
        currentIndex = 0;
        loadSequences(currentCycle);
        displayOperation(displayOperations.DISPLAY_OPE_CLEAN);// display clean
        //
        displayBurnProcessStart("-");
        displayBurnProcessEnd("-");
        //
        timerBurn=0;
        displayTimeElapsed("00:00:00");

    }

    //operation clean
    private void operationPrint() {
        displayOperation(displayOperations.DISPLAY_OPE_PRINT);// display print
    }

    //operation serial number
    private void operationSerialNumber() {

    }
}