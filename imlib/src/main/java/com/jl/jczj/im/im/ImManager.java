package com.jl.jczj.im.im;

import com.jl.jczj.im.callback.IMCallback;
import com.jl.jczj.im.service.ImChannelEnum;

/**
 * Created by zhouyi on 2017/5/27.
 */

public class ImManager {
    private static ImManager instance;

    //public static final String SERVICE_NAME = "/service.json";
    public static String URL;


    static {
        instance = new ImManager();
        onLine();
    }

    private ImManager() {
    }

    public static ImManager getInstance() {
        return instance;
    }

    public void init(IMCallback imCallback) {
        ImGroupManager.getInstance().init(ImChannelEnum.TIM, imCallback);
    }

    public static void onLine() {
        URL = "http://immapi.xjball.com/service.json";
    }

    public static void offLine() {
        URL = "http://immapi.xjq.net/service.json";
    }

    /**
     * 检查使用那个通道获取Im消息
     */
    private void checkImChannel() {


    }

    public void registerMessageListener() {

    }

    /**
     * 登出
     */
    public void logout(IMCallback callback) {

    }

    /**
     * 登录
     */
    public void login(IMCallback callback) {

    }
}
