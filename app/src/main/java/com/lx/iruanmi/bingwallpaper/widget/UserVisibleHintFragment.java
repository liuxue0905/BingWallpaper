package com.lx.iruanmi.bingwallpaper.widget;

import android.support.v4.app.Fragment;

/**
 * Created by liuxue on 2015/5/1.
 */
public abstract class UserVisibleHintFragment extends Fragment {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        } else if (!isVisibleToUser && isResumed()) {
            onInVisible();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE
        onVisible();
    }

//    private boolean isPaused;
//
//    protected boolean isPaused() {
//        return isPaused;
//    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint() && isResumed()) {
            //INSERT CUSTOM CODE HERE
            onInVisible();
        }
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void onVisible();

    protected abstract void onInVisible();
}
