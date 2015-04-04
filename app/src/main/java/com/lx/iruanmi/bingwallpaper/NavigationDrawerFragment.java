package com.lx.iruanmi.bingwallpaper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lx.iruanmi.bingwallpaper.util.MobclickAgentHelper;
import com.lx.iruanmi.bingwallpaper.util.Utility;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();

//    /**
//     * Remember the position of the selected item.
//     */
//    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String STATE_SELECTED_COUNTRY = "selected_navigation_drawer_country";
    private static final String STATE_SELECTED_DATE = "selected_navigation_drawer_date";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private Spinner spinnerC;
    private Button btnDate;
    private CalendarView viewCanlendarView;
    private View mFragmentContainerView;

    private String mCurrentSelectedDate;
    private String mCurrentSelectedCountry;

    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedCountry = savedInstanceState.getString(STATE_SELECTED_COUNTRY);
            mCurrentSelectedDate = savedInstanceState.getString(STATE_SELECTED_DATE);
            mFromSavedInstanceState = true;
        } else {
            mCurrentSelectedCountry = getCountry(0);
            mCurrentSelectedDate = DateTime.now().toString(getString(R.string.bing_date_formate));
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面

        if (!isAdded()) {
            return;
        }

        updateWidgets();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        spinnerC = (Spinner) view.findViewById(R.id.spinnerC);
        viewCanlendarView = (CalendarView) view.findViewById(R.id.viewCanlendarView);
        btnDate = (Button) view.findViewById(R.id.btnDate);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.c_display, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerC.setAdapter(adapter);
//        spinnerC.setSelection(0);
        spinnerC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrentSelectedCountry = getCountry(position);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("country", mCurrentSelectedCountry);
                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_NAVIGATION_DRAWER_FRAGMENT_COUNTRY, map);

                updateWidgets();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTime = DateTimeFormat.forPattern(getString(R.string.bing_date_formate)).parseDateTime(mCurrentSelectedDate);
                DateWidgetFragment fragment = DateWidgetFragment.newInstance(dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth(), getMinDate(), getMaxDate());
                fragment.setOnDateWidgetFragmentInteractionListener(new DateWidgetFragment.OnDateWidgetFragmentInteractionListener() {

                    @Override
                    public void onDateWidgetFragmentInteraction(int year, int monthOfYear, int dayOfMonth) {
                        if (!isAdded()) {
                            return;
                        }

                        mCurrentSelectedDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0).toString(getString(R.string.bing_date_formate));

                        MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_NAVIGATION_DRAWER_FRAGMENT_DATE_BUTTON);

                        updateWidgets();
                    }
                });
                fragment.show(getFragmentManager(), DateWidgetFragment.class.getName());
            }
        });

        viewCanlendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if (!isAdded()) {
                    return;
                }

                mCurrentSelectedDate = new DateTime(year, month + 1, dayOfMonth, 0, 0).toString(getString(R.string.bing_date_formate));

                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_NAVIGATION_DRAWER_DATE_CALENDAR);

                updateWidgets();
            }
        });

        updateWidgets();

        selectItem();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_NAVIGATION_DRAWER_ON_DRAWER_CLOSED);

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()

                selectItem();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                MobclickAgent.onEvent(getActivity(), MobclickAgentHelper.EVENT_ID_FRAGMENT_NAVIGATION_DRAWER_ON_DRAWER_OPENED);

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    //    private void selectItem(int positionC, int year, int monthOfYear, int dayOfMonth, int positionBing) {
    private void selectItem() {
//        mCurrentSelectedPosition = position;
//        if (mDrawerListView != null) {
//            mDrawerListView.setItemChecked(position, true);
//        }
//        if (mDrawerLayout != null) {
//            mDrawerLayout.closeDrawer(mFragmentContainerView);
//        }

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(mCurrentSelectedDate, mCurrentSelectedCountry);
        }
    }

    private String getCountry(int position) {
        String[] cArray = getResources().getStringArray(R.array.c);
        return cArray[position];
    }

    private String getCurrentCountry() {
        return getCountry(spinnerC.getSelectedItemPosition());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        outState.putString(STATE_SELECTED_COUNTRY, mCurrentSelectedCountry);
        outState.putString(STATE_SELECTED_DATE, mCurrentSelectedDate);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

//        if (item.getItemId() == R.id.action_example) {
//            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
//            return true;
//        }

//        else if (item.getItemId() == R.id.action_update) {
//            UmengUpdateAgent.setUpdateOnlyWifi(false);
//            UmengUpdateAgent.forceUpdate(getActivity());
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    private void updateWidgets() {
        Log.d(TAG, String.format("updateWidgets() mCurrentSelectedCountry:%s mCurrentSelectedDate:%s", mCurrentSelectedCountry, mCurrentSelectedDate));

        viewCanlendarView.setMinDate(getMinDate());
        viewCanlendarView.setMaxDate(getMaxDate());

        DateTime dateTime = DateTimeFormat.forPattern(getString(R.string.bing_date_formate)).parseDateTime(mCurrentSelectedDate);
        viewCanlendarView.setDate(dateTime.getMillis(), false, true);

        btnDate.setText(mCurrentSelectedDate);
    }

    private long getMinDate() {
        return Utility.getMinDate();
    }

    private long getMaxDate() {
//        return Utility.getMaxDate();
        return DateTime.now().getMillis();
    }

    public void setBingFragmentParams(String date, String country) {
        mCurrentSelectedDate = date;
        mCurrentSelectedCountry = country;

        updateWidgets();

        selectItem();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(String date, String country);
    }
}
