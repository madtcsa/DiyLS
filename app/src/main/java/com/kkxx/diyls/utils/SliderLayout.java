package com.kkxx.diyls.utils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kkxx.diyls.R;
import com.kkxx.diyls.matchview.MatchTextView;

/**
 * @author chenwei
 */
public class SliderLayout extends RelativeLayout {
    private Context context;
    private int locationX = 0;
    private Handler handler = new Handler();
    private static int BACK_DURATION = 1;
    private RelativeLayout mMatchView;
    private MatchTextView mMatchTextView;
    private OnUnlockListener mOnUnlockListener;

    public SliderLayout(Context context) {
        super(context);
        SliderLayout.this.context = context;
        init();
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        SliderLayout.this.context = context;
        init();
    }

    public SliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        SliderLayout.this.context = context;
        init();
    }

    public void setOnUnlockListener(OnUnlockListener mOnUnlockListener) {
        this.mOnUnlockListener = mOnUnlockListener;
    }

    private void init() {
        locationX = getScreenWidth() / 2;
        handler.post(() -> {
            if (backx >= 0) {
                backx = backx - 5;
                if (backx > 0) {
                    mMatchView.scrollTo(backx, y);
                    handler.sendEmptyMessageDelayed(0, BACK_DURATION);
                } else {
                    backx = 0;
                    mMatchView.scrollTo(x, y);
                }
            } else {
                backx = backx + 5;
                if (backx < 0) {
                    mMatchView.scrollTo(backx, y);
                    handler.sendEmptyMessageDelayed(0, BACK_DURATION);
                } else {
                    backx = 0;
                    mMatchView.scrollTo(x, y);
                }
            }
            float progress = 1 - Math.abs(backx) * 1f / Math.abs(unlockx);
            if (progress >= 1) {
                progress = 1;
            } else if (progress <= 0) {
                progress = 0.1f;
            }
            mMatchTextView.setProgress(progress);
        });
    }

    /**
     * 这个方法里可以得到一个其他资源
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMatchView = (RelativeLayout) findViewById(R.id.matchview);
        mMatchTextView = (MatchTextView) findViewById(R.id.mMatchTextView);
        y = mMatchView.getScrollY();
        x = mMatchView.getScrollX();
        unlockx = getScreenWidth() / 4;
    }

    int x = 0;
    int y = 0;
    int unlockx = 0;
    int backx = 0;
    int moveX = 0;
    private boolean isUnLock = false;

    /**
     * 对拖拽图片不同的点击事件处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isUnLock) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationX = (int) event.getX();
                return isActionDown(event);

            case MotionEvent.ACTION_MOVE:
                moveX = x + locationX - (int) event.getX();
                if (!isUnLocked(moveX)) {
                    mMatchView.scrollTo(moveX, y);
                    float progress = 1 - Math.abs(moveX) * 1f / Math.abs(unlockx);
                    if (progress >= 1) {
                        progress = 1;
                    } else if (progress <= 0) {
                        progress = 0.1f;
                    }
                    mMatchTextView.setProgress(progress);
                } else {
                    isUnLock = true;
                    mOnUnlockListener.onUnlock();
                }
                return true;
            case MotionEvent.ACTION_UP:
                backx = moveX;
                handler.sendEmptyMessage(0);
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断是否点击到了滑动区域
     */
    private boolean isActionDown(MotionEvent event) {
        Rect rect = new Rect();
        mMatchView.getHitRect(rect);
        return rect.contains((int) event.getX(), (int) event.getY());
    }

    /**
     * 判断是否解锁
     */
    private boolean isUnLocked(int moveX) {
        return Math.abs(moveX) >= Math.abs(unlockx);
    }

    /**
     * 获取屏幕宽度
     */
    private int getScreenWidth() {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }

    public interface OnUnlockListener {
        void onUnlock();
    }
}