package frc.team852.sample;

//see github if you have questions: https://github.com/prometheus/client_java

//dont forget import

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class PrometheusExample {

    public static void main(String[] args) {
        requests.inc(.5);
        inprogressRequests.inc(2);
        System.out.println(requests.get() + ", " + inprogressRequests.get());
    }

    //Counter: Used to count things, can only increment ( inc() ) (usually by 1)
    static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests.").register();


    //Gauge: Used to track any value, can inc(), dec(), and set()
    static final Gauge inprogressRequests = Gauge.build()
            .name("inprogress_requests").help("Inprogress requests.").register();


}
