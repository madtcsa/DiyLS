/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kkxx.diyls.render;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.kkxx.diyls.render.util.BitmapRegionLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class DemoRenderController extends RenderController {

    private static final long ANIMATION_CYCLE_TIME_MILLIS = 35000;
    private static final long FOCUS_DELAY_TIME_MILLIS = 2000;
    private static final long FOCUS_TIME_MILLIS = 6000;

    private Animator mCurrentScrollAnimator;
    private boolean mReverseDirection = false;
    private boolean mAllowFocus = true;
    private String mPictureName;
    private final Handler mHandler = new MyHandler(this);


    public DemoRenderController(Context context, BlurRenderer renderer, Callbacks callbacks, boolean allowFocus, String mPictureName) {
        super(context, renderer, callbacks);
        mAllowFocus = allowFocus;
        this.mPictureName = mPictureName;
        runAnimation();
    }


    private void runAnimation() {
        if (mCurrentScrollAnimator != null) {
            mCurrentScrollAnimator.cancel();
        }

        mCurrentScrollAnimator = ObjectAnimator.ofFloat(mRenderer,
                "normalOffsetX", mReverseDirection ? 1f : 0f,
                mReverseDirection ? 0f : 1f)
                                               .setDuration(
                                                       ANIMATION_CYCLE_TIME_MILLIS);
        mCurrentScrollAnimator.start();
        mCurrentScrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mReverseDirection = !mReverseDirection;
                runAnimation();
            }
        });
        if (mAllowFocus) {
            mHandler.sendEmptyMessageDelayed(1, FOCUS_DELAY_TIME_MILLIS);
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<DemoRenderController> mActivity;


        public MyHandler(DemoRenderController activity) {
            mActivity = new WeakReference<DemoRenderController>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            if (msg.arg1 == 0) {
                mActivity.get().mRenderer.setIsBlurred(false, false);
            }
            else {
                mActivity.get().mRenderer.setIsBlurred(true, false);
            }
            mActivity.get().mHandler.sendEmptyMessageDelayed(1,
                    FOCUS_TIME_MILLIS);
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        if (mCurrentScrollAnimator != null) {
            mCurrentScrollAnimator.cancel();
        }
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    protected BitmapRegionLoader openDownloadedCurrentArtwork(boolean forceReload) {
        try {
            InputStream m = new FileInputStream(mPictureName);
            return BitmapRegionLoader.newInstance(m);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
