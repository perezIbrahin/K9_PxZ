package Alert;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.k9_pxz.K9Activity;
import com.example.k9_pxz.R;

import Interface.RecyclerViewClickInterface;
import Util.Status;

public class AlertCustomDialog extends AppCompatActivity {
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;

    public AlertCustomDialog(RecyclerViewClickInterface recyclerViewClickInterface, Context context) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.context = context;
    }

    public AlertCustomDialog(RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public void  alertDialogUUID(String uuid){
        //final String getUUID = uuid;
        Status status=new Status();
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);//, R.style.Theme_AppCompat_Light
            builder.setTitle(R.string.alert_Title_uuid);
            builder.setIcon(R.drawable.ic_baseline_warning_24);
            builder.setMessage("Do you want to save it? " + "" + uuid)
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            recyclerViewClickInterface.onItemPostSelect(status.SAVE_NEW_UUID,
                                    status.DIALOG_ALERT_UUID);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           // mBLEScanner.startScan(mScanCallback);
                            recyclerViewClickInterface.onItemPostSelect(status.START_SCAN_BLE,
                                    status.DIALOG_ALERT_UUID);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            //
            TextView textView = ((TextView) alert.findViewById(android.R.id.message));
            //textView.setTextColor(Color.LTGRAY);
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            Log.d(TAG, "alertDialogBeforeStart: Exception" + e);
        }
    }

}
