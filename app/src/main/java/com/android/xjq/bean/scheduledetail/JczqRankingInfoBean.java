package com.android.xjq.bean.scheduledetail;

import com.google.gson.annotations.Expose;

/**
 * Created by lingjiu on 2017/1/20 10:27.
 */
public class JczqRankingInfoBean {

    private int mc;
    private int md;
    private int ig;
    private int lg;
    private int ml;
    private int mp;
    private String re;
    private int tr;
    private int rg;
    private int ti;
    private String tn;
    private int mw;
    private String cl;//队伍所属赛制颜色十六进制

    /**
     * 队伍的类型(主队，客队，都不是),用来显示主客不一样的背景色
     */
    @Expose
    private TeamType teamType;

    public static enum TeamType {
        HOME,
        GUEST,
        NORMAL
    }

    public TeamType getTeamType() {
        return teamType;
    }

    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;
    }


    public int getMc() {
        return mc;
    }

    public void setMc(int mc) {
        this.mc = mc;
    }

    public int getMd() {
        return md;
    }

    public void setMd(int md) {
        this.md = md;
    }

    public int getIg() {
        return ig;
    }

    public void setIg(int ig) {
        this.ig = ig;
    }

    public int getLg() {
        return lg;
    }

    public void setLg(int lg) {
        this.lg = lg;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getRg() {
        return rg;
    }

    public void setRg(int rg) {
        this.rg = rg;
    }

    public int getTi() {
        return ti;
    }

    public void setTi(int ti) {
        this.ti = ti;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public int getMw() {
        return mw;
    }

    public void setMw(int mw) {
        this.mw = mw;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getCl() {
        return cl;
    }
}
