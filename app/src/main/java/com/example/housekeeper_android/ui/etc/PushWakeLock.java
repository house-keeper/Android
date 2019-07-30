package com.example.housekeeper_android.ui.etc;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public class PushWakeLock {
    private static PowerManager.WakeLock sCpuWakeLock;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static boolean isScreenLock;
    private static String TAG = "HouseKeeper";

    public static void acquireCpuWakeLock(Context context) {
        if (sCpuWakeLock != null)
            return;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);

        sCpuWakeLock.acquire();
    }

    public static void releaseCpuLock() {
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
}
