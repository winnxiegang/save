package com.tencent.ilivesdk.core;

import com.tencent.av.TIMAvManager;

/**
 * 录制参数
 */
public class ILiveRecordOption {
    private TIMAvManager.RecordParam mParam;

    public ILiveRecordOption(){
        mParam = TIMAvManager.getInstance().new RecordParam();
        mParam.setFilename("noname");
    }

    /**
     * 设置录制后的文件名
     */
    public ILiveRecordOption fileName(String fileName){
        mParam.setFilename(fileName);
        return this;
    }

    /**
     * 添加视频标签
     */
    @Deprecated
    public ILiveRecordOption addTag(String tag){
        mParam.addTag(tag);
        return this;
    }

    /**
     * 设置视频分类ID
     */
    @Deprecated
    public ILiveRecordOption classId(int classId){
        mParam.setClassId(classId);
        return this;
    }

    /**
     * 设置是否转码
     */
    @Deprecated
    public ILiveRecordOption transCode(boolean enable){
        mParam.setTransCode(enable);
        return this;
    }

    /**
     * 设置不否截图
     */
    @Deprecated
    public ILiveRecordOption screenShot(boolean enable){
        mParam.setSreenShot(enable);
        return this;
    }

    /**
     * 设置是否打水印
     */
    @Deprecated
    public ILiveRecordOption waterMark(boolean enable){
        mParam.setWaterMark(enable);
        return this;
    }

    /**
     * 设置录制类型
     */
    public ILiveRecordOption recordType(TIMAvManager.RecordType Type){
        mParam.setRecordType(Type);
        return this;
    }
    public TIMAvManager.RecordParam getParam() {
        return mParam;
    }
}
