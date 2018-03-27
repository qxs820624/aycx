package com.android.liyun.base;

import java.util.List;

public class GoodsListBean {


    /**
     * goods : [{"goods_id":"8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","model":"D20151107","name":"力云LIYUN车载充电器8","pay_points":"5000","price":"58.00"},{"goods_id":"9","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/9/1-230x230.jpg","model":"D20151107","name":"力云LIYUN车载充电器9","pay_points":"3000","price":"198.00"},{"goods_id":"10","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/10/1-230x230.jpg","model":"D20151107","name":"力云LIYUN车载充电器A","pay_points":"5000","price":"158.00"},{"goods_id":"11","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/11/1-230x230.jpg","model":"D20151107","name":"力云LIYUN车载充电器B","pay_points":"5000","price":"450.00"}]
     * msg : 查询成功
     * status : 0
     */

    private String msg;
    private String status;
    private List<GoodsBean> goods;

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

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * goods_id : 8
         * image : http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg
         * model : D20151107
         * name : 力云LIYUN车载充电器8
         * pay_points : 5000
         * price : 58.00
         */

        private String goods_id;
        private String image;
        private String model;
        private String name;
        private String pay_points;
        private String price;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPay_points() {
            return pay_points;
        }

        public void setPay_points(String pay_points) {
            this.pay_points = pay_points;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
