package com.lx.iruanmi.bingwallpaper.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liuxue on 2015/3/21.
 */
public class HackyDrawerLayout extends DrawerLayout {

    public static final String TAG = "HackyDrawerLayout";

    private boolean isLocked;
    private boolean mIsDisallowIntercept = false;

    public HackyDrawerLayout(Context context) {
        super(context);

        init();
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        isLocked = false;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // keep the info about if the innerViews do requestDisallowInterceptTouchEvent
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "dispatchTouchEvent()");
        // the incorrect array size will only happen in the multi-touch scenario.
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "onInterceptTouchEvent()");
//        return super.onInterceptTouchEvent(ev);

        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override

    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "onTouchEvent()");
//        return super.onTouchEvent(ev);

        if (!isLocked) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
