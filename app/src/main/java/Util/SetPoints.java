package Util;

public class SetPoints {
    public  int SP_BLE_MODE_NONE = 0;
    public  int SP_BLE_MODE_PERC = 04;
    public  int SP_BLE_MODE_VIB = 05;
    //frequency string
    public final String SP_BLE_SP_FREQ1 = "16";
    public final String SP_BLE_SP_FREQ2 = "17";
    public final String SP_BLE_SP_FREQ3 = "18";
    public final String SP_BLE_SP_FREQ4 = "19";
    public final String SP_BLE_SP_FREQ5 = "20";
    //Intensity string
    public final String SP_BLE_SP_INT1 = "32";
    public final String SP_BLE_SP_INT2 = "33";
    public final String SP_BLE_SP_INT3 = "34";
    public final String SP_BLE_SP_INT4 = "35";
    public final String SP_BLE_SP_INT5 = "36";
    //Time string
    public final String SP_BLE_SP_TIME1 = "48";
    public final String SP_BLE_SP_TIME2 = "49";
    public final String SP_BLE_SP_TIME3 = "50";
    public final String SP_BLE_SP_TIME4 = "51";
    public final String SP_BLE_SP_TIME5 = "52";

    //Transducers Left string
    public final String SP_BLE_SP_TRA1 = "64"; //64
    public final String SP_BLE_SP_TRA2 = "65";
    public final String SP_BLE_SP_TRA3 = "66";
    public final String SP_BLE_SP_TRA4 = "67";
    public final String SP_BLE_SP_TRA5 = "68";

    //Transducers Right
    public final String SP_BLE_SP_TRB1 = "80";//80
    public final String SP_BLE_SP_TRB2 = "81";
    public final String SP_BLE_SP_TRB3 = "82";
    public final String SP_BLE_SP_TRB4 = "83";
    public final String SP_BLE_SP_TRB5 = "84";
    //command string
    public final String SP_BLE_CMD_START = "97";
    public final String SP_BLE_CMD_STOP = "101";

    public final String SP_BLE_CMD_TOTAL_BODY_PERC = "94";
    public final String SP_BLE_CMD_TOTAL_BODY_VIB = "95";
    //--------------------------------------------
    public final int INT_BLE_SP_CLEAN = -1;

    //Frequency
    public final int INT_BLE_SP_FREQ1 = 10;
    public final int INT_BLE_SP_FREQ2 = 11;
    public final int INT_BLE_SP_FREQ3 = 12;
    public final int INT_BLE_SP_FREQ4 = 13;
    public final int INT_BLE_SP_FREQ5 = 14;
    public final int INT_BLE_SP_FREQ_MAX = 15;
    public final int INT_BLE_SP_FREQ_NONE = 19;
    //Intensity in voltage
    public final int INT_BLE_SP_INT1 = 20;//20
    public final int INT_BLE_SP_INT2 = 21;//21
    public final int INT_BLE_SP_INT3 = 22;//22
    public final int INT_BLE_SP_INT4 = 23;//23
    public final int INT_BLE_SP_INT5 = 24;//24
    public final int INT_BLE_SP_INT_MAX = 25;
    public final int INT_BLE_SP_INT_NONE = 29;
    //Time
    public final int INT_BLE_SP_TIME1 = 30;
    public final int INT_BLE_SP_TIME2 = 31;
    public final int INT_BLE_SP_TIME3 = 32;
    public final int INT_BLE_SP_TIME4 = 33;
    public final int INT_BLE_SP_TIME5 = 34;
    public final int INT_BLE_SP_TIME_NONE = 39;
    //Transducers Left
    public final int INT_BLE_SP_TRA1 = 40;
    public final int INT_BLE_SP_TRA2 = 41;
    public final int INT_BLE_SP_TRA3 = 42;
    public final int INT_BLE_SP_TRA4 = 43;
    public final int INT_BLE_SP_TRA5 = 44;
    public final int INT_BLE_SP_TRA6 = 45;
    public final int INT_BLE_SP_TRA_NONE = 49;
    //Transducers Right
    public final int INT_BLE_SP_TRB1 = 50;
    public final int INT_BLE_SP_TRB2 = 51;
    public final int INT_BLE_SP_TRB3 = 52;
    public final int INT_BLE_SP_TRB4 = 53;
    public final int INT_BLE_SP_TRB5 = 54;
    public final int INT_BLE_SP_TRB6 = 55;
    public final int INT_BLE_SP_TRB_NONE = 59;
    //modes
    public final int INT_BLE_SP_MODE1 = 70;//percussion
    public final int INT_BLE_SP_MODE2 = 71;//vibration
    public final int INT_BLE_SP_MODE3 = 72;//full percussion
    public final int INT_BLE_SP_MODE4 = 73;//total body
    public final int INT_BLE_SP_MODE5 = 74;
    public final int INT_BLE_SP_MODE9 = 79;//CLEAN
    //commands
    public final int INT_BLE_CMD_INIT = 60;
    public final int INT_BLE_CMD_START = 61;
    public final int INT_BLE_CMD_STOP = 62;
    public final int INT_BLE_CMD_TOTAL_VIB = 63;
    //64-SPARE
    public final int INT_BLE_SP_COOL_ALL_STOP = 65;//LINK
    public final int INT_BLE_SP_COOL_ALL_INIT = 66;
    public final int INT_BLE_CMD_TOTAL_PERC = 67;
    public final int INT_BLE_CMD_NONE = 69;
    //
    public final int INT_BLE_CMD_SLEEP = 03;
    public final int INT_BLE_CMD_WAKE = 04;
    //
    public final int INT_BLE_CMD_END = 68;
    //status
    public final int INT_BLE_STATUS_READY = 91;
    public final int INT_BLE_STATUS_WORKING = 92;
    public final int INT_BLE_STATUS_ALARM1 = 93;//?
    public final int INT_BLE_STATUS_ALARM2 = 94;//OVER CURRENT
    public final int INT_BLE_STATUS_ALARM3 = 95;//UNDER CURRENT
    //get current working module
    public final int INT_BLE_STATUS_MOD1 = 5;
    public final int INT_BLE_STATUS_MOD2 = 6;
    public final int INT_BLE_STATUS_MOD3 = 7;
    public final int INT_BLE_STATUS_MOD4 = 8;
    public final int INT_BLE_STATUS_MOD5 = 9;

    //Frequency
    private int BLE_SP_FREQ1 = 0x10;
    private int BLE_SP_FREQ2 = 0X11;
    private int BLE_SP_FREQ3 = 0X12;
    private int BLE_SP_FREQ4 = 0X13;
    private int BLE_SP_FREQ5 = 0X14;
    //Intensity
    private int BLE_SP_INT1 = 0X20;
    private int BLE_SP_INT2 = 0X21;
    private int BLE_SP_INT3 = 0X22;

    public int getBLE_CMD_SLEEP() {
        return BLE_CMD_SLEEP;
    }

    public int getBLE_CMD_WAKE() {
        return BLE_CMD_WAKE;
    }

    private int BLE_SP_INT4 = 0X23;
    private int BLE_SP_INT5 = 0X24;
    //Time
    private int BLE_SP_TIME1 = 0X30;
    private int BLE_SP_TIME2 = 0X31;
    private int BLE_SP_TIME3 = 0X32;
    private int BLE_SP_TIME4 = 0X33;
    private int BLE_SP_TIME5 = 0X34;
    //Transducers Left
    private int BLE_SP_TRA1 = 0X40;
    private int BLE_SP_TRA2 = 0X41;
    private int BLE_SP_TRA3 = 0X42;
    private int BLE_SP_TRA4 = 0X43;
    private int BLE_SP_TRA5 = 0X44;
    //Transducers Right
    private int BLE_SP_TRB1 = 0X50;
    private int BLE_SP_TRB2 = 0X51;
    private int BLE_SP_TRB3 = 0X52;
    private int BLE_SP_TRB4 = 0X53;
    private int BLE_SP_TRB5 = 0X54;
    //cooling boxes
    private int BLE_SP_COOL_BOX1 = 0X70;
    private int BLE_SP_COOL_BOX2 = 0X72;
    private int BLE_SP_COOL_BOX3 = 0X73;
    private int BLE_SP_COOL_BOX4 = 0X74;
    private int BLE_SP_COOL_BOX5 = 0X75;

    public final int BLE_CMD_START = 0X61;
    public final int BLE_CMD_STOP = 0x62;
    public final int BLE_CMD_SLEEP = 0X03;
    public final int BLE_CMD_WAKE = 0X04;
    public final int BLE_SP_COOL_ALL_STOP = 0X65;
    public final int BLE_SP_COOL_ALL_INIT = 0X66;

    //communication
    public final int BLE_GET_PV = 0X46;


    public int getBLE_SP_COOL_BOX1() {
        return BLE_SP_COOL_BOX1;
    }

    public int getBLE_SP_COOL_BOX2() {
        return BLE_SP_COOL_BOX2;
    }

    public int getBLE_SP_COOL_BOX3() {
        return BLE_SP_COOL_BOX3;
    }

    public int getBLE_SP_COOL_BOX4() {
        return BLE_SP_COOL_BOX4;
    }

    public int getBLE_SP_COOL_BOX5() {
        return BLE_SP_COOL_BOX5;
    }

    public int getBLE_SP_COOL_ALL_STOP() {
        return BLE_SP_COOL_ALL_STOP;
    }


    public int getBLE_SP_COOL_ALL_INIT() {
        return BLE_SP_COOL_ALL_INIT;
    }


    public int getBLE_SP_FREQ1() {
        return BLE_SP_FREQ1;
    }

    public int getBLE_SP_FREQ2() {
        return BLE_SP_FREQ2;
    }

    public int getBLE_SP_FREQ3() {
        return BLE_SP_FREQ3;
    }

    public int getBLE_SP_FREQ4() {
        return BLE_SP_FREQ4;
    }

    public int getBLE_SP_FREQ5() {
        return BLE_SP_FREQ5;
    }

    public int getBLE_SP_INT1() {
        return BLE_SP_INT1;
    }

    public int getBLE_SP_INT2() {
        return BLE_SP_INT2;
    }

    public int getBLE_SP_INT3() {
        return BLE_SP_INT3;
    }

    public int getBLE_SP_INT4() {
        return BLE_SP_INT4;
    }

    public int getBLE_SP_INT5() {
        return BLE_SP_INT5;
    }

    public int getBLE_SP_TIME1() {
        return BLE_SP_TIME1;
    }

    public int getBLE_SP_TIME2() {
        return BLE_SP_TIME2;
    }

    public int getBLE_SP_TIME3() {
        return BLE_SP_TIME3;
    }

    public int getBLE_SP_TIME4() {
        return BLE_SP_TIME4;
    }

    public int getBLE_SP_TIME5() {
        return BLE_SP_TIME5;
    }

    public int getBLE_SP_TRA1() {
        return BLE_SP_TRA1;
    }

    public int getBLE_SP_TRA2() {
        return BLE_SP_TRA2;
    }

    public int getBLE_SP_TRA3() {
        return BLE_SP_TRA3;
    }

    public int getBLE_SP_TRA4() {
        return BLE_SP_TRA4;
    }

    public int getBLE_SP_TRA5() {
        return BLE_SP_TRA5;
    }

    public int getBLE_SP_TRB1() {
        return BLE_SP_TRB1;
    }

    public int getBLE_SP_TRB2() {
        return BLE_SP_TRB2;
    }

    public int getBLE_SP_TRB3() {
        return BLE_SP_TRB3;
    }

    public int getBLE_SP_TRB4() {
        return BLE_SP_TRB4;
    }

    public int getBLE_SP_TRB5() {
        return BLE_SP_TRB5;
    }

    public int getBLE_CMD_START() {
        return BLE_CMD_START;
    }

    public int getBLE_CMD_STOP() {
        return BLE_CMD_STOP;
    }


}
