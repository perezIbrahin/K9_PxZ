package Util;

import android.util.Log;

public class IntToArray {
    private static final String TAG = "IntToArray";

    public IntToArray() {
    }

    public  static byte[] intToByteArray(int a){
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        Log.d(TAG, "intToByteArray: ret[3] "+ ret[3]);
        Log.d(TAG, "intToByteArray: ret[2] "+ ret[2]);
        Log.d(TAG, "intToByteArray: ret[1] "+ ret[1]);
        Log.d(TAG, "intToByteArray: ret[0] "+ ret[0]);
        return ret;
    }
}
