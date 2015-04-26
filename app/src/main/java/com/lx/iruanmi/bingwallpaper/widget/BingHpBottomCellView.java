package com.lx.iruanmi.bingwallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lx.iruanmi.bingwallpaper.R;
import com.lx.iruanmi.bingwallpaper.db.Bing;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * TODO: document your custom view class.
 */
public class BingHpBottomCellView extends FrameLayout {

    @InjectView(R.id.viewBingMusCardContentView)
    public BingMusCardView viewBingMusCardContentView;
    @InjectView(R.id.viewBingHpCtrlsView)
    public BingHpCtrlsView viewBingHpCtrlsView;

    public BingHpBottomCellView(Context context) {
        super(context);
        init(null, 0);
    }

    public BingHpBottomCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BingHpBottomCellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_hp_bottom_cell, this);
        ButterKnife.inject(this);
    }

    public void bind(String date, Bing bing) {
        viewBingMusCardContentView.bind(bing);
        viewBingHpCtrlsView.bind(date, bing);
    }
}
