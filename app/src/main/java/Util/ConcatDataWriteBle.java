package Util;

public class ConcatDataWriteBle {
    public ConcatDataWriteBle() {
    }
    public int concatDataToWrite(int register, int value){
        int factor = 256;//this is for 16 bits. Move to byte 1
        int mValue = (register * factor) + value;
        return mValue;
    }
}
