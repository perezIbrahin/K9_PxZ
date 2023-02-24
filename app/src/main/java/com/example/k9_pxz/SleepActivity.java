package com.example.k9_pxz;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Util.LocaleHelper;
import Util.Rev;

public class SleepActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SleepActivity";
    private Button btnExitSleep;
    private TextView tvSleepRev;
    private ImageView ivAnimateLogo;
    private ConstraintLayout constraintLayout;

    private int height = 0;
    private int width = 0;
    //variables
    private int xMAXDelta = 800;
    private int yMAXDelta = 600;

    private int xMINDelta = 10;
    private int yMINDelta = 10;

    private int xDelta = xMINDelta;
    private int yDelta = yMAXDelta;
    //


    private int cycle = 0;

    //
    private CountDownTimer countDownTimer;

    //revision
    private Rev rev = new Rev();
    //Language
    private String language = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.layout_sleep);
        //remove menu bar
        removeMenuBar();
        //remove action bar from top
        removeActionBar();
        //init all components
        initGUI();
        //init others stuff
        initOther();
        //init language
        initLang();
        //buttons events
        eventsBtn();

        //
        displaySoftRev(rev.APP_REV_PAGE_12);
        //
        getScreenMetric();

        setCountDownTimer();

        btnExitSleep.setVisibility(View.INVISIBLE);
    }

    private void initGUI() {
        btnExitSleep = findViewById(R.id.btnSleepDisable);
        tvSleepRev = findViewById(R.id.tvSleepRev);
        ivAnimateLogo = findViewById(R.id.ivAnimateLogo);
        constraintLayout=findViewById(R.id.clSleep);
    }

    //init other stuff
    private void initOther() {
        language = getExtrasFromAct();
    }

    //init language
    private void initLang() {
        loadContentByLanguage(getResourcesLanguage(language));
    }

    //get resources for the language
    private Resources getResourcesLanguage(String language) {
        Context context;
        Resources resources;
        context = LocaleHelper.setLocale(SleepActivity.this, language);
        resources = context.getResources();
        return resources;
    }

    //load all the text according to the language
    private void loadContentByLanguage(Resources resources) {
        btnExitSleep.setText(resources.getString(R.string.string_text_btn_sleep));
    }

    private void eventsBtn() {
        btnExitSleep.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);
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
        }else if(v== constraintLayout){
            Log.d(TAG, "onClick:constraintLayout ");
            launchActivity(MainActivity.class);
        }
    }

    //load layout
    private void loadLayout(int layout) {
        try {
            //setContentView(R.layout.activity_main);
            setContentView(layout);
        } catch (Exception e) {
            Log.d(TAG, "loadLayout: ex:" + e.getMessage());
        }
    }

    //display software revision
    private void displaySoftRev(String revision) {
        if (revision != null) {
            if (tvSleepRev != null) {
                tvSleepRev.setText(revision);

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

    //get language
    private String getExtrasFromAct() {
        String lang = "en";
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                //get language
                lang = bundle.getString("language");
                if (lang != null) {
                    return lang;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getExtrasFromAct: " + e.getMessage());
        }
        return "en";
    }

    //get screen metric
    private void getScreenMetric() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;

             xMAXDelta = width;
             yMAXDelta =  height;

            Log.d(TAG, "getScreenMetric:height: " + height + ".width:" + width);
        } catch (Exception e) {
            Log.d(TAG, "getScreenMetric: ex:" + e.getMessage());
        }

    }

    //count down timer
    private void setCountDownTimer() {
        try {
            if (countDownTimer != null) {
                countDownTimer = null;
            }

            countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onFinish() {
                    getCoordinates();
                    countDownTimer.start();
                }
            }.start();


        } catch (Exception e) {
            Log.d(TAG, "setCountDownTimer: ex:" + e.getMessage());
        }


    }

    //animate icon
    private void getCoordinates() {
        int offset = 20;
        Log.d(TAG, "getCoordinates: yDelta:" + yDelta + "/xDelta:" + xDelta);
        if (cycle == 0) {
            if (yDelta > yMINDelta) {
                xDelta = xDelta + offset;
                yDelta = yDelta - xDelta;
                updateLogo(xDelta, yDelta);
                Log.d(TAG, "getCoordinates:  cycle0");

                return;
            }
            cycle = 1;
        } else if (cycle == 1) {
            if (xDelta > xMINDelta) {
                xDelta = xDelta - offset;
                yDelta = yMINDelta;
                updateLogo(xDelta, yDelta);
                Log.d(TAG, "getCoordinates:  cycle1");
                return;
            }
            cycle = 2;
        } else if (cycle == 2) {
            if (yDelta < yMAXDelta) {
                yDelta = yDelta + offset;
                xDelta = yDelta;
                updateLogo(xDelta, yDelta);
                Log.d(TAG, "getCoordinates:  cycle2");
                return;
            }
            cycle = 3;
        } else if (cycle == 3) {
            if (yDelta > yMINDelta) {
                yDelta = yDelta - offset;
                xDelta = xMINDelta;
                updateLogo(xDelta, yDelta);
                Log.d(TAG, "getCoordinates:  cycle3");
                return;
            }
            cycle = 0;
        }


    }

    private void updateLogo(int x, int y) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ivAnimateLogo.setY((float) y);
                    ivAnimateLogo.setX((float) x);
                } catch (Exception e) {
                    Log.d(TAG, "run: ex:" + e.getMessage());
                }

            }
        });
    }
}