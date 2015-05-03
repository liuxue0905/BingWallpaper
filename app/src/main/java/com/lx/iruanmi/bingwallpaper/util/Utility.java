package com.lx.iruanmi.bingwallpaper.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.lx.iruanmi.bingwallpaper.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * Created by liuxue on 2015/3/15.
 */
public class Utility {

    private static final String TAG = "Utility";

    /**
     * Cancel an {@link AsyncTask}.  If it's already running, it'll be interrupted.
     */
    public static void cancelTaskInterrupt(AsyncTask<?, ?, ?> task) {
        cancelTask(task, true);
    }

    /**
     * Cancel an {@link AsyncTask}.
     *
     * @param mayInterruptIfRunning <tt>true</tt> if the thread executing this
     *                              task should be interrupted; otherwise, in-progress tasks are allowed
     *                              to complete.
     */
    public static void cancelTask(AsyncTask<?, ?, ?> task, boolean mayInterruptIfRunning) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(mayInterruptIfRunning);
        }
    }

    public static int indexOf(Object[] array, String e) {
        if (array != null && array.length > 0) {
            ArrayList<Object> list = new ArrayList<Object>(Arrays.asList(array));
            int index = list.indexOf(e);
            list.clear();
            return index;
        }
        return -1;
    }

    public static final int DAYS_SHOW = 62;

    public static long getMinDate() {
        return DateTime.now().minusDays(DAYS_SHOW - 1).millisOfDay().setCopy(0).getMillis();
    }

    public static long getMaxDate() {
        return DateTime.now().getMillis();
    }

    public static int getPositionMaxDate(Context context, String ymd) {
        DateTime dateLocal = DateTimeFormat.forPattern(context.getString(R.string.bing_date_formate)).parseDateTime(ymd);
        return DAYS_SHOW - 1 - Days.daysBetween(dateLocal, DateTime.now()).getDays();
    }

//    public static long getMaxDate() {
//        DateTime maxDate = DateTime.now();
//        Log.d(TAG, "getMinDate() maxDate:" + maxDate);
//        return maxDate.getMillis();
//    }

    public static void deleteExternalStoragePublicPicture(String name) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, name);
        file.delete();
    }

    public static boolean hasExternalStoragePublicPicture(String name) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, name);
        return file.exists();
    }

    public static void deleteExternalStoragePrivatePicture(Context context, String name) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, name);
            file.delete();
        }
    }

    public static boolean hasExternalStoragePrivatePicture(Context context, String name) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, name);
            return file.exists();
        }
        return false;
    }

    public static void deleteExternalStoragePrivateFile(Context context, String name) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(context.getExternalFilesDir(null), name);
        if (file != null) {
            file.delete();
        }
    }

    public static boolean hasExternalStoragePrivateFile(Context context, String name) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(context.getExternalFilesDir(null), name);
        if (file != null) {
            return file.exists();
        }
        return false;
    }

    public static void scanFile(Context context, String path, final MediaScannerConnection.OnScanCompletedListener listener) {
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(context,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                        if (listener != null) {
                            listener.onScanCompleted(path, uri);
                        }
                    }
                });
    }

    public static void actionViewImage(Context context, String subPath) {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, subPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        context.startActivity(intent);
    }

    public static boolean isBingUpdated(Context context, String date) {
        DateTime dateTimeZHCN = Utility.getDateTimeLocalToZHCN(context, date);
        DateTime nowDateTimeZHCN = DateTime.now().toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID)));
        DateTime updateDateTimeZHCN = getUpdateDateTimeZHCN();

        Log.d(TAG, "isBingUpdated() dateTimeZHCN:" + dateTimeZHCN);
        Log.d(TAG, "isBingUpdated() nowDateTimeZHCN:" + nowDateTimeZHCN);
        Log.d(TAG, "isBingUpdated() updateDateTimeZHCN:" + updateDateTimeZHCN);

        Log.d(TAG, "isBingUpdated() Days.daysBetween(updateDateTimeZHCN, dateTimeZHCN):" + Days.daysBetween(updateDateTimeZHCN, dateTimeZHCN));
        Log.d(TAG, "isBingUpdated() Hours.hoursBetween(updateDateTimeZHCN, dateTimeZHCN):" + Hours.hoursBetween(updateDateTimeZHCN, dateTimeZHCN));

        Hours hours = Hours.hoursBetween(updateDateTimeZHCN, dateTimeZHCN);

        boolean isBingUpdated = !(hours.isLessThan(Hours.ZERO) && hours.isGreaterThan(Hours.hours(-16)));
        Log.d(TAG, "isBingUpdated() isBingUpdated:" + isBingUpdated);
        return isBingUpdated;
    }

    public static final String TIME_ZONE_ID = "GMT+08:00";

    public static DateTime getUpdateDateTimeZHCN() {
        return DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID)))
                .millisOfDay().setCopy(0)
                .hourOfDay().setCopy(16);
    }

    public static DateTime getDateTimeLocalToZHCN(Context context, String date) {
        DateTime dateLocal = DateTimeFormat.forPattern(context.getString(R.string.bing_date_formate)).parseDateTime(date);
        DateTime dateTimeLocal = DateTime.now().year().setCopy(dateLocal.getYear()).monthOfYear().setCopy(dateLocal.getMonthOfYear()).dayOfMonth().setCopy(dateLocal.getDayOfMonth());
        DateTime dateTimeZHCN = dateTimeLocal.toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID)));

        Log.d(TAG, "getDateTimeLocalToZHCN() dateLocal:" + dateLocal);
        Log.d(TAG, "getDateTimeLocalToZHCN() dateTimeLocal:" + dateTimeLocal);
        Log.d(TAG, "getDateTimeLocalToZHCN() dateTimeZHCN:" + dateTimeZHCN);

        return dateTimeZHCN;
    }

    public static void actionDoudouBlog(Context context) {
        String url = "http://www.iruanmi.com/bing-wallpaper/";
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] positionToYmds(Context context, int position) {
        String[] ymds = positionToYmd(context, position).split("-");
        return ymds;
    }

    public static String positionToYmd(Context context, int position) {
        DateTime dateTime = DateTime.now().minusDays(Utility.DAYS_SHOW - 1 - position).toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone(Utility.TIME_ZONE_ID)));
        return dateTime.toString(context.getString(R.string.bing_date_formate));
    }

//    public static int ymdToPosition(Context context, String y, String m, String d) {
//
//    }

    public static String getYmd(String y, String m, String d) {
        return y + '-' + m + '-' + d;
    }

    public static String[] getYmds(String ymd) {
        String[] ymds = ymd.split("-");
        return ymds;
    }
}
