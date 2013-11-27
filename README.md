Get stuff:
---------
---------

wget http://snappy.googlecode.com/files/snappy-1.0.5.tar.gz <br/>
tar -zxvf snappy-1.0.5.tar.gz <br/>
git clone https://github.com/facebook/rocksdb.git <br/>
export SNAPPY_HOME=`cd snappy-1.0.5; pwd` <br/>
export ROCKSDB_HOME=`cd rocksdb; pwd` <br/>
export ROCKSDBJNI_HOME=`cd rocksdbjni; pwd` <br/>

Compile Snappy:
---------
---------
cd ${SNAPPY_HOME} <br/>
./configure --disable-shared --with-pic <br/>
make <br/>

Compile RocksDB:
---------
---------
cd ${ROCKSDB_HOME} <br/>
Add -fPIC flag to CFLAGS and CXXFLAGS in Makefile
compile rocksDB <br/>

Compile JNI:
---------
---------
cd ${ROCKSDBJNI_HOME} <br/>
mvn clean install -P download -P linux64 <br/>

Fix C++ code (hawtjni does not have support for std::shared_ptr and thus we have to fix pointer to shared_ptr conversion):
------------
------------
cd rocksdbjni-linux64/target/native-build <br/>
Fix Makefile by replacing 'CXX = g++' with 'CXX = g++ -std=gnu++11' <br/>
Fix rocksdbjni.cpp by replacing 'rc = (intptr_t)(rocksdb::Cache *)rocksdb::NewLRUCache((size_t)arg0);' with 'rc = (intptr_t)(rocksdb::Cache *)rocksdb::NewLRUCache((size_t)arg0).get();' <br/>

Fix rocksdbjni_structs.cpp: <br/>

Replace 'env->SetLongField(lpObject, NativeOptionsFc.merge_operator, (jlong)(intptr_t)lpStruct->merge_operator);' with 'env->SetLongField(lpObject, NativeOptionsFc.merge_operator, (jlong)(intptr_t)lpStruct->merge_operator.get());' <br/>

Replace 'env->SetLongField(lpObject, NativeOptionsFc.block_cache, (jlong)(intptr_t)lpStruct->block_cache);' with 'env->SetLongField(lpObject, NativeOptionsFc.block_cache, (jlong)(intptr_t)lpStruct->block_cache.get());' <br/>

Replace 'env->SetLongField(lpObject, NativeOptionsFc.info_log, (jlong)(intptr_t)lpStruct->info_log);' with 'env->SetLongField(lpObject, NativeOptionsFc.info_log, (jlong)(intptr_t)lpStruct->info_log.get());' <br/>

Replace 'lpStruct->merge_operator = (rocksdb::MergeOperator*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.merge_operator);' with 'lpStruct->merge_operator = std::shared_ptr<rocksdb::MergeOperator>((rocksdb::MergeOperator*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.merge_operator));' <br/>

Replace 'lpStruct->block_cache = (rocksdb::Cache*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.block_cache);' with 'lpStruct->block_cache = std::shared_ptr<rocksdb::Cache>((rocksdb::Cache*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.block_cache));' <br/>
 
Replace 'lpStruct->info_log = (rocksdb::Logger*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.info_log);' with 'lpStruct->info_log = std::shared_ptr<rocksdb::Logger>((rocksdb::Logger*)(intptr_t)env->GetLongField(lpObject, NativeOptionsFc.info_log));'
