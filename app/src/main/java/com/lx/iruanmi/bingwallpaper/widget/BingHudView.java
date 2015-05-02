package com.lx.iruanmi.bingwallpaper.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lx.iruanmi.bingwallpaper.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liuxue on 2015/4/30.
 */
public class BingHudView extends FrameLayout implements ImageLoadingListener, ImageLoadingProgressListener {

    @InjectView(R.id.layoutProgress)
    public View layoutProgress;
    @InjectView(R.id.pb)
    public ProgressBar pb;
    @InjectView(R.id.btnRefresh)
    public Button btnRefresh;
    @InjectView(R.id.tvInfo)
    public TextView tvInfo;
    @InjectView(R.id.tvProgress)
    public TextView tvProgress;

    public BingHudView(Context context) {
        super(context);
        init(context);
    }

    public BingHudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BingHudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BingHudView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_bing_hud, this);
        ButterKnife.inject(this);
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        this.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        tvProgress.setVisibility(View.VISIBLE);
        tvProgress.setText(null);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(tvInfo.getContext().getString(R.string.pic_loading));
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        this.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(tvInfo.getContext().getString(R.string.pic_loaded_failed));
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        this.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);
        tvInfo.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        this.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);
        tvInfo.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdate(String imageUri, View view, int current, int total) {
        this.setVisibility(View.VISIBLE);
        tvProgress.setText((int)(current * 100.0 / total) + "%");
    }

    public void onBingPreExecute() {
        this.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.GONE);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(getResources().getString(R.string.bing_loading));
    }

    public void onBingException() {
        this.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(getResources().getString(R.string.bing_loaded_failed));
    }

    public void onBingCancelled() {
        this.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.GONE);
        pb.setVisibility(View.INVISIBLE);
        tvInfo.setVisibility(View.GONE);
    }
}
