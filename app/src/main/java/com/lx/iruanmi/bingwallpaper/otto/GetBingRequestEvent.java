package com.lx.iruanmi.bingwallpaper.otto;

import java.io.Serializable;

/**
 * Created by liuxue on 2015/4/28.
 */
public class GetBingRequestEvent implements Serializable {

    private static final long serialVersionUID = 0L;

    public /*final*/ String y;
    public /*final*/ String m;
    public /*final*/ String d;
    public /*final*/ String c;

    public GetBingRequestEvent(String y, String m, String d, String c) {
        this.y = y;
        this.m = m;
        this.d = d;
        this.c = c;
    }

    public String getYmd() {
        return y + '-' + m + '-' + d;
    }

    @Override
    public String toString() {
        return "DateEvent{" +
                "y='" + y + '\'' +
                ", m='" + m + '\'' +
                ", d='" + d + '\'' +
                ", c='" + c + '\'' +
                '}';
    }

    public void setYmd(String ymd) {
        String[] ymds = ymd.split("-");
        this.y = ymds[0];
        this.m = ymds[1];
        this.d = ymds[2];
    }
}
