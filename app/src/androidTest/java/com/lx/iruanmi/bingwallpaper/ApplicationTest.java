package com.lx.iruanmi.bingwallpaper;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.db.BingDao;
import com.lx.iruanmi.bingwallpaper.db.DBUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.TimeZone;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    INetworkImpl mINetwork;
    BingDao mBingDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

//        mINetwork = new INetworkImpl();
//        mBingDao = BingWallpaperApplication.getInstance().getDaoSession().getBingDao();
//        mBingDao = ((BingWallpaperApplication)getApplication()).getBingDao();
    }

    @SmallTest
    public void testAdminAjax() {
        INetworkImpl i = new INetworkImpl();
        Bing bing = i.adminAjax("get_bing", "2015", "03", "09", "ROW");
        System.out.print("bing:" + bing);
        Log.d("LX", "bing:" + bing);
    }

    private void adminAjaxGetBing(String ymd, String y, String m, String d, String c) {
        Bing bing = DBUtil.getBing(ymd, c);
        if (bing != null) {
            return;
        }

        try {
            bing = mINetwork.adminAjax("get_bing", y, m, d, c);
            mBingDao.insertOrReplace(bing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String DATE_ZHCN_MIN = "2013-07-19";
    public static final String DATE_ROW_MIN = "2013-07-18";

    @SmallTest
    public void testDB() {

        //Bing China 2013-07-19
        //Bing Global 2013-07-18

        mINetwork = new INetworkImpl();
        mBingDao = BingWallpaperApplication.getInstance().getBingDao();

        DateTime minDateTimeZHCN = DateTimeFormat.forPattern("yyyy-MM-dd")
                .withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+08:00")))
                .parseDateTime(DATE_ROW_MIN);
        DateTime nowDateTimeZHCN = DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+08:00")));
        DateTime currentDateTimeZHCN = minDateTimeZHCN;

        while (!currentDateTimeZHCN.isAfter(nowDateTimeZHCN)) {

            String ymd = currentDateTimeZHCN.toString("yy-MM-dd");
            String y = String.valueOf(currentDateTimeZHCN.getYear());
            String m = String.valueOf(currentDateTimeZHCN.getMonthOfYear());
            String d = String.valueOf(currentDateTimeZHCN.getDayOfMonth());

            adminAjaxGetBing(ymd, y, m, d, "ZH-CN");
            adminAjaxGetBing(ymd, y, m, d, "ROW");

            currentDateTimeZHCN = currentDateTimeZHCN.plusDays(1);
        }
    }
}