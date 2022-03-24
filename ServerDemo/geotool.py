from math import *

def geoDistance(lat1, lon1, lat2, lon2): # return geometry distance of 2 points on earth
    R = 6.3781e6
    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c
