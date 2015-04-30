package com.lx.iruanmi.bingwallpaper.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lx.iruanmi.bingwallpaper.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by liuxue on 2015/4/30.
 */
public class BingItem extends FrameLayout {

    @InjectView(R.id.viewPhotoView)
    PhotoView viewPhotoView;

    public BingItem(Context context) {
        super(context);
        init(context);
    }

    public BingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BingItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_pager_item_bing, this);
        ButterKnife.inject(this);
    }
}
