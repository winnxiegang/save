package com.jl.jczj.im.bean;

import com.android.banana.commlib.bean.NormalObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2017/12/11.
 */

public class ChatMsgMedalBean {
    //标签 群主 管理员啥啥啥的
    public String medalConfigCode;
    public String medalLevelConfigCode;
    public String ownerId;
    public String ownerName;
    public List<MedalLabelConfigListBean> labelInfoList;

    //转发过来带的动态勋章
   // public String medalConfigCode;
   // public String medalLevelConfigCode;
    public String userId;
    public NormalObject medalType;

    public ChatMsgMedalBean() {
        labelInfoList = new ArrayList<>();
    }

    public static class MedalLabelConfigListBean {
        /**
         * content : 大佬
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
