package com.android.banana.groupchat.ilistener;


import com.android.banana.commlib.emoji.EmojBean;

import java.io.Serializable;

/**
 * Created by zhouyi on 2016/1/12 14:10.
 */
public interface EmojClickListener extends Serializable {

    public void onEmojiClick(EmojBean bean);
}
