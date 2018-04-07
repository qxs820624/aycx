package com.android.liyun.bean;

import java.util.List;

public class OrdersGoodsBean {

    /**
     * msg : 查询成功
     * orders : [{"address":"","email":"","good":[{"goods_id":"9","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/9/1-230x230.jpg","name ":"力云LIYUN车载充电器9","price":"198.00","quantity":"1","total":"198.00"}],"name":"你","order_id":"39","order_num_alias":"O20180401050809","pay_time":"0","payment_code":"weixin","points_order":"0","return_points":"0","shipping_city":"0","shipping_country":"0","shipping_method":"1","shipping_name":"你","shipping_province":"0","shipping_tel":"18820103138","status":"已完成","tel":"","uid":"5"},{"address":"","email":"","good":[{"goods_id":"11","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/11/1-230x230.jpg","name ":"力云LIYUN车载充电器B","price":"450.00","quantity":"1","total":"450.00"}],"name":"你","order_id":"37","order_num_alias":"O20180331095607","pay_time":"0","payment_code":"weixin","points_order":"0","return_points":"0","shipping_city":"0","shipping_country":"0","shipping_method":"1","shipping_name":"你","shipping_province":"0","shipping_tel":"18820103138","status":"待发货","tel":"","uid":"5"},{"address":"","email":"","good":[{"goods_id":"8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","name ":"力云LIYUN车载充电器8","price":"58.00","quantity":"1","total":"58.00"}],"name":"你","order_id":"35","order_num_alias":"O20180328110053","pay_time":"0","payment_code":"weixin","points_order":"0","return_points":"0","shipping_city":"0","shipping_country":"0","shipping_method":"1","shipping_name":"你","shipping_province":"0","shipping_tel":"18820103138","status":"待发货","tel":"","uid":"5"},{"address":"","email":"","good":[{"goods_id":"8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","name ":"力云LIYUN车载充电器8","price":"58.00","quantity":"1","total":"58.00"}],"name":"你","order_id":"34","order_num_alias":"O20180328110000","pay_time":"0","payment_code":"weixin","points_order":"0","return_points":"0","shipping_city":"0","shipping_country":"0","shipping_method":"1","shipping_name":"你","shipping_province":"0","shipping_tel":"18820103138","status":"待付款","tel":"","uid":"5"},{"address":"","email":"","good":[{"goods_id":"8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","name ":"力云LIYUN车载充电器8","price":"58.00","quantity":"1","total":"58.00"}],"name":"你","order_id":"33","order_num_alias":"O20180328105047","pay_time":"0","payment_code":"weixin","points_order":"0","return_points":"0","shipping_city":"0","shipping_country":"0","shipping_method":"1","shipping_name":"你","shipping_province":"0","shipping_tel":"18820103138","status":"待发货","tel":"","uid":"5"}]
     * status : 0
     */

    private String msg;
    private String status;
    private List<OrdersBean> orders;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }


}
