package com.tencent.livesdk;

/**
 * Created by willguo on 17/3/1.
 */

public class ILVChangeRoleRes {
    private int micRes;
    private int camRes;

    public ILVChangeRoleRes(int micRes, int camRes) {
        this.micRes = micRes;
        this.camRes = camRes;
    }

    public int getMicRes() {
        return micRes;
    }

    public void setMicRes(int micRes) {
        this.micRes = micRes;
    }

    public int getCamRes() {
        return camRes;
    }

    public void setCamRes(int camRes) {
        this.camRes = camRes;
    }
}
