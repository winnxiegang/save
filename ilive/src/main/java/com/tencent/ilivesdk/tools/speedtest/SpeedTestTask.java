package com.tencent.ilivesdk.tools.speedtest;

import com.tencent.ilivesdk.protos.gv_comm_operate;
import com.tencent.imsdk.IMMsfCoreProxy;
import com.tencent.imsdk.QLog;
import com.tencent.mobileqq.pb.InvalidProtocolBufferMicroException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 *  对单个接口机的测速任务
 */

class SpeedTestTask implements ILiveServerInfo, ILiveSpeedTestResult{

    private static final String TAG = SpeedTestTask.class.getSimpleName();

    private static final int BUSS_TYPE = 7;


    InetAddress ip;
    int ipInt;
    int port;
    int testCnt;
    int testGap;
    private int testTimeout;
    private int testPkgSize;
    private long testId;
    private long uin;
    private int clientIp;
    private DatagramChannel channel;
    private List<RecvPkt> results;
    private boolean[] hasResult;
    private int currentPkg = 0;
    private boolean needStop = false;

    private int totalRtt = 0, rtt0_50 = 0, rtt50_100 = 0, rtt100_200 = 0, rtt200_300 = 0, rtt300_700 = 0, rtt700_1000 = 0, rtt1000 = 0,
            jitter0_20 = 0, jitter20_50 = 0, jitter50_100 = 0, jitter100_200 = 0, jitter200_300 = 0, jitter300_500 = 0, jitter500_800 = 0, jitter800 = 0,
            t1_uploss = 10000, t1_dwloss = 10000,
            up_cons_loss0 = 0, up_cons_loss1 = 0, up_cons_loss2 = 0, up_cons_loss3 = 0, up_cons_lossb3 = 0,
            dw_cons_loss0 = 0, dw_cons_loss1 = 0, dw_cons_loss2 = 0, dw_cons_loss3 = 0, dw_cons_lossb3 = 0,
            up_disorder = 0, dw_disorder = 0,
            maxRtt, avg_rtt = Integer.MAX_VALUE, minRtt = Integer.MAX_VALUE;
    private int[] mUpSeq = null;


    SpeedTestTask(gv_comm_operate.SpeedAccessInf info, long testId, long uin, int clientIp) throws UnknownHostException {
        ipInt = info.access_ip.get();
        port = info.access_port.get();
        byte[] ipBytes = new byte[4];
        ipBytes[3] =  (byte) ((ipInt>>24) & 0xFF);
        ipBytes[2] =  (byte) ((ipInt>>16) & 0xFF);
        ipBytes[1] =  (byte) ((ipInt>>8) & 0xFF);
        ipBytes[0] =  (byte) (ipInt & 0xFF);
        ip = InetAddress.getByAddress(ipBytes);
        testCnt = info.test_cnt.get();
        testGap = info.test_gap.get();
        testTimeout = info.test_timeout.get();
        testPkgSize = info.test_pkg_size.get();
        testPkgSize = info.test_pkg_size.get();
        results = new LinkedList<>();
        this.testId = testId;
        this.uin = uin;
        this.clientIp = clientIp;
        hasResult = new boolean[testCnt];
        Arrays.fill(hasResult, false);
    }



    void read(byte[] pbBody) throws InvalidProtocolBufferMicroException {
        RecvPkt pkt = new RecvPkt(pbBody);
        if (pkt.getRtt() < testTimeout && !hasResult[pkt.getPktSeq()]) {
            hasResult[pkt.getPktSeq()] = true;
            results.add(pkt);
        }

    }

    void stop() {
        needStop = true;
    }


    void start() throws IOException, InterruptedException {
        //TODO 处理包pb包内容大于testPkgSize的情况
        channel = DatagramChannel.open();
        channel.connect(new InetSocketAddress(ip, port));
        channel.configureBlocking(true);
        channel.socket().setSoTimeout(testTimeout);
        new Thread(new Runnable() {
            @Override
            public void run() {

                ByteBuffer buf = ByteBuffer.allocate(testPkgSize);
                long time = 0;
                while (true) {
                    try {
                        buf.clear();
                        if (time != 0 && Calendar.getInstance().getTimeInMillis() - time >= testTimeout ||
                                results.size() == testCnt || needStop) {
                            release();
                            break;
                        }
                        if (currentPkg >= testCnt) {
                            time = Calendar.getInstance().getTimeInMillis();
                        }
                        channel.read(buf);
                        buf.flip();
                        if (buf.limit() == 0) continue;
                        byte headSign = buf.get();
                        byte ver = buf.get();
                        short pktLen = buf.getShort();
                        short pbLen = buf.getShort();
                        long testId = buf.getLong();
                        byte[] pbBody = new byte[pbLen];
                        buf.get(pbBody);
                        read(pbBody);
                    }catch (IOException e) {
                        QLog.e(TAG, QLog.USR, "read UDP IOException");
                        break;
                    }
                }

            }
        }).start();
        ByteBuffer buffer = ByteBuffer.allocate(testPkgSize);
        gv_comm_operate.SpeedTestHeadPkt pkt = new gv_comm_operate.SpeedTestHeadPkt();
        for (int i = 0; i < testCnt; ++i) {
            if (needStop) return;
            buffer.clear();
            buffer.put((byte)0x95);
            buffer.put((byte)0x1);
            buffer.putShort((short)testPkgSize);
            pkt.test_id.set(testId);
            pkt.uin.set(uin);
            pkt.sdkappid.set(IMMsfCoreProxy.get().getSdkAppId());
            pkt.seq.set(i);
            pkt.timestamp.set(Calendar.getInstance().getTimeInMillis());
            pkt.buss_type.set(BUSS_TYPE);
            byte[] pb = pkt.toByteArray();
            buffer.putShort((short)pb.length);
            buffer.putLong(testId);
            buffer.put(pb);
            int left = testPkgSize - (1 + 1 + 2 + 2 + 8 + pb.length);
            for (int j = 0; j < left; ++j) {
                buffer.put((byte)0x1);
            }
            buffer.flip();
            channel.write(buffer);
            currentPkg++;
            Thread.sleep(testGap);
        }

        Thread.sleep(testTimeout);
    }

    gv_comm_operate.SpeedTestResult getResult() {
        QLog.d(TAG, QLog.CLR, "start calculate result for server " + ip);
        gv_comm_operate.SpeedTestResult ret = new gv_comm_operate.SpeedTestResult();
        ret.access_ip.set(ipInt);
        ret.access_port.set(port);
        ret.clientip.set(clientIp);
        ret.test_cnt.set(testCnt);
        ret.test_pkg_size.set(testPkgSize);
        if (results.size() > 0 && checkDataValidity()) {
            int[] rtts = new int[testCnt];
            Arrays.fill(rtts, -1);
            for (int i = 0; i < results.size(); ++i) {
                int rtt = results.get(i).getRtt();
                totalRtt += rtt;
                if (rtt > maxRtt) {
                    maxRtt = rtt;
                }
                if (rtt < minRtt) {
                    minRtt = rtt;
                }
                if (rtt < 50) rtt0_50++;
                if (rtt >= 50 && rtt < 100) rtt50_100++;
                if (rtt >= 100 && rtt < 200) rtt100_200++;
                if (rtt >= 200 && rtt < 300) rtt200_300++;
                if (rtt >= 300 && rtt < 700) rtt300_700++;
                if (rtt >= 700 && rtt < 1000) rtt700_1000++;
                if (rtt >= 1000) rtt1000++;
                rtts[results.get(i).getPktSeq()] = rtt;
            }
            avg_rtt = totalRtt/results.size();
            QLog.d(TAG, QLog.CLR, "recv rtts " + arrayToString(rtts));
            QLog.d(TAG, QLog.CLR, "up seq " + arrayToString(mUpSeq));
            t1_uploss = (testCnt - mUpSeq.length)*10000/testCnt;
            if (mUpSeq.length - results.size() < 0) {
                t1_dwloss = 0;
            }else {
                t1_dwloss = (mUpSeq.length - results.size())*10000/mUpSeq.length;
            }
            for (int i = 0; i < mUpSeq.length; ++i) {
                for (int j = i + 1; j < mUpSeq.length; ++j) {
                    if (mUpSeq[i] > mUpSeq[j]) {
                        up_disorder++;
                    }
                }
            }
            ret.up_seq.set(asList(mUpSeq));
            Arrays.sort(mUpSeq);
            int last = -1;
            for (int i = 0; i <= mUpSeq.length; ++i) {
                int sub;
                if (i == mUpSeq.length) {
                    if (last == mUpSeq.length - 1) break;
                    sub = testCnt - last - 1;
                }else {
                    sub = mUpSeq[i] - last - 1;
                    last = mUpSeq[i];
                }
                if (sub == 0) {
                    up_cons_loss0++;
                }else if (sub == 1) {
                    up_cons_loss1++;
                }else if (sub == 2) {
                    up_cons_loss2++;
                }else if (sub == 3) {
                    up_cons_loss3++;
                }else {
                    up_cons_lossb3++;
                }
            }
            int[] dwSeq = getDwSeq();
            QLog.d(TAG, QLog.CLR, "down seq " + arrayToString(dwSeq));
            ret.dw_seq.set(asList(dwSeq));
            for (int i = 0; i < dwSeq.length; ++i) {
                for (int j = i + 1; j < dwSeq.length; ++j) {
                    if (dwSeq[i] > dwSeq[j]) {
                        dw_disorder++;
                    }
                }
            }
            Arrays.sort(dwSeq);
            last = -1;
            for (int i = 0; i <= dwSeq.length; ++i) {
                int sub;
                if (i == dwSeq.length) {
                    if (last == dwSeq.length - 1) break;
                    sub = testCnt - last - 1;
                    int missUp = missCount(mUpSeq, last + 1, testCnt);
                    sub -= missUp;
                }else {
                    sub = dwSeq[i] - last - 1;
                    int missUp = missCount(mUpSeq, last + 1, dwSeq[i]);
                    last = dwSeq[i];
                    sub -= missUp;
                }
                if (sub == 0) {
                    dw_cons_loss0++;
                }else if (sub == 1) {
                    dw_cons_loss1++;
                }else if (sub == 2) {
                    dw_cons_loss2++;
                }else if (sub == 3) {
                    dw_cons_loss3++;
                }else {
                    dw_cons_lossb3++;
                }
            }
            for (int i = 1; i < rtts.length; ++i) {
                if (rtts[i] >= 0 && rtts[i - 1] >= 0) {
                    int diff = Math.abs(rtts[i] - rtts[i - 1]);
                    if (diff < 20) jitter0_20++;
                    if (diff >= 20 && diff < 50) jitter20_50++;
                    if (diff >= 50 && diff < 100) jitter50_100++;
                    if (diff >= 100 && diff < 200) jitter100_200++;
                    if (diff >= 200 && diff < 300) jitter200_300++;
                    if (diff >= 300 && diff < 500) jitter300_500++;
                    if (diff >= 500 && diff < 800) jitter500_800++;
                    if (diff >= 800) jitter800++;
                }else {
                    jitter800++;
                }
            }
        }else {
            maxRtt = Integer.MAX_VALUE;
            minRtt = Integer.MAX_VALUE;
            avg_rtt = Integer.MAX_VALUE;
            rtt1000 = testCnt;
        }
        ret.avg_rtt.set(avg_rtt);
        ret.max_rtt.set(maxRtt);
        ret.min_rtt.set(minRtt);
        ret.rtt0_50.set(rtt0_50);
        ret.rtt50_100.set(rtt50_100);
        ret.rtt100_200.set(rtt100_200);
        ret.rtt200_300.set(rtt200_300);
        ret.rtt300_700.set(rtt300_700);
        ret.rtt700_1000.set(rtt700_1000);
        ret.rtt1000.set(rtt1000);
        ret.jitter0_20.set(jitter0_20);
        ret.jitter20_50.set(jitter20_50);
        ret.jitter50_100.set(jitter50_100);
        ret.jitter100_200.set(jitter100_200);
        ret.jitter200_300.set(jitter200_300);
        ret.jitter300_500.set(jitter300_500);
        ret.jitter500_800.set(jitter500_800);
        ret.jitter800.set(jitter800);
        ret.t1_uploss.set(t1_uploss);
        ret.t1_dwloss.set(t1_dwloss);
        ret.up_cons_loss0.set(up_cons_loss0);
        ret.up_cons_loss1.set(up_cons_loss1);
        ret.up_cons_loss2.set(up_cons_loss2);
        ret.up_cons_loss3.set(up_cons_loss3);
        ret.up_cons_lossb3.set(up_cons_lossb3);
        ret.dw_cons_loss0.set(dw_cons_loss0);
        ret.dw_cons_loss1.set(dw_cons_loss1);
        ret.dw_cons_loss2.set(dw_cons_loss2);
        ret.dw_cons_loss3.set(dw_cons_loss3);
        ret.dw_cons_lossb3.set(dw_cons_lossb3);
        ret.up_disorder.set(up_disorder);
        ret.dw_disorder.set(dw_disorder);
        String str = getResultString();
        QLog.d(TAG, QLog.CLR, "test result " + getResultString());
        return ret;

    }

    /**
     *  确认是否数据有效，否则不计算结果，直接返回默认值
     */
    private boolean checkDataValidity() {
        for (RecvPkt pkt : results) {
            if (pkt.getPktSeq() < 0 || pkt.getPktSeq() >= testCnt) return false;
            int[] upseq = pkt.getUpseq();
            if (mUpSeq == null || upseq.length > mUpSeq.length) {
                mUpSeq = upseq;
                boolean flag = false;
                if (upseq.length == 0) return false;
                for (int i : upseq) {
                    if (i < 0 || i >= testCnt) return false;
                    if (!flag && i == pkt.getPktSeq()) flag = true;
                }
                if (!flag) return false;
            }
        }
        return true;
    }


    private int[] getDwSeq() {
        int[] ret = new int[results.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = results.get(i).pkt.seq.get();
        }
        return ret;
    }

    private int missCount(int[] array, int s, int e) {
        int count = 0;
        for (int anArray : array) {
            if (anArray > s && anArray < e) {
                ++count;
            }
            if (anArray >= e) break;
        }
        return e - s - count;
    }


    private List<Integer> asList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        return list;
    }


    private void release() throws IOException {
        channel.disconnect();
        channel.close();
    }

    private String arrayToString(int[] array) {
        if (array == null) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i : array) {
            sb.append(i).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }


    private String getResultString() {
        StringBuilder sb = new StringBuilder();
        sb.append("rtt0_50 " + rtt0_50 + ",rtt50_100 " + rtt50_100 + ",rtt100_200 " + rtt100_200 + ",rtt200_300 " + rtt200_300 + ",rtt300_700 " + rtt300_700 + ",rtt700_1000 " + rtt700_1000 + ",rtt1000 " + rtt1000 + "\n");
        sb.append("jitter0_20 " + jitter0_20 + ",jitter20_50 " + jitter20_50 + ",jitter50_100 " + jitter50_100 + ",jitter100_200 " + jitter100_200 + ",jitter200_300 " + jitter200_300 + ",jitter300_500 " + jitter300_500 + ",jitter500_800 " + jitter500_800 + ",jitter800 " + jitter800 + "\n");
        sb.append("t1_uploss " + t1_uploss + ",t1_dwloss " + t1_dwloss + "\n");
        sb.append("up_cons_loss0 " + up_cons_loss0 + ",up_cons_loss1 " + up_cons_loss1 + ",up_cons_loss2 " + up_cons_loss2 + ",up_cons_loss3 " + up_cons_loss3 + ",up_cons_lossb3 " + up_cons_lossb3 + "\n");
        sb.append("dw_cons_loss0 " + dw_cons_loss0 + ",dw_cons_loss1 " + dw_cons_loss1 + ",dw_cons_loss2 " + dw_cons_loss2 + ",dw_cons_loss3 " + dw_cons_loss3 + ",dw_cons_lossb3 " + dw_cons_lossb3 + "\n");
        sb.append("up_disorder " + up_disorder + ",dw_disorder " + dw_disorder + "\n");
        sb.append("max_rtt " + maxRtt + ",min_rtt " + minRtt + ",avg_rtt " + avg_rtt);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "SpeedTestTask connect to" + ip + " " + port;
    }


    /**
     * 获取服务器地址
     */
    @Override
    public InetAddress getAddress() {
        return ip;
    }

    /**
     * 获取服务器端口
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * 获取测速目标服务器信息
     */
    @Override
    public ILiveServerInfo getServerInfo() {
        return this;
    }

    /**
     * 获取上行丢包率
     *
     * @return 上行丢包率（万分比）
     */
    @Override
    public int getUpLoss() {
        return t1_uploss;
    }

    /**
     * 获取下行丢包率
     *
     * @return 下行丢包率（万分比）
     */
     @Override
    public int getDownLoss() {
        return t1_dwloss;
    }

    /**
     * 获取平均时延
     *
     * @return 平均时延，单位毫秒
     */
    @Override
    public int getAvgRtt() {
        return avg_rtt;
    }

    /**
     * 获取最大时延
     *
     * @return 最大时延，单位毫秒
     */
    @Override
    public int getMaxRtt() {
        return maxRtt;
    }

    /**
     * 获取最小时延
     *
     * @return 最小时延，单位毫秒
     */
    @Override
    public int getMinRtt() {
        return minRtt;
    }

    /**
     * 获取上行乱序
     *
     * @return 上行乱序数，服务器收到包的顺序和发送顺序相比较获得的逆序数
     */
    @Override
    public int getUpDisorder() {
        return up_disorder;
    }

    /**
     * 获取下行乱序
     *
     * @return 下行乱序数，客户端收到回包的顺序和发送顺序相比较获得的逆序数
     */
    @Override
    public int getDownDisorder() {
        return dw_disorder;
    }
}


class RecvPkt {

    private static final String TAG = RecvPkt.class.getSimpleName();
    final long timeStamp;
    final gv_comm_operate.SpeedTestHeadPkt pkt;

    RecvPkt(byte[] buffer) throws InvalidProtocolBufferMicroException {
        this.timeStamp = Calendar.getInstance().getTimeInMillis();
        pkt = new gv_comm_operate.SpeedTestHeadPkt();
        pkt.mergeFrom(buffer);
    }

    int getRtt() {
        long sendTime = pkt.timestamp.get();
        if (timeStamp < sendTime) {
            QLog.d(TAG, QLog.USR,  "recv timestamp error");
            return Integer.MAX_VALUE;
        }else {
            return (int) (timeStamp - sendTime);
        }
    }

    int getPktSeq() {
        return pkt.seq.get();
    }

    int[] getUpseq() {
        int[] ret = new int[pkt.up_seq.get().size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = pkt.up_seq.get().get(i);
        }
        return ret;
    }
}