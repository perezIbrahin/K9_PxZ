package com.example.k9_pxz;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Adapter.RecyclerViewAdapScan;
import Alert.CustomAlert;
import Bluetooth.BluetoothCharacter;
import Interface.RecyclerViewClickInterface;
import Model.ModelScan;
import Util.Status;

public class ScanActivity extends AppCompatActivity implements RecyclerViewClickInterface, View.OnClickListener {
    private static final String TAG = "ScanActivity";
    Status status = new Status();

    //RecyclerView
    private RecyclerView recyclerViewScan;
    private RecyclerView recyclerViewDevice;
    //Adapter
    private RecyclerViewAdapScan viewAdapScan = new RecyclerViewAdapScan();
    //arraylist-Recycler View Adapter
    private ArrayList<ModelScan> modelScan = new ArrayList<>();

    //GUI
    private Button btnScan;
    private Button btnHome;

    //Bluetooth
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mGatt;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothLeScanner mBLEScanner;
    //public ScanActivity.LeDeviceListAdapter mLeDeviceListAdapter;
    public BluetoothCharacter bluetoothCharacter = new BluetoothCharacter();
    public Status statusSystem = new Status();//status of the system
    //ProgressDialog
    private ProgressDialog progressDialog;

    //save shared preferences
    //Variables to save data
    public static final String SHARED_PREFS = "sharedPrefs";
    //variables to save text
    public static final String BLE_ADD = "text";
    public static final String SYSTEM_SERIAL_NUMBER = "serial";
    //private String
    private String BLE_ADD_GOT = "0";
    private String SERIAL_NUMBER = "0";
    CustomAlert customAlert;

    public String DATA_BLE_ADD="DATA_BLE_ADD";
    public String DATA_SYSTEM_SERIAL="DATA_SYSTEM_SERIAL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initGUI();
        events();

        initBLE();// Init Bluetooth

        bluetoothScanWithFilter();//set some filters on Bluetooth scan

        /*
         * enable  GUI stuff
         * */
        //Permissions
        checkPermissions();
        // Boadcast
        broadCastReceiver();

        //
        //proceedScan();
    }

    private void initGUI() {
        btnScan = findViewById(R.id.btnScanDev);
        btnHome = findViewById(R.id.btnHome);
    }

    private void events() {
        btnScan.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    //inject data to the model scan
    private ModelScan injectDataModelScan(String devName, String devUUID, String devRssi) {
        ModelScan modelScan = new ModelScan();
        modelScan.setDeviceName(devName);
        modelScan.setDevUIID(devUUID);
        modelScan.setDevRssi(devRssi);
        Log.d(TAG, "injectDataModelScan:name: " + modelScan.getDeviceName() + ".UUID:" + modelScan.getDevUIID());
        return modelScan;
    }

    //update Recycler view scan devices
    private void updateRecyclerViewScan() {
        recyclerViewScan = findViewById(R.id.recyclerViewScan);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewScan.setLayoutManager(linearLayoutManager);
        recyclerViewScan.setHasFixedSize(true);
        viewAdapScan = new RecyclerViewAdapScan(this, modelScan);//modelDevicesArrayList
        recyclerViewScan.setAdapter(viewAdapScan);
    }

    //update GUI adapter Scan for devices
    private boolean updateGuiRecyclerViewScan(String devName, String devUUID, String devRssi) {
        if (modelScan != null) {
            //modelScan.clear();
            Log.d(TAG, "updateGuiRecyclerViewMainDev: ");
            //modelScan.clear();
            modelScan.add(injectDataModelScan(devName, devUUID, devRssi));
            return true;
        }
        return false;
    }

    //clean model
    private void cleanModel() {
        modelScan.clear();
    }

    @Override
    public void onItemPostSelect(int position, String value) {
        Log.d(TAG, "onItemPostSelect: pos:" + position + ":value:" + value);
        if (value != null) {
            if (position == status.STATUS_SERIAL_LINK) {
                saveData(value);
            } else if (position == status.STATUS_SCAN) {
                BLE_ADD_GOT = value;
                //text to get serial number
                //CustomAlert
                customAlert = new CustomAlert(this, this);
                Log.d(TAG, "onItemPostSelect:BLE_ADD_GOT "+BLE_ADD_GOT);
                if(BLE_ADD_GOT!=null){
                    customAlert.showDialogSerialLink(BLE_ADD_GOT);//BLE_ADD_GOT
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnScan) {
            proceedScan();
        } else if (v == btnHome) {
            goHome();
            /*Bundle bundle = new Bundle();
            Log.d(TAG, "onClick: get address " + BLE_ADD_GOT);
            bundle.putString("myAdd", BLE_ADD_GOT);
            Intent intent = new Intent(ScanActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            //launchActivity(MainActivity.class);*/
        }
    }

    //go to home
    private void goHome(){
        Bundle bundle = new Bundle();
        Log.d(TAG, "onClick: get address " + BLE_ADD_GOT);
        bundle.putString(DATA_BLE_ADD, BLE_ADD_GOT);//
        bundle.putString(DATA_SYSTEM_SERIAL, SERIAL_NUMBER);//
        Intent intent = new Intent(ScanActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
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
            //String deviceNameFound = mBluetoothDevice.getName().toString();
            String deviceFound = mBluetoothDevice.getAddress().toString();
            Log.d(TAG, "*************************************************************");
            //Log.d(TAG, "Device found name:-> " + deviceNameFound);
            Log.d(TAG, "Device found Add:-> " + deviceFound);
            //Log.d(TAG, "Looking for :->" + bluetoothCharacter.MAC);
            Log.d(TAG, "*************************************************************");

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

            //Insert data into the model
            if (mBluetoothDevice != null) {
                try {
                    if (mBluetoothDevice.getName() != null) {
                        Log.d(TAG, "onScanResult:name: " + mBluetoothDevice.getName());
                        if (mBluetoothDevice.getName().equalsIgnoreCase("KZ")) {
                            getBluetoothDevice(mBluetoothDevice.getName(), mBluetoothDevice.getAddress(), "remote");
                        }

                    } else {
                        getBluetoothDevice("N/A", mBluetoothDevice.getAddress(), "remote");
                    }
                } catch (Exception e) {
                    Log.d(TAG, "onScanResult: exception" + e.getMessage());
                }
            } else {
                Log.d(TAG, "onScanResult: bluetooth device null");
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

    //bluetooth scan with filters
    private void bluetoothScanWithFilter() {
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
                    return;
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
                    //mBLEScanner.startScan(mScanCallback);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "onPostResume: " + e.getMessage());
        }
    }

    public void proceedScan() {
        try {
            if (mBluetoothAdapter != null) {
                scanStart();


                boolean scanning = true;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return;
                }
                mBLEScanner.startScan(mScanCallback);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    //  @Override
                    public void run() {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            //return;
                        }

                        mBLEScanner.stopScan(mScanCallback);
                        scanEnded();
                        //progressDialog.dismiss();
                    }
                }, 5000);
            }
        } catch (Exception e) {
            Log.d(TAG, "proceedScan: " + e.getMessage());
        }
    }

    //Dialog scanning in progress
    private void runProgressDialog() {
        String title = "Scanning in progress ...";
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
    }

    //scan start
    private void scanStart() {
        Log.d(TAG, "scanStart: ");
        cleanModel();
        runProgressDialog();
    }

    //scan ended
    private void scanEnded() {
        organizeList();
        progressDialog.dismiss();
        updateRecyclerViewScan();
    }

    //get device data
    private void getBluetoothDevice(String name, String Add, String UUID) {
        String defaultValue = "empty";
        String mName = name;
        String mAdd = Add;
        String mUUID = UUID;

        if (name == null || name.isEmpty()) {
            mName = defaultValue;
        }

        if (mAdd == null || mAdd.isEmpty()) {
            mAdd = defaultValue;
        }

        if (mUUID == null || mUUID.isEmpty()) {
            mUUID = defaultValue;
        }
        updateGuiRecyclerViewScan(mName, mAdd, mUUID);
    }

    //organize list
    private void organizeList() {
        //ArrayList<ModelScan> modelScan = new ArrayList<>();
        Log.d(TAG, "organizeList: modelScan size" + modelScan.size());
        //HashSet  hashSet = new HashSet();
        HashSet<ModelScan> hashSet = new HashSet<ModelScan>();
        hashSet.addAll(modelScan);
        modelScan.clear();
        modelScan.addAll(hashSet);
        Log.d(TAG, "organizeList: model scan size after" + modelScan.size());
    }

    //-----------Save/Load data----------------//
    //Method. to take care of save data
    public void saveData(String serial) {
        SERIAL_NUMBER=serial;
        //shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);//mode private means no other app can change it
        SharedPreferences.Editor editor = sharedPreferences.edit();//enable to change the value
        editor.putString(BLE_ADD, BLE_ADD_GOT);//get the text from the editText
        editor.putString(SYSTEM_SERIAL_NUMBER, serial);//get the text from the editText
        editor.apply();
        //12ez567890Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();

        goHome();
    }

    //
}