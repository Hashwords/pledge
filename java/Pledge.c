/*
 * Copyright (c) 2016 Sam Hart. All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
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
