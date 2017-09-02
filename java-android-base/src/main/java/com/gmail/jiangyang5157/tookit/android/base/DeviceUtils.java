package com.gmail.jiangyang5157.tookit.android.base;

import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.TypedValue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Yang 9/16/2015.
 */
public class DeviceUtils {

    public static final int STORAGE_UNIT = 1024;
    public static final String SYMBOL_BYTE = "BYTE";
    public static final String SYMBOL_KB = "KB";
    public static final String SYMBOL_MB = "MB";
    public static final String SYMBOL_GB = "GB";

    public static boolean sdkValidate(int sdkVersion) {
        return Build.VERSION.SDK_INT >= sdkVersion;
    }

    public static boolean glesValidate(final Context context, int version) {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().reqGlEsVersion >= version;
    }

    public static boolean isWiFiEnabled(final Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean ret = false;
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            ret = false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Returned value is converted to integer pixels for you.
     * An offset conversion involves simply truncating the base value to an integer.
     */
    public static int getDimensionPixelOffset(Resources resources, final int resId) {
        return resources.getDimensionPixelOffset(resId);
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getSdacrdFile() {
        return Environment.getExternalStorageDirectory();
    }

    public static void authority(String filePath) throws IOException {
        Runtime.getRuntime().exec("chmod 777 " + filePath);
    }

    private static double getSize(final long fileLength, final String symbol) {
        double ret = 0;
        switch (symbol) {
            case SYMBOL_BYTE:
                ret = (float) fileLength;
                break;
            case SYMBOL_KB:
                ret = (float) fileLength / STORAGE_UNIT;
                break;
            case SYMBOL_MB:
                ret = (float) fileLength / (STORAGE_UNIT * STORAGE_UNIT);
                break;
            case SYMBOL_GB:
                ret = (float) fileLength / (STORAGE_UNIT * STORAGE_UNIT * STORAGE_UNIT);
                break;
        }

        int scale = 2;
        int roundingMode = BigDecimal.ROUND_UP;
        BigDecimal bigDecimal = new BigDecimal(ret);
        bigDecimal = bigDecimal.setScale(scale, roundingMode);
        ret = bigDecimal.doubleValue();
        return ret;
    }

    @Deprecated
    public static double getAvailableSize(File file, final String symbol) {
        StatFs statFs = new StatFs(file.getPath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        long available = availableBlocks * blockSize;
        return getSize(available, symbol);
    }

    public static double getSize(File file, final String symbol) {
        return getSize(file.length(), symbol);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Since path might be "/storage/emulated/0/..." or "content://com.android.externalstorage.documents/document/primary:..." etc.
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The uri to query
     */
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (sdkValidate(Build.VERSION_CODES.KITKAT) && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String ret = null;

        final String column = "_data";
        final String[] projection = {
                column
        };

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret = cursor.getString(cursor.getColumnIndexOrThrow(column));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return ret;
    }

    public static String buildStringDate(String template) {
        return buildDate(template).format(new Date());
    }

    public static String buildStringDate(long milliseconds, String template) {
        return buildDate(template).format(new Date(milliseconds));
    }

    public static long buildLongDate() {
        return new Date().getTime();
    }

    public static long buildLongDate(String stringDate, String template) throws ParseException {
        return buildDate(template).parse(stringDate).getTime();
    }

    private static SimpleDateFormat buildDate(String template) {
        return buildDate(template, Locale.getDefault());
    }

    private static SimpleDateFormat buildDate(String template, Locale locale) {
        return new SimpleDateFormat(template, locale);
    }
}
