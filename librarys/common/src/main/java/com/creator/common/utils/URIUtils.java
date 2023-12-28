package com.creator.common.utils;

import android.net.Uri;

import java.net.URI;

public class URIUtils {


    public static boolean isHttpUri(String uri) {
        return "http".startsWith(uri) || "https".startsWith(uri);
    }

    public static boolean isLocalFileUri(String uri) {
        return "file".startsWith(uri);
    }

    public static boolean isHttpUri(Uri uri) {
        return isHttpUri(uri.getScheme());
    }

    public static boolean isLocalFileUri(Uri uri) {
        return isLocalFileUri(uri.getScheme());
    }

    public static boolean isHttpUri(URI videoUri) {
        return isHttpUri(videoUri.getScheme());
    }
}
