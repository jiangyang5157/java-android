package com.gmail.jiangyang5157.tookit.base.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yang
 * @date 4/13/2016
 */
public class RegularExpressionUtils {

    public static final String DATE_REGEX_LABEL = "MMM dd, yyyy HH:mm:ss";
    public static final String DATE_REGEX_FILE_NAME = "yyyyMMddHHmmss";

    // Tue, 15 Nov 1994 12:45:26 GMT
    public static final String DATE_REGEX_HTTP_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz";

    /**
     * Accurate regex that captures the four parts of the IP Address
     */
    public static final String IP_ADDRESS_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    /**
     * Accurate regex that captures the Port
     */
    public static final String PORT_REGEX = "^([\\d]{1,5})$";
    public static final String URL_REGEX = "(https?:\\/\\/)+[\\w.:\\-]+(/[\\w.:\\-~!@#$%&+=|;?,]+)*";

    public static void main(String[] args) {
        String[] contents = new String[]{
                "developer.android.com/index.html",
                "http://developer.android.com/index.html",
                "https://developer.android.com/index.html",
                "asd//####https://developer.android.com/index.html",
                "https://de-ve-lo..per.android.com/index.html",
                "https://developer.android.com/in-dex.html",
                "https://developer.android.com/in dex.html",
                "https://developer.android.com/in#dex.html",
                "(https://developer.android.com/index.html)",
                "https:// developer.android.com/index.html)",
                "https://developer.an::droid.com/index.html)",
                "https://dev#eloper.an::droid.com/index.html)",
                "http://developer.android.com/index.html/",
                "http://developer.android.com/index.html///",
                "http://developer.android.com/index.html///asdasd-asd.asd--asd//",
                "Indonesia<br>BaliIsForMe.com (http://www.BaliIsForMe.com)",
        };

        Pattern pattern = Pattern.compile(URL_REGEX);
        for (String content : contents) {
            Matcher matcher = pattern.matcher(content);
            System.out.println();
            boolean find = matcher.find();
            System.out.println("content: " + content + " matcher.find=" + find);
            if (find) {
                System.out.println("start, end: " + matcher.start() + ", " + matcher.end());
            }
        }
    }
}
