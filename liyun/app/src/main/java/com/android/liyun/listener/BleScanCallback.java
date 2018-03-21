package com.android.liyun.listener;

import android.bluetooth.BluetoothDevice;

/**
* 
*@author hzx
*created at 2018/3/20 12:16
*/
public interface BleScanCallback {

    boolean checkBluetoothPermission();

    void enableBluetooth();

    //发现所有扫描到的设备，用于绑定设备
    void discoverDevice(BluetoothDevice bluetoothDevice, int rssi);

    //发现扫描到的绑定设备，用于连接设备
    void discoverBindDevice(BluetoothDevice bluetoothDevice);

    void startScanning();

    void stopScanning();
}
