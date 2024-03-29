package Alert;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.k9_pxz.R;

import Interface.InterfaceSetupInfo;
import Util.Bitwise;
import Util.Configuration;
import Util.Message;
import Util.Rev;
import Util.Util_Dialog;

public class K9Alert extends AppCompatActivity {
    private static final String TAG = "CustomAlert";
    InterfaceSetupInfo interfaceSetupInfo;
    Message message = new Message();
    Configuration configuration = new Configuration();
    Util_Dialog util_dialog = new Util_Dialog();
    Rev rev = new Rev();
    CountDownTimer countDownTimer;
    Context context;
    //to get language
    String language = "en";
    Context contextLang;
    //user internal
    private Context contextLoc;
    private Resources resources;

    public K9Alert(InterfaceSetupInfo interfaceSetupInfo, Context context) {
        this.interfaceSetupInfo = interfaceSetupInfo;
        this.context = context;
        contextLang = context;
    }

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


        } catch (Exception e) {
            Log.d(TAG, "alertDialogLivePage: " + e.getMessage());
        }
    }

    //notifications about selected parametres
    private void displayText(TextView tv, String text) {
        if (tv != null) {
            Log.d(TAG, "displayText: " + text);
            tv.setText(text);
            tv.setTextSize(26f);
        } else {
            Log.d(TAG, "SetImageView: null");
        }
    }

    //adding text
    private String addingText(String input, String newtext) {
        if (input.length() > 5) {
            Log.d(TAG, "addingText: input:" + input + ". new text:" + newtext);
            return input + "\r\n" + newtext;
        }
        Log.d(TAG, "addingText: just new text" + newtext);
        return newtext;
    }

    //alert dialig missing settings
    public void alertDialogMissingPara(String title, int status, String textfreq, String textInt, String textTime, String textTrA, String textTrB, String btnConfirm) {
        try {
            //get bit active
            Bitwise bitwise = new Bitwise();

            //
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_check_configuration, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            //alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setView(promptsView);

            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDialogConfRev);
            //set text revision
            if(tvRev!=null){
                tvRev.setText(rev.APP_REV_PAGE_51);
            }


            ////set the revision
            final TextView tvTitle = (TextView) promptsView.findViewById(R.id.tvTextDialogSettTitle);
            if(tvTitle!=null){
                tvTitle.setText(title);
            }


            //Add missing parameteres
            final TextView tvCheck0 = (TextView) promptsView.findViewById(R.id.tvCheck0);
            String text = "0";

            //
            Log.d(TAG, "alertDialogMissingPara: status:" + status);

            //
            for (int i = 0; i < 5; i++) {
                if (i == 0) {

                    if (!bitwise.isBitEnable(status, i)) {//freq
                        if (textfreq != null) {
                            String newText = textfreq;
                            text = addingText(text, newText);
                            displayText(tvCheck0, text);
                        }

                    }
                } else if (i == 1) {
                    if (!bitwise.isBitEnable(status, i)) {//int
                        if (textInt != null) {
                            String newText = textInt;
                            text = addingText(text, newText);
                            displayText(tvCheck0, text);
                        }
                    }
                } else if (i == 2) {
                    if (!bitwise.isBitEnable(status, i)) {//timer
                        //String newText = "Missing Timer";
                        if (textTime != null) {
                            String newText = textTime;
                            text = addingText(text, newText);
                            displayText(tvCheck0, text);
                        }
                    }
                } else if (i == 3) {
                    if (!bitwise.isBitEnable(status, i)) {//tr-a
                        if (textTrA != null) {
                            String newText = textTrA;
                            text = addingText(text, newText);
                            displayText(tvCheck0, text);
                        }

                    }

                } else if (i == 4) {
                    if (!bitwise.isBitEnable(status, i)) {//tr-b
                        if (textTrB != null) {
                            String newText = textTrB;
                            text = addingText(text, newText);
                            displayText(tvCheck0, text);
                        }
                    }

                }

            }

            AlertDialog alertDialog = alertDialogBuilder.create();

            //button confirm
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnConfConfirm);
            //set name of button with language
            if(btnConf!=null){
                btnConf.setText(btnConfirm);
            }


            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_SIDERAIL", util_dialog.LOCATION_CONFIRM_SIDERAIL);
                            alertDialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogSiderail: ex:" + e.getMessage());
                }
            }

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
            //AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();


        } catch (Exception e) {
            Log.d(TAG, "alertDialogMissingPara: ex " + e.getMessage());
        }
    }

    //side rail dialog
    public void alertDialogSiderail(String title, String confirm, String cancel) {
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog_siderail, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //get text view revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgSrRev);
            //set text revision
            tvRev.setText(rev.APP_REV_PAGE_50);
            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogSettTitle);
            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }


            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnConfConfirm);

            final Button btnCancel = (Button) promptsView.findViewById(R.id.btnSRCancel);

            //set text buttons with language confirm
            if (btnConf != null) {
                btnConf.setText(confirm);
            }

            //set text buttons with language cancel
            if (btnCancel != null) {
                btnCancel.setText(cancel);
            }

            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_SIDERAIL", util_dialog.LOCATION_CONFIRM_SIDERAIL);
                            alertDialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogSiderail: ex:" + e.getMessage());
                }
            }

            //button cancel
            if (btnCancel != null) {
                try {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "alertDialogSiderail: ex:" + e.getMessage());
                }
            }


            //alertDialogBuilder.setView(promptsView);

            // set dialog message
           /* alertDialogBuilder
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
                    );*/

            // show it
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogSiderail: " + e.getMessage());
        }
    }

    //Burn process end
    public void alertDialogBurnEnd(String title, String confirm, String cancel) {
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_burn_process_end, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //get text view revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgBurnRev);
            //set text revision
            tvRev.setText(rev.APP_REV_PAGE_61);
            //get text view for dialog
            final TextView tvTextDialog = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogBurnTitle);
            //set text
            if (title != null) {
                tvTextDialog.setText(title);
            }


            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnConfConfirm);

            final Button btnCancel = (Button) promptsView.findViewById(R.id.btnSRCancel);

            //set text buttons with language confirm
            if (btnConf != null) {
                btnConf.setText(confirm);
            }

            //set text buttons with language cancel
            if (btnCancel != null) {
                btnCancel.setText(cancel);
            }

            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_BURNING_DONEL", util_dialog.LOCATION_CONFIRM_BURNING_DONE);
                            alertDialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogBurning: ex:" + e.getMessage());
                }
            }

            //button cancel
            if (btnCancel != null) {
                try {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "alertDialogBurning: ex:" + e.getMessage());
                }
            }


            //alertDialogBuilder.setView(promptsView);

            // set dialog message
           /* alertDialogBuilder
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
                    );*/

            // show it
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogSiderail: " + e.getMessage());
        }
    }

    //connection fail
    public void alertDialogConnectionFail(String title) {
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog_conection_lost, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgConLostRev);
            tvRev.setText(rev.APP_REV_PAGE_53);
            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnConLostConfirm);
            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //send command
                            try {
                                //interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_ACK_CON_FAIL", util_dialog.LOCATION_ACK_CON_FAIL);
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                            //remove dialog
                            try {
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                }
            }

            // show it
            Log.d(TAG, "alertDialogConnectionFail: show");
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogConnectionFail: " + e.getMessage());
        }
    }

    //therapy complete
    public void alertDialogTherapyDone(String title, String textBtnConfirm) {
        try {
            Log.d(TAG, "alertDialogTherapyDone: ");
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_therapy_complete, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgCompRev);
            tvRev.setText(rev.APP_REV_PAGE_52);

            //display timer
            final TextView tvTimer = (TextView) promptsView
                    .findViewById(R.id.tvCompTimer);

            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogSettTitle);
            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }

            Log.d(TAG, "alertDialogTherapyDone: 2");
            //timer
            countDownTimer = new CountDownTimer(20000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        Log.d(TAG, "alertDialogTherapyDone onTick: " + millisUntilFinished / 100);
                        if (tvTimer != null) {
                            Log.d(TAG, "alertDialogTherapyDone onTick: " + millisUntilFinished / 100);
                            String display = String.valueOf(millisUntilFinished / 100);
                            tvTimer.setText("Dialog wil close in: " + display + " seconds.");
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                    }
                }

                @Override
                public void onFinish() {
                    try {
                        alertDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                    }
                }
            }.start();


            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnCompConfirm);
            if (btnConf != null) {
                btnConf.setText(textBtnConfirm);
            }


            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (countDownTimer != null) {
                                    countDownTimer = null;
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                            //send command
                            try {
                                interfaceSetupInfo.onItemSetupInfo("util_dialog.THERAPY_DONE", util_dialog.THERAPY_DONE);
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                            //remove dialog
                            try {
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                }
            }

            // alertDialogBuilder.setView(promptsView);
            // alertDialogBuilder.setTitle(title);
           /* alertDialogBuilder.setIcon(R.drawable.ic_notifications_blue_3_24dp);
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
*/
            // create alert dialog

            // show it
            Log.d(TAG, "alertDialogConnectionFail: show");
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogConnectionFail: " + e.getMessage());
        }
    }

    //lock system
    public void alertDialogLock(String title, String confirm, String cancel) {
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog_p53_lock, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgLockRev);
            tvRev.setText(rev.APP_REV_PAGE_54);


            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogLock);
            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }

            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnLockConfirm);
            final Button btnCancel = (Button) promptsView.findViewById(R.id.btnLockCancel);

            //set text buttons with language confirm
            if (btnConf != null) {
                btnConf.setText(confirm);
            }

            //set text buttons with language cancel
            if (btnCancel != null) {
                btnCancel.setText(cancel);
            }

            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_LOCK", util_dialog.LOCATION_CONFIRM_LOCK);
                            alertDialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogLock: ex:" + e.getMessage());
                }
            }

            //button cancel
            if (btnCancel != null) {
                try {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "alertDialogLock: ex:" + e.getMessage());
                }
            }

            // show it
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogSiderail: " + e.getMessage());
        }
    }

    //aboutsystem
    public void alertDialogAbout(String title, String confirm) {
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog_p53_lock, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgLockRev);
            tvRev.setText(rev.APP_REV_PAGE_54);


            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogLock);
            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }

            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnLockConfirm);


            //set text buttons with language confirm
            if (btnConf != null) {
                btnConf.setText(confirm);
            }



            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_LOCK", util_dialog.LOCATION_CONFIRM_LOCK);
                            alertDialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogLock: ex:" + e.getMessage());
                }
            }

            // show it
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogSiderail: " + e.getMessage());
        }
    }

    //alarm system emergency stop
    public void alertDialogSystemEmergencyStop(String title, String textBtnConfirm) {
        try {
            Log.d(TAG, "alertDialogSystemEmergencyStop: ");
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_emergency_stop, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvEmergStopRev);
            tvRev.setText(rev.APP_REV_PAGE_55);


            //set emergency stop
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvEmergencyStop);

            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }

            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnEmegStopConfirm);

            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //send command
                            try {
                                interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_EMERGENCY_STOP_CONFIRM", util_dialog.LOCATION_EMERGENCY_STOP_CONFIRM);
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                            //remove dialog
                            try {
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                }
            }


            // show it
            Log.d(TAG, "alertDialogConnectionFail: show");
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogConnectionFail: " + e.getMessage());
        }
    }

    //alarm under current
    public void alertDialogUnderCurrent(String title, String textBtnConfirm) {
        try {
            Log.d(TAG, "alertDialogUnderCurrent: ");
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_alarm_unde_current, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvUcRev);
            tvRev.setText(rev.APP_REV_PAGE_56);


            //set emergency stop
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvUnderCurrent);

            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }

            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnUcConfirm);

            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //send command
                            try {
                                interfaceSetupInfo.onItemSetupInfo("util_dialog.UNDER_CURRENT_CONFIRM", util_dialog.UNDER_CURRENT_CONFIRM);
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogUNDER_CURRENT_CONFIRM: ex:" + e.getMessage());
                            }
                            //remove dialog
                            try {
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogUNDER_CURRENT_CONFIRM: ex:" + e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                }
            }


            // show it
            Log.d(TAG, "alertDialogUNDER_CURRENT_CONFIRMFail: show");
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogConnectionFail: " + e.getMessage());
        }
    }

    //alarm over current
    public void alertDialogOverCurrent(String title, String textBtnConfirm) {
        try {
            Log.d(TAG, "alertDialogUnderCurrent: ");
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_alarm_over_current, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvOcRev);
            tvRev.setText(rev.APP_REV_PAGE_57);


            //set emergency stop
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvOverCurrent);

            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }

            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnOcConfirm);

            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //send command
                            try {
                                interfaceSetupInfo.onItemSetupInfo("util_dialog.OVER_CURRENT_CONFIRM", util_dialog.OVER_CURRENT_CONFIRM);
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                            //remove dialog
                            try {
                                alertDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogTherapyDone: ex:" + e.getMessage());
                }
            }


            // show it
            Log.d(TAG, "alertDialogConnectionFail: show");
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogConnectionFail: " + e.getMessage());
        }
    }

    //Auto sleep mode
    public void alertDialogAutoSleep(String title, String confirm, String cancel) {
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog_auto_sleep, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgSleepRev);
            tvRev.setText(rev.APP_REV_PAGE_58);


            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogSleep);
            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }

            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnSleepConfirm );
            final Button btnCancel = (Button) promptsView.findViewById(R.id.btnSleepCancel);

            //set text buttons with language confirm
            if (btnConf != null) {
                btnConf.setText(confirm);
            }

            //set text buttons with language cancel
            if (btnCancel != null) {
                btnCancel.setText(cancel);
            }

            //button confirm
            if (btnConf != null) {
                try {
                    btnConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_AUTO_SLEEP", util_dialog.LOCATION_AUTO_SLEEP);
                            alertDialog.dismiss();*/

                            alertDialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "alertDialogAutoSleep: ex:" + e.getMessage());
                }
            }

            //button cancel
            if (btnCancel != null) {
                try {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "alertDialogAutoSleep: ex:" + e.getMessage());
                }
            }

            // show it
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogAutoSleep: " + e.getMessage());
        }
    }

    //After stop Wait
    public void alertDialogStoppingWait(String title, String confirm, String cancel) {
        CountDownTimer countDownTimer ;

        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog_wait, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setCancelable(false);

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgWaitRev);
            tvRev.setText(rev.APP_REV_PAGE_59);


            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogWait);
            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }
            //text timer
            final TextView tvTextDialogTimer = (TextView) promptsView
                    .findViewById(R.id.tvCounterWait);

            //prograess bar
            final ProgressBar progressBar=(ProgressBar) promptsView.findViewById(R.id.pbWait);
            progressBar.setMax(5000);


            countDownTimer=new CountDownTimer(5000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, "onTick  countDownTimer: "+millisUntilFinished/1000);
                    int value=(int)millisUntilFinished/1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(tvTextDialogTimer!=null){
                                if(value>0){
                                    Log.d(TAG, "onTick  countDownTimer int: "+value);
                                    tvTextDialogTimer.setText((String.valueOf(value)));
                                    progressBar.setProgress(value);
                                }else{
                                    Log.d(TAG, "onTick  countDownTimer int: "+"0");
                                    tvTextDialogTimer.setText("0");
                                }
                            }
                        }
                    });
                }

                @Override
                public void onFinish() {
                    alertDialog.dismiss();
                }
            }.start();

            // show it
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogStopping wait: " + e.getMessage());
        }
    }

    //After stop Wait
    public void alertDialogLoading(String title, String confirm, String cancel) {
        CountDownTimer countDownTimer ;

        try {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.layout_dialog_loading, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);
            alertDialogBuilder.setCancelable(false);

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            //set the revision
            final TextView tvRev = (TextView) promptsView
                    .findViewById(R.id.tvDilgLoadRev);
            tvRev.setText(rev.APP_REV_PAGE_60);


            //get text view for dialog
            final TextView tvTextDialogSR = (TextView) promptsView
                    .findViewById(R.id.tvTextDialogLoad);
            //set text
            if (title != null) {
                tvTextDialogSR.setText(title);
            }
            //text timer
            final TextView tvTextDialogTimer = (TextView) promptsView
                    .findViewById(R.id.tvCounterLoad);


            countDownTimer=new CountDownTimer(7000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d(TAG, "onTick  countDownTimer: "+millisUntilFinished/1000);
                    int value=(int)millisUntilFinished/1000;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(tvTextDialogTimer!=null){
                                if(value>0){
                                    Log.d(TAG, "onTick  countDownTimer int: "+value);
                                    tvTextDialogTimer.setText((String.valueOf(value)));
                                }else{
                                    Log.d(TAG, "onTick  countDownTimer int: "+"0");
                                    tvTextDialogTimer.setText("0");
                                }
                            }
                        }
                    });
                }

                @Override
                public void onFinish() {
                    alertDialog.dismiss();
                }
            }.start();

            // show it
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "alertDialogStopping wait: " + e.getMessage());
        }
    }
}
