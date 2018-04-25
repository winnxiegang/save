package com.android.banana.event;

/**
 * Created by zaozao on 2017/10/24.
 */

public class GroupChatJoinExitEvent {
    public GroupChatJoinExitEvent(String groupId, boolean exit) {
        this.groupId = groupId;
        this.exit = exit;
    }

    public String groupId;

    public boolean exit;//置顶为true，取消置顶为false

}

