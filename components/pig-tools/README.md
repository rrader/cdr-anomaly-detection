Pattern generator
=======================

Execute:

```
cp samples/data4w.csv ./data.csv
pig -x local pattern_generator.pig
```


Dependencies:
-----------------
1)
```
git clone git@github.com:rjurney/pig-to-json.git
cd pig-to-json
ant
cp dist/lib/pig-to-json.jar ../
cd ..
cp pig-to-json/lib/json-simple-1.1.jar ./
```

2)
```
git clone https://github.com/mattb/pig-redis.git
ant hadoop
cp dist/pig-redis.jar ../
cd ..
```

http://hortonworks.com/blog/pig-tojson-and-redis-to-publish-data-with-flask/

