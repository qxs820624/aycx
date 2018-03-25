package com.android.liyun.bean;

import java.util.List;


/**
 * 购物车列表
 */
public class CartListBean {
    /**
     * status : 0
     * msg : 查询成功
     * goods : [{"goods_id":"9","name":"力云LIYUN车载充电器9","price":"198.00","quantity":"1","pay_points":"3000","image":"/public/uploads/cache/images/osc1/9/1.jpg","model":"D20151107"}]
     */

    private String status;
    private String msg;
    private List<GoodsBean> goods;

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

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * goods_id : 9
         * name : 力云LIYUN车载充电器9
         * price : 198.00
         * quantity : 1
         * pay_points : 3000
         * image : /public/uploads/cache/images/osc1/9/1.jpg
         * model : D20151107
         */

        private String goods_id;
        private String name;
        private String price;
        private String quantity;
        private String pay_points;
        private String image;
        private String model;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPay_points() {
            return pay_points;
        }

        public void setPay_points(String pay_points) {
            this.pay_points = pay_points;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }
}
