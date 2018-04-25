package com.android.xjq.bean.draw;

import com.android.banana.commlib.bean.BaseOperator;
import com.android.banana.commlib.bean.NormalObject;
import com.android.xjq.view.wheelview.WheelView;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/9.
 */

public class LuckyDrawOpenUserEntity implements BaseOperator {
    public List<String> participateUserIds;
    public HashMap<String, String> userIdAndLoginName;
    public NormalObject issueStatus;
    @Expose
    public ArrayList<PrizeUser> prizeUsers;

    @Override
    public void operatorData() {
        prizeUsers = new ArrayList<>();
        if (participateUserIds != null && participateUserIds.size() > 0) {
            for (String participateUserId : participateUserIds) {
                if (userIdAndLoginName.containsKey(participateUserId)) {
                    String userName = userIdAndLoginName.get(participateUserId);
                    PrizeUser prizeUser = new PrizeUser(userName, participateUserId);
                    prizeUsers.add(prizeUser);
                }
            }
        }

        for (int i = prizeUsers.size(); i < 7; i++) {
            prizeUsers.add(prizeUsers.get(i - prizeUsers.size()));
        }
    }


    public class PrizeUser implements WheelView.WheelItemText {
        public String userName;
        public String userId;

        public PrizeUser(String userName, String userId) {
            this.userId = userId;
            this.userName = userName;
        }

        @Override
        public String getText() {
            return userName;
        }
    }
}
