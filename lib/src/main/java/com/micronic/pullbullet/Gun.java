package com.micronic.pullbullet;

public class Gun {

    private static final Barel barel = new Barel();

    public static Bullet pull(int serial, int horsePower, Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, horsePower, magnet, null));
    }

    public static Bullet pull(int serial, int horsePower, Primer primer,
                              Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, horsePower, magnet, primer));
    }

    public static Bullet pull(int serial, Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, 100, magnet, null));
    }

    public static Bullet pull(int serial, Primer primer, Magnet magnet) {
        return new Bullet(serial, barel.pul(serial, 100, magnet, primer));
    }

    public static void release(int serial, Magnet magnet) {
        barel.releas(serial, magnet);
    }

    public static void shoot(Bullet bullet) {
        barel.checkNull(bullet, "Bullet to be shot cannot be null");
        barel.shoot(bullet.getSerial(), bullet.getTailTag());
    }

    public static void shoot(int serial, TailTag tailTag) {
        barel.shoot(serial, tailTag);
    }

    public static void shootDelayed(int serial, TailTag tailTag, long timeout) {
        barel.shootDelayed(serial, tailTag, timeout);
    }

    public static void shootDelayed(Bullet bullet, long timeout) {
        barel.checkNull(bullet, "Bullet to be shot cannot be null");
        shootDelayed(bullet.getSerial(), bullet.getTailTag(), timeout);
    }

    public static void shootInfinity(Bullet bullet) {
        barel.checkNull(bullet, "Bullet to be shot cannot be null");
        barel.shootInfinit(bullet.getSerial(), bullet.getTailTag());
    }

    public static void shootInfinity(int serial, TailTag tailTag) {
        barel.shootInfinit(serial, tailTag);
    }

    public static int getFireCount() {
        return barel.count;
    }

    public static void shatterDelayedBullets() {
        barel.shatterDelayedBullets();
    }
}
