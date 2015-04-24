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

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class Rifle extends Barrel {
    private static Class<?> sClass = RegularService.class;
    private final ActivityManager am;
    private final String areg;
    private final String aunreg;
    private final String broken;
    private final ClipboardManager cliper;
    private final Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ThreadLocal<ConcurrentLinkedQueue<Intent>> intents = new ThreadLocal<ConcurrentLinkedQueue<Intent>>() {
        @Override
        protected ConcurrentLinkedQueue<Intent> initialValue() {
            return new ConcurrentLinkedQueue<Intent>();
        }
    };
    private final ThreadLocal<Boolean> isDispatching = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    private final String iSerial;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg0 != null && arg1 != null) {
                int serial = arg1.getIntExtra(iSerial, 0);
                if (serial == 0 || size(serial) == 0)
                    return;
                Bundle bundle;
                String data = getResultData() == null ? "" : getResultData();
                if (data.equals(broken))
                    bundle = getResultExtras(false);
                else
                    bundle = arg1.getExtras();
                bundle.remove(iSerial);
                TailTag tailTag = decode(bundle);
                post(serial, tailTag);
                if (shattered) {
                    abortBroadcast();
                    return;
                }
                setResultData(broken);
                setResultExtras(encode(last));
            }
        }
    };
    private final String iTag;
    private final String kaction;
    private final int key = 912379;
    private final String khms;
    private final String kkey;
    private final String ksers;
    private final LocationManager lm;
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
    private final Map<Integer, Preamble> ors = new HashMap<Integer, Preamble>();
    private final Map<Integer, TailTag> recs = new HashMap<Integer, TailTag>();
    private final ConcurrentMap<Integer, Intent> sticks = new ConcurrentHashMap<Integer, Intent>();
    private volatile int count = 0;
    private volatile int priority = 100;
    private volatile boolean registered = false;

    public Rifle(Context context) {
        if (context == null)
            throw new NullPointerException("Context must not be null.");
        this.context = context.getApplicationContext();
        cliper = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packag = context.getPackageName();
        iTag = packag + ".pullbullet.shoot";
        iSerial = packag + ".pullbullet.serial";
        broken = packag + ".pullbullet.broken";
        kkey = packag + ".pullbullet.service.key";
        ksers = packag + ".pullbullet.service.sers";
        khms = packag + ".pullbullet.service.hms";
        kaction = packag + ".pullbullet.service.action";
        areg = packag + ".pullbullet.service.reg";
        aunreg = packag + ".pullbullet.service.unreg";
        Primer b = new Primer();
        b.add(new Check("level", "100", Check.LESS_THAN));
        override(-1, b, -4);
        override(-2, -3);
        override(-3, -2);
        override(-26, -27);
        override(-27, -26);
        override(-27, -50);
        override(-28, -30);
        override(-30, -28);
        override(-32, -33);
        override(-33, -32);
        override(-41, -42);
        override(-42, -41);
        override(-46, -47);
        override(-47, -46);
    }

    public static <T extends RegularService> void setServiceClass(Class<T> sClass) {
        if (sClass == null)
            throw new NullPointerException("Cannot set a null class for service.");
        Rifle.sClass = sClass;
    }

    @Override
    protected void checkNegative(int serial, TailTag tag, String gun) {
        if (serial < 0) {
            int key = tag.get(kkey).toInteger();
            if (key != this.key)
                throw new IllegalArgumentException("A " + gun
                        + " cannot shoot negative bullets. Be positive :)");
            else
                tag.remove(kkey);
        }
    }

    private void com(int[] sers, int[] hms, boolean reg) {
        if (sers.length > 0) {
            try {
                Intent in = new Intent(context, sClass);
                in.putExtra(ksers, sers);
                in.putExtra(khms, hms);
                in.putExtra(kkey, this.key);
                in.putExtra(kaction, reg ? areg : aunreg);
                context.startService(in);
            } catch (Exception e) {
                throw new IllegalStateException(
                        "RegularService is either not declared in the manifest or is disabled.");
            }
        }
    }

    private TailTag decode(Bundle bundle) {
        if (bundle == null)
            return new TailTag();
        TailTag tag = new TailTag();
        for (String key : bundle.keySet())
            tag.put(key, bundle.getString(key));
        return tag;
    }

    private Bundle encode(TailTag tag) {
        if (tag == null)
            return new Bundle();
        Bundle bundle = new Bundle();
        for (Tag t : tag) {
            bundle.putString(t.getKey(), t.toString());
        }
        return bundle;
    }

    private void fire() {
        if (isDispatching.get()) {
            return;
        }
        isDispatching.set(true);
        try {
            while (true) {
                Intent intent = intents.get().poll();
                if (intent == null)
                    break;
                fireIntent(intent);
            }
        } finally {
            isDispatching.set(false);
        }
    }

    private void fireIntent(Intent intent) {
        context.sendOrderedBroadcast(intent, null);
    }

    public synchronized int getFireCount() {
        return count;
    }

    @Override
    protected TailTag getSticky(int serial) {
        try {
            if (serial >= 0) {
                Intent in = context.registerReceiver(null, new IntentFilter(iTag
                        + serial));
                if (in != null) {
                    return decode(in.getExtras());
                }
            } else if (serial == -63) {
                return new TailTag().put("clip", Utils.getClip(cliper));
            } else if (serial == -1) {
                return Utils.putBattery(new TailTag(), context);
            } else if (serial == -64) {
                RunningTaskInfo inf = am.getRunningTasks(1).get(0);
                TailTag tag = new TailTag();
                String pckg = inf.topActivity.getPackageName();
                tag.put("package", pckg);
                tag.put("name", Utils.getAppName(pckg, context));
                tag.put("activity", inf.topActivity.getShortClassName());
                tag.put("activityClass", inf.topActivity.getClassName());
                return tag;
            } else if (serial == -66) {
                Location loc = lm.getLastKnownLocation("gps");
                if (loc == null)
                    loc = lm.getLastKnownLocation("net");
                return Utils.locate(new TailTag(), loc);
            }
        } catch (Exception e) {
        }
        return recs.get(serial);
    }

    private void override(int ser, int... sers) {
        override(ser, null, sers);
    }

    private void override(int ser, Primer p, int... sers) {
        ors.put(ser, new Preamble(sers, p));
    }

    private void post(final int serial, final TailTag tailTag) {
        put(serial, tailTag);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            shoot(serial, tailTag, false);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    shoot(serial, tailTag, false);
                }
            });
        }
    }

    public Bullet pull(int serial, int horsePower, Magnet magnet) {
        return pull(serial, null, magnet);
    }

    protected TailTag pull(int serial, int power, Magnet magnet, Primer primer) {
        if (magnet != null) {
            if (serial < 0)
                com(new int[]{serial}, new int[]{1}, true);
            rePull(power);
            pul(serial, power, magnet, primer);
            regBroadcast(power);
        }
        return getSticky(serial);
    }

    public synchronized Bullet pull(int serial, int horsePower, Primer primer, Magnet magnet) {
        return new Bullet(serial, pull(serial, horsePower, magnet, primer));
    }

    public Bullet pull(int serial, Magnet magnet) {
        return pull(serial, 100, magnet);
    }

    public Bullet pull(int serial, Primer primer, Magnet magnet) {
        return pull(serial, 100, primer, magnet);
    }

    private void put(int ser, TailTag tag) {
        if (ser >= 0)
            return;
        Preamble pr = ors.get(ser);
        if (pr == null)
            recs.put(ser, tag);
        else {
            int[] ovrs = pr.sers;
            Primer p = pr.primer;
            if (apply(p, tag)) {
                for (int s : ovrs)
                    recs.remove(s);
                recs.put(ser, tag);
            }
        }
    }

    private void regBroadcast(int power) {
        if (!registered) {
            try {
                IntentFilter filter = new IntentFilter(iTag);
                filter.setPriority(power);
                context.registerReceiver(receiver, filter);
                Log.e("rifle", "power " + power);
                registered = true;
            } catch (Exception e) {
            }
        }
    }

    public synchronized void release(int serial, Magnet magnet) {
        if (serial < 0)
            com(new int[]{serial}, new int[]{1}, false);
        releas(serial, magnet);
        Intent in = sticks.remove(serial);
        if (in != null)
            context.removeStickyBroadcast(in);
    }

    public synchronized void releaseAll() {
        int[] sers = new int[size()];
        int[] hms = new int[size()];
        int i = 0;
        for (Integer ser : serials()) {
            sers[i] = ser;
            hms[i] = size(ser);
            i++;
        }
        com(sers, hms, false);
        clear();
        try {
            if (registered) {
                context.unregisterReceiver(receiver);
                registered = false;
            }
            for (Intent in : sticks.values()) {
                context.removeStickyBroadcast(in);
            }
        } catch (Exception e) {
        }
        sticks.clear();
    }

    public synchronized void releaseAllFor(int serial) {
        Intent st = sticks.get(serial);
        if (st != null) {
            context.removeStickyBroadcast(st);
        }
        int size = 0;
        if (handlers.containsKey(serial)) {
            size = handlers.get(serial).size();
            handlers.get(serial).clear();
        }
        if (serial < 0)
            com(new int[]{serial}, new int[]{size}, false);
    }

    private void rePull(int power) {
        if (priority < power)
            if (registered) {
                Log.e("rifle", "rereg");
                try {
                    IntentFilter filter = new IntentFilter(iTag);
                    filter.setPriority(power);
                    context.unregisterReceiver(receiver);
                    context.registerReceiver(receiver, filter);
                    priority = power;
                } catch (Exception e) {
                }
                Log.e("rifle", "rereg success");
            }
    }

    public void shoot(Bullet bullet) {
        checkNull(bullet, "Bullet to be shot cannot be null");
        shoot(bullet.getSerial(), bullet.getTailTag());
    }

    public synchronized void shoot(int serial, TailTag tailTag) {
        checkNegative(serial, tailTag, "Rifle");
        count++;
        Intent intent = new Intent(iTag);
        intent.putExtra(iSerial, serial);
        intent.putExtras(encode(tailTag));
        intents.get().offer(intent);
        fire();
    }

    public void shootDelayed(Bullet bullet, long timeout) {
        checkNull(bullet, "Bullet to be shot cannot be null");
        shootDelayed(bullet.getSerial(), bullet.getTailTag(), timeout);
    }

    public synchronized void shootDelayed(int serial, TailTag tailTag, long timeout) {
        shootDelayed(serial, timeout, tailTag, ls);
    }

    @Override
    protected void shootInfinit(int serial, TailTag tailTag) {
        checkNull(tailTag,
                "A bullet di infinty cannot be shot with a null tailTag.");
        count++;
        Bundle bundle = encode(tailTag);
        Intent intent = new Intent(iTag + serial);
        intent.putExtras(bundle);
        context.sendStickyBroadcast(intent);
        sticks.put(serial, intent);
        shoot(serial, tailTag);
    }

    /* @permission android.permission.BROADCAST_STICKY */
    public synchronized void shootInfinity(Bullet bullet) {
        checkNull(bullet, "Bullet to be shot cannot be null");
        shootInfinit(bullet.getSerial(), bullet.getTailTag());
    }

    /* @permission android.permission.BROADCAST_STICKY */
    public void shootInfinity(int serial, TailTag tailTag) {
        shootInfinit(serial, tailTag);
    }

    private class Preamble {
        public final Primer primer;
        public final int[] sers;

        public Preamble(int[] sers, Primer primer) {
            this.sers = sers;
            this.primer = primer;
        }
    }

}
