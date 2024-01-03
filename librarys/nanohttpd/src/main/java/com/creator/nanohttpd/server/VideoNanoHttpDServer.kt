package com.creator.nanohttpd.server

import android.content.Context
import android.net.Uri
import com.creator.common.Constants
import com.creator.common.bean.FileBean
import com.creator.common.utils.FileUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.URIUtils
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class VideoNanoHttpDServer(
    port: Int = Constants.NanoHttpd.PORT,
    var videoUri: Uri? = null,
    val context: Context? = null
) : NanoHTTPD(port) {
    constructor(port: Int = Constants.NanoHttpd.PORT, uri: String, context: Context? = null) : this(
        port,
        Uri.parse(uri),
        context
    )
    private val TAG = this.javaClass.simpleName

    companion object {
        enum class RequestURI {
            VIDEO,
        }

        //请求地址Map
        val map = mapOf<RequestURI, String>(
            RequestURI.VIDEO to "/video"
        )
    }

    private var fileBean: FileBean = FileBean(context, videoUri)


    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        LogUtil.d(TAG, "当前访问uri:::$uri")
        val response = when (uri) {
            //视频请求
            map[RequestURI.VIDEO] -> serveVideo(session)

            else -> {
                newFixedLengthResponse("Invalid endpoint")
            }
        }

        return response
    }


    /**
     * 处理视频请求
     */
    private fun serveVideo(session: IHTTPSession): Response {
        return try {
            //判断视频地址是否是Http
            if (URIUtils.isHttpUri(videoUri)) {
                val videoUrl = URL(videoUri.toString())
                val connection = videoUrl.openConnection() as HttpURLConnection
                connection.connect()
                val videoStream: InputStream = connection.inputStream
                newChunkedResponse(Response.Status.OK, fileBean.mimeType, videoStream)
            } else {
                //响应本地视频文件
                serveVideoFile(session)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            newFixedLengthResponse("Error serving video")
        }
    }


    /**
     *  处理视频文件请求
     */
    private fun serveVideoFile(session: IHTTPSession): Response {
        return try {
            // 本地视频文件路径
            // 获取请求头中的 Range 字段
            val rangeHeader = session.headers["range"]

            // 如果存在 Range 字段，则处理范围请求
            rangeHeader?.let { servePartialVideoFile(it) } ?: // 否则，处理完整文件请求
            serveFullVideoFile()
        } catch (e: IOException) {
            e.printStackTrace()
            newFixedLengthResponse("Error serving video file")
        }
    }

    /**
     *处理范围请求的视频文件
     */
    private fun servePartialVideoFile(rangeHeader: String): Response {
        // 获取文件大小

        // 解析 Range 字段，获取请求的范围
        val rangeValues = rangeHeader.substring("bytes=".length).split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val startRange = rangeValues[0].toLong()
        val endRange = if (rangeValues.size > 1) rangeValues[1].toLong() else fileBean.fileSize - 1

        // 计算内容长度
        val contentLength = endRange - startRange + 1

        // 创建文件输入流，定位到请求的范围开始处
        val fileInputStream = FileInputStream(fileBean.file)
        fileInputStream.skip(startRange)

        // 创建范围请求的响应
        val response = newFixedLengthResponse(
            Response.Status.PARTIAL_CONTENT,
            fileBean.mimeType,
            fileInputStream,
            contentLength
        )
        response.addHeader("Content-Range", "bytes $startRange-$endRange/${fileBean.fileSize}")
        return response
    }

    /**
     * 处理完整视频文件请求
     */
    private fun serveFullVideoFile(): Response {
        // 创建文件输入流
        val fileInputStream = FileInputStream(fileBean.file)

        // 创建支持分块传输的响应
        return newChunkedResponse(Response.Status.OK, fileBean.mimeType, fileInputStream)
    }

}
