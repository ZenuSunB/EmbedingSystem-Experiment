package com.example.embedingsysterm2.JNI_interface;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.embedingsysterm2.R;

public class LedJniObject  {
    /** Called when the activity is first created. */

    /* 相关变量声明 */
    private static int fd;
    private static int [] LedState = new int[]{0,0,0,0};

    public void open()
    {
        String path = new String("/dev/ledtest");
        fd = LedDeviceOpen(path);	//调用jni函数
        LedDeviceIoctl(0, 0);
        LedDeviceIoctl(0, 1);
        LedDeviceIoctl(0, 2);
        LedDeviceIoctl(0, 3);
    }
    public void close()
    {
        LedDeviceClose();
    }
    public void light(int which)
    {
        assert which<=3&&which>=0;
        if(LedState[which]==0)
        {
            LedDeviceIoctl(1,which);
            LedState[which]=1;
        }
        else
        {
            LedDeviceIoctl(0, which);
            LedState[which] = 0;
        }
    }


    /* 添加函数声明,告诉编译和链接器该方法在本地代码中实现 */
    public native int LedDeviceOpen(String path);
    public native void LedDeviceIoctl(int cmd, int arg);
    public native void LedDeviceClose();

    /* 加载JNI代码编译生成的共享库 */
    static {
        System.loadLibrary("led-jni");
    }


}