package com.jl.jczj.im.loop;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhouyi on 2017/5/27.
 */

public class MessageLoopOperator {

    private final int LOOP_INTERVAL = 5000;

    private LoopMessageListener listener;

    private Timer mTimer;

    public void init(LoopMessageListener listener) {
        mTimer = new Timer();
        this.listener = listener;
    }

    public void run() {
        mTimer.schedule(new MyTimerTask(), LOOP_INTERVAL);

    }

    public void cancel() {
        mTimer.cancel();
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            listener.execute();
        }
    }
}
