/*******************************************************************************
* Copyright (C) 2011, FuseSource Corp.  All rights reserved.
*
*     http://fusesource.com
*
* The software in this package is published under the terms of the
* CDDL license a copy of which has been included with this distribution
* in the license.txt file.
*******************************************************************************/
#include "rocksdbjni.h"

void cacheCompactionFilterJNIFields(JNIEnv *env, jobject lpObject);
struct JNICompactionFilter *getCompactionFilterJNIFields(JNIEnv *env, jobject lpObject, struct JNICompactionFilter *lpStruct);
void setCompactionFilterJNIFields(JNIEnv *env, jobject lpObject, struct JNICompactionFilter *lpStruct);

void cacheComparatorJNIFields(JNIEnv *env, jobject lpObject);
struct JNIComparator *getComparatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIComparator *lpStruct);
void setComparatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIComparator *lpStruct);

void cacheLoggerJNIFields(JNIEnv *env, jobject lpObject);
struct JNILogger *getLoggerJNIFields(JNIEnv *env, jobject lpObject, struct JNILogger *lpStruct);
void setLoggerJNIFields(JNIEnv *env, jobject lpObject, struct JNILogger *lpStruct);

void cacheMergeOperatorJNIFields(JNIEnv *env, jobject lpObject);
struct JNIMergeOperator *getMergeOperatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIMergeOperator *lpStruct);
void setMergeOperatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIMergeOperator *lpStruct);

void cacheNativeOptionsFields(JNIEnv *env, jobject lpObject);
struct rocksdb::Options *getNativeOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::Options *lpStruct);
void setNativeOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::Options *lpStruct);

void cacheRangeJNIFields(JNIEnv *env, jobject lpObject);
struct rocksdb::Range *getRangeJNIFields(JNIEnv *env, jobject lpObject, struct rocksdb::Range *lpStruct);
void setRangeJNIFields(JNIEnv *env, jobject lpObject, struct rocksdb::Range *lpStruct);

void cacheNativeReadOptionsFields(JNIEnv *env, jobject lpObject);
struct rocksdb::ReadOptions *getNativeReadOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::ReadOptions *lpStruct);
void setNativeReadOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::ReadOptions *lpStruct);

void cacheNativeSliceFields(JNIEnv *env, jobject lpObject);
struct rocksdb::Slice *getNativeSliceFields(JNIEnv *env, jobject lpObject, struct rocksdb::Slice *lpStruct);
void setNativeSliceFields(JNIEnv *env, jobject lpObject, struct rocksdb::Slice *lpStruct);

void cacheNativeWriteOptionsFields(JNIEnv *env, jobject lpObject);
struct rocksdb::WriteOptions *getNativeWriteOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::WriteOptions *lpStruct);
void setNativeWriteOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::WriteOptions *lpStruct);

