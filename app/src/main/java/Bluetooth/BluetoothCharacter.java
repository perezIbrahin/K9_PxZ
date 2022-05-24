package Bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by Patry on 10/5/2017.
 */

public class BluetoothCharacter {
    //Services
    public final String CHARA_FREQUENCY = "6d8615d4-e30b-4bcc-915d-ef18e37a2ec0";
    public final String CHARA_INTENSITY = "2ffa317c-ebe9-4e33-9d0a-24cbe8141d44";
    public final String CHARA_TIMER = "6dd9937b-1819-4ebe-b480-43ed813e7805";
    public final String CHARA_DEVICES = "3dc61f67-65ba-45d5-a31a-84402f81a776";
    public final String CHARA_COMMANDS = "25cc76dc-d80f-4221-82be-4aadf3ff12b5";
    public final String CHARA_SCALE = "1c6285fa-c683-4115-803a-62fd01fd32d";

    //Request permission
    public static final int REQUEST_ENABLE_BLE = 1;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    //MAC address

    public final String MAC_K9 = "90:FD:9F:5F:55:16";//address of the unit to connect-k9
    public final String MAC_KM = "90:FD:9F:0A:F5:F6";
    public final String MAC_KZ = "90:FD:9F:0A:F5:F6";//address of the unit to connect-KZ
    public final String MAC_K1200_SYST_B = "90:FD:9F:5F:1D:C2";//address of the unit to connect-KZ//"00:OB:57:B9:B5:7C"
    public final String MAC_K1200_TEST = "90:FD:9F:40:22:B4";//address of the unit to connect-KZ

    public final String MAC_K1200_SYST_A = "90:FD:9F:40:1C:F6";//address of the unit to connect-KZ

    //Bluetooth Characteristic
    public BluetoothGattCharacteristic chara_ext;
    public BluetoothGattCharacteristic chara_intensity; //scale
    public BluetoothGattCharacteristic chara_exp;//using this service to send data to BGM113
    public BluetoothGattCharacteristic chara_devices;
    public BluetoothGattCharacteristic chara_commands;
    public BluetoothGattCharacteristic chara_feedback;

    //
    //K750
    public BluetoothGattCharacteristic mCHARAC_K750_ART_HEAD;
    public BluetoothGattCharacteristic mCHARAC_K750_ART_FOOT;
    public BluetoothGattCharacteristic mCHARAC_K750_LIFT_HEAD;
    public BluetoothGattCharacteristic mCHARAC_K750_LIFT_FOOT;
    public BluetoothGattCharacteristic mCHARAC_K750_EXTENSION;
    public BluetoothGattCharacteristic mCHARAC_K750_BRAKE;
    public BluetoothGattCharacteristic mCHARAC_K750_SCALE;
    public BluetoothGattCharacteristic mCHARAC_K750_CMD;
    public BluetoothGattCharacteristic mCHARAC_K750_SIDERAIL;

    /* Service and Characteristic to indicate*/
    public static int SERVICE_NUMBER = 5;


}
