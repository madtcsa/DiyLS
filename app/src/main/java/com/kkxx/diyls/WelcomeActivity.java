package com.kkxx.diyls;

import android.content.Intent;
import android.os.Bundle;

import com.kkxx.diyls.welcome.AnimatedMuzeiLogoFragment;


/**
 * @author chenwei
 */
public class WelcomeActivity extends BaseActivity {

    private boolean isInit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInit = localSharedPreferences.getBoolean(PREFS_IS_INIT, false);
        setContentView(R.layout.activity_welcome);
        final AnimatedMuzeiLogoFragment logoFragment
                = (AnimatedMuzeiLogoFragment) getFragmentManager().findFragmentById(
                R.id.animated_logo_fragment);
        logoFragment.reset();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoFragment.start();
            }
        }, 200);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent();
                if (isInit) {
                    mIntent.setClass(WelcomeActivity.this, HomePageActivity.class);
                } else {
                    mIntent.setClass(WelcomeActivity.this, InitSysActivity.class);
                }
                startActivity(mIntent);
                overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
                WelcomeActivity.this.finish();
            }
        }, 2500);
    }
}
