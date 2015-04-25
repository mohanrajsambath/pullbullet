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

public class Gun {

    private static final Pregun pregun = new Pregun();

    public static synchronized void clear() {
        pregun.clear();
    }

    public static synchronized Bullet pull(int serial, int horsePower, Primer primer,
                                           Magnet magnet) {
        return pregun.pull(serial, horsePower, primer, magnet);
    }

    public static synchronized Bullet pull(int serial, int horsePower, Magnet magnet) {
        return pull(serial, horsePower, null, magnet);
    }

    public static synchronized Bullet pull(int serial, Magnet magnet) {
        return pull(serial, 100, null, magnet);
    }

    public static synchronized Bullet pull(int serial, Primer primer, Magnet magnet) {
        return pull(serial, 100, primer, magnet);
    }

    public static synchronized void release(int serial, Magnet magnet) {
        pregun.release(serial, magnet);
    }

    public static synchronized void shoot(Bullet bullet) {
        pregun.checkNull(bullet, "Bullet to be shot cannot be null");
        pregun.shoot(bullet.getSerial(), bullet.getTailTag());
    }

    public static synchronized void shoot(int serial, TailTag tailTag) {
        pregun.shoot(serial, tailTag);
    }

    public static synchronized void shootDelayed(int serial, TailTag tailTag, long timeout) {
        pregun.shootDelayed(serial, tailTag, timeout);
    }

    public static synchronized void shootDelayed(Bullet bullet, long timeout) {
        pregun.checkNull(bullet, "Bullet to be shot cannot be null");
        shootDelayed(bullet.getSerial(), bullet.getTailTag(), timeout);
    }

    public static synchronized void shootInfinity(Bullet bullet) {
        pregun.checkNull(bullet, "Bullet to be shot cannot be null");
        pregun.shootInfinity(bullet.getSerial(), bullet.getTailTag());
    }

    public static synchronized void shootInfinity(int serial, TailTag tailTag) {
        pregun.shootInfinity(serial, tailTag);
    }

    public static synchronized int getFireCount() {
        return pregun.count;
    }

    public static synchronized void shatterDelayedBullets() {
        pregun.shatterDelayedBullets();
    }
}
