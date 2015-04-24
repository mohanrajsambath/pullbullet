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
