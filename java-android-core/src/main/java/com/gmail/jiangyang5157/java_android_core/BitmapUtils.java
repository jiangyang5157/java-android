package com.gmail.jiangyang5157.java_android_core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Yang 9/16/2015.
 */
public class BitmapUtils {

    public static Bitmap load(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    /**
     * @param sampleSize size width = width / sampleSize; high = high / sampleSize
     * @return Bitmap
     */
    public static Bitmap load(String filePath, int sampleSize) throws IOException {
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = sampleSize;

        URL url = new URL("file://" + filePath);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.connect();

        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        Bitmap ret = BitmapFactory.decodeStream(bis, null, mOptions);

        bis.close();
        return ret;
    }

    public static Bitmap reverse(Bitmap src) {
        return Bitmap.createScaledBitmap(src, src.getWidth(), -src.getHeight(), true);
    }

    /**
     * @param size [0, 100]
     */
    public static void save(Bitmap src, String dst, int size) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dst));
        src.compress(Bitmap.CompressFormat.JPEG, size, bos);
        bos.flush();
        bos.close();
    }

    public static Bitmap rotate(Bitmap src, float degrees) {
        int imgWidth = src.getWidth();
        int imgHeight = src.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(imgWidth, imgHeight);
        matrix.setRotate(degrees);
        return Bitmap.createBitmap(src, 0, 0, imgWidth, imgHeight, matrix, true);
    }

    public static Bitmap scale(Bitmap src, int newWidth, int newHeight) {
        int oldWidth = src.getWidth();
        int oldHeight = src.getHeight();
        double scaleWidth = newWidth / oldWidth;
        double scaleHeight = newHeight / oldHeight;
        Matrix matrix = new Matrix();
        matrix.postScale((float) scaleWidth, (float) scaleHeight);
        return Bitmap.createBitmap(src, 0, 0, oldWidth, oldHeight, matrix, true);
    }

    public static Bitmap scale(Bitmap src, float scale) {
        int oldWidth = src.getWidth();
        int oldHeight = src.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(src, 0, 0, oldWidth, oldHeight, matrix, true);
    }
}
