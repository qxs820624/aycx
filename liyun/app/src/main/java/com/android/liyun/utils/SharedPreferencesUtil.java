package com.android.liyun.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
*
*@author hzx
*created at 2018/3/31 13:34
*/
public class SharedPreferencesUtil {

    private static final String TAG = "SharedPreferencesUtil";

    public static final String NAME_BLUETOOTH_PARAMS    = "NAME_BLUETOOTH_PARAMS";
    public static final String KEY_DEVICE_NAME          = "KEY_DEVICE_NAME";
    public static final String KEY_DEVICE_SERIAL_NUMBER = "KEY_DEVICE_SERIAL_NUMBER";

    public static final String NAME_PROGRESS_LOCATION = "NAME_PROGRESS_LOCATION";
    public static final String KEY_PROGRESS_INNER_X = "KEY_PROGRESS_INNER_X";
    public static final String KEY_PROGRESS_INNER_Y = "KEY_PROGRESS_INNER_Y";
    public static final String KEY_PROGRESS_OUTER_X = "KEY_PROGRESS_OUTER_X";
    public static final String KEY_PROGRESS_OUTER_Y = "KEY_PROGRESS_OUTER_Y";
    public static final String KEY_PROGRESS_SHANK_X = "KEY_PROGRESS_SHANK_X";
    public static final String KEY_PROGRESS_SHANK_Y = "KEY_PROGRESS_SHANK_Y";
    public static final String KEY_PROGRESS_KNEE_X  = "KEY_PROGRESS_KNEE_X";
    public static final String KEY_PROGRESS_KNEE_Y  = "KEY_PROGRESS_KNEE_Y";
    public static final String KEY_PROGRESS_ANKLE_X = "KEY_PROGRESS_ANKLE_X";
    public static final String KEY_PROGRESS_ANKLE_Y = "KEY_PROGRESS_ANKLE_Y";

    public static final String NAME_BACKGROUND_LOCATION = "NAME_BACKGROUND_LOCATION";
    public static final String KEY_BACKGROUND_CUSTOMIZE = "KEY_BACKGROUND_CUSTOMIZE";
    public static final String KEY_BACKGROUND_PATH      = "KEY_BACKGROUND_PATH";

    public static final String NAME_MEMBER_INFO    = "NAME_MEMBER_INFO";
    public static final String KEY_POSITION_GENDER = "KEY_POSITION_GENDER";
    public static final String KEY_POSITION_HEIGHT = "KEY_POSITION_HEIGHT";
    public static final String KEY_POSITION_WEIGHT = "KEY_POSITION_WEIGHT";
    public static final String KEY_POSITION_CAREER = "KEY_POSITION_CAREER";

    public static final float DEFAULT_LOCATION = -1;
    public static final int   DEFAULT_POSITION = 0;

    /**
     * 保存绑定设备的参数
     *
     * @param context
     * @param name         设备名称
     * @param serialNumber 序列号
     */
    public static boolean saveDeviceParams(Context context, String name, String serialNumber) {
        SharedPreferences        share = context.getSharedPreferences(NAME_BLUETOOTH_PARAMS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit  = share.edit();
        edit.putString(KEY_DEVICE_NAME, name);
        edit.putString(KEY_DEVICE_SERIAL_NUMBER, serialNumber);
        return  edit.commit();
    }

    /**
     * 读取已绑定设备的参数
     *
     * @param context
     * @param key
     * @return
     */
    public static String loadDeviceParams(Context context, String key) {
        SharedPreferences share = context.getSharedPreferences(NAME_BLUETOOTH_PARAMS, Context.MODE_PRIVATE);
        return share.getString(key, null);
    }

    /**
     * 保存进度环的位置
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveProgressLocation(Context context, String key, float value) {
        SharedPreferences        share = context.getSharedPreferences(NAME_PROGRESS_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit  = share.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    /**
     * 读取进度环的位置
     *
     * @param context
     * @param key
     * @return
     */
    public static float loadProgressLocation(Context context, String key) {
        SharedPreferences share = context.getSharedPreferences(NAME_PROGRESS_LOCATION, Context.MODE_PRIVATE);
        return share.getFloat(key, DEFAULT_LOCATION);
    }

    /**
     * 保存首页背景图案的状态值
     *
     * @param context
     * @param customize 用户是否自定义
     * @param path      图片路径
     */
    public static void saveBackgroundStatus(Context context, boolean customize, String path) {
        SharedPreferences        share = context.getSharedPreferences(NAME_BACKGROUND_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit  = share.edit();
        edit.putBoolean(KEY_BACKGROUND_CUSTOMIZE, customize);
        edit.putString(KEY_BACKGROUND_PATH, path);
        edit.commit();
    }

    /**
     * 读取背景图片的保存路径
     * 非用户自定义会返回null
     *
     * @param context
     * @return
     */
    public static String loadBackgroundPath(Context context) {
        SharedPreferences share = context.getSharedPreferences(NAME_BACKGROUND_LOCATION, Context.MODE_PRIVATE);

        if (share.getBoolean(KEY_BACKGROUND_CUSTOMIZE, false)) {
            return share.getString(KEY_BACKGROUND_PATH, null);
        } else {
            return null;
        }
    }

    /**
     * 保存用户数据的在spinner的position
     *
     * @param context
     * @param key
     * @param position
     */
    public static void saveMemberInfo(Context context, String key, int position) {
        SharedPreferences        share = context.getSharedPreferences(NAME_MEMBER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit  = share.edit();
        edit.putInt(key, position);
        edit.commit();
    }

    /**
     * 通过key，找到position
     *
     * @param context
     * @param key
     * @return
     */
    public static int loadMemberInfo(Context context, String key) {
        SharedPreferences share = context.getSharedPreferences(NAME_MEMBER_INFO, Context.MODE_PRIVATE);
        return share.getInt(key, DEFAULT_POSITION);
    }
}