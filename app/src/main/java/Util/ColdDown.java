package Util;

import android.util.Log;

public class ColdDown {
    private static final String TAG = "ColdDown";
    public static final int MODULE1=1;
    public static final int MODULE2=2;
    public static final int MODULE3=3;
    public static final int MODULE4=4;
    public static final int MODULE5=5;

    public ColdDown() {
    }

    //set in memory e current time that transducer work
    public int setWorkingTimeTransd(int currentValue){
       if(currentValue>0){
           return currentValue;
       }
       return 0;
    }

    //get old tranaducer selected
    public int memOldTransducerSelected(int transdA,int transdB){
        if(transdA>0){


        }else if(transdB>0){

        }
        return 0;

    }
}
