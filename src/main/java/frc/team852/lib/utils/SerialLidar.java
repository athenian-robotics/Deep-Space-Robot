package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialLidar extends SerialPort implements PIDSource {

    private int dist, distance, lastDistance;
    private String distString;
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
//            this.distString = this.readString();
//            if(distString != null){
//                return Integer.parseInt(distString);
//            }

            this.bytes = this.read(2);
            if(bytes != null)
                return (this.bytes[0] & 0xff) | (this.bytes[1] & 0xff) << 8;
        }
        catch (RuntimeException ex){
            DriverStation.reportError("Error reading lidar! " + ex.getMessage(), false);
        }

//        if(bytes != null)
//            this.dist = (this.bytes[0] & 0xff) | (this.bytes[1] & 0xff) << 8;

        return 0;
    }

    private void startUpLidar(){
        this.dist = 0;
        this.lastDistance = 0;
        this.running = new AtomicBoolean(true);
        this.executor.submit(() -> {
            Timer.delay(5);
            reset();
            while(running.get()) {
                this.fetchLidar();
                Timer.delay(0.05);
            }
        });
    }

    public void shutDownLidar(){
        this.running.set(false);
        this.executor.shutdown();
    }

    private void fetchLidar(){
        this.lastDistance = distance;
        this.distance = getDist();
        this.rate = (dist-lastDistance)/(Timer.getFPGATimestamp()-lastSampleTime);
        lastSampleTime = Timer.getFPGATimestamp();

        SmartDashboard.putNumber("Lidar Distance:", this.distance);
        SmartDashboard.putNumber("Lidar Rate:", this.rate);
        if(distance == 0){
            System.out.println("Error retrieving lidar distance!");
        }
    }

    public int getLidarDistance(){
        return distance;
    }

    public double getLidarRate(){
        return rate;
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
