package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SleepActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnExitSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        initGUI();
        eventsBtn();
    }
    private void initGUI() {
        btnExitSleep = findViewById(R.id.btnSleepDisable);
    }

    private void eventsBtn() {
        btnExitSleep.setOnClickListener(this);
    }

    private void launchActivity(Class mclass) {
        if (mclass != null) {
            Intent i = new Intent(getApplicationContext(), mclass);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnExitSleep) {
            launchActivity(MainActivity.class);
        }
    }
}