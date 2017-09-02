package com.gmail.jiangyang5157.tookit.android.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * @author Yang 9/16/2015.
 */
public class AppUtils {

    public static String getAppPackageName(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).packageName;
    }

    public static String getAppVersionName(final Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).versionName;
    }

    public static String getExecutablePath(final Context context) {
        return context.getApplicationInfo().sourceDir;
    }

    public static String getProfilePath(final Context context) {
        return context.getApplicationInfo().dataDir;
    }

    public static void registerOnSharedPreferenceChangeListener(
            final Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(
            final Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static void buildToast(Context context, int stringResId) {
        Toast.makeText(context, getString(context, stringResId), Toast.LENGTH_SHORT).show();
    }

    public static void buildToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    public static Drawable getDrawable(final Context context, final int resId, Resources.Theme theme) {
        return context.getResources().getDrawable(resId, theme);
    }

    public static String getString(final Context context, final int resId) {
        return context.getResources().getString(resId);
    }

    public static int getInt(final Context context, final int resId) {
        return context.getResources().getInteger(resId);
    }

    public static boolean getBool(final Context context, final int resId) {
        return context.getResources().getBoolean(resId);
    }

    public static int getColor(final Context context, final int resId, Resources.Theme theme) {
        return context.getResources().getColor(resId, theme);
    }
}
