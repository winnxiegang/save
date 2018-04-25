package com.etiennelawlor.imagegallery.library.entity;

import java.util.List;

/**
 * Created by zhouyi on 2015/12/23 10:33.
 */
public class StoreSelectBean {

    public enum StoreSubType{

        JCLQ_MATCH,

        JCZQ_MACTH

    }

    public static final int STORE_IMAGE = 1;

    public static final int STORE_PURCHASE = 2;

    public static final int STORE_MATCH = 3;

    private int storeType;

    private StoreSubType storeSubType;

    private List<? extends Object> list;

    public StoreSelectBean(int storeType, List<? extends Object> list) {

        this.storeType = storeType;

        this.list = list;

    }



    public List<? extends Object> getList() {
        return list;
    }

    public void setList(List<Photo> list) {
        this.list = list;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    public StoreSubType getStoreSubType() {
        return storeSubType;
    }

    public void setStoreSubType(StoreSubType storeSubType) {
        this.storeSubType = storeSubType;
    }
}
