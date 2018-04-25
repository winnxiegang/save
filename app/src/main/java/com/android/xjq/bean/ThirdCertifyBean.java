package com.android.xjq.bean;

import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/7/28.
 */

public class ThirdCertifyBean implements BaseOperator {

    /**
     * certifyUrlMap : {"OAP":"http://m.50jjx.net/extra/service.htm?sign=BaOW3st8TAxChKi4dHljjL4XHYL4z5NGS4VnobN8wgopC8z6NTC18qKA3yN8mVmDF6H7zh2QZbcB%0D%0ACzcjTSoXeZ8FFYYbUjX%2BHd3veS25HWT7gtDlyYh8W5kAiXQ4FFWia%2Feg58rAT4ZHAOvItUun7fV%2F%0D%0AoBuBApY8oCOgNq76DtWCsNAqkiODGpy0WM%2F0WgVYzsQZ5fU%2B9lTPFCDWjLCfbfrtEmW84S0s5QIO%0D%0AA5Nnh003nEJ4GYEq3ld%2BA%2FLkdbtZOFRLTz3NkC4Fc6m9npb11EoaItE5IsogVHDP4ba%2BrjK0aJSX%0D%0A3F2VqA%2FyltYI7uhoBZE%2BWDX2u7gEsV%2FYe4tNfQ%3D%3D%0D%0A&timestamp=20170728172245&returnUrl=http%3A%2F%2Flivemweb.huohongshe.net%2FafterThirdCerify.htm%3FauthedUserId%3D8201704060510016&authState=from_live&signType=RSA&appCode=live&service=USER_ROLE_AUTH_LOGIN"}
     * jumpLogin : false
     * nowDate : 2017-07-28 17:22:45
     * success : true
     * thirdChannelAndLogoUrlMap : {"OAP":"http://img-livemapi.huohongshe.net/auth/oap.png"}
     * thirdUserMappingConfigs : [{"id":1,"thirdChannelCode":"OAP","thirdChannelName":"寄居蟹"}]
     * thirdUserMappingMap : {}
     */

    private HashMap<String, String> certifyUrlMap;
    private HashMap<String, String> thirdChannelAndLogoUrlMap;
    //已绑定的第三方
    private HashMap<String,ThirdUserMapBean> thirdUserMappingMap;
    private List<ThirdUserMappingConfigsBean> thirdUserMappingConfigs;

    @Override
    public void operatorData() {
        if (thirdUserMappingConfigs != null && thirdUserMappingConfigs.size() > 0) {
            for (ThirdUserMappingConfigsBean bean : thirdUserMappingConfigs) {
                if (certifyUrlMap != null && certifyUrlMap.containsKey(bean.getThirdChannelCode())) {
                    bean.setCertifyUrl(certifyUrlMap.get(bean.getThirdChannelCode()));
                }
                if (thirdChannelAndLogoUrlMap != null && thirdChannelAndLogoUrlMap.containsKey(bean.getThirdChannelCode())) {
                    bean.setLogoUrl(thirdChannelAndLogoUrlMap.get(bean.getThirdChannelCode()));
                }
                if (thirdUserMappingMap != null && thirdUserMappingMap.containsKey(bean.getThirdChannelCode())) {
                    bean.setHasBind(true);
                    bean.setBindUserName(thirdUserMappingMap.get(bean.getThirdChannelCode()).getThirdLoginName());
                }
            }
        }
    }


    public HashMap<String, ThirdUserMapBean> getThirdUserMappingMap() {
        return thirdUserMappingMap;
    }

    public void setThirdUserMappingMap(HashMap<String, ThirdUserMapBean> thirdUserMappingMap) {
        this.thirdUserMappingMap = thirdUserMappingMap;
    }

    public HashMap<String, String> getCertifyUrlMap() {
        return certifyUrlMap;
    }

    public void setCertifyUrlMap(HashMap<String, String> certifyUrlMap) {
        this.certifyUrlMap = certifyUrlMap;
    }

    public HashMap<String, String> getThirdChannelAndLogoUrlMap() {
        return thirdChannelAndLogoUrlMap;
    }

    public void setThirdChannelAndLogoUrlMap(HashMap<String, String> thirdChannelAndLogoUrlMap) {
        this.thirdChannelAndLogoUrlMap = thirdChannelAndLogoUrlMap;
    }

    public List<ThirdUserMappingConfigsBean> getThirdUserMappingConfigs() {
        return thirdUserMappingConfigs;
    }

    public void setThirdUserMappingConfigs(List<ThirdUserMappingConfigsBean> thirdUserMappingConfigs) {
        this.thirdUserMappingConfigs = thirdUserMappingConfigs;
    }


    public static class ThirdUserMapBean {
        private String thirdChannel;
        private String  thirdLoginName;
        private String  thirdUserId;

        public String getThirdChannel() {
            return thirdChannel;
        }

        public void setThirdChannel(String thirdChannel) {
            this.thirdChannel = thirdChannel;
        }

        public String getThirdLoginName() {
            return thirdLoginName;
        }

        public void setThirdLoginName(String thirdLoginName) {
            this.thirdLoginName = thirdLoginName;
        }

        public String getThirdUserId() {
            return thirdUserId;
        }

        public void setThirdUserId(String thirdUserId) {
            this.thirdUserId = thirdUserId;
        }
    }

    public static class ThirdUserMappingConfigsBean {
        /**
         * id : 1
         * thirdChannelCode : OAP
         * thirdChannelName : 寄居蟹
         */

        private int id;
        private String thirdChannelCode;
        private String thirdChannelName;
        @Expose
        private String logoUrl;
        @Expose
        private String certifyUrl;
        @Expose
        private boolean isHasBind;
        @Expose
        private String bindUserName;

        public String getBindUserName() {
            return bindUserName;
        }

        public void setBindUserName(String bindUserName) {
            this.bindUserName = bindUserName;
        }

        public boolean isHasBind() {
            return isHasBind;
        }

        public void setHasBind(boolean hasBind) {
            isHasBind = hasBind;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getCertifyUrl() {
            return certifyUrl;
        }

        public void setCertifyUrl(String certifyUrl) {
            this.certifyUrl = certifyUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getThirdChannelCode() {
            return thirdChannelCode;
        }

        public void setThirdChannelCode(String thirdChannelCode) {
            this.thirdChannelCode = thirdChannelCode;
        }

        public String getThirdChannelName() {
            return thirdChannelName;
        }

        public void setThirdChannelName(String thirdChannelName) {
            this.thirdChannelName = thirdChannelName;
        }
    }
}
