/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

static jint fd;  
/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 */

JNIEXPORT jint JNICALL
Java_com_example_embedingsysterm2_JNI_1interface_LedJniObject_LedDeviceOpen(JNIEnv* env, jobject thiz,
						               jstring path)
{
	/* Opening device */
	jboolean iscopy;
	const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
	fd = open(path_utf, O_RDWR | O_SYNC);
	(*env)->ReleaseStringUTFChars(env, path, path_utf);
	if (fd == -1)
		return -1;	/* TODO: throw an exception */
    	return fd;
}

JNIEXPORT void JNICALL
Java_com_example_embedingsysterm2_JNI_1interface_LedJniObject_LedDeviceIoctl( JNIEnv* env,jobject thiz,
				                          jint cmd, jint arg)
{
	ioctl(fd,cmd,arg);
}

JNIEXPORT void JNICALL
Java_com_example_embedingsysterm2_JNI_1interface_LedJniObject_LedDeviceClose( JNIEnv* env, jobject thiz)
{
    	close(fd);
}

