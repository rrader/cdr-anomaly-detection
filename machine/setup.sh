cd /opt
wget http://www.eu.apache.org/dist/maven/maven-3/3.2.1/binaries/apache-maven-3.2.1-bin.tar.gz
tar xvf apache-maven-3.2.1-bin.tar.gz
echo "export PATH=$PATH:/opt/apache-maven-3.2.1/bin/" >> /etc/profile.d/java.sh
export PATH=$PATH:/opt/apache-maven-3.2.1/bin/


yum install -y vim htop

# ========== Kafka ==============
cd /opt
wget "http://apache.ip-connect.vn.ua/kafka/0.8.1/kafka_2.9.2-0.8.1.tgz" -O kafka.tgz
tar zxf kafka.tgz
rm kafka.tgz
cd kafka*
echo 'export HADOOP_CLASSPATH=${HADOOP_CLASSPATH}:'`pwd`/libs/'*' >> /etc/hadoop/conf.empty/hadoop-env.sh

# Start:
# bin/kafka-server-start.sh config/server.properties

# Create topic:
# bin/kafka-topics.sh --zookeeper localhost:2181 --partition 1 --create --topic calls --replication-factor 1

# Simple producer/consumer
# bin/kafka-console-producer.sh --broker-list sandbox.hortonworks.com:9092 --topic test
# bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic calls --from-beginning

# ======== HDFS Writer ==========
cd /components/k8hadoop/k8hadoop
mvn package
export HADOOP_CLASSPATH="$HADOOP_CLASSPATH:`pwd`/target/"'*'
cp /opt/kafka_2.9.2-0.8.1/libs/* /usr/lib/hadoop/lib/
# hadoop jar target/k8hadoop-0.0.1-SNAPSHOT.jar com.zj.kafka.k8hadoop.HadoopConsumer -z localhost:2181 -t calls /tmp/cdr/

# ====== Starting services ======
cd /vagrant
bash start.sh

# ========== My code ============
# ---------- Producer -----------
cd /components/producers/asterisk-imitator
mvn clean package
#java -cp 'target/*' ua.kpi.rrader.cdr.source.AsteriskImitator p1
cd /opt/kafka
