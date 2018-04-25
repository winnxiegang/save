package com.android.xjq.bean.scheduledetail;

import com.android.banana.commlib.bean.liveScoreBean.JczqDataBean;
import com.android.banana.commlib.bean.liveScoreBean.TeamImageUrlUtils;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaozao on 2018/2/6.
 * 用处：赛程详情页伤停
 */

public class JclqInjuryBean{

    /**
     * nowDate : 2018-02-06 12:25:15
     * raceInjury : {"gti":[],"hti":[{"as":16,"cc":6,"detail":"技术犯规","gr":"2018-02-05 00:00:00","id":1063,"pn":"钱","pos":"中锋","status":"停赛","ti":6814,"tn":"国王"},{"as":0,"cc":0,"detail":"-","gr":"2018-02-05 00:00:00","id":1263,"pn":"11","pos":"中锋","status":"status","ti":6814,"tn":"国王"},{"as":0,"cc":0,"detail":"test","gr":"2018-02-05 00:00:00","id":1264,"pn":"47","pos":"前锋","status":"status","ti":6814,"tn":"国王"},{"detail":"-","gr":"2018-02-05 00:00:00","id":1265,"pn":"4747","pos":"前锋","status":"status","ti":6814,"tn":"国王"},{"detail":"test123456","gr":"2018-02-05 00:00:00","id":1266,"pn":"哈哈","pos":"后卫","status":"test","ti":6814,"tn":"国王"}]}
     * success : true
     */


    private RaceInjuryBean raceInjury;
    @Expose
    private List<PlayInjuryBean> injuryBeanList = new ArrayList<>();


    public RaceInjuryBean getRaceInjury() {
        return raceInjury;
    }

    public void setRaceInjury(RaceInjuryBean raceInjury) {
        this.raceInjury = raceInjury;
    }

    public List<PlayInjuryBean> getInjuryBeanList() {
        return injuryBeanList;
    }

    public void setInjuryBeanList(List<PlayInjuryBean> injuryBeanList) {
        this.injuryBeanList = injuryBeanList;
    }

    public void operatorData(JczqDataBean jczqDataBean) {
        if(raceInjury!=null){
            if(raceInjury.getHti()==null||raceInjury.getHti().size()==0){
                PlayInjuryBean injuryBean = new PlayInjuryBean();
                injuryBean.showEmpty = true;
                raceInjury.getHti().add(injuryBean);
            }
            String guestLogoUrl  = TeamImageUrlUtils.getBTGuestLogoUrl(jczqDataBean.getInnerGuestTeamId());

            String homeTeamLogoUrl =TeamImageUrlUtils.getBTHomeLogoUrl((jczqDataBean.getInnerHomeTeamId()));

            raceInjury.getHti().get(0).showTeamTitle =true;
            raceInjury.getHti().get(0).showHomeTeamName = true;
            raceInjury.getHti().get(0).setTn(jczqDataBean.getHomeTeamName());
            raceInjury.getHti().get(0).logoUrl =homeTeamLogoUrl;
            if(raceInjury.getGti()==null||raceInjury.getGti().size()==0){

                PlayInjuryBean injuryBean = new PlayInjuryBean();
                injuryBean.showEmpty = true;
                raceInjury.getGti().add(injuryBean);
            }
            raceInjury.getGti().get(0).showTeamTitle =true;
            raceInjury.getGti().get(0).showHomeTeamName = true;
            raceInjury.getGti().get(0).setTn(jczqDataBean.getGuestTeamName());
            raceInjury.getGti().get(0).setTi(jczqDataBean.getInnerGuestTeamId());
            raceInjury.getGti().get(0).logoUrl = guestLogoUrl;
            injuryBeanList.addAll(raceInjury.getHti());
            injuryBeanList.addAll(raceInjury.getGti());
        }
    }

    public static class RaceInjuryBean {
        private List<PlayInjuryBean> gti;
        private List<PlayInjuryBean> hti;

        public List<PlayInjuryBean> getGti() {
            return gti;
        }

        public void setGti(List<PlayInjuryBean> gti) {
            this.gti = gti;
        }

        public List<PlayInjuryBean> getHti() {
            return hti;
        }

        public void setHti(List<PlayInjuryBean> hti) {
            this.hti = hti;
        }

    }

    public class PlayInjuryBean{
        private long id;
        // 赛事id
        private long ri;
        //球队id
        private long ti;
        //球队名字
        private String tn;
        //球员名字
        private String pn;
        //球员id
        private long pid;
        //位置
        private String pos;
        //场次
        private int cc;
        //进球数
        private int goal;
        //状态
        private String status;
        //备注
        private String detail;
        //平均评分
        private double as;
        //球衣号
        private int sn;
        @Expose
        private  boolean showHomeTeamName = false;

        @Expose
        public boolean isHaveExpand =false;

        @Expose
        public boolean showTeamTitle =false;
        @Expose
        public boolean showEmpty =false;
        @Expose
        public String logoUrl;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getTi() {
            return ti;
        }

        public void setTi(long ti) {
            this.ti = ti;
        }

        public String getTn() {
            return tn;
        }

        public void setTn(String tn) {
            this.tn = tn;
        }

        public String getPn() {
            return pn;
        }

        public void setPn(String pn) {
            this.pn = pn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getSn() {
            return sn;
        }

        public void setSn(int sn) {
            this.sn = sn;
        }
    }

}
