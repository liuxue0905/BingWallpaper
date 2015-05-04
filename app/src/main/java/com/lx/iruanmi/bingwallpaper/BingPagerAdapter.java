package com.lx.iruanmi.bingwallpaper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;
import com.lx.iruanmi.bingwallpaper.util.Utility;

/**
 * Created by liuxue on 2015/4/30.
 */
public class BingPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "BingPagerAdapter";

    private Context context;
    private String c;

    private String pix;

    public BingPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return Utility.DAYS_SHOW;
    }

    @Override
    public Fragment getItem(int position) {
//        Log.d(TAG, "getItem() position:" + position);
        String[] ymds = Utility.positionToYmds(context, position);
        String y = ymds[0];
        String m = ymds[1];
        String d = ymds[2];
        GetBingRequest getBingRequest = new GetBingRequest(y, m, d, this.c);
        return BingItemFragment.newInstance(position, getBingRequest, this.pix);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
////        return super.instantiateItem(container, position);
//        BingItemFragment f = (BingItemFragment) super.instantiateItem(container, position);
//        return f;
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

    public void setPix(String pix) {
//        Log.d(TAG, "setPix() pix:" + pix);
        this.pix = pix;
        this.notifyDataSetChanged();
    }
}
