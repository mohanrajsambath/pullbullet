package com.micronic.pullbullet;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class Barrel {
    protected final ConcurrentMap<Integer, Set<Magnett>> handlers = new ConcurrentHashMap<Integer, Set<Magnett>>();
    protected final ConcurrentMap<Long, Set<Bullet>> pshots = new ConcurrentHashMap<Long, Set<Bullet>>();
    protected final ConcurrentMap<Integer, TailTag> sticks = new ConcurrentHashMap<Integer, TailTag>();
    private final ThreadLocal<ConcurrentLinkedQueue<Shooter>> bullets = new ThreadLocal<ConcurrentLinkedQueue<Shooter>>() {
        @Override
        protected ConcurrentLinkedQueue<Shooter> initialValue() {
            return new ConcurrentLinkedQueue<Shooter>();
        }
    };
    private final ThreadLocal<Boolean> isDispatching = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    private final ThreadLocal<ConcurrentLinkedQueue<Timer>> timers = new ThreadLocal<ConcurrentLinkedQueue<Timer>>() {
        @Override
        protected ConcurrentLinkedQueue<Timer> initialValue() {
            return new ConcurrentLinkedQueue<Timer>();
        }
    };
    protected TailTag last;
    protected boolean shattered = false;

    protected void checkNull(Object obj, String msg) {
        if (obj == null)
            throw new NullPointerException(msg);
    }

    private void addNewTimer(long timeout, Timer.Listener ls) {
        Log.e("barrel", "new timer added");
        Timer timer = new Timer(ls);
        timer.queue(timeout);
        timers.get().add(timer);
    }

    protected Timer getTimer(long timeout) {
        for (Timer timer : timers.get()) {
            if (timer.canQueue(timeout))
                return timer;
        }
        return null;
    }

    protected boolean apply(Primer p, TailTag tag) {
        if (p == null)
            return true;
        for (Check c : p) {
            if (!c.matches(tag.get(c.getArg()).toString()))
                return false;
        }
        return true;
    }

    protected void checkNegative(int serial, TailTag tag, String gun) {
        if (serial < 0) {
            throw new IllegalArgumentException("A " + gun
                    + " cannot shoot negative bullets. Be positive :)");
        }
    }

    protected void clear() {
        for (Integer serial : handlers.keySet()) {
            Set<Magnett> magnets = handlers.get(serial);
            if (magnets != null)
                magnets.clear();
        }
        handlers.clear();
        shatterDelayedBullets();
    }

    public void shatterDelayedBullets() {
        for (int i = 0; i < timers.get().size(); i++) {
            Timer timer = timers.get().poll();
            timer.stop();
        }
        for (Long tt : pshots.keySet()) {
            Set<Bullet> bullets = pshots.get(tt);
            if (bullets != null)
                bullets.clear();
        }
        pshots.clear();
    }

    private void dispatch(Shooter shooter) {
        if (shooter.tag != null && shooter.tag.get("pullbullet.destruct").toBoolean()) {
            shattered = true;
            return;
        }
        shooter.magnet.onStuck(new Bullet(shooter.serial, shooter.tag));
    }

    private void enqueue(Magnet magnet, int serial, TailTag tag) {
        bullets.get().offer(new Shooter(magnet, serial, tag));
    }

    protected TailTag getSticky(int serial) {
        return sticks.get(serial);
    }

    private void postAll() {
        if (isDispatching.get()) {
            return;
        }
        shattered = false;
        isDispatching.set(true);
        int size = bullets.get().size();
        int i = 0;
        boolean not = false;
        try {
            while (true) {
                if (i >= size) {
                    not = true;
                }
                Shooter shooter = bullets.get().poll();
                if (shooter == null)
                    break;
                if (i != 0 && !not)
                    shooter.tag = last;
                dispatch(shooter);
                last = shooter.tag;
                i++;
            }
        } finally {
            isDispatching.set(false);
        }
    }

    protected TailTag pul(int serial, int hp, Magnet magnet, Primer primer) {
        if (magnet != null) {
            Set<Magnett> magnets = handlers.get(serial);
            if (magnets == null) {
                Set<Magnett> magnetsCreated = new CopyOnWriteArraySet<Magnett>();
                magnets = handlers.putIfAbsent(serial, magnetsCreated);
                if (magnets == null)
                    magnets = magnetsCreated;
            }
            magnets.add(new Magnett(magnet, hp, primer));
        }
        return getSticky(serial);
    }

    protected void releas(int serial, Magnet magnet) {
        if (magnet == null) {
            sticks.remove(serial);
        } else {
            Set<Magnett> magnets = handlers.get(serial);
            if (magnets != null)
                magnets.remove(magnet);
        }
    }

    protected Integer[] serials() {
        return handlers.keySet().toArray(new Integer[handlers.keySet().size()]);
    }

    protected void shoot(int serial, TailTag tailTag, boolean bounceIfMissed) {
        boolean dispatched = false;
        Set<Magnett> magnets = handlers.get(serial);
        if (magnets != null && !magnets.isEmpty()) {
            List<Magnett> magnts = new ArrayList<Magnett>(magnets);
            Collections.sort(magnts, new Comparator<Magnett>() {

                @Override
                public int compare(Magnett arg0, Magnett arg1) {
                    return arg1.power - arg0.power;
                }
            });
            if (!magnts.isEmpty()) {
                dispatched = true;
                for (Magnett magnet : magnts) {
                    if (apply(magnet.primer, tailTag))
                        enqueue(magnet.magnet, serial, tailTag);
                }
            }
        }
        String serialKey = "serial";
        if (!dispatched && serial != -1 && bounceIfMissed)
            shoot(-1, new TailTag().put(serialKey, serial), false);
        postAll();
    }

    protected void shootDelayed(int serial, long timeout, TailTag tailTag,
                                Timer.Listener ls) {
        Timer timer = getTimer(timeout);
        if (timer == null)
            addNewTimer(timeout, ls);
        else
            timer.queue(timeout);
        Set<Bullet> bullets = pshots.get(timeout);
        if (bullets == null) {
            Set<Bullet> bsCreated = new CopyOnWriteArraySet<Bullet>();
            bullets = pshots.putIfAbsent(timeout, bsCreated);
            if (bullets == null)
                bullets = bsCreated;
        }
        bullets.add(new Bullet(serial, tailTag));
    }

    protected void shootInfinit(int serial, TailTag tailTag) {
        checkNull(tailTag,
                "A bullet di infinty cannot be shot with a null tailtag.");
        TailTag tag = sticks.get(serial);
        if (tag == null)
            sticks.put(serial, tailTag);
        shoot(serial, tailTag, false);
    }

    protected int size() {
        return handlers.size();
    }

    protected int size(int serial) {
        Set<Magnett> magnets = handlers.get(serial);
        if (magnets == null || magnets.isEmpty())
            return 0;
        return magnets.size();
    }

    private static class Magnett {
        final Magnet magnet;
        final int power;
        final Primer primer;

        public Magnett(Magnet magnet, int power, Primer primer) {
            this.magnet = magnet;
            this.power = power;
            this.primer = primer;
        }

    }

    private static class Shooter {
        final Magnet magnet;
        final int serial;
        TailTag tag;

        public Shooter(Magnet magnet, int serial, TailTag tag) {
            this.magnet = magnet;
            this.serial = serial;
            this.tag = tag;
        }
    }

}
