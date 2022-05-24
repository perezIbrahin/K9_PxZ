package Util;

import android.util.Log;

public class Cooling {
    private static final String TAG = "Cooling";
    private SetPointsBluetooth setPointsBluetooth = new SetPointsBluetooth();
    private ControlGUI controlGUI = new ControlGUI();

    private int Factor = 10;

    //check module
    public int checkCoolingModules(int transdA, int transdB, boolean currentLimit) {
        try {
            if (checkModA(transdA) == checkModB(transdB)) {
                //same module
                return (checkModA(transdA));
            } else {
                //different module

                //check if the system is on the limit. if it can not turn both system
                if (true) {//!currentLimit
                    return ((checkModB(transdB) * Factor) + (checkModA(transdA)));
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "checkCoolingModules: ex:" + e.getMessage());
        }
        return -1;
    }

    //
    public int getModA(int value) {
        //return the unit portion
        int ret = 0;
        try {
            int base = value;
            int myValue = value / 10;
            Log.d(TAG, "getMod1: myvalue:" + myValue);
            switch (myValue) {
                case 1:
                    ret = base - 10;
                    break;
                case 2:
                    ret = base - 20;
                    break;
                case 3:
                    ret = base - 30;
                    break;
                case 4:
                    ret = base - 40;
                    break;
                case 5:
                    ret = base - 50;
                    break;
                default:
                    ret =0;
                    break;
            }
            return ret;
        } catch (Exception e) {
            Log.d(TAG, "getMod1: ex:" + e.getMessage());
        }
        return 0;

    }

    public int getModB(int value) {
        //return the unit portion
        int ret = 0;
        try {
           ret = value / 10;
            return ret;
        } catch (Exception e) {
            Log.d(TAG, "getMod1: ex:" + e.getMessage());
        }
        return 0;
    }


    //check location transducer
    private int checkModA(int transdA) {
        try {
            if (transdA == setPointsBluetooth.INT_BLE_SP_TRA1) {
                return controlGUI.POS1;
            } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA2) {
                return controlGUI.POS2;
            } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA3) {
                return controlGUI.POS3;
            } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA4) {
                return controlGUI.POS4;
            } else if (transdA == setPointsBluetooth.INT_BLE_SP_TRA5) {
                return controlGUI.POS5;
            } else {
                return controlGUI.POS0;
            }
        } catch (Exception e) {
            Log.d(TAG, "checkModA: ex:" + e.getMessage());
        }
        return controlGUI.POS0;
    }

    //check location transducer
    private int checkModB(int transdB) {
        try {
            if (transdB == setPointsBluetooth.INT_BLE_SP_TRB1) {
                return controlGUI.POS1;
            } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB2) {
                return controlGUI.POS2;
            } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB3) {
                return controlGUI.POS3;
            } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB4) {
                return controlGUI.POS4;
            } else if (transdB == setPointsBluetooth.INT_BLE_SP_TRB5) {
                return controlGUI.POS5;
            } else {
                return controlGUI.POS0;
            }
        } catch (Exception e) {
            Log.d(TAG, "checkModB: ex:" + e.getMessage());
        }
        return controlGUI.POS0;
    }
}
