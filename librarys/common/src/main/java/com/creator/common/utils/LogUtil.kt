package com.creator.common.utils

import android.util.Log

object LogUtil {

    const val TAG="EternalBondsLogUtil"


    inline fun d(tag:String,msg:String,tr:Throwable?=null){
        Log.d("$TAG--$tag",msg,tr)
    }

    inline fun e(tag:String,msg:String,tr:Throwable?=null){
        Log.e("$TAG--$tag",msg,tr)
    }
}