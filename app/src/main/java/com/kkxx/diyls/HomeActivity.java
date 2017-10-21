package com.kkxx.diyls;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.kkxx.diyls.fragment.AboutFragment;
import com.kkxx.diyls.fragment.DetialSettingFragment;
import com.kkxx.diyls.fragment.FaceSettingFragment;
import com.kkxx.diyls.fragment.GestureSettingFragment;
import com.kkxx.diyls.fragment.HomeFragment;
import com.kkxx.diyls.fragment.InitFragment;
import com.kkxx.diyls.fragment.MenuFragment;
import com.kkxx.diyls.render.RendererFragment;
import com.kkxx.diyls.render.util.UriUtil;
import com.kkxx.diyls.utils.ActionBarDrawerToggle;
import com.kkxx.diyls.utils.DrawerArrowDrawable;

import java.io.File;

public class HomeActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private CharSequence mTitle;
    public int position;
    private int oldPosition = 0;
    private String[] sNewsList;

    private Fragment mContent;
    private Fragment mHomeFragment;
    private Fragment mDetialSettingFragment;
    private Fragment mInitSettingFragment;
    private Fragment mGestureSettingFragment;
    private Fragment mFaceSettingFragment;
    private Fragment mAboutFragment;

    RendererFragment mRendererFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_home);
        sNewsList = getResources().getStringArray(R.array.menu_names);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mHomeFragment = new HomeFragment();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, drawerArrow,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                int a = (int) (slideOffset * (255.0f));
                getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(a, 0, 0, 0)));
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View view) {

                if (position != 5) {
                    if (position == oldPosition) {
                        return;
                    } else {
                        oldPosition = position;
                    }
                    setTitle(sNewsList[position]);
                }
                invalidateOptionsMenu(); // creates call to
                switch (position) {
                    case 0:
                        if (mHomeFragment == null) {
                            mHomeFragment = new HomeFragment();
                        }
                        mContent = mHomeFragment;
                        break;
                    case 1:
                        if (mDetialSettingFragment == null) {
                            mDetialSettingFragment = new DetialSettingFragment();
                        }
                        mContent = mDetialSettingFragment;
                        break;
                    case 2:
                        if (mGestureSettingFragment == null) {
                            mGestureSettingFragment = new GestureSettingFragment();
                        }
                        mContent = mGestureSettingFragment;
                        break;
                    case 3:
                        if (mFaceSettingFragment == null) {
                            mFaceSettingFragment = new FaceSettingFragment();
                        }
                        mContent = mFaceSettingFragment;
                        break;
                    case 4:
                        if (mInitSettingFragment == null) {
                            mInitSettingFragment = new InitFragment();
                        }
                        mContent = mInitSettingFragment;
                        break;
                    case 5:
                        Toast.makeText(mActivity, getResources().getString(R.string.update),
                                Toast.LENGTH_LONG).show();
                        return;
                    case 6:
                        if (mAboutFragment == null) {
                            mAboutFragment = new AboutFragment();
                        }
                        mContent = mAboutFragment;
                        break;
                    default:
                        return;
                }
                getFragmentManager().beginTransaction().replace(R.id.content_frame, mContent)
                        .commit();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment())
                .commit();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, mHomeFragment).commit();

        mRendererFragment = RendererFragment.createInstance(
                UriUtil.getImageAbsolutePath(this, BaseActivity.mUri));
        getFragmentManager().beginTransaction().add(R.id.frame_init, mRendererFragment).commit();
        setTitle(getResources().getString(R.string.app_name));
    }

    public void closeMenu() {
        mDrawerLayout.closeDrawer(findViewById(R.id.menu_frame));
    }

    public void toGesture() {
        if (mGestureSettingFragment == null) {
            mGestureSettingFragment = new GestureSettingFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.content_frame, mGestureSettingFragment)
                .commit();
        oldPosition = 2;
    }


    public void toFace() {
        if (mFaceSettingFragment == null) {
            mFaceSettingFragment = new FaceSettingFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mFaceSettingFragment)
                .commit();
        oldPosition = 3;
    }

    public void openMenu() {
        mDrawerLayout.openDrawer(findViewById(R.id.menu_frame));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_settings) {
            Intent i = new Intent();
            i.setClass(HomeActivity.this, WelcomeActivity.class);
            i.putExtra("show", true);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment
                .MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }
}
