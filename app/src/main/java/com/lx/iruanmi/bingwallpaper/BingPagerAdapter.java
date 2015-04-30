package com.lx.iruanmi.bingwallpaper;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by liuxue on 2015/4/30.
 */
public class BingPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
//        photoView.setImageResource(sDrawables[position]);

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
