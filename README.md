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
compile rocksDB <br/>

Compile JNI:
---------
---------
cd ${ROCKSDBJNI_HOME} <br/>
mvn clean install -P download -P linux64 <br/>
