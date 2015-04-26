package com.lx.iruanmi.bingwallpaper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lx.iruanmi.bingwallpaper.R;
import com.lx.iruanmi.bingwallpaper.db.Bing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * TODO: document your custom view class.
 */
public class BingMusCardView extends LinearLayout {

    private static final String TAG = "BingMusCardContentView";

    @InjectView(R.id.tvTitle)
    public TextView tvTitle;
    @InjectView(R.id.tvCopyright)
    public TextView tvCopyright;

    public BingMusCardView(Context context) {
        super(context);
        init(null, 0);
    }

    public BingMusCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BingMusCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_bing_muscard, this);
        ButterKnife.inject(this);
    }

    public void bind(Bing bing) {
        if (bing == null) {
            return;
        }

        String str = bing.bing_copyright;

        Pattern p = Pattern.compile("(.*)(\\(\\u00a9 .+\\))");
        Matcher m = p.matcher(bing.getBing_copyright());
        if (m.find()) {
            Log.d(TAG, "m.groupCount():" + m.groupCount());
            Log.d(TAG, "m.group(0):" + m.group(0));
            Log.d(TAG, "m.group(1):" + m.group(1));
            Log.d(TAG, "m.group(2):" + m.group(2));
            tvTitle.setText(m.group(1));
            tvCopyright.setText(m.group(2));
            tvTitle.setVisibility(View.VISIBLE);
            tvCopyright.setVisibility(View.VISIBLE);
        }
    }
}
