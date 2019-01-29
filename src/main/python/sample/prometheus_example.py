from prometheus_client import Counter
from prometheus_client import Gauge

# Two types of Prometheus objects: Counters and Gauges

# Counter: Only used to increment, usually by 1
c = Counter('my_failures', 'Description of counter')
c.inc()     # Increment by 1
c.inc(1.6)  # Increment by given value


# Here's a more advanced example of count: number of exceptions

# With annotation
@c.count_exceptions()
def f():
  pass


# Within your code
with c.count_exceptions():
  pass

# Count only one type of exception
with c.count_exceptions(ValueError):
  pass

# Gauges: Used to track any value, anything that's not counting will be here (e.g. temperature, cpu usage, ...)
# Can inc, dec, and set
g = Gauge('my_inprogress_requests', 'Description of gauge')
g.inc()      # Increment by 1
g.dec(10)    # Decrement by given value
g.set(4.2)   # Set to a given value

g.set_to_current_time()   # Set to current unixtime


# Another use case: Increment when entered, decrement when exited.
@g.track_inprogress()
def f():
  pass

with g.track_inprogress():
  pass

# A gauge can also take its value from a callback
d = Gauge('data_objects', 'Number of objects')
my_dict = {}
d.set_function(lambda: len(my_dict))

