package com.example.embedingsysterm2.clientSocketThread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
public class ClientSocketTools {
    public ClientSocketTools() {
    }

    @SuppressLint("LongLogTag")
    public static String getLocalIpAddress() {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException var4) {
            Log.e("WifiPreference IpAddress", var4.toString());
        }

        return null;
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch (Exception var3) {
            return 1.0F;
        }
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            return dm;
        } catch (Exception var3) {
            return null;
        }
    }

    public static String byte2hex(byte[] b, int len) {
        String hs = "";
        String tmp = "";

        for(int n = 0; n < len; ++n) {
            tmp = Integer.toHexString(b[n] & 255);
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }

        tmp = null;
        return hs.toUpperCase();
    }

    public static String byte2hex(byte[] b, int start, int len) {
        String hs = "";
        String tmp = "";

        for(int n = start; n < len; ++n) {
            tmp = Integer.toHexString(b[n] & 255);
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }

        tmp = null;
        return hs.toUpperCase();
    }

    public static int byte2int(byte[] b, int index) {
        int temp = b[index + 0];
        temp = temp & 255;
        temp |= b[index + 1] << 8;
        temp &= 65535;
        temp |= b[index + 2] << 16;
        temp &= 16777215;
        temp |= b[index + 3] << 24;
        return temp;
    }

    public static float byte2float(byte[] b, int index) {
        int temp = b[index + 0];
        temp = temp & 255;
        temp = (int)((long)temp | (long)b[index + 1] << 8);
        temp &= 65535;
        temp = (int)((long)temp | (long)b[index + 2] << 16);
        temp &= 16777215;
        temp = (int)((long)temp | (long)b[index + 3] << 24);
        return Float.intBitsToFloat(temp);
    }
}
