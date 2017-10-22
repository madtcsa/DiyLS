package com.kkxx.diyls;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkxx.diyls.render.MissView;
import com.kkxx.diyls.render.util.UriUtil;
import com.kkxx.diyls.utils.DataString;
import com.kkxx.diyls.utils.SliderLayout;
import com.kkxx.diyls.utils.VibratorUtil;

import java.io.File;

public class LockScreenService extends Service {

    private boolean isShow;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
        //重新启动
        startService(new Intent(LockScreenService.this, LockScreenService.class));
    }

    private BroadcastReceiver screenReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context
                        .KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
                keyguardLock.disableKeyguard();
                if (BaseActivity.localSharedPreferences.getBoolean(BaseActivity.PREFS_IS_OPEN, true)) {
                    if (!isShow) {
                        createFloatView();
                    }
                }
            } else if (action.equals(Intent.ACTION_SCREEN_ON)) {

            }
        }
    };

    public static final int FLAG_LAYOUT_IN_SCREEN = 0x00000100;

    public void createFloatView() {
        final View mFloatView = View.inflate(getApplicationContext(), R.layout.screenlock_main,
                null);
        final WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.width = -1;
        wmParams.height = -1;
        wmParams.type = 2010;

        if (!BaseActivity.localSharedPreferences.getBoolean(BaseActivity.PREFS_SETTING_SHOWMENU,
                false)) {
            wmParams.flags = FLAG_LAYOUT_IN_SCREEN;
        }

        isShow = true;
        //初始化日期星期
        TextView mDataTextView = (TextView) mFloatView.findViewById(R.id.date);
        mDataTextView.setText(DataString.StringData());

        String path = "";
        if (!TextUtils.isEmpty(BaseActivity.localSharedPreferences.getString(
                BaseActivity.PREFS_IMAGE_PATH, ""))) {
            path = BaseActivity.localSharedPreferences.getString(BaseActivity.PREFS_IMAGE_PATH, "");
        } else {
            path = UriUtil.getImageAbsolutePath(this.getApplication(), BaseActivity.mUri);
        }
        if (TextUtils.isEmpty(path)) {
            path = DiyLSApplication.getSDPath() + "/" + DiyLSApplication.DIR_NAME + "/" +
                    DiyLSApplication.IMAGE_NAME;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MissView missView = (MissView) mFloatView.findViewById(R.id.missview);
            missView.setVisibility(View.VISIBLE);
            missView.initPicture(path);
        } else {
            ImageView imageView = (ImageView) mFloatView.findViewById(R.id.imageview);
            imageView.setImageURI(Uri.fromFile(new File(path)));
            imageView.setVisibility(View.VISIBLE);
        }

        SliderLayout mSliderLayout = (SliderLayout) mFloatView.findViewById(R.id.sliderlayout);
        //滑动解锁
        mSliderLayout.setOnUnlockListener(() -> {
            if (BaseActivity.localSharedPreferences.getBoolean(BaseActivity.PREFS_SETTING_SHAKE, false)) {
                VibratorUtil.Vibrate(getApplicationContext(), 50);
            }
            windowManager.removeView(mFloatView);
            isShow = false;
        });
        mSliderLayout.setVisibility(View.VISIBLE);
        windowManager.addView(mFloatView, wmParams);
    }
}
