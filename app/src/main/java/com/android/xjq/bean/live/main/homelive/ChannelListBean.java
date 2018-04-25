package com.android.xjq.bean.live.main.homelive;

import com.android.banana.commlib.bean.PaginatorBean;
import com.android.xjq.bean.live.BaseOperator;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/3/30.
 */

public class ChannelListBean implements BaseOperator {


    private boolean jumpLogin;

    private String nowDate;

    private PaginatorBean paginator;

    private List<ChannelListEntity> resultList;

    private HashMap<String, Boolean> haveCouponChannelIdMap;

    @Override
    public void operatorData() {
        if (haveCouponChannelIdMap != null && haveCouponChannelIdMap.size() > 0) {
            if (resultList != null & resultList.size() > 0) {
                for (ChannelListEntity channelListEntity : resultList) {
                    if (haveCouponChannelIdMap.containsKey(String.valueOf(channelListEntity.getId()))) {
                        channelListEntity.setHasCoupon(true);
                    }
                }
            }
        }
    }

    public HashMap<String, Boolean> getHaveCouponChannelIdMap() {
        return haveCouponChannelIdMap;
    }

    public void setHaveCouponChannelIdMap(HashMap<String, Boolean> haveCouponChannelIdMap) {
        this.haveCouponChannelIdMap = haveCouponChannelIdMap;
    }

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

    public List<ChannelListEntity> getResultList() {
        return resultList;
    }

    public void setResultList(List<ChannelListEntity> resultList) {
        this.resultList = resultList;
    }

}
