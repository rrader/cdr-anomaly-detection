
@outputSchema("pattern:chararray")
def generate_pattern(group, times):
	timesd = dict((x[1], x[2]) for x in times)
	ids = [str(timesd[i] if i in timesd else 0) for i in range(1, 7*24+1)]
	ids = [str(group)] + ids
	csv = ','.join(ids)
	return csv

@outputSchema("value:double")
def EMA(D, weight_field, wmax, alpha):
	"""
	Calculates exponential moving average
	note: weights are reversed!
	"""
	weights = [x for x in range(1, wmax+1)]
	weights_values = {}
	wv = 1.0
	for w in weights:
		weights_values[w] = wv
		wv *= alpha
	denom = sum(weights_values.values())
	numer = 0.0
	for weight in weights:
		numer += sum(1 for x in D if x[weight_field] == weight)*weights_values[weight]
	return numer/denom
