package com.android.xjq.bean.medal;

import java.util.List;

/**
 * Created by ajiao on 2017/10/11 0011.
 */

public class MedalInfoBean {
    /**
     * labelInfoList : [{"content":"胖胖"}]
     * medalConfigCode : FAN_MEDAL
     * medalLevelConfigCode : Lv4
     * ownerId : 8201703300470001
     * ownerName : 糯米团子_LIVE
     */

    private String medalConfigCode;
    private String medalLevelConfigCode;
    private String ownerId;
    private String ownerName;
    private List<LabelInfoListBean> labelInfoList;

    public String getMedalConfigCode() {
        return medalConfigCode;
    }

    public void setMedalConfigCode(String medalConfigCode) {
        this.medalConfigCode = medalConfigCode;
    }

    public String getMedalLevelConfigCode() {
        return medalLevelConfigCode;
    }

    public void setMedalLevelConfigCode(String medalLevelConfigCode) {
        this.medalLevelConfigCode = medalLevelConfigCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<LabelInfoListBean> getLabelInfoList() {
        return labelInfoList;
    }

    public void setLabelInfoList(List<LabelInfoListBean> labelInfoList) {
        this.labelInfoList = labelInfoList;
    }

    public static class LabelInfoListBean {
        /**
         * content : 胖胖
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
