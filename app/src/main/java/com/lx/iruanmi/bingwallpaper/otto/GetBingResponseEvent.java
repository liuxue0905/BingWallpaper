package com.lx.iruanmi.bingwallpaper.otto;

import com.lx.iruanmi.bingwallpaper.db.Bing;

/**
 * Created by liuxue on 2015/5/2.
 */
public class GetBingResponseEvent {

    public GetBingRequestEvent getBingRequestEvent;
    public Bing bing;

    public GetBingResponseEvent(GetBingRequestEvent getBingRequestEvent, Bing bing) {
        this.getBingRequestEvent = getBingRequestEvent;
        this.bing = bing;
    }
}
