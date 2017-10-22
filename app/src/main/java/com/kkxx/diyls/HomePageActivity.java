package com.kkxx.diyls;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.kkxx.diyls.BaseActivity;
import com.kkxx.diyls.LockScreenService;
import com.kkxx.diyls.R;
import com.kkxx.diyls.fragment.HomePageFragment;
import com.kkxx.diyls.render.RendererFragment;
import com.kkxx.diyls.render.util.UriUtil;

/**
 * @author chenwei
 */
public class HomePageActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private RendererFragment mRendererFragment;
    private Fragment homePageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);
        mFragmentManager = getFragmentManager();
        mRendererFragment = RendererFragment.createInstance(UriUtil.getImageAbsolutePath(this,
                BaseActivity.mUri));
        homePageFragment = new HomePageFragment();
        mFragmentManager.beginTransaction().add(R.id.frame_init, mRendererFragment).commit();
        mFragmentManager.beginTransaction().replace(R.id.content_frame, homePageFragment).commit();
    }
}
