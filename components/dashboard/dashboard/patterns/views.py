# -*- coding: utf-8 -*

from django.shortcuts import render
from django.shortcuts import render_to_response
import json
from django.http import HttpResponse
import redis

r = redis.StrictRedis(host='localhost', port=6379, db=0)

def generate_pattern(data):
    pattern = [["Name", "M", "T", "W", "T", "F", "S", "S"]]
    for hour in range(0,24):
        line = [str(hour)]
        for day in range(0,7):
            line.append(float(data[day*24 + hour]))
        pattern.append(line)
    return pattern

def list_to_js(data):
    conv = []
    for x,y in data:
        conv.append("[%s, %s]" % (x, y))
    return "[" + ",".join(conv) + "]"

def list_to_js_i(data):
    conv = []
    for x,y in data:
        conv.append("[%s, %s, %s]" % (x, y[0], y[1]))
    return "[" + ",".join(conv) + "]"

def home(request):
    patterns = {}
    raw_patterns = {}
    for phone in r.keys("+*"):
        raw_data = r.get(phone).split(',')
        data = raw_data[1:7*24+1]
        sigma = raw_data[7*24+1:7*24*2+1]
        pattern = generate_pattern(data)
        patterns[phone] = pattern
        raw_patterns[phone] = {"data": data, "sigma": sigma}
    patterns["Test"] = generate_pattern([0,2,6,12,10,14,25,10,18,38,41,44,37,35,40,36,49,29,3,3,1,0,0,0,0,2,5,8,9,15,13,16,20,34,47,30,35,34,26,37,40,32,1,3,2,0,0,0,0,3,4,7,8,17,16,21,21,36,39,38,41,31,23,36,23,35,1,2,1,0,0,0,0,5,7,7,16,10,7,11,18,32,30,33,46,38,37,33,33,38,2,1,1,0,0,0,0,3,9,4,14,16,8,18,23,30,35,35,46,43,35,45,47,38,5,3,2,0,0,0,0,2,7,8,12,9,16,12,26,32,30,40,35,42,38,33,37,34,3,0,2,0,0,0,0,1,3,8,13,10,14,7,25,36,44,38,34,29,37,40,27,41,1,1,1,0,0,0])

    hist = {}
    PERIOD = 7*24
    for phone in r.keys("frequency*"):
        key = phone.replace("frequency:+", "")
        pattern = raw_patterns["+" + key]
        last_hour = int(r.get("h_" + phone))
        hist[key] = {}
        hist[key]["data"] = list_to_js(list(enumerate(reversed(r.lrange(phone, 0, PERIOD-1)))))

        last_hour -= PERIOD+1  # first data hour
        h = last_hour - (last_hour // 24) * 24
        w = (last_hour//24 + 3)%7
        first = w*24+h
        p_value = lambda i: float(pattern["data"][(first + i) % (24*7)])  # pattern
        s_value = lambda i: 1.96 * max(1.0, float(pattern["sigma"][(first + i) % (24*7)]))  # sigma

        hist[key]["pattern"] = list_to_js(list(enumerate([p_value(i) for i in range(0, PERIOD)])))
        hist[key]["appropriate"] = list_to_js_i(list(enumerate([(max(0, p_value(i) - s_value(i)), p_value(i) + s_value(i)) for i in range(0, PERIOD)])))
    return render_to_response('index.html', {"patterns": patterns, "hist": hist})

def XX(request):
    return HttpResponse(json.dumps([[1, 522], [20, 312], [30, 762], [15, 24]]), content_type="application/json")
