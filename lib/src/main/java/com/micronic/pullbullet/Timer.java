package com.micronic.pullbullet;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class Timer {

    private final Listener ls;
    private final List<Long> queue = new ArrayList<Long>();
    private long dcr = 0;
    private boolean started = false;
    private CountDownTimer timer;

    public Timer(Listener ls) {
        this.ls = ls;
    }

    private void add(long time) {
        queue.add(time);
        Collections.sort(queue);
        start();
    }

    private boolean decrement(Long val) {
        Log.e("timer", "val to be decremented " + val);
        if (queue.isEmpty()) {
            dcr = 0;
            Log.e("timer", "dcr failed");
            return false;
        }
        dcr += val;
        for (int i = 0; i < queue.size(); i++) {
            Long tt = queue.remove(i);
            tt -= val;
            if (tt < 0)
                tt = 0L;
            queue.add(i, tt);
            Log.e("timer", "tt:" + queue.get(i));
        }
        return true;
    }

    public long getCurrent() {
        if (queue.isEmpty())
            return 0L;
        else
            return queue.get(0);
    }

    public boolean isStarted() {
        return started;
    }

    public boolean queue(long time) {
        boolean canQueue = canQueue(time);
        if (canQueue)
            add(time);
        return canQueue;
    }

    public boolean canQueue(long time) {
        if (started) {
            if (time <= queue.get(0))
                return false;
        }
        return true;
    }

    private void start() {
        if (started) {
        } else {
            Log.e("timer", "started");
            started = true;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            Log.e("timer", "started");
            timer = new CountDownTimer(queue.get(0), queue.get(0) / 10) {

                @Override
                public void onFinish() {
                    Log.e("timer", "finished");
                    started = false;
                    try {
                        Long out = queue.remove(0);
                        ls.onFinished(out + dcr);
                        if (decrement(out))
                            start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTick(long arg0) {
                }
            };
            timer.start();
        }
    }

    public boolean stop() {
        if (started) {
            Log.e("timer", "stopped");
            timer.cancel();
            timer = null;
            started = false;
            return true;
        }
        Log.e("timer", "already stopped");
        return false;
    }

    public interface Listener {
        public void onFinished(long time);
    }
}
