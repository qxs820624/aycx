package com.android.liyun.http;

/**
 * Created by Administrator on 2017/9/9.
 */

public interface RequestWhatI {

    int LOGIN = 1;
    int SENDCODE = 2;
    int REGIST = 3;
    int RESETPWD = 3;
    int IDENTIFICATION = 5;
    int TUIJIAN = 6;
    int GET_GOODS_DETAIL = 7;
    int ADDRESS = 8;
    int ADDMAN = 9;
    int DELADD = 10;//刪除地址
    int SETDEFAULT = 11;//设置默认地址
    int ADDCART = 12;//添加购物车
    int CARTLIST = 13;//购物车列表
    int DRIVEVIDEL = 14;//行车记录
    int ADD_ORDER = 15;//下单
    int GET_DEFAULT_ADD = 16;//获取默认地址
    int FAVORITE = 17;//添加收藏
    int GOOD_LIST = 18;//商品列表
}
