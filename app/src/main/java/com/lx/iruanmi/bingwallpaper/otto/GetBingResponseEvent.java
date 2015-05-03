package com.lx.iruanmi.bingwallpaper.otto;

import com.lx.iruanmi.bingwallpaper.db.Bing;
import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;

/**
 * Created by liuxue on 2015/5/2.
 */
public class GetBingResponseEvent {

    public GetBingRequest getBingRequest;
    public Bing bing;

    public GetBingResponseEvent(GetBingRequest getBingRequest, Bing bing) {
        this.getBingRequest = getBingRequest;
        this.bing = bing;
    }
}
