package com.android.liyun.listener;

/**
* 
*@author hzx
*created at 2018/3/20 12:17
*/
public interface HttpTransactionCallback {

    void onConnectionShutDown(int type);
    void onConnectionAvalible(boolean floag);

    void onConnectionChangeWIFI(int type);

    void onConnectionChangeGPRS(int type);
    void onPostFinished();
}
