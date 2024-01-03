package com.creator.nanohttpd.server

import android.content.Context
import android.net.Uri
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
        LogUtil.d(TAG, "当前访问uri:::$uri")
        val response = when (uri) {
            "/video" -> {
                try {
                    if (URIUtils.isHttpUri(videoUri)) {
                        val videoUrl =
                            URL(videoUri.toString())
                        val connection = videoUrl.openConnection() as HttpURLConnection
                        connection.connect()
                        val videoStream: InputStream = connection.inputStream
                        newChunkedResponse(Response.Status.OK, "video/mp4", videoStream)
                    } else {
                        serveVideoFile(session)
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

        return response
    }

    fun serveVideoFile(session: IHTTPSession): Response {
        return try {
            val videoFileStr =
                FileUtil.getRealPathFromUri(context, Uri.parse(videoUri.toString()))
            val videoFile = File(videoFileStr)
            val rangeHeader = session.headers["range"]
            rangeHeader?.let { servePartialVideoFile(videoFile, it) } ?: serveFullVideoFile(
                videoFile
            )
        } catch (e: IOException) {
            e.printStackTrace()
            newFixedLengthResponse("Error serving video file")
        }
    }

    private fun servePartialVideoFile(videoFile: File, rangeHeader: String): Response {
        val fileSize = videoFile.length()
        val rangeValues = rangeHeader.substring("bytes=".length).split("-".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val startRange = rangeValues[0].toLong()
        val endRange = if (rangeValues.size > 1) rangeValues[1].toLong() else fileSize - 1
        val contentLength = endRange - startRange + 1
        val fileInputStream = FileInputStream(videoFile)
        fileInputStream.skip(startRange)
        val response = newFixedLengthResponse(
            Response.Status.PARTIAL_CONTENT,
            Constants.Video.MIME_TYPE[Enums.VideoType.MP4],
            fileInputStream,
            contentLength
        )
        response.addHeader("Content-Range", "bytes $startRange-$endRange/$fileSize")
        return response
    }

    private fun serveFullVideoFile(videoFile: File): Response {
        val fileInputStream = FileInputStream(videoFile)
        return newChunkedResponse(
            Response.Status.OK,
            Constants.Video.MIME_TYPE[Enums.VideoType.MP4],
            fileInputStream
        )
    }


}
