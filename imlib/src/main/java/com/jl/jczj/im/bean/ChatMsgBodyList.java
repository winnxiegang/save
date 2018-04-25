package com.jl.jczj.im.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qiaomu on 2017/6/17.
 */

public class ChatMsgBodyList implements Serializable {
    public long lastReadMessageSequence;
    public ArrayList<ChatMsgBody> messages;
}

