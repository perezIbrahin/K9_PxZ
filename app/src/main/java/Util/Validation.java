package Util;

import android.util.Log;

public class Validation {
    private static final String TAG = "Validation";
    private String ALL_NUMBERS="\\d+(?:\\.\\d+)?";

    public Validation() {
    }

    //validate serial number
    public boolean validateSerialNumber(String value){
        if(value!=null){
            Log.d(TAG, "validateSerialNumber: "+value.length());
            if(value.length()>0){
                //check length
                if (value.length()!=10){
                    Log.d(TAG, "validateSerialNumber: failed need to be 10 characters");
                    return  false;
                }
                //check last 6 numbers
                String last6=value.substring(4,10);//get substring
                Log.d(TAG, "validateSerialNumber: last6:"+last6);
                if(!last6.matches(ALL_NUMBERS)){
                    Log.d(TAG, "validateSerialNumber: last6 not numbers");
                    return false;
                }
                //check first 2 numbers on position 1-2
                String first2=value.substring(0,2);//get substring
                Log.d(TAG, "validateSerialNumber:first2: "+first2);
                if(!first2.matches(ALL_NUMBERS)){
                    Log.d(TAG, "validateSerialNumber:first2 not numbers");
                    return false;
                }

                //check character on position 3-4
                String first34=value.substring(2,4);//get substring
                Log.d(TAG, "validateSerialNumber:first34: "+first34);
                if(first34.matches(ALL_NUMBERS)){
                    Log.d(TAG, "validateSerialNumber: first34   numbers");
                    return false;
                }

                return true;
            }
            return false;
        }

        return  false;
    }

}
