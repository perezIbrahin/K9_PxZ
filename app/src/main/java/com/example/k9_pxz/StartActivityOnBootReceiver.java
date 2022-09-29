package com.example.k9_pxz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartActivityOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())){
            Intent i=new Intent(context,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
