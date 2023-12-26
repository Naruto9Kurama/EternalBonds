package com.creator.common.utils;

import android.net.Uri;

public class URIUtils {


    public static boolean isHttpUri(String uri) {
        return "http".startsWith(uri) || "https".startsWith(uri);
    }
    public static boolean isLocalFileUri(String uri) {
        return "file".startsWith(uri);
    }
    public static boolean isHttpUri(Uri uri) {
        return "http".equals(uri.getScheme()) || "https".equals(uri.getScheme());
    }

    public static boolean isLocalFileUri(Uri uri) {
        return "file".equals(uri.getScheme());
    }

}
