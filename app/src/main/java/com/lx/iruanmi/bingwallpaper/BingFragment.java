package com.lx.iruanmi.bingwallpaper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;
import com.lx.iruanmi.bingwallpaper.eventbus.GetBingResponseEvent;
import com.lx.iruanmi.bingwallpaper.util.MobclickAgentHelper;
import com.lx.iruanmi.bingwallpaper.util.SystemUiHider;
import com.lx.iruanmi.bingwallpaper.util.Utility;
import com.lx.iruanmi.bingwallpaper.widget.BingHpBottomCellView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;
import de.greenrobot.event.EventBus;
import uk.co.senab.photoview.sample.HackyViewPager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link BingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingFragment extends Fragment {

    private static final String TAG = BingFragment.class.getSimpleName();

    // parameter arguments
    // the fragment initialization parameters
    private static final String ARG_GET_BING_REQUEST = "GetBingRequestEvent";

    private static final String STATE_GET_BING_REQUEST = "bing_fragment_get_bing_request";

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
    private GetBingRequest mGetBingRequest;

    @InjectView(R.id.viewBingHpBottomCellView)
    BingHpBottomCellView viewBingHpBottomCellView;
    @InjectView(R.id.viewViewPager)
    HackyViewPager viewViewPager;

    BingPagerAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    public BingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param getBingRequest Parameter 2.
     * @return A new instance of fragment BingFragment.
     */
    public static BingFragment newInstance(GetBingRequest getBingRequest) {
        BingFragment fragment = new BingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GET_BING_REQUEST, getBingRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() this:" + this);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGetBingRequest = (GetBingRequest) getArguments().getSerializable(ARG_GET_BING_REQUEST);
        }

        if (savedInstanceState != null) {
            mGetBingRequest = (GetBingRequest) savedInstanceState.getSerializable(STATE_GET_BING_REQUEST);
        }

        View contentView = getActivity().getWindow().getDecorView();
        mSystemUiHider = SystemUiHider.getInstance(getActivity(), contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider.show();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("GetBingRequest", mGetBingRequest != null ? mGetBingRequest.toString() : null);
        MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_ONCREATE, map);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() this:" + this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume() this:" + this);
        super.onResume();

        MobclickAgent.onPageStart(TAG); //统计页面

//        BusProvider.getInstance().register(this);
        EventBus.getDefault().register(this);

        mSystemUiHider.show();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() this:" + this);
        super.onPause();

        MobclickAgent.onPageEnd(TAG);

//        BusProvider.getInstance().unregister(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() this:" + this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bing, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

//        ((BingActivity) activity).onSectionAttached(
//                (GetBingRequest) getArguments().getSerializable(ARG_GET_BING_REQUEST));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_GET_BING_REQUEST, mGetBingRequest);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated() this:" + this);
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.inject(this, view);

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
//                            viewBingHpBottomCellView.viewBingMusCardContentView.animate()
//                                    .translationY(visible ? 0 : mControlsHeight)
//                                    .setDuration(mShortAnimTime);
                            viewBingHpBottomCellView.viewBingMusCardContentView.animate()
                                    .translationY(visible ? 0 : (int)(mControlsHeight * 1.5))
                                    .setDuration((int)(mShortAnimTime * 1.5));
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            viewBingHpBottomCellView.viewBingMusCardContentView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible) {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                        } else {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                        }
                        viewViewPager.setLocked(!visible);
                        viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcFullSmall.setChecked(!visible);

                        if (mAdapter != null && viewViewPager != null) {
                            BingItemFragment fragment = (BingItemFragment) mAdapter.instantiateItem(viewViewPager, viewViewPager.getCurrentItem());
                            if (fragment != null) {
                                fragment.onSystemUiVisibilityChange(visible);
                            }
                        }

                        if (mListener != null) {
                            mListener.onBingFragmentSystemUiVisibilityChange(visible);
                        }

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
                String bingpix = viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcLandscapePortrait.isChecked() ? "1080x1920" : "1920x1080";
                mAdapter.setPix(bingpix);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("isChecked", String.valueOf(isChecked));
                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_CB_LANDSCAPE_PORTRAIT, map);
            }
        });

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

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("isChecked", String.valueOf(isChecked));
                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_CB_FULL_SMALL, map);
            }
        });

        viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewViewPager.setCurrentItem(viewViewPager.getCurrentItem() - 1, false);

                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_BTN_PREVIOUS);
            }
        });

        viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewViewPager.setCurrentItem(viewViewPager.getCurrentItem() + 1, false);

                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_BING_BTN_NEXT);
            }
        });

//        viewViewPager.setOffscreenPageLimit(1);
//        mAdapter = new BingPagerAdapter(getActivity(), getFragmentManager());
//        mAdapter.setC(mGetBingRequest.c);
//        String bingpix = viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcLandscapePortrait.isChecked() ? "1080x1920" : "1920x1080";
//        mAdapter.setPix(bingpix);
//        viewViewPager.setAdapter(mAdapter);
//        viewViewPager.setCurrentItem(Utility.getPositionMaxDate(getActivity(), mGetBingRequest.getYmd()));
    }



    @OnPageChange(R.id.viewViewPager)
    void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected() position:" + position);

        viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcPrevious.setEnabled(position > 0);
        viewBingHpBottomCellView.viewBingHpCtrlsView.btnHpcNext.setEnabled(position < mAdapter.getCount() -1);

        if (mListener != null) {
            mListener.onBingFragmentGetBingRequest(mAdapter.getGetBingRequest(position));
        }
//        Activity activity = getActivity();
//        ((BingActivity) activity).onSectionAttached(mAdapter.getGetBingRequest(position));
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void bind(GetBingRequest getBingRequest) {
        Log.d(TAG, "bind() getBingRequest:" + getBingRequest);
        Log.d(TAG, "bind() mAdapter:" + mAdapter);
        Log.d(TAG, "bind() viewViewPager:" + viewViewPager);
        mGetBingRequest = getBingRequest;

        if (mAdapter == null) {
            mAdapter = new BingPagerAdapter(getActivity(), getFragmentManager());
            mAdapter.setC(mGetBingRequest.c);
            String bingpix = viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcLandscapePortrait.isChecked() ? "1080x1920" : "1920x1080";
            mAdapter.setPix(bingpix);
            viewViewPager.setOffscreenPageLimit(1);
            viewViewPager.setAdapter(mAdapter);
        }

        if (!mAdapter.getC().equals(getBingRequest.c)) {
            mAdapter.setC(getBingRequest.c);
        }

        viewViewPager.setCurrentItem(Utility.getPositionMaxDate(getActivity(), getBingRequest.getYmd()), false);
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
    public interface OnFragmentInteractionListener {
        //        public void onFragmentInteraction(Uri uri);
        void onBingFragmentSystemUiVisibilityChange(boolean visible);
        void onBingFragmentGetBingRequest(GetBingRequest getBingRequest);
    }

//    @Subscribe
    public void onEventMainThread(GetBingResponseEvent event) {
        Log.d(TAG, "onEventMainThread() GetBingResponseEvent:" + event);

        viewBingHpBottomCellView.bind(event.getBingRequest.getYmd(), event.bing);

        final Bing bing = event.bing;

        if (bing == null) {
            return;
        }

        String bingpix = viewBingHpBottomCellView.viewBingHpCtrlsView.cbHpcLandscapePortrait.isChecked() ? bing.bing_9x16 : bing.bing_16x9;
        final String url = getString(R.string.bing_url_formate, getString(R.string.bing_host), bing.getBing_picname(), bingpix);
        Log.d(TAG, "url:" + url);
        final String subPath = url.substring(url.lastIndexOf('/'));

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

                Toast.makeText(getActivity(), getString(R.string.download_start), Toast.LENGTH_LONG).show();
            }
        });
    }
}
