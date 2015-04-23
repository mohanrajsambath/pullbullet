package com.micronic.pullbullet;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class RegularService extends Service {
    private final List<String> actions = new ArrayList<String>();
    private final Map<String, Handle> acts = new HashMap<String, Handle>();
    private final int key = 912379;
    private final List<String> pactions = new ArrayList<String>();
    private final TailTag registry = new TailTag();
    private final Map<Integer, Handle> sers = new HashMap<Integer, Handle>();
    private String areg;
    private String aunreg;
    private boolean bfull = false;
    private ClipboardManager cliper;
    private String kaction;
    private String khms;
    private String kkey;
    private final Shaker shaker = new Shaker(new Shaker.Listener() {

        @Override
        public void hearShake() {
            Bullet bullet = new Bullet(-60, new TailTag().put(kkey, key));
            rifle.shoot(bullet);
        }

        @Override
        public void orChanged(int or) {
            Bullet bullet = new Bullet(-(54 + or), new TailTag().put(kkey, key));
            rifle.shoot(bullet);
        }

        @Override
        public void proxyChanged(int proxy) {
            Bullet bullet = new Bullet(-(61 + proxy), new TailTag().put(kkey,
                    key));
            rifle.shoot(bullet);
        }

    });
    private final LocationListener ls = new LocationListener() {

        @Override
        public void onLocationChanged(Location arg0) {
            rifle.shoot(-66, Utils.locate(new TailTag().put(kkey, key), arg0));
        }

        @Override
        public void onProviderDisabled(String arg0) {

        }

        @Override
        public void onProviderEnabled(String arg0) {

        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        }
    };
    private final OnPrimaryClipChangedListener cls = new OnPrimaryClipChangedListener() {

        @Override
        public void onPrimaryClipChanged() {
            TailTag tag = new TailTag().put(kkey, key);
            tag.put("clip", Utils.getClip(cliper));
            rifle.shoot(-63, tag);
        }
    };
    private String ksers;
    private LocationManager lm;
    private Rifle rifle;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            received(arg1, rifle, kkey);
        }
    };
    private final BroadcastReceiver preceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            received(arg1, rifle, kkey);
        }
    };
    private SensorManager sensor;

    protected boolean canSelfDestruct() {
        return true;
    }

    private void checkSensor() {
        int ac = registry.get("acount").toInteger();
        int pc = registry.get("ppcount").toInteger();
        if (ac == 0)
            shaker.stop(sensor);
        if (pc == 0)
            shaker.stopProxy(sensor);
    }

    protected boolean isIntentClear(Intent intent) {
        int key = intent.getIntExtra(kkey, 0);
        int[] sers = intent.getIntArrayExtra(ksers);
        int[] hms = intent.getIntArrayExtra(khms);
        String action = intent.getStringExtra(kaction);
        return key != this.key || sers == null || hms == null || sers.length == 0
                || hms.length == 0 || sers.length != hms.length
                || action == null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        kkey = getPackageName() + ".pullbullet.service.key";
        ksers = getPackageName() + ".pullbullet.service.sers";
        khms = getPackageName() + ".pullbullet.service.hms";
        kaction = getPackageName() + ".pullbullet.service.action";
        areg = getPackageName() + ".pullbullet.service.reg";
        aunreg = getPackageName() + ".pullbullet.service.unreg";
        registry.clear();
        actions.clear();
        pactions.clear();
        rifle = new Rifle(getApplicationContext());
        sensor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        cliper = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        for (Handle hd : Handles.handles) {
            for (int ser : hd.sers)
                sers.put(ser, hd);
            for (String act : hd.acts)
                acts.put(act, hd);
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        registry.clear();
        shaker.stop(sensor);
        shaker.stopProxy(sensor);
        actions.clear();
        pactions.clear();
        acts.clear();
        sers.clear();
        try {
            cliper.removePrimaryClipChangedListener(cls);
            lm.removeUpdates(ls);
            unregisterReceiver(receiver);
            unregisterReceiver(preceiver);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Log.e("rifle", "service on");
            int key = intent.getIntExtra(kkey, 0);
            int[] sers = intent.getIntArrayExtra(ksers);
            int[] hms = intent.getIntArrayExtra(khms);
            String action = intent.getStringExtra(kaction);
            if (key != this.key || sers == null || hms == null
                    || sers.length == 0 || hms.length == 0
                    || sers.length != hms.length || action == null)
                return START_STICKY;
            Log.e("rifle", "Key:" + key + " Action:" + action);
            Log.e("rifle", "sers size " + sers.length);
            Log.e("rifle", "hms size " + hms.length);
            if (action.equals(areg)) {
                for (int i = 0; i < sers.length; i++) {
                    int ser = sers[i];
                    int hm = hms[i];
                    for (int j = 0; j < hm; j++)
                        register(ser);
                }
            } else if (action.equals(aunreg)) {
                for (int i = 0; i < sers.length; i++) {
                    int ser = sers[i];
                    int hm = hms[i];
                    for (int j = 0; j < hm; j++)
                        unregister(ser);
                }
            }
        }
        return START_STICKY;
    }

    private void received(Intent arg1, Rifle rifle, String kkey) {
        if (arg1 != null && rifle != null && kkey != null) {
            Log.e("rifle", "received " + arg1.getAction());
            Handle handle = acts.get(arg1.getAction());
            if (handle != null) {
                Bullet bullet = null;
                try {
                    bullet = handle.handle(arg1, getApplicationContext(), registry);
                } catch (Exception e) {
                }
                if (bullet != null) {
                    bullet.getTailTag().put(kkey, key);
                    if (bullet.getSerial() == -1) {
                        int level = bullet.getTailTag().get("level")
                                .toInteger();
                        if (level == 100 && !bfull)
                            rifle.shoot(new Bullet(-4, bullet.getTailTag()
                                    .clone()));
                        bfull = level == 100;
                    }
                    rifle.shoot(bullet);
                }
            }
        }
    }

    private void register(int serial) {
        if (serial >= 0)
            return;
        if (serial == -66) {
            Criteria c = new Criteria();
            c.setAccuracy(Criteria.ACCURACY_COARSE);
            c.setAltitudeRequired(false);
            c.setBearingRequired(false);
            c.setCostAllowed(true);
            c.setSpeedRequired(false);
            String pr = lm.getBestProvider(c, true);
            if (pr != null && !pr.isEmpty()) {
                registry.put("lcount", registry.get("lcount").toInteger() + 1);
                lm.requestLocationUpdates(pr, 5, 0, ls);
            }
        } else if (-serial >= 51 && -serial <= 53) {

        } else if (-serial >= 54 && -serial <= 60) {
            registry.put("acount", registry.get("acount").toInteger() + 1);
            shaker.start(sensor);
        } else if (-serial == 61 || -serial == 62) {
            registry.put("ppcount", registry.get("ppcount").toInteger() + 1);
            shaker.startProxy(sensor);
        } else if (serial == -63) {
            registry.put("ccount", registry.get("ccount").toInteger() + 1);
            cliper.addPrimaryClipChangedListener(cls);
        } else if (-serial >= 12 && -serial <= 18) {
            Handle handle = sers.get(serial);
            if (handle != null) {
                IntentFilter f = new IntentFilter();
                f.addDataScheme("package");
                handle.reg(serial, f, getApplicationContext(), preceiver, pactions, registry, "p");
            }
        } else {
            Handle handle = sers.get(serial);
            if (handle != null) {
                handle.reg(serial, new IntentFilter(), getApplicationContext(), receiver, actions,
                        registry, "n");
            } else
                Log.e("rifle", "no handle");
        }
    }

    private void selfDestruct() {
        int nc = registry.get("ncount").toInteger();
        int ac = registry.get("acount").toInteger();
        int pc = registry.get("pcount").toInteger();
        int cc = registry.get("ccount").toInteger();
        int lc = registry.get("lcount").toInteger();
        int ppc = registry.get("ppcount").toInteger();
        if (nc == 0 && ac == 0 && pc == 0 && ppc == 0 && cc == 0 && lc == 0 && canSelfDestruct())
            stopService(new Intent(getApplicationContext(), RegularService.class));
    }

    private void unregister(int serial) {
        if (serial >= 0)
            return;
        if (serial == -66) {
            int lc = registry.get("lcount").toInteger();
            lc--;
            registry.put("lcount", lc);
            if (lc == 0)
                lm.removeUpdates(ls);
        }
        if (-serial >= 51 && -serial <= 53) {

        } else if (-serial >= 54 && -serial <= 60) {
            registry.put("acount", registry.get("acount").toInteger() - 1);
            checkSensor();
        } else if (-serial == 61 || -serial == 62) {
            registry.put("ppcount", registry.get("ppcount").toInteger() - 1);
            checkSensor();
        } else if (serial == -63) {
            int cc = registry.get("ccount").toInteger();
            cc--;
            registry.put("ccount", cc);
            if (cc == 0)
                cliper.removePrimaryClipChangedListener(cls);
        } else if (-serial >= 12 && -serial <= 18) {
            Handle handle = sers.get(serial);
            if (handle != null) {
                IntentFilter f = new IntentFilter();
                f.addDataScheme("package");
                handle.unreg(serial, f, getApplicationContext(), preceiver, pactions, registry,
                        "p");
            }
        } else {
            Handle handle = sers.get(serial);
            if (handle != null) {
                handle.unreg(serial, new IntentFilter(), getApplicationContext(), receiver,
                        actions, registry, "n");
            }
        }
        selfDestruct();
    }

}
