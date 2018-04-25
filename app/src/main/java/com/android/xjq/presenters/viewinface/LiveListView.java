package com.android.xjq.presenters.viewinface;


import com.android.xjq.model.live.RoomInfoJson;
import com.android.xjq.presenters.UserServerHelper;

import java.util.ArrayList;


/**
 *  列表页面回调
 */
public interface LiveListView {


    void showRoomList(UserServerHelper.RequestBackInfo result, ArrayList<RoomInfoJson> roomlist);
}
