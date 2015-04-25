package com.lx.iruanmi.bingwallpaper;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;

/**
 * Created by liuxue on 2015/4/7.
 */
public class BingConverter extends GsonConverter {

    public BingConverter(Gson gson) {
        super(gson);
    }

    public BingConverter(Gson gson, String charset) {
        super(gson, charset);
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
//        return super.fromBody(body, type);
        try {
            return super.fromBody(body, type);
        } catch (ConversionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
