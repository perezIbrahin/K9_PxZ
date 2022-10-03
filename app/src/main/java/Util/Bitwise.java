package Util;

import android.util.Log;

public class Bitwise {
    private static final String TAG = "Bitwise";

    public Bitwise() {
    }

    //
    private int getBitPos(boolean input, int status, int valueSet, int valueReset) {
        int newStatus = 0;
        if (input) {
            //set
            newStatus = status | valueSet;
            return newStatus;
        }
        //reset
        newStatus = status & valueReset;
        return newStatus;
    }

    //
    private int getPos(int status,int bitPosition, boolean bit0, boolean bit1, boolean bit2, boolean bit3, boolean bit4, boolean bit5) {
        int newStatus2 = status;

        //reset
        int resetBit0 = 0xFF;
        int resetBit1 = 0xFD;
        int resetBit2 = 0xFB;
        int resetBit3 = 0xF7;
        int resetBit4 = 0xEF;
        int resetBit5 = 0xDF;
        //set
        int setBit0 = 0x01;
        int setBit1 = 0x02;
        int setBit2 = 0x04;
        int setBit3 = 0x08;
        int setBit4 = 0x10;
        int setBit5 = 0x20;

        //
        if (bitPosition == 0) {//bit0
            newStatus2 = getBitPos(bit0, newStatus2, setBit0, resetBit0);
        } else if (bitPosition == 1) {//bit1
            newStatus2 = getBitPos(bit1, newStatus2, setBit1, resetBit1);
        } else if (bitPosition == 2) {//bit2

            newStatus2 = getBitPos(bit2, newStatus2, setBit2, resetBit2);
        } else if (bitPosition == 3) {//bit3
            newStatus2 = getBitPos(bit3, newStatus2, setBit3, resetBit3);
        } else if (bitPosition == 4) {//bit4
            newStatus2 = getBitPos(bit4, newStatus2, setBit4, resetBit4);
        } else if (bitPosition == 5) {//bit5
            newStatus2 = getBitPos(bit5, newStatus2, setBit5, resetBit5);
        }
        return newStatus2;
    }


    //set reset bit operation
    public int operationBit( boolean bit0, boolean bit1, boolean bit2, boolean bit3, boolean bit4, boolean bit5) {
        int newStatus2 = 0;
        //newStatus2 = status;
        Log.d(TAG, "operationBit: bit2:"+bit2);
        //
        for (int i = 0; i < 5; i++) {
            newStatus2=getPos(newStatus2,i, bit0,  bit1,  bit2,  bit3,  bit4,  bit5);
        }
        Log.d(TAG, "operationBit:status: "+newStatus2);
        return newStatus2;
    }

    //get bit is in one
    public boolean isBitEnable(int status, int position){
        if((status & (1<<position))>0){
            Log.d(TAG, "isBitEnable: pos:"+position+". true");
            return  true;
        }
        Log.d(TAG, "isBitEnable: pos:"+position+". false");
        return false;
    }


}
