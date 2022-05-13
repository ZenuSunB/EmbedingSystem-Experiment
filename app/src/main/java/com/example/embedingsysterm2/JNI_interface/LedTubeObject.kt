package com.example.embedingsysterm2.JNI_interface

class LedTubeObject {


    external fun show(str:String)
    /* 加载JNI代码编译生成的共享库 */
    companion object
    {
        init {
        System.loadLibrary("ledTube-jni")
        }
    }
    public fun showString(str:String)
    {
        assert(str.length>=0&&str.length<=8)
        show(str)
    }

}