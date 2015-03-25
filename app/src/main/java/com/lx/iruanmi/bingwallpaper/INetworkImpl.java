package com.lx.iruanmi.bingwallpaper;

import com.lx.iruanmi.bingwallpaper.db.Bing;

import retrofit.RestAdapter;
import retrofit.http.Path;

/**
 * Created by liuxue on 2015/3/9.
 */
public class INetworkImpl implements INetwork {

//    private static INetworkImpl ourInstance = new INetworkImpl();
//
//    public static INetworkImpl getInstance() {
//        return ourInstance;
//    }

    private INetwork mINetwork;

    public INetworkImpl() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://www.iruanmi.com/wp-admin/")
//                .setClient(new OkClient(okHttpClient))
//                .setConverter(new SimpleXMLConverter())
//                .setExecutors(Executors.newSingleThreadExecutor(), new MainThreadExecutor())
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestFacade request) {
//                        request.addHeader("Cache-Control", "public, max-age=900");
//                    }
//                })
                .build();

        mINetwork = restAdapter.create(INetwork.class);
    }

    @Override
    public Bing adminAjax(@Path("action") String action, @Path("y") String y, @Path("m") String m, @Path("d") String d, @Path("c") String c) {

        Bing bing = mINetwork.adminAjax(action, y, m, d, c);

        return bing;
    }
}
