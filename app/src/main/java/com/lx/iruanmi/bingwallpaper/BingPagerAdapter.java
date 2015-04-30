package com.lx.iruanmi.bingwallpaper;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.lx.iruanmi.bingwallpaper.util.Utility;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by liuxue on 2015/4/30.
 */
public class BingPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return Utility.DAYS_SHOW;
    }

    private static final int[] sDrawables = { R.drawable.hpc_down_normal, R.drawable.hpc_full_normal, R.drawable.hpc_landscape_normal,
            R.drawable.hpc_next_normal, R.drawable.hpc_portrait_normal, R.drawable.hpc_previous_normal, R.drawable.hpc_small_normal };

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setImageResource(sDrawables[position % sDrawables.length]);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
