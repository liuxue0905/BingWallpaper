package com.lx.iruanmi.bingwallpaper;

import com.lx.iruanmi.bingwallpaper.db.Bing;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by liuxue on 2015/3/9.
 */
public interface INetwork {
    /**
     * "http://www.iruanmi.com/wp-admin//admin-ajax.php?action=get_bing&y=2015&m=03&d=09&c=ROW"
     *
     * @param action get_bing
     * @param y      2015
     * @param m      03
     * @param c      ZH-CN必应中国|ROW必应国际
     * @return {"ID":"1228","bing_date":"2015-03-06","bing_country":"ZH-CN","bing_picname":"\/az\/hprichbg\/rb\/DragonFlyBeijing_ZH-CN8555054089","bing_maxpix":"1920x1200","bing_16x9":"1920x1080","bing_9x16":"1080x1920","bing_9x15":"768x1280","bing_copyright":"\u873b\u8713 (\u00a9 Beijing, China)"}
     */
    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=2419200")
    @GET("/admin-ajax.php")
    Bing adminAjax(@Query("action") String action, @Query("y") String y, @Query("m") String m, @Query("d") String d, @Query("c") String c);
}
