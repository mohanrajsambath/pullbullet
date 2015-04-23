package com.micronic.pullbullet;

import android.os.Handler;
import android.os.Looper;

import java.util.Set;

public class Barel extends Barrel {
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
    public int count = 0;

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
    protected void shootInfinit(int serial, TailTag tailTag) {
        checkNull(tailTag,
                "A bullet di infinty cannot be shot with a null tailtag.");
        count++;
        TailTag tag = sticks.get(serial);
        if (tag == null)
            sticks.put(serial, tailTag);
        shoot(serial, tailTag);
    }
}
