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

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

public class Shaker implements SensorEventListener {
    private final Listener listener;
    private final SampleQueue queue = new SampleQueue();
    private Sensor accelerometer;
    private int or = 6;
    private int prox = 2;
    private Sensor proxy;

    public Shaker(Listener listener) {
        this.listener = listener;
    }

    private int checkOrientation(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (z > 9 && (x > -0.8 && x < 1.2) && (y > -0.8 && y < 1.2))
            return 0;
        else if (z < -9 && (x > -0.8 && x < 1.2) && (y > -0.8 && y < 1.2))
            return 1;
        else if (y > 9 && (x > -0.8 && x < 1.2) && (z > -0.8 && z < 1.2))
            return 2;
        else if (y < -9 && (x > -0.8 && x < 1.2) && (z > -0.8 && z < 1.2))
            return 3;
        else if (x > 9 && (y > -0.8 && y < 1.2) && (z > -0.8 && z < 1.2))
            return 4;
        else if (x < -9 && (y > -0.8 && y < 1.2) && (z > -0.8 && z < 1.2))
            return 5;
        return or;
    }

    private int checkProxy(SensorEvent event) {
        float v = event.values[0];
        if (v == 0)
            return 0;
        else
            return 1;
    }

    private boolean isAccelerating(SensorEvent event) {
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
        final double magnitudeSquared = ax * ax + ay * ay + az * az;
        return magnitudeSquared > 13 * 13;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == proxy) {
            int pro = checkProxy(event);
            if (pro != prox) {
                listener.proxyChanged(pro);
                prox = pro;
            }
        } else {
            boolean accelerating = isAccelerating(event);
            long timestamp = event.timestamp;
            queue.add(timestamp, accelerating);
            if (queue.isShaking()) {
                queue.clear();
                listener.hearShake();
            }
            int ori = checkOrientation(event);
            if (or != ori && (ori <= 5 && ori >= 0)) {
                listener.orChanged(ori);
                or = ori;
            }
        }
    }

    public boolean start(SensorManager sensorManager) {
        if (accelerometer != null) {
            return true;
        }

        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
        return accelerometer != null;
    }

    public boolean startProxy(SensorManager sm) {
        if (proxy != null)
            return true;
        proxy = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proxy != null) {
            sm.registerListener(this, proxy, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return proxy != null;
    }

    public void stop(SensorManager sm) {
        if (accelerometer != null) {
            sm.unregisterListener(this, accelerometer);
            accelerometer = null;
        }
    }

    public void stopProxy(SensorManager sm) {
        if (proxy != null) {
            sm.unregisterListener(this, proxy);
            proxy = null;
        }
    }

    public interface Listener {
        void hearShake();

        void orChanged(int or);

        void proxyChanged(int proxy);
    }

    static class Sample {
        boolean accelerating;
        Sample next;
        long timestamp;
    }

    static class SamplePool {
        private Sample head;

        Sample acquire() {
            Sample acquired = head;
            if (acquired == null) {
                acquired = new Sample();
            } else {
                head = acquired.next;
            }
            return acquired;
        }

        void release(Sample sample) {
            sample.next = head;
            head = sample;
        }
    }

    static class SampleQueue {
        private final SamplePool pool = new SamplePool();
        private int acceleratingCount;
        private Sample newest;
        private Sample oldest;
        private int sampleCount;

        void add(long timestamp, boolean accelerating) {
            purge(timestamp - 500000000);
            Sample added = pool.acquire();
            added.timestamp = timestamp;
            added.accelerating = accelerating;
            added.next = null;
            if (newest != null) {
                newest.next = added;
            }
            newest = added;
            if (oldest == null) {
                oldest = added;
            }
            sampleCount++;
            if (accelerating) {
                acceleratingCount++;
            }
        }

        List<Sample> asList() {
            List<Sample> list = new ArrayList<Sample>();
            Sample s = oldest;
            while (s != null) {
                list.add(s);
                s = s.next;
            }
            return list;
        }

        void clear() {
            while (oldest != null) {
                Sample removed = oldest;
                oldest = removed.next;
                pool.release(removed);
            }
            newest = null;
            sampleCount = 0;
            acceleratingCount = 0;
        }

        boolean isShaking() {
            return newest != null
                    && oldest != null
                    && newest.timestamp - oldest.timestamp >= (500000000 >> 1)
                    && acceleratingCount >= (sampleCount >> 1)
                    + (sampleCount >> 2);
        }

        void purge(long cutoff) {
            while (sampleCount >= 4 && oldest != null
                    && cutoff - oldest.timestamp > 0) {
                Sample removed = oldest;
                if (removed.accelerating) {
                    acceleratingCount--;
                }
                sampleCount--;

                oldest = removed.next;
                if (oldest == null) {
                    newest = null;
                }
                pool.release(removed);
            }
        }
    }
}
