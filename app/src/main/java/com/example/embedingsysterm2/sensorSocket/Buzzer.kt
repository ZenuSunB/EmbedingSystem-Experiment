package com.example.embedingsysterm2.sensorSocket

import com.example.embedingsysterm2.clientSocketThread.ClientSocketThread
import com.example.embedingsysterm2.clientSocketThread.ClientSocketTools
import kotlin.concurrent.thread

class Buzzer {
    companion object {
        private var clientSocketThread: ClientSocketThread? = null;
        private val buffer = byteArrayOf(
            0xFE.toByte(),
            0xE0.toByte(),
            0x08,   //帧长度    0000,1111
            0x12,  //帧类型  0001,0010
            0x72, //命令类型  0111,0010
            0x00,   //数据域说明
            0x00.toByte(),   //有效数据区0
            0x0A
        )  //结束

        public fun open() {
            buffer[3] = 0x12//打开设备
            thread {
                clientSocketThread =
                    ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(), 6109);
            }
        }

        public fun stop() {
            buffer[3] = 0x13//关闭设备
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }
    }
}