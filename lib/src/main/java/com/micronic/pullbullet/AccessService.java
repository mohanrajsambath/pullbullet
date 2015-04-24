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

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.view.accessibility.AccessibilityEvent;

import java.util.Date;


public class AccessService extends AccessibilityService {

    private String packag = "";
    private Rifle rifle;

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.notificationTimeout = 100;
        setServiceInfo(info);
        rifle = new Rifle(getApplicationContext());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent arg0) {
        String packag = String.valueOf(arg0.getPackageName());
        String name = Utils.getAppName(packag, getApplicationContext());
        TailTag tag = new TailTag().put("package", packag).put("name", name).put(getPackageName() + ".pullbullet.service.key", 912379);
        if (arg0.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (!packag.equals(this.packag)) {
                this.packag = packag;
                rifle.shoot(-64, tag);
            }
        } else {
            try {
                Notification nf = (Notification) arg0.getParcelableData();
                tag.put("number", nf.number);
                tag.put("timeLong", nf.when);
                tag.put("time", new Date(nf.when).toString());
                tag.put("title", String.valueOf(nf.tickerText));
            } catch (Exception e) {
            }
            rifle.shoot(-65, tag);
        }
    }

    @Override
    public void onInterrupt() {

    }

}
