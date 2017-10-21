package com.kkxx.diyls;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;
/**
 *
 * @author Administrator
 * @date 2015/3/26
 */
public class BaseActivity extends Activity {
    private long exitTime = 0;
    Typeface mContentFace;
    public static final String PREFS = "locker_pref";
    public static final String PREFS_IS_INIT = "is_init";
    public static final String PREFS_IS_OPEN = "is_open";

    public static final String PREFS_SETTING_SHOWMENU = "setting_showmenu";
    public static final String PREFS_SETTING_SHAKE = "setting_shake";
    public static final String PREFS_SETTING_SHOWLINE = "setting_showline";
    public static final String PREFS_GESTURE = "gesture";
    public static final String PREFS_MODE = "home_mode";//解锁模式
    public static final String PREFS_IMAGE_PATH = "image_path";//图片地址
    public static final String PREFS_FACE_STRING = "face_string";//人脸识别保存的脸

    public static SharedPreferences localSharedPreferences;
    public static Uri mUri;
    public Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentFace = Typeface.createFromAsset(this.getAssets(),
                "font/cartoon.ttf");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
