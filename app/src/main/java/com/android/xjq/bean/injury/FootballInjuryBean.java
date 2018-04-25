package com.android.xjq.bean.injury;

import com.android.banana.commlib.bean.PaginatorBean;

import java.util.List;
import java.util.Map;

/**
 * Created by DaChao on 2017/11/30.
 */

public class FootballInjuryBean {

    public String nowDate;
    public PaginatorBean paginator;
    public List<Match> raceInfoAndInjuryList;

    public static class Match {

        //开赛时间
        public String gs;

        //比赛名字
        public String mn;

        //主队id
        public long htid;

        //主队名称
        public String htn;

        //主队伤停人员详情列表
        public List<Player> hlist;

        //客队id
        public int gtid;

        //客队名称
        public String gtn;

        //客队伤停详情列表
        public List<Player> glist;

        public static class Player {

            //球员名字
            public String pn;

            //位置
            public Map<String, Object> pos;

            //场次
            public int po;

            //进球数
            public int goal;

            //状态
            public String status;

            //备注损伤详情
            public String detail;

//            public static class Position {
//
//                //位置描述
//                public String message;
//
//                //英文描述
//                public String name;
//
//                public int value;
//
//            }

        }

    }

}
