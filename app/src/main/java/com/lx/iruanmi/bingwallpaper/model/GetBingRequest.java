package com.lx.iruanmi.bingwallpaper.model;

import java.io.Serializable;

/**
 * Created by liuxue on 2015/5/2.
 */
public class GetBingRequest implements Serializable {

    private static final long serialVersionUID = 0L;

    public String y;
    public String m;
    public String d;
    public String c;

    public GetBingRequest(String y, String m, String d, String c) {
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
        return "GetBingRequest{" +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetBingRequest)) return false;

        GetBingRequest that = (GetBingRequest) o;

        if (c != null ? !c.equals(that.c) : that.c != null) return false;
        if (d != null ? !d.equals(that.d) : that.d != null) return false;
        if (m != null ? !m.equals(that.m) : that.m != null) return false;
        if (y != null ? !y.equals(that.y) : that.y != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = y != null ? y.hashCode() : 0;
        result = 31 * result + (m != null ? m.hashCode() : 0);
        result = 31 * result + (d != null ? d.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        return result;
    }
}
