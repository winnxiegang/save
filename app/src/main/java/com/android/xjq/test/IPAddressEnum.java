package com.android.xjq.test;

/**
 * Created by zhouyi on 2016/11/25 11:34.
 */
//每个组员的ip地址
public enum IPAddressEnum {

    ZHOU_YI("周神", "http://10.0.4.65:8080", ""),

    LING_JIU("零九", "http://10.0.4.46:8080", ""),

    ZAO_ZAO("皂皂", "http://10.0.4.37:8080", "");

    private String developerName;

    private String ip;

    private String projectName;

    IPAddressEnum(String developerName, String ip, String projectName) {

        this.developerName = developerName;

        this.ip = ip;

        this.projectName = projectName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }
}
