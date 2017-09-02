package com.gmail.jiangyang5157.tookit.android.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Yang 9/16/2015.
 */
public class EncodeUtils {

    public static String encodeObject(Object object) throws IOException {
        if (object == null) {
            return "";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        baos.close();
        oos.close();
        return base64;
    }

    public static Object decodeObject(String base64) throws IOException, ClassNotFoundException {
        if (base64 == null || base64.equals("")) {
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(base64.getBytes(), Base64.DEFAULT));
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object ret = ois.readObject();

        bais.close();
        ois.close();
        return ret;
    }

    /**
     * @param bitmap
     * @param quality (0, 100]
     * @return base64 string
     */
    public static String encodeBitmap(Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null) {
            return "";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        baos.close();
        return base64;
    }

    public static Drawable decodeDrawable(Context context, String base64) throws IOException {
        if (base64 == null || base64.equals("")) {
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(base64.getBytes(), Base64.DEFAULT));
        Drawable ret = Drawable.createFromResourceStream(context.getResources(), null, bais, null, null);
        bais.close();
        return ret;
    }
}
