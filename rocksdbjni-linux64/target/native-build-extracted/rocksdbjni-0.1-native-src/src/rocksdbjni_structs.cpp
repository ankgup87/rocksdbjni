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
#include "hawtjni.h"
#include "rocksdbjni_structs.h"

typedef struct CompactionFilterJNI_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, filter_method, name;
} CompactionFilterJNI_FID_CACHE;

CompactionFilterJNI_FID_CACHE CompactionFilterJNIFc;

void cacheCompactionFilterJNIFields(JNIEnv *env, jobject lpObject)
{
	if (CompactionFilterJNIFc.cached) return;
	CompactionFilterJNIFc.clazz = env->GetObjectClass(lpObject);
	CompactionFilterJNIFc.target = env->GetFieldID(CompactionFilterJNIFc.clazz, "target", "J");
	CompactionFilterJNIFc.filter_method = env->GetFieldID(CompactionFilterJNIFc.clazz, "filter_method", "J");
	CompactionFilterJNIFc.name = env->GetFieldID(CompactionFilterJNIFc.clazz, "name", "J");
	hawtjni_w_barrier();
	CompactionFilterJNIFc.cached = 1;
}

struct JNICompactionFilter *getCompactionFilterJNIFields(JNIEnv *env, jobject lpObject, struct JNICompactionFilter *lpStruct)
{
	if (!CompactionFilterJNIFc.cached) cacheCompactionFilterJNIFields(env, lpObject);
	lpStruct->target = (jobject)(intptr_t)env->GetLongField(lpObject, CompactionFilterJNIFc.target);
	lpStruct->filter_method = (jmethodID)(intptr_t)env->GetLongField(lpObject, CompactionFilterJNIFc.filter_method);
	lpStruct->name = (const char *)(intptr_t)env->GetLongField(lpObject, CompactionFilterJNIFc.name);
	return lpStruct;
}

void setCompactionFilterJNIFields(JNIEnv *env, jobject lpObject, struct JNICompactionFilter *lpStruct)
{
	if (!CompactionFilterJNIFc.cached) cacheCompactionFilterJNIFields(env, lpObject);
	env->SetLongField(lpObject, CompactionFilterJNIFc.target, (jlong)(intptr_t)lpStruct->target);
	env->SetLongField(lpObject, CompactionFilterJNIFc.filter_method, (jlong)(intptr_t)lpStruct->filter_method);
	env->SetLongField(lpObject, CompactionFilterJNIFc.name, (jlong)(intptr_t)lpStruct->name);
}

typedef struct ComparatorJNI_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, compare_method, name;
} ComparatorJNI_FID_CACHE;

ComparatorJNI_FID_CACHE ComparatorJNIFc;

void cacheComparatorJNIFields(JNIEnv *env, jobject lpObject)
{
	if (ComparatorJNIFc.cached) return;
	ComparatorJNIFc.clazz = env->GetObjectClass(lpObject);
	ComparatorJNIFc.target = env->GetFieldID(ComparatorJNIFc.clazz, "target", "J");
	ComparatorJNIFc.compare_method = env->GetFieldID(ComparatorJNIFc.clazz, "compare_method", "J");
	ComparatorJNIFc.name = env->GetFieldID(ComparatorJNIFc.clazz, "name", "J");
	hawtjni_w_barrier();
	ComparatorJNIFc.cached = 1;
}

struct JNIComparator *getComparatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIComparator *lpStruct)
{
	if (!ComparatorJNIFc.cached) cacheComparatorJNIFields(env, lpObject);
	lpStruct->target = (jobject)(intptr_t)env->GetLongField(lpObject, ComparatorJNIFc.target);
	lpStruct->compare_method = (jmethodID)(intptr_t)env->GetLongField(lpObject, ComparatorJNIFc.compare_method);
	lpStruct->name = (const char *)(intptr_t)env->GetLongField(lpObject, ComparatorJNIFc.name);
	return lpStruct;
}

void setComparatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIComparator *lpStruct)
{
	if (!ComparatorJNIFc.cached) cacheComparatorJNIFields(env, lpObject);
	env->SetLongField(lpObject, ComparatorJNIFc.target, (jlong)(intptr_t)lpStruct->target);
	env->SetLongField(lpObject, ComparatorJNIFc.compare_method, (jlong)(intptr_t)lpStruct->compare_method);
	env->SetLongField(lpObject, ComparatorJNIFc.name, (jlong)(intptr_t)lpStruct->name);
}

typedef struct LoggerJNI_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, log_method;
} LoggerJNI_FID_CACHE;

LoggerJNI_FID_CACHE LoggerJNIFc;

void cacheLoggerJNIFields(JNIEnv *env, jobject lpObject)
{
	if (LoggerJNIFc.cached) return;
	LoggerJNIFc.clazz = env->GetObjectClass(lpObject);
	LoggerJNIFc.target = env->GetFieldID(LoggerJNIFc.clazz, "target", "J");
	LoggerJNIFc.log_method = env->GetFieldID(LoggerJNIFc.clazz, "log_method", "J");
	hawtjni_w_barrier();
	LoggerJNIFc.cached = 1;
}

struct JNILogger *getLoggerJNIFields(JNIEnv *env, jobject lpObject, struct JNILogger *lpStruct)
{
	if (!LoggerJNIFc.cached) cacheLoggerJNIFields(env, lpObject);
	lpStruct->target = (jobject)(intptr_t)env->GetLongField(lpObject, LoggerJNIFc.target);
	lpStruct->log_method = (jmethodID)(intptr_t)env->GetLongField(lpObject, LoggerJNIFc.log_method);
	return lpStruct;
}

void setLoggerJNIFields(JNIEnv *env, jobject lpObject, struct JNILogger *lpStruct)
{
	if (!LoggerJNIFc.cached) cacheLoggerJNIFields(env, lpObject);
	env->SetLongField(lpObject, LoggerJNIFc.target, (jlong)(intptr_t)lpStruct->target);
	env->SetLongField(lpObject, LoggerJNIFc.log_method, (jlong)(intptr_t)lpStruct->log_method);
}

typedef struct MergeOperatorJNI_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, partial_merge_method, full_merge_method, name;
} MergeOperatorJNI_FID_CACHE;

MergeOperatorJNI_FID_CACHE MergeOperatorJNIFc;

void cacheMergeOperatorJNIFields(JNIEnv *env, jobject lpObject)
{
	if (MergeOperatorJNIFc.cached) return;
	MergeOperatorJNIFc.clazz = env->GetObjectClass(lpObject);
	MergeOperatorJNIFc.target = env->GetFieldID(MergeOperatorJNIFc.clazz, "target", "J");
	MergeOperatorJNIFc.partial_merge_method = env->GetFieldID(MergeOperatorJNIFc.clazz, "partial_merge_method", "J");
	MergeOperatorJNIFc.full_merge_method = env->GetFieldID(MergeOperatorJNIFc.clazz, "full_merge_method", "J");
	MergeOperatorJNIFc.name = env->GetFieldID(MergeOperatorJNIFc.clazz, "name", "J");
	hawtjni_w_barrier();
	MergeOperatorJNIFc.cached = 1;
}

struct JNIMergeOperator *getMergeOperatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIMergeOperator *lpStruct)
{
	if (!MergeOperatorJNIFc.cached) cacheMergeOperatorJNIFields(env, lpObject);
	lpStruct->target = (jobject)(intptr_t)env->GetLongField(lpObject, MergeOperatorJNIFc.target);
	lpStruct->partial_merge_method = (jmethodID)(intptr_t)env->GetLongField(lpObject, MergeOperatorJNIFc.partial_merge_method);
	lpStruct->full_merge_method = (jmethodID)(intptr_t)env->GetLongField(lpObject, MergeOperatorJNIFc.full_merge_method);
	lpStruct->name = (const char *)(intptr_t)env->GetLongField(lpObject, MergeOperatorJNIFc.name);
	return lpStruct;
}

void setMergeOperatorJNIFields(JNIEnv *env, jobject lpObject, struct JNIMergeOperator *lpStruct)
{
	if (!MergeOperatorJNIFc.cached) cacheMergeOperatorJNIFields(env, lpObject);
	env->SetLongField(lpObject, MergeOperatorJNIFc.target, (jlong)(intptr_t)lpStruct->target);
	env->SetLongField(lpObject, MergeOperatorJNIFc.partial_merge_method, (jlong)(intptr_t)lpStruct->partial_merge_method);
	env->SetLongField(lpObject, MergeOperatorJNIFc.full_merge_method, (jlong)(intptr_t)lpStruct->full_merge_method);
	env->SetLongField(lpObject, MergeOperatorJNIFc.name, (jlong)(intptr_t)lpStruct->name);
}

typedef struct NativeOptions_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID create_if_missing, error_if_exists, paranoid_checks, disable_seek_compaction, write_buffer_size, block_size, max_open_files, block_restart_interval, num_levels, level0_file_num_compaction_trigger, level0_stop_writes_trigger, target_file_size_base, max_bytes_for_level_base, source_compaction_factor, max_grandparent_overlap_factor, max_background_compactions, disable_auto_compactions, delete_obsolete_files_period_micros, level0_slowdown_writes_trigger, max_write_buffer_number, comparator, info_log, env, block_cache, compression, merge_operator, compaction_filter;
} NativeOptions_FID_CACHE;

NativeOptions_FID_CACHE NativeOptionsFc;

void cacheNativeOptionsFields(JNIEnv *env, jobject lpObject)
{
	if (NativeOptionsFc.cached) return;
	NativeOptionsFc.clazz = env->GetObjectClass(lpObject);
	NativeOptionsFc.create_if_missing = env->GetFieldID(NativeOptionsFc.clazz, "create_if_missing", "Z");
	NativeOptionsFc.error_if_exists = env->GetFieldID(NativeOptionsFc.clazz, "error_if_exists", "Z");
	NativeOptionsFc.paranoid_checks = env->GetFieldID(NativeOptionsFc.clazz, "paranoid_checks", "Z");
	NativeOptionsFc.disable_seek_compaction = env->GetFieldID(NativeOptionsFc.clazz, "disable_seek_compaction", "Z");
	NativeOptionsFc.write_buffer_size = env->GetFieldID(NativeOptionsFc.clazz, "write_buffer_size", "J");
	NativeOptionsFc.block_size = env->GetFieldID(NativeOptionsFc.clazz, "block_size", "J");
	NativeOptionsFc.max_open_files = env->GetFieldID(NativeOptionsFc.clazz, "max_open_files", "I");
	NativeOptionsFc.block_restart_interval = env->GetFieldID(NativeOptionsFc.clazz, "block_restart_interval", "I");
	NativeOptionsFc.num_levels = env->GetFieldID(NativeOptionsFc.clazz, "num_levels", "I");
	NativeOptionsFc.level0_file_num_compaction_trigger = env->GetFieldID(NativeOptionsFc.clazz, "level0_file_num_compaction_trigger", "I");
	NativeOptionsFc.level0_stop_writes_trigger = env->GetFieldID(NativeOptionsFc.clazz, "level0_stop_writes_trigger", "I");
	NativeOptionsFc.target_file_size_base = env->GetFieldID(NativeOptionsFc.clazz, "target_file_size_base", "I");
	NativeOptionsFc.max_bytes_for_level_base = env->GetFieldID(NativeOptionsFc.clazz, "max_bytes_for_level_base", "I");
	NativeOptionsFc.source_compaction_factor = env->GetFieldID(NativeOptionsFc.clazz, "source_compaction_factor", "I");
	NativeOptionsFc.max_grandparent_overlap_factor = env->GetFieldID(NativeOptionsFc.clazz, "max_grandparent_overlap_factor", "I");
	NativeOptionsFc.max_background_compactions = env->GetFieldID(NativeOptionsFc.clazz, "max_background_compactions", "I");
	NativeOptionsFc.disable_auto_compactions = env->GetFieldID(NativeOptionsFc.clazz, "disable_auto_compactions", "Z");
	NativeOptionsFc.delete_obsolete_files_period_micros = env->GetFieldID(NativeOptionsFc.clazz, "delete_obsolete_files_period_micros", "J");
	NativeOptionsFc.level0_slowdown_writes_trigger = env->GetFieldID(NativeOptionsFc.clazz, "level0_slowdown_writes_trigger", "I");
	NativeOptionsFc.max_write_buffer_number = env->GetFieldID(NativeOptionsFc.clazz, "max_write_buffer_number", "I");
	NativeOptionsFc.comparator = env->GetFieldID(NativeOptionsFc.clazz, "comparator", "J");
	NativeOptionsFc.info_log = env->GetFieldID(NativeOptionsFc.clazz, "info_log", "J");
	NativeOptionsFc.env = env->GetFieldID(NativeOptionsFc.clazz, "env", "J");
	NativeOptionsFc.block_cache = env->GetFieldID(NativeOptionsFc.clazz, "block_cache", "J");
	NativeOptionsFc.compression = env->GetFieldID(NativeOptionsFc.clazz, "compression", "I");
	NativeOptionsFc.merge_operator = env->GetFieldID(NativeOptionsFc.clazz, "merge_operator", "J");
	NativeOptionsFc.compaction_filter = env->GetFieldID(NativeOptionsFc.clazz, "compaction_filter", "J");
	hawtjni_w_barrier();
	NativeOptionsFc.cached = 1;
}

struct rocksdb::Options *getNativeOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::Options *lpStruct)
{
	if (!NativeOptionsFc.cached) cacheNativeOptionsFields(env, lpObject);
	lpStruct->create_if_missing = env->GetBooleanField(lpObject, NativeOptionsFc.create_if_missing);
	lpStruct->error_if_exists = env->GetBooleanField(lpObject, NativeOptionsFc.error_if_exists);
	lpStruct->paranoid_checks = env->GetBooleanField(lpObject, NativeOptionsFc.paranoid_checks);
	lpStruct->disable_seek_compaction = env->GetBooleanField(lpObject, NativeOptionsFc.disable_seek_compaction);
	lpStruct->write_buffer_size = (size_t)env->GetLongField(lpObject, NativeOptionsFc.write_buffer_size);
	lpStruct->block_size = (size_t)env->GetLongField(lpObject, NativeOptionsFc.block_size);
	lpStruct->max_open_files = env->GetIntField(lpObject, NativeOptionsFc.max_open_files);
	lpStruct->block_restart_interval = env->GetIntField(lpObject, NativeOptionsFc.block_restart_interval);
	lpStruct->num_levels = env->GetIntField(lpObject, NativeOptionsFc.num_levels);
	lpStruct->level0_file_num_compaction_trigger = env->GetIntField(lpObject, NativeOptionsFc.level0_file_num_compaction_trigger);
	lpStruct->level0_stop_writes_trigger = env->GetIntField(lpObject, NativeOptionsFc.level0_stop_writes_trigger);
	lpStruct->target_file_size_base = env->GetIntField(lpObject, NativeOptionsFc.target_file_size_base);
	lpStruct->max_bytes_for_level_base = env->GetIntField(lpObject, NativeOptionsFc.max_bytes_for_level_base);
	lpStruct->source_compaction_factor = env->GetIntField(lpObject, NativeOptionsFc.source_compaction_factor);
	lpStruct->max_grandparent_overlap_factor = env->GetIntField(lpObject, NativeOptionsFc.max_grandparent_overlap_factor);
	lpStruct->max_background_compactions = env->GetIntField(lpObject, NativeOptionsFc.max_background_compactions);
	lpStruct->disable_auto_compactions = env->GetBooleanField(lpObject, NativeOptionsFc.disable_auto_compactions);
	lpStruct->delete_obsolete_files_period_micros = (uint64_t)env->GetLongField(lpObject, NativeOptionsFc.delete_obsolete_files_period_micros);
	lpStruct->level0_slowdown_writes_trigger = env->GetIntField(lpObject, NativeOptionsFc.level0_slowdown_writes_trigger);
	lpStruct->max_write_buffer_number = env->GetIntField(lpObject, NativeOptionsFc.max_write_buffer_number);
	lpStruct->comparator = (const rocksdb::Comparator*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.comparator);
	lpStruct->info_log = (rocksdb::Logger*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.info_log);
	lpStruct->env = (rocksdb::Env*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.env);
	lpStruct->block_cache = (rocksdb::Cache*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.block_cache);
	lpStruct->compression = (rocksdb::CompressionType)env->GetIntField(lpObject, NativeOptionsFc.compression);
	lpStruct->merge_operator = (rocksdb::MergeOperator*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.merge_operator);
	lpStruct->compaction_filter = (rocksdb::CompactionFilter*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.compaction_filter);
	return lpStruct;
}

void setNativeOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::Options *lpStruct)
{
	if (!NativeOptionsFc.cached) cacheNativeOptionsFields(env, lpObject);
	env->SetBooleanField(lpObject, NativeOptionsFc.create_if_missing, (jboolean)lpStruct->create_if_missing);
	env->SetBooleanField(lpObject, NativeOptionsFc.error_if_exists, (jboolean)lpStruct->error_if_exists);
	env->SetBooleanField(lpObject, NativeOptionsFc.paranoid_checks, (jboolean)lpStruct->paranoid_checks);
	env->SetBooleanField(lpObject, NativeOptionsFc.disable_seek_compaction, (jboolean)lpStruct->disable_seek_compaction);
	env->SetLongField(lpObject, NativeOptionsFc.write_buffer_size, (jlong)lpStruct->write_buffer_size);
	env->SetLongField(lpObject, NativeOptionsFc.block_size, (jlong)lpStruct->block_size);
	env->SetIntField(lpObject, NativeOptionsFc.max_open_files, (jint)lpStruct->max_open_files);
	env->SetIntField(lpObject, NativeOptionsFc.block_restart_interval, (jint)lpStruct->block_restart_interval);
	env->SetIntField(lpObject, NativeOptionsFc.num_levels, (jint)lpStruct->num_levels);
	env->SetIntField(lpObject, NativeOptionsFc.level0_file_num_compaction_trigger, (jint)lpStruct->level0_file_num_compaction_trigger);
	env->SetIntField(lpObject, NativeOptionsFc.level0_stop_writes_trigger, (jint)lpStruct->level0_stop_writes_trigger);
	env->SetIntField(lpObject, NativeOptionsFc.target_file_size_base, (jint)lpStruct->target_file_size_base);
	env->SetIntField(lpObject, NativeOptionsFc.max_bytes_for_level_base, (jint)lpStruct->max_bytes_for_level_base);
	env->SetIntField(lpObject, NativeOptionsFc.source_compaction_factor, (jint)lpStruct->source_compaction_factor);
	env->SetIntField(lpObject, NativeOptionsFc.max_grandparent_overlap_factor, (jint)lpStruct->max_grandparent_overlap_factor);
	env->SetIntField(lpObject, NativeOptionsFc.max_background_compactions, (jint)lpStruct->max_background_compactions);
	env->SetBooleanField(lpObject, NativeOptionsFc.disable_auto_compactions, (jboolean)lpStruct->disable_auto_compactions);
	env->SetLongField(lpObject, NativeOptionsFc.delete_obsolete_files_period_micros, (jlong)lpStruct->delete_obsolete_files_period_micros);
	env->SetIntField(lpObject, NativeOptionsFc.level0_slowdown_writes_trigger, (jint)lpStruct->level0_slowdown_writes_trigger);
	env->SetIntField(lpObject, NativeOptionsFc.max_write_buffer_number, (jint)lpStruct->max_write_buffer_number);
	env->SetLongField(lpObject, NativeOptionsFc.comparator, (jlong)(intptr_t)lpStruct->comparator);
	env->SetLongField(lpObject, NativeOptionsFc.info_log, (jlong)(intptr_t)lpStruct->info_log);
	env->SetLongField(lpObject, NativeOptionsFc.env, (jlong)(intptr_t)lpStruct->env);
	env->SetLongField(lpObject, NativeOptionsFc.block_cache, (jlong)(intptr_t)lpStruct->block_cache);
	env->SetIntField(lpObject, NativeOptionsFc.compression, (jint)lpStruct->compression);
	env->SetLongField(lpObject, NativeOptionsFc.merge_operator, (jlong)(intptr_t)lpStruct->merge_operator);
	env->SetLongField(lpObject, NativeOptionsFc.compaction_filter, (jlong)(intptr_t)lpStruct->compaction_filter);
}

typedef struct RangeJNI_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID start, limit;
} RangeJNI_FID_CACHE;

RangeJNI_FID_CACHE RangeJNIFc;

void cacheRangeJNIFields(JNIEnv *env, jobject lpObject)
{
	if (RangeJNIFc.cached) return;
	RangeJNIFc.clazz = env->GetObjectClass(lpObject);
	RangeJNIFc.start = env->GetFieldID(RangeJNIFc.clazz, "start", "Lorg/fusesource/rocksdbjni/internal/NativeSlice;");
	RangeJNIFc.limit = env->GetFieldID(RangeJNIFc.clazz, "limit", "Lorg/fusesource/rocksdbjni/internal/NativeSlice;");
	hawtjni_w_barrier();
	RangeJNIFc.cached = 1;
}

struct rocksdb::Range *getRangeJNIFields(JNIEnv *env, jobject lpObject, struct rocksdb::Range *lpStruct)
{
	if (!RangeJNIFc.cached) cacheRangeJNIFields(env, lpObject);
	{
	jobject lpObject1 = env->GetObjectField(lpObject, RangeJNIFc.start);
	if (lpObject1 != NULL) getNativeSliceFields(env, lpObject1, &lpStruct->start);
	}
	{
	jobject lpObject1 = env->GetObjectField(lpObject, RangeJNIFc.limit);
	if (lpObject1 != NULL) getNativeSliceFields(env, lpObject1, &lpStruct->limit);
	}
	return lpStruct;
}

void setRangeJNIFields(JNIEnv *env, jobject lpObject, struct rocksdb::Range *lpStruct)
{
	if (!RangeJNIFc.cached) cacheRangeJNIFields(env, lpObject);
	{
	jobject lpObject1 = env->GetObjectField(lpObject, RangeJNIFc.start);
	if (lpObject1 != NULL) setNativeSliceFields(env, lpObject1, &lpStruct->start);
	}
	{
	jobject lpObject1 = env->GetObjectField(lpObject, RangeJNIFc.limit);
	if (lpObject1 != NULL) setNativeSliceFields(env, lpObject1, &lpStruct->limit);
	}
}

typedef struct NativeReadOptions_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID verify_checksums, fill_cache, snapshot;
} NativeReadOptions_FID_CACHE;

NativeReadOptions_FID_CACHE NativeReadOptionsFc;

void cacheNativeReadOptionsFields(JNIEnv *env, jobject lpObject)
{
	if (NativeReadOptionsFc.cached) return;
	NativeReadOptionsFc.clazz = env->GetObjectClass(lpObject);
	NativeReadOptionsFc.verify_checksums = env->GetFieldID(NativeReadOptionsFc.clazz, "verify_checksums", "Z");
	NativeReadOptionsFc.fill_cache = env->GetFieldID(NativeReadOptionsFc.clazz, "fill_cache", "Z");
	NativeReadOptionsFc.snapshot = env->GetFieldID(NativeReadOptionsFc.clazz, "snapshot", "J");
	hawtjni_w_barrier();
	NativeReadOptionsFc.cached = 1;
}

struct rocksdb::ReadOptions *getNativeReadOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::ReadOptions *lpStruct)
{
	if (!NativeReadOptionsFc.cached) cacheNativeReadOptionsFields(env, lpObject);
	lpStruct->verify_checksums = env->GetBooleanField(lpObject, NativeReadOptionsFc.verify_checksums);
	lpStruct->fill_cache = env->GetBooleanField(lpObject, NativeReadOptionsFc.fill_cache);
	lpStruct->snapshot = (const rocksdb::Snapshot*)(intptr_t)env->GetLongField(lpObject, NativeReadOptionsFc.snapshot);
	return lpStruct;
}

void setNativeReadOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::ReadOptions *lpStruct)
{
	if (!NativeReadOptionsFc.cached) cacheNativeReadOptionsFields(env, lpObject);
	env->SetBooleanField(lpObject, NativeReadOptionsFc.verify_checksums, (jboolean)lpStruct->verify_checksums);
	env->SetBooleanField(lpObject, NativeReadOptionsFc.fill_cache, (jboolean)lpStruct->fill_cache);
	env->SetLongField(lpObject, NativeReadOptionsFc.snapshot, (jlong)(intptr_t)lpStruct->snapshot);
}

typedef struct NativeSlice_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID data_, size_;
} NativeSlice_FID_CACHE;

NativeSlice_FID_CACHE NativeSliceFc;

void cacheNativeSliceFields(JNIEnv *env, jobject lpObject)
{
	if (NativeSliceFc.cached) return;
	NativeSliceFc.clazz = env->GetObjectClass(lpObject);
	NativeSliceFc.data_ = env->GetFieldID(NativeSliceFc.clazz, "data_", "J");
	NativeSliceFc.size_ = env->GetFieldID(NativeSliceFc.clazz, "size_", "J");
	hawtjni_w_barrier();
	NativeSliceFc.cached = 1;
}

struct rocksdb::Slice *getNativeSliceFields(JNIEnv *env, jobject lpObject, struct rocksdb::Slice *lpStruct)
{
	if (!NativeSliceFc.cached) cacheNativeSliceFields(env, lpObject);
	lpStruct->data_ = (const char*)(intptr_t)env->GetLongField(lpObject, NativeSliceFc.data_);
	lpStruct->size_ = (size_t)env->GetLongField(lpObject, NativeSliceFc.size_);
	return lpStruct;
}

void setNativeSliceFields(JNIEnv *env, jobject lpObject, struct rocksdb::Slice *lpStruct)
{
	if (!NativeSliceFc.cached) cacheNativeSliceFields(env, lpObject);
	env->SetLongField(lpObject, NativeSliceFc.data_, (jlong)(intptr_t)lpStruct->data_);
	env->SetLongField(lpObject, NativeSliceFc.size_, (jlong)lpStruct->size_);
}

typedef struct NativeWriteOptions_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID sync, disableWAL;
} NativeWriteOptions_FID_CACHE;

NativeWriteOptions_FID_CACHE NativeWriteOptionsFc;

void cacheNativeWriteOptionsFields(JNIEnv *env, jobject lpObject)
{
	if (NativeWriteOptionsFc.cached) return;
	NativeWriteOptionsFc.clazz = env->GetObjectClass(lpObject);
	NativeWriteOptionsFc.sync = env->GetFieldID(NativeWriteOptionsFc.clazz, "sync", "Z");
	NativeWriteOptionsFc.disableWAL = env->GetFieldID(NativeWriteOptionsFc.clazz, "disableWAL", "Z");
	hawtjni_w_barrier();
	NativeWriteOptionsFc.cached = 1;
}

struct rocksdb::WriteOptions *getNativeWriteOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::WriteOptions *lpStruct)
{
	if (!NativeWriteOptionsFc.cached) cacheNativeWriteOptionsFields(env, lpObject);
	lpStruct->sync = env->GetBooleanField(lpObject, NativeWriteOptionsFc.sync);
	lpStruct->disableWAL = env->GetBooleanField(lpObject, NativeWriteOptionsFc.disableWAL);
	return lpStruct;
}

void setNativeWriteOptionsFields(JNIEnv *env, jobject lpObject, struct rocksdb::WriteOptions *lpStruct)
{
	if (!NativeWriteOptionsFc.cached) cacheNativeWriteOptionsFields(env, lpObject);
	env->SetBooleanField(lpObject, NativeWriteOptionsFc.sync, (jboolean)lpStruct->sync);
	env->SetBooleanField(lpObject, NativeWriteOptionsFc.disableWAL, (jboolean)lpStruct->disableWAL);
}

