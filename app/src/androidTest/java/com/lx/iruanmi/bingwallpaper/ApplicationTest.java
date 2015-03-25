package com.lx.iruanmi.bingwallpaper;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.db.BingDao;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @SmallTest
    public void testAdminAjax() {
        INetworkImpl i = new INetworkImpl();
        Bing bing = i.adminAjax("get_bing", "2015", "03", "09", "ROW");
        System.out.print("bing:" + bing);
        Log.d("LX", "bing:" + bing);
    }

    @SmallTest
    public void testDB() {
        BingDao bingDao = BingWallpaperApplication.getInstance().getDaoSession().getBingDao();

        INetworkImpl i = new INetworkImpl();

        Calendar calendarBegin = new GregorianCalendar(2014, 10 - 1, 1);
//        Calendar calendarEnd = new GregorianCalendar(2014, 12 - 1, 31);
//        Calendar calendarBegin = new GregorianCalendar(2015, 1 - 1, 1);
        Calendar calendarEnd = new GregorianCalendar(2015, 1 - 1, 31);

        while (!calendarBegin.after(calendarEnd)) {

            int year = calendarBegin.get(Calendar.YEAR);
            int monthOfYear = calendarBegin.get(Calendar.MONTH);
            int dayOfMonth = calendarBegin.get(Calendar.DAY_OF_MONTH);

            Bing bingZHCN = i.adminAjax("get_bing", String.valueOf(year), String.valueOf(monthOfYear + 1), String.valueOf(dayOfMonth), "ZH-CN");
            bingDao.insert(bingZHCN);

            Bing bingROW = i.adminAjax("get_bing", String.valueOf(year), String.valueOf(monthOfYear + 1), String.valueOf(dayOfMonth), "ROW");
            bingDao.insert(bingROW);

            calendarBegin.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}