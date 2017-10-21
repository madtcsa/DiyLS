package com.kkxx.diyls.fragment;

import android.text.TextUtils;
import android.widget.CompoundButton;

import com.kkxx.diyls.BaseActivity;
import com.kkxx.diyls.HomeActivity;
import com.kkxx.diyls.R;
import com.kkxx.diyls.utils.switchbutton.SwitchButton;

public class DetialSettingFragment extends BaseFragment
        implements CompoundButton.OnCheckedChangeListener {

    private SwitchButton mShowMenu;
    private SwitchButton mShake;
    private SwitchButton mShowLine;


    @Override
    int getLayoutId() {
        return R.layout.fragment_detialsetting;
    }


    @Override
    void initUI() {
        mShowMenu = (SwitchButton) mView.findViewById(
                R.id.btn_detialsetting_showmenu);
        mShake = (SwitchButton) mView.findViewById(
                R.id.btn_detialsetting_shake);
        mShowLine = (SwitchButton) mView.findViewById(
                R.id.btn_detialsetting_showline);
        mShowMenu.setOnCheckedChangeListener(this);
        mShake.setOnCheckedChangeListener(this);
        mShowLine.setOnCheckedChangeListener(this);

        mShowMenu.setChecked(BaseActivity.localSharedPreferences.getBoolean(
                HomeActivity.PREFS_SETTING_SHOWMENU, false));
        mShake.setChecked(BaseActivity.localSharedPreferences.getBoolean(
                HomeActivity.PREFS_SETTING_SHAKE, false));
        mShowLine.setChecked(BaseActivity.localSharedPreferences.getBoolean(
                HomeActivity.PREFS_SETTING_SHOWLINE, false));
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String prefsString = null;
        switch (buttonView.getId()) {
            case R.id.btn_detialsetting_showmenu:
                prefsString = HomeActivity.PREFS_SETTING_SHOWMENU;
                break;
            case R.id.btn_detialsetting_shake:
                prefsString = HomeActivity.PREFS_SETTING_SHAKE;
                break;
            case R.id.btn_detialsetting_showline:
                prefsString = HomeActivity.PREFS_SETTING_SHOWLINE;
                break;
        }
        if (!TextUtils.isEmpty(prefsString)) {
            if (isChecked) {
                BaseActivity.localSharedPreferences.edit()
                                                    .putBoolean(prefsString,
                                                            true)
                                                    .commit();
            }
            else {
                BaseActivity.localSharedPreferences.edit()
                                                    .putBoolean(prefsString,
                                                            false)
                                                    .commit();
            }
        }
    }
}
