package frc.team852.sample;

//see github if you have questions: https://github.com/prometheus/client_java

//dont forget import
import io.prometheus.client.*;

public class PrometheusExample {

    //Counter: Used to count things, can only increment ( inc() ) (usually by 1)
    static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests.").register();

    void requests(){
        requests.inc();
    }

    //Gauge: Used to track any value, can inc(), dec(), and set()
    static final Gauge inprogressRequests = Gauge.build()
            .name("inprogress_requests").help("Inprogress requests.").register();

    void processRequest() {
        inprogressRequests.inc();
        inprogressRequests.dec();
        inprogressRequests.set(5);
    }



}
