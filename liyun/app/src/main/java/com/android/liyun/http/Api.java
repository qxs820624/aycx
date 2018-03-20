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

    /**
     * 商品列表
     *
     * @param what
     * @param module
     * @param action
     */
    public void goodList(int what, String module, String action) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        mHttpRequest.postDataString(url, what, "resetPwd", paramsTreeMap, false);
        mHttpTag.put("goodList", 0);
    }

    /**
     * 添加商品
     *
     * @param what
     * @param module
     * @param action
     * @param goods_id
     * @param uid
     * @param quantity
     * @param option
     */
    public void addGoods(int what, String module, String action, String goods_id, String uid, String quantity, String option) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("goods_id", goods_id);
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("quantity", quantity);
        paramsTreeMap.put("option", option);
        mHttpRequest.postDataString(url, what, "resetPwd", paramsTreeMap, false);
        mHttpTag.put("addGoods", 0);
    }

    /**
     * 清空购物车
     *
     * @param what
     * @param module
     * @param action
     * @param uid
     */
    public void emptyGoods(int what, String module, String action, String uid) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("uid", uid);
        mHttpRequest.postDataString(url, what, "resetPwd", paramsTreeMap, false);
        mHttpTag.put("emptyGoods", 0);
    }

    /**
     * 从购物车移除某个产品
     *
     * @param what
     * @param module
     * @param action
     * @param goods_id
     * @param uid
     */

    public void removeGoods(int what, String module, String action, String goods_id, String uid) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("goods_id", goods_id);
        mHttpRequest.postDataString(url, what, "resetPwd", paramsTreeMap, false);
        mHttpTag.put("removeGoods", 0);
    }

    /**
     * 购物车列表
     *
     * @param what
     * @param module
     * @param action
     * @param uid
     */
    public void shoppCartList(int what, String module, String action, String uid) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", module);
        paramsTreeMap.put("action", action);
        paramsTreeMap.put("uid", uid);
        mHttpRequest.postDataString(url, what, "resetPwd", paramsTreeMap, false);
        mHttpTag.put("shoppCartList", 0);
    }
}
