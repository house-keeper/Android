package com.example.housekeeper_android.ui.etc;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class PushWakeLock {
    private static PowerManager.WakeLock sCpuWakeLock;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static boolean isScreenLock;
    private static String TAG = "HouseKeeper";

    public static void acquireCpuWakeLock(Context context) {
        if (sCpuWakeLock != null)
            return;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        Log.d("TESTTEST","acquireCpuWakeLock");
        sCpuWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);

        sCpuWakeLock.acquire();
    }

    public static void releaseCpuLock() {
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
}
