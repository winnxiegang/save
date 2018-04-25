package com.android.xjq.bean.live;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/9/13.
 */

public class ChannelUserConfigBean implements BaseOperator {

    private int chatIntervalSeconds;
    private int chatWords;
    private boolean canSendUrl;
    private HashMap<String, String> fontColorAndMemoMap;
    private List<String> fontColorList;
    private List<String> allFontColorList;
    @Expose
    private List<ChannelBubbleConfigBean> channelBubbleConfigList;


    @Override
    public void operatorData() {
        channelBubbleConfigList = new ArrayList<>();
        if (allFontColorList == null || allFontColorList.size() == 0) return;
        for (String fontColor : allFontColorList) {
            ChannelBubbleConfigBean bean = new ChannelBubbleConfigBean();
            bean.setFontColor(fontColor);
            if (fontColorAndMemoMap != null && fontColorAndMemoMap.containsKey(fontColor)) {
                String memo = fontColorAndMemoMap.get(fontColor);
                bean.setMemo(memo);
            }
            if (fontColorList != null) {
                if (fontColorList.contains(fontColor)) {
                    bean.setEnable(true);
                }
            }
            channelBubbleConfigList.add(bean);
        }
    }

    public List<ChannelBubbleConfigBean> getChannelBubbleConfigList() {
        return channelBubbleConfigList;
    }

    public void setChannelBubbleConfigList(List<ChannelBubbleConfigBean> channelBubbleConfigList) {
        this.channelBubbleConfigList = channelBubbleConfigList;
    }

    public HashMap<String, String> getFontColorAndMemoMap() {
        return fontColorAndMemoMap;
    }

    public void setFontColorAndMemoMap(HashMap<String, String> fontColorAndMemoMap) {
        this.fontColorAndMemoMap = fontColorAndMemoMap;
    }

    public int getChatIntervalSeconds() {
        return chatIntervalSeconds;
    }

    public void setChatIntervalSeconds(int chatIntervalSeconds) {
        this.chatIntervalSeconds = chatIntervalSeconds;
    }

    public int getChatWords() {
        return chatWords;
    }

    public void setChatWords(int chatWords) {
        this.chatWords = chatWords;
    }

    public boolean isCanSendUrl() {
        return canSendUrl;
    }

    public void setCanSendUrl(boolean canSendUrl) {
        this.canSendUrl = canSendUrl;
    }

    public List<String> getFontColorList() {
        return fontColorList;
    }

    public void setFontColorList(List<String> fontColorList) {
        this.fontColorList = fontColorList;
    }

    public List<String> getAllFontColorList() {
        return allFontColorList;
    }

    public void setAllFontColorList(List<String> allFontColorList) {
        this.allFontColorList = allFontColorList;
    }

    public static class ChannelBubbleConfigBean {
        private String fontColor;
        private String memo;
        private boolean isEnable;
        private int resId;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public boolean isEnable() {
            return isEnable;
        }

        public void setEnable(boolean enable) {
            isEnable = enable;
        }
    }
}
