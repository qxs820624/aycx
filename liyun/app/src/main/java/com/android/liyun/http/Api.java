package com.android.liyun.http;

import android.content.Context;
import android.os.Handler;


import com.android.liyun.base.AddBean;

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

    /**
     * 实名认证
     *
     * @param what
     * @param uid
     * @param token
     * @param phone
     * @param realname
     * @param email
     * @param idcard
     */
    public void identiFication(int what, String uid, String token, String phone, String realname, String email, String idcard) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "User");
        paramsTreeMap.put("action", "Identification");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("phone", phone);
        paramsTreeMap.put("realname", realname);
        paramsTreeMap.put("email", email);
        paramsTreeMap.put("idcard", idcard);
        mHttpRequest.postDataString(url, what, "resetPwd", paramsTreeMap, false);
        mHttpTag.put("shoppCartList", 0);
    }

    /**
     * 推荐商品
     *
     * @param what
     */
    public void tuiJian(int what) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Good");
        paramsTreeMap.put("action", "tuijian");
        mHttpRequest.postDataString(url, what, "tuijian", paramsTreeMap, false);
        mHttpTag.put("tuijian", 0);
    }

    /**
     * 获取商品详情
     *
     * @param what
     */
    public void getGoodsDetail(int what, String goods_id) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Good");
        paramsTreeMap.put("action", "getdetail");
        paramsTreeMap.put("goods_id", goods_id);
        mHttpRequest.postDataString(url, what, "getdetail", paramsTreeMap, false);
        mHttpTag.put("getdetail", 0);
    }

    /**
     * 添加地址
     *
     * @param what
     * @param addBean
     */
    public void addAddress(int what, AddBean addBean) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", addBean.getModule());
        paramsTreeMap.put("action", addBean.getAction());
        paramsTreeMap.put("uid", addBean.getUid());
        paramsTreeMap.put("token", addBean.getToken());
        paramsTreeMap.put("name", addBean.getName());
        paramsTreeMap.put("telephone", addBean.getTelephone());
        paramsTreeMap.put("address", addBean.getAddress());
        paramsTreeMap.put("city", addBean.getCity());
        paramsTreeMap.put("country", addBean.getCountry());
        paramsTreeMap.put("province", addBean.getProvince());
        paramsTreeMap.put("isdefault", "0");
        mHttpRequest.postDataString(url, what, "addAddress", paramsTreeMap, false);
        mHttpTag.put("addAddress", 0);
    }

    /**
     * 地址管理
     *
     * @param what
     * @param uid
     * @param token
     */
    public void addManagement(int what, String uid, String token) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Address");
        paramsTreeMap.put("action", "load");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        mHttpRequest.postDataString(url, what, "addManagement", paramsTreeMap, false);
        mHttpTag.put("addManagement", 0);
    }

    /**
     * 删除地址
     *
     * @param what
     * @param uid
     * @param token
     * @param addressid
     */
    public void delAddress(int what, String uid, String token, String addressid) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Address");
        paramsTreeMap.put("action", "delete");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("addressid", addressid);
        mHttpRequest.postDataString(url, what, "delAddress", paramsTreeMap, false);
        mHttpTag.put("delAddress", 0);
    }

    /**
     * 設置默认地址
     *
     * @param what
     * @param uid
     * @param token
     * @param addressid
     * @param isdefault
     */
    public void setDefault(int what, String uid, String token, String addressid, String isdefault) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Address");
        paramsTreeMap.put("action", "setdefault");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("addressid", addressid);
        paramsTreeMap.put("isdefault", isdefault);
        mHttpRequest.postDataString(url, what, "delAddress", paramsTreeMap, false);
        mHttpTag.put("delAddress", 0);
    }

    /**
     * 添加购物车
     *
     * @param what
     * @param uid
     * @param token
     * @param goods_id
     * @param quantity
     * @param option
     */
    public void addCart(int what, String uid, String token, String goods_id, String quantity, String option) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Car");
        paramsTreeMap.put("action", "Addtocart");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("goods_id", goods_id);
        paramsTreeMap.put("quantity", quantity);
        paramsTreeMap.put("option", option);
        mHttpRequest.postDataString(url, what, "delAddress", paramsTreeMap, false);
        mHttpTag.put("delAddress", 0);
    }

    /**
     * 购物车列表
     *
     * @param what
     * @param uid
     * @param token
     */
    public void cartList(int what, String uid, String token) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Car");
        paramsTreeMap.put("action", "load");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        mHttpRequest.postDataString(url, what, "cartList", paramsTreeMap, false);
        mHttpTag.put("cartList", 0);
    }

    /**
     * 蓝牙数据
     *
     * @param what
     * @param uid
     * @param token
     * @param common
     */
    public void driveVideo(int what, String uid, String token, String common) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "User");
        paramsTreeMap.put("action", "sendDriving");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("common", common);
        mHttpRequest.postDataString(url, what, "cartList", paramsTreeMap, false);
        mHttpTag.put("cartList", 0);
    }

    /**
     * 添加订单
     *
     * @param what
     * @param uid
     * @param token
     * @param addressid
     * @param goodsid
     * @param common
     */
    public void addOrder(int what, String uid, String token, String addressid, String goodsid, String comment) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Order");
        paramsTreeMap.put("action", "add");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("addressid", addressid);
        paramsTreeMap.put("goodsid", goodsid);
        paramsTreeMap.put("comment", comment);
        paramsTreeMap.put("count", 1 + "");
        mHttpRequest.postDataString(url, what, "cartList", paramsTreeMap, false);
        mHttpTag.put("cartList", 0);
    }

    /**
     * 获取默认地址
     *
     * @param what
     * @param uid
     * @param token
     */
    public void getDefaultAdd(int what, String uid, String token) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Address");
        paramsTreeMap.put("action", "getdefault");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        mHttpRequest.postDataString(url, what, "getDefaultAdd", paramsTreeMap, false);
        mHttpTag.put("getDefaultAdd", 0);
    }

    /**
     * 添加收藏
     *
     * @param what
     * @param uid
     * @param token
     * @param goods_id
     */
    public void Favorite(int what, String uid, String token, String goods_id) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Favorite");
        paramsTreeMap.put("action", "add");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("goods_id", goods_id);
        mHttpRequest.postDataString(url, what, "getDefaultAdd", paramsTreeMap, false);
        mHttpTag.put("getDefaultAdd", 0);
    }

    /**
     * 收藏夹列表
     *
     * @param what
     * @param uid
     * @param token
     */
    public void favoriteGoodList(int what, String uid, String token) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Favorite");
        paramsTreeMap.put("action", "load");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        mHttpRequest.postDataString(url, what, "favoriteGoodList", paramsTreeMap, false);
        mHttpTag.put("favoriteGoodList", 0);
    }

    /**
     * 商品列表
     *
     * @param what
     * @param categoryid
     * @param page
     */
    public void goodsList(int what, String categoryid, String page) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Good");
        paramsTreeMap.put("action", "load");
        paramsTreeMap.put("categoryid", categoryid);
        paramsTreeMap.put("page", page);
        mHttpRequest.postDataString(url, what, "getDefaultAdd", paramsTreeMap, false);
        mHttpTag.put("getDefaultAdd", 0);
    }

    /**
     * 获取订单列表
     *
     * @param what
     * @param uid
     * @param token
     * @param page
     */
    public void getOrderList(int what, String uid, String token, String page) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Order");
        paramsTreeMap.put("action", "getlist");
        paramsTreeMap.put("uid", uid);
        paramsTreeMap.put("token", token);
        paramsTreeMap.put("page", page);
        mHttpRequest.postDataString(url, what, "getOrderList", paramsTreeMap, false);
        mHttpTag.put("getOrderList", 0);
    }

    /**
     * 积分排名
     *
     * @param what
     */
    public void ranking(int what) {
        String url = ConstValues.url;
        TreeMap<String, String> paramsTreeMap = new TreeMap<>();
        paramsTreeMap.put("module", "Order");
        paramsTreeMap.put("action", "jifenpaiming");
        mHttpRequest.postDataString(url, what, "ranking", paramsTreeMap, false);
        mHttpTag.put("ranking", 0);
    }

}
