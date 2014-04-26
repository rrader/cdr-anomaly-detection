REGISTER 'piggybank.jar';
DEFINE UnixToISO org.apache.pig.piggybank.evaluation.datetime.convert.UnixToISO();
DEFINE ISOToUnix org.apache.pig.piggybank.evaluation.datetime.convert.ISOToUnix();
DEFINE ISOToHour org.apache.pig.piggybank.evaluation.datetime.truncate.ISOToHour();

line = LOAD 'data.csv' USING PigStorage('\t') AS (id:chararray,data:chararray);
data_s = FOREACH line GENERATE FLATTEN(STRSPLIT(data, ','));

data = FOREACH data_s GENERATE $0 as src, $1 as dst, (int)$2 as start, (int)$3 as answer,(int)$4 as end,
			(int)$5 as duration, (int)$6 as billsec, $7 as disposition, UnixToISO((int)$2*1000) as startTime;

D = FOREACH data GENERATE src, ISOToUnix(ISOToHour(startTime));

DUMP D;
