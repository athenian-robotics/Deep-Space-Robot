package frc.team852.sample;

//see github if you have questions: https://github.com/prometheus/client_java

//dont forget import

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.MetricsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import java.io.IOException;

public class PrometheusExample {

    static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests.").register();


    //Gauge: Used to track any value, can inc(), dec(), and set()
    static final Gauge inprogressRequests = Gauge.build()
            .name("inprogress_requests").help("Inprogress requests.").register();

    public static void main(String[] args) throws Exception {
        Server server = new Server(8000);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");

        server.start();
        server.join();

        while (true) {
            requests.inc(.5);
            inprogressRequests.inc(2);
            System.out.println(requests.get() + ", " + inprogressRequests.get());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    //Counter: Used to count things, can only increment ( inc() ) (usually by 1)


}
