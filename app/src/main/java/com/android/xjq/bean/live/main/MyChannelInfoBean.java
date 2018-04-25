package com.android.xjq.bean.live.main;

import com.android.xjq.bean.live.BaseOperator;
import com.android.xjq.bean.live.main.homelive.ChannelListEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lingjiu on 2017/4/1.
 */

public class MyChannelInfoBean implements BaseOperator {

    private List<ChannelListEntity> channelInfoSimpleList;

    private HashMap<Integer, String> channelIdAndUserIdentityMap;


    @Override
    public void operatorData() {
        if (channelIdAndUserIdentityMap != null && channelIdAndUserIdentityMap.size() > 0) {
            if (channelInfoSimpleList != null && channelInfoSimpleList.size() > 0) {
                for (ChannelListEntity channelListEntity : channelInfoSimpleList) {
                    if (channelIdAndUserIdentityMap.containsKey(channelListEntity.getId())) {
                        ArrayList<String> horseList = new ArrayList<>();
                        horseList.add(channelIdAndUserIdentityMap.get(channelListEntity.getId()));
                        channelListEntity.setHorseList(horseList);
                    }
                }
            }

        }
    }

    public HashMap<Integer, String> getChannelIdAndUserIdentityMap() {
        return channelIdAndUserIdentityMap;
    }

    public void setChannelIdAndUserIdentityMap(HashMap<Integer, String> channelIdAndUserIdentityMap) {
        this.channelIdAndUserIdentityMap = channelIdAndUserIdentityMap;
    }

    public List<ChannelListEntity> getChannelInfoSimpleList() {
        return channelInfoSimpleList;
    }

    public void setChannelInfoSimpleList(List<ChannelListEntity> channelInfoSimpleList) {
        this.channelInfoSimpleList = channelInfoSimpleList;
    }

}
