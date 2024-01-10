package com.creator.common.utils;

import android.content.Context;
import android.os.Build;

import com.creator.common.activity.RequestPermissionsHelpActivity;

/**
 * 新版动态权限申请类
 * 说明：不用依赖activity接收回调，可以再任意地方请求权限
 * Created by yangqin on 2018/5/28.
 */

public class AppPermissionUtil {

    /**
     * 去请求所有权限
     * @param context
     * @param permissions 需要请求的权限列表
     * @param listener 请求权限回调
     */
    public static void requestPermissions(Context context, String[] permissions, OnPermissionListener listener) {
        if(context==null||listener==null){
            throw new NullPointerException("context参数为空，或者listener参数为空");
        }

        if (Build.VERSION.SDK_INT <= 22) {
            //SDK小于22之前的版本之前发返回权限允许
            listener.onPermissionGranted();
        } else {
            //打开一个一像素的activity去请求权限，并回调返回结果
            RequestPermissionsHelpActivity.start(context,permissions,listener);
        }
    }

    public interface OnPermissionListener {

        void onPermissionGranted();//授权

        void onPermissionDenied();//拒绝
    }
}

