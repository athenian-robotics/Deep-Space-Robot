package frc.team852.sample;

//see github if you have questions: https://github.com/prometheus/client_java

//dont forget import
import io.prometheus.client.*;

public class PrometheusExample {



    //Counter: increase and reset when process restarts
    //only use increment with counter
    //put the following 2 lines of code at the top of your program to use a counter
    static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests.").register();

    //Guage: increase and decrease
    //can use inc and dec
    //put the following 2 lines of code at the top of your program to use a guage
    static final Gauge inprogressRequests = Gauge.build()
            .name("inprogress_requests").help("Inprogress requests.").register();


    //for counter
    void requests(){
        requests.inc();
    }

    //for guage
    void processRequest() {
        inprogressRequests.inc();
        // Your code here.
        inprogressRequests.dec();
    }



}
