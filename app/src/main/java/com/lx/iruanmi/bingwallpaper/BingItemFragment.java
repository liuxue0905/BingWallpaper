package com.lx.iruanmi.bingwallpaper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.db.DBUtil;
import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;
import com.lx.iruanmi.bingwallpaper.eventbus.GetBingResponseEvent;
import com.lx.iruanmi.bingwallpaper.util.Utility;
import com.lx.iruanmi.bingwallpaper.widget.BingHudView;
import com.lx.iruanmi.bingwallpaper.widget.UserVisibleHintFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by liuxue on 2015/5/1.
 */
public class BingItemFragment extends UserVisibleHintFragment {

    private static final String TAG = "BingItemFragment";

    private static final String ARG_POSITION = "position";
    private static final String ARG_GET_BING_REQUEST = "DateEvent";
    private static final String ARG_BING_PIX = "bingpix";

    private int position;
    private GetBingRequest mGetBingRequest;
    private String bingpix;

    @InjectView(R.id.viewPhotoView)
    PhotoView viewPhotoView;
    @InjectView(R.id.viewBingHudView)
    BingHudView viewBingHudView;

    public static Fragment newInstance(int position, GetBingRequest getBingRequest, String bingpix) {
        Log.d(TAG, "newInstance() position:" + position);
        BingItemFragment f = new BingItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putSerializable(ARG_GET_BING_REQUEST, getBingRequest);
        args.putSerializable(ARG_BING_PIX, bingpix);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            mGetBingRequest = (GetBingRequest) getArguments().getSerializable(ARG_GET_BING_REQUEST);
            bingpix = getArguments().getString(ARG_BING_PIX);
        }
//        if (savedInstanceState != null) {
//
//        }
        Log.d(TAG, "onCreate() position:" + position + ",this:" + this);
    }

    private Bitmap mLoadedImage;

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() position:" + position + ",this:" + this);

        if (viewPhotoView != null) {
            viewPhotoView.setImageBitmap(null);
        }
        if (mLoadedImage != null && !mLoadedImage.isRecycled()) {
            mLoadedImage.recycle();
        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() position:" + position + ",this:" + this);
        return inflater.inflate(R.layout.fragment_bing_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated() position:" + position + ",this:" + this);
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        viewBingHudView.btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Utility.cancelTaskInterrupt(mGetBingTask);
                ImageLoader.getInstance().cancelDisplayTask(viewPhotoView);
                getBing();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(TAG); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(TAG);

        Utility.cancelTaskInterrupt(mGetBingTask);
        ImageLoader.getInstance().cancelDisplayTask(viewPhotoView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onVisible() {
        Log.d(TAG, "onVisible() position:" + position + ",this:" + this);
        Log.d(TAG, "onVisible() mGetBingRequest:" + mGetBingRequest);
        Log.d(TAG, "onVisible() mGetBingResponseEvent:" + mGetBingResponseEvent);
        Log.d(TAG, "onVisible() mLoadedImage:" + mLoadedImage);
        if (mLoadedImage != null) {
            Log.d(TAG, "onVisible() mLoadedImage.isRecycled():" + mLoadedImage.isRecycled());
        }

        if (mGetBingResponseEvent == null || mGetBingResponseEvent.bing == null) {
            mGetBingResponseEvent = new GetBingResponseEvent(mGetBingRequest, null);
            EventBus.getDefault().post(mGetBingResponseEvent);

            getBing();
        } else {
            if (mLoadedImage == null || mLoadedImage.isRecycled()) {
                loadBingPicture(mGetBingResponseEvent.bing);
            }
            EventBus.getDefault().post(mGetBingResponseEvent);
        }
    }

    @Override
    protected void onInVisible() {
        Log.d(TAG, "onInVisible() position:" + position + ",this:" + this);

        if (mGetBingResponseEvent == null || mGetBingResponseEvent.bing == null || mLoadedImage == null || mLoadedImage.isRecycled()) {
            Utility.cancelTaskInterrupt(mGetBingTask);
            ImageLoader.getInstance().cancelDisplayTask(viewPhotoView);
            viewBingHudView.onBingPreExecute();
        }

        viewPhotoView.setScale(1.0F, true);
    }

    private GetBingTask mGetBingTask;

    private GetBingResponseEvent mGetBingResponseEvent;

    private void getBing() {
        mGetBingTask = new GetBingTask(mGetBingRequest);
        mGetBingTask.execute();
    }

    public void bind(GetBingRequest getBingRequest) {
        Log.d(TAG, "bind() position:" + position + ",this:" + this);
    }

    public void onSystemUiVisibilityChange(boolean visible) {
        if (viewPhotoView != null) {
            if (visible) {
                viewPhotoView.setScale(1.0F, true);
            }
        }
    }

    private class GetBingTask extends AsyncTask<Void, Void, Bing> {

        private GetBingRequest getBingRequest;

        private GetBingTask(GetBingRequest getBingRequest) {
            this.getBingRequest = getBingRequest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            viewBingHudView.onBingPreExecute();
        }

        @Override
        protected Bing doInBackground(Void... params) {
            try {
                DateTime dateTimeZHCN = Utility.getDateTimeLocalToZHCN(getActivity(), getBingRequest.getYmd());
                String dateZHCN = dateTimeZHCN.toString(getString(R.string.bing_date_formate));

                Bing bing = DBUtil.getBing(dateZHCN, getBingRequest.c);

                if (bing == null) {
                    String y = String.valueOf(dateTimeZHCN.getYear());
                    String m = String.valueOf(dateTimeZHCN.getMonthOfYear());
                    String d = String.valueOf(dateTimeZHCN.getDayOfMonth());

                    INetwork network = new INetworkImpl();
                    bing = network.adminAjax("get_bing", y, m, d, getBingRequest.c);
                    DBUtil.insertOrReplace(bing);
                }
                return bing;
            } catch (Exception e) {
                e.printStackTrace();
                final Exception fe = e;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(fe);
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bing bing) {
            super.onPostExecute(bing);

            if (isCancelled()) {
                viewBingHudView.onBingCancelled();
                return;
            }

            mGetBingResponseEvent.bing = bing;
            EventBus.getDefault().post(mGetBingResponseEvent);

            if (bing == null) {
//                pb.setVisibility(View.GONE);
//                tvInfo.setVisibility(View.GONE);
                return;
            }

            loadBingPicture(bing);
        }

        void onPostExecute(Exception e) {
            if (isCancelled()) {
                return;
            }

            viewBingHudView.onBingException();

            if (e instanceof retrofit.RetrofitError) {
                retrofit.RetrofitError re = (retrofit.RetrofitError) e;

                switch (re.getKind()) {
                    case CONVERSION:
//                        boolean isBingUpdated = Utility.isBingUpdated(getActivity(), mDate);
//                        Log.d(TAG, "isBingUpdated:" + isBingUpdated);

                        DateTime updateDateTimeZHCN = Utility.getUpdateDateTimeZHCN();
                        DateTime updateDateTimeDefault = updateDateTimeZHCN.toDateTime(DateTimeZone.getDefault());

                        Log.d(TAG, "updateDateTimeZHCN:" + updateDateTimeZHCN);
                        Log.d(TAG, "updateDateTimeDefault:" + updateDateTimeDefault);

                        String updateDateTimeString = updateDateTimeDefault.toString("HH:mm");
                        Toast.makeText(getActivity(), getString(R.string.bing_updates_tips, updateDateTimeString), Toast.LENGTH_LONG).show();

                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void loadBingPicture(final Bing bing) {
        Log.d(TAG, "loadBingPicture() viewPhotoView:" + viewPhotoView);



        if (bing == null) {
            return;
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub)
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
//                .showImageOnLoading(R.drawable.ic_image_loader_loading)
//                .showImageForEmptyUri(R.drawable.ic_image_loader_fail)
//                .showImageOnFail(R.drawable.ic_image_loader_fail)
//                .cacheInMemory(false)
                .cacheOnDisk(true)
//                .considerExifParams(true)
                .build();

        String bingpix = getBingPix(this.bingpix, bing);
        final String url = getString(R.string.bing_url_formate, getString(R.string.bing_host), bing.getBing_picname(), bingpix);
        Log.d(TAG, "loadBingPicture() url:" + url);

        ImageLoader.getInstance().displayImage(url, viewPhotoView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d(TAG, "ImageLoadingListener.onLoadingStarted()");
                super.onLoadingStarted(imageUri, view);

                viewBingHudView.onLoadingStarted(imageUri, view);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d(TAG, "ImageLoadingListener.onLoadingFailed()");
                super.onLoadingFailed(imageUri, view, failReason);

//                viewPhotoView.setImageResource(R.drawable.ic_image_loader_fail);
                viewBingHudView.onLoadingFailed(imageUri, view, failReason);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d(TAG, "ImageLoadingListener.onLoadingComplete()");
                Log.d(TAG, "ImageLoadingListener.onLoadingComplete() loadedImage:" + loadedImage);
                if (loadedImage != null) {
                    Log.d(TAG, "ImageLoadingListener.onLoadingComplete() loadedImage.isRecycled():" + loadedImage.isRecycled());
                }
                super.onLoadingComplete(imageUri, view, loadedImage);

                mLoadedImage = loadedImage;
                viewPhotoView.setImageBitmap(loadedImage);
                viewBingHudView.onLoadingComplete(imageUri, view, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d(TAG, "ImageLoadingListener.onLoadingCancelled()");
                super.onLoadingCancelled(imageUri, view);

                if (mGetBingResponseEvent != null && mGetBingResponseEvent.bing != null) {
                    if (mLoadedImage != null && !mLoadedImage.isRecycled()) {
                        return;
                    }
                }

//                viewPhotoView.setImageResource(R.drawable.ic_image_loader_fail);
                viewBingHudView.onLoadingCancelled(imageUri, view);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                Log.d(TAG, String.format("onProgressUpdate() current:%d total:%d", current, total));
                Log.d(TAG, (int) (current * 100.0 / total) + "%");

                viewBingHudView.onProgressUpdate(imageUri, view, current, total);
            }
        });

    }

    private static String getBingPix(String bingpix, Bing bing) {
//        Log.d(TAG, "getBingPix() bingpix:" + bingpix);
//        Log.d(TAG, "getBingPix() bing:" + bing);
        if (!bingpix.equals(bing.bing_9x16) && !bingpix.equals(bing.bing_16x9)) {
            String[] wh = bingpix.split("x");
            int w = Integer.parseInt(wh[0]);
            int h = Integer.parseInt(wh[1]);
            if (w < h) {
                return bing.bing_9x16;
            } else if (w > h) {
                return bing.bing_16x9;
            }
        }
        return bingpix;
    }

//    public void onEventMainThread(FullSmallEvent event) {
//        if (!event.isFull) {
//            viewPhotoView.setScale(1.0F, true);
//        }
//    }
}
