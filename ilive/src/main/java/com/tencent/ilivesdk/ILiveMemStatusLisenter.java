package com.tencent.ilivesdk;


/**
 * 房间内成员状态回调接口
 */
public interface ILiveMemStatusLisenter {

    /**
     * 兼容之前老接口
     * @param eventid  用户事件
     * @param updateList id列表
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_IN
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_OUT
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_HAS_CAMERA_VIDEO
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_NO_CAMERA_VIDEO
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_HAS_AUDIO
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_NO_AUDIO
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_HAS_SCREEN_VIDEO
     * @see ILiveConstants#TYPE_MEMBER_CHANGE_NO_SCREEN_VIDEO
     * @return  返回true 表示 自己处理
     */
    boolean onEndpointsUpdateInfo(final int eventid, final String[] updateList);

}


