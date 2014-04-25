LOGDIR=/var/log/cdr/
mkdir -p $LOGDIR

# kafka
cd /opt/kafka*
nohup bin/kafka-server-start.sh config/server.properties > $LOGDIR/kafka.log &
sleep 5
bin/kafka-topics.sh --zookeeper localhost:2181 --partition 1 --create --topic calls --replication-factor 1
