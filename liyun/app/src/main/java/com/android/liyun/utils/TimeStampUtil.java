package com.android.liyun.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
*
*@author hzx
*created at 2018/3/20 12:16
*/
public class TimeStampUtil {

    private static final String TAG = "TimeStampUtil";

    /**
     * 时间戳(单位毫秒秒)转普通格式时间
     *
     * @return
     */
    public static String unixTimeStamp2Datems(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    /**
     * 计算时间差
     *
     * @return
     */
    public static double calculateEnergyNum(String connectTime, String disConnectTime) {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long allMinutes = 0;
        try {
            Date d1 = df.parse(disConnectTime);
            Date d2 = df.parse(connectTime);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            Log.d(TAG,"" + days + "天" + hours + "小时" + minutes + "分");
            allMinutes = days * 24 * 60 + hours * 60 + minutes;
        } catch (Exception e) {
        }
        return allMinutes*1.0 / 30;
    }
}

