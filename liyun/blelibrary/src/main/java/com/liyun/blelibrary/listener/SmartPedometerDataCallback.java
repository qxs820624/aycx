package com.liyun.blelibrary.listener;

/**
* 
*@author hzx
*created at 2018/3/20 12:16
*/
public interface SmartPedometerDataCallback {
    void getPedometerStatusData(byte[] data);

    void getPedometerRecordStatusData(int startId, int endId);

    void getActionRecordData(byte[] data,byte action);
    //void getPedometerRecordData(PedometerRecordData pedometerRecordData);
}
