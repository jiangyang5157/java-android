package com.gmail.jiangyang5157.tookit.base.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Yang
 * @since 3/9/2016
 */
public class IoUtils {
    private static final String TAG = "[IoUtils]";

    public interface OnReadingListener {
        boolean onReadLine(String line);
    }

    public static String read(InputStream in) throws IOException {
        final StringBuilder body = new StringBuilder();
        read(in, new OnReadingListener() {
            @Override
            public boolean onReadLine(String line) {
                if (line == null) {
                    return false;
                } else {
                    body.append(line).append(System.getProperty("line.separator"));
                    return true;
                }
            }
        });

        return body.toString();
    }

    public static void read(InputStream in, OnReadingListener onReadingListener) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        boolean valid = true;
        while (valid) {
            valid = onReadingListener.onReadLine(reader.readLine());
        }
        reader.close();
    }

    public static void write(InputStream in, File dst) throws IOException {
        System.out.println(TAG + " write: " + dst.getAbsolutePath());
        dst.getParentFile().mkdirs();

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        FileOutputStream fileOutputStream = new FileOutputStream(dst);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER_SIZE);

        int length;
        while ((length = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
            bufferedOutputStream.write(buffer, 0, length);
        }

        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static void copy(File src, File dst) throws IOException {
        System.out.println(TAG + " copy " + src.getAbsolutePath() + " to " + dst.getAbsolutePath());
        write(new FileInputStream(src), dst);
    }

    public static void unzip(InputStream in, File dst, boolean replace) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);

        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            String zipEntryName = zipEntry.getName();
            File file = new File(dst.getAbsolutePath() + File.separator + zipEntryName);
            System.out.println(TAG + " unzip: " + file.getAbsolutePath());

            if (zipEntry.isDirectory()) {
                file.mkdirs();
            } else if (!file.exists() || file.isDirectory() || replace) {
                write(zipInputStream, file);
            }
        }

        zipInputStream.close();
        bufferedInputStream.close();
    }
}
