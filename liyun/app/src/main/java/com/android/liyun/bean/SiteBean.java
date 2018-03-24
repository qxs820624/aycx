package com.android.liyun.bean;

import java.util.List;

/**
 * Created by sunwubin on 2017/11/1.
 */

public class SiteBean {

    /**
     * status : 0
     * msg : 查询成功
     * address : [{"name":"2","telephone":"3","address":"4","isdefault":"0","city":"5","country":"6","province":"7"},{"name":"name","telephone":"telephone","address":"address","isdefault":"0","city":"5","country":"6","province":"7"},{"name":"孙武斌","telephone":"18611062775","address":"你好啊","isdefault":"0","city":"5203","country":"520525","province":"52"},{"name":"孙玉斌","telephone":"18611062775","address":"你好你好","isdefault":"0","city":"4201","country":"420102","province":"42"}]
     */

    private String status;
    private String msg;
    private List<AddressBean> address;

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

    public List<AddressBean> getAddress() {
        return address;
    }

    public void setAddress(List<AddressBean> address) {
        this.address = address;
    }

    public static class AddressBean {
        /**
         * name : 2
         * telephone : 3
         * address : 4
         * isdefault : 0
         * city : 5
         * country : 6
         * province : 7
         */

        private String name;
        private String telephone;
        private String address;
        private String isdefault;
        private String city;
        private String country;
        private String province;
        private String addressid;

        public String getAddressid() {
            return addressid;
        }

        public void setAddressid(String addressid) {
            this.addressid = addressid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(String isdefault) {
            this.isdefault = isdefault;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }
}
