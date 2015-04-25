/*
 * Copyright (C) 2015 Vallabh Shevate
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.micronic.pullbullet;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;

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
        if (queue.isEmpty()) {
            dcr = 0;
            return false;
        }
        dcr += val;
        for (int i = 0; i < queue.size(); i++) {
            Long tt = queue.remove(i);
            tt -= val;
            if (tt < 0)
                tt = 0L;
            queue.add(i, tt);
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
        if (!started) {
            started = true;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(queue.get(0), queue.get(0) / 10) {

                @Override
                public void onFinish() {
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
            timer.cancel();
            timer = null;
            started = false;
            return true;
        }
        return false;
    }

    public interface Listener {
        public void onFinished(long time);
    }
}
