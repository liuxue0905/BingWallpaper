package com.lx.iruanmi.bingwallpaper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.db.DBUtil;
import com.lx.iruanmi.bingwallpaper.util.MobclickAgentHelper;
import com.lx.iruanmi.bingwallpaper.util.SystemUiHider;
import com.lx.iruanmi.bingwallpaper.util.Utility;
import com.lx.iruanmi.bingwallpaper.widget.BingHpBottomCellView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;

import uk.co.senab.photoview.PhotoView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.lx.iruanmi.bingwallpaper.BingFragment.OnBingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingFragment extends Fragment {

    private static final String TAG = BingFragment.class.getSimpleName();

    // parameter arguments
    // the fragment initialization parameters
    private static final String ARG_COUNTRY = "country";
    private static final String ARG_DATE = "date";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = false;
    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
//    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_FULLSCREEN & ~SystemUiHider.FLAG_HIDE_NAVIGATION;
    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };
    // parameters
    private String mCountry;
    private String mDate;
    private PhotoView viewPhotoView;
    private ProgressBar pb;
    private Button btnRefresh;
    private TextView tvProgress;
    private BingHpBottomCellView viewBingHpBottomCellView;
    private OnBingFragmentInteractionListener mListener;
    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    //    private Bing mBing;
    private GetBingTask mGetBingTask;

    public BingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date    Parameter 2.
     * @param country Parameter 1.
     * @return A new instance of fragment BingFragment.
     */
    public static BingFragment newInstance(String date, String country) {
        BingFragment fragment = new BingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_COUNTRY, country);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCountry = getArguments().getString(ARG_COUNTRY);
            mDate = getArguments().getString(ARG_DATE);
        }

        View contentView = getActivity().getWindow().getDecorView();
        mSystemUiHider = SystemUiHider.getInstance(getActivity(), contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.show();

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("country", mCountry);
        map.put("date", mDate);
        MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_ONCREATE, map);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bing, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Utility.cancelTaskInterrupt(mGetBingTask);
        mGetBingTask = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPhotoView = (PhotoView) view.findViewById(R.id.viewPhotoView);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
        tvProgress = (TextView) view.findViewById(R.id.tvProgress);
        viewBingHpBottomCellView = (BingHpBottomCellView) view.findViewById(R.id.viewBingHpBottomCellView);


        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = viewBingHpBottomCellView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            viewBingHpBottomCellView.viewBingMusCardContentView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            viewBingHpBottomCellView.viewBingMusCardContentView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible) {
                            ((ActionBarActivity) getActivity()).getSupportActionBar().show();
                        } else {
                            ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
                        }
                        viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcFullSmall.setChecked(!visible);

                        mListener.onBingFragmentSystemUiVisibilityChange(visible);

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
//                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });
        mSystemUiHider.show();

        viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcLandscapePortrait.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getBing();

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("isChecked", String.valueOf(isChecked));
                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_CB_LANDSCAPE_PORTRAIT, map);
            }
        });

        getBing();

        viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcFullSmall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    if (isChecked) {
                        mSystemUiHider.hide();
                    } else {
                        mSystemUiHider.show();
                    }
                }

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("isChecked", String.valueOf(isChecked));
                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_CB_FULL_SMALL, map);
            }
        });

        viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateTime dateTime = DateTimeFormat.forPattern(getString(R.string.bing_date_formate)).parseDateTime(mDate);
                Log.d(TAG, "dateTime:" + dateTime);
                dateTime = dateTime.minusDays(1);
                Log.d(TAG, "dateTime:" + dateTime);
                mListener.onBingFragmentDateChanged(dateTime.toString(getString(R.string.bing_date_formate)), mCountry);

                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_BTN_PREVIOUS);
            }
        });

        viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateTime dateTime = DateTimeFormat.forPattern(getString(R.string.bing_date_formate)).parseDateTime(mDate);
                Log.d(TAG, "dateTime:" + dateTime);
                dateTime = dateTime.plusDays(1);
                Log.d(TAG, "dateTime:" + dateTime);
                mListener.onBingFragmentDateChanged(dateTime.toString(getString(R.string.bing_date_formate)), mCountry);

                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_BTN_NEXT);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBing();
            }
        });
    }

    private void getBing() {
        Utility.cancelTaskInterrupt(mGetBingTask);
        mGetBingTask = new GetBingTask(mDate, mCountry);
        mGetBingTask.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        String country = getArguments().getString(ARG_COUNTRY);
        String date = getArguments().getString(ARG_DATE);
        Log.d(TAG, String.format("onAttach() date:%s country:%s", date, country));
        ((BingActivity) activity).onSectionAttached(date, country);

        try {
            mListener = (OnBingFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBingFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ImageLoader.getInstance().cancelDisplayTask(viewPhotoView);
    }

    private void loadBingPicture(final Bing bing) {
        if (bing == null) {
            return;
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub)
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
//                .cacheInMemory(true)
                .cacheOnDisk(true)
//                .considerExifParams(true)
                .build();

        String bingpix = viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcLandscapePortrait.isChecked() ? getString(R.string.bing_9x15) : getString(R.string.bing_15x9);
        final String url = getString(R.string.bing_url_formate, getString(R.string.bing_host), bing.getBing_picname(), bingpix);
        Log.d(TAG, "url:" + url);
        final String subPath = url.substring(url.lastIndexOf('/'));

//        boolean has = Utility.hasExternalStoragePublicPicture(subPath);
//        if (has) {
//            viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcDown.setEnabled(false);
//        }

        ImageLoader.getInstance().displayImage(url, viewPhotoView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);

                pb.setVisibility(View.VISIBLE);
                tvProgress.setVisibility(View.VISIBLE);
                tvProgress.setText(tvProgress.getContext().getString(R.string.pic_loading));
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);

                pb.setVisibility(View.GONE);
                tvProgress.setVisibility(View.VISIBLE);
                tvProgress.setText(tvProgress.getContext().getString(R.string.pic_loaded_failed));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);

                viewPhotoView.setImageBitmap(loadedImage);

                pb.setVisibility(View.GONE);
                tvProgress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);

                pb.setVisibility(View.GONE);
                tvProgress.setVisibility(View.GONE);
            }
        });

        viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_BTN_DOWN);

                DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                boolean has = Utility.hasExternalStoragePublicPicture(subPath);
                if (has) {
                    Toast.makeText(getActivity(), getString(R.string.download_successful), Toast.LENGTH_SHORT).show();
                    Utility.actionViewImage(getActivity(), subPath);
                    return;
                }

//                DownloadManager.Query query = new DownloadManager.Query();
//                query.setFilterByStatus(DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_SUCCESSFUL);
//                Cursor cursor = manager.query(query);
//                Log.d(TAG, "cursor:" + DatabaseUtils.dumpCursorToString(cursor));

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, subPath);
                request.setTitle(bing.bing_picname);
                request.setDescription(bing.bing_copyright);
                request.allowScanningByMediaScanner();
                request.setVisibleInDownloadsUi(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                manager.enqueue(request);
            }
        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBingFragmentInteractionListener {
        //        public void onBingFragmentInteraction(Uri uri);
        public void onBingFragmentDateChanged(String date, String country);

        public void onBingFragmentSystemUiVisibilityChange(boolean visible);
    }

    private class GetBingTask extends AsyncTask<Void, Void, Bing> {

        private String date;
        private String country;

        private GetBingTask(String date, String country) {
            this.date = date;
            this.country = country;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            btnRefresh.setVisibility(View.GONE);
            tvProgress.setVisibility(View.VISIBLE);
            tvProgress.setText(getString(R.string.bing_loading));
        }

        @Override
        protected Bing doInBackground(Void... params) {
            try {
                DateTime dateTimeZHCN = Utility.getDateTimeLocalToZHCN(getActivity(), mDate);
                String dateZHCN = dateTimeZHCN.toString(getString(R.string.bing_date_formate));

                Bing bing = DBUtil.getBing(dateZHCN, country);
                Log.d(TAG, "bing:" + bing);

                if (bing == null) {
                    String y = String.valueOf(dateTimeZHCN.getYear());
                    String m = String.valueOf(dateTimeZHCN.getMonthOfYear());
                    String d = String.valueOf(dateTimeZHCN.getDayOfMonth());

                    INetwork network = new INetworkImpl();
                    bing = network.adminAjax("get_bing", y, m, d, country);
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
                pb.setVisibility(View.INVISIBLE);
                tvProgress.setVisibility(View.GONE);
                return;
            }

            viewBingHpBottomCellView.bind(mDate, bing);

            if (bing == null) {
//                pb.setVisibility(View.GONE);
//                tvProgress.setVisibility(View.GONE);
                return;
            }

            loadBingPicture(bing);
        }

//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//
//            ImageLoader.getInstance().cancelDisplayTask(viewPhotoView);
//        }

        void onPostExecute(Exception e) {
            if (isCancelled()) {
                return;
            }

            pb.setVisibility(View.INVISIBLE);
            btnRefresh.setVisibility(View.VISIBLE);
            tvProgress.setVisibility(View.VISIBLE);
            tvProgress.setText(tvProgress.getContext().getString(R.string.bing_loaded_failed));

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

}
