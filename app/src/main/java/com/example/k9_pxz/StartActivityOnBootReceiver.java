package com.example.k9_pxz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StartActivityOnBootReceiver extends BroadcastReceiver {
    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        String message = "BootDeviceReceiver onReceive, action is " + action;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        if(Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())){
            Intent i=new Intent(context,MainActivity.class);
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
            Log.d(TAG_BOOT_BROADCAST_RECEIVER, "------ tried to launch MainActivity -------"); // this is printed

            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
            //startServiceByAlarm(context);
        }
    }

    /* Start RunAfterBootService service directly and invoke the service every 10 seconds. */
    private void startServiceDirectly(Context context)
    {
        try {
            while (true) {
                String message = "BootDeviceReceiver onReceive start service directly.";

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                Log.d(TAG_BOOT_BROADCAST_RECEIVER, message);

                // This intent is used to start background service. The same service will be invoked for each invoke in the loop.
                Intent startServiceIntent = new Intent(context, MainActivity.class);
                context.startService(startServiceIntent);

                // Current thread will sleep one second.
                Thread.sleep(10000);
            }
        }catch(InterruptedException ex)
        {
            Log.e(TAG_BOOT_BROADCAST_RECEIVER, ex.getMessage(), ex);
        }
    }


    /* Create an repeat Alarm that will invoke the background service for each execution time.
     * The interval time can be specified by your self.  */
    private void startServiceByAlarm(Context context)
    {
        // Get alarm manager.
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Create intent to invoke the background service.
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long startTime = System.currentTimeMillis();
        long intervalTime = 60*1000;

        String message = "Start service use repeat alarm. ";

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        Log.d(TAG_BOOT_BROADCAST_RECEIVER, message);

        // Create repeat alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
    }

}
