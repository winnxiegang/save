package com.android.xjq.bean;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by qiaomu on 2018/3/22.
 * <p>
 * 发话题 发说说的字数配置
 */

public class SubjectCreateConfig {
    @SerializedName("subejctContentLengthConfigMap")
    public HashMap<String, Config> subjectConfigMap;

    public static class Config {

        /**
         * maxTitleLength : 140
         * minTitleLength : 0
         * minContentLength : 5
         * maxContentLength : 500
         */

        public int maxTitleLength;
        public int minTitleLength;
        public int minContentLength;
        public int maxContentLength;
    }
}
