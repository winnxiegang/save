package com.tencent.ilivesdk;

/**
 * 常量定义
 */
public class ILiveConstants {
    /** 已经是群组成员(IM错误码) */
    public static final int IS_ALREADY_MEMBER = 10013;
    /** 最大视频数量 */
    public static final int MAX_AV_VIDEO_NUM = 4;

    /** 无效(未开启)摄像头 */
    public static final int NONE_CAMERA = -1;
    /** 前置摄像头 */
    public static final int FRONT_CAMERA = 0;
    /** 后置摄像头 */
    public static final int BACK_CAMERA = 1;


    /** 支持后台模式 */
    public static final int VIDEOMODE_BSUPPORT = 0;
    /** 普通模式 */
    public static final int VIDEOMODE_NORMAL = 1;
    /** 后台静默模式(后台不允许上下行) */
    public static final int VIDEOMODE_BMUTE = 2;

    /**
     * AVEndPoint 命令字
     */
    /** 进入房间事件 */
    public static final int TYPE_MEMBER_CHANGE_IN = 1;
    /** 退出房间事件 */
    public static final int TYPE_MEMBER_CHANGE_OUT = 2;
    /** 有发摄像头视频事件 */
    public static final int TYPE_MEMBER_CHANGE_HAS_CAMERA_VIDEO = 3;
    /** 无发摄像头视频事件 */
    public static final int TYPE_MEMBER_CHANGE_NO_CAMERA_VIDEO = 4;
    /** 有发语音事件 */
    public static final int TYPE_MEMBER_CHANGE_HAS_AUDIO = 5;
    /** 无发语音事件 */
    public static final int TYPE_MEMBER_CHANGE_NO_AUDIO = 6;
    /** 有发屏幕视频事件 */
    public static final int TYPE_MEMBER_CHANGE_HAS_SCREEN_VIDEO = 7;
    /** 无发屏幕视频事件 */
    public static final int TYPE_MEMBER_CHANGE_NO_SCREEN_VIDEO = 8;

    /**
     * 模块名称<br>
     */
    /** iLiveSDK模块 */
    public static final String Module_ILIVESDK      = "ILiveSDK";
    /** AVSDK模块 */
    public static final String Module_AVSDK         = "AVSDK";
    /** IMSDK模块 */
    public static final String Module_IMSDK         = "IMSDK";
    /** TLS模块 */
    public static final String Module_TLSSDK        = "TLSSDK";

    /**
     * 异常id<br>
     */
    /** 要创建的IM房间已存在 */
    public static final int EXCEPTION_IMROOM_EXIST          = 1;
    /** 已是房间成员 */
    public static final int EXCEPTION_ALREADY_MEMBER        = 2;
    /** 打开摄像头失败 */
    public static final int EXCEPTION_ENABLE_CAMERA_FAILED  = 3;
    /** 打开Mic失败 */
    public static final int EXCEPTION_ENABLE_MIC_FAILED     = 4;
    /** 没有配置AVRootView */
    public static final int EXCEPTION_NO_ROOT_VIEW          = 5;
    /** 请求用户画面失败 */
    public static final int EXCEPTION_REQUEST_VIDEO_FAILED  = 6;
    /** 渲染用户画面失败 */
    public static final int EXCEPTION_RENDER_USER_FAILED    = 7;
    /** 发送信令失败 */
    public static final int EXCEPTION_MESSAGE_EXCEPTION     = 8;


    /** 日志关键字 */
    public static final String LOG_KEY_PR = "Key_Procedure";

    /**
     * 错误码<br>
     */
    /** 无效的整型返回值(通用) */
    public static final int INVALID_INTETER_VALUE   = -1;
    /** 成功 */
    public static final int NO_ERR                  = 0;
    /** IM模块未就绪或未加载 */
    public static final int ERR_IM_NOT_READY        = 8001;
    /** AV模块未就绪或未加载 */
    public static final int ERR_AV_NOT_READY        = 8002;
    /** 无有效的房间 */
    public static final int ERR_NO_ROOM             = 8003;
    /** 目标已存在 */
    public static final int ERR_ALREADY_EXIST       = 8004;
    /** 空指针错误 */
    public static final int ERR_NULL_POINTER        = 8005;
    /** 进入AV房间失败 */
    public static final int ERR_ENTER_AV_ROOM_FAIL  = 8006;
    /** 用户取消 */
    public static final int ERR_USER_CANCEL         = 8007;
    /** 状态异常 */
    public static final int ERR_WRONG_STATE         = 8008;
    /** 未登录 */
    public static final int ERR_NOT_LOGIN           = 8009;
    /** 已在房间中 */
    public static final int ERR_ALREADY_IN_ROOM     = 8010;
    /** 内部忙(上一请求未完成) */
    public static final int ERR_BUSY_HERE           = 8011;
    /** 网络未识别或网络不可达 */
    public static final int ERR_NET_UNDEFINE        = 8012;
    /** iLiveSDK处理失败(通用) */
    public static final int ERR_SDK_FAILED          = 8020;
    /** 无效的参数 */
    public static final int ERR_INVALID_PARAM       = 8021;
    /** 无法找到目标 */
    public static final int ERR_NOT_FOUND           = 8022;
    /** 请求不支持 */
    public static final int ERR_NOT_SUPPORT         = 8023;
    /** 状态已到位(一般为重复调用引起) */
    public static final int ERR_ALREADY_STATE       = 8024;
    /** 被踢下线 */
    public static final int ERR_KICK_OUT            = 8050;
    /** 票据过期(需更新票据userSig) */
    public static final int ERR_EXPIRE              = 8051;


    /**
     * 数据上报事件<br>
     */
    /** 登录事件 */
    public static final int EVENT_ILIVE_LOGIN              =  10001;
    /** 初始化事件 */
    public static final int EVENT_ILIVE_INIT               =  10002;
    /** 创建房间事件 */
    public static final int EVENT_ILIVE_CREATEROOM         =  10003;
    /** 加入房间事件 */
    public static final int EVENT_ILIVE_JOINROOM           =  10004;
    /** 发送群文本消息事件 */
    public static final int EVENT_SEND_GROUP_TEXT_MSG      =  10005;
    /** 发送群自定义消息事件 */
    public static final int EVENT_SEND_GROUP_CUSTOM_MSG    =  10006;
    /** 发送C2C自定义消息事件 */
    public static final int EVENT_SEND_C2C_CUSTOM_MSG      =  10007;
    /** 发起呼叫事件(视频通话) */
    public static final int EVENT_MAKE_CALL                =  10008;
    /** 接听来电事件(视频通话) */
    public static final int EVENT_ACCEPT_CALL              =  10009;
    /** 拒接来电事件(视频通话) */
    public static final int EVENT_REJECT_CALL              =  10010;


    /** 发起多人会话  */
    public static final int EVENT_MAKE_MULTICALL           =  10013;
    /** 加入AV房房间  */
    public static final int EVENT_ENTER_AVROOM             =  10014;
    /** 创建直播聊天室  */
    public static final int EVENT_CREATE_AVCHATROOM        =  10015;
    /** 加入直播聊天室  */
    public static final int EVENT_JOIN_AVCHATROOM          =  10016;


}
