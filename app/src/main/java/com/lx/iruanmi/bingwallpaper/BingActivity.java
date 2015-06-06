package com.lx.iruanmi.bingwallpaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;
import com.lx.iruanmi.bingwallpaper.util.Utility;
import com.lx.iruanmi.bingwallpaper.widget.HackyDrawerLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import butterknife.ButterKnife;

public class BingActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, BingFragment.OnFragmentInteractionListener {

    private static final String TAG = "BingActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private BingFragment mBingFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing);

        mContext = this;

        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mBingFragment = (BingFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_bing);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (HackyDrawerLayout) findViewById(R.id.drawer_layout));

//        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mBingFragment.bind(mNavigationDrawerFragment.getGetBingRequest());

        UmengUpdateAgent.setSlotId("65102");
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setUpdateListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    public void onNavigationDrawerItemSelected(GetBingRequest getBingRequest) {
        Log.d(TAG, "onNavigationDrawerItemSelected() GetBingRequest:" + getBingRequest);

        onSectionAttached(getBingRequest);

        if (mBingFragment != null) {
            mBingFragment.bind(getBingRequest);
            return;
        }
    }

    public void onSectionAttached(GetBingRequest getBingRequest) {
        Log.d(TAG, "onSectionAttached() getBingRequest:" + getBingRequest);

        String[] cArray = getResources().getStringArray(R.array.c);
        String[] cDisplayArray = getResources().getStringArray(R.array.c_display);

        String cDisplay = cDisplayArray[Utility.indexOf(cArray, getBingRequest.c)];

        mTitle = getString(R.string.title_activity_bing_formate, getBingRequest.getYmd(), cDisplay);

        if (getSupportActionBar() != null) {
            restoreActionBar();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.bing, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_update) {
            UmengUpdateAgent.setSlotId("65102");
            UmengUpdateAgent.setUpdateOnlyWifi(false);
            UmengUpdateAgent.forceUpdate(this);
            UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

                @Override
                public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                    Log.d(TAG, "onUpdateReturned() i:" + i + ",updateResponse:" + updateResponse);
                    switch (i) {
                        case UpdateStatus.Yes: // has update
                            UmengUpdateAgent.showUpdateDialog(mContext, updateResponse);
                            break;
                        case UpdateStatus.No: // has no update
                            Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
                            break;
                        case UpdateStatus.NoneWifi: // none wifi
                            Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                            break;
                        case UpdateStatus.Timeout: // time out
                            Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            return true;
        } else if (id == R.id.action_doudoublog) {
            Utility.actionDoudouBlog(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBingFragmentSystemUiVisibilityChange(boolean visible) {
        Log.d(TAG, "onBingFragmentSystemUiVisibilityChange() visible:" + visible);
        ((HackyDrawerLayout) findViewById(R.id.drawer_layout)).setLocked(!visible);
    }

    @Override
    public void onBingFragmentGetBingRequest(GetBingRequest getBingRequest) {
        mNavigationDrawerFragment.bind(getBingRequest);
        onSectionAttached(getBingRequest);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            ((HackyDrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
