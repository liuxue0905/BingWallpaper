package com.lx.iruanmi.bingwallpaper;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lx.iruanmi.bingwallpaper.util.Utility;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by liuxue on 2015/4/30.
 */
public class BingPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "BingPagerAdapter";

    private Context context;

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
        Log.d(TAG, "getItem() position:" + position);

        String[] ymds = Utility.positionToYmds(context, position);

        String y = ymds[0];
        String m = ymds[1];
        String d = ymds[2];
        String c = "ZH-CN";
        return BingItemFragment.newInstance(position, y, m, d, c);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

}
