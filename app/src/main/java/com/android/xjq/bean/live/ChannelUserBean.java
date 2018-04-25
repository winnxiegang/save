package com.android.xjq.bean.live;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/12.
 */
public class ChannelUserBean {
    /**
     * levelCode : lv9
     * levelImageUrl : http://livemapi.huohongshe.net/userLevelImg.resource?levelCode=lv9
     * thirdChannelList : []
     * userId : 8201703290460008
     * userName : 苏氏二货
     */

    public String levelCode;
    public String levelImageUrl;
    public String userId;
    public String loginName;
    public HashMap<String, String> thirdChannelAndLogoUrlMap;
    public String userLogoUrl;
    @Expose
    public List<String> userMedalList;
    @Expose
    public List<String> userHorseList;
}
