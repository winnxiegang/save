package com.tencent.ilivesdk.core.impl;

import com.tencent.av.NetworkUtil;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILivePushOption;
import com.tencent.ilivesdk.data.ILivePushRes;
import com.tencent.ilivesdk.data.ILivePushUrl;
import com.tencent.ilivesdk.engines.communication.CommunicationEngine;
import com.tencent.ilivesdk.protos.gv_comm_operate;
import com.tencent.imsdk.BaseConstants;
import com.tencent.imsdk.IMMsfCoreProxy;

import java.nio.ByteBuffer;
import java.util.List;

/**
 *
 */

public class PushUseCase {

    private CommunicationEngine communicationEngine;
    private static int bussType = 7;
    private static int authType = 6;


    public PushUseCase(CommunicationEngine communicationEngine) {
        this.communicationEngine = communicationEngine;
    }


    public void start(String id, int roomId, ILivePushOption option, final ILiveCallBack<ILivePushRes> callBack){

        if (!communicationEngine.isLogin(id)){
            callBack.onError(ILiveConstants.Module_ILIVESDK, ILiveConstants.ERR_NOT_LOGIN, "current user not login. id: " + id);
            return;
        }
        gv_comm_operate.GVCommOprHead head = new gv_comm_operate.GVCommOprHead();
        head.uint32_buss_type.set(bussType);        //opensdk
        head.uint32_auth_type.set(authType);        //opensdk
        head.uint32_auth_key.set(roomId);
        head.uint64_uin.set(communicationEngine.getLoginUin(id));
        head.uint32_sdk_appid.set(ILiveSDK.getInstance().getAppId());

        gv_comm_operate.ReqBody reqbody = new gv_comm_operate.ReqBody();

        reqbody.req_0x6.setHasFlag(true);
        reqbody.req_0x6.uint32_oper.set(1);

        reqbody.req_0x6.uint32_live_code.set(option.getEncode().getEncode());

        if (option.getChannelName() != null) {
            reqbody.req_0x6.str_channel_name.set(option.getChannelName());
        }

        if (option.getChannelDesc() != null) {
            reqbody.req_0x6.str_channel_describe.set(option.getChannelDesc());
        }

        if(option.getRecordFileType() != ILivePushOption.RecordFileType.NONE){
            reqbody.req_0x6.uint32_record_type.set(option.getRecordFileType().getType());
        }

        byte[] busibuf = NetworkUtil.formReq(id, 0x140, roomId, "",
                head.toByteArray(), reqbody.toByteArray());

        //do the request
        communicationEngine.videoProtoRequest(busibuf, new ILiveCallBack<byte[]>() {
            @Override
            public void onSuccess(byte[] data) {
                gv_comm_operate.RspBody rsp = new gv_comm_operate.RspBody();

                byte[] buff = NetworkUtil.parseRsp(data);
                if(buff == null){
                    callBack.onError(ILiveConstants.Module_ILIVESDK, BaseConstants.ERR_PARSE_RESPONSE_FAILED, "parse streamer rsp failed");
                    return;
                }
                try {
                    rsp.mergeFrom(buff);
                    if (rsp.rsp_0x6.uint32_result.get() != 0) {
                        callBack.onError(ILiveConstants.Module_ILIVESDK, rsp.rsp_0x6.uint32_result.get(), rsp.rsp_0x6.str_errorinfo.get());
                        return;
                    }
                    ILivePushRes resp = new ILivePushRes(rsp.rsp_0x6.uint64_channel_id.get(), rsp.rsp_0x6.uint32_tape_task_id.get());

                    for (gv_comm_operate.LiveUrl url : rsp.rsp_0x6.msg_live_url.get()) {
                        ILivePushUrl l = new ILivePushUrl(url.uint32_type.get(), url.string_play_url.get(), url.rate_type.get());
                        resp.addUrl(l);
                    }
                    callBack.onSuccess(resp);
                } catch (Throwable e) {
                    callBack.onError(ILiveConstants.Module_ILIVESDK, BaseConstants.ERR_PARSE_RESPONSE_FAILED, "parse streamer rsp failed");
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                callBack.onError(module, errCode, errMsg);
            }
        });
    }

    public void stop(String id, int roomId, List<Long> channelIDs, final ILiveCallBack callBack) {
        if (!communicationEngine.isLogin(id)){
            callBack.onError(ILiveConstants.Module_ILIVESDK, ILiveConstants.ERR_NOT_LOGIN, "current user not login. id: " + id);
            return;
        }
        gv_comm_operate.GVCommOprHead head = new gv_comm_operate.GVCommOprHead();
        head.uint32_buss_type.set(bussType);        //opensdk
        head.uint32_auth_type.set(authType);        //opensdk
        head.uint32_auth_key.set(roomId);
        head.uint64_uin.set(communicationEngine.getLoginUin(id));
        head.uint32_sdk_appid.set(ILiveSDK.getInstance().getAppId());

        gv_comm_operate.ReqBody reqbody = new gv_comm_operate.ReqBody();

        reqbody.req_0x6.setHasFlag(true);
        reqbody.req_0x6.uint32_oper.set(2);
        reqbody.req_0x6.uint64_channel_id.set(channelIDs);

        byte[] busibuf = NetworkUtil.formReq(id, 0x140, roomId, "",
                head.toByteArray(), reqbody.toByteArray());

        communicationEngine.videoProtoRequest(busibuf, new ILiveCallBack<byte[]>() {
            @Override
            public void onSuccess(byte[] data) {
                callBack.onSuccess(0);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                callBack.onError(module, errCode, errMsg);
            }
        });
    }




    /**
     * cld_pkg_head + LongConnHead + 0x28 + HeadLen + BodyLen + head + reqbody + 0x29
     */
    private byte[] formReq(String strIdentifier, int subcmd, int roomNUm, String sig, byte[] head, byte[] reqbody) {


        //cld_pkg_head
        short wVersion = 0;
        short wCmd = 0;
        short wSeq = (short) IMMsfCoreProxy.get().random.nextInt();
        int dwUin = 0;
        int cld_head_size = 2 + 2 + 2 + 4;
        //LongConnHead
        short wSubCmd = (short) subcmd;
        long llAcount = communicationEngine.getLoginUin(strIdentifier);
        byte cKeyLen = 0;
        byte[] sKey = "".getBytes();
        if (sig != null) {
            sKey = sig.getBytes();
        }
        cKeyLen = (byte) sKey.length;
        int dwRoomNum = roomNUm;
        int conn_head_size = 2 + 8 + 1 + sKey.length + 4;

        int head_size = head.length;
        int body_size = reqbody.length;
        int pkg_size = cld_head_size + conn_head_size + 1 + 4 + 4 + head_size + body_size + 1;
        ByteBuffer buf = ByteBuffer.allocate(pkg_size);

        //cld_pkg_head
        buf.putShort(wVersion).putShort(wCmd).putShort(wSeq).putInt(dwUin);
        //LongConnHead
        buf.putShort(wSubCmd).putLong(llAcount).put(cKeyLen).put(sKey).putInt(dwRoomNum);
        //stx + headlen + bodylen + head + body + etx
        buf.put((byte)0x28).putInt(head_size).putInt(body_size).put(head).put(reqbody).put((byte) 0x29);

        buf.flip();
        return buf.array();
    }
}
