package com.android.banana.event;

/**
 * Created by zaozao on 2017/6/23.
 */

public class GroupChatMessageToTopEvent {
    public GroupChatMessageToTopEvent(String groupId, boolean toTop) {
        this.groupId = groupId;
        this.toTop = toTop;
    }

    public String groupId;

    public boolean toTop;//置顶为true，取消置顶为false

}
