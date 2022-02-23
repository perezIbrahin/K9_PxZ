package Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BluetoothManag extends AppCompatActivity {
    private static final String TAG = "BluetoothMana";
    //variables
   /* private static final int REQUEST_ENABLE_BT = 1;

    Context context;
    BluetoothGatt bluetoothGatt;
    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;

    public BluetoothManag(Context context, BluetoothGatt bluetoothGatt, BluetoothAdapter bluetoothAdapter, BluetoothManager bluetoothManager, BluetoothLeScanner bluetoothLeScanner) {
        this.context = context;
        this.bluetoothGatt = bluetoothGatt;
        this.bluetoothAdapter = bluetoothAdapter;
        this.bluetoothManager = bluetoothManager;
        this.bluetoothLeScanner = bluetoothLeScanner;
    }

    BluetoothLeScanner bluetoothLeScanner;
    String devNameRequested;
    String devAddRequested;

    public BluetoothManag(String devNameRequested, String devAddRequested) {
        this.devNameRequested = devNameRequested;
        this.devAddRequested = devAddRequested;
    }

    public BluetoothManag(Context context, BluetoothGatt bluetoothGatt, BluetoothAdapter bluetoothAdapter, BluetoothLeScanner bluetoothLeScanner) {
        this.context = context;
        this.bluetoothGatt = bluetoothGatt;
        this.bluetoothAdapter = bluetoothAdapter;
        this.bluetoothLeScanner = bluetoothLeScanner;
    }

    //constructor
    public BluetoothManag(Context context) {
        this.context = context;
    }

    private ScanSettings settings;
    private List<ScanFilter> filters;

    //init bluetooth
    public boolean initBluetooth() {
        //check if the system support ble


        if (bluetoothGatt == null) {
            Log.d(TAG, "initBluetooth:bluetoothGatt null ");
        }

        if (bluetoothAdapter == null) {
            Log.d(TAG, "initBluetooth:bluetoothAdapter null ");
        }

        if (bluetoothLeScanner == null) {
            Log.d(TAG, "initBluetooth:bluetoothLeScanner null ");
        }

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Log.d(TAG, "initBluetooth: Bluetooth is not enable");
        }

        //
        if (initBLEScanner()) {
            Log.d(TAG, "initBluetooth: initBLEScanner done");
        }
        return true;
    }

    //init BLE scanner
    private boolean initBLEScanner() {
        if (bluetoothAdapter != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                Log.d(TAG, "initBLE:SDK_INT>=21");
                bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
                //start scan
                startBleScan(bluetoothLeScanner);
                return true;
            } else {
                Log.d(TAG, "initBLE:SDK_INT<21");
                return false;
            }
        } else {
            Log.d(TAG, "initBLEScanner: null");
            return false;
        }
    }

    //scan bluetooth devices  call back
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult: "+result.getDevice().getName().toString());
            //scan for devices
            BluetoothDevice bluetoothDevice = result.getDevice();
            if (searchForName(devNameRequested, bluetoothDevice.getName().toString())) {
                //connect
                connectToBle(context, bluetoothGatt, bluetoothDevice, bluetoothLeScanner);
            }

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


    //connect to device
    private boolean connectToBle(Context context, BluetoothGatt bluetoothGatt, BluetoothDevice device, BluetoothLeScanner bluetoothLeScanner) {
        if (context != null && device != null) {
            try {
                if (bluetoothGatt != null) {
                    bluetoothGatt = null;
                }
                bluetoothGatt = device.connectGatt(context, true, gattCallback);
                //missing check for connectin status
                stopBleScan(bluetoothLeScanner);

            } catch (Exception e) {
                e.getLocalizedMessage();
            }


        }

        return false;
    }

    //stop scan
    public void stopBleScan(BluetoothLeScanner bluetoothLeScanner) {
        try {
            bluetoothLeScanner.stopScan(mScanCallback);
            Log.d(TAG, "stopBleScan: ");
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    //start scan
    public void startBleScan(BluetoothLeScanner bluetoothLeScanner) {
        try {
            bluetoothLeScanner.startScan(mScanCallback);
            Log.d(TAG, "startBleScan: ");
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    //gatt Callback
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {       
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "onConnectionStateChange: status:"+status);
            
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            bluetoothGatt=gatt;
            Log.d(TAG, "onServicesDiscovered: "+bluetoothGatt.toString());
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
            Log.d(TAG, "onReliableWriteCompleted: ");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.d(TAG, "onReadRemoteRssi: ");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.d(TAG, "onMtuChanged: ");
        }

        @Override
        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
            super.onServiceChanged(gatt);
            Log.d(TAG, "onServiceChanged: ");
        }
    };


    //find devices for address
    private boolean searchForAdd(String devAddRequested, String devAddFound) {
        if (devAddRequested.equalsIgnoreCase(devAddFound)) {
            return true;
        }
        return false;
    }

    //find devices for name
    private boolean searchForName(String devNameRequested, String devNameFound) {
        if (devNameRequested.equalsIgnoreCase(devNameFound)) {
            return true;
        }
        return false;
    }

    //


    //


*/

}
