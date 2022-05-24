package Util;

import android.util.Log;

public class Util_timer {
    private static final String TAG = "Util_timer";
    public Util_timer() {
    }

    //convert timer to setpoint
    public long timerCalc(int input) {
        long ret = 0;
        long offset = 60000;
        if (input > 0) {
            ret = (long) input * offset;
        }
        return ret;
    }

    //convert  int to str
    public String convertIntTimeStr(int input){
        String baseSec=":00";
        String baseMin="00";
        try{
            if(input>0){
                baseMin=String.valueOf(input);
                return  baseMin+baseSec;
            }else{
                return  baseMin+baseSec;
            }
        }catch (Exception e){
            Log.d(TAG, "convertIntTimeStr: ex:"+e.getMessage());
        }
        return  baseMin+baseSec;
    }

}
