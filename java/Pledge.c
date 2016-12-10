/*
 * Copyright (c) 2016 Sam Hart. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the University nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */

/*
  javah Pledge

  gcc -shared -fPIC -I $JAVA_HOME/include/ -I $JAVA_HOME/include/openbsd/ \
  Pledge.c -o libPledge.so
 */

#include <err.h>
#include "Pledge.h"
#include <stdlib.h>
#include <unistd.h>

const char *pledge_chars = "pledge";

JNIEXPORT void JNICALL Java_Pledge_pledge
(JNIEnv *env,jobject obj,jstring promises,jobjectArray paths)
{
  const char *nativeString = (*env)->GetStringUTFChars(env,promises,0);

  jint arraySize = paths == NULL ? 0 : (*env)->GetArrayLength(env,paths);
  if(0 != arraySize)
  {
    int i;
    const char *strings[arraySize];

    for(i = 0;i<arraySize;i++)
    {
      jstring string = (jstring)(*env)->GetObjectArrayElement(env,paths,i);
      strings[i] = (*env)->GetStringUTFChars(env,string,0);
    }

    if(-1 == pledge(nativeString,strings))
      err(EXIT_FAILURE,pledge_chars);

    for(i = 0;i<arraySize;i++)
    {
      jstring string = (jstring)(*env)->GetObjectArrayElement(env,paths,i);
      (*env)->ReleaseStringUTFChars(env,string,strings[i]);
    }
  }
  else
  {
    if(-1 == pledge(nativeString,NULL))
      err(EXIT_FAILURE,pledge_chars);
  }
  
  (*env)->ReleaseStringUTFChars(env,promises,nativeString);

  return;
}
