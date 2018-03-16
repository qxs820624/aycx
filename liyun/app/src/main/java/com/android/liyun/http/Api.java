package com.android.liyun.http;

import android.content.Context;
import android.os.Handler;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/9/9.
 */

public class Api {

    private HttpRequest mHttpRequest;
    private Map<String, Integer> mHttpTag;
    private Handler mHandler;
    private Context context;

    public Api(Handler handler, Context context) {
        mHttpRequest = new HttpRequest(handler, context);
        mHttpTag = new HashMap<>();
        this.mHandler = handler;
    }


    public void downLoadFile(int what, String url, String dir, String fileName) {
        mHttpRequest.downLoadFile(url, what, dir, fileName, false);
        mHttpTag.put("download", 0);
    }

    /**
     * 发送验证码
     *
     * @param what
     * @param module
     * @param action
     * @param phone
     * @param sendType
     */
    public void sentCode(int what, String module, String action, String phone, String sendType) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("phone", phone);
        paramsTreeMap.put("sendType", sendType);
        mHttpRequest.postDataString(url, what, "sentcode", paramsTreeMap, false);
        mHttpTag.put("sentcode", 0);
    }

    /**
     * 注册
     *
     * @param what
     * @param module
     * @param action
     * @param Phone
     * @param smsCode
     * @param Passwd
     */
    public void userRegist(int what, String module, String action, String Phone, String smsCode, String Passwd) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("Phone", Phone);
        paramsTreeMap.put("smsCode", smsCode);
        paramsTreeMap.put("Passwd", Passwd);
        mHttpRequest.postDataString(url, what, "userRegist", paramsTreeMap, false);
        mHttpTag.put("regist", 0);
    }

    /**
     * 登录
     *
     * @param what
     * @param module
     * @param action
     * @param account
     * @param password
     */
    public void login(int what, String module, String action, String account, String password) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("account", account);
        paramsTreeMap.put("password", password);
        mHttpRequest.postDataString(url, what, "login", paramsTreeMap, false);
        mHttpTag.put("login", 0);
    }

    /**
     * 重置密码
     *
     * @param what
     * @param module
     * @param action
     * @param phone
     * @param smsCode
     * @param newPassword
     */
    public void resetPwd(int what, String module, String action, String phone, String smsCode, String newPassword) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("phone", phone);
        paramsTreeMap.put("smsCode", smsCode);
        paramsTreeMap.put("newPassword", newPassword);
        mHttpRequest.postDataString(url, what, "resetPwd", paramsTreeMap, false);
        mHttpTag.put("resetPwd", 0);
    }


}
