package com.liyun.blelibrary.listener;

/**
* 
*@author hzx
*created at 2018/3/20 12:16
*/
public interface SmartSystemDataCallback {

    void getConnectionParamData(byte action);

    void onReceiveFirmwareUpgradeState(byte state, int counter);

    void onUploadFirmwareSuccess();

    void onReceiveFirmwareUpgradeTime(int updateTime);
}
