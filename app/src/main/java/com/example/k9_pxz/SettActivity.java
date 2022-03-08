package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import Alert.AlertCustomDialog;
import Alert.CustomAlert;
import Interface.RecyclerViewClickInterface;
import Util.Navigation;
import Util.Safety;

public class SettActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickInterface {
    private static final String TAG = "SettActivity";
    Safety safety=new Safety();
    Navigation navigation=new Navigation();
    //GUI
    private Button btnSetLink;
    private Button btnHome;
    private Button btnBurn;

    //


    AlertCustomDialog alertCustomDialog;
    CustomAlert customAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sett);
        initGui();
        events();
    }

    private void initGui() {
        btnSetLink = findViewById(R.id.btnBurnStart);
        btnHome=findViewById(R.id.btnHome);
        btnBurn=findViewById(R.id.btnBurnStop);

    }

    private void events(){
        btnSetLink.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnBurn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==btnSetLink){
            //alertCustomDialog=new AlertCustomDialog(this, this);
            customAlert=new CustomAlert(this, this);
            customAlert.showDialog();
            customAlert.showDialogLink(safety.PASS_QC);
            //launchActivity(ScanActivity.class);
        }else if(v==btnHome){
            launchActivity(MainActivity.class);
        }else if(v==btnBurn){
            launchActivity(BurningActivityRutine.class);
        }
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    @Override
    public void onItemPostSelect(int position, String value) {
        Log.d(TAG, "onItemPostSelect: "+position+"value:"+value);
        if(value.equalsIgnoreCase(navigation.NAV_LINK)){
            launchActivity(ScanActivity.class);
        }
    }
}