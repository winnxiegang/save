package com.tencent.ilivesdk.tools.speedtest;

import com.tencent.imsdk.BaseConstants;

/**
 *  测速使用的常量
 */

public class SpeedTestConstants extends BaseConstants {

    static final int BUSS_TYPE = 7;
    static final int AUTH_TYPE = 6;

    public static final int CODE_RUNNING = -1;
    public static final String MSG_RUNNING = "speed test is running,you must stop current running to start another one";

    public static final int CODE_TEST_REQ_RET_ERROR = -2;
    public static final int CODE_TEST_IP_UNKNOWN = -3;
    public static final int CODE_TEST_REPORT_ERROR = -4;
    public static final String MSG_TEST_IP_UNKNOWN = "exception indicate that the IP address of a host could not be determined";
    public static final String MSG_TEST_REPORT_ERROR = "speed test id is not the original one,something must be wrong";
    //接口机发包测试发生错误
    public static final int CODE_TESTING_ERROR = -5;



}
