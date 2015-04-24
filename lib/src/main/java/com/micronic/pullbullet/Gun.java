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

    private static final Barel barel = new Barel();

    public static synchronized Bullet pull(int serial, int horsePower, Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, horsePower, magnet, null));
    }

    public static synchronized Bullet pull(int serial, int horsePower, Primer primer,
                                           Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, horsePower, magnet, primer));
    }

    public static synchronized Bullet pull(int serial, Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, 100, magnet, null));
    }

    public static synchronized Bullet pull(int serial, Primer primer, Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, 100, magnet, primer));
    }

    public static synchronized void release(int serial, Magnet magnet) {
        barel.releas(serial, magnet);
    }

    public static synchronized void shoot(Bullet bullet) {
        barel.checkNull(bullet, "Bullet to be shot cannot be null");
        barel.shoot(bullet.getSerial(), bullet.getTailTag());
    }

    public static synchronized void shoot(int serial, TailTag tailTag) {
        barel.shoot(serial, tailTag);
    }

    public static synchronized void shootDelayed(int serial, TailTag tailTag, long timeout) {
        barel.shootDelayed(serial, tailTag, timeout);
    }

    public static synchronized void shootDelayed(Bullet bullet, long timeout) {
        barel.checkNull(bullet, "Bullet to be shot cannot be null");
        shootDelayed(bullet.getSerial(), bullet.getTailTag(), timeout);
    }

    public static synchronized void shootInfinity(Bullet bullet) {
        barel.checkNull(bullet, "Bullet to be shot cannot be null");
        barel.shootInfinit(bullet.getSerial(), bullet.getTailTag());
    }

    public static synchronized void shootInfinity(int serial, TailTag tailTag) {
        barel.shootInfinit(serial, tailTag);
    }

    public static synchronized int getFireCount() {
        return barel.count;
    }

    public static synchronized void shatterDelayedBullets() {
        barel.shatterDelayedBullets();
    }
}
