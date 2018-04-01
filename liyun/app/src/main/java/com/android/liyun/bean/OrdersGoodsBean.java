package com.android.liyun.bean;

import java.util.List;

public class OrdersGoodsBean {

    /**
     * status : 0
     * msg : 查询成功
     * orders : [{"order_id":"37","order_num_alias":"O20180331095607","uid":"5","return_points":"0","points_order":"0","name":"你","email":"","tel":"","shipping_name":"你","address":"","shipping_tel":"18820103138","shipping_city":"0","shipping_country":"0","shipping_province":"0","shipping_method":"1","payment_code":"weixin","pay_time":"0","good":[{"goods_id":"11","name ":"力云LIYUN车载充电器B","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/11/1-230x230.jpg","quantity":"1","price":"450.00","total":"450.00"}]},{"order_id":"35","order_num_alias":"O20180328110053","uid":"5","return_points":"0","points_order":"0","name":"你","email":"","tel":"","shipping_name":"你","address":"","shipping_tel":"18820103138","shipping_city":"0","shipping_country":"0","shipping_province":"0","shipping_method":"1","payment_code":"weixin","pay_time":"0","good":[{"goods_id":"8","name ":"力云LIYUN车载充电器8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","quantity":"1","price":"58.00","total":"58.00"}]},{"order_id":"34","order_num_alias":"O20180328110000","uid":"5","return_points":"0","points_order":"0","name":"你","email":"","tel":"","shipping_name":"你","address":"","shipping_tel":"18820103138","shipping_city":"0","shipping_country":"0","shipping_province":"0","shipping_method":"1","payment_code":"weixin","pay_time":"0","good":[{"goods_id":"8","name ":"力云LIYUN车载充电器8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","quantity":"1","price":"58.00","total":"58.00"}]},{"order_id":"33","order_num_alias":"O20180328105047","uid":"5","return_points":"0","points_order":"0","name":"你","email":"","tel":"","shipping_name":"你","address":"","shipping_tel":"18820103138","shipping_city":"0","shipping_country":"0","shipping_province":"0","shipping_method":"1","payment_code":"weixin","pay_time":"0","good":[{"goods_id":"8","name ":"力云LIYUN车载充电器8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","quantity":"1","price":"58.00","total":"58.00"}]}]
     */

    private String status;
    private String msg;
    private List<OrdersBean> orders;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        /**
         * order_id : 37
         * order_num_alias : O20180331095607
         * uid : 5
         * return_points : 0
         * points_order : 0
         * name : 你
         * email :
         * tel :
         * shipping_name : 你
         * address :
         * shipping_tel : 18820103138
         * shipping_city : 0
         * shipping_country : 0
         * shipping_province : 0
         * shipping_method : 1
         * payment_code : weixin
         * pay_time : 0
         * good : [{"goods_id":"11","name ":"力云LIYUN车载充电器B","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/11/1-230x230.jpg","quantity":"1","price":"450.00","total":"450.00"}]
         */

        private String order_id;
        private String order_num_alias;
        private String uid;
        private String return_points;
        private String points_order;
        private String name;
        private String email;
        private String tel;
        private String shipping_name;
        private String address;
        private String shipping_tel;
        private String shipping_city;
        private String shipping_country;
        private String shipping_province;
        private String shipping_method;
        private String payment_code;
        private String pay_time;
        private List<GoodBean> good;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_num_alias() {
            return order_num_alias;
        }

        public void setOrder_num_alias(String order_num_alias) {
            this.order_num_alias = order_num_alias;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getReturn_points() {
            return return_points;
        }

        public void setReturn_points(String return_points) {
            this.return_points = return_points;
        }

        public String getPoints_order() {
            return points_order;
        }

        public void setPoints_order(String points_order) {
            this.points_order = points_order;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getShipping_name() {
            return shipping_name;
        }

        public void setShipping_name(String shipping_name) {
            this.shipping_name = shipping_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getShipping_tel() {
            return shipping_tel;
        }

        public void setShipping_tel(String shipping_tel) {
            this.shipping_tel = shipping_tel;
        }

        public String getShipping_city() {
            return shipping_city;
        }

        public void setShipping_city(String shipping_city) {
            this.shipping_city = shipping_city;
        }

        public String getShipping_country() {
            return shipping_country;
        }

        public void setShipping_country(String shipping_country) {
            this.shipping_country = shipping_country;
        }

        public String getShipping_province() {
            return shipping_province;
        }

        public void setShipping_province(String shipping_province) {
            this.shipping_province = shipping_province;
        }

        public String getShipping_method() {
            return shipping_method;
        }

        public void setShipping_method(String shipping_method) {
            this.shipping_method = shipping_method;
        }

        public String getPayment_code() {
            return payment_code;
        }

        public void setPayment_code(String payment_code) {
            this.payment_code = payment_code;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public List<GoodBean> getGood() {
            return good;
        }

        public void setGood(List<GoodBean> good) {
            this.good = good;
        }

        public static class GoodBean {
            /**
             * goods_id : 11
             * name  : 力云LIYUN车载充电器B
             * image : http://219.153.49.223:8088/public/uploads/cache/images/osc1/11/1-230x230.jpg
             * quantity : 1
             * price : 450.00
             * total : 450.00
             */

            private String goods_id;
            private String name;
            private String image;
            private String quantity;
            private String price;
            private String total;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }
        }
    }
}
