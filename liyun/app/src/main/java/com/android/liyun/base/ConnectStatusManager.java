package com.android.liyun.base;

/**
*@author hzx
*created at 2018/3/19 17:03
*/
public class ConnectStatusManager {

    private static boolean connected = false;
    private static StatusChangeCallback statusChangeCallback;

    public static boolean isConnected() {
        return connected;
    }

    public static synchronized void changeConnectionStatus(boolean connected) {
        if (ConnectStatusManager.connected == connected) {
            return;
        }

        ConnectStatusManager.connected = connected;

        if (statusChangeCallback != null) {
            statusChangeCallback.changeStatus(connected);
        }
    }

    public static void setStatusChangeCallback(StatusChangeCallback statusChangeCallback) {
        ConnectStatusManager.statusChangeCallback = statusChangeCallback;
    }

    public interface StatusChangeCallback {
        void changeStatus(boolean isConnected);
    }
}
