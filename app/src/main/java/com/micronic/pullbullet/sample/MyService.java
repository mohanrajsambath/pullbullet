package com.micronic.pullbullet.sample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.micronic.pullbullet.Bullet;
import com.micronic.pullbullet.Gun;
import com.micronic.pullbullet.Magnet;
import com.micronic.pullbullet.Rifle;
import com.micronic.pullbullet.TailTag;

public class MyService extends Service implements Magnet {
    private Rifle rifle;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rifle = new Rifle(this);
        Gun.pull(3, this);
        rifle.pull(4, this);
    }

    @Override
    public void onStuck(Bullet bullet) {
        int serial = bullet.getSerial();
        if (serial == 3)
            Gun.shoot(1, new TailTag().put("mykey", "Service by gun: Got your message, '" + bullet.getTailTag().get("mykey") + "' !"));
        else
            rifle.shoot(2, new TailTag().put("mykey", "Service by rifle: Got your message, '" + bullet.getTailTag().get("mykey") + "' !"));
    }
}
