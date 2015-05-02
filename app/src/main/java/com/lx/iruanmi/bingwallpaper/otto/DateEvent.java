package com.lx.iruanmi.bingwallpaper.otto;

/**
 * Created by liuxue on 2015/4/28.
 */
public class DateEvent {

    public final String ymd;

    public final String y;
    public final String m;
    public final String d;
    public final String c;

    public DateEvent(String y, String m, String d, String c) {
        this.y = y;
        this.m = m;
        this.d = d;
        this.c = c;

        this.ymd = y + '-' + m + '-' + d;
    }

    public DateEvent(String ymd, String c) {
        String[] ymds = ymd.split("-");

        this.y = ymds[0];
        this.m = ymds[1];
        this.d = ymds[2];
        this.c = c;

        this.ymd = ymd;
    }
}
