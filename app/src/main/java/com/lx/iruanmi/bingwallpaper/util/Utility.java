package com.lx.iruanmi.bingwallpaper.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.lx.iruanmi.bingwallpaper.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
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

    public static long getMinDate() {
        return DateTime.now().minusDays(64).millisOfDay().setCopy(0).getMillis();
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
        DateTime dateTimeZHCN = Utility.getDateTimeZHCN(context, date);
        String dateZHCN = Utility.getDateStringZHCN(context, date);

        DateTime nowDateTime = DateTime.now();
        DateTime nowDateTimeZHCN = nowDateTime.toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+08:00")));

        DateTime updateDateTime = DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+08:00")))
                .millisOfDay().setCopy(0)
                .hourOfDay().setCopy(16);

        return false;
    }

    public static DateTime getDateTimeZHCN(Context context, String date) {
        DateTime dateLocal = DateTimeFormat.forPattern(context.getString(R.string.bing_date_formate)).parseDateTime(date);
        DateTime dateTimeLocal = DateTime.now().year().setCopy(dateLocal.getYear()).monthOfYear().setCopy(dateLocal.getMonthOfYear()).dayOfMonth().setCopy(dateLocal.getDayOfMonth());
        DateTime dateTimeZHCN = dateTimeLocal.toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+08:00")));

        Log.d(TAG, "dateLocal:" + dateLocal);
        Log.d(TAG, "dateTimeLocal:" + dateTimeLocal);
        Log.d(TAG, "dateTimeZHCN:" + dateTimeZHCN);

        return dateTimeZHCN;
    }

    public static String getDateStringZHCN(Context context, String date) {
        DateTime dateTimeZHCN = getDateTimeZHCN(context, date);
        String dateZHCN = dateTimeZHCN.toString(context.getString(R.string.bing_date_formate));

        Log.d(TAG, "dateTimeZHCN:" + dateTimeZHCN);
        Log.d(TAG, "dateZHCN:" + dateZHCN);

        return dateZHCN;
    }
}
