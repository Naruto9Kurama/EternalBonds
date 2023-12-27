package com.creator.common.utils

import okhttp3.*
import java.io.IOException

object OkHttpClientUtil {

    private val client = OkHttpClient()

    // 异步 GET 请求
    fun asyncGet(url: String, callback: Callback) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(callback)
    }

    // 同步 GET 请求
    fun syncGet(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response = client.newCall(request).execute()
            return response.body?.string() ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    // 异步 POST 请求
    fun asyncPost(url: String, requestBody: RequestBody, callback: Callback) {
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(callback)
    }

    // 同步 POST 请求
    fun syncPost(url: String, requestBody: RequestBody): String {
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            return response.body?.string() ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

}
