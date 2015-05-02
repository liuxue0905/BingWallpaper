package com.lx.iruanmi.bingwallpaper.otto;

import com.lx.iruanmi.bingwallpaper.db.Bing;

/**
 * Created by liuxue on 2015/5/1.
 */
public class BingEvent {

    public final String y;
    public final String m;
    public final String d;
    public final String c;
    public final Bing bing;

    public BingEvent(String y, String m, String d, String c, Bing bing) {
        this.y = y;
        this.m = m;
        this.d = d;
        this.c = c;
        this.bing = bing;
    }
}
