package com.micronic.pullbullet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.List;

public abstract class Handle {

    public final String[] acts;
    public final int[] sers;

    public Handle(int[] sers, String[] acts) {
        this.sers = sers;
        this.acts = acts;
    }

    public String getAct(int ser) {
        int i = 0;
        for (int s : sers) {
            if (s == ser)
                return acts[i];
            i++;
        }
        return null;
    }

    public int getSerial(String action) {
        int i = 0;
        for (String act : acts) {
            if (act.equals(action))
                return sers[i];
            i++;
        }
        return 0;
    }

    public Bullet handle(Intent in, Context con, TailTag reg) {
        TailTag tag = new TailTag();
        int ser = getSerial(in.getAction());
        return new Bullet(ser, tag);
    }

    public Bullet process(Context con, Bullet b) {
        b.getTailTag().put(con.getPackageName() + ".pullbullet.service.key",
                912379);
        return b;
    }

    public void reg(int ser, IntentFilter f, Context con,
                    BroadcastReceiver rec, List<String> acts, TailTag reg, String var) {
        int cnt = reg.get("cnt" + ser).toInteger();
        int count = reg.get(var + "count").toInteger();
        if (cnt == 0) {
            count++;
            reg.put(var + "count", count);
            regd(ser, f, con, rec, acts, reg, var + "regd");
        }
        reg.put("cnt" + ser, cnt + 1);
    }

    public void regd(int ser, IntentFilter f, Context con,
                     BroadcastReceiver rec, List<String> actss, TailTag reg, String var) {
        if (acts.length == 0) {
            Log.e("rifle", "no acts");
            return;
        }
        String act = getAct(ser);
        if (act != null && !act.isEmpty()) {
            if (!actss.contains(act))
                actss.add(act);
            for (String actt : actss) {
                f.addAction(actt);
            }
            try {
                if (reg.get(var).toBoolean())
                    con.unregisterReceiver(rec);
                else
                    reg.put(var, true);
                con.registerReceiver(rec, f);
            } catch (Exception e) {
            }
        }
    }

    public void unreg(int ser, IntentFilter f, Context con,
                      BroadcastReceiver rec, List<String> acts, TailTag reg, String var) {
        int cnt = reg.get("cnt" + ser).toInteger();
        cnt--;
        reg.put("cnt" + ser, cnt);
        int count = reg.get(var + "count").toInteger();
        if (cnt == 0) {
            count--;
            reg.put(var + "count", count);
            String atorm = unregd(ser, reg);
            if (atorm != null && !atorm.isEmpty()) {
                acts.remove(atorm);
                for (String act : acts)
                    f.addAction(act);
                try {
                    if (acts.isEmpty())
                        con.unregisterReceiver(rec);
                    else {
                        if (reg.get(var).toBoolean())
                            con.unregisterReceiver(rec);
                        else
                            reg.put(var, true);
                        con.registerReceiver(rec, f);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public String unregd(int ser, TailTag reg) {
        return getAct(ser);
    }
}
