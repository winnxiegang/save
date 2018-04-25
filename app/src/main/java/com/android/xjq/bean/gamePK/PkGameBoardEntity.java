package com.android.xjq.bean.gamePK;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/3/1.
 */

public class PkGameBoardEntity {

    public boolean success;
    public List<Integer> multipleList;
    public ArrayList<PkGameBoarInfoBean> pkGameBoardList;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Integer> getMultipleList() {
        return multipleList;
    }

    public void setMultipleList(List<Integer> multipleList) {
        this.multipleList = multipleList;
    }

    public ArrayList<PkGameBoarInfoBean> getPkGameBoardList() {
        return pkGameBoardList;
    }

    public void setPkGameBoardList(ArrayList<PkGameBoarInfoBean> pkGameBoardList) {
        this.pkGameBoardList = pkGameBoardList;
    }

}
