package com.tencent.ilivesdk.core;

/**
 *
 */

public class ILivePushOption {

    Encode encode =Encode.RTMP;
    String channelName;
    String channelDesc;
    RecordFileType recordFileType = RecordFileType.NONE;


    /**
     * 设置推流编码类型(默认为RTMP)
     */
    public ILivePushOption encode(Encode encode){
        this.encode = encode;
        return this;
    }

    /**
     * 设置频道名称(可选)
     */
    public ILivePushOption channelName(String name){
        channelName = name;
        return this;
    }

    /**
     * 设置频道描述(可选)
     */
    public ILivePushOption channelDesc(String desc){
        channelDesc = desc;
        return this;
    }

    /**
     * 设置录制文件类型(可选)
     */
    public ILivePushOption setRecordFileType(RecordFileType fileType){
        recordFileType = fileType;
        return this;
    }

    public Encode getEncode() {
        return encode;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelDesc() {
        return channelDesc;
    }

    public RecordFileType getRecordFileType() {
        return recordFileType;
    }

    public enum  Encode {
        /**
         * 请求HLS编码的视频流URL
         */
        HLS(0x01),

        /**
         * 请求FLV编码的视频流URL
         */
        FLV(0x02),


        /**
         * 请求原始编码的视频流URL
         */
        RAW(0x04),

        /**
         * 请求RTMP编码的视频流URL
         */
        RTMP(0x05),

        /**
         * 同时请求HLS和RTMP编码的视频流URL
         */
        HLS_AND_RTMP(0x6);



        private int encode;

        private Encode(int type) {
            encode = type;
        }

        public int getEncode() {
            return this.encode;
        }
    }

    /**
     * 自动录制文件类型
     */
    public enum RecordFileType
    {
        /**
         * 不录制
         */
        NONE(0x00),

        /**
         * 录制HLS
         */
        RECORD_HLS(0x01),
        /**
         * 录制FLV
         */
        RECORD_FLV(0x02),
        /**
         * 同时录制HLS和FLV
         */
        RECORD_HLS_FLV(0x03),
        /**
         * 录制MP4
         */
        RECORD_MP4(0x04),
        /**
         * 同时录制HLS和MP4
         */
        RECORD_HLS_MP4(0x05),
        /**
         * 同时录制FLV和MP4
         */
        RECORD_FLV_MP4(0x06),
        /**
         * 同时录制HLS，FLV和MP4
         */
        RECORD_HLS_FLV_MP4(0x07);//同时录制hls,flv和mp4


        private int type;
        RecordFileType(int type){
            this.type = type;
        }

        public int getType(){
            return this.type;
        }
    }
}
