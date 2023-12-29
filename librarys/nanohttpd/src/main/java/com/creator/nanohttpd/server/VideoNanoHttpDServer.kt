package com.creator.nanohttpd.server

import android.content.Context
import android.net.Uri
import android.util.Log
import com.creator.common.Constants
import com.creator.common.enums.Enums
import com.creator.common.utils.FileUtil
import com.creator.common.utils.LogUtil
import com.creator.common.utils.URIUtils
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL


class VideoNanoHttpDServer(
    port: Int = Constants.NanoHttpd.PORT,
    var videoUri: URI? = null,
    val context: Context? = null
) :
    NanoHTTPD(port) {
    constructor(port: Int = Constants.NanoHttpd.PORT, uri: String) : this(port, URI(uri))

    private val TAG = this.javaClass.simpleName

    lateinit var currentVideoUri: String

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        Log.d(TAG, "当前访问uri:::$uri")
        return when (uri) {
            "/video" -> {
                try {
                    if (URIUtils.isHttpUri(videoUri)) {
                        val videoUrl =URL(videoUri.toString())  // Replace with the actual HTTP video URL
                        val connection = videoUrl.openConnection() as HttpURLConnection
                        connection.connect()
                        val videoStream: InputStream = connection.inputStream
                        newChunkedResponse(Response.Status.OK, "video/mp4", videoStream)
                    } else {
                        serveVideoFile()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    newFixedLengthResponse("Error serving video")
                }
            }

            else -> {
                newFixedLengthResponse("Invalid endpoint")
            }
        }
    }

    private fun serveVideoFile(): Response {
        return try {

            // 在这里，你可以使用videoUri进行必要的处理，例如复制文件
            // 这里简单地将文件复制到临时文件中
            val inputStream =
                context!!.contentResolver.openInputStream(Uri.parse(videoUri.toString()))
//            newFixedLengthResponse(Response.Status.OK, Constants.Video.MIME_TYPE[Enums.VIDEO_TYPE.MKV], videoStream, videoFile.length())
            newChunkedResponse(
                Response.Status.OK,
                Constants.Video.MIME_TYPE[Enums.VIDEO_TYPE.MP4],
                inputStream
            )
        } catch (e: Exception) {
            LogUtil.d(TAG, e.message.toString(), e)
            e.printStackTrace()
            newFixedLengthResponse(
                Response.Status.INTERNAL_ERROR,
                "text/plain",
                "Error serving video file"
            )
        }
    }

    /*fun main(args: Array<String>) {
        val server = VideoServer(8081)
        try {
            server.start()
            println("Server started on port 8081")
        } catch (e: IOException) {
            e.printStackTrace()
            System.err.println("Error starting server")
        }
    }*/
}
