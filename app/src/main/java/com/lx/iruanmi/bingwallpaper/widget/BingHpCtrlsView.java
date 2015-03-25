package com.lx.iruanmi.bingwallpaper.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.lx.iruanmi.bingwallpaper.R;
import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.util.Utility;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;

import java.util.TimeZone;

/**
 * TODO: document your custom view class.
 */
public class BingHpCtrlsView extends LinearLayout {

    private static final String TAG = "BingHpCtrlsView";

    public CheckBox cbHpcFullSmall;
    public CheckBox cbHpcLandscapePortrait;
    public Button btnHpcPrevious;
    public Button btnHpcNext;
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
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_hp_ctrls, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        cbHpcFullSmall = (CheckBox) findViewById(R.id.cbHpcFullSmall);
        cbHpcLandscapePortrait = (CheckBox) findViewById(R.id.cbHpcLandscapePortrait);
        btnHpcPrevious = (Button) findViewById(R.id.btnHpcPrevious);
        btnHpcNext = (Button) findViewById(R.id.btnHpcNext);
        btnHpcDown = (Button) findViewById(R.id.btnHpcDown);
    }

    public void bind(String date, Bing bing) {
        cbHpcFullSmall.setEnabled(bing != null);
        btnHpcDown.setEnabled(bing != null && !TextUtils.isEmpty(bing.bing_picname));

        DateTime nowDateTime = DateTime.now();
        DateTime dateDateTime = DateTimeFormat.forPattern(getResources().getString(R.string.bing_date_formate)).parseDateTime(date);
        DateTime minDateTime = new DateTime(Utility.getMinDate());

        btnHpcNext.setEnabled(!Days.ZERO.equals(Days.daysBetween(nowDateTime, dateDateTime)));
        btnHpcPrevious.setEnabled(!Days.ZERO.equals(Days.daysBetween(minDateTime, dateDateTime)));

        if (bing == null) {
            return;
        }
    }
}
