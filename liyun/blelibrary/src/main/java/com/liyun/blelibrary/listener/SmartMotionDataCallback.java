package com.liyun.blelibrary.listener;

/**
* 
*@author hzx
*created at 2018/3/20 12:16
*/
public interface SmartMotionDataCallback {

    void getMotionRecognizeData(byte[] data);

    void getMotionContinuityData(byte[] data);

    void getMotionStopData(byte[] data);
}
