package com.lx.iruanmi.bingwallpaper.db;

import android.util.Log;

import com.lx.iruanmi.bingwallpaper.BingWallpaperApplication;

/**
 * Created by liuxue on 2015/3/21.
 */
public class DBUtil {

    private static final String TAG = "DBUtil";

    public static long insertOrReplace(Bing bing) {
        BingDao bingDao = BingWallpaperApplication.getInstance().getBingDao();

//        long count = bingDao.queryBuilder().where(BingDao.Properties.ID.eq(bing.ID)).count();

//        if (count == 0) {
//            bingDao.insertOrReplace()
//        } else {
//
//        }

        return bingDao.insertOrReplace(bing);
    }

    public static Bing getBing(String ymd, String c) {
        Log.d(TAG, String.format("getBing() ymd:%s c:%s", ymd, c));
        BingDao bingDao = BingWallpaperApplication.getInstance().getBingDao();
        return bingDao.queryBuilder().where(BingDao.Properties.Bing_date.eq(ymd), BingDao.Properties.Bing_country.eq(c)).unique();
    }
}
