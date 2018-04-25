package com.tencent.ilivesdk.tools.quality;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 质量数据
 */

public class ILiveQualityData {

    private static Map<String, LiveInfo> lives = new ConcurrentHashMap<>();

    //开始时间
    private long startTime;
    //结束时间
    private long endTime;
    //发包丢包率，以百分比乘以100为返回值。如丢包率为12.34%，则sendLossRate = 1234
    private int sendLossRate;
    //收包丢包率，以百分比乘以100为返回值。如丢包率为12.34%，则recvLossRate = 1234
    private int recvLossRate;
    //app占用CPU，以百分比乘以100为返回值。如占用率为12.34%，则appCPURate = 1234
    private int appCPURate;
    //系统占用CPU，以百分比乘以100为返回值。如占用率为12.34%，则sysCPURate = 1234
    private int sysCPURate;
    //发送码率
    private int sendKbps;
    //接收码率
    private int recvKbps;
    //上行视频帧率
    private int upFPS;

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getSendLossRate() {
        return sendLossRate;
    }

    public int getRecvLossRate() {
        return recvLossRate;
    }

    public int getAppCPURate() {
        return appCPURate;
    }

    public int getSysCPURate() {
        return sysCPURate;
    }

    public int getSendKbps() {
        return sendKbps;
    }

    public int getRecvKbps() {
        return recvKbps;
    }

    public int getUpFPS() {
        return upFPS;
    }

    public Map<String, LiveInfo> getLives() {
        return new HashMap<>(lives);
    }


    public ILiveQualityData(long startTime, long endTime, long sendLossRate, long recvLossRate, long appCPURate, long sysCPURate, long sendKbps, long recvKbps, long fps){
        this.startTime = startTime;
        this.endTime = endTime;
        this.sendLossRate = (int)sendLossRate;
        this.recvLossRate = (int)recvLossRate;
        this.appCPURate = (int)appCPURate;
        this.sysCPURate = (int)sysCPURate;
        this.sendKbps = (int)sendKbps;
        this.recvKbps = (int)recvKbps;
        this.upFPS = (int)fps;
    }

    public String toString(){
        String livesStr = "";
        if (lives.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(", lives:").append("\n");
            for (String id : lives.keySet()) {
                sb.append(id).append(" ");
                sb.append("width:").append(lives.get(id).getWidth()).append(" ").append("height:").append(lives.get(id).getHeight()).append("\n");
            }
            livesStr = sb.toString();
        }
        return "QualityData " +"\n"+
                "startTime='" + startTime + "\n"+
                ", endTime='" + endTime + "\n" +
                ", sendLossRate='" + sendLossRate + "\n" +
                ", recvLossRate='" + recvLossRate + "\n" +
                ", appCPURate='" + appCPURate + "\n" +
                ", sysCPURate='" + sysCPURate + "\n" +
                ", sendKbps='" + sendKbps + "\n" +
                ", recvKbps='" + recvKbps + "\n" +
                ", upFPS='" + upFPS + "\n" +
                livesStr;
    }

    public static void addLive(String id, int width, int height) {
        lives.put(id, new LiveInfo(width, height));
    }

    public static void removeLive(String id) {
        if (lives.containsKey(id)) {
            lives.remove(id);
        }
    }

    public static void clearLive() {
        if (lives != null) {
            lives.clear();
        }
    }


}

