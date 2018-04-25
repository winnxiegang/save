package com.android.xjq.bean.live.main.channel;

import com.android.banana.commlib.bean.PaginatorBean;
import com.android.xjq.bean.live.main.homelive.ChannelListEntity;

import java.util.List;

/**
 * Created by lingjiu on 2017/4/6.
 */

public class ChildChannelListInfo {


    private boolean jumpLogin;
    private String nowDate;
    private PaginatorBean paginator;
    private List<ChannelListEntity> channelInfos;


    public boolean isJumpLogin() {
        return jumpLogin;
    }

    public void setJumpLogin(boolean jumpLogin) {
        this.jumpLogin = jumpLogin;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public List<ChannelListEntity> getChannelInfos() {
        return channelInfos;
    }

    public void setChannelInfos(List<ChannelListEntity> channelInfos) {
        this.channelInfos = channelInfos;
    }

}
