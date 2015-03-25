package com.lx.iruanmi.bingwallpaper.util;

import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of ImageDownloader which uses {@link com.squareup.okhttp.OkHttpClient} for image stream retrieving.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class OkHttpImageDownloader extends BaseImageDownloader {

    private static final String TAG = "OkHttpImageDownloader";
    private OkHttpClient client;

    public OkHttpImageDownloader(Context context, OkHttpClient client) {
        super(context);
        this.client = client;
    }

    @Override
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        Log.d(TAG, "getStreamFromNetwork()");
        Request request = new Request.Builder().url(imageUri).build();
        return client.newCall(request).execute().body().byteStream();
    }
}
