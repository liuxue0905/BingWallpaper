package com.lx.iruanmi.bingwallpaper.eventbus;

import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;

/**
 * Created by liuxue on 2015/4/28.
 */
public class GetBingRequestEvent {

    public GetBingRequest getBingRequest;

    public GetBingRequestEvent(GetBingRequest getBingRequest) {
        this.getBingRequest = getBingRequest;
    }
}
