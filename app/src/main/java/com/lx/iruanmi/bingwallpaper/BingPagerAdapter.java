package com.lx.iruanmi.bingwallpaper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;
import com.lx.iruanmi.bingwallpaper.util.Utility;

/**
 * Created by liuxue on 2015/4/30.
 */
public class BingPagerAdapter extends /*FragmentPagerAdapter*/FragmentStatePagerAdapter {

    private static final String TAG = "BingPagerAdapter";

    private Context context;
    private String c;

    private String pix;

    public BingPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Utility.DAYS_SHOW;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem() position:" + position);

        GetBingRequest getBingRequest = new GetBingRequest(Utility.positionToYmd(context, position), this.c);

        Log.d(TAG, "getItem() newInstance begin position:" + position);
        Fragment f = BingItemFragment.newInstance(position, getBingRequest, this.pix);
        Log.d(TAG, "getItem() newInstance end position:" + position);
        return f;
    }

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
////        return super.instantiateItem(container, position);
//
//        BingItemFragment fragment = (BingItemFragment) mFragmentManager.findFragmentByTag(BingItemFragment.class.getSimpleName() + '-' + position);
//
//        if (fragment != null) {
//            return fragment;
//        }
//
//        fragment = (BingItemFragment) super.instantiateItem(container, position);
////        GetBingRequest getBingRequest = new GetBingRequest(Utility.positionToYmd(context, position), this.c);
////        fragment.bind(getBingRequest);
//
//        if (mCurTransaction == null) {
//            mCurTransaction = mFragmentManager.beginTransaction();
//        }
//
//        mCurTransaction.remove(fragment);
//        mCurTransaction.add(container.getId(), fragment, BingItemFragment.class.getSimpleName() + '-' + position);
//
//        return fragment;
//    }

//    @Override
//    public void finishUpdate(ViewGroup container) {
//        super.finishUpdate(container);
//        if (mCurTransaction != null) {
//            mCurTransaction.commitAllowingStateLoss();
//            mCurTransaction = null;
//            mFragmentManager.executePendingTransactions();
//        }
//    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    public void setC(String c) {
//        Log.d(TAG, "setC() c:" + c);
        this.c = c;
        this.notifyDataSetChanged();
    }

    public String getC() {
        return this.c;
    }

    public void setPix(String pix) {
//        Log.d(TAG, "setPix() pix:" + pix);
        this.pix = pix;
        this.notifyDataSetChanged();
    }

}
