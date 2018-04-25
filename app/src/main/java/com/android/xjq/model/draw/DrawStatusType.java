package com.android.xjq.model.draw;

/**
 * Created by lingjiu on 2018/3/7.
 */

public @interface DrawStatusType {
    //初始
    String INIT = "INIT";
    //抽奖进行中
    String WAIT_DRAW = "WAIT_DRAW";
    //结束抽奖
    String FINISH = "FINISH";

}
