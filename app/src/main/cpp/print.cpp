//
// Created by Zenu on 2022/4/22.
//

#include <jni.h>
#include <experimental/string>
#include "stdio.h"
#include "stdlib.h"


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_embedingsysterm2_MainActivity_print(JNIEnv *env, jobject thiz) {
    char* str=new char[20];
    for(int i=0;i<20;i++)
    {
        str[i]='0'+i;
    }
    return env->NewStringUTF(str);
}