package com.lx.iruanmi.bingwallpaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lx.iruanmi.bingwallpaper.util.Utility;
import com.lx.iruanmi.bingwallpaper.widget.HackyDrawerLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;


public class BingActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, BingFragment.OnBingFragmentInteractionListener {

    private static final String TAG = "BingActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (HackyDrawerLayout) findViewById(R.id.drawer_layout));

//        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
    public void onNavigationDrawerItemSelected(String date, String country) {
        Log.d(TAG, String.format("onNavigationDrawerItemSelected() date:%s country:%s", date, country));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, BingFragment.newInstance(date, country))
                .commit();
    }

    public void onSectionAttached(String date, String country) {
        Log.d(TAG, String.format("onSectionAttached() date:%s country:%s", date, country));

        String[] cArray = getResources().getStringArray(R.array.c);
        String[] cDisplayArray = getResources().getStringArray(R.array.c_display);

        String cDisplay = cDisplayArray[Utility.indexOf(cArray, country)];

        mTitle = getString(R.string.title_activity_bing_formate, date, cDisplay);

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
        }

        else if (id == R.id.action_update) {
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBingFragmentDateChanged(String date, String country) {
        mNavigationDrawerFragment.setBingFragmentParams(date, country);
    }

    @Override
    public void onBingFragmentSystemUiVisibilityChange(boolean visible) {
        ((HackyDrawerLayout) findViewById(R.id.drawer_layout)).setLocked(!visible);
    }
}
