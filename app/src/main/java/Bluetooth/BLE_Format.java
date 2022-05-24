package Bluetooth;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SFLOAT;

import android.bluetooth.BluetoothGattCharacteristic;

public class BLE_Format {
    public BLE_Format() {
    }

    /* reads and return what what FORMAT is indicated by characteristic's properties
     * seems that value makes no sense in most cases */
    public int getValueFormat(BluetoothGattCharacteristic ch) {
        int properties = ch.getProperties();

        if ((BluetoothGattCharacteristic.FORMAT_FLOAT & properties) != 0)
            return BluetoothGattCharacteristic.FORMAT_FLOAT;
        if ((FORMAT_SFLOAT & properties) != 0)
            return FORMAT_SFLOAT;
        if ((BluetoothGattCharacteristic.FORMAT_SINT16 & properties) != 0)
            return BluetoothGattCharacteristic.FORMAT_SINT16;
        if ((BluetoothGattCharacteristic.FORMAT_SINT32 & properties) != 0)
            return BluetoothGattCharacteristic.FORMAT_SINT32;
        if ((BluetoothGattCharacteristic.FORMAT_SINT8 & properties) != 0)
            return BluetoothGattCharacteristic.FORMAT_SINT8;
        if ((BluetoothGattCharacteristic.FORMAT_UINT16 & properties) != 0)
            return BluetoothGattCharacteristic.FORMAT_UINT16;
        if ((BluetoothGattCharacteristic.FORMAT_UINT32 & properties) != 0)
            return BluetoothGattCharacteristic.FORMAT_UINT32;
        if ((BluetoothGattCharacteristic.FORMAT_UINT8 & properties) != 0)
            return BluetoothGattCharacteristic.FORMAT_UINT8;

        return 0;
    }
}
