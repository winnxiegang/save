package com.android.xjq.bean.live.main;

import com.android.xjq.bean.live.BaseOperator;
import com.android.banana.commlib.LoginInfoHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/3/6 14:09.
 */
public class UserRoleInfoBean implements BaseOperator {


    private String defaultUserId;
    private List<UserRoleInfo> userSimples;

    @Override
    public void operatorData() {
        if (userSimples != null && userSimples.size() > 0) {
            for (UserRoleInfo userSimple : userSimples) {
                if (userSimple.getUserId().equals(LoginInfoHelper.getInstance().getUserId())) {
                    userSimple.setCurrentRole(true);
                }

                if (userSimple.getUserId().equals(defaultUserId)) {

                    userSimple.setDefaultRole(true);
                }
                //添加勋章图标
                HashMap<String, String> thirdChannelAndLogoUrlMap = userSimple.getThirdChannelAndLogoUrlMap();
                if (thirdChannelAndLogoUrlMap != null && thirdChannelAndLogoUrlMap.size() > 0) {
                    List<String> userMedalList = new ArrayList<>();
                    for (String key : thirdChannelAndLogoUrlMap.keySet()) {
                        userMedalList.add(thirdChannelAndLogoUrlMap.get(key));
                    }
                    userSimple.setUserMedalList(userMedalList);
                }

            }
        }

    }

    public String getDefaultUserId() {
        return defaultUserId;
    }

    public void setDefaultUserId(String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    public List<UserRoleInfo> getUserSimples() {
        return userSimples;
    }

    public void setUserSimples(List<UserRoleInfo> userSimples) {
        this.userSimples = userSimples;
    }

}
