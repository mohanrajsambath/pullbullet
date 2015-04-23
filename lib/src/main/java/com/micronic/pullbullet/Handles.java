package com.micronic.pullbullet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"UseSparseArrays", "InlinedApi"})
@SuppressWarnings("deprecation")
public class Handles {
    public final static List<Handle> handles = new ArrayList<Handle>();

    static {
        handles.add(new Handle(new int[]{-1, -2, -3, -4}, new String[]{
                Intent.ACTION_BATTERY_CHANGED, Intent.ACTION_BATTERY_LOW,
                Intent.ACTION_BATTERY_OKAY}) {
            @Override
            public String getAct(int ser) {
                if (ser == -4)
                    return Intent.ACTION_BATTERY_CHANGED;
                return super.getAct(ser);
            }

            @Override
            public String unregd(int ser, TailTag reg) {
                int bc = reg.get("cnt-1").toInteger();
                int fc = reg.get("cnt-4").toInteger();
                if (ser == -1 || ser == -4) {
                    if (bc == 0 && fc == 0)
                        return Intent.ACTION_BATTERY_CHANGED;
                } else
                    return getAct(ser);
                return null;
            }

            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                int ser = getSerial(in.getAction());
                return new Bullet(ser, Utils.putBattery(new TailTag(), con));
            }
        });
        handles.add(new Handle(new int[]{-5},
                new String[]{"android.provider.Telephony.SMS_RECEIVED"}) {
            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                String adrs = "";
                String msgg = "";
                Object[] pdus = (Object[]) in.getExtras().get("pdus");
                for (Object pdu : pdus) {
                    SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdu);
                    adrs = msg.getOriginatingAddress();
                    msgg += msg.getMessageBody();
                }
                tag.put("number", adrs);
                tag.put("name", Utils.getName(adrs, con));
                tag.put("messsage", msgg);
                return new Bullet(-5, tag);
            }
        });
        handles.add(new Handle(new int[]{-6},
                new String[]{"android.net.conn.CONNECTIVITY_CHANGE"}) {
            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                NetworkInfo info = (NetworkInfo) in.getExtras().get(
                        "networkInfo");
                if (info != null) {
                    tag.put("state", info.getState() == null ? null : info
                            .getState().toString());
                    tag.put("detailedState",
                            info.getDetailedState() == null ? null : info
                                    .getDetailedState().toString());
                    tag.put("connected", info.isConnected());
                    tag.put("available", info.isAvailable());
                    tag.put("connectedOrConnecting",
                            info.isConnectedOrConnecting());
                    tag.put("failover", info.isFailover());
                    tag.put("roaming", info.isRoaming());
                    tag.put("reason", info.getReason());
                    tag.put("subtype", info.getSubtype());
                    tag.put("subtypeName", info.getSubtypeName());
                    tag.put("networkName", info.getTypeName());
                    tag.put("description", info.describeContents());
                }
                return new Bullet(-6, tag);
            }
        });
        handles.add(new Handle(new int[]{-7, -8, -9, -10, -11}, new String[]{
                "android.intent.action.PHONE_STATE",
                Intent.ACTION_NEW_OUTGOING_CALL}) {
            @Override
            public String getAct(int ser) {
                if (-ser >= 7 && -ser <= 10)
                    return "android.intent.action.PHONE_STATE";
                else
                    return Intent.ACTION_NEW_OUTGOING_CALL;
            }

            @Override
            public String unregd(int ser, TailTag reg) {
                if (-ser >= 6 && -ser <= 9) {
                    int sevn = reg.get("cnt-7").toInteger();
                    int eigt = reg.get("cnt-8").toInteger();
                    int nin = reg.get("cnt-9").toInteger();
                    int ten = reg.get("cnt-10").toInteger();
                    if (ten == 0 && sevn == 0 && eigt == 0 && nin == 0)
                        return "android.intent.action.PHONE_STATE";
                } else
                    return Intent.ACTION_NEW_OUTGOING_CALL;
                return null;
            }

            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                int subs = in.getIntExtra("subscription", 0);
                if (subs == 1)
                    return null;
                String no = in.getStringExtra("incoming_number");
                String name = Utils.getName(no, con);
                String state = in.getStringExtra("state");
                boolean ringing = reg.get("ringing").toBoolean();
                boolean incall = reg.get("incall").toBoolean();
                double inCallTime = reg.get("inCallTime").toDouble();
                int serial = -7;
                if (in.getAction().equals("android.intent.action.PHONE_STATE")) {
                    if (state.equals("RINGING")) {
                        ringing = true;
                        Log.e("rifle", state);
                        serial = -7;// incoming call
                    } else if (state.equals("IDLE")) {
                        if (ringing) {
                            ringing = false;
                            if (!incall) {
                                incall = false;
                                serial = -8;// call missed
                            } else {
                                incall = false;
                                inCallTime = System.currentTimeMillis()
                                        - inCallTime;
                                tag.put("inCallTime", inCallTime);
                                serial = -9;// call ended
                            }
                        }
                    } else if (state.equals("OFFHOOK")) {
                        if (ringing) {
                            incall = true;
                            inCallTime = System.currentTimeMillis();
                            serial = -10;// call answered
                        }
                    }
                } else {
                    ringing = true;
                    serial = -11;// call out
                }
                reg.put("ringing", ringing);
                reg.put("incall", incall);
                reg.put("inCallTime", inCallTime);
                tag.put("number", no);
                tag.put("name", name);
                return new Bullet(serial, tag);
            }
        });

        handles.add(new Handle(new int[]{-12, -13, -14, -15, -16, -17, -18},
                new String[]{Intent.ACTION_PACKAGE_CHANGED,
                        Intent.ACTION_PACKAGE_ADDED,
                        Intent.ACTION_PACKAGE_FULLY_REMOVED,
                        Intent.ACTION_PACKAGE_REMOVED,
                        Intent.ACTION_PACKAGE_REPLACED,
                        Intent.ACTION_PACKAGE_DATA_CLEARED,
                        Intent.ACTION_PACKAGE_RESTARTED}) {
            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                int ser = getSerial(in.getAction());
                tag.put("uid", in.getIntExtra(Intent.EXTRA_UID, 0));
                if (in.hasExtra(Intent.EXTRA_REPLACING))
                    tag.put("replacing",
                            in.getBooleanExtra(Intent.EXTRA_REPLACING, false));
                if (in.hasExtra("android.intent.extra.user_handle"))
                    tag.put("userHandle", in.getIntExtra(
                            "android.intent.extra.user_handle", 0));
                if (in.hasExtra("android.intent.extra.REMOVED_FOR_ALL_USERS"))
                    tag.put("removedForAllUsers",
                            in.getBooleanExtra(
                                    "android.intent.extra.REMOVED_FOR_ALL_USERS",
                                    false));
                if (in.hasExtra(Intent.EXTRA_DATA_REMOVED))
                    tag.put("dataRemoved", in.getBooleanExtra(
                            Intent.EXTRA_DATA_REMOVED, false));
                if (ser == -11) {
                    String[] names = in
                            .getStringArrayExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
                    if (names == null)
                        tag.put("changedComponentsSize", 0);
                    else {
                        tag.put("changedComponentsSize", names.length);
                        int i = 0;
                        for (String name : names) {
                            tag.put("changedComponent" + i, name);
                            i++;
                        }
                    }
                    tag.put("dontKillApp", in.getBooleanExtra(
                            Intent.EXTRA_DONT_KILL_APP, false));
                }
                Uri data = in.getData();
                if (data != null) {
                    String[] a = data.toString().split(":");
                    String pckg = a[a.length - 1];
                    tag.put("package", pckg);
                    tag.put("name", Utils.getAppName(pckg, con));
                }
                return new Bullet(ser, tag);
            }
        });

        handles.add(new Handle(new int[]{-19},
                new String[]{Intent.ACTION_TIMEZONE_CHANGED}) {
            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                tag.put("timeZone", in.getStringExtra("time-zone"));
                return new Bullet(-19, tag);
            }
        });

        handles.add(new Handle(new int[]{-20, -21}, new String[]{
                Intent.ACTION_MEDIA_BUTTON, Intent.ACTION_CAMERA_BUTTON}) {
            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                KeyEvent ev = (KeyEvent) in.getExtras().get(
                        Intent.EXTRA_KEY_EVENT);
                if (ev != null) {
                    tag.put("keyCode", ev.getKeyCode());
                }
                int ser = getSerial(in.getAction());
                return new Bullet(ser, tag);
            }
        });

        handles.add(new Handle(new int[]{-22, -23},
                new String[]{Intent.ACTION_HEADSET_PLUG}) {
            @Override
            public String getAct(int ser) {
                return Intent.ACTION_HEADSET_PLUG;
            }

            @Override
            public String unregd(int ser, TailTag reg) {
                int plgd = reg.get("cnt-22").toInteger();
                int unplgd = reg.get("cnt-23").toInteger();
                if (plgd == 0 && unplgd == 0)
                    return Intent.ACTION_HEADSET_PLUG;
                else return null;
            }

            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                int state = in.getIntExtra("state", 0);
                tag.put("mic", in.getIntExtra("microphone", 0));
                tag.put("name", in.getStringExtra("name"));
                if (state == 1)
                    return new Bullet(-22, tag);
                return new Bullet(-23, tag);
            }
        });

        handles.add(new Handle(new int[]{-24},
                new String[]{Intent.ACTION_DOCK_EVENT}) {
            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                int state = in.getIntExtra(Intent.EXTRA_DOCK_STATE, 0);
                String desc = "";
                tag.put("state", state);
                switch (state) {
                    case 2:
                        desc = "car";
                        break;
                    case 1:
                        desc = "desk";
                        break;
                    case 4:
                        desc = "he_desk";
                        break;
                    case 3:
                        desc = "le_desk";
                        break;
                    case 0:
                        desc = "undocked";
                        break;
                }
                tag.put("name", desc);
                return new Bullet(-24, tag);
            }
        });

        handles.add(new Handle(new int[]{-25},
                new String[]{"android.intent.action.SIM_STATE_CHANGED"}) {
            @Override
            public Bullet handle(Intent in, Context con, TailTag reg) {
                TailTag tag = new TailTag();
                tag.put("slot", in.getIntExtra("slot", 0));
                tag.put("phone", in.getIntExtra("phone", 0));
                tag.put("subscription", in.getIntExtra("subscription", 0));
                tag.put("phoneName", in.getStringExtra("phoneName"));
                tag.put("ss", in.getStringExtra("ss"));
                tag.put("reason", in.getStringExtra("reason"));
                return new Bullet(-25, tag);
            }
        });

        handles.add(new Handle(new int[]{-26, -27, -28, -29,
                -30, -31, -32, -33, -34, -35, -36, -37, -38, -39, -40, -41,
                -42, -43, -44, -45, -46, -47, -48, -49, -50}, new String[]{
                Intent.ACTION_SCREEN_ON, Intent.ACTION_SCREEN_OFF,
                Intent.ACTION_DEVICE_STORAGE_LOW,
                Intent.ACTION_AIRPLANE_MODE_CHANGED,
                Intent.ACTION_DEVICE_STORAGE_OK, Intent.ACTION_DATE_CHANGED,//30
                Intent.ACTION_DREAMING_STARTED, Intent.ACTION_DREAMING_STOPPED,
                Intent.ACTION_INPUT_METHOD_CHANGED,
                Intent.ACTION_LOCALE_CHANGED, Intent.ACTION_MEDIA_BAD_REMOVAL,
                Intent.ACTION_MEDIA_CHECKING, Intent.ACTION_MEDIA_EJECT,
                Intent.ACTION_MEDIA_MOUNTED, Intent.ACTION_MEDIA_REMOVED,//39
                Intent.ACTION_MEDIA_SCANNER_FINISHED,
                Intent.ACTION_MEDIA_SCANNER_STARTED,
                Intent.ACTION_MEDIA_SHARED, Intent.ACTION_MEDIA_UNMOUNTABLE,//43
                Intent.ACTION_MEDIA_UNMOUNTED, Intent.ACTION_POWER_CONNECTED,
                Intent.ACTION_POWER_DISCONNECTED, Intent.ACTION_TIME_CHANGED,
                Intent.ACTION_TIME_TICK, Intent.ACTION_USER_PRESENT,}) {
        });
    }

}
