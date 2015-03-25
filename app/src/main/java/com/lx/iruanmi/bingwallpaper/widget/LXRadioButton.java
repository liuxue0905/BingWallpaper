package com.lx.iruanmi.bingwallpaper.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;

/**
 * Created by liuxue on 2015/3/12.
 */
public class LXRadioButton extends RadioButton {

    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public LXRadioButton(Context context) {
        super(context);
    }

    public LXRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LXRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LXRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void setOnCheckedChangeWidgetListener(CompoundButton.OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        if (mOnCheckedChangeWidgetListener != null) {
            mOnCheckedChangeWidgetListener.onCheckedChanged(this, checked);
        }
    }
}
