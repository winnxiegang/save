package com.tencent.ilivesdk.core.impl;

import com.tencent.av.TIMAvManager;

import java.util.List;

/**
 *
 */

class StreamerRecorderContext {

    int busiType;
    int authType;
    int authKey;	//relationId
    long uin;
    int sdkAppId;
    int operation;
    String sig;
    int subcmd;
    int roomId;
    TIMAvManager.StreamParam streamParam;
    TIMAvManager.RecordParam recordParam;
    List<Long> chnlIDs;
}


