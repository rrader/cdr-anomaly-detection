
@outputSchema("pattern:tuple(src:chararray,counts:bag{time:tuple(id:int,count:long)})")
def generate_pattern(group, times):
	print(group, times)
	timesd = dict((x[1], x[2]) for x in times)
	print(timesd)
	ids = [(i, (timesd[i] if i in timesd else 0)) for i in range(1, 7*24+1)]
	return (group, ids)
