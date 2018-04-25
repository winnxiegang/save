package com.android.xjq.bean.injury;

import java.util.List;
import java.util.Map;

/**
 * Created by DaChao on 2017/11/30.
 */

public class BasketballInjuryBean {

    public String nowDate;
    public Map<Integer, Team> teamInjuryMap;

    public List<Team> teamInjuryList;

    public static class Team {

        private String teamId;//没什么用，和里面的ti一个意思

        private String teamName;//没什么用，和里面的tn一个意思

        public List<Player> injuryList;

        public static class Player {

            //球队id
            public long ti;

            //球队名字
            public String tn;

            //球员id
            public long id;

            //球员名字
            public String pn;

            //位置
            public String pos;

            //场次
            public int cc;

            //场均得分
            public int as;

            //状态
            public String status;

            //备注
            public String detail;

            //刷新时间
            public String gr;

        }
    }

}
