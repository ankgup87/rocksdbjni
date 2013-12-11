
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
#include "rocksdb/cache.h"
#include "rocksdb/filter_policy.h"

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

      env->ReleaseByteArrayElements(rc, b, 0);
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
        env->SetLongArrayRegion(buf, i, 1, &longPtr);
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
        env->ReleaseByteArrayElements(rc, b, 0);
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

struct JNILRUCache : public rocksdb::Cache {
  jlong size;
  std::shared_ptr<rocksdb::Cache> lruCache;

  rocksdb::Cache::Handle* Insert(const rocksdb::Slice& key, void* value, size_t charge,
                 void (*deleter)(const rocksdb::Slice& key, void* value)){
    return getCachePtr()->Insert(key, value, charge, deleter);
  }

  rocksdb::Cache::Handle* Lookup(const rocksdb::Slice& key)
  {
    return getCachePtr()->Lookup(key);
  }

  void Release(rocksdb::Cache::Handle* handle)
  {
    getCachePtr()->Release(handle);
  }

  void* Value(rocksdb::Cache::Handle* handle)
  {
    return getCachePtr()->Value(handle);
  }

  void Erase(const rocksdb::Slice& key)
  {
    return getCachePtr()->Erase(key);
  }

  uint64_t NewId()
  {
    return getCachePtr()->NewId();
  }

  size_t GetCapacity()
  {
    return getCachePtr()->GetCapacity();
  }

  std::shared_ptr<rocksdb::Cache> getCachePtr()
  {
    if(lruCache.get() == NULL)
    {
      lruCache = rocksdb::NewLRUCache(size);
    }

    return lruCache;
  }
};

struct JNIBloomFilter : public rocksdb::FilterPolicy {
  jint bits_per_key;
  const mutable rocksdb::FilterPolicy* filterPolicy;
  const char *name;

  const char* Name() const
  {
    return name;
  }

  void CreateFilter(const rocksdb::Slice* keys, int n, std::string* dst) const
  {
    getFilterPolicyPtr()->CreateFilter(keys, n, dst);
  }

  bool KeyMayMatch(const rocksdb::Slice& key, const rocksdb::Slice& filter) const
  {
    return getFilterPolicyPtr()->KeyMayMatch(key, filter);
  }

  const rocksdb::FilterPolicy* getFilterPolicyPtr() const
  {
    if(filterPolicy == NULL)
    {
      filterPolicy = rocksdb::NewBloomFilterPolicy(bits_per_key);
    }

    return filterPolicy;
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
