package com.android.xjq.bean.live.main.banner;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Duzaoqiu on 2016/7/22 15:51.
 */
public class BannerEntity{


    private List<BannerInfoEntity> infoClientSimpleList;

    public void setInfoClientSimpleList(List<BannerInfoEntity> infoClientSimpleList) {
        this.infoClientSimpleList = infoClientSimpleList;
    }

    public List<BannerInfoEntity> getInfoClientSimpleList() {
        return infoClientSimpleList;
    }

    public static class BannerInfoEntity {
        private String id;
        private String title;
        private String imageUrl;
        private String channelTitle;
        private String linkUrl;
        private HashMap<String,String> propMap;

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public HashMap<String, String> getPropMap() {
            return propMap;
        }

        public void setPropMap(HashMap<String, String> propMap) {
            this.propMap = propMap;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }
    }
}
