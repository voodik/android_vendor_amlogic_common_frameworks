/*
 * Copyright (c) 2014 Amlogic, Inc. All rights reserved.
 *
 * This source code is subject to the terms and conditions defined in the
 * file 'LICENSE' which is part of this source code package.
 *
 * Description:
 *     AMLOGIC HdmiCecExtend
 */

package com.droidlogic;

import android.content.Context;
import android.content.ContentResolver;
import android.provider.Settings.Global;
import android.database.ContentObserver;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.HdmiControlManager.HotplugEventListener;
import android.hardware.hdmi.HdmiControlManager.VendorCommandListener;
import android.hardware.hdmi.HdmiPlaybackClient;
import android.hardware.hdmi.HdmiHotplugEvent;
import android.hardware.hdmi.HdmiPortInfo;
import android.hardware.hdmi.HdmiPlaybackClient.OneTouchPlayCallback;
//import android.hardware.hdmi.IHdmiControlService;
import android.util.Log;
import android.net.Uri;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
//import android.os.UserHandle;
//import android.os.ServiceManager;
import android.os.Handler;
//import com.android.internal.app.LocalePicker;
//import com.android.internal.app.LocalePicker.LocaleInfo;


import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


import java.util.Locale;
import java.util.List;
import java.io.UnsupportedEncodingException;
import com.droidlogic.app.HdmiCecManager;
import android.os.SystemClock;
import android.os.MessageQueue;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import java.util.NoSuchElementException;
import android.os.Build;
import android.os.Handler;
import android.os.HwBinder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import java.util.List;
import android.os.SystemProperties;
import java.util.Collections;
import java.util.ArrayList;
import android.text.TextUtils;
import vendor.amlogic.hardware.hdmicec.V1_0.CecEvent;
import vendor.amlogic.hardware.hdmicec.V1_0.CecLogicalAddress;
import vendor.amlogic.hardware.hdmicec.V1_0.CecMessage;
import vendor.amlogic.hardware.hdmicec.V1_0.ConnectType;
//import vendor.amlogic.hardware.hdmicec.V1_0.HdmiPortInfo;
import vendor.amlogic.hardware.hdmicec.V1_0.HdmiPortType;
import vendor.amlogic.hardware.hdmicec.V1_0.HotplugEvent;
import vendor.amlogic.hardware.hdmicec.V1_0.HotplugEvent;
import vendor.amlogic.hardware.hdmicec.V1_0.IDroidHdmiCEC;
import vendor.amlogic.hardware.hdmicec.V1_0.IDroidHdmiCecCallback;
import vendor.amlogic.hardware.hdmicec.V1_0.OptionKey;
import vendor.amlogic.hardware.hdmicec.V1_0.Result;
import vendor.amlogic.hardware.hdmicec.V1_0.SendMessageResult;
import com.droidlogic.app.SystemControlManager;

public class HdmiCecExtend implements VendorCommandListener, HotplugEventListener {
    private final String TAG = "HdmiCecExtend";
    private static final int DISABLED = 0;
    private static final int ENABLED = 1;

    static final int MESSAGE_FEATURE_ABORT = 0x00;
    static final int MESSAGE_IMAGE_VIEW_ON = 0x04;
    static final int MESSAGE_TUNER_STEP_INCREMENT = 0x05;
    static final int MESSAGE_TUNER_STEP_DECREMENT = 0x06;
    static final int MESSAGE_TUNER_DEVICE_STATUS = 0x07;
    static final int MESSAGE_GIVE_TUNER_DEVICE_STATUS = 0x08;
    static final int MESSAGE_RECORD_ON = 0x09;
    static final int MESSAGE_RECORD_STATUS = 0x0A;
    static final int MESSAGE_RECORD_OFF = 0x0B;
    static final int MESSAGE_TEXT_VIEW_ON = 0x0D;
    static final int MESSAGE_RECORD_TV_SCREEN = 0x0F;
    static final int MESSAGE_GIVE_DECK_STATUS = 0x1A;
    static final int MESSAGE_DECK_STATUS = 0x1B;
    static final int MESSAGE_SET_MENU_LANGUAGE = 0x32;
    static final int MESSAGE_CLEAR_ANALOG_TIMER = 0x33;
    static final int MESSAGE_SET_ANALOG_TIMER = 0x34;
    static final int MESSAGE_TIMER_STATUS = 0x35;
    static final int MESSAGE_STANDBY = 0x36;
    static final int MESSAGE_PLAY = 0x41;
    static final int MESSAGE_DECK_CONTROL = 0x42;
    static final int MESSAGE_TIMER_CLEARED_STATUS = 0x043;
    static final int MESSAGE_USER_CONTROL_PRESSED = 0x44;
    static final int MESSAGE_USER_CONTROL_RELEASED = 0x45;
    static final int MESSAGE_GIVE_OSD_NAME = 0x46;
    static final int MESSAGE_SET_OSD_NAME = 0x47;
    static final int MESSAGE_SET_OSD_STRING = 0x64;
    static final int MESSAGE_SET_TIMER_PROGRAM_TITLE = 0x67;
    static final int MESSAGE_SYSTEM_AUDIO_MODE_REQUEST = 0x70;
    static final int MESSAGE_GIVE_AUDIO_STATUS = 0x71;
    static final int MESSAGE_SET_SYSTEM_AUDIO_MODE = 0x72;
    static final int MESSAGE_REPORT_AUDIO_STATUS = 0x7A;
    static final int MESSAGE_GIVE_SYSTEM_AUDIO_MODE_STATUS = 0x7D;
    static final int MESSAGE_SYSTEM_AUDIO_MODE_STATUS = 0x7E;
    static final int MESSAGE_ROUTING_CHANGE = 0x80;
    static final int MESSAGE_ROUTING_INFORMATION = 0x81;
    static final int MESSAGE_ACTIVE_SOURCE = 0x82;
    static final int MESSAGE_GIVE_PHYSICAL_ADDRESS = 0x83;
    static final int MESSAGE_REPORT_PHYSICAL_ADDRESS = 0x84;
    static final int MESSAGE_REQUEST_ACTIVE_SOURCE = 0x85;
    static final int MESSAGE_SET_STREAM_PATH = 0x86;
    static final int MESSAGE_DEVICE_VENDOR_ID = 0x87;
    static final int MESSAGE_VENDOR_COMMAND = 0x89;
    static final int MESSAGE_VENDOR_REMOTE_BUTTON_DOWN = 0x8A;
    static final int MESSAGE_VENDOR_REMOTE_BUTTON_UP = 0x8B;
    static final int MESSAGE_GIVE_DEVICE_VENDOR_ID = 0x8C;
    static final int MESSAGE_MENU_REQUEST = 0x8D;
    static final int MESSAGE_MENU_STATUS = 0x8E;
    static final int MESSAGE_GIVE_DEVICE_POWER_STATUS = 0x8F;
    static final int MESSAGE_REPORT_POWER_STATUS = 0x90;
    static final int MESSAGE_GET_MENU_LANGUAGE = 0x91;
    static final int MESSAGE_SELECT_ANALOG_SERVICE = 0x92;
    static final int MESSAGE_SELECT_DIGITAL_SERVICE = 0x93;
    static final int MESSAGE_SET_DIGITAL_TIMER = 0x97;
    static final int MESSAGE_CLEAR_DIGITAL_TIMER = 0x99;
    static final int MESSAGE_SET_AUDIO_RATE = 0x9A;
    static final int MESSAGE_INACTIVE_SOURCE = 0x9D;
    static final int MESSAGE_CEC_VERSION = 0x9E;
    static final int MESSAGE_GET_CEC_VERSION = 0x9F;
    static final int MESSAGE_VENDOR_COMMAND_WITH_ID = 0xA0;
    static final int MESSAGE_CLEAR_EXTERNAL_TIMER = 0xA1;
    static final int MESSAGE_SET_EXTERNAL_TIMER = 0xA2;
    static final int MESSAGE_REPORT_SHORT_AUDIO_DESCRIPTOR = 0xA3;
    static final int MESSAGE_REQUEST_SHORT_AUDIO_DESCRIPTOR = 0xA4;
    static final int MESSAGE_INITIATE_ARC = 0xC0;
    static final int MESSAGE_REPORT_ARC_INITIATED = 0xC1;
    static final int MESSAGE_REPORT_ARC_TERMINATED = 0xC2;
    static final int MESSAGE_REQUEST_ARC_INITIATION = 0xC3;
    static final int MESSAGE_REQUEST_ARC_TERMINATION = 0xC4;
    static final int MESSAGE_TERMINATE_ARC = 0xC5;
    static final int MESSAGE_CDC_MESSAGE = 0xF8;
    static final int MESSAGE_ABORT = 0xFF;

    static final int CEC_KEYCODE_POWER = 0x40;
    static final int CEC_KEYCODE_ROOT_MENU = 0x09;
    static final int CEC_KEYCODE_POWER_ON_FUNCTION = 0x6D;

    // Send result codes. It should be consistent with hdmi_cec.h's send_message error code.
    static final int SEND_RESULT_SUCCESS = 0;
    static final int SEND_RESULT_NAK = 1;
    static final int SEND_RESULT_BUSY = 2;
    static final int SEND_RESULT_FAILURE = 3;

    static final int STANDBY_SCREEN_OFF = 0;
    static final int STANDBY_SHUTDOWN = 1;

    /** Logical address for TV */
    public static final int ADDR_TV = 0;
    /** Logical address for recorder 1 */
    public static final int ADDR_RECORDER_1 = 1;
    /** Logical address for recorder 2 */
    public static final int ADDR_RECORDER_2 = 2;
    /** Logical address for tuner 1 */
    public static final int ADDR_TUNER_1 = 3;
    /** Logical address for playback 1 */
    public static final int ADDR_PLAYBACK_1 = 4;
    /** Logical address for audio system */
    public static final int ADDR_AUDIO_SYSTEM = 5;
    /** Logical address for tuner 2 */
    public static final int ADDR_TUNER_2 = 6;
    /** Logical address for tuner 3 */
    public static final int ADDR_TUNER_3 = 7;
    /** Logical address for playback 2 */
    public static final int ADDR_PLAYBACK_2 = 8;
    /** Logical address for recorder 3 */
    public static final int ADDR_RECORDER_3 = 9;
    /** Logical address for tuner 4 */
    public static final int ADDR_TUNER_4 = 10;
    /** Logical address for playback 3 */
    public static final int ADDR_PLAYBACK_3 = 11;
    /** Logical address reserved for future usage */
    public static final int ADDR_RESERVED_1 = 12;
    /** Logical address reserved for future usage */
    public static final int ADDR_RESERVED_2 = 13;
    /** Logical address for TV other than the one assigned with {@link #ADDR_TV} */
    public static final int ADDR_SPECIFIC_USE = 14;
    /** Logical address for devices to which address cannot be allocated */
    public static final int ADDR_UNREGISTERED = 15;
    /** Logical address used in the destination address field for broadcast messages */
    public static final int ADDR_BROADCAST = 15;
    /** Logical address used to indicate it is not initialized or invalid. */
    public static final int ADDR_INVALID = -1;

    private static final int ONE_TOUCH_PLAY_DELAY = 100;
    private static final int MILIS_DELAY = 0;
    private static final int LONG_MILIS_DELAY = 6000;
    private static final int MSG_WAKE_LOCK = 1;
    private static final int MSG_GET_MENU_LANGUAGE = 2;
    private static final int MSG_ONE_TOUCH_PLAY = 3;
    /**
     * add wake lock for HdmiControlService.
     * Timeout in millisecond for device clean up and sending standby message to TV.
     */
    private static final int TIMER_DEVICE_CLEANUP = 2000;

    static final int MENU_STATE_ACTIVATED = 0;
    static final int MENU_STATE_DEACTIVATED = 1;
    private static final int OSD_NAME_MAX_LENGTH = 13;

    private final SettingsObserver mSettingsObserver;
    private Context mContext = null;
    private HdmiControlManager mControl = null;
    private HdmiPlaybackClient mPlayback = null;
    private List<HdmiPortInfo> mPortInfo = null;
    private boolean mLanguangeChanged = false;
    private int mPhyAddr = -1;
    private int mVendorId = 0;
    private PowerManager mPowerManager;
    private ActiveWakeLock mWakeLock;

    private final SystemControlManager mSystemControl;

    private long mNativePtr = 0;
    private final Object mLock = new Object();
    private IDroidHdmiCEC mProxy = null;
    private IDroidHdmiCecCallback mHdmiCecCallback = null;
    private final List<Integer> mLocalDevices;
    private static final int HDMICEC_EXTEND_COOKIE = 1;
    private static final int HDMI_EVENT_HOT_PLUG = 2;
    private static final int HDMI_EVENT_ADD_LOGICAL_ADDRESS = 4;
    private static final int HDMI_EVENT_RECEIVE_MESSAGE = 9;
    static final String PROPERTY_DEVICE_TYPE = "ro.hdmi.device_type";
    static final String UBOOTENV_REBOOT_MODE = "ubootenv.var.reboot_mode_android";
    /*
    static {
        System.loadLibrary("hdmicec_jni");
    }
    */

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_WAKE_LOCK:
                getWakeLock().release();
                break;
            case MSG_GET_MENU_LANGUAGE:
                SendGetMenuLanguage(ADDR_TV);
                break;
            case MSG_ONE_TOUCH_PLAY:
                String mode = mSystemControl.getBootenv(UBOOTENV_REBOOT_MODE, "normal");
                Log.i(TAG, "rebootmode: " + mode);
                if (!mode.equals("quiescent")) {
                    Log.i(TAG, "rebootmode noraml");
                    mHandler.postDelayed(mDelayedRun, ONE_TOUCH_PLAY_DELAY);
                }
                break;
            default:
                break;
            }
        };
    };

    public HdmiCecExtend(Context ctx) {

        //init();

        mContext = ctx;
        mControl = (HdmiControlManager) mContext.getSystemService(Context.HDMI_CONTROL_SERVICE);
        mPowerManager = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        mSettingsObserver = new SettingsObserver(mHandler);
        mSystemControl = SystemControlManager.getInstance();
        registerContentObserver();
        if (mControl != null) {
            mPlayback = mControl.getPlaybackClient();
            if (mPlayback != null) {
                mPlayback.setVendorCommandListener(this);
                mVendorId = getCecVendorId();
                mHandler.removeMessages(MSG_ONE_TOUCH_PLAY);
                mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_ONE_TOUCH_PLAY), LONG_MILIS_DELAY);
            }
            mControl.addHotplugEventListener(this);
        } else {
            Log.d(TAG, "can't find HdmiControlManager");
        }
        mLocalDevices = getIntList(SystemProperties.get(PROPERTY_DEVICE_TYPE));
        connectToProxy();
    }

    public void updatePortInfo() {
        if (mControl != null) {
            mPhyAddr = getCecPhysicalAddress();
        }
    }

    @Override
    public void onReceived(HdmiHotplugEvent event) {
        Log.d(TAG, "HdmiHotplugEvent, connected:" + event.isConnected());
        if (mPlayback != null) {
            updatePortInfo();
            if (!event.isConnected()) {
                mPortInfo = null;
            }
            if (event.isConnected()) {
                mHandler.removeMessages(MSG_ONE_TOUCH_PLAY);
                mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_ONE_TOUCH_PLAY), ONE_TOUCH_PLAY_DELAY*3);
           }
        }
    }

    /**
     * The callback is called:
     * 1. before HdmiControlService is disabled.
     * 2. after HdmiControlService is enabled and the local address assigned.
     * The client shouldn't hold the thread too long since this is a blocking call.
     */
    @Override
    public void onControlStateChanged(boolean enabled, int reason) {
        Log.d(TAG, "enabled = " + enabled + ", reason = " + reason);
        switch (reason) {
            case HdmiControlManager.CONTROL_STATE_CHANGED_REASON_WAKEUP:
                if (enabled) {
                    mHandler.removeMessages(MSG_ONE_TOUCH_PLAY);
                    mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_ONE_TOUCH_PLAY), ONE_TOUCH_PLAY_DELAY);
                }
                break;
            case HdmiControlManager.CONTROL_STATE_CHANGED_REASON_STANDBY:
                if (!enabled) {//go to standby
                    getWakeLock().acquire();
                    mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_WAKE_LOCK), TIMER_DEVICE_CLEANUP);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceived(int srcAddress, int destAddress, byte[] params, boolean hasVendorId) {
        // TODO Auto-generated method stub
    }

    private ActiveWakeLock getWakeLock() {
        if (mWakeLock == null) {
            mWakeLock = new SystemWakeLock();
        }
        return mWakeLock;
    }

    private interface ActiveWakeLock {
        void acquire();
        void release();
        boolean isHeld();
    }

    private class SystemWakeLock implements ActiveWakeLock {
        private final WakeLock mWakeLock;
        public SystemWakeLock() {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
            mWakeLock.setReferenceCounted(false);
        }

        @Override
        public void acquire() {
            mWakeLock.acquire();
        }

        @Override
        public void release() {
            mWakeLock.release();
        }

        @Override
        public boolean isHeld() {
            return mWakeLock.isHeld();
        }
    }

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {

        }
    }

    public boolean updateLanguage(Locale locale) {
        try {
            //LocalePicker.updateLocale(locale);
            Class<?> lp = Class.forName("com.android.internal.app.LocalePicker");
            Method updatelocale = lp.getMethod("updateLocale", Locale.class);
            updatelocale.invoke(null, locale);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onLanguageChange(String iso3Language) {
        Log.d(TAG, "onLanguageChange, iso3Language: " + iso3Language);

        if (iso3Language.equals("chi") || iso3Language.equals("zho")) {
            HdmiCecLanguageHelp cecLanguage = new HdmiCecLanguageHelp(iso3Language);
            Locale l = new Locale(cecLanguage.LanguageCode(), cecLanguage.CountryCode());
            mLanguangeChanged = updateLanguage(l);
            return;
        }

        Locale currentLocale = mContext.getResources().getConfiguration().locale;
        if (currentLocale.getISO3Language().equals(iso3Language)) {
            // Do not switch language if the new language is the same as the
            // current one.
            // This helps avoid accidental country variant switching from
            // en_US to en_AU
            // due to the limitation of CEC. See the warning below.
            return;
        }
        Log.d(TAG, "onLanguageChange, change language");

        // Don't use Locale.getAvailableLocales() since it returns a locale
        // which is not available on Settings.
        /*final List<LocaleInfo> localeInfos = LocalePicker.getAllAssetLocales(mContext, false);
        for (LocaleInfo localeInfo : localeInfos) {
            if (localeInfo.getLocale().getISO3Language().equals(iso3Language)) {
                // WARNING: CEC adopts ISO/FDIS-2 for language code, while
                // Android requires
                // additional country variant to pinpoint the locale. This
                // keeps the right
                // locale from being chosen. 'eng' in the CEC command, for
                // instance,
                // will always be mapped to en-AU among other variants like
                // en-US, en-GB,
                // an en-IN, which may not be the expected one.
                LocalePicker.updateLocale(localeInfo.getLocale());
                mLanguangeChanged = true;
            }
        }*/
        /* com.droidlogic is system signed, it can still use the hidden api, use reflect to avoid build problem */
        /* TODO: use the IActivityManager Api */
        try {
            Class<?> lp = Class.forName("com.android.internal.app.LocalePicker");
            Class<?> lpiClass = Class.forName("com.android.internal.app.LocalePicker.LocaleInfo");
            Method getAllAssetLocales = lp.getMethod("getAllAssetLocales", Context.class, boolean.class);
            Method getlocale = lpiClass.getMethod("getLocale", null);

            final List localeInfos = (List)getAllAssetLocales.invoke(null, mContext, false);
            for (Object o : localeInfos) {
                Locale locale = (Locale)getlocale.invoke(o, null);
                if (locale.getISO3Language().equals(iso3Language)) {
                    updateLanguage(locale);
                    mLanguangeChanged = true;
                }
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

        mLanguangeChanged = false;
    }

    private void wakeUp(long time, String reason) {
        try {
            Class<?> cls = Class.forName("android.os.PowerManager");
            Method method = cls.getMethod("wakeUp", long.class, String.class);
            method.invoke(mPowerManager, time, reason);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void onCecMessageRx(byte[] msg) {
        int initiator = 0;
        int opcode = 0;
        long time = 0;
        if (msg.length <= 1) {
            return;
        }
        initiator = ((msg[0] >> 4) & 0xf);
        opcode = (msg[1] & 0xFF);
        if (mPhyAddr == -1) {
            mPhyAddr = getCecPhysicalAddress();
        }
        Log.d(TAG, "onCecMessageRx " + String.format("intitiator = 0x%02x", initiator) + String.format(" opcode = 0x%02x", opcode));
        /* TODO: process messages service can't process */
        switch (opcode) {
        case MESSAGE_IMAGE_VIEW_ON:
        case MESSAGE_TEXT_VIEW_ON:
        case MESSAGE_SYSTEM_AUDIO_MODE_REQUEST:
            time = SystemClock.uptimeMillis();
            wakeUp(time, "android.policy:POWER");
            break;
        case MESSAGE_USER_CONTROL_PRESSED:
            int para = (msg[2] & 0xFF);
            if (para == CEC_KEYCODE_POWER || para == CEC_KEYCODE_ROOT_MENU ||  para == CEC_KEYCODE_POWER_ON_FUNCTION) {
                time = SystemClock.uptimeMillis();
                wakeUp(time, "android.policy:POWER");
            }
            break;
        /*
        case MESSAGE_SET_MENU_LANGUAGE:
            if (isAutoChangeLanguageOn()) {
                try {
                    byte lan[] = new byte[3];
                    System.arraycopy(msg, 2, lan, 0, 3);
                    String iso3Language = new String(lan, 0, 3, "US-ASCII");
                    onLanguageChange(iso3Language);
                } catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "process MESSAGE_SET_MENU_LANGUAGE failed");
                }
            }
            break;
        */
        default:
            break;
        }
    }

    private void onAddAddress(int addr) {
        Log.d(TAG, "onAddressAllocated:" + String.format("0x%02x", addr));
        mHandler.removeMessages(MSG_ONE_TOUCH_PLAY);
        mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_ONE_TOUCH_PLAY), ONE_TOUCH_PLAY_DELAY);
    }

    private final Runnable mDelayedRun = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (isOneTouchPlayOn() && mPlayback != null) {
                        /* to wake up tv in case of last oneTouchPlayAction timeout and not finish when wake up playback and try to start a new action */
                        SendCecMessage(ADDR_TV, buildCecMsg(MESSAGE_TEXT_VIEW_ON, new byte[0]));
                        mPlayback.oneTouchPlay(mOneTouchPlayCallback);
                    }
                }
            }).start();
        }
    };

    private final OneTouchPlayCallback mOneTouchPlayCallback = new OneTouchPlayCallback () {
        @Override
        public void onComplete(int result) {
            Log.d(TAG, "OneTouchPlayCallback, onComplete: " + result);
            switch (result) {
            case HdmiControlManager.RESULT_SUCCESS:
            case HdmiControlManager.RESULT_TIMEOUT:
                break;
            }
        }
    };

    private byte[] buildCecMsg(int opcode, byte[] operands) {
        byte[] params = new byte[operands.length + 1];
        params[0] = (byte)(opcode & 0xff);
        System.arraycopy(operands, 0, params, 1, operands.length);
        return params;
    }

    private void SendActiveSource(int dest, int physicalAddr) {
        byte[] msg = new byte[] {(byte)(MESSAGE_ACTIVE_SOURCE & 0xff),
                                 (byte)((physicalAddr >> 8) & 0xff),
                                 (byte) (physicalAddr & 0xff)
                                };
        SendCecMessage(dest, msg);
    }

    private void ReportPhysicalAddr(int dest, int phyAddr, int type) {
        byte[] msg = new byte[] {(byte)(MESSAGE_REPORT_PHYSICAL_ADDRESS & 0xff),
                                 (byte)((phyAddr >> 8) & 0xff),
                                 (byte) (phyAddr & 0xff),
                                 (byte) (type & 0xff)
                                };
        SendCecMessage(dest, msg);
    }

    private void SendVendorId(int dest, int id) {
        byte[] msg = new byte[] {(byte)(MESSAGE_DEVICE_VENDOR_ID & 0xff),
                                 (byte)((id >> 16) & 0xff),
                                 (byte)((id >>  8) & 0xff),
                                 (byte)((id >>  0) & 0xff)
                                };
        SendCecMessage(dest, msg);
    }

    private void SendImageViewOn(int dest) {
        SendCecMessage(dest, buildCecMsg(MESSAGE_IMAGE_VIEW_ON, new byte[0]));
    }

    private void SendReportMenuStatus(int dest, int status) {
        byte[] para = new byte[] {(byte) (status& 0xFF)};
        byte[] msg = buildCecMsg(MESSAGE_MENU_STATUS, para);
        SendCecMessage(dest, msg);
    }

    private void SendGetMenuLanguage(int dest) {
        SendCecMessage(dest, buildCecMsg(MESSAGE_GET_MENU_LANGUAGE, new byte[0]));
    }

    private void SendCecVersion(int dest, int version) {
        byte body[] = new byte[2];
        body[0] = (byte)(MESSAGE_CEC_VERSION & 0xff);
        body[1] = (byte)(version & 0xff);
        SendCecMessage(dest, body);
    }

    private void SendDeckStatus(int dest, int status) {
        byte body[] = new byte[2];
        body[0] = (byte)(MESSAGE_DECK_STATUS & 0xff);
        body[1] = (byte)(status & 0xff);
        SendCecMessage(dest, body);
    }

    public int SendCecMessage(int dest, byte[] body) {
        int retry = 2, ret = 0;
        while (retry > 0) {
            //ret = sendCecMessage(dest, body);
            for (int src : mLocalDevices) {
                ret = sendCecMessage(src, dest, body, true);
            }
            if (ret == SEND_RESULT_SUCCESS) {
                break;
            }
            retry--;
        }
        return ret;
    }

    private class SettingsObserver extends ContentObserver {
        public SettingsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            String option = uri.getLastPathSegment();
            Log.d(TAG, "onChange, option = " + option);
            switch (option) {
                case HdmiCecManager.HDMI_CONTROL_ONE_TOUCH_PLAY_ENABLED:
                    break;
                case HdmiCecManager.HDMI_CONTROL_AUTO_CHANGE_LANGUAGE_ENABLED:
                    updateMenuLanguage();
                    break;
                default:
                    break;
            }
        }
    }

    private void updateMenuLanguage() {
        if (isAutoChangeLanguageOn()) {
            SendGetMenuLanguage(ADDR_TV);
        }
    }

    private void registerContentObserver() {
        ContentResolver resolver = mContext.getContentResolver();
        String[] settings = new String[] {
                HdmiCecManager.HDMI_CONTROL_ONE_TOUCH_PLAY_ENABLED,
                HdmiCecManager.HDMI_CONTROL_AUTO_CHANGE_LANGUAGE_ENABLED
        };
        for (String s : settings) {
            resolver.registerContentObserver(Global.getUriFor(s), false, mSettingsObserver);
        }
    }

    private boolean isOneTouchPlayOn() {
        ContentResolver cr = mContext.getContentResolver();
        return Global.getInt(cr, HdmiCecManager.HDMI_CONTROL_ONE_TOUCH_PLAY_ENABLED, ENABLED) == ENABLED;
    }

    private boolean isAutoChangeLanguageOn() {
        ContentResolver cr = mContext.getContentResolver();
        return Global.getInt(cr, HdmiCecManager.HDMI_CONTROL_AUTO_CHANGE_LANGUAGE_ENABLED, ENABLED) == ENABLED;
    }

     private void init() {
        synchronized (mLock) {
            mNativePtr = nativeInit(this);
         }
        Log.d(TAG, "init, mNativePtr = " + mNativePtr);
     }

    private void connectToProxy() {
        synchronized (mLock) {
            if (mProxy != null) {
                return;
            }
            try {
                mProxy = IDroidHdmiCEC.getService();
                mProxy.linkToDeath(new DeathRecipient(), HDMICEC_EXTEND_COOKIE);
                mHdmiCecCallback = new HdmiCecCallback(this);
                mProxy.setCallback(mHdmiCecCallback, ConnectType.TYPE_EXTEND);
            } catch (NoSuchElementException e) {
                Log.e(TAG, "connectToProxy: hdmicecd service not found."
                        + " Did the service fail to start?", e);
            } catch (RemoteException e) {
                Log.e(TAG, "connectToProxy: hdmicecd service not responding.", e);
            }
        }

        Log.d(TAG, "connect to hdmicecd service success.");
    }

    final class DeathRecipient implements HwBinder.DeathRecipient {
        DeathRecipient() {
        }

        @Override
        public void serviceDied(long cookie) {
            if (HDMICEC_EXTEND_COOKIE == cookie) {
                Log.e(TAG, "hdmicecd service died cookie: " + cookie);
                synchronized (mLock) {
                    mProxy = null;
                }
            }
        }
    }

    private List<Integer> getIntList(String string) {
        ArrayList<Integer> list = new ArrayList<>();
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(',');
        splitter.setString(string);
        for (String item : splitter) {
            try {
                list.add(Integer.parseInt(item));
            } catch (NumberFormatException e) {
                Log.e(TAG, "Can't parseInt: " + item);
            }
        }
        return Collections.unmodifiableList(list);
   }

    private int getCecPhysicalAddress() {
        int ret = 0;
        synchronized (mLock) {
            if (mNativePtr != 0) {
                ret = nativeGetPhysicalAddr(mNativePtr);
            }
        }
        Log.d(TAG, String.format("getCecPhysicalAddress = %x", ret));
        return ret;
    }

    private int getCecVendorId() {
        int ret = -1;
        synchronized (mLock) {
            if (mNativePtr != 0) {
                ret = nativeGetVendorId(mNativePtr);
            }
        }
        Log.d(TAG, "getCecVendorId, ret = " + ret);
        return ret;
    }

    private int getCecVersion() {
        int ret = -1;
        synchronized (mLock) {
            if (mNativePtr != 0) {
                ret = nativeGetCecVersion(mNativePtr);
            }
        }
        Log.d(TAG, "getCecVersion, ret = " + ret);
        return ret;
    }

    private int sendCecMessage(int dest, byte[] body) {
        int ret = -1;
        synchronized (mLock) {
            if (mNativePtr != 0) {
                ret = nativeSendCecMessage(mNativePtr, dest, body);
            }
        }
        Log.d(TAG, "sendCecMessage, " + toString(dest, body));
        return ret;
    }

    private int sendCecMessage(int src, int dest, byte[] body, boolean isExtend) {
        int ret = -1;
        CecMessage message = new CecMessage();
        message.initiator = src;
        message.destination = dest;
        for (int i = 0; i < body.length; i++) {
            message.body.add((Byte)body[i]);
        }
        synchronized (mLock) {
            if (mProxy != null) {
                try {
                    ret = mProxy.sendMessage(message, true);
                } catch (RemoteException e) {
                    Log.e(TAG, "sendCecMessage: hdmicecd service not responding.", e);
                }
            }
        }
        Log.d(TAG, "sendCecMessage, " + toString(dest, body));
        return ret;
    }

    private String toString(int dest, byte[] body) {
        StringBuffer s = new StringBuffer();
        s.append(String.format("dest: %d", dest));
        if (body.length > 0) {
            s.append(", body:");
            for (byte data : body) {
                s.append(String.format(" %02X", data));
            }
        }
        return s.toString();
    }

    class HdmiCecLanguageHelp {
        private final String [][] mCecLanguage = {
            {"chi", "zh", "CN"},
            {"zho", "zh", "TW"}
        };

        private int mIndex;

        HdmiCecLanguageHelp(String str) {
            int size;
            for (size = 0; size < mCecLanguage.length; size++) {
                if (mCecLanguage[size][0].equals(str)) {
                    mIndex = size;
                    break;
                }
            }
            if (size == mCecLanguage.length) {
                mIndex = -1;
            }
        }

        public String LanguageCode() {
            if (mIndex != -1) {
                return mCecLanguage[mIndex][1];
            }
            return null;
        }

        public String CountryCode() {
            if (mIndex != -1) {
                return mCecLanguage[mIndex][2];
            }
            return null;
        }

        /*
         * get android language code for cec language code
         */
        public final String getCecLanguageCode(String cecLanguage) {
            int size;
            for (size = 0; size < mCecLanguage.length; size++) {
                if (mCecLanguage[size][0].equals(cecLanguage))
                    return mCecLanguage[size][1];
            }
            return null;
        }

        /*
         * get android country code for cec language code
         */
        public final String getCecCountryCode(String cecLanguage) {
            int size;
            for (size = 0; size < mCecLanguage.length; size++) {
                if (mCecLanguage[size][0].equals(cecLanguage))
                    return mCecLanguage[size][2];
            }
            return null;
        }
    }

    class HdmiCecCallback extends IDroidHdmiCecCallback.Stub {
        public HdmiCecCallback(HdmiCecExtend mExtend) {
            mHdmiCecExtend = mExtend;
        }

        public void notifyCallback(CecEvent cecEvent) {
            //Slog.d(TAG, "notifyCallback: " + cecEvent);
            int eventType = (int)cecEvent.eventType;
            int bodySize = cecEvent.cec.body.size();
            if (eventType == HDMI_EVENT_HOT_PLUG) {
                //boolean connected = cecEvent.hotplug.connected;
                //int port_id = cecEvent.hotplug.portId;
                //if (mHdmiCecExtend != null) {
                //    mHdmiCecExtend.onReceived(new HdmiHotplugEvent(port_id, connected));
                //}
            } else if (eventType == HDMI_EVENT_ADD_LOGICAL_ADDRESS) {
                if (mHdmiCecExtend != null) {
                    mHdmiCecExtend.onAddAddress(cecEvent.logicalAddress);
                }
            } else {
                byte[] msg = new byte[bodySize + 1];
                msg[0] = (byte) (((cecEvent.cec.initiator << 0x04) & 0xF0) + (cecEvent.cec.destination & 0x0F));
                for (int i = 0; i < bodySize; i++) {
                    msg[i + 1] =  (byte)cecEvent.cec.body.get(i);
                }
                if (mHdmiCecExtend != null) {
                    mHdmiCecExtend.onCecMessageRx(msg);
                }
            }
        }
        private HdmiCecExtend mHdmiCecExtend = null;
    }
    /* for native */
    public native int nativeSendCecMessage(long ptr, int dest, byte[] body);
    public native long nativeInit(HdmiCecExtend ext);
    public native int nativeGetPhysicalAddr(long ptr);
    public native int nativeGetVendorId(long ptr);
    public native int nativeGetCecVersion(long ptr);
}
