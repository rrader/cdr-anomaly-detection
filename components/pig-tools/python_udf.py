
@outputSchema("pattern:chararray")
def generate_pattern(group, times):
	timesd = dict((x[1], x[2]) for x in times)
	ids = [str(timesd[i] if i in timesd else 0) for i in range(1, 7*24+1)]
	ids = [str(group)] + ids
	csv = ','.join(ids)
	return csv
