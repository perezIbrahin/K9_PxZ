package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettActivity";
    //GUI
    private Button btnSetLink;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sett);
        initGui();
        events();
    }

    private void initGui() {
        btnSetLink = findViewById(R.id.btnSetLink);
        btnHome=findViewById(R.id.btnHome);

    }

    private void events(){
        btnSetLink.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==btnSetLink){
            launchActivity(ScanActivity.class);
        }else if(v==btnHome){
            launchActivity(MainActivity.class);
        }
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }
}