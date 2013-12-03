The following are instructions to get RocksDB JNI layer running. 
This has been tested on RHEL 6.1 (2.6.32-131) with GCC 4.7.2. 

Installing custom hawtjni (needed for smart pointer JNI code)
------------------------
* git clone https://github.com/ankgup87/hawtjni.git <br/>
* cd hawtjni <br/>
* mvn install <br/>

Compile snappy
------------------------
* wget http://snappy.googlecode.com/files/snappy-1.0.5.tar.gz <br/>
* tar -zxvf snappy-1.0.5.tar.gz <br/>
* ./configure --disable-shared --with-pic <br/>
* make <br/>
* sudo make install <br/>

Compile rocksDB
----
* git clone https://github.com/facebook/rocksdb.git <br/>
* Add -fPIC flag to CFLAGS and CXXFLAGS in rocksdb/Makefile <br/>
* Compile RocksDB as shown on https://github.com/facebook/rocksdb/blob/master/INSTALL.md <br/>


Set env variables
----
* export ROCKSDB_HOME=/path/to/rocksdb <br/>
* export SNAPPY_HOME=/path/to/snappy <br/>

Compile JNI:
---------
* git clone https://github.com/ankgup87/rocksdbjni.git 
* cd rocksdbjni <br/>
* mvn install -P download -P linux64 <br/>
* Build will break once as Makefile is not setup correctly for c++11 compilation(TODO: fix this.!) In rocksdbjni-linux64/target/native-build/Makefile, append -std=c++11 to CXX=g++ line. <br/>
* mvn install -P download -P linux64 <br/>

Output JAR files will be present at: <br/>
rocksdbjni/target/rocksdbjni-0.1.jar <br/>
rocksdbjni-linux64/target/rocksdbjni-linux64-0.1.jar <br/>
