package com.android.liyun.bean;

import java.util.List;

public class FavoritesListBean {


    /**
     * status : 0
     * msg : 查询成功
     * favorites : [{"name":"力云LIYUN车载充电器8","favorite_id":"27","goods_id":"8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","favoritedatetime":"2018-03-27 09:20:58"},{"name":"力云LIYUN车载充电器8","favorite_id":"28","goods_id":"8","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg","favoritedatetime":"2018-03-27 09:22:32"},{"name":"力云LIYUN车载充电器9","favorite_id":"29","goods_id":"9","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/9/1-230x230.jpg","favoritedatetime":"2018-03-28 07:25:58"},{"name":"力云LIYUN车载充电器9","favorite_id":"30","goods_id":"9","image":"http://219.153.49.223:8088/public/uploads/cache/images/osc1/9/1-230x230.jpg","favoritedatetime":"2018-03-28 07:26:02"}]
     */

    private String status;
    private String msg;
    private List<FavoritesBean> favorites;

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

    public List<FavoritesBean> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<FavoritesBean> favorites) {
        this.favorites = favorites;
    }

    public static class FavoritesBean {
        /**
         * name : 力云LIYUN车载充电器8
         * favorite_id : 27
         * goods_id : 8
         * image : http://219.153.49.223:8088/public/uploads/cache/images/osc1/8/1-230x230.jpg
         * favoritedatetime : 2018-03-27 09:20:58
         */

        private String name;
        private String favorite_id;
        private String goods_id;
        private String image;
        private String favoritedatetime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFavorite_id() {
            return favorite_id;
        }

        public void setFavorite_id(String favorite_id) {
            this.favorite_id = favorite_id;
        }

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

        public String getFavoritedatetime() {
            return favoritedatetime;
        }

        public void setFavoritedatetime(String favoritedatetime) {
            this.favoritedatetime = favoritedatetime;
        }
    }
}
