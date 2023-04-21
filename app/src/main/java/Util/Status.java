package Util;

public class Status {
    //not in use
    public  int CON_BLE_DISC=0;
    public  int CON_BLE_CONN=1;
    public  int CON_BLE_DISC_REQUESTED=2;
    public  int CON_BLE_DISCOVERY_FINISHED=3;
    public  int CON_BLE_FOUND=4;
    public  int CON_BLE_ERROR=10;

    //
    public  String DIALOG_ALERT_UUID="DIALOG_ALERT_UUID";
    //
    public int SAVE_NEW_UUID=1;
    public int START_SCAN_BLE=2;
    //Ble status
    public String BLE_CONNECTED="connected";
    public String BLE_DISCONNECTED="disconnected";
    //
    public int STATUS_PASS=1;
    public int STATUS_SERIAL_LINK=2;
    public int STATUS_SCAN =4;

    //select mode
    public int SELECT_MODE_NONE=0;
    public int SELECT_MODE_PERCUSSION=1;
    public int SELECT_MODE_VIBRATION=2;
    public int SELECT_MODE_TOTAL_PERCUSSION=3;
    public int SELECT_MODE_TOTAL_VIBRATION=4;

    //lock screen mode
    public int SELECT_SCREEN_UNLOCK=0;
    public int SELECT_SCREEN_LOCK=1;

    //pass data between activity

    //ethernet
    public String ETH_SOCKET_NOT_WORKING="ETH_SOCKET_NOT_WORKING";
    public String ETH_SOCKET_CAN_CONNECT="ETH_SOCKET_CAN_CONNECT";

    //
    public String PASS_OK="GO_LINK";




}
