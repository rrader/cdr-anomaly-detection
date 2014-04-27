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

times_by_weekday = FOREACH times GENERATE group.src, group.hourOfDay, group.weekDay, (group.weekDay+1)*(group.hourOfDay+1) as id, COUNT(D) as count;

times2 = FOREACH times GENERATE group.src, group.hourOfDay, group.weekDay, (group.weekDay+1)*(group.hourOfDay+1) as id, COUNT(D) as count;

times3 = ORDER times2 BY id;
DUMP times3;
