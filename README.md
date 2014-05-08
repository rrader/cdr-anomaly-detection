cdr-anomaly-detection
=====================

Anomaly detection in CDR logs study

Components
=====================

Producer: Asterisk-imitator
---------------------

Kafka producer. Emits CSV lines of CDR.

*TBD*

Massive test
=====================

1. Run ImitatorTest.testMassive 
2. 

	cd components/pig-tools/
	cp ../producers/asterisk-imitator/data.csv ./

3. redis-cli FLUSHDB
4. 

	cd components/pig-tools
	rm -rf patterns
	pig -x local pattern_generator.pig 

5. 

	cd components/KMeansHadoop
	./localJob.sh

6. Run StormTopologyTest.testMassive

