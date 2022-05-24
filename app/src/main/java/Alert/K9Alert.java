package Alert;

import android.content.Context;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.k9_pxz.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Interface.InterfaceSetupInfo;
import Util.Configuration;
import Util.Message;
import Util.Util_Dialog;

public class K9Alert extends AppCompatActivity {
    private static final String TAG = "CustomAlert";
    InterfaceSetupInfo interfaceSetupInfo;
    Message message = new Message();
    Configuration configuration = new Configuration();
    Util_Dialog util_dialog=new Util_Dialog();

    public K9Alert(InterfaceSetupInfo interfaceSetupInfo, Context context) {
        this.interfaceSetupInfo = interfaceSetupInfo;
        this.context = context;
    }

    Context context;

    public void alertDialogLivePage(String title) {
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setView(promptsView);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setNegativeButton("Save",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_EXIT_THERAPY", util_dialog.LOCATION_EXIT_THERAPY);
                                }
                            })
                    .setPositiveButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }
                    );
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();


        }catch (Exception e){
            Log.d(TAG, "alertDialogLivePage: "+e.getMessage());
        }
    }

    public void alertDialogMissingPara(String title) {
        try {
           // LayoutInflater li = LayoutInflater.from(context);
            //View promptsView = li.inflate(R.layout.layout_dialog, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            //alertDialogBuilder.setView(promptsView);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }
                    );
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();


        }catch (Exception e){
            Log.d(TAG, "alertDialogMissingPara: "+e.getMessage());
        }
    }

    public void alertDialogSiderail(String title) {
        try {
            // LayoutInflater li = LayoutInflater.from(context);
            //View promptsView = li.inflate(R.layout.layout_dialog, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setIcon(R.drawable.ic_warning_black_24dp);
            //alertDialogBuilder.setView(promptsView);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setNegativeButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_SIDERAIL", util_dialog.LOCATION_CONFIRM_SIDERAIL);
                                }
                            })
                    .setPositiveButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }
                    );
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }catch (Exception e){
            Log.d(TAG, "alertDialogSiderail: "+e.getMessage());
        }
    }

    public void alertDialogConnectionFail(String title) {
        try {
            // LayoutInflater li = LayoutInflater.from(context);
            //View promptsView = li.inflate(R.layout.layout_dialog, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setIcon(R.drawable.ic_warning_black_24dp);
            //alertDialogBuilder.setView(promptsView);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setNegativeButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_CONN_FAILED", util_dialog.LOCATION_CONFIRM_CONN_FAILED);
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            Log.d(TAG, "alertDialogConnectionFail: show");
            alertDialog.show();
        }catch (Exception e){
            Log.d(TAG, "alertDialogConnectionFail: "+e.getMessage());
        }
    }

    public void alertDialogTherapyDone(String title) {
        try {
            // LayoutInflater li = LayoutInflater.from(context);
            //View promptsView = li.inflate(R.layout.layout_dialog, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setIcon(R.drawable.ic_notifications_blue_3_24dp);
            //alertDialogBuilder.setView(promptsView);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }
                    );

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            Log.d(TAG, "alertDialogConnectionFail: show");
            alertDialog.show();
        }catch (Exception e){
            Log.d(TAG, "alertDialogConnectionFail: "+e.getMessage());
        }
    }


}
