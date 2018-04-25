package com.tencent.livesdk;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMMessagePriority;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveFunc;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 直播接口实现类
 */
public class LiveMgr extends ILVLiveManager implements TIMMessageListener {
    private static String TAG = "ILVBLive";
    protected ILVLiveConfig liveConfig = null;
    private boolean isBeVideoMember = false;

    @Override
    public int init(ILVLiveConfig config) {
        //使用SDK消息过滤机制 否则用他们自己的
        if (config.getRoomMessageListener() == null)
            config.messageListener(this);
        liveConfig = config;

        ILiveLog.v(TAG, ILiveConstants.LOG_KEY_PR+"|init entered version:"+getVersion());
        return ILiveRoomManager.getInstance().init(config);
    }

    @Override
    public int createRoom(int roomId, ILVLiveRoomOption hostOption, ILiveCallBack callBack) {
        return ILiveRoomManager.getInstance().createRoom(roomId, hostOption, callBack);
    }

    @Override
    public int joinRoom(int roomId, ILVLiveRoomOption memberOption, ILiveCallBack callback) {
        Log.v(TAG, "joinRoom->id: " + roomId);
        return ILiveRoomManager.getInstance().joinRoom(roomId, memberOption, callback);
    }

    @Override
    public int switchRoom(int roomId, ILVLiveRoomOption hostOption, ILiveCallBack callback) {
        Log.v(TAG, "switchRoom->id: " + roomId);
        return ILiveRoomManager.getInstance().switchRoom(roomId, hostOption, callback);
    }

    @Override
    public int quitRoom(ILiveCallBack callback) {
        Log.v(TAG, "quitRoom->enter ");
        return ILiveRoomManager.getInstance().quitRoom(callback);
    }

    @Override
    public int linkRoomRequest(String toId, ILiveCallBack callBack) {
        if (ILiveLoginManager.getInstance().getMyUserId().equals(toId)){
            return ILiveConstants.ERR_INVALID_PARAM;
        }
        ILVCustomCmd cmd = new ILVCustomCmd(ILVText.ILVTextType.eC2CMsg, toId,
                "", ILVLiveConstants.ILVLIVE_CMD_LINKROOM_REQ);
        return sendOnlineCustomCmd(cmd, callBack);
    }

    @Override
    public int acceptLinkRoom(String toId, ILiveCallBack callBack) {
        ILVCustomCmd cmd = new ILVCustomCmd(ILVText.ILVTextType.eC2CMsg, toId,
                ""+ILiveRoomManager.getInstance().getRoomId(), ILVLiveConstants.ILVLIVE_CMD_LINKROOM_ACCEPT);
        return sendOnlineCustomCmd(cmd, callBack);
    }

    @Override
    public int refuseLinkRoom(String toId, ILiveCallBack callBack) {
        ILVCustomCmd cmd = new ILVCustomCmd(ILVText.ILVTextType.eC2CMsg, toId,
                "", ILVLiveConstants.ILVLIVE_CMD_LINKROOM_REFUSE);
        return sendOnlineCustomCmd(cmd, callBack);
    }

    @Override
    public int linkRoom(int roomId, final String accountId, String sign, final ILiveCallBack callBack) {
        ILiveLog.d(TAG, "linkRoom->enter->id:"+roomId+", "+accountId);
        return ILiveRoomManager.getInstance().linkRoom(roomId, accountId, sign, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                mLinkedUserId.add(accountId);
                ILVCustomCmd cmd = new ILVCustomCmd(ILVText.ILVTextType.eC2CMsg, accountId,
                        "", ILVLiveConstants.ILVLIVE_CMD_LINKROOM_SUCC);
                sendOnlineCustomCmd(cmd, callBack);
                ILiveFunc.notifySuccess(callBack, 0);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ILiveLog.d(TAG, "linkRoom->failed:"+module+"|"+errCode+"|"+errMsg);
                ILiveFunc.notifyError(callBack, module, errCode, errMsg);
            }
        });
    }

    @Override
    public int unlinkRoom(ILiveCallBack callBack) {
        ILiveLog.d(TAG, "unlinkRoom->enter");
        return ILiveRoomManager.getInstance().unlinkRoom(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                for (String id : mLinkedUserId) {
                    ILiveLog.d(TAG, "unlinkRoom->nnotify: "+id);
                    ILVCustomCmd cmd = new ILVCustomCmd(ILVText.ILVTextType.eC2CMsg, id,
                            "", ILVLiveConstants.ILVLIVE_CMD_UNLINKROOM);
                    sendOnlineCustomCmd(cmd, null);
                }
                mLinkedUserId.clear();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ILiveLog.d(TAG, "unlinkRoom->failed:"+module+"|"+errCode+"|"+errMsg);
            }
        });
    }
    @Override
    public int setAvVideoView(AVRootView view) {
        return ILiveRoomManager.getInstance().initAvRootView(view);
    }

    @Override
    public void onPause() {
        ILiveRoomManager.getInstance().onPause();
    }

    @Override
    public void onResume() {
        ILiveRoomManager.getInstance().onResume();
    }


    @Override
    public void onDestory() {
        ILiveRoomManager.getInstance().onDestory();
    }


    @Override
    public int sendGroupTextMsg(String msg, ILiveCallBack callBack) {
        if (msg.length() == 0)
            return -1;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                return -1;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1;
        }
        TIMMessage Nmsg = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText(msg);
        if (Nmsg.addElement(elem) != 0) {
            return -1;
        }
        if (ILiveRoomManager.getInstance() != null) {
            ILiveRoomManager.getInstance().sendGroupMessage(Nmsg, callBack);
        }
        return 0;
    }


    @Override
    public void shutdown() {
        ILiveRoomManager.getInstance().shutdown();
    }

    @Override
    public void sendGroupCustomMessage(String context, TIMMessagePriority level, ILiveCallBack callback) {
        //发送自定义消息
        TIMMessage Gmsg = new TIMMessage();
        Gmsg.setPriority(level);
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(context.getBytes());
        elem.setDesc("");
        Gmsg.addElement(elem);

        if (ILiveRoomManager.getInstance() != null) {
            ILiveRoomManager.getInstance().sendGroupMessage(Gmsg, callback);
        }
    }

    @Override
    public void sendC2CCustomMessage(String context, final String sendId, TIMMessagePriority level, ILiveCallBack<TIMMessage> callBack) {
        //发送自定义消息
        TIMMessage msg = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        msg.setPriority(level);
        elem.setData(context.getBytes());
        elem.setDesc("");
        msg.addElement(elem);
        ILiveRoomManager.getInstance().sendC2CMessage(sendId, msg, callBack);
    }

    private TIMMessage buildMsgFromText(ILVText text) {
        if (text.getText().length() == 0)
            return null;
        try {
            byte[] byte_num = text.getText().getBytes("utf8");
            if (byte_num.length > 160) {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        TIMMessage Nmsg = new TIMMessage();
        TIMTextElem elem = new TIMTextElem(); //文本消息
        elem.setText(text.getText());
        Nmsg.setPriority(text.getPriority());
        if (Nmsg.addElement(elem) != 0) {
            return null;
        }
        return Nmsg;
    }

    @Override
    public int sendText(ILVText txt, ILiveCallBack callback) {
        TIMMessage msg = buildMsgFromText(txt);
        if (null == msg)
            return ILiveConstants.ERR_INVALID_PARAM;
        if (ILiveRoomManager.getInstance() != null) {
            ILiveLog.d(TAG, "sendText->"+txt.getType()+"|"+txt.getDestId()+"|"+txt.getText());
            if (ILVText.ILVTextType.eGroupMsg == txt.getType()) { //群消息
                return ILiveRoomManager.getInstance().sendGroupMessage(msg, callback);
            } else { //C2C消息
                return ILiveRoomManager.getInstance().sendC2CMessage(txt.getDestId(), msg, callback);
            }
        }

        return ILiveConstants.ERR_IM_NOT_READY;
    }

    @Override
    public int sendOnlineText(ILVText text, ILiveCallBack callBack) {
        TIMMessage msg = buildMsgFromText(text);
        if (null == msg)
            return ILiveConstants.ERR_INVALID_PARAM;
        if (ILiveRoomManager.getInstance() != null) {
            ILiveLog.d(TAG, "sendOnlineText->"+text.getType()+"|"+text.getDestId()+"|"+text.getText());
            if (ILVText.ILVTextType.eGroupMsg == text.getType()) { //群消息
                return ILiveRoomManager.getInstance().sendGroupOnlineMessage(msg, callBack);
            } else { //C2C消息
                return ILiveRoomManager.getInstance().sendC2COnlineMessage(text.getDestId(), msg, callBack);
            }
        }

        return ILiveConstants.ERR_IM_NOT_READY;
    }

    @Override
    public int sendCustomCmd(ILVCustomCmd cmd, ILiveCallBack callback) {
        if (cmd.getCmd() == -1) return -1;
        //发送自定义消息
        TIMMessage msg = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        String strCmd = jasonToString(cmd.getCmd(), cmd.getParam());
        //elem.setExt(ILVLiveConstants.TCEXT_MAGIC.getBytes());
        elem.setData(strCmd.getBytes());
        elem.setDesc("");
        msg.addElement(elem);
        ILiveLog.d(TAG, "sendCustomCmd->"+cmd.getType()+"|"+cmd.getDestId()+"|"+strCmd);
        if (ILVText.ILVTextType.eC2CMsg == cmd.getType()) {//C2C消息
            return ILiveRoomManager.getInstance().sendC2CMessage(cmd.getDestId(), msg, callback);
        } else {//群消息
            return ILiveRoomManager.getInstance().sendGroupMessage(msg, callback);
        }
    }

    @Override
    public int sendOnlineCustomCmd(ILVCustomCmd cmd, ILiveCallBack callBack) {
        if (cmd.getCmd() == -1) return -1;
        //发送自定义消息
        TIMMessage msg = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        //elem.setExt(ILVLiveConstants.TCEXT_MAGIC.getBytes());
        String strCmd = jasonToString(cmd.getCmd(), cmd.getParam());
        elem.setData(strCmd.getBytes());
        elem.setDesc("");
        msg.addElement(elem);
        ILiveLog.d(TAG, "sendOnlineCustomCmd->"+cmd.getType()+"|"+cmd.getDestId()+"|"+strCmd);
        if (ILVText.ILVTextType.eC2CMsg == cmd.getType()) {//C2C消息
            return ILiveRoomManager.getInstance().sendC2COnlineMessage(cmd.getDestId(), msg, callBack);
        } else {//群消息
            return ILiveRoomManager.getInstance().sendGroupOnlineMessage(msg, callBack);
        }
    }

    @Override
    public int sendOtherMessage(String destUser, TIMMessage message, ILiveCallBack callBack) {
        if (TextUtils.isEmpty(destUser)) {
            return ILiveRoomManager.getInstance().sendGroupMessage(message, callBack);
        } else {
            return ILiveRoomManager.getInstance().sendC2CMessage(destUser, message, callBack);
        }
    }

    @Override
    public int sendOnlineOtherMessage(String destUser, TIMMessage message, ILiveCallBack callBack) {
        if (TextUtils.isEmpty(destUser)) {
            return ILiveRoomManager.getInstance().sendGroupOnlineMessage(message, callBack);
        } else {
            return ILiveRoomManager.getInstance().sendC2COnlineMessage(destUser, message, callBack);
        }
    }

    @Override
    public void upToVideoMember(String role, final boolean isOpenCam, final boolean isOpenMic, final ILiveCallBack<ILVChangeRoleRes> callBack) {
        ILiveLog.d(TAG, "ILVB-LiveBusiness|upToVideoMember " + role);
        ILiveRoomManager.getInstance().changeRole(role, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                isBeVideoMember = true;
                ILiveLog.d(TAG, "ILVB-LiveBusiness|changeAuthAndRole suc ");
                int camRes = 0;
                int micRes = 0;
                if (isOpenCam) {
                    camRes = ILiveRoomManager.getInstance().enableCamera(ILiveRoomManager.getInstance().getCurCameraId(), true);
                }
                if (isOpenMic) {
                    micRes = ILiveRoomManager.getInstance().enableMic(true);
                }
                ILVChangeRoleRes res = new ILVChangeRoleRes(micRes, camRes);
                callBack.onSuccess(res);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ILiveLog.e(TAG, "ILVB-LiveBusiness|changeAuthAndRole fail");
                callBack.onError(module, errCode, errMsg);

            }
        });

    }

    @Override
    public void downToNorMember(String role, final ILiveCallBack<ILVChangeRoleRes> callBack) {
        if (isBeVideoMember == true) {
            ILiveLog.d(TAG, "ILVB-LiveBusiness|downToNorMember ");
            ILiveRoomManager.getInstance().changeRole(role, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    ILiveLog.d(TAG, "ILVB-LiveBusiness|changeAuthAndRole suc");
                    isBeVideoMember = false;
                    int camRes = ILiveRoomManager.getInstance().enableCamera(ILiveRoomManager.getInstance().getCurCameraId(), false);
                    int micRes = ILiveRoomManager.getInstance().enableMic(false);
                    ILVChangeRoleRes res = new ILVChangeRoleRes(micRes, camRes);
                    callBack.onSuccess(res);
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    callBack.onError(module, errCode, errMsg);
                    ILiveLog.e(TAG, "ILVB-LiveBusiness|changeAuthAndRole fail");

                }
            });
        }

    }


    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        Log.i(TAG, "onNewMessages->size: " + list.size());
        for (TIMMessage msg : list) {
            if (!parseIMMessage(msg)) { // 上抛其他消息
                if (null != liveConfig && null != liveConfig.getLiveMsgListener()) {
                    liveConfig.getLiveMsgListener().onNewOtherMsg(msg);
                }
            }
        }

        return false;
    }


    private boolean parseIMMessage(TIMMessage currMsg) {
        boolean bInnerMsg = false;
        for (int j = 0; j < currMsg.getElementCount(); j++) {
            if (currMsg.getElement(j) == null)
                continue;
            TIMElem elem = currMsg.getElement(j);
            TIMElemType type = elem.getType();
            String sendId = currMsg.getSender();


//                定制消息
            if (type == TIMElemType.Custom && liveConfig.isCustomProtocol() == false) {
                TIMCustomElem customElem = (TIMCustomElem) elem;
                if (liveConfig.getLiveMsgListener() != null) {
                    bInnerMsg = handleCustomMsg(currMsg, customElem, sendId);
                }

                continue;
            }

            //文本消息
            if (type == TIMElemType.Text) {
                TIMTextElem textElem = (TIMTextElem) elem;
                if (liveConfig.getLiveMsgListener() != null) {
                    ILVText ilvText;
                    bInnerMsg = true;
                    if (TIMConversationType.C2C == currMsg.getConversation().getType()) {
                        ilvText = new ILVText(ILVText.ILVTextType.eC2CMsg, currMsg.getConversation().getPeer(), textElem.getText());
                    } else if (TIMConversationType.Group == currMsg.getConversation().getType()) {
                        ilvText = new ILVText(ILVText.ILVTextType.eGroupMsg, currMsg.getConversation().getPeer(), textElem.getText());
                    } else {
                        bInnerMsg = false;  // 归为other消息
                        continue;
                    }

                    ilvText.setPriority(currMsg.getPriority());
                    liveConfig.getLiveMsgListener().onNewTextMsg(ilvText, sendId, currMsg.getSenderProfile());
                }
            }
        }

        return bInnerMsg;
    }


    private String jasonToString(int cmd, String param) {
        JSONObject inviteCmd = new JSONObject();
        try {
            inviteCmd.put(ILVLiveConstants.CMD_KEY, cmd);
            inviteCmd.put(ILVLiveConstants.CMD_PARAM, param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inviteCmd.toString();
    }

    /**
     * 处理内部信令
     * @param cmd
     * @param id
     * @param userProfile
     */
    private boolean processInnerMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile){
        switch (cmd.getCmd()){
        case ILVLiveConstants.ILVLIVE_CMD_UNLINKROOM:
            ILiveLog.d(TAG, "processInnerMsg->unlink from "+id);
            mLinkedUserId.remove(id);
            break;
        case ILVLiveConstants.ILVLIVE_CMD_LINKROOM_SUCC:
            ILiveLog.d(TAG, "processInnerMsg->link from "+id);
            mLinkedUserId.add(id);
            break;
        case ILVLiveConstants.ILVLIVE_CMD_LINKROOM_REQ:
            ILiveLog.d(TAG, "processInnerMsg->link req from "+id+", " + mLinkedUserId.size());
            if (mLinkedUserId.size() >= 3){     // 达到跨房上限直接内部处理
                ILVCustomCmd limitCmd = new ILVCustomCmd(ILVText.ILVTextType.eC2CMsg, id,
                        "", ILVLiveConstants.ILVLIVE_CMD_LINKROOM_LIMIT);
                sendOnlineCustomCmd(limitCmd, null);
                return true;
            }
            break;
        }
        return false;
    }


    /**
     * 处理自定义消息  更多自定义消息可以查看类ILVLiveConstants,也支持用户自定义自己的消息
     *
     * @param elem 自定义消息类型
     * @param id   发送者
     */
    private boolean handleCustomMsg(TIMMessage msg, TIMCustomElem elem, String id) { //解析Jason格式Custom消息
        try {
            ILiveLog.i(TAG, "handleCustomMsg : " + id);
/*            String strExt = new String(elem.getExt(), "UTF-8");
            if (!strExt.equals(ILVLiveConstants.TCEXT_MAGIC)) {  // 检测是否内部信令
                return false;
            }*/
            String customText = new String(elem.getData(), "UTF-8");
            JSONTokener jsonParser = new JSONTokener(customText);
            JSONObject json = (JSONObject) jsonParser.nextValue();
            int action = json.getInt(ILVLiveConstants.CMD_KEY);
            String param = json.getString(ILVLiveConstants.CMD_PARAM);
            ILVCustomCmd cmd = new ILVCustomCmd();
            if (TIMConversationType.Group == msg.getConversation().getType()) {
                cmd.setType(ILVText.ILVTextType.eGroupMsg);
            } else {
                cmd.setType(ILVText.ILVTextType.eC2CMsg);
            }
            cmd.setDestId(msg.getConversation().getPeer());
            cmd.setCmd(action);
            cmd.setParam(param);
            if (!processInnerMsg(cmd, id, msg.getSenderProfile())) { // 内部处理决定是否上抛
                liveConfig.getLiveMsgListener().onNewCustomMsg(cmd, id, msg.getSenderProfile());
            }
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getVersion() {
        return version;
    }


}
