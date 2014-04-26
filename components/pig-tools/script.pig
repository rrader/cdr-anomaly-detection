REGISTER 'udf.py' USING jython AS myudf;

REGISTER 'piggybank.jar';
DEFINE UnixToISO org.apache.pig.piggybank.evaluation.datetime.convert.UnixToISO();
DEFINE ISOToUnix org.apache.pig.piggybank.evaluation.datetime.convert.ISOToUnix();
DEFINE ISOToHour org.apache.pig.piggybank.evaluation.datetime.truncate.ISOToHour();

line = LOAD 'data.csv' USING PigStorage('\t') AS (id:chararray,data:chararray);
data_s = FOREACH line GENERATE FLATTEN(STRSPLIT(data, ','));

data = FOREACH data_s GENERATE $0 as src, $1 as dst, (int)$2 as start, (int)$3 as answer,(int)$4 as end,
			(int)$5 as duration, (int)$6 as billsec, $7 as disposition;

-- 1.1.1970 is Thursday, so weekday is (daynumber from 0 time) + 3
-- (Monday is 0)
D = FOREACH data GENERATE src, start,
						  (start-(start/(60*60*24))*(60*60*24))/60/60 as hourOfDay,
						  ((start/(60*60*24)) - (start/(60*60*24))/7*7 + 3)%7 as weekDay;

times = GROUP D BY (src,hourOfDay,weekDay);
times2 = FOREACH times GENERATE group,COUNT(D);

--DESCRIBE times2;
by_src = GROUP times2 BY group.src;

DUMP by_src;


/*by_src = GROUP D BY src;

E = FOREACH by_src {
	times = FOREACH D GENERATE (start,hourOfDay,weekDay) as time;
	records = BagToTuple(times);
	GENERATE group as src, records as record;
}

DESCRIBE E;
--DUMP E;

X = FOREACH E GENERATE myudf.group_phones(record);
--DESCRIBE X;
DUMP X;

*/
--GENERATE X;

--DUMP E;

--by_period = GROUP D BY (hourOfDay, weekDay);
--pattern = FOREACH by_period GENERATE group, COUNT(D);

--DUMP pattern;
