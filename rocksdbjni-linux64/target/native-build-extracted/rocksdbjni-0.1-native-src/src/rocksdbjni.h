/*******************************************************************************
 * Copyright (C) 2011, FuseSource Corp.  All rights reserved.
 *
 *     http://fusesource.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *    * Neither the name of FuseSource Corp. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
#ifndef ROCKSDBJNI_H
#define ROCKSDBJNI_H

#ifdef HAVE_CONFIG_H
  /* configure based build.. we will use what it discovered about the platform */
  #include "config.h"
#endif
#if defined(_WIN32) || defined(_WIN64)
    /* Windows based build */
    #define _WIN32_WINNT 0x0501
    #include <windows.h>
#endif
#if !defined(HAVE_CONFIG_H) && (defined(_WIN32) || defined(_WIN64))
    #define HAVE_STDLIB_H 1
    #define HAVE_STRINGS_H 1
#endif

#ifdef HAVE_UNISTD_H
  #include <unistd.h>
#endif

#ifdef HAVE_STDLIB_H
  #include <stdlib.h>
#endif

#ifdef HAVE_STRINGS_H
  #include <string.h>
#endif

#ifdef HAVE_SYS_ERRNO_H
  #include <sys/errno.h>
#endif

#include "hawtjni.h"
#include <stdint.h>
#include <stdarg.h>

#ifdef __cplusplus

#include "rocksdb/db.h"
#include "rocksdb/options.h"
#include "rocksdb/write_batch.h"
#include "rocksdb/cache.h"
#include "rocksdb/comparator.h"
#include "rocksdb/env.h"
#include "rocksdb/slice.h"
#include "rocksdb/merge_operator.h"
#include "rocksdb/compaction_filter.h"

struct JNIComparator : public rocksdb::Comparator {
  jobject target;
  jmethodID compare_method;
  const char *name;

  int Compare(const rocksdb::Slice& a, const rocksdb::Slice& b) const {
    JNIEnv *env;
    if ( hawtjni_attach_thread(&env, "rocksdb") ) {
      return 0;
    }
    int rc = env->CallIntMethod(target, compare_method, (jlong)(intptr_t)&a, (jlong)(intptr_t)&b);
    hawtjni_detach_thread();
    return rc;
  }

  const char* Name() const {
     return name;
  }

  void FindShortestSeparator(std::string*, const rocksdb::Slice&) const { }
  void FindShortSuccessor(std::string*) const { }
};

struct JNICompactionFilter : public rocksdb::CompactionFilter {
  jobject target;
  jmethodID filter_method;
  const char *name;

  bool Filter(int level,
              const rocksdb::Slice& key,
              const rocksdb::Slice& existing_value,
              std::string* new_value,
              bool* value_changed) const {
    JNIEnv *env;
    if ( hawtjni_attach_thread(&env, "rocksdb") ) {
      return 0;
    }
    jboolean rc = env->CallBooleanMethod(target, filter_method, (jlong)(intptr_t)&key, (jlong)(intptr_t)&existing_value);
    hawtjni_detach_thread();
    return rc;
  }

  const char* Name() const {
     return name;
  }
};

struct JNILogger : public rocksdb::Logger {
  jobject target;
  jmethodID log_method;

  void Logv(const char* format, va_list ap) {
    JNIEnv *env;
    if ( hawtjni_attach_thread(&env, "rocksdb") ) {
      return;
    }

    char buffer[1024];
    vsnprintf(buffer, sizeof(buffer), format, ap);

    jstring message = env->NewStringUTF(buffer);
    if( message ) {
      env->CallVoidMethod(target, log_method, message);
      env->DeleteLocalRef(message);
    }

    if (env->ExceptionOccurred() ) {
      env->ExceptionDescribe();
      env->ExceptionClear();
    }
    hawtjni_detach_thread();
  }

};

struct JNIMergeOperator : public rocksdb::MergeOperator {
  jobject target;
  jmethodID partial_merge_method;
  jmethodID full_merge_method;
  const char *name;

  bool PartialMerge(const rocksdb::Slice& key,
                    const rocksdb::Slice& left_operand,
                    const rocksdb::Slice& right_operand,
                    std::string* new_value,
                    rocksdb::Logger* logger) const {
    JNIEnv *env;
    if ( hawtjni_attach_thread(&env, "rocksdb") ) {
      return 0;
    }

    jbyteArray rc = (jbyteArray)env->CallObjectMethod(target, partial_merge_method, (jlong)(intptr_t)&key, (jlong)(intptr_t)&left_operand, (jlong)(intptr_t)&right_operand);

    bool retCode = rc != NULL;
    if(rc != NULL)
    {
      jbyte* b = env->GetByteArrayElements(rc, NULL);

      if(new_value == NULL)
      {
        std::string s = std::string((char* ) b);
        new_value = &s;
      }
      else
      {
        new_value->assign((char* ) b, env->GetArrayLength(rc));
      }

      env->ReleaseByteArrayElements(rc, b, JNI_ABORT);
      env->DeleteLocalRef(rc);
    }

    hawtjni_detach_thread();
    return retCode;
  }

  bool FullMerge(const rocksdb::Slice& key,
                 const rocksdb::Slice* existing_value,
                 const std::deque<std::string>& operand_list,
                 std::string* new_value,
                 rocksdb::Logger* logger) const {
      JNIEnv *env;
      if ( hawtjni_attach_thread(&env, "rocksdb") ) {
        return 0;
      }

      jlong existingPtr;
      if(existing_value == NULL)
      {
        existingPtr = 0;
      }
      else
      {
        existingPtr = (jlong)(intptr_t)existing_value;
      }

      jlongArray buf = env->NewLongArray(operand_list.size());

      rocksdb::Slice* sArray = new rocksdb::Slice[operand_list.size()];
      for(int i = 0; i < operand_list.size(); i++)
      {
        sArray[i] = rocksdb::Slice(operand_list[i]);
        jlong longPtr = (jlong)(intptr_t)&sArray[i];
        env->SetLongArrayRegion(buf, i, 1, &longPtr);;
      }

      jbyteArray rc = (jbyteArray)env->CallObjectMethod(target, full_merge_method, (jlong)(intptr_t)&key, existingPtr, buf);

      bool retCode = rc != NULL;
      if(rc != NULL)
      {
        jbyte* b = env->GetByteArrayElements(rc, NULL); 
        if(new_value == NULL)
        {
          std::string s = std::string((char* ) b);
          new_value = &s;
        }
        else
        {
          new_value->assign((char* ) b, env->GetArrayLength(rc));
        }
        env->ReleaseByteArrayElements(rc, b, JNI_ABORT);
        env->DeleteLocalRef(rc);
      }

      delete[] sArray;
      env->DeleteLocalRef(buf);

      hawtjni_detach_thread();
      return retCode;
    }

    const char* Name() const {
       return name;
    }
};


#endif


#ifdef __cplusplus
extern "C" {
#endif

void buffer_copy(const void *source, size_t source_pos, void *dest, size_t dest_pos, size_t length);

#ifdef __cplusplus
} /* extern "C" */
#endif


#endif /* ROCKSDBJNI_H */
