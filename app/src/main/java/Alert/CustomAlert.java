package Alert;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.k9_pxz.R;
import com.example.k9_pxz.VibActivity;

import Interface.RecyclerViewClickInterface;
import Util.Navigation;
import Util.Safety;
import Util.Status;
import Util.Validation;

public class CustomAlert extends AppCompatActivity {
    private static final String TAG = "myAlert";
    Status status = new Status();
    Safety safety = new Safety();
    Navigation navigation = new Navigation();
    Validation validation = new Validation();

    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;

    public CustomAlert(RecyclerViewClickInterface recyclerViewClickInterface, Context context) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.context = context;
    }

    public CustomAlert(RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    //Alert Dialog UUID
    public void AlertDialogUUID(Context context, String title, String message) {
        Status status = new Status();
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);//, R.style.Theme_AppCompat_Light
            builder.setTitle(title);
            builder.setIcon(R.drawable.ic_baseline_warning_24);
            builder.setMessage(message)
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
            Log.d(TAG, "AlertDialog: Exception" + e.getMessage());
        }
    }

    //Check for Transducers
    public void alertDialogCheckSelectedTransd(Context context) {
        Log.d(TAG, "alertDialogCheckSelectedTransd: ");
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);//, R.style.Theme_AppCompat_Light
            builder.setTitle(R.string.alert_Title);
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage("Check transducers! Need to match A# with B#.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            TextView textView = ((TextView) alert.findViewById(android.R.id.message));
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(40);
            textView.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            Log.d(TAG, "alertDialogBeforeStart: Exception" + e);
        }
    }
/*
    //get password
    public void entryPass() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.searchprompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.user_input);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Go",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // DO THE METHOD HERE WHEN PROCEED IS CLICKED
                                String user_text = (userInput.getText()).toString();

                                // CHECK FOR USER'S INPUT
                                if (user_text.equals("oeg")) {
                                    Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
                                    Search_Tips(user_text);

                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "The password you have entered is incorrect." + " \n" + "Please try again";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", null);
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
});

        }
        }*/


    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.searchprompt, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.user_input);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Enter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String user_text = (userInput.getText()).toString();

                                /** CHECK FOR USER'S INPUT **/
                                if (user_text.equals("oeg")) {
                                    Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
                                    Search_Tips(user_text);
                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showDialog();
                                        }
                                    });
                                    builder.create().show();

                                }
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
    }

    //publish a dialog asking pass for link
    public void showDialogLink(String pass) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.searchprompt, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.user_input);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Enter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String user_text = (userInput.getText()).toString();

                                /** CHECK FOR USER'S INPUT **/
                                if (user_text.equals(pass)) {
                                    Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
                                    //approvedGoLinkPage();
                                    Search_Tips(pass);
                                    //Search_Tips(user_text);
                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showDialog();
                                        }
                                    });
                                    builder.create().show();

                                }
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
    }

    //Get the serial number
    public void showDialogSerialLink(String uuid) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.search_serial_number, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.user_input_serial);

        final TextView tvLinkBle= (TextView) promptsView
                .findViewById(R.id.tvLinkBle);
        if(uuid!=null){
            tvLinkBle.setText(uuid);
            Log.d(TAG, "onClick: uuid"+uuid);
        }else{
            Log.d(TAG, "onClick: uuid null");
            tvLinkBle.setText("00:00:00:00:00");
        }

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Enter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String user_text = (userInput.getText()).toString();


                                if (!user_text.isEmpty()) {
                                    saveSerialNumber(user_text);
                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showDialog();
                                        }
                                    });
                                    builder.create().show();
                                }
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
    }

    //validation data failed
    private void showAlertVailadionDataFailed() {
        Log.d(TAG, "showAlertVailadionDataFailed: ");
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.validation_data_failed, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        /*final EditText userInput = (EditText) promptsView
                .findViewById(R.id.user_input_serial);*/

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
    }

    //validation data failed
    public void showAlertInfo(String serial) {
        Log.d(TAG, "showAlertVailadionDataFailed: ");
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.about, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final TextView tvSerialNumber = (TextView) promptsView
                .findViewById(R.id.tvAboutSerialNumber);
        if(serial!=null){
            tvSerialNumber.setText(serial);
        }else{
            tvSerialNumber.setText("unknown");
        }



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
    }


    // Check Password
    private String Search_Tips(String text) {
        if (text != null) {
            Log.d(TAG, "Search_Tips: " + text);
            if (text.equals(safety.PASS_QC)) {
                recyclerViewClickInterface.onItemPostSelect(1, navigation.NAV_LINK);
            } else if (text.equals(safety.PASS_OP)) {
                //
            } else if (text.equals(safety.PASS_SUP)) {
                //
            }
            return text;
        } else {
            return "0";
        }

    }

    //serial number
    private void saveSerialNumber(String message) {
        if (validation.validateSerialNumber(message)) {
            //validation ok
            recyclerViewClickInterface.onItemPostSelect(status.STATUS_SERIAL_LINK, message);
            Log.d(TAG, "saveSerialNumber: " + message);
        } else {
            //validation failed
            showAlertVailadionDataFailed();
        }
    }


    //
    private void approvedGoLinkPage() {

    }


}
