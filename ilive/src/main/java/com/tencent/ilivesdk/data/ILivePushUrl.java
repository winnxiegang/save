package com.tencent.ilivesdk.data;

/**
 *
 */

public class ILivePushUrl {

    final int encodeType;
    final String url;
    final int rate;

    public ILivePushUrl(int encodeType, String url, int rate) {
        this.encodeType = encodeType;
        this.url = url;
        this.rate = rate;
    }


    /**
     * 获取视频流编码类型
     *
     * @return 视频流编码类型
     */
    public int getEncode() {
        return this.encodeType;
    }

    /**
     * 获取视频流播放URL
     *
     * @return 视频流播放URL
     */
    public String getUrl() {
        return url == null ? "" : url;
    }

    /**
     * 获取视频流码率
     * @return 视频流码率
     */
    public RateType getRateType() {
        for(RateType r : RateType.values()){
            if(r.getValue() == rate){
                return r;
            }
        }

        return RateType.RATE_TYPE_ORIGINAL;
    }

    /**
     * 推流码率类型
     */
    public enum RateType {
        /**
         * 原始码率
         */
        RATE_TYPE_ORIGINAL(0),

        /**
         * 标清码率550K
         */
        RATE_TYPE_550(10),

        /**
         * 高清码率900K
         */
        RATE_TYPE_900(20);

        private int value = 0;

        RateType(int v){
            value = v;
        }

        public int getValue() {
            return value;
        }
    }
}
