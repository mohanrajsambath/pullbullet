package com.micronic.pullbullet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RegularReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        if (arg0 != null && arg1 != null) {
            String action = arg1.getAction();
            int ser = 0;
            if (action.equals(Intent.ACTION_BOOT_COMPLETED))
                ser = -51;
            else if (action.equals(Intent.ACTION_SHUTDOWN))
                ser = -52;
            else if (action.equals(Intent.ACTION_MY_PACKAGE_REPLACED))
                ser = -53;
            if (ser == 0)
                return;
            String kkey = arg0.getPackageName() + ".pullbullet.service.key";
            Rifle rifle = new Rifle(arg0);
            Log.e("rifle", "received " + arg1.getAction());
            Bullet bullet = new Bullet(ser, new TailTag().put(kkey, 912379));
            rifle.shoot(bullet);
        }
    }

}
