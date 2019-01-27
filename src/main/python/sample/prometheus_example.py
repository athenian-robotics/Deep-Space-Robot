# see github for questions: https://github.com/prometheus/client_python/blob/master/README.md

# counter
# can increase only but not decease

from prometheus_client import Counter
c = Counter('my_failures', 'Description of counter')
c.inc()     # Increment by 1
c.inc(1.6)  # Increment by given value

#count exceptions

@c.count_exceptions()
def f():
  pass

with c.count_exceptions():
  pass

# Count only one type of exception
with c.count_exceptions(ValueError):
  pass



# Gauges
# can incease and decrease

from prometheus_client import Gauge
g = Gauge('my_inprogress_requests', 'Description of gauge')
g.inc()      # Increment by 1
g.dec(10)    # Decrement by given value
g.set(4.2)   # Set to a given value

# utilities for common use cases:

g.set_to_current_time()   # Set to current unixtime

# Increment when entered, decrement when exited.
@g.track_inprogress()
def f():
  pass

with g.track_inprogress():
  pass

# a gauge can also take its value from a callback

d = Gauge('data_objects', 'Number of objects')
my_dict = {}
d.set_function(lambda: len(my_dict))

