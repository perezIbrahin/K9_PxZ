package com.example.k9_pxz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import Bluetooth.BluetoothShare;
import Util.Message;
import Util.Rev;

public class ReportBurnActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ReportBurnActivity";

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final int REQUEST_APP_SETTINGS = 168;

    TextView tvManualRev;
    WebView webview;
    PDFView pdfViewK9;
    private FloatingActionButton fbprint;
    //Bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;

    private ConnectThread mConnectThread;


    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.WRITE_SECURE_SETTINGS
            /* ETC.. */
    };

    //revision
    private Rev rev = new Rev();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.activity_report_burn);
        //remove menu bar
        removeMenuBar();
        //remove action bar from top
        removeActionBar();
        //init
        initGUI();
        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_16);
        //buttons
        eventsBtn();//events
        //load pdf create before
        loadPdf();

        //initBluetooth();


    }

    //GUI
    private void initGUI() {
        tvManualRev = (TextView) findViewById(R.id.tvReportBurnRev);
        //webview = (WebView) findViewById(R.id.webview);
        pdfViewK9 = findViewById(R.id.pdfViewReportBurn);
        fbprint = findViewById(R.id.fbPrint);
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

    //load layout
    private void loadLayout(int layout) {
        try {
            setContentView(layout);
        } catch (Exception e) {
            Log.d(TAG, "loadLayout: ex:" + e.getMessage());
        }
    }

    //display software revision
    private void displaySoftRev(String revision) {
        if (revision != null) {
            if (tvManualRev != null) {
                tvManualRev.setText(revision);
            }
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

    private void loadPdf() {
        try {
            if (pdfViewK9 != null) {
                String fileName = "";
                Document document = new Document();
                fileName = "k9_burning" + ".pdf";
                Context context = getApplicationContext();
                String dest = context.getExternalFilesDir(null) + "/";

                File dir = new File(dest);
                if (!dir.exists())
                    dir.mkdirs();
                File pdfFile = new File(dest + "/" + fileName);
                Uri mURI = FileProvider.getUriForFile(
                        context,
                        context.getApplicationContext()
                                .getPackageName() + ".provider", pdfFile);

                Log.d(TAG, "loadPdf: " + dest + fileName);
                //pdfViewK9.fromAsset(dest  + fileName).load();
                //pdfViewK9.fromSource(dest  + fileName)

                pdfViewK9.fromUri(mURI)
                        .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(true)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        // allows to draw something on the current page, usually visible in the middle of the screen
                        // .onDraw(onDrawListener)
                        // allows to draw something on all pages, separately for every page. Called only for visible pages
                        //.onDrawAll(onDrawListener)
                        //.onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
                        //.onPageChange(onPageChangeListener)
                        //.onPageScroll(onPageScrollListener)
                        //.onError(onErrorListener)
                        //.onPageError(onPageErrorListener)
                        //.onRender(onRenderListener) // called after document is rendered for the first time
                        // called on single tap, return true if handled, false to toggle scroll handle visibility
                        // .onTap(onTapListener)
                        //.onLongPress(onLongPressListener)
                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                        .password(null)
                        .scrollHandle(null)
                        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                        // spacing between pages in dp. To define spacing color, set view background
                        .spacing(5)
                        //.autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                        //.linkHandler(DefaultLinkHandler)
                        //.pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                        // .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                        // .pageSnap(false) // snap pages to screen boundaries
                        // .pageFling(false) // make a fling change only a single page like ViewPager
                        // .nightMode(false) // toggle night mode

                        .load();
                //pdfViewK9.fromFile(File);
                // pdfViewK9.fromBytes(byte[])
                //pdfViewK9.fromStream(InputStream)
            }
        } catch (Exception e) {
            Log.d(TAG, "loadPdf: ex:" + e.getMessage());
        }
    }

    /**********************************************
     *Events
     */
    //events buttons
    private void eventsBtn() {
        fbprint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (fbprint == v) {
            print();
        }
    }


    private void initBluetooth() {

        // Use this check to determine whether Bluetooth classic is supported on the device.
        // Then you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "bluetooth_not_supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        //. Determine if Android supports Bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.d(TAG, "initBluetooth:Device does not support Bluetooth ");
        }

        // Turn on Bluetooth if disabled
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
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
            startActivityForResult(enableBtIntent, 1);
        }

        //get paired devices
        getPairedDevices();

        //
        if (mDevice.getAddress()!=null){
            mConnectThread = new ConnectThread(mDevice);
            mConnectThread.start();
        }
    }

    //get paired devices
    private void getPairedDevices() {
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
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mDevice = device;
                Log.d(TAG, "getPairedDevices: Name:" + mDevice.getName() + "---uuid:" + mDevice.getUuids() + "--add:" + mDevice.getAddress().toString());
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return TODO;
                }
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

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
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                Log.d(TAG, "run: mmSocket.connect ");
            } catch (IOException connectException) {
                Log.d(TAG, "run:IOException connectException ");
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            while (true) {
                try {
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    for(int i = begin; i < bytes; i++) {
                        if(buffer[i] == "#".getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;

            switch(msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    break;
            }
        }
    };

    private void print() {
        //permission to management ethernet
        //initPermission();
        //mConnectedThread = new ConnectedThread(mmSocket);
       // mConnectedThread.start();



    }

    /*Bluetooth*/
    private void shareFilesBluetooth() {
        ContentValues values = new ContentValues();
        // values.put(BluetoothShare.URI, "file:///sdcard/refresh.txt");
        Log.d(TAG, "shareFilesBluetooth: uri:" + getAddOfFileStr());

        final BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
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
        String destination = manager.getAdapter().getAddress().toString();


        values.put(BluetoothShare.URI, getAddOfFileStr());//getAddOfFile()
        values.put(BluetoothShare.DESTINATION, destination);
        values.put(BluetoothShare.DIRECTION, BluetoothShare.DIRECTION_OUTBOUND);
        Long ts = System.currentTimeMillis();
        values.put(BluetoothShare.TIMESTAMP, ts);
        getContentResolver().insert(BluetoothShare.CONTENT_URI, values);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        String permission = permissions[i];
                        if (Manifest.permission.READ_PHONE_STATE.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                // you now have permission
                            }
                        }
                        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                // you now have permission
                            }
                        }
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    //not used-disconnect
    public String disconnectEthernet1() throws NoSuchMethodException {
        //get existing network
        Log.d(TAG, "disconnectEthernet1: list of networks:" + doesEthExist());
        if (doesEthExist()) {

            StringBuffer output = new StringBuffer();
            Log.d(TAG, "disconnectEthernet1: ");
            Process p;
            try {
                p = Runtime.getRuntime().exec("ifconfig eth0 down");
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            } catch (Exception e) {
                Log.d(TAG, "  exception " + e.toString());
                e.printStackTrace();
            }
            String response = output.toString();
            Log.d(TAG, " response " + response);
            return response;
        }


        return "0";
    }

    //not used-
    public static void turnEthOnOrOff(boolean input) {

        try {
            if (!input) {
                if (isEthOn()) {
                    Runtime.getRuntime().exec("ifconfig eth0 down");
                }
            } else {
                Runtime.getRuntime().exec("ifconfig eth0 up");
            }


        } catch (IOException e) {
            Log.e("OLE", "Runtime Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //not used-check if ethernet is on
    public static boolean isEthOn() {
        try {

            String line;
            boolean r = false;
            Process p = Runtime.getRuntime().exec("netcfg");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (line.contains("eth0")) {
                    if (line.contains("UP")) {
                        r = true;
                    } else {
                        r = false;
                    }
                }
            }
            input.close();

            Log.e("OLE", "isEthOn: " + r);
            return r;

        } catch (IOException e) {
            Log.e("OLE", "Runtime Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }


    //not used-check if the network exist
    public static boolean doesEthExist() {
        List<String> list = getListOfNetworkInterfaces();
        return list.contains("eth0");
    }

    // //not used-get a list of networks
    public static List<String> getListOfNetworkInterfaces() {

        List<String> list = new ArrayList<String>();

        Enumeration<NetworkInterface> nets;

        try {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {

            e.printStackTrace();
            return null;
        }

        for (NetworkInterface netint : Collections.list(nets)) {
            list.add(netint.getName());
        }

        return list;

    }

    /*Permissions*/
    private void initPermission() {
        /* All version with SDK_INT < 22 grant permissions on install time. */
        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {
            Toast.makeText(this, "Please grant all permissions", Toast.LENGTH_LONG).show();
            goToSettings();
        }
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    public boolean hasPermissions(@NonNull String... permissions) {
        for (String permission : permissions)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission))
                    return false;
            }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_APP_SETTINGS) {
            if (hasPermissions(requiredPermissions)) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Uri getAddOfFile() {
        String fileName = "k9_burning" + ".pdf";
        Context context = getApplicationContext();
        String dest = context.getExternalFilesDir(null) + "/";

        File pdfFile = new File(dest + "/" + fileName);
        Uri mURI = FileProvider.getUriForFile(
                context,
                context.getApplicationContext()
                        .getPackageName() + ".provider", pdfFile);
        return mURI;
    }

    private String getAddOfFileStr() {
        String fileName = "k9_burning" + ".pdf";
        Context context = getApplicationContext();
        String dest = context.getExternalFilesDir(null) + "/";

        File pdfFile = new File(dest + "/" + fileName);
        Uri mURI = FileProvider.getUriForFile(
                context,
                context.getApplicationContext()
                        .getPackageName() + ".provider", pdfFile);
        String str = mURI.toString();
        return str;
    }

}