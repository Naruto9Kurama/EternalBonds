package com.creator.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    // 相机权限
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    // 存储权限
    public static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    // 位置权限
    public static final String ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    // 联系人权限
    public static final String READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS;
    public static final String WRITE_CONTACTS_PERMISSION = Manifest.permission.WRITE_CONTACTS;
    // 录音权限
    public static final String RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO;
    // 电话权限
    public static final String CALL_PHONE_PERMISSION = Manifest.permission.CALL_PHONE;
    // 短信权限
    public static final String SEND_SMS_PERMISSION = Manifest.permission.SEND_SMS;
    public static final String RECEIVE_SMS_PERMISSION = Manifest.permission.RECEIVE_SMS;

    public static final int PERMISSION_REQUEST_CODE = 100;

    /**
     * 检查是否已授予所有权限
     */
    public static boolean checkAllPermissions(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean requestFilePermissions(Activity activity) {
        String[] strings = {
                Manifest.permission.READ_MEDIA_VIDEO,
        };
        requestPermissions(activity,strings);
        return false;
    }

    /**
     * 请求权限
     */
    public static void requestPermissions(Activity activity, String[] permissions) {
        List<String> permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    activity,
                    permissionsToRequest.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    /**
     * 处理权限请求结果
     */
    public static boolean handlePermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            // 所有请求的权限都被授予
            return true;
        }
        return false;
    }
}
