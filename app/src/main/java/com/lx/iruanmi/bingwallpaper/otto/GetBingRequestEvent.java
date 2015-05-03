package com.lx.iruanmi.bingwallpaper.otto;

import com.lx.iruanmi.bingwallpaper.model.GetBingRequest;

import java.io.Serializable;

/**
 * Created by liuxue on 2015/4/28.
 */
public class GetBingRequestEvent {

    public GetBingRequest getBingRequest;

    public GetBingRequestEvent(GetBingRequest getBingRequest) {
        this.getBingRequest = getBingRequest;
    }
}
