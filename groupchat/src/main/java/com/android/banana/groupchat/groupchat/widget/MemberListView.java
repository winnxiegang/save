package com.android.banana.groupchat.groupchat.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.android.banana.R;
import com.android.banana.groupchat.view.baselist.BaseListView;
import com.android.banana.groupchat.view.baselist.base.BaseViewHolder;

/**
 * Created by kokuma on 2017/10/26.
 * 参照GroupChatRoomMemberAdapter
 */

public class MemberListView extends BaseListView {

    public MemberListView(Context context) {
        super(context);
        initUI(R.layout.item_member_select);
    }

    public MemberListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(R.layout.item_member_select);
    }

    public MemberListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(R.layout.item_member_select);
    }

    @Override
    protected void setConvert(BaseViewHolder viewHolder, Object bean) {
        //
    }
}
