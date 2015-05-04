package com.lx.iruanmi.bingwallpaper.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.lx.iruanmi.bingwallpaper.R;
import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.util.Utility;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * TODO: document your custom view class.
 */
public class BingHpCtrlsView extends LinearLayout {

    private static final String TAG = "BingHpCtrlsView";

    @InjectView(R.id.cbHpcFullSmall)
    public CheckBox cbHpcFullSmall;
    @InjectView(R.id.cbHpcLandscapePortrait)
    public CheckBox cbHpcLandscapePortrait;
    @InjectView(R.id.btnHpcPrevious)
    public Button btnHpcPrevious;
    @InjectView(R.id.btnHpcNext)
    public Button btnHpcNext;
    @InjectView(R.id.btnHpcDown)
    public Button btnHpcDown;

    public BingHpCtrlsView(Context context) {
        super(context);
        init(null, 0);
    }

    public BingHpCtrlsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }


    public BingHpCtrlsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BingHpCtrlsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, -1);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_hp_ctrls, this);
        ButterKnife.inject(this);
    }

    public void bind(String date, Bing bing) {
        cbHpcFullSmall.setEnabled(bing != null);
        btnHpcDown.setEnabled(bing != null && !TextUtils.isEmpty(bing.bing_picname));
    }
}
