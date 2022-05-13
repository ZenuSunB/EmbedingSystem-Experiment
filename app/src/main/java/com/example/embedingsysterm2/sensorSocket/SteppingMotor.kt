package com.example.embedingsysterm2.sensorSocket

import com.example.embedingsysterm2.clientSocketThread.ClientSocketThread
import com.example.embedingsysterm2.clientSocketThread.ClientSocketTools
import kotlin.concurrent.thread

class SteppingMotor {
    companion object {
        private var clientSocketThread: ClientSocketThread? = null;
        private val buffer = byteArrayOf(
            -2,
            -32,
            8,   //帧长度    0000,1000
            50,  //帧类型    0011,0010
            114, //命令类型  0111,0010
            0,   //数据域说明
            2,   //有效数据区
            10)  //结束

        public fun open()
        {
            thread {
                clientSocketThread =
                    ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(), 6109);
            }
        }
        public fun stop()
        {
            buffer[6]=1
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }
        public fun turnLeft()
        {
            buffer[6]=2
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: java.lang.Exception) {
                var3.printStackTrace()
            }
        }

        public fun turnRight()
        {
            buffer[6]=3
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: java.lang.Exception) {
                var3.printStackTrace()
            }
        }
    }
}