package com.android.xjq.bean.myzhuwei;

import java.util.List;

/**
 * Created by kokuma on 2017/11/7.
 */

public class ThemeChildBean {
    private boolean success;
    private String nowDate;

    private boolean jumpLogin;
    private List<FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean> gameBoardList;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public List<FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean> getGameBoardList() {
        return gameBoardList;
    }

    public void setGameBoardList(List<FootballRacesBean.FootballRaceClientSimpleListBean.GameBoardBean> gameBoardList) {
        this.gameBoardList = gameBoardList;
    }

}
