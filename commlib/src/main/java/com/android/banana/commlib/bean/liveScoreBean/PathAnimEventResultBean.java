package com.android.banana.commlib.bean.liveScoreBean;

import com.android.banana.commlib.bean.NormalObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2017/8/21.
 * <p>
 * 动画直播 事件对象
 */

public class PathAnimEventResultBean {
    public ArrayList<AnimEventBean> flashScoreList;
    public long timestamp;

    public static class AnimEventBean {
        /**
         * 动作主体id(teamId,raceId)
         */
        @SerializedName("as")
        public long actionSubject;

        /**
         * 动作时间
         */
        @SerializedName("atime")
        public long actionTime;

        /**
         * 动作类型(进球等)
         */
        @SerializedName("atype")
        public NormalObject actionType;

        /**
         * 客队比分
         */
        @SerializedName("gts")
        public int guestTeamScore;

        /**
         * 主队比分
         */
        @SerializedName("hts")
        public int homeTeamScore;

        /**
         *
         */
        public long id;

        /**
         * 赛事id
         */
        @SerializedName("ri")
        public long raceId;

        /**
         * 赛事状态
         */
        @SerializedName("rs")
        public NormalObject raceStatus;

        /**
         * 赛事时间
         */
        @SerializedName("rt")
        public String raceTime;

        /**
         * 主体类型(TEAM,RACE)
         */
        @SerializedName("st")
        public NormalObject subjectType;
    }

}
