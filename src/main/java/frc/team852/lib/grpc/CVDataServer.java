package frc.team852.lib.grpc;

import frc.team852.lib.callbacks.BallListener;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CVDataServer {
  private Server server;
  private CVDataImpl dataImpl;

  public CVDataServer() {
  }

  public static void main(String[] args)
          throws IOException, InterruptedException {
    final CVDataServer server = new CVDataServer();
    server.start();
    server.blockUntilShutdown();
  }

  public void start()
          throws IOException {
    /* The port on which the server should run */
    int port = 50051;
    dataImpl = new CVDataImpl();
    server = ServerBuilder.forPort(port)
            .addService(dataImpl)
            .build()
            .start();
    System.out.println("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(
            new Thread(() -> {
              // Use stderr here since the logger may have been reset by its JVM shutdown hook.
              System.err.println("*** shutting down grpc server since JVM is shutting down");
              CVDataServer.this.stop();
              System.err.println("*** server shut down");
            }));
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown()
          throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  public void registerBallListener(BallListener listener) {
    this.dataImpl.registerBallListener(listener);
  }

}
