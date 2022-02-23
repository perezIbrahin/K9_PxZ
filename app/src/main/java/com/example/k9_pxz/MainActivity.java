package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = " MainActivity";
    //GUI
    private Button btnMainK9;
    private Button btnMainSett;
    private Button btnMainInfo;
    private Button btnMainSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGUI();
        eventsBtn();
    }

    private void initGUI() {
        btnMainK9 = findViewById(R.id.btnMainK9);
        btnMainSett = findViewById(R.id.btnMainSet);
        btnMainInfo = findViewById(R.id.btnMainInfo);
        btnMainSleep = findViewById(R.id.btnMainSllep);
    }

    private void eventsBtn() {
        btnMainK9.setOnClickListener(this);
        btnMainSett.setOnClickListener(this);
        btnMainInfo.setOnClickListener(this);
        btnMainSleep.setOnClickListener(this);
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnMainK9) {
            launchActivity(VibActivity.class);
        } else if (v == btnMainSett) {
            launchActivity(SettActivity.class);
        }else if (v == btnMainInfo) {
            launchActivity(InfoActivity.class);
        }
        else if (v == btnMainSleep) {
            launchActivity(SleepActivity.class);
        }
    }
}