package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import Adapter.RecyclerViewAdapBurn;
import Alert.CustomAlert;
import Interface.RecyclerViewClickInterface;
import Model.ModelBurn;
import Util.Util_Burn;

public class BurningActivityRutine extends AppCompatActivity implements RecyclerViewClickInterface, View.OnClickListener {
    private static final String TAG = "BurningActivityRutine";

    /*
    *
    * https://www.youtube.com/watch?v=gpH4Zr1ffnU*/
    //GUI
    private Button btnBurnStart;
    private Button btnBurnStop;
    private Button btnBurnPause;
    private Button btnBurnReport;
    private Button btnHome;

    //private String
    private String BLE_ADD_GOT = "0";
    private String SERIAL_NUMBER = "0";
    CustomAlert customAlert;

    public String DATA_BLE_ADD="DATA_BLE_ADD";
    public String DATA_SYSTEM_SERIAL="DATA_SYSTEM_SERIAL";


    private Util_Burn utilBurn = new Util_Burn();

    //RecyclerView
    private RecyclerView recyclerViewBurn;
    //Adapter
    private RecyclerViewAdapBurn viewAdapBurn = new RecyclerViewAdapBurn();
    //arraylist-Recycler View Adapter
    private ArrayList<ModelBurn> modelBurns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burning_rutine);

        firstTimeLoadGUI();
        initGUI();
        eventsBtn();
    }

    //inject data to the model scan
    private ModelBurn injectDataModelBurn(String modName, String modFreq, String modInt, String modTime, String modCycles, String modStatus) {
        ModelBurn modelBurn = new ModelBurn();
        modelBurn.setModName(modName);
        modelBurn.setModFreq(modFreq);
        modelBurn.setModInt(modInt);
        modelBurn.setModTime(modTime);
        modelBurn.setModCycles(modCycles);
        modelBurn.setModStatus(modStatus);
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
    private boolean updateGuiRecyclerViewBurn(String modName, String modFreq, String modInt, String modTime, String modCycles, String modStatus) {
        if (modelBurns != null) {
            //modelScan.clear();
            Log.d(TAG, "updateGuiRecyclerViewMainDev: ");
            //modelScan.clear();
            modelBurns.add(injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
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

        switch (module) {
            case 1:
                modelBurns.add(0, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 2:
                modelBurns.add(1, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 3:
                modelBurns.add(2, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 4:
                modelBurns.add(3, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
            case 5:
                modelBurns.add(4, injectDataModelBurn(modName, modFreq, modInt, modTime, modCycles, modStatus));
                break;
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

    //init GUI
    private void initGUI() {
        btnBurnStart = findViewById(R.id.btnBurnStart);
        btnBurnStop = findViewById(R.id.btnBurnStop);
        btnBurnPause = findViewById(R.id.btnBurnPause);
        btnBurnReport = findViewById(R.id.btnBurnReport);
        btnHome = findViewById(R.id.btnHome);
    }

    //events buttons
    private void eventsBtn() {
        btnBurnStart.setOnClickListener(this);
        btnBurnStop.setOnClickListener(this);
        btnBurnPause.setOnClickListener(this);
        btnBurnReport.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onItemPostSelect(int position, String value) {

    }

    @Override
    public void onClick(View v) {
        if (v == btnBurnStart) {

        } else if (v == btnBurnStop) {

        } else if (v == btnBurnPause) {

        } else if (v == btnBurnReport) {

        } else if (v == btnHome) {
            goHome();
        }
    }

    //go to home
    private void goHome(){
        Bundle bundle = new Bundle();
        Log.d(TAG, "onClick: get address " + BLE_ADD_GOT);
        bundle.putString(DATA_BLE_ADD, BLE_ADD_GOT);//
        bundle.putString(DATA_SYSTEM_SERIAL, SERIAL_NUMBER);
        Intent intent = new Intent(BurningActivityRutine.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}