package com.android.xjq.bean;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/3/4.
 */

public class AddressListBean {
    public ArrayList<ProvinceCityCounty> areaProvinceList;

    public ArrayList<ProvinceCityCounty> areaCityList;

    public ArrayList<ProvinceCityCounty> areaCountList;

    public ArrayList<ProvinceCityCounty> areaTownList;
    public static class ProvinceCityCounty {

        /**
         * id : 1
         * countryId : 1
         * name : 北京
         * gmtCreate : 2016-03-25 10:07:28
         * gmtModified : 2016-04-06 15:44:06
         * alias : 北京
         * enabled : true
         */

        public String id;
        public String provinceId;
        public String countryId;
        public String cityId;
        public String townId;
        public String name;
        public String gmtCreate;
        public String gmtModified;
        public String alias;
        public boolean enabled;
    }
}
