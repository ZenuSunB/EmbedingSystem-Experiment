package com.example.embedingsysterm2.sensorSocket

import android.os.Handler
import android.util.Log
import com.example.embedingsysterm2.clientSocketThread.ClientSocketThread
import com.example.embedingsysterm2.clientSocketThread.ClientSocketTools
import com.example.embedingsysterm2.clientSocketThread.MessageListener
import kotlin.concurrent.thread

class NfcDetect {
    interface NfcCallback
    {
        fun onMesgDect(obj:ByteArray?);
    }
    companion object {
        private val buffer = byteArrayOf(-2, -32, 8, 0, 0, 0, 2, 10)
        private var clientSocketThread: ClientSocketThread? = null
        private var listener: NfcCallback? = null
        public fun setHandler(listener:NfcCallback)
        {
            this.listener=listener
        }
        public fun init()
        {
            thread {
                clientSocketThread =
                    ClientSocketThread.getClientSocket(ClientSocketTools.getLocalIpAddress(), 6109);
                clientSocketThread?.setListener(object : MessageListener {
                    override fun Message(message: ByteArray?, message_len: Int) {
//                        Log.d("TAG", "Message: ++++++++++++++"+message)
                        listener?.onMesgDect(message);
                    }
                })
            }
        }
        public fun open()
        {
            buffer[3]=81
            buffer[4]=114
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }
        public fun read()
        {
            buffer[3]=85
            buffer[4]=114
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }
        public fun stop()
        {
            buffer[3]=0x53
            try {
                clientSocketThread!!.outputStream.write(buffer)
            } catch (var3: Exception) {
                var3.printStackTrace()
            }
        }
    }
}