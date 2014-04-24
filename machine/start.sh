LOGDIR=/var/log/cdr/
mkdir -p $LOGDIR

# kafka
cd /opt/kafka*
nohup bin/kafka-server-start.sh config/server.properties > $LOGDIR/kafka.log &

