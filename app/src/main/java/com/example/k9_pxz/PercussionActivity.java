package com.example.k9_pxz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import Util.BluetoothStatus;

public class PercussionActivity extends AppCompatActivity {
    private static final String TAG = "PercussionActivity";
    private static final boolean TODO = false;
    private static final int PERMISSION_ASK = 25;
    //Bluetooth
    private BluetoothGatt mGatt;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    //bluetooth variables
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //devices to find
    BluetoothStatus bluetoothStatus = new BluetoothStatus();
    private String DEVICE_NAME_REQUESTED = bluetoothStatus.DEFAULT_NAME_BLE;
    private String DEVICE_ADD_REQUESTED = bluetoothStatus.DEFAULT_ADD_BLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percussion);


        //init bluetooth
        initBle();

        //Request permissions for bluetooth and others
        requestPermissions();

        //Broadcast BLE devices
        broadCastReceiver();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        doBleAfterResume();//Bluetooth operations
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBleScan();
    }

    //Request permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        }, 0);


    }

    //Operation after go to resume operations
    private void doBleAfterResume() {
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(PercussionActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    Log.d(TAG, "onResume:ActivityCompat#requestPermissions ");
                    /*
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }*/
                     /*  public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                              int[] grantResults);*/
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Log.d(TAG, "onResume: startActivityForResult");
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        Log.d(TAG, "onResume: bluetooth enable");
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        //setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);

    }

    //stop ble scan
    private void stopBleScan() {
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    //start ble scanb
    private void startBleScan() {
        scanLeDevice(true);
    }

    //Init BLE
    private void initBle() {
        mHandler = new Handler();
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //Scan for devices also check for permissions
    private void scanLeDevice(final boolean enable) {
        Log.d(TAG, "scanLeDevice:enable: " + enable);
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;

                    if (ActivityCompat.checkSelfPermission(PercussionActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        Log.d(TAG, "run scanLeDevice: request permissions");

                        /*
                        if(!ActivityCompat.shouldShowRequestPermissionRationale(PercussionActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)){
                            Log.d(TAG, "run: request permission fine location");
                        }

                        if(!ActivityCompat.shouldShowRequestPermissionRationale(PercussionActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)){
                            Log.d(TAG, "run: request permission coarse location");
                        }

                        //ACCESS_BACKGROUND_LOCATION
                        if(!ActivityCompat.shouldShowRequestPermissionRationale(PercussionActivity.this,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                            Log.d(TAG, "run: request permission access background location");
                        }*/


                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.d(TAG, "scanLeDevice: start");
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.d(TAG, "scanLeDevice: stop");
        }
        //invalidateOptionsMenu();
    }

    //ask for permissions
   /* @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForBluetoothPermissions() {
        String[] permissions = new String[] {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        };
        requestPermissions(permissions, PERMISSION_ASK);
    }*/

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ASK:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // all requested permissions were granted
                    // perform your task here
                } else {
                    // permissions not granted
                    // DO NOT PERFORM THE TASK, it will fail/crash
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }*/

    //List off devices scanning
    private class LeDeviceListAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            //mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                //Log.d(TAG, "addDevice: "+device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Log.d(TAG, "mLeScanCallback: ");
                            //permissionFineLocation();
                            mLeDeviceListAdapter.addDevice(device);
                            
                            //
                            if (ActivityCompat.checkSelfPermission(PercussionActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                Log.d(TAG, "run:mLeScanCallback request permissions ");
                                if (ContextCompat.checkSelfPermission(PercussionActivity.this,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // Should we show an explanation?
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(PercussionActivity.this,
                                            android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                        Log.d(TAG, "run:shouldShowRequestPermissionRationale ");
                                        // Show an explanation to the user *asynchronously* -- don't block
                                        // this thread waiting for the user's response! After the user
                                        // sees the explanation, try again to request the permission.
                                    } else {
                                        Log.d(TAG, "run:ActivityCompat.requestPermissions ");
                                        // No explanation needed, we can request the permission.
                                        ActivityCompat.requestPermissions(PercussionActivity.this,
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                    }
                                   // return;
                                }
                                if(device!=null){
                                   // Log.d(TAG, "run: "+device.getName().toString());
                                }else{
                                    Log.d(TAG, "run: device null");
                                }
                                //Log.d(TAG, "run:LeScanCallback " + device.getName().toString());

                            }
                        }
                    });
                }
            };



    /*//check for Bluetooh devices  using name
    private boolean checkBleByName(BluetoothDevice device, String nameRequested) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return TODO;
            }
            //
            if (device == null) {
                return false;
            }

            //
            if (device.getName().toString().equalsIgnoreCase(nameRequested)) {
                Toast.makeText(this, "Connecting with:" + device.getName().toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "checkBleByName: found" + device.getName());
            } else {
                Log.d(TAG, "checkBleByName: " + device.getName());
            }
        } catch (Exception e) {
            Log.d(TAG, "checkBleByName: exception" + e.getMessage());
        }
        return false;
    }*/

    /*
    //check for Bluetooh devices  using address
    private boolean checkBleByAddress(String addFound, String addRequested) {
        try {
            if (addFound.equalsIgnoreCase(addRequested)) {
                //connectToDevice(BluetoothDevice device);
            }
        } catch (Exception e) {
            Log.d(TAG, "checkBleByName: exception" + e.getMessage());

        }
        return false;
    }*/

                //connect with bluetooth devices
                public void connectToDevice(BluetoothDevice device) {
                    Log.d(TAG, "connectToDevice: trying to connect");
                    if (mGatt == null) {
                        //stopBleScan();
                        //runthreadConnection(device);
                        //mGatt = device.connectGatt(this, true, gattCallback);
                        // Stop scan


                        //connectToDevice(device);
                        //message
                        //toastMessage("Connecting with device: " + device);

                        //wakeUp(true);

                    }
                }

                //thread to creat the connection with device
                private void runthreadConnection(BluetoothDevice device) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (device != null) {
                                            if (ActivityCompat.checkSelfPermission(PercussionActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                Log.d(TAG, "run: check permissions");
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
                                            mGatt = device.connectGatt(PercussionActivity.this, true, gattCallback);
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }, 1000);
                }

                //Bluettoth gatt servers
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
                    }

                    @Override
                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                        super.onServicesDiscovered(gatt, status);
                        Log.d(TAG, "onServicesDiscovered: " + status);
                    }

                    @Override
                    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicRead(gatt, characteristic, status);
                    }

                    @Override
                    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicWrite(gatt, characteristic, status);
                    }

                    @Override
                    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                        super.onCharacteristicChanged(gatt, characteristic);
                    }

                    @Override
                    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                        super.onDescriptorRead(gatt, descriptor, status);
                    }

                    @Override
                    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                        super.onDescriptorWrite(gatt, descriptor, status);
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

                //Broadcast devices
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

                //Boradcast connections
                private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        String action = intent.getAction();
                        Log.d(TAG, "mBroadcastReceiver: " + action);

                    }
                };
            }