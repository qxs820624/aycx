package com.android.liyun.bean;

import android.bluetooth.BluetoothDevice;

/**
*
*@author hzx
*created at 2018/3/20 12:16
*/
public class Device  {

    private String name;
    private String address;
    private BluetoothDevice bluetoothDevice;
    private  int rssi;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}