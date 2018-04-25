package com.android.banana.commlib.bean.liveScoreBean;

import com.android.banana.commlib.bean.NormalObject;
import com.google.gson.annotations.Expose;

/**
 * Created by zhouyi on 2016/5/26 10:38.
 */

/**
 * 竞彩足球返回的比分直播，一场比赛的数据
 */
public class JczqDynamicDataBean {

    private int hr;
    private int hti;
    private int hs;
    private int gy;
    private int id;
    private String fst;
    private String sst;
    /**
     * value : 0
     * message : 下半场
     * name : PLAY_S
     */

    private NormalObject s;
    private int gti;
    private int hy;
    private int hhs;
    private int gs;
    private String st;
    private int gr;
    private int hgs;

    @Expose
    private String liveHaveStartTime;//已经开赛的时间从0-90.

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getHti() {
        return hti;
    }

    public void setHti(int hti) {
        this.hti = hti;
    }

    public int getHs() {
        return hs;
    }

    public void setHs(int hs) {
        this.hs = hs;
    }

    public int getGy() {
        return gy;
    }

    public void setGy(int gy) {
        this.gy = gy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFst() {
        return fst;
    }

    public void setFst(String fst) {
        this.fst = fst;
    }

    public String getSst() {
        return sst;
    }

    public void setSst(String sst) {
        this.sst = sst;
    }



    public int getGti() {
        return gti;
    }

    public void setGti(int gti) {
        this.gti = gti;
    }

    public int getHy() {
        return hy;
    }

    public void setHy(int hy) {
        this.hy = hy;
    }

    public int getHhs() {
        return hhs;
    }

    public void setHhs(int hhs) {
        this.hhs = hhs;
    }

    public int getGs() {
        return gs;
    }

    public void setGs(int gs) {
        this.gs = gs;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public int getGr() {
        return gr;
    }

    public void setGr(int gr) {
        this.gr = gr;
    }

    public int getHgs() {
        return hgs;
    }

    public void setHgs(int hgs) {
        this.hgs = hgs;
    }

    public NormalObject getS() {
        return s;
    }

    public void setS(NormalObject s) {
        this.s = s;
    }

    public String getLiveHaveStartTime() {
        return liveHaveStartTime;
    }

    public void setLiveHaveStartTime(String liveHaveStartTime) {
        this.liveHaveStartTime = liveHaveStartTime;
    }
}
