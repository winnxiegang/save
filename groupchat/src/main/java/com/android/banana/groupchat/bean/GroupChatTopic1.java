package com.android.banana.groupchat.bean;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

/**
 * Created by qiaomu on 2017/11/6.
 */

public class GroupChatTopic1 {
    public Map<String, JczqDataBean> footballRaceMap;
    public Map<String, JczqDataBean> basketballRaceMap;
    public List<RaceIdAndGameBoard> gameBoardList;
    //直播间内的
    public RaceIdAndGameBoard defaultGameBoard;
    //礼物倍数选择
    public List<Integer> multipleList;
    public int themeMaxMum;

    /*篮球足球基本信息*/
//    public static class Basketball {
//
//        /**
//         * raceStatus : {"name":"FINISH","message":"完场"}
//         * sectionCount : 4
//         * homeScoreSection1 : 0
//         * guestScoreSection1 : 0
//         * homeScoreSection2 : 0
//         * guestScoreSection2 : 0
//         * homeScoreSection3 : 0
//         * guestScoreSection3 : 0
//         * homeScoreSection4 : 0
//         * guestScoreSection4 : 0
//         * id : 2650349723207890980015447300
//         * innerRaceId : 434380
//         * innerGuestTeamId : 6818
//         * innerHomeTeamId : 6815
//         * innerMatchId : 401
//         * matchName : NBA
//         * guestTeamName : 雷霆
//         * homeTeamName : 开拓者
//         * halfHomeScore : -1
//         * halfGuestScore : -1
//         * fullGuestScore : -1
//         * fullHomeScore : -1
//         * gmtStart : 2017-11-06 10:00:00
//         */
//
//        public NormalObject raceStatus;
//        public int sectionCount;
//        public int homeScoreSection1;
//        public int guestScoreSection1;
//        public int homeScoreSection2;
//        public int guestScoreSection2;
//        public int homeScoreSection3;
//        public int guestScoreSection3;
//        public int homeScoreSection4;
//        public int guestScoreSection4;
//        public String id;
//        public int innerRaceId;
//        public int innerGuestTeamId;
//        public int innerHomeTeamId;
//        public int innerMatchId;
//        public String matchName;
//        public String guestTeamName;
//        public String homeTeamName;
//        public int halfHomeScore;
//        public int halfGuestScore;
//        public int fullGuestScore;
//        public int fullHomeScore;
//        public String gmtStart;
//
//    }

    /*热力值*/
    public static class Thermodynamic {

        /**
         * id : 2660222263607890980017863674
         * boardId : 2660222263107890980012080330
         * optionCode : GUEST_WIN
         * optionName : 客胜
         * enabled : true
         * gmtCreate : 2017-11-04 21:30:22
         * gmtModified : 2017-11-04 21:30:22
         * totalFee : 0
         * totalPlayUserCount : 0
         * totalPlayOrderCount : 0
         * totalPaidFee:0
         */

        public String id;
        public String boardId;
        public String optionCode;
        public String optionName;
        public boolean enabled;
        public String gmtCreate;
        public String gmtModified;
        public double totalFee;
        public double totalPaidFee;
        public int totalPlayUserCount;
        public int totalPlayOrderCount;
        //助威的礼物信息
        //public BetGiftEntity.BetGiftBean gifInfo;

        public String betFormImageUrl;
        public String betFormName;
        public String betFormSingleFee;
        public String betFormNo;

    }


    /*赛事信息 */
    public static class RaceIdAndGameBoard {
        /**
         * id : 2660222263107890980012080330
         * raceType : BASKETBALL
         * raceId : 2650349723207890980015447300
         * plate : 1.5
         * raceStageType : {"id":2,"code":"FIRST_HALF","name":"上半场","orderNum":2,"enabled":true,"gmtCreate":"2017-11-02 16:55:01","gmtModified":"2017-11-02 16:55:01"}
         * gmtGameStart : 2017-11-04 21:30:22
         * gmtGamePause : 2017-11-07 21:30:11
         * saleStatus : {"name":"PROGRESSING","message":"进行中"}
         * optionGroup : {"id":2,"code":"RFSF","name":"让分胜负玩法","minCount":1,"maxCount":1,"enabled":true,"gmtCreate":"2017-11-02 16:40:07","gmtModified":"2017-11-02 16:40:07"}
         * lotteryStatus : {"name":"INIT","message":"初始"}
         * prizeStatus : WAIT
         * gmtCreate : 2017-11-04 21:30:22
         * gmtModified : 2017-11-04 21:30:22
         * minOptionCount : 1
         * maxOptionCount : 1
         */
        public List<Thermodynamic> gameBoardOptionEntries;
        public String id;
        public String raceType;
        public String raceId;
        public double plate;
        public String gameBoardId;
        public RaceStageTypeBean raceStageType;
        public String gmtGameStart;
        public String gmtGamePause;
        public NormalObject saleStatus;
        public String optionGroup;
        public NormalObject lotteryStatus;
        public NormalObject prizeStatus;
        public String gmtCreate;
        public String gmtModified;
        public int minOptionCount;
        public int maxOptionCount;
        //左边选项信息
        public Thermodynamic leftGameBoardOptionEntry;
        //右边选项信息
        public Thermodynamic rightGameBoardOptionEntry;
        @Expose
        public String homeName;
        @Expose
        public String guestName;

        /*赛事 状态*/
        public static class RaceStageTypeBean {
            /**
             * id : 2
             * code : FIRST_HALF
             * name : 上半场
             * orderNum : 2
             * enabled : true
             * gmtCreate : 2017-11-02 16:55:01
             * gmtModified : 2017-11-02 16:55:01
             */

            public int id;
            public String code;
            public String name;
            public int orderNum;
            public boolean enabled;
            public String gmtCreate;
            public String gmtModified;
        }

    }
}
