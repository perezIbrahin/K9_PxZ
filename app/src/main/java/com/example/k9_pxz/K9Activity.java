package com.example.k9_pxz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
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
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import Bluetooth.BluetoothManag;
import Util.BluetoothStatus;
import Util.Feedback;
import Util.Flags;
import Util.GenerateSound;
import Util.UpdateIconCom;

public class K9Activity extends AppCompatActivity {
    //public class K9Activity extends AppCompatActivity implements RecyclerViewClickInterfac
    private static final String TAG = "K9Activity";
    //GUI
    private ImageView ivConnection;


    //BLE
    /*Bluetooth*/
    //private static final String EXTRA_DATA ="" ;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String ACTION_STATE_CLOSED =
            "com.example.bluetooth.le.ACTION_STATE_CLOSED";

    /*BLE stuff*/
    //private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothManag bluetoothManag;
    private BluetoothGatt bluetoothGatt;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    //private BluetoothLeScanner bluetoothLeScanner;
    BluetoothManager bluetoothManager;

    //Permissions
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    //

    BluetoothGattCharacteristic charaAcc;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    final String BLE_NAME = "KZ";//name od the device to connect
    String BLE_ADD = "0";//name od the device to connect
    //to coonect
    String deviceFoundAddress = "0";
    String deviceFoundByName = "0";

    BluetoothGattCharacteristic CHARACTERISTIC_FREQUENCY;
    BluetoothGattCharacteristic CHARACTERISTIC_INTENSITY;
    BluetoothGattCharacteristic CHARACTERISTIC_TIME;
    BluetoothGattCharacteristic CHARACTERISTIC_TRANSDUCERS;
    BluetoothGattCharacteristic CHARACTERISTIC_COMMANDS;
    BluetoothGattCharacteristic CHARACTERISTIC_PROCESS_VALUE;
    /*variable BlE*/
    private static final int REQUEST_ENABLE_BT = 1;

    //sound
    GenerateSound generateSound = new GenerateSound();
    private Feedback feedback = new Feedback();
    private Flags flags = new Flags();
    UpdateIconCom updateIconCom = new UpdateIconCom();


    private BluetoothStatus bluetoothStatus = new BluetoothStatus();
    /*Variables*/
    private String Temporal_UUID = "0";
    //timer schedule
    Timer timer;
    // MyTimerTask myTimerTask;


    //BLE new Module
    private BluetoothLeScanner bluetoothLeScanner;
    //private LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();
    private boolean scanning;
    private Handler handler = new Handler();
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k9);

        //
        initGui();

        initModuleBle();

        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                //bleScan();
                //countDownTimer.start();

            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initBleAdapter();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //bleScan();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //init GUi
    private void initGui() {
        ivConnection = findViewById(R.id.ivConnection);
    }

    //init instances
    private void initModuleBle() {

        if (initBLE()) {
            Log.d(TAG, "initModuleBle: init BLE");
        }

        //Bluetooth Permissions
        checkPermissions();

        //Broadcast connection
        broadCastReceiver();


    }

    //init BLE
    private boolean initBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getApplicationContext(), "ble_not_supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes Bluetooth adapter.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            Log.d(TAG, "initBluetooth:bluetoothManager ");
        }

        //get adapter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothAdapter = bluetoothManager.getAdapter();
            Log.d(TAG, "initBluetooth: get adapter" + bluetoothAdapter.getName());
        }
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        return true;
    }

    //BluetoothScan
    private void bleScan() {
        try {
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                Log.d(TAG, "bleScan: bluetooth adapter null");
            } else {
                Log.d(TAG, "bleScan: bluetooth adapter not null");
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
                Log.d(TAG, "bleScan: scan mode " + bluetoothAdapter.getScanMode());
                Log.d(TAG, "bleScan:get state " + bluetoothAdapter.getState());
                //
                Log.d(TAG, "bleScan: ");

                bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
                newScanBle();

                if (Build.VERSION.SDK_INT >= 21) {
                    Log.d(TAG, "initBLE:SDK_INT>=21");
                    //bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
                    // bluetoothAdapter.getBluetoothLeScanner().startScan(mScanCallback);


                    /*settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .build();
                    filters = new ArrayList<ScanFilter>();
                    bluetoothLeScanner.startScan(mScanCallback);*/
                } else {
                    Log.d(TAG, "initBLE:SDK_INT<21");
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "bleScan: exception" + e.getLocalizedMessage());
        }
    }

    //mew scan method
    private void newScanBle() {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    bluetoothLeScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(mScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(mScanCallback);
        }


    }


    // Adapter for holding devices found through scanning
    private class LeDeviceListAdapter extends BaseAdapter {

        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
          //  mInflator = DeviceScanActivity.this.getLayoutInflater();
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
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: resultcode" + resultCode);
        switch (resultCode) {
            case 10:
                Log.d(TAG, "onActivityResult: code 10");
                break;

            case REQUEST_ENABLE_BT:
                Log.d(TAG, "onActivityResult: code:" + REQUEST_ENABLE_BT);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //scan bluetooth devices  call back
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult: " + result.getRssi());

            Log.d(TAG, "onScanResult: " + result.getDevice().getName().toString());
            //scan for devices
            BluetoothDevice bluetoothDevice = result.getDevice();
            /*if (searchForName(devNameRequested, bluetoothDevice.getName().toString())) {
                //connect
                connectToBle(context, bluetoothGatt, bluetoothDevice, bluetoothLeScanner);
            }*/

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d(TAG, "onBatchScanResults: ");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, "onScanFailed: ");
        }
    };


    //Broadcast
    private void broadCastReceiver() {
        try {
            Log.d(TAG, "broadCastReceiver: ");
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            this.registerReceiver(mBroadcastReceiver, filter);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: Exception BroadCast Receiver" + e.getMessage());
        }
    }

    //Broadcast
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            String action = intent.getAction();
            //mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Device found
                Log.d(TAG, "onReceive: Device Found");
                //updateComIcon(R.string.ble_found);


            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
                Log.d(TAG, "onReceive: Device is now connected Bluetooth");
                Toast.makeText(context, "Device is now connected", Toast.LENGTH_SHORT).show();
                //myInstance.dismiss();
               /* updateComIcon(R.string.ble_connected);
                value = bluetoothStatus.getBLE_CONNECTED();
                callUpdate(UPDATE_VALUE);*/


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
                Log.d(TAG, "onReceive:Done searching ");
                //updateComIcon(R.string.ble_discovery_finished);
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
                Log.d(TAG, "onReceive: Device is about to disconnect ");
                // updateComIcon(R.string.ble_disconnected_requested);
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                Log.d(TAG, "onReceive: Device has disconnected");
                //Toast.makeText(context, "Device has disconnected", Toast.LENGTH_SHORT).show();
                //updateComIcon(R.string.ble_disconnected);
                //value = bluetoothStatus.getBLE_OFF();
                // callUpdate(UPDATE_VALUE);
            }
        }
    };


    //check bluetooth permissions
    private void checkPermissions() {
        /*Check permisions*/
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
                /*ActivityCompat.requestPermissions(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);*/
            }
        }
    }

/*
    private void initBleAdapter() {
        //mBluetoothAdapter=null;
        // sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        // sensorManager.registerListener(this, ambientLight, SensorManager.SENSOR_DELAY_NORMAL);
        //Enable Scan
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //Enable Bluetooth
            try {
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
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
    */
/*
    //look for the bluetooth device
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            // Log.d(TAG, "onScanResult: callbackType" + callbackType);
            //Log.d(TAG, "onScanResult: ScanResult" + result);
            // Log.d(TAG, "onScanResult:getName" + result.getDevice().getName());
            //Log.d(TAG, "onScanResult:getAddress " + result.getDevice().getAddress());

            BluetoothDevice mBluetoothDevice = result.getDevice();
            deviceFoundAddress = mBluetoothDevice.getAddress().toString();//BLE_ADD
            // Log.d(TAG, "onScanResult: " + mBluetoothDevice);

            //get name
            deviceFoundByName = result.getDevice().getName();


            //

            if (deviceFoundByName != null) {
                if (BLE_ADD.equalsIgnoreCase("0")) {
                    //alert dialog
                    alertDialogPublishUUID(deviceFoundAddress);
                    return;
                }

                if ((deviceFoundByName.equalsIgnoreCase(BLE_NAME)) && (deviceFoundAddress.equalsIgnoreCase(BLE_ADD))) {
                    connectToDevice(result.getDevice());
                    // Log.d(TAG, "onScanResult: Address OK---------------");
                }
            }
        }*/
/*
        //Connection with device
        public void connectToDevice(BluetoothDevice device) {
            Log.d(TAG, "connectToDevice: trying to connect");
            if (mGatt == null) {
                mDevice = device;
                mGatt = device.connectGatt(K9Activity.this, true, gattCallback);
                // Stop scan
                mBLEScanner.stopScan(mScanCallback);
                connectToDevice(device);
                //message
                toastMessage("Connecting with device: " + device);

                wakeUp(true);

                //Bluetooth adapter
                // mWriteToCharact = new WriteToCharact(mGatt);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            //Log.d(TAG, "onBatchScanResults: " + results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, "onScanFailed: " + errorCode);
        }
    };*/
/*
    // gattCallback
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "onConnectionStateChange: status" + status);
            switch (status) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.d(TAG, "onConnectionStateChange: STATE_CONNECTED ");
                    toastMessage("BLE status: Connected ");
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    Log.d(TAG, "onConnectionStateChange: STATE_DISCONNECTING ");
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.d(TAG, "onConnectionStateChange: STATE_DISCONNECTED");
                    android.os.Message msg = android.os.Message.obtain();
                    break;
            }
            Log.d(TAG, "onConnectionStateChange: newStatus " + newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    //update UI
                    android.os.Message msg = android.os.Message.obtain();
                    Log.d(TAG, "onConnectionStateChange:Message " + msg);
                    String deviceName = gatt.getDevice().getName();
                    Log.d(TAG, "onConnectionStateChange: deviceName" + deviceName);

                    gatt.discoverServices();
                    Log.d(TAG, "onConnectionStateChange: *****************************");
                    Log.d(TAG, "onConnectionStateChange:STATE_CONNECTED ");
                    Log.d(TAG, "onConnectionStateChange: run discovered services");
                    Log.d(TAG, "onConnectionStateChange: *****************************");
                    flags.FLAG_BLE_CONNECTED = true;
                    reScheduleTimer();
                    //addNotifBluetoothConn();


                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    Log.i("gattCallback", "reconnecting...");
                    BluetoothDevice mDevice = gatt.getDevice();
                    mGatt = null;
                    connectToDevice(mDevice);
                    // addNotifBluetoothDisco();
                    Log.d(TAG, "onConnectionStateChange: *****************************");
                    Log.d(TAG, "onConnectionStateChange:STATE_DISCONNECTED ");
                    Log.d(TAG, "onConnectionStateChange: *****************************");
                    flags.FLAG_BLE_CONNECTED = false;
                    timerScheduleCancel();
                    break;


                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d(TAG, "onConnectionStateChange: *****************************");
            Log.d(TAG, "onServices --- Discovered: ");
            Log.d(TAG, "onConnectionStateChange: *****************************");
            mGatt = gatt;
            // mDialog.cancelDialog();
            // List of the Services
            List<BluetoothGattService> services = gatt.getServices();
            //Log.d(TAG, "onServicesDiscovered: status" + status);
            //
            for (BluetoothGattService service : services) {
                if (service.getUuid().equals(UUID_SERVICE_FREQUENCY)) {
                    Log.d(TAG, "onServicesDiscovered: found UUID_SERVICE_FREQUENCY");
                    CHARACTERISTIC_FREQUENCY = mGatt.getService(UUID_SERVICE_FREQUENCY).getCharacteristic(UUID_CHARACTERISTIC_FREQUENCY);
                    // Log.d(TAG, "onServicesDiscovered:  CHARACTERISTIC_FREQUENCY" + CHARACTERISTIC_FREQUENCY);
                    for (BluetoothGattDescriptor descriptor : CHARACTERISTIC_FREQUENCY.getDescriptors()) {
                        //Log.d(TAG, "onServicesDiscovered: descriptor Frequency " + descriptor);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mGatt.writeDescriptor(descriptor);
                        // Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for Frequency ");
                    }
                    gatt.setCharacteristicNotification(CHARACTERISTIC_FREQUENCY, true);
                } else if (service.getUuid().equals(UUID_SERVICE_INTENSITY)) {
                    Log.d(TAG, "onServicesDiscovered: found UUID_SERVICE_INTENSITY");
                    CHARACTERISTIC_INTENSITY = mGatt.getService(UUID_SERVICE_INTENSITY).getCharacteristic(UUID_CHARACTERISTIC_INTENSITY);
                    // Log.d(TAG, "onServicesDiscovered:   CHARACTERISTIC_INTENSITY" + CHARACTERISTIC_INTENSITY);
                    for (BluetoothGattDescriptor descriptor : CHARACTERISTIC_INTENSITY.getDescriptors()) {
                        // Log.d(TAG, "onServicesDiscovered: descriptor Intensity " + descriptor);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mGatt.writeDescriptor(descriptor);
                        // Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for Intensity ");
                    }
                    gatt.setCharacteristicNotification(CHARACTERISTIC_INTENSITY, true);

                } else if (service.getUuid().equals(UUID_SERVICE_TIME)) {
                    Log.d(TAG, "onServicesDiscovered: found UUID_SERVICE_TIME");
                    CHARACTERISTIC_TIME = mGatt.getService(UUID_SERVICE_TIME).getCharacteristic(UUID_CHARACTERISTIC_TIME);
                    // Log.d(TAG, "onServicesDiscovered:  CHARACTERISTIC_TIME" + CHARACTERISTIC_TIME);
                    for (BluetoothGattDescriptor descriptor : CHARACTERISTIC_TIME.getDescriptors()) {
                        // Log.d(TAG, "onServicesDiscovered: descriptor Time " + descriptor);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mGatt.writeDescriptor(descriptor);
                        // Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for Time ");
                    }
                    gatt.setCharacteristicNotification(CHARACTERISTIC_TIME, true);


                } else if (service.getUuid().equals(UUID_SERVICE_TRANSDUCERS)) {
                    Log.d(TAG, "onServicesDiscovered: found UUID_SERVICE_TRANSDUCERS");
                    CHARACTERISTIC_TRANSDUCERS = mGatt.getService(UUID_SERVICE_TRANSDUCERS).getCharacteristic(UUID_CHARACTERISTIC_TRANSDUCERS);
                    // Log.d(TAG, "onServicesDiscovered:  CHARACTERISTIC_Transducers" + CHARACTERISTIC_TRANSDUCERS);
                    for (BluetoothGattDescriptor descriptor : CHARACTERISTIC_TRANSDUCERS.getDescriptors()) {
                        //Log.d(TAG, "onServicesDiscovered: descriptor Transducers " + descriptor);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mGatt.writeDescriptor(descriptor);
                        // Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for Transducers ");
                    }
                    gatt.setCharacteristicNotification(CHARACTERISTIC_TRANSDUCERS, true);
                    //
                } else if (service.getUuid().equals(UUID_SERVICE_COMMANDS)) {
                    Log.d(TAG, "onServicesDiscovered: found UUID_SERVICE_COMMANDS");
                    CHARACTERISTIC_COMMANDS = mGatt.getService(UUID_SERVICE_COMMANDS).getCharacteristic(UUID_CHARACTERISTIC_COMMANDS);
                    // Log.d(TAG, "onServicesDiscovered:  CHARACTERISTIC_COMMANDS" + CHARACTERISTIC_COMMANDS);
                    for (BluetoothGattDescriptor descriptor : CHARACTERISTIC_COMMANDS.getDescriptors()) {
                        Log.d(TAG, "onServicesDiscovered: descriptor commands " + descriptor);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mGatt.writeDescriptor(descriptor);
                        //  Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for commands ");
                    }
                    gatt.setCharacteristicNotification(CHARACTERISTIC_COMMANDS, true);

                } else if (service.getUuid().equals(UUID_SERVICE_PROCESS_VALUE)) {
                    Log.d(TAG, "onServicesDiscovered: found UUID_SERVICE_PROCESS_VALUE");
                    CHARACTERISTIC_PROCESS_VALUE = mGatt.getService(UUID_SERVICE_PROCESS_VALUE).getCharacteristic(UUID_CHARACTERISTIC_PROCESS_VALUE);

                    Log.d(TAG, "onServicesDiscovered: broadcast  CHARACTERISTIC_PROCESS_VALUE!!!!!");
                    Log.d(TAG, "onServicesDiscovered:  CHARACTERISTIC_PROCESS_VALUE " + CHARACTERISTIC_PROCESS_VALUE);
                    for (BluetoothGattDescriptor descriptor : CHARACTERISTIC_PROCESS_VALUE.getDescriptors()) {
                        Log.d(TAG, "onServicesDiscovered: descriptor process" + descriptor);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mGatt.writeDescriptor(descriptor);
                        Log.d(TAG, "onServicesDiscovered0:----ENABLE_NOTIFICATION for process ");
                    }
                    gatt.setCharacteristicNotification(CHARACTERISTIC_PROCESS_VALUE, true);
                    //
                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED, CHARACTERISTIC_PROCESS_VALUE);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "*********************************");
            Log.d(TAG, "onCharacteristicRead: ");
            Log.d(TAG, "*********************************");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, CHARACTERISTIC_PROCESS_VALUE);
                Log.d(TAG, "onCharacteristicRead: " + characteristic.getValue());
                Log.d(TAG, "onCharacteristicRead characteristic:/[0]: " + CHARACTERISTIC_PROCESS_VALUE.getValue()[0] + "/[1]:" + CHARACTERISTIC_PROCESS_VALUE.getValue()[1] + "/[2]:" + CHARACTERISTIC_PROCESS_VALUE.getValue()[2] + "/[3]:" + CHARACTERISTIC_PROCESS_VALUE.getValue()[3]);
                if (CHARACTERISTIC_PROCESS_VALUE.getValue()[0] == feedback.I2C_ERROR) {
                    value = feedback.I2C_ERROR_STR;
                    callUpdate(UPDATE_MCU);
                } else if (CHARACTERISTIC_PROCESS_VALUE.getValue()[0] == feedback.MCU_ERROR) {
                    value = feedback.MCU_ERROR_STR;
                    callUpdate(UPDATE_MCU);

                } else if (CHARACTERISTIC_PROCESS_VALUE.getValue()[0] == feedback.SYS_OK) {
                    value = feedback.SYS_OK_STR;
                    callUpdate(UPDATE_MCU);
                }
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
            Log.d(TAG, "onCharacteristicChanged: ");
            if ((CHARACTERISTIC_PROCESS_VALUE.getUuid().equals(UUID_CHARACTERISTIC_PROCESS_VALUE))) {
                value = "Notification:" + CHARACTERISTIC_PROCESS_VALUE.getValue()[0];
                Log.d(TAG, "onCharacteristicChanged: " + value);
                //callUpdate(UPDATE_VALUE);
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorRead: " + descriptor);
            Log.d(TAG, "*********************************");


        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorWrite get value: " + descriptor.getValue());
            Log.d(TAG, "onDescriptorWrite: permissions" + descriptor.getPermissions());
            Log.d(TAG, "onDescriptorWrite: ");
            Log.d(TAG, "*********************************");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.d(TAG, "onReliableWriteCompleted: ");
            Log.d(TAG, "*********************************");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.d(TAG, "onReadRemoteRssi: ");
            Log.d(TAG, "*********************************");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.d(TAG, "onMtuChanged: ");
        }
    };*/

    /*
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        Log.d(TAG, "broadcastUpdate: action" + action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        if (CHARACTERISTIC_PROCESS_VALUE.equals(characteristic.getUuid())) {
            Log.d(TAG, "broadcastUpdate:CHARACTERISTIC_KZ_STATUS");
            int flag = CHARACTERISTIC_PROCESS_VALUE.getProperties();
            Log.d(TAG, "broadcastUpdate: Flag" + flag);
            int format = -1;
            //
            for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                Log.d(TAG, "onServicesDiscovered: descriptor " + descriptor);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(descriptor);
                Log.d(TAG, "onServicesDiscovered!!!!!!:----ENABLE_NOTIFICATION ");//Accelerometer notification
            }
            mGatt.setCharacteristicNotification(characteristic, true);
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
    }*/


    /*Connection with device*/
    /*public void connectToDevice(BluetoothDevice device) {
        Log.d(TAG, "connectToDevice: trying to connect");
        if (mGatt == null) {
            this.mDevice = device;
            mGatt = device.connectGatt(this, true, gattCallback);
            // Stop scan
            mBLEScanner.stopScan(mScanCallback);
            connectToDevice(device);
            //message
            toastMessage("Connecting with device: " + device);

            wakeUp(true);

            //Bluetooth adapter
            // mWriteToCharact = new WriteToCharact(mGatt);
        }
    }*/

/*
    //Take care to write the value on the characteristic !!!!
    public void setValueCharact(BluetoothGattCharacteristic charac, int valueCharact) {
        Log.d(TAG, "setValueCharact: " + valueCharact);
        if (valueCharact > 0) {
            writeCharac(mGatt, charac, valueCharact);
            Log.d(TAG, "setValueCharact: sent it");
        }
    }*/

    /* Write charact int  */
    /*
    public void writeCharac(BluetoothGatt gatt, BluetoothGattCharacteristic charac, int valueInt) {
        try {
            int unixTime = 0;
            if (mBluetoothAdapter == null || gatt == null) {
                Log.w(TAG, "BluetoothAdapter not initialized");
                return;
            }
            if (charac == null) {
                Log.e(TAG, "charact not found!");
            }
            if (valueInt > 0) {
                unixTime = valueInt;

            } else {
                unixTime = 1;
            }


            String unixTimeString = Integer.toHexString(unixTime);
            // Log.d(TAG, "writeCharac: unixTimeString" + unixTimeString);

            byte[] byteArray = hexStringToByteArray(unixTimeString);
            // Log.d(TAG, "writeCharac: byteArray " + byteArray);
            // Log.d(TAG, "writeCharac:charac " + charac);
            charac.setValue(byteArray);
            boolean status = mGatt.writeCharacteristic(charac);
            if (status) {
                //addNotifBluetoothSend();
                //Toast.makeText(this, "Written Successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "writeCharac: Written Successfully");
                value = String.valueOf(valueInt);
                callUpdate(UPDATE_DEVICE);

                checkAllSpUpdates(valueInt);

            } else {
                //addNotifBluetoothCError();
                //Toast.makeText(this, "Error writing characteristicControl", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "writeCharac: Error writing characteristicControl" + status);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /*

    //new function to read
    public void readCustomCharacteristic(BluetoothGattCharacteristic mReadCharacteristic) {
        if (mBluetoothAdapter == null || mGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        if (mReadCharacteristic == null) {
            Log.d(TAG, "readCustomCharacteristic:not initialized ");
            return;
        }

        //get the read characteristic from the service
        // BluetoothGattCharacteristic mWriteCharacteristic = mCustomService.getCharacteristic(UUID.fromString("00000001-0000-1000-8000-00805f9b34fb"));
        // mWriteCharacteristic.setValue(value, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT, 0);


        Log.d(TAG, "mReadCharacteristic: " + mReadCharacteristic.getValue());
        if (!mGatt.readCharacteristic(mReadCharacteristic)) {
            Log.w(TAG, "Failed to read characteristic");
        } else {

        }
    }*/
/*
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

    //Request data from BLE
    private boolean requestPvFromBLE(int value) {
        readCustomCharacteristic(CHARACTERISTIC_PROCESS_VALUE);
        return true;

    }*/
    /*
    //upgrade display
    private void callUpdate(int what) {
        android.os.Message msg = android.os.Message.obtain();
        msg.obj = value;//value
        msg.what = what;
        msg.setTarget(uiHandler);
        msg.sendToTarget();
    }

    //Upgrade device information
    private void updateDevice(String value) {
        Log.d(TAG, "updateDevice: value" + value);
        try {
            if (value != null) {
                tvWriteCmd.setText(value);
                //upgradeIcons(value);
                updateFeedbackValue(value);//

                //
                //disable feedback when another command is pressed
                setDisableFeedback(false);
            }
        } catch (Exception e) {
            Log.d(TAG, "updateValue: ");
        }
    }

    //display icon Connection with bluetooth
    private void displayComIcon(int communication) {
        updateIconCom.updateIconCommunication(ivConnection,communication);
    }


    //Alert Dialog first time get UUID
    private void alertDialogPublishUUID(String uuid) {
        Temporal_UUID=uuid;
        new AlertCustomDialog(this,getApplicationContext());
    }*/

    //----------------Uitilities---------------------
/*
    //Save in memory the new UUID  to connect
    private boolean saveNewUUID(String uuid) {
        BLE_ADD = uuid;
        return true;
    }

    //------------------Toast--------------
    //Toast Message
    //Toast message
    private void toastMessage(String message) {
        if (message != null) {
            Toast.makeText(this, "message", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*
    //wake up mode nad sleep
    private boolean wakeUp(boolean input) {
        final String WAKE_UP_MODE = "WAKE UP";
        final String SLEEP_MODE = "SLEEP";
        String field = "0";
        boolean ret = false;
        if (input) {
            //wake up mode
            //mFlags.FLAG_SLEEP_MODE = false;
            // mFlags.FLAG_WAKE_UP_MODE = true;
            //Do
            // turnOnScreen();
            //visibleAllSleep();
            //
            //visibleSleepModeBtn();
            //invisibleWakeUpModeBtn();
            //
            field = WAKE_UP_MODE;
            ret = true;
        } else {
            //sleep mode
            //mFlags.FLAG_SLEEP_MODE = true;
            //mFlags.FLAG_WAKE_UP_MODE = false;
            //Log.d(TAG, "onClick: Flags Wake up->sleep:" + mFlags.FLAG_SLEEP_MODE + "Wake->" + mFlags.FLAG_WAKE_UP_MODE);
            //Do
            //turnOffScreen();
            // invisibleAllSleep();
            //switching mode
            //invisibleSleepModeBtn();
            //visibleWakeUpModeBtn();
            //
            field = SLEEP_MODE;
            ret = false;
        }
        generateSound.generateToneAlert();
        return ret;
    }*/


    /*
    //re-schedule Timer
    private void reScheduleTimer() {
        if (flags.FLAG_BLE_CONNECTED) {
            if (timer != null) {
                timer.cancel();
            }
            //re-schedule timer here
            //otherwise, IllegalStateException of
            //"TimerTask is scheduled already"
            //will be thrown
            timer = new Timer();
            myTimerTask = new MyTimerTask();
            //delay 1000ms, repeat in 5000ms
            timer.schedule(myTimerTask, 1000, 5000);
            Log.d(TAG, "reScheduleTimer: ");
        } else {
            Log.d(TAG, "reScheduleTimer: flag bluetooth" + flags.FLAG_BLE_CONNECTED);
        }
    }

    @Override
    public void onItemPostSelect(int position, String value) {
        Status status=new Status();
        if(value.equalsIgnoreCase(status.DIALOG_ALERT_UUID)){
            if(position==status.SAVE_NEW_UUID){
                saveNewUUID(Temporal_UUID);
            }else if(position==status.START_SCAN_BLE){
                
            }
        }
    }




    // class timer schedule
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
            final String strDate = simpleDateFormat.format(calendar.getTime());

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // textCounter.setText(strDate);
                    //sendDataPVBLE(0x25);

                    //Log.d(TAG, "run: CHARACTERISTIC_PROCESS_VALUE");
                    //setValueCharact(CHARACTERISTIC_PROCESS_VALUE,0x10);
                    Log.d(TAG, "run: timer schedule");
                    // Log.d(TAG, "run:CHARACTERISTIC_PROCESS_VALUE "+CHARACTERISTIC_PROCESS_VALUE.toString());
                    // readCustomCharacteristic(CHARACTERISTIC_PROCESS_VALUE);

                    requestPvFromBLE(new SetPointsBluetooth().BLE_GET_PV);
                }
            });
        }

    }*/


}