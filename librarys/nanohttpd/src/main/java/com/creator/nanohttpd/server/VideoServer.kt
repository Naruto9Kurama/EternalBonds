package com.creator.nanohttpd.server

import android.util.Log
import com.creator.common.Constants
import fi.iki.elonen.NanoHTTPD
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class VideoServer(port: Int = Constants.NanoHttpd.PORT) : NanoHTTPD(port) {
    private val TAG = this.javaClass.simpleName

    lateinit var currentVideoUri: String
    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        Log.d(TAG, "当前访问uri:::$uri")
        return when (uri) {
            "/video" -> {
                try {
                    val videoUrl =
                        URL(currentVideoUri)  // Replace with the actual HTTP video URL
                    val connection = videoUrl.openConnection() as HttpURLConnection
                    connection.connect()
                    val videoStream: InputStream = connection.inputStream

                    newChunkedResponse(Response.Status.OK, "video/mp4", videoStream)
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