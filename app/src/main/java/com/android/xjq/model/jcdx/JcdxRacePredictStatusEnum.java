package com.android.xjq.model.jcdx;

/**
 * Created by zaozao on 2017/8/30.
 */

public enum JcdxRacePredictStatusEnum {
    SF_WIN,

    SF_LOST,

    SF_DRAW,

    SF_HALF_WIN,

    SF_HALF_LOST,

    DEFAULT;

    public static JcdxRacePredictStatusEnum safeValueOf(String name) {

        try {
            return JcdxRacePredictStatusEnum.valueOf(name);

        } catch (Exception e) {
        }

        return DEFAULT;
    }
}
