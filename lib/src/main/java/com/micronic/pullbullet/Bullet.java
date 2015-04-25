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

public class Bullet {

    public final static int GUN_STRAIGHT_HIT = -1;
    public final static int BATTERY = -1;
    public final static int BATTERY_LOW = -2;
    public final static int BATTERY_OKAY = -3;
    public final static int BATTERY_FULL = -4;
    /**
     * @permission android.permission.RECEIVE_SMS
     * @permission android.permission.READ_CONTACTS to get name
     */
    public final static int SMS_IN = -5;
    public final static int CONNECTIVITY = -6;
    /**
     * @permission android.permission.READ_CONTACTS to get name
     * @permission android.permission.READ_PHONE_STATE
     */
    public final static int CALL_IN = -7;
    /**
     * @permission android.permission.READ_CONTACTS to get name
     * @permission android.permission.READ_PHONE_STATE
     */
    public final static int CALL_MISSED = -8;
    /**
     * @permission android.permission.READ_CONTACTS to get name
     * @permission android.permission.READ_PHONE_STATE
     */
    public final static int CALL_ENDED = -9;
    /**
     * @permission android.permission.READ_CONTACTS to get name
     * @permission android.permission.READ_PHONE_STATE
     */
    public final static int CALL_ANSWERED = -10;
    /** @permission android.permission.PROCESS_OUTGOING_CALLS
     * @permission android.permission.READ_CONTACTS to get name
     */
    public final static int CALL_OUT = -11;
    public final static int PACKAGE_CHANGED = -12;
    public final static int PACKAGE_ADDED = -13;
    /**
     * @API 14
     */
    public final static int PACKAGE_FULLY_REMOVED = -14;
    public final static int PACKAGE_REMOVED = -15;
    public final static int PACKAGE_REPLACED = -16;
    public final static int PACKAGE_DATA_CLEARED = -17;
    public final static int PACKAGE_RESTARTED = -18;
    public final static int TIMEZONE_CHANGED = -19;
    public final static int MEDIA_BUTTON_CLICKED = -20;
    public final static int CAMERA_BUTTON_CLICKED = -21;
    public final static int HEADSET_PLUGGED = -22;
    public final static int HEADSET_UNPLUGGED = -23;
    public final static int DOCKED = -24;
    public final static int SIM_STATE_CHANGED = -25;
    public final static int SCREEN_ON = -26;
    public final static int SCREEN_OFF = -27;
    public final static int STORAGE_LOW = -28;
    public final static int AIRPLANE_MODE_CHANGED = -29;
    public final static int STORAGE_OK = -30;
    public final static int DATE_CHANGED = -31;
    /**
     * @API 17
     */
    public final static int DREAMING_STARTED = -32;
    /**
     * @API 17
     */
    public final static int DREAMING_STOPPED = -33;
    public final static int INPUT_METHOD_CHANGED = -34;
    public final static int LOCALE_CHANGED = -35;
    public final static int MEDIA_BAD_REMOVAL = -36;
    public final static int MEDIA_IN_CHECK = -37;
    public final static int MEDIA_IJECTED = -38;
    public final static int MEDIA_MOUNTED = -39;
    public final static int MEDIA_REMOVED = -40;
    public final static int MEDIA_SCANNER_FINISHED = -41;
    public final static int MEDIA_SCANNER_STARTED = -42;
    public final static int MEDIA_SHARED = -43;
    public final static int MEDIA_UNMOUNTABLE = -44;
    public final static int MEDIA_UNMOUNTED = -45;
    public final static int POWER_CONNECTED = -46;
    public final static int POWER_DISCONNECTED = -47;
    public final static int TIME_CHANGED = -48;
    public final static int TIME_TICK = -49;
    public final static int SCREEN_UNLOCKED = -50;
    /** @permission android.permission.RECEIVE_BOOT_COMPLETED
     * @RegularReceiver
     */
    public final static int BOOT_COMPLETED = -51;
    /**
     * @RegularReceiver
     */
    public final static int SHUTDOWN = -52;
    /**
     * @RegularReceiver
     */
    public final static int MY_APP_REPLACED = -53;
    public final static int DEVICE_FACE_UP = -54;
    public final static int DEVICE_FACE_DOWN = -55;
    public final static int DEVICE_STANDING_UP = -56;
    public final static int DEVICE_UPSIDE_DOWN = -57;
    public final static int DEVICE_ON_LEFT_SIDE = -58;
    public final static int DEVICE_ON_RIGHT_SIDE = -59;
    public final static int DEVICE_BEING_SHAKEN = -60;
    public final static int PROXIMITY_NEAR = -61;
    public final static int PROXIMITY_AWAY = -62;
    /**
     * @API 11
     */
    public final static int TEXT_COPIED = -63;
    /**
     * @API 14
     * @AccessService
     * @permission android.permission.BIND_ACCESSIBILITY_SERVICE
     * @permission android.permission.GET_TOP_ACTIVITY_INFO
     */
    public final static int APP_OPENED = -64;
    /**
     * @API 14
     * @AccessService
     * @permission android.permission.BIND_ACCESSIBILITY_SERVICE
     */
    public final static int NOTIFICATION_RECEIVED = -65;
    /** @permission android.permission.ACCESS_COARSE_LOCATION
     * @permission android.permission.ACCESS_FINE_LOCATION
     */
    public final static int OBTAIN_LOCATION = -66;

    private final int serial;
    private final TailTag tailTag;

    public Bullet(int serial, TailTag tailTag) {
        this.serial = serial;
        this.tailTag = tailTag;
    }

    public Bullet clone() {
        return new Bullet(serial, tailTag.clone());
    }

    public int getSerial() {
        return serial;
    }

    public boolean hasTailTag() {
        return tailTag != null;
    }

    public TailTag getTailTag() {
        return tailTag;
    }

    public void shatter() {
        if (hasTailTag())
            tailTag.put("pullbullet.destruct", true);
    }
}
