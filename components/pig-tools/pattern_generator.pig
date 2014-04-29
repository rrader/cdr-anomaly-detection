-- Program that calculates per-number patterns of usage

REGISTER 'python_udf.py' USING jython AS myfuncs;

-- 1. load and parse data
line = LOAD 'data.csv' USING PigStorage('\t') AS (id:chararray,data:chararray);
--line = LOAD '/tmp/cdr/part-1398500976237_0010-m-00000' USING PigStorage('\t') AS (id:chararray,data:chararray);
data_s = FOREACH line GENERATE FLATTEN(STRSPLIT(data, ','));

data = FOREACH data_s GENERATE $0 AS src, $1 AS dst, (int)$2 AS start, (int)$3 AS answer,(int)$4 AS end,
			(int)$5 AS duration, (int)$6 AS billsec, $7 AS disposition;

-- 2. get last record time
data_r = ORDER data BY start DESC;
last = FOREACH (LIMIT data_r 1) GENERATE start;

-- 3. last month records
data_operational = FILTER data BY start > (last.$0 - 4*7*24*60*60);

-- 4. calculate hours and day-of-weeks from timestamps

-- 1.1.1970 is Thursday, so weekday is (daynumber from 0 time) + 3
-- (Monday is 0)
D = FOREACH data_operational GENERATE src, start,
						  (start-(start/(60*60*24))*(60*60*24))/(60*60) AS hourOfDay,
						  ((start/(60*60*24)) - (start/(60*60*24))/7*7 + 3)%7 AS weekDay,
						  (last.$0 - start)/(60*60*24*7)+1 AS weekNumber;

-- 5. generate per-caller-patterns using EMA
times = GROUP D BY (src,hourOfDay,weekDay);
times2 = FOREACH times {
	GENERATE group.src as src,
			 (group.weekDay*24+group.hourOfDay) AS id,
			 myfuncs.EMA(D, 4, 4, 0.5) as ema;
}

-- 6. last week records
data_1week = FILTER D BY weekNumber == 1;
times_1week = GROUP data_1week BY (src,hourOfDay,weekDay);

times_1week2 = FOREACH times_1week {
	GENERATE group.src as src,
			 (group.weekDay*24+group.hourOfDay) AS id,
			 COUNT(data_1week) as count;
}

-- 7. Calculate dispersion
comb = JOIN times2 BY (src, id) FULL OUTER, times_1week2 BY (src, id);

diffs = FOREACH comb {
		GENERATE times2::src,
			myfuncs.SQR( ((times_1week2::count is null) ? 0 : times_1week2::count) -
						 ((times2::ema is null) ? 0 : times2::ema) ) as diff;
}

diffs_gr = GROUP diffs BY src;
sigma = FOREACH diffs_gr {
	GENERATE group as src, SQRT(SUM(diffs.diff)/(COUNT(diffs))) as sigma;
}

-- 8. Generate pattern vector, based on id:ema pairs
by_src = GROUP times2 BY src, sigma BY src;

patterns = FOREACH by_src {
	times_ordered = ORDER times2 BY id;
	GENERATE myfuncs.generate_pattern(group, times_ordered, BagToTuple(sigma.sigma).$0);
}

-- 9. Patterns have been calculated!
--STORE patterns INTO '/tmp/patterns';
STORE patterns INTO 'patterns';
--DUMP patterns;
