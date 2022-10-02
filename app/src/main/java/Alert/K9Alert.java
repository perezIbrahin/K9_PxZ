package Alert;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
            tv.setText(text);
            tv.setTextSize(26f);
        } else {
            Log.d(TAG, "SetImageView: null");
        }
    }

    //adding text
    private String addingText(String input, String newtext) {
        if (input.length() > 5) {
            return input + "\r\n" + newtext;
        }
        return newtext;
    }

    //alert dialig missing settings
    public void alertDialogMissingPara(String title, int status) {
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
            tvRev.setText(rev.APP_REV_PAGE_51);


            final TextView tvCheck0 = (TextView) promptsView.findViewById(R.id.tvCheck0);
            String text = "0";


            //
            for (int i = 0; i < 5; i++) {
                if (i == 0) {

                    if (!bitwise.isBitEnable(status, i)) {
                        //String newText = "Missing Frequency";
                        String newText = resources.getString(R.string.string_text_check_freq);
                        text = addingText(text, newText);
                        displayText(tvCheck0, text);
                    }
                } else if (i == 1) {

                    if (!bitwise.isBitEnable(status, i)) {
                        //String newText = "Missing Intensity";
                        String newText = resources.getString(R.string.string_text_check_int);
                        text = addingText(text, newText);
                        displayText(tvCheck0, text);
                    }
                } else if (i == 2) {
                    if (!bitwise.isBitEnable(status, i)) {
                        //String newText = "Missing Timer";
                        String newText = resources.getString(R.string.string_text_check_tim);
                        text = addingText(text, newText);
                        displayText(tvCheck0, text);
                    }


                } else if (i == 3) {
                    if (!bitwise.isBitEnable(status, i)) {
                        String newText = "Missing Select location -A";
                        text = addingText(text, newText);
                        displayText(tvCheck0, text);
                    }

                } else if (i == 4) {
                    if (!bitwise.isBitEnable(status, i)) {
                        String newText = "Missing Select location -B";
                        text = addingText(text, newText);
                        displayText(tvCheck0, text);
                    }

                }

            }


            AlertDialog alertDialog = alertDialogBuilder.create();
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnConfConfirm);

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
            Log.d(TAG, "alertDialogMissingPara: " + e.getMessage());
        }
    }

    //side rail dialog
    public void alertDialogSiderail(String title) {
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
                    .findViewById(R.id.tvTextDialogComplete);
            //set text
            if(title!=null){
                tvTextDialogSR.setText(title);
            }


            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnConfConfirm);
            final Button btnCancel = (Button) promptsView.findViewById(R.id.btnSRCancel);

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
                                //interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_SIDERAIL", util_dialog.LOCATION_CONFIRM_SIDERAIL);
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
                    .findViewById(R.id.tvTextDialogComplete);
            //set text
            if(title!=null){
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
                                interfaceSetupInfo.onItemSetupInfo("util_dialog.LOCATION_CONFIRM_SIDERAIL", util_dialog.LOCATION_CONFIRM_SIDERAIL);
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
            if(title!=null){
                tvTextDialogSR.setText(title);
            }

            //get buttons
            final Button btnConf = (Button) promptsView.findViewById(R.id.btnLockConfirm);
            final Button btnCancel = (Button) promptsView.findViewById(R.id.btnLockCancel);

            //set text buttons with language confirm
            if (btnConf!=null){
                btnConf.setText(confirm);
            }

            //set text buttons with language cancel
            if (btnCancel!=null){
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


}
