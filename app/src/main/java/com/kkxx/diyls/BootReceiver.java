package com.kkxx.diyls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 * @author Administrator
 * @date 2015/5/5
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context paramContext, Intent paramIntent) {
        if (!paramIntent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            return;
        }
        if (BaseActivity.localSharedPreferences.getBoolean(BaseActivity.PREFS_IS_OPEN, true)) {
            Intent localIntent = new Intent(paramContext, LockScreenService.class);
            paramContext.startService(localIntent);
        }
    }
}
