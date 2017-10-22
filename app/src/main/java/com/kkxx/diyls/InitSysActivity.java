package com.kkxx.diyls;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.kkxx.diyls.render.RendererFragment;
import com.kkxx.diyls.render.util.UriUtil;
import com.kkxx.diyls.utils.ShimmerFrameLayout;

public class InitSysActivity extends BaseActivity implements View.OnClickListener {

    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initsys);
        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        findViewById(R.id.btn_closesys).setOnClickListener(this);
        findViewById(R.id.btn_initdone).setOnClickListener(this);
        initTextFont();
        localSharedPreferences.edit().putBoolean(PREFS_IS_INIT, true).apply();
    }


    private void initTextFont() {
        ((TextView) findViewById(R.id.text_init1)).setTypeface(mContentFace);
        ((TextView) findViewById(R.id.text_init2)).setTypeface(mContentFace);
        ((TextView) findViewById(R.id.text_init3)).setTypeface(mContentFace);
        ((TextView) findViewById(R.id.btn_initdone)).setTypeface(mContentFace);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btn_closesys:
                i = new Intent("android.settings.SECURITY_SETTINGS");
                startActivity(i);
                break;
            case R.id.btn_initdone:
                i = new Intent();
                i.setClass(InitSysActivity.this, HomePageActivity.class);
                startActivity(i);
                InitSysActivity.this.finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        getFragmentManager().beginTransaction().add(R.id.frame_init,
                RendererFragment.createInstance(
                        UriUtil.getImageAbsolutePath(
                                InitSysActivity.this,
                                BaseActivity.mUri))).commit();
    }


    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.finish();
        return true;
    }
}
