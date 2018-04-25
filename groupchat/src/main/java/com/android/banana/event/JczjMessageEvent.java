package com.android.banana.event;

/**
 * Created by zhouyi on 2015/12/14 15:11.
 */
public class JczjMessageEvent {

    public static final String SHOW_SLIDE_PANE = "show_slide_pane";

    public static final String EXIT="exit";

    public static final String PUSH_CONNECT_SUCCESS = "push_connect_succsss";

    public static final String REFRESH_HOME_INDEX = "refresh_home_index";

    public static final String JOIN_GROUP_CHAT_ROOM = "join_group_chat_room";

    public static final String EXIT_GROUP_CHAT_ROOM = "exit_group_chat_room";

    public static final String HOME_CLICK_LOOK_MESSAGE = "home_click_look_message";

    public String message;

    public JczjMessageEvent(String message){

        this.message = message;
    }

    public boolean isShowSlidePane(){
        if(SHOW_SLIDE_PANE.equals(message)){
            return true;
        }
        return false;
    }
    public boolean isClickLookMessage(){
        if(HOME_CLICK_LOOK_MESSAGE.equals(message)){
            return true;
        }
        return false;
    }

    public boolean isExit(){
        if(EXIT.equals(message)){
            return true;
        }
        return false;
    }
    public boolean isJoinRoom(){
        if(JOIN_GROUP_CHAT_ROOM.equals(message)){
            return true;
        }
        return false;
    }

    public boolean isExitRoom(){
        if(EXIT_GROUP_CHAT_ROOM.equals(message)){
            return true;
        }
        return false;
    }
    public boolean isRefreshHomeIndex(){
        if(REFRESH_HOME_INDEX.equals(message)){
            return true;
        }
        return false;
    }


}
