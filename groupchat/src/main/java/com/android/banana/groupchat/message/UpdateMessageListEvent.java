package com.android.banana.groupchat.message;

/**
 * Created by zaozao on 2017/7/6.
 */

public class UpdateMessageListEvent {
    private String groupId;
    private boolean update;

    public UpdateMessageListEvent(String chatRoomId) {
        this.groupId = chatRoomId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
