from prometheus_client import start_http_server, Summary, Counter, Gauge
import random
import time

REQUEST_TIME = Summary('request_processing_seconds', 'Time spent processing request')
c = Counter('counter', 'thisCounts')
g = Gauge('gauge', 'thisGauges')


def process_request(t):
    """A dummy function that takes some time."""
    time.sleep(t)
    c.inc()
    g.set(random.randint(1,100))
    print(c._value.get())


if __name__ == '__main__':
    # Start up the server to expose the metrics.
    start_http_server(8001)
    # Generate some requests.
    while True:
        process_request(random.random())
