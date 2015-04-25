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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.micronic.pullbullet.Bullet;
import com.micronic.pullbullet.Check;
import com.micronic.pullbullet.Gun;
import com.micronic.pullbullet.Magnet;
import com.micronic.pullbullet.Primer;
import com.micronic.pullbullet.Rifle;
import com.micronic.pullbullet.Tag;
import com.micronic.pullbullet.TailTag;

public class MainActivity extends Activity implements Magnet, View.OnClickListener {

    private TextView mTextView;
    private Rifle rifle;
    private ScrollView sc;
    private Vibrator vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView1);
        sc = (ScrollView) findViewById(R.id.scroller);
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Button gunButton = (Button) findViewById(R.id.buttonGun);
        Button rifleButton = (Button) findViewById(R.id.buttonRifle);
        gunButton.setOnClickListener(this);
        rifleButton.setOnClickListener(this);
        startService(new Intent(this, MyService.class));
        rifle = new Rifle(this);
        rifle.pull(Bullet.BATTERY, this);//-1
        rifle.pull(-2, this);
        rifle.pull(-3, this);
        rifle.pull(-4, this);
        rifle.pull(-5, this);
        rifle.pull(-6, this);
        rifle.pull(-7, this);
        rifle.pull(-8, this);
        rifle.pull(-9, this);
        rifle.pull(-10, this);
        rifle.pull(-11, this);
        rifle.pull(-12, this);
        rifle.pull(-13, this);
        rifle.pull(-14, this);
        rifle.pull(-15, this);
        rifle.pull(-16, this);
        rifle.pull(-17, this);
        rifle.pull(-18, this);
        rifle.pull(-19, this);
        rifle.pull(-20, this);
        rifle.pull(-21, this);
        rifle.pull(-22, this);
        rifle.pull(-23, this);
        rifle.pull(-24, this);
        rifle.pull(-25, this);
        rifle.pull(-26, this);
        rifle.pull(-27, this);
        rifle.pull(-28, this);
        rifle.pull(-29, this);
        rifle.pull(-30, this);
        rifle.pull(-31, this);
        rifle.pull(-32, this);
        rifle.pull(-33, this);
        rifle.pull(-34, this);
        rifle.pull(-35, this);
        rifle.pull(-36, this);
        rifle.pull(-37, this);
        rifle.pull(-38, this);
        rifle.pull(-39, this);
        rifle.pull(-40, this);
        rifle.pull(-41, this);
        rifle.pull(-42, this);
        rifle.pull(-43, this);
        rifle.pull(-44, this);
        rifle.pull(-45, this);
        rifle.pull(-46, this);
        rifle.pull(-47, this);
        rifle.pull(-48, this);
        rifle.pull(-49, this);
        rifle.pull(-50, this);
        rifle.pull(-51, this);
        rifle.pull(-52, this);
        rifle.pull(-53, this);
        rifle.pull(-54, this);
        rifle.pull(-55, this);
        rifle.pull(-56, this);
        rifle.pull(-57, this);
        rifle.pull(-58, this);
        rifle.pull(-59, this);
        rifle.pull(-60, this);
        rifle.pull(-61, this);
        rifle.pull(-62, this);
        rifle.pull(-63, this);
        Primer primer = new Primer();
        primer.add(new Check("name", "settings", Check.EQUALS_IGNORE_CASE));
        rifle.pull(-64, primer, this);
        rifle.pull(-65, this);
        rifle.pull(-66, this);
        Gun.pull(1, this);
        rifle.pull(1, this);//rifle and gun are independant of each other.
    }

    private void append(String text) {
        mTextView.append(text + "\n");
        sc.post(new Runnable() {

            @Override
            public void run() {
                sc.smoothScrollTo(0, mTextView.getBottom());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onStuck(Bullet bullet) {
        int serial = bullet.getSerial();
        if ((-serial >= 54 && -serial <= 63) || serial == -4 || serial == -64)
            vb.vibrate(125);
        append("Bullet #" + serial);
        for (Tag tag : bullet.getTailTag())
            append(tag.getKey() + ":" + tag.toString());
        append("\n");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonGun) {
            Gun.shoot(2, new TailTag().put("mykey", "Hi from activity by gun!"));
        } else {
            rifle.shoot(2, new TailTag().put("mykey", "Hi from activity by rifle!"));
        }
    }

    @Override
    protected void onDestroy() {
        //Always call this method to free resources and memory.
        rifle.clear();
        super.onDestroy();
    }
}
