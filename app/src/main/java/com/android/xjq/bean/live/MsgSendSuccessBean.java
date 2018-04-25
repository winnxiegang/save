package com.android.xjq.bean.live;

import com.android.xjq.bean.medal.MedalInfoBean;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/19.
 */

public class MsgSendSuccessBean {
    public List<String> roleCodes;
    public List<String> fontColorList;
    public String userVotedContent;
    public List<MedalInfoBean> medalInfos;
    public List<MedalInfoBean> achieves;
    @Expose
    List<MedalInfoBean> medalInfoBeanList;

    public List<MedalInfoBean> getMedalInfoBeanList() {
        if (medalInfoBeanList != null) return medalInfoBeanList;
        medalInfoBeanList = new ArrayList<>();
        if (medalInfos != null)
            medalInfoBeanList.addAll(medalInfos);
        if (achieves != null)
            medalInfoBeanList.addAll(achieves);
        return medalInfoBeanList;
    }

}
