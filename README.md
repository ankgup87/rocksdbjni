wget http://snappy.googlecode.com/files/snappy-1.0.5.tar.gz
tar -zxvf snappy-1.0.5.tar.gz
https://github.com/facebook/rocksdb.git
export SNAPPY_HOME=`cd snappy-1.0.5; pwd`
export ROCKSDB_HOME=`cd rocksdb; pwd`
export ROCKSDBJNI_HOME=`cd rocksdbjni; pwd`
cd ${SNAPPY_HOME}
./configure --disable-shared --with-pic
make
cd ${ROCKSDB_HOME}
compile rocksDB
cd ${LEVELDBJNI_HOME}
mvn clean install -P download -P linux64
