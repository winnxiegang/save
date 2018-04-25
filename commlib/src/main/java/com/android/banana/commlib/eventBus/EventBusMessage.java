package com.android.banana.commlib.eventBus;

/**
 * Created by zhouyi on 2017/4/18.
 */

public class EventBusMessage {

    public static final String RE_LOGIN_SUCCESS = "RE_LOGIN_SUCCESS";

    public static final String USER_ALREADY_LOGIN_OTHER = "user_already_login_other";

    public static final String USER_KICK_OUT_FROM_CHANNEL = "user_kick_out_from_channel";

    public static String CHANNEL_USER_ENTER_OTHER_CHANNEL = "channel_user_enter_other_channel";

    public static String ROLE_SWITCH_SUCCESS = "role_switch_success";

    public static String WEIXIN_RECHARGE_SUCCESS = "weixin_recharge_success";

    public static String WEIXIN_RECHARGE_FAIL = "weixin_recharge_fail";

    public static String LIVE_ROOM_REFRESH = "live_room_refresh";

    public static String INIT_PASSWORD_OK = "init_password_ok";

    public static String GO_MAIN_TAB;


    public String getMessage() {
        return message;
    }

    private String message;

    private int value;

    public int getValue() {
        return value;
    }

    public EventBusMessage(String message) {
        this.message = message;
    }

    public EventBusMessage(String message, int value) {
        this.message = message;
        this.value = value;
    }

    public boolean isReLoginSuccess() {
        return RE_LOGIN_SUCCESS.equals(message);
    }

    public boolean isRoleSwitchSuccess() {
        return ROLE_SWITCH_SUCCESS.equals(message);
    }

    public boolean isWeiXinPayResult() {
        return WEIXIN_RECHARGE_SUCCESS.equals(message) ||
                WEIXIN_RECHARGE_FAIL.equals(message);
    }

    public boolean isKickOut() {
        return USER_ALREADY_LOGIN_OTHER.equals(message) ||
                USER_KICK_OUT_FROM_CHANNEL.equals(message) ||
                CHANNEL_USER_ENTER_OTHER_CHANNEL.equals(message);
    }

    public boolean isRefreshLiveRoom() {
        return LIVE_ROOM_REFRESH.equals(message);
    }

    public boolean isInitPasswordOk() {
        return INIT_PASSWORD_OK.equals(message);
    }
}
