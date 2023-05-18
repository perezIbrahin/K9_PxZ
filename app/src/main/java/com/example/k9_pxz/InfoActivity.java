package com.example.k9_pxz;

import static java.lang.Long.MAX_VALUE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import Util.Key_Util;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG = "InfoActivity";
    private Key_Util keyUtil = new Key_Util();
    private String SERIAL_NUMBER = "0";
    private String DEVICE_ID = "123456";
    //working time
    private long year = 1;
    private long day = 1;
    private long hours = 1;
    private long minutes = 1;
    private long seconds = 1;
    private long totalSecs = 1;
    private String timeString = "00:00:00";
    //GUI
    private TextView tvSN;
    private TextView tvLife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //
        initGui();
        //
        loadPreferences2();
        //
        loadPreferences();
        //
        displaySerialNumber(SERIAL_NUMBER, DEVICE_ID);
        //working time
        displayWorkingTime(getWorkingTime());
    }

    private void initGui() {
        tvSN = findViewById(R.id.tvSerialNumber);
        tvLife = findViewById(R.id.tvTotalRun);
    }

    private void loadPreferences2() {
        SharedPreferences sharedPref = getSharedPreferences(keyUtil.KEY_SETTINGS2, MODE_PRIVATE);
        SERIAL_NUMBER = sharedPref.getString(keyUtil.KEY_SERIAL_NUMBER, "0");
        DEVICE_ID = sharedPref.getString(keyUtil.KEY_ID, "12345");
        Log.d(TAG, "loadPreferences:KEY_SERIAL_NUMBER:" + SERIAL_NUMBER);
    }

    private void loadPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(keyUtil.KEY_SETTINGS, MODE_PRIVATE);
        totalSecs = sharedPref.getLong(keyUtil.KEY_LIFE_TIME, 1);
    }

    private String getWorkingTime() {
        try {
            if (true) {
                if (totalSecs > MAX_VALUE) {/**/
                    totalSecs = MAX_VALUE;
                }

                year = day / 365;
                day = hours / 24;
                hours = totalSecs / 3600;
                minutes = (totalSecs % 3600) / 60;
                seconds = totalSecs % 60;
                timeString = String.format("%02d:%02d:%02d:%02d:%02d", year, day, hours, minutes, seconds);
            }
        } catch (Exception e) {
            Log.d(TAG, "getWorkingTime: ex:" + e.getMessage());
        }
        return timeString;
    }

    //display working time
    private void displayWorkingTime(String life) {
        try {
            if (life != null) {
                tvLife.setText("Data format: Year:Day:Hour:Minute:Seconds" + "\n" + life);
            }
        } catch (Exception e) {
            Log.d(TAG, "displaySerialNumber: ex:" + e.getMessage());
        }

    }

    //display serial number
    private void displaySerialNumber(String sn, String id) {
        try {
            if (sn != null && id != null) {
                tvSN.setText("S/N:" + sn + "\n" + "ID:" + id);
            }
        } catch (Exception e) {
            Log.d(TAG, "displaySerialNumber: ex:" + e.getMessage());
        }

    }

}