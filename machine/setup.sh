cd /opt
wget http://www.eu.apache.org/dist/maven/maven-3/3.2.1/binaries/apache-maven-3.2.1-bin.tar.gz
tar xvf apache-maven-3.2.1-bin.tar.gz
echo "export PATH=$PATH:/opt/apache-maven-3.2.1/bin/" >> /etc/profile.d/java.sh
export PATH=$PATH:/opt/apache-maven-3.2.1/bin/


yum install -y vim

# ========== Kafka ==============
cd /opt
wget "http://apache.ip-connect.vn.ua/kafka/0.8.1/kafka_2.9.2-0.8.1.tgz" -O kafka.tgz
tar zxf kafka.tgz
rm kafka.tgz
cd kafka*
# Start:
# bin/kafka-server-start.sh config/server.properties

# Create topic:
# bin/kafka-topics.sh --zookeeper localhost:2181 --partition 1 --create --topic test --replication-factor 1

# Simple producer/consumer
# bin/kafka-console-producer.sh --broker-list sandbox.hortonworks.com:9092 --topic test
# bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning

# ========== My code ============
cd /components/producers/asterisk-imitator
mvn clean package