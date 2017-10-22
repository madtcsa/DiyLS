package com.kkxx.diyls;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

//import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Administrator
 * @date 2015/4/30
 */
public class DiyLSApplication extends Application {
    public static final String PREFS = "locker_pref";
    public static final String DIR_NAME = "diyls_img";
    public static final String FACEIMG_NAME = "Face_img.jpeg";
    public static final String IMAGE_NAME = "night2.jpg";
    public static String facePath = DiyLSApplication.getSDPath() + "/" +
            DiyLSApplication.DIR_NAME + "/" +
            DiyLSApplication.FACEIMG_NAME;


    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
        BaseActivity.localSharedPreferences = getSharedPreferences(PREFS, 0);
        initImage();
        if (BaseActivity.localSharedPreferences.getBoolean(
                BaseActivity.PREFS_IS_OPEN, true)) {
            startService(new Intent(this, LockScreenService.class));
        }
    }


    private void initImage() {
        try {
            if (!TextUtils.isEmpty(BaseActivity.localSharedPreferences.getString(BaseActivity.PREFS_IMAGE_PATH,""))) {
                BaseActivity.mUri = Uri.fromFile(new File(
                        BaseActivity.localSharedPreferences.getString(
                                BaseActivity.PREFS_IMAGE_PATH, "")));
                return;
            }
            File image = new File(
                    getSDPath() + "/" + DIR_NAME + "/" + IMAGE_NAME);
            if (image.exists()) {
                BaseActivity.mUri = Uri.fromFile(image);
                return;
            }

            File file = new File(getSDPath() + "/" + DIR_NAME);
            if (!file.exists()) {
                file.mkdirs();
            }
            assetsDataToSD(getSDPath() + "/" + DIR_NAME + "/" + IMAGE_NAME);
            BaseActivity.mUri = Uri.fromFile(
                    new File(getSDPath() + "/" + DIR_NAME + "/" + IMAGE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void assetsDataToSD(String fileName) throws IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(fileName);
        myInput = this.getAssets().open(IMAGE_NAME);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }


    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

}
