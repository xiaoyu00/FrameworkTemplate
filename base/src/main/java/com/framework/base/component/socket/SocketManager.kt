package com.framework.base.component.socket

import okhttp3.*
import okio.ByteString
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.util.concurrent.TimeUnit


object SocketManager {
    private const val timeout = 30000L
    private val url = "ws://xxxxx"

    //已连接标记
    private var isConnect = false
    private var webSocket: WebSocket? = null

    private val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            isConnect = true
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            //收到消息...（一般是这里处理json）
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            //收到消息...（一般很少这种消息）
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            //连接关闭...
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            //连接失败...
            isConnect = false
        }
    }

    fun initConnect() {
        val mClient = OkHttpClient.Builder()
            .readTimeout(timeout, TimeUnit.SECONDS) //设置读取超时时间
            .writeTimeout(timeout, TimeUnit.SECONDS) //设置写的超时时间
            .connectTimeout(timeout, TimeUnit.SECONDS) //设置连接超时时间
            .build()
        val request = Request.Builder().get().url(url).build()
        webSocket = mClient.newWebSocket(request, socketListener)
    }

    fun sendMessage(msg: String?): Boolean {
        msg?.let {
            return webSocket?.send(it) ?: false
        }
        return false
    }

    fun isConnect() = isConnect
}