package com.example.embedingsysterm2.clientSocketThread;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketThread extends Thread {
    private Socket socket = null;
    private static ClientSocketThread clientSocket = null;
    private MessageListener listener;
    private final int buffer_size = 64;
    public static Boolean isConnected;

    public static ClientSocketThread getClientSocket(String ip, int port) {
        if (clientSocket == null) {
            clientSocket = new ClientSocketThread(ip, port);
            clientSocket.start();
        }

        return clientSocket;
    }

    private ClientSocketThread(String ip, int port) {
        try {
            isConnected = true;
            this.socket = new Socket(ip, port);
        } catch (IOException var4) {
            isConnected = false;
            var4.printStackTrace();
        }

    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public void release() {
        this.interrupt();
        isConnected = false;
        clientSocket = null;
        this.socket = null;
    }

    public void run() {
        boolean var1 = true;

        while(!interrupted() && isConnected) {
            byte[] buffer = new byte[64];

            try {
                int len = this.getInputStream().read(buffer, 0, 64);
                this.FrameFilter(buffer, len);
                sleep(10L);
            } catch (InterruptedException var4) {
                isConnected = false;
                clientSocket = null;
            } catch (Exception var5) {
                isConnected = false;
                clientSocket = null;
            }
        }

    }

    public void FrameFilter(byte[] buffer, int len) {
        int index = 0;
        int frmlen = 0;
        byte status = 0;
        byte[] sensordata = (byte[])null;

        while(len-- > 0) {
            byte ch = buffer[index++];
            switch(status) {
                case 0:
                    if (ch == -2) {
                        status = 1;
                    }
                    break;
                case 1:
                    if ((byte)(ch & 224) == -32) {
                        status = 2;
                    } else {
                        status = 0;
                    }
                    break;
                case 2:
                    frmlen = ch;
                    if (ch < 64) {
                        frmlen = ch - 6;
                        ++index;
                        ++index;
                        sensordata = new byte[frmlen];
                        System.arraycopy(buffer, index, sensordata, 0, frmlen);
                        index += frmlen;
                        status = 3;
                    } else {
                        status = 0;
                    }
                    break;
                case 3:
                    if (this.listener != null) {
                        this.listener.Message(sensordata, frmlen);
                    }

                    status = 0;
            }
        }

    }

    private InputStream getInputStream() throws Exception {
        return this.socket.getInputStream();
    }

    public OutputStream getOutputStream() throws Exception {
        return this.socket.getOutputStream();
    }
}
