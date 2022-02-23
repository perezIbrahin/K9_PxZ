package Util;

public class Setpoint {
    //max. time of next cycle prog1
    private final int PROG1_MAX_TIME_CYCLE = 15;

    public int getPROG1_MAX_TIME_CYCLE() {
        return PROG1_MAX_TIME_CYCLE;
    }

    //frequency
    private final int FREQ1 = 10;
    private final int FREQ2 = 20;
    private final int FREQ3 = 25;
    private final int FREQ4 = 30;
    private final int FREQ5 = 35;
    //Intensity
    private final int INT1 = 20;
    private final int INT2 = 25;
    private final int INT3 = 30;
    private final int INT4 = 35;
    private final int INT5 = 40;
    //time
    public final int TIME1 = 5;
    public final int TIME2 = 10;
    public final int TIME3 = 15;
    public final int TIME4 = 20;
    public final int TIME5 = 25;

    public int getFREQ1() {
        return FREQ1;
    }

    public int getFREQ2() {
        return FREQ2;
    }

    public int getFREQ3() {
        return FREQ3;
    }

    public int getFREQ4() {
        return FREQ4;
    }

    public int getFREQ5() {
        return FREQ5;
    }

    public int getINT1() {
        return INT1;
    }

    public int getINT2() {
        return INT2;
    }

    public int getINT3() {
        return INT3;
    }

    public int getINT4() {
        return INT4;
    }

    public int getINT5() {
        return INT5;
    }

    public int getTIME1() {
        return TIME1;
    }

    public int getTIME2() {
        return TIME2;
    }

    public int getTIME3() {
        return TIME3;
    }

    public int getTIME4() {
        return TIME4;
    }

    public int getTIME5() {
        return TIME5;
    }


    //

    private final int FREQ_MIN = 30;
    private final int FREQ_MAX = 45;
    //
    private final int INT_MIN = 20;
    private final int INT_MAX = 50;
    //
    private final int TIME_MIN = 0;
    private final int TIME_MAX = 45;

    public int getFREQ_MIN() {
        return FREQ_MIN;
    }

    public int getFREQ_MAX() {
        return FREQ_MAX;
    }

    public int getINT_MIN() {
        return INT_MIN;
    }

    public int getINT_MAX() {
        return INT_MAX;
    }

    public int getTIME_MIN() {
        return TIME_MIN;
    }

    public int getTIME_MAX() {
        return TIME_MAX;
    }


}
