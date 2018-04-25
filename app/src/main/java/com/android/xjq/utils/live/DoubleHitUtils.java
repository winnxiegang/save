package com.android.xjq.utils.live;

import com.android.xjq.bean.live.DoubleHitShowBean;
import com.android.xjq.bean.live.main.gift.GiftShowConfigBean;
import com.android.xjq.view.LandscapeGiftShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2017/4/11.
 */

public class DoubleHitUtils {

    private List<DoubleHitShowBean> list = new ArrayList<>();

    private LandscapeGiftShow doubleHitView;

    //礼物显示配置信息
    private GiftShowConfigBean giftShowConfigBean;

    public DoubleHitUtils(LandscapeGiftShow doubleHitView) {
        this.doubleHitView = doubleHitView;
    }


    //移除老数据(如果当前数据存在list中大于30秒，则需要移除)
    public void removeOldData() {
        long currentTime = System.currentTimeMillis();
        List<DoubleHitShowBean> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DoubleHitShowBean bean = list.get(i);
            long diffTime = currentTime - bean.getAddTime();
            if (diffTime / 1000 >= 30 && bean.getAllDoubleHitCount() == bean.getCurrentShowCount()) {
                tempList.add(bean);
            }
        }
        list.removeAll(tempList);
    }

    //增加数据
    public void addData(DoubleHitShowBean addBean) {

        for (DoubleHitShowBean bean : list) {
            //id相同且当前还未显示完成
            if (bean.getId().equals(addBean.getId())) {
                if (bean.getAllDoubleHitCount() < addBean.getAllDoubleHitCount()) {
                    if (bean.getShowStatusEnum() == LandscapeGiftShow.DoubleHitShowStatusEnum.FINISH) {
                        bean.setAllDoubleHitCount(addBean.getAllDoubleHitCount());
                        bean.setShowStatusEnum(LandscapeGiftShow.DoubleHitShowStatusEnum.WAIT);
                        doubleHitView.showDoubleHit(bean);
                    } else {
                        bean.setAllDoubleHitCount(addBean.getAllDoubleHitCount());
                    }
                }
                if (giftShowConfigBean != null) giftShowConfigBean.setDoubleHitCount(bean);
                return;
            }
        }
        if (addBean.getAllDoubleHitCount() > 10) {
            addBean.setCurrentShowCount(addBean.getAllDoubleHitCount() - 8);
        }
        if (giftShowConfigBean != null) giftShowConfigBean.setDoubleHitCount(addBean);
        list.add(addBean);
        doubleHitView.showDoubleHit(addBean);
        removeOldData();
    }


    public void setGiftShowConfig(GiftShowConfigBean giftShowConfigBean) {
        this.giftShowConfigBean = giftShowConfigBean;

        doubleHitView.setBaseConfig(giftShowConfigBean.getShowCount(), giftShowConfigBean.getBannerIntervalSeconds());
    }
}
