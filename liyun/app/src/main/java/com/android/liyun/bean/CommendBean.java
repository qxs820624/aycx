package com.android.liyun.bean;

import java.util.List;

/**
 * 推荐商品
 */
public class CommendBean {


    /**
     * status : 0
     * msg : 查询成功
     * goods : {"one":[{"goods_id":"8","name":"青瓷手绘荷花陶瓷茶叶罐 储物罐 瓷罐 收纳罐","price":"58.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/8/1.jpg","model":"D20151107"},{"goods_id":"11","name":"艺创 手工制作粗陶柴烧整套功夫木柄茶具 陶瓷 侧把 壶承礼盒","price":"450.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/11/1.jpg","model":"D20151107"},{"goods_id":"10","name":"艺创 定窑白瓷手绘山水整套功夫茶具 盖碗 茶杯套组","price":"158.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/10/1.jpg","model":"D20151107"},{"goods_id":"9","name":"艺创 定窑白瓷手绘胭红整套功夫茶具陶瓷 盖碗 茶杯套组","price":"198.00","pay_points":"3000","image":"/public/uploads/cache/images/osc1/9/1.jpg","model":"D20151107"}],"two":[{"goods_id":"4","name":"艺创 青花甜白功夫茶具陶瓷配件 茶叶过滤网隔茶渣网","price":"30.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/4/1.jpg","model":"D20151107"},{"goods_id":"7","name":"艺创 个人杯 品茗杯 功夫茶具茶杯 手拉坯柴烧","price":"30.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/7/1.jpg","model":"D20151107"},{"goods_id":"6","name":"艺创 品茗杯陶瓷功夫茶具 定窑白荷花大号茶个人主人杯","price":"39.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/6/1.jpg","model":"D20151107"},{"goods_id":"5","name":"旅行茶具套装便携式功夫茶具 车载 户外 手绘茶具","price":"0.20","pay_points":"5000","image":"/public/uploads/cache/images/osc1/5/1.jpg","model":"D20151107"}],"three":[{"goods_id":"1","name":"艺创 青花甜白三才大盖碗功夫茶具敬茶陶瓷泡茶器","price":"49.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/1/1.jpg","model":"D2016031002"},{"goods_id":"3","name":"艺创 青花甜白功夫茶具配件 公道杯茶海分茶器陶瓷","price":"39.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/3/2.jpg","model":"D20151107"},{"goods_id":"12","name":"艺创 青花白瓷手绘荷花整套功夫茶具陶瓷 盖碗 茶杯创意礼盒","price":"168.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/12/1.jpg","model":"D20151107"},{"goods_id":"2","name":"艺创 青花甜白功夫茶具小品茗茶杯陶瓷茶盏瓷杯主人杯6个","price":"79.00","pay_points":"5000","image":"/public/uploads/cache/images/osc1/2/2.jpg","model":"D2016031001"}]}
     */

    private String status;
    private String msg;
    private GoodsBean goods;

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

    public GoodsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsBean goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        private List<OneBean> one;
        private List<TwoBean> two;
        private List<ThreeBean> three;

        public List<OneBean> getOne() {
            return one;
        }

        public void setOne(List<OneBean> one) {
            this.one = one;
        }

        public List<TwoBean> getTwo() {
            return two;
        }

        public void setTwo(List<TwoBean> two) {
            this.two = two;
        }

        public List<ThreeBean> getThree() {
            return three;
        }

        public void setThree(List<ThreeBean> three) {
            this.three = three;
        }

        public static class OneBean {
            /**
             * goods_id : 8
             * name : 青瓷手绘荷花陶瓷茶叶罐 储物罐 瓷罐 收纳罐
             * price : 58.00
             * pay_points : 5000
             * image : /public/uploads/cache/images/osc1/8/1.jpg
             * model : D20151107
             */

            private String goods_id;
            private String name;
            private String price;
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

        public static class TwoBean {
            /**
             * goods_id : 4
             * name : 艺创 青花甜白功夫茶具陶瓷配件 茶叶过滤网隔茶渣网
             * price : 30.00
             * pay_points : 5000
             * image : /public/uploads/cache/images/osc1/4/1.jpg
             * model : D20151107
             */

            private String goods_id;
            private String name;
            private String price;
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

        public static class ThreeBean {
            /**
             * goods_id : 1
             * name : 艺创 青花甜白三才大盖碗功夫茶具敬茶陶瓷泡茶器
             * price : 49.00
             * pay_points : 5000
             * image : /public/uploads/cache/images/osc1/1/1.jpg
             * model : D2016031002
             */

            private String goods_id;
            private String name;
            private String price;
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
}
