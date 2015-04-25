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

import android.os.Handler;
import android.os.Looper;

import java.util.Set;

public class Pregun extends Barrel {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Timer.Listener ls = new Timer.Listener() {

        @Override
        public void onFinished(long time) {
            Set<Bullet> bullets = pshots.get(time);
            if (bullets != null) {
                for (Bullet b : bullets)
                    shoot(b.getSerial(), b.getTailTag());
                bullets.clear();
            }
        }
    };
    public volatile int count = 0;

    @Override
    protected void clear() {
        super.clear();
        count = 0;
    }

    @Override
    protected Timer getTimer(long timeout) {
        return null;
    }

    public void shoot(final int serial, final TailTag tailTag) {
        checkNegative(serial, tailTag, "Gun");
        count++;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            shoot(serial, tailTag, true);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    shoot(serial, tailTag, true);
                }
            });
        }
    }

    public void shootDelayed(int serial, TailTag tag, long timeout) {
        shootDelayed(serial, timeout, tag, ls);
    }

    @Override
    protected void shootInfinity(int serial, TailTag tailTag) {
        super.shootInfinity(serial, tailTag);
        count++;
    }
}
