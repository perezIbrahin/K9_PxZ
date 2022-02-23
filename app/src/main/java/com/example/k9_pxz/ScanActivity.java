package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import Adapter.RecyclerViewAdapScan;
import Interface.RecyclerViewClickInterface;
import Model.ModelScan;

public class ScanActivity extends AppCompatActivity implements RecyclerViewClickInterface {
    private static final String TAG = "ScanActivity";

    //RecyclerView
    private RecyclerView recyclerViewScan;
    private RecyclerView recyclerViewDevice;
    //Adapter
    private RecyclerViewAdapScan viewAdapScan = new RecyclerViewAdapScan();
    //arraylist-Recycler View Adapter
    private ArrayList<ModelScan> modelScan = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initGUI();

    }

    private void initGUI() {


    }

    //inject data to the model scan
    private ModelScan injectDataModelScan(String devName, String devUUID, String devRssi) {
        ModelScan modelScan=new ModelScan();
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
            modelScan.clear();
            Log.d(TAG, "updateGuiRecyclerViewMainDev: ");
            modelScan.clear();
            modelScan.add(injectDataModelScan(devName,devUUID,devRssi));
            updateRecyclerViewScan();
            return  true;
        }
        return false;
    }



    @Override
    public void onItemPostSelect(int position, String value) {

    }
}