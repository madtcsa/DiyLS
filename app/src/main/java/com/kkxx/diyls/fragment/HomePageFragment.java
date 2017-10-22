package com.kkxx.diyls.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.kkxx.diyls.BaseActivity;
import com.kkxx.diyls.R;
import com.kkxx.diyls.utils.switchbutton.SwitchButton;

import static com.kkxx.diyls.BaseActivity.PREFS_SETTING_SHAKE;
import static com.kkxx.diyls.BaseActivity.PREFS_SETTING_SHOWMENU;

/**
 * DATE：2017/10/21 on 12:27
 * Description:
 *
 * @author kkxx
 */

public class HomePageFragment extends BaseFragment {

    private Button selectBgBtn;
    private SwitchButton statusBarSettingSb, vibrateSettingSb;

    @Override
    int getLayoutId() {
        return R.layout.fragment_home_page;
    }

    @Override
    void initUI() {
        selectBgBtn = (Button) mView.findViewById(R.id.chose_image);
        statusBarSettingSb = (SwitchButton) mView.findViewById(R.id.btn_detialsetting_showmenu);
        vibrateSettingSb = (SwitchButton) mView.findViewById(R.id.btn_detialsetting_shake);
        selectBgBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });
        statusBarSettingSb.setChecked(BaseActivity.localSharedPreferences.getBoolean(
                PREFS_SETTING_SHOWMENU, false));
        vibrateSettingSb.setChecked(BaseActivity.localSharedPreferences.getBoolean(
                PREFS_SETTING_SHAKE, false));
        statusBarSettingSb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            BaseActivity.localSharedPreferences.edit().putBoolean(PREFS_SETTING_SHOWMENU, isChecked)
                    .apply();
        });
        vibrateSettingSb.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            BaseActivity.localSharedPreferences.edit().putBoolean(PREFS_SETTING_SHAKE, isChecked)
                    .apply();
        }));
    }
}
