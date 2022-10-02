package com.example.k9_pxz;

import android.app.Application;
import android.content.Context;

import Util.LocaleHelper;

public class Home extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
