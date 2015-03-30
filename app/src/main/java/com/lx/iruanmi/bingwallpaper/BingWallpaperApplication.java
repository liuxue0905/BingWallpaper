package com.lx.iruanmi.bingwallpaper;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lx.iruanmi.bingwallpaper.db.BingDao;
import com.lx.iruanmi.bingwallpaper.db.DaoMaster;
import com.lx.iruanmi.bingwallpaper.db.DaoSession;
import com.lx.iruanmi.bingwallpaper.util.OkHttpImageDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.okhttp.OkHttpClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UpdateConfig;

/**
 * Created by liuxue on 2015/3/15.
 */
public class BingWallpaperApplication extends Application {

    private static BingWallpaperApplication ourInstance = null;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private BingDao bingDao;

    public static BingWallpaperApplication getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ourInstance = this;

//        UpdateConfig.setDebug(true);
//        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);

        initImageLoader(getApplicationContext());

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bing-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        bingDao = daoSession.getBingDao();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ourInstance = null;

        daoSession.clear();
        daoMaster = null;
        db.close();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.imageDownloader(new OkHttpImageDownloader(context, new OkHttpClient()));
//        config.defaultDisplayImageOptions();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public BingDao getBingDao() {
        return bingDao;
    }
}
