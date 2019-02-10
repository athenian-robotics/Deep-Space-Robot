package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialLidar extends SerialPort implements PIDSource {

    private int dist, lastDist;
    private double rate, lastSampleTime;
    private byte[] bytes;
    private PIDSourceType pidSourceType = PIDSourceType.kDisplacement;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private AtomicBoolean running;

    public SerialLidar(int baudRate, Port port) {
        super(baudRate, port);
        this.startUpLidar();
    }

    private int getDist(){
        try{
            this.bytes = read(2);
        }
        catch (RuntimeException ex){
            DriverStation.reportError("Error reading lidar! " + ex.getMessage(), true);
        }

        if(bytes != null)
            this.dist = (this.bytes[0] & 0xff) | (this.bytes[1] & 0xff) << 8;

        return this.dist;
    }

    private void startUpLidar(){
        this.dist = 0;
        this.lastDist = 0;
        this.running = new AtomicBoolean(true);
        this.executor.submit(() -> {
            Timer.delay(2);
            reset();
            while(running.get()) {
                this.lastDist = this.dist;
                this.dist = this.getDist();
                rate = (dist-lastDist)/(Timer.getFPGATimestamp()-lastSampleTime);
                lastSampleTime = Timer.getFPGATimestamp();
                Timer.delay(0.02);
            }
        });
    }

    public void shutDownLidar(){
        this.running.set(false);
        this.executor.shutdown();
    }

    public int getLidarDistance(){
        if(dist == 0){
            System.out.println("Error retrieving lidar distance!");
            return lastDist;
        }
        return dist;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        pidSourceType = pidSource;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return pidSourceType;
    }

    @Override
    public double pidGet() {
        if(pidSourceType == PIDSourceType.kRate)
            return rate;
        return getLidarDistance();
    }
}
