#!/usr/bin/env python3
import fileinput
import json

x = json.loads('\n'.join([l for l in fileinput.input()]))
for l in x['entity']:
    print(l.keys())
for l in x['entity'][0]['animation']:
    print(l.keys(), l['name'])
for y in x['entity'][0]['animation'][0]['mainline']['key']:
    print(y)