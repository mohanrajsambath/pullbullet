package com.micronic.pullbullet;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.Date;

public class Utils {
    public static String getName(String number, Context con) {
        if (number == null)
            return null;
        try {
            Cursor cursor = con.getContentResolver().query(
                    Uri.withAppendedPath(
                            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(number)),
                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null, null, null);
            if (cursor.moveToFirst()) {
                final String name = cursor.getString(0);
                cursor.close();
                return name;
            }
            cursor.close();
        } catch (Exception e) {
        }
        return null;
    }

    public static String getAppName(String packag, Context con) {
        try {
            PackageManager pm = con.getApplicationContext().getPackageManager();
            ApplicationInfo ai;
            ai = pm.getApplicationInfo(packag, 0);
            return String.valueOf(pm.getApplicationLabel(ai));
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("NewApi")
    public static String getClip(ClipboardManager cliper) {
        try {
            ClipData data = cliper.getPrimaryClip();
            Item item = data.getItemAt(0);
            return String.valueOf(item.getText());
        } catch (Exception e) {
        }
        return null;
    }

    public static TailTag putBattery(TailTag tag, Context con) {
        try {
            Intent in = con.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            tag.put("health", in.getIntExtra("health", 0));
            tag.put("temperature", in.getIntExtra("temperature", 0));
            tag.put("voltage", in.getIntExtra("voltage", 0));
            tag.put("status", in.getIntExtra("status", 0));
            tag.put("scale", in.getIntExtra("scale", 0));
            tag.put("plugged", in.getIntExtra("plugged", 0));
            tag.put("level", in.getIntExtra("level", 0));
            tag.put("iconSmall", in.getIntExtra("icon-small", 0));
            tag.put("invalidCharger", in.getIntExtra("invalid_charger", 0));
            tag.put("technology", in.getStringExtra("technology"));
            tag.put("present", in.getBooleanExtra("present", false));
        } catch (Exception e) {
        }
        return tag;
    }

    public static TailTag locate(TailTag tag, Location loc) {
        if (loc != null) {
            tag.put("latitude", loc.getLatitude());
            tag.put("longitude", loc.getLongitude());
            tag.put("provider", loc.getProvider());
            tag.put("altitude", loc.getAltitude());
            tag.put("speed", loc.getSpeed());
            tag.put("bearing", loc.getBearing());
            tag.put("accuracy", loc.getAccuracy());
            tag.put("timeLong", loc.getTime());
            tag.put("time", new Date(loc.getTime()).toString());
        }
        return tag;
    }

}
