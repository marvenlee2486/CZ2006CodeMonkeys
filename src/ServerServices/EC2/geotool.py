from math import *

def geoDistance(lat1, lon1, lat2, lon2): # return geometry distance of 2 points on earth, accepts str / int as parameter.
    if isinstance(lat1, str): lat1 = float(lat1)
    if isinstance(lon1, str): lon1 = float(lon1)
    if isinstance(lat2, str): lat2 = float(lat2)
    if isinstance(lon2, str): lon2 = float(lon2) 
    
    R = 6.3781e6
    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c
