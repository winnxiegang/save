package com.tencent.livesdk;

/**
 * 直播通话配置
 */
public class ILVLiveConstants {
    /** 无效消息 */
    public static final int ILVLIVE_CMD_NONE            = 0x700;         //1792
    /** 用户加入直播, Group消息 */
    public static final int ILVLIVE_CMD_ENTER           = 0x701;        // 1793
    /** 用户退出直播, Group消息 */
    public static final int ILVLIVE_CMD_LEAVE           = 0x702;        // 1794
    /** 邀请上麦，C2C消息 */
    public static final int ILVLIVE_CMD_INVITE          = 0x703;        // 1795
    /** 取消邀请上麦，C2C消息 */
    public static final int ILVLIVE_CMD_INVITE_CANCEL   = 0x704;        // 1796
    /** 关闭上麦，C2C消息 */
    public static final int ILVLIVE_CMD_INVITE_CLOSE    = 0x705;        // 1797
    /** 同意上麦，C2C消息 */
    public static final int ILVLIVE_CMD_INTERACT_AGREE  = 0x706;        // 1798
    /** 拒绝上麦，C2C消息 */
    public static final int ILVLIVE_CMD_INTERACT_REJECT = 0x707;        // 1799
    /** 请求跨房连麦，C2C消息 */
    public static final int ILVLIVE_CMD_LINKROOM_REQ    = 0x708;        // 1800
    /** 同意跨房连麦，C2C消息 */
    public static final int ILVLIVE_CMD_LINKROOM_ACCEPT = 0x709;        // 1801
    /** 拒绝跨房连麦，C2C消息 */
    public static final int ILVLIVE_CMD_LINKROOM_REFUSE = 0x70A;        // 1802
    /** 跨房连麦者达到上限, C2C消息 */
    public static final int ILVLIVE_CMD_LINKROOM_LIMIT  = 0x70B;        // 1803
    /** 跨房连麦成功 */
    public static final int ILVLIVE_CMD_LINKROOM_SUCC   = 0x70C;        // 1804
    /** 取消跨房连麦 */
    public static final int ILVLIVE_CMD_UNLINKROOM      = 0x70D;        // 1805

    /**
     * 用户自定义消息段
     */
    public static final int ILVLIVE_CMD_CUSTOM_LOW_LIMIT = 0x800;          //自定义消息段下限
    public static final int ILVLIVE_CMD_CUSTOM_UP_LIMIT = 0x900;          //自定义消息段上线

    /** 信令专用标识 */
    //public static final String TCEXT_MAGIC          = "LiveNotification";

    /**
     * 命令字
     */
    public static final String CMD_KEY = "userAction";
    /**
     * 命令参数
     */
    public static final String CMD_PARAM = "actionParam";




}
