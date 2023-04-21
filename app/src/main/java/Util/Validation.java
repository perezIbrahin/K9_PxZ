package Util;

import android.util.Log;

public class Validation {
    private static final String TAG = "Validation";
    private String ALL_NUMBERS = "\\d+(?:\\.\\d+)?";

    public int IsUnknown = 0;
    public int IsPercussion = 1;
    public int IsPercussionVibration = 2;

    public Validation() {
    }

    //validate serial number
    public boolean validateSerialNumber(String value) {
        /* format
        (12345678910)
        ##x9v12345
        (12)
        ##:decimal
        (3)
        x:alfa
        (4)
        9:model
        (5)
        x:model vibration or percussion
        (6-10)
        #####:decimal

         */
        if (value != null) {
            Log.d(TAG, "validateSerialNumber: " + value.length());
            if (value.length() > 0) {
                //check length
                if (value.length() != 10) {
                    Log.d(TAG, "validateSerialNumber: failed need to be 10 characters");
                    return false;
                }
                //check first 2 numbers on position 1-2//year
                String getYear = value.substring(0, 2);//get substring
                Log.d(TAG, "validateSerialNumber:year: " + getYear);
                if (!getYear.matches(ALL_NUMBERS)) {
                    Log.d(TAG, "validateSerialNumber:wrong  Year not numbers");
                    return false;
                }

                //check character on position 3-get month
                String getMonth = value.substring(2, 3);//get substring
                Log.d(TAG, "validateSerialNumber:month: " + getMonth);
                if (getMonth.matches(ALL_NUMBERS)) {
                    Log.d(TAG, "validateSerialNumber: wrong  Month   numbers");
                    return false;
                }

                //check character on position 4-get model
                String getModel = value.substring(3, 4);//get substring
                Log.d(TAG, "validateSerialNumber:getModel: " + getModel);
                if (!getModel.equalsIgnoreCase("9")) {
                    Log.d(TAG, "validateSerialNumber: wrong Model   getModel ");
                    return false;
                }

                //check character on position 5-get model of k9
                String getK9Model = value.substring(4, 5);//get substring
                Log.d(TAG, "validateSerialNumber: wrong K9 model " + getK9Model);

                if (!getK9Model.equalsIgnoreCase("P")) {
                    if (!getK9Model.equalsIgnoreCase("V")) {
                        Log.d(TAG, "validateSerialNumber: wrong K9 Model ");
                        return false;
                    }
                }

                //check last 6 numbers
                String last6 = value.substring(5, 10);//get substring
                Log.d(TAG, "validateSerialNumber: last6:" + last6);
                if (!last6.matches(ALL_NUMBERS)) {
                    Log.d(TAG, "validateSerialNumber: last6 not numbers");
                    return false;
                }

                /*//check if 4 character is P or V
                String char4IsP = value.substring(3, 4);//get substring
                Log.d(TAG, "validateSerialNumber:char4IsP " + char4IsP);
                if ((!char4IsP.equalsIgnoreCase("p"))) {
                    if (!char4IsP.equalsIgnoreCase("v")) {
                        Log.d(TAG, "validateSerialNumber: char4IsP: false");
                        return false;
                    }
                    Log.d(TAG, "validateSerialNumber: char4IsP: true");
                    return true;
                }*/
                return true;
            }
            return false;
        }

        return false;
    }

    //validate if percussion or Percussion/Vibration
    public int validateIsPV(String value) {
        if (value != null) {
            Log.d(TAG, "validateIsPV: " + value.length());
            if (value.length() > 0) {
                //check length
                if (value.length() != 10) {
                    Log.d(TAG, "validateIsPV: failed need to be 10 characters");
                    return IsUnknown;
                }

                //check if 4 character is P (percussion)
                String char4IsP = value.substring(3, 4);//get substring
                Log.d(TAG, "validateSerialNumber:char4IsP " + char4IsP);
                if ((char4IsP.equalsIgnoreCase("p"))) {
                    Log.d(TAG, "validateSerialNumber: char4IsP: Percussion");
                    return IsPercussion;
                }

                //check if 4 character is  V (PercussionVibration)
                if ((char4IsP.equalsIgnoreCase("v"))) {
                    Log.d(TAG, "validateSerialNumber: char4IsP: PercussionVibration");
                    return IsPercussionVibration;
                }
                return IsUnknown;
            }
            return IsUnknown;
        }
        return IsUnknown;
    }

}
