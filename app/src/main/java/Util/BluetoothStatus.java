package Util;

public class BluetoothStatus {
    private final String BLE_OFF = "Failure";

    public String getBLE_OFF() {
        return BLE_OFF;
    }

    public String getBLE_CONNECTED() {
        return BLE_CONNECTED;
    }

    private final String BLE_CONNECTED = "Connected";

    //Defaults values
    public  String DEFAULT_NAME_BLE="KZ";
    public  String DEFAULT_ADD_BLE="90:FD:9F:0A:F5:F6";

}
