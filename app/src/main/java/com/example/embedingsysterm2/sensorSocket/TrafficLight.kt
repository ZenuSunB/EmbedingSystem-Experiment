package com.example.embedingsysterm2.sensorSocket

import com.example.embedingsysterm2.clientSocketThread.ClientSocketThread
import com.example.embedingsysterm2.clientSocketThread.ClientSocketTools
import kotlin.concurrent.thread

class TrafficLight{
    companion object {
        private var clientSocketThread: ClientSocketThread? = null;
        private val buffer = byteArrayOf(
            0xFE.toByte(),
            0xE0.toByte(),
            0x08,   //帧长度
            0x44,  //帧类型
            0x72, //命令类型
            0x00,   //数据域说明
            0x12,   //有效数据区
            0x0A)  //结束

        public fun open()
        {
            buffer[3]=0x12//打开设备
            thread {
                clientSocketThread =
                    ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(), 6109);
            }
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }
        public fun stop()
        {
            buffer[3]=0x13//关闭设备
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }
        public fun light(value:Int)
        {
            when(value)
            {
                0->buffer[6]=0x11 //红
                1->buffer[6]=0x12 //黄
                2->buffer[6]=0x14 //绿
            }
            //红  黄   绿
            buffer[3]=0x44//

            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }

    }
}