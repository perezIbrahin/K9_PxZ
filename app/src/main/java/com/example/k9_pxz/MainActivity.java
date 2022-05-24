package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;

//import Alert.AlertCustomDialog;
import Alert.CustomAlert;
import Interface.RecyclerViewClickInterface;
//import Util.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClickInterface {
    private static final String TAG = " MainActivity";
    //GUI
    private Button btnMainK9;
    private Button btnMainSett;
    private Button btnMainInfo;
    private Button btnMainSleep;

    //shared preferences
    public static final String SHARED_PREFS1 = "sharedPrefs";
    //variables to save text
    public static final String BLE_ADD_K9 = "00:00:00:00:00";
    public static final String SERIAL_K9="serial";
    //variables to save text
    public static final String BLE_ADD = "text";

    //private String
    private String myBleAdd="0";
    private String mySerialAdd="0";

    public String DATA_BLE_ADD="DATA_BLE_ADD";
    public String DATA_SYSTEM_SERIAL="DATA_SYSTEM_SERIAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGUI();
        eventsBtn();

        getExtrasFromAct();//get extras from other activity
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData();
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
           // launchActivity(VibActivity.class);
            passBleAddToVib(myBleAdd);
        } else if (v == btnMainSett) {
            launchActivity(SettActivity.class);
        }else if (v == btnMainInfo) {
            CustomAlert customAlert=new CustomAlert(this,this);
            customAlert.showAlertInfo(mySerialAdd);

            //launchActivity(InfoActivity.class);
        }
        else if (v == btnMainSleep) {
            launchActivity(SleepActivity.class);
        }
    }

    //get extras from activity
    private void getExtrasFromAct(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            //String add = bundle.getString("myAdd");
            String add="KZ";
            String serial="00:00:00:00:00";

            try {
                if(DATA_BLE_ADD!=null){
                    add = bundle.getString(DATA_BLE_ADD);

                    if(add.equalsIgnoreCase(myBleAdd)){
                        Log.d(TAG, "getExtrasFromAct: same address");
                    }else{
                        Log.d(TAG, "getExtrasFromAct: address change");
                        myBleAdd=add;
                        mySerialAdd=serial;
                        saveData();
                    }
                }
                if(DATA_SYSTEM_SERIAL!=null){
                    serial = bundle.getString(DATA_SYSTEM_SERIAL);
                    Log.d(TAG, "getExtrasFromAct: "+add+".serial:"+serial);
                }




            }catch (Exception e){
                Log.d(TAG, "getExtrasFromAct: "+e.getMessage());
            }


        }
    }

    //pass bluettoth address to vibration activity
    private void passBleAddToVib(String add){
        try {
            if(add!=null){
                Bundle bundle = new Bundle();
                Log.d(TAG, "passBleAddToVib "+add);
                bundle.putString("vibAdd", add);
                Intent intent = new Intent(MainActivity.this, VibrationPercussionActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }catch (Exception e){
            Log.d(TAG, "passBleAddToVib: exception"+e.getMessage());
        }
    }

    //Method. to take care of save data
    public void saveData() {
        //shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);//mode private means no other app can change it
        SharedPreferences.Editor editor = sharedPreferences.edit();//enable to change the value
        editor.putString(BLE_ADD_K9, myBleAdd);//get the text from the editText
        editor.putString(SERIAL_K9, mySerialAdd);//get the text from the editText
        editor.apply();
        //Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
    }

    //Method to load the data
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);//mode private means no other app can change it
        myBleAdd = sharedPreferences.getString(BLE_ADD, "");
        mySerialAdd=sharedPreferences.getString(SERIAL_K9, "");
    }

    @Override
    public void onItemPostSelect(int position, String value) {

    }
}