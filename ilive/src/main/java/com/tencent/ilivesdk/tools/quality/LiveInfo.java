package com.tencent.ilivesdk.tools.quality;

/**
 * 直播信息
 */

public class LiveInfo {

    private int width;
    private int height;

    public LiveInfo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
