package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialLidar extends SerialPort implements PIDSource {

    private int[] currVal, lastVal, badVal;
    private int numCharsRead;
    private byte[] currByte, lastByte;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private AtomicBoolean running;

    /**
     * Create a new serial lidar and spin off into it's own thread
     * @param baudRate Baud rate of the lidar
     * @param port port of the lidar
     * @param dataBits Number of data bits per transfer
     * @param parity The type of parity checking to use
     * @param stopBits The number of stop bits to use
     */
    public SerialLidar(int baudRate, Port port, int dataBits, Parity parity, StopBits stopBits) {
        super(baudRate, port, dataBits, parity, stopBits);
        this.startUpLidar();
    }

    private int[] getDist(){
        while(true) {
            currByte = read(1);

            if (lastByte[0] == 0x59 && currByte[0] == 0x59) {
                lastByte[0] = 0x00;
                break;
            } else {
                lastByte = currByte;
                numCharsRead++;
            }
            if (numCharsRead > 30) {
                return badVal;
            }
        }
        byte[] b = new byte[7];
        byte checksum = (byte) 0xB2;
        for(int i = 0; i<=6; i++){
            b[i] = read(1)[0];
            if(i<=5) checksum = (byte) (b[i] + checksum);
        }
        if(checksum != b[6]){
            return badVal;
        }
        int[] ans = new int[]{((b[1] << 8) + b[0]), ((b[3] << 8) + b[2])};
        return ans;
    }

    /**
     * Retrieve the distance from the lidar thread
     * @return 2 int array of [distance, signal strength]
     */
    public int[] getLidarDistance(){
        if (currVal == badVal) return lastVal;
        return currVal;
    }

    private void startUpLidar(){
        byte[] setStandardMode = {0x42,0x57,0x02,0x00,0x00,0x00,0x01,0x06,0x00,0x00};
        write(setStandardMode, 8);
        currByte = new byte[]{0x00};
        lastByte = currByte;
        badVal = new int[]{-1, -1};
        currVal = badVal;
        lastVal = currVal;
        running = new AtomicBoolean(true);
        executor.submit(() -> {
            Timer.delay(2);
            while(running.get()) {
                lastVal = currVal;
                currVal = this.getDist();
                Timer.delay(0.05);
            }
        });
    }

    public void shutDownLidar(){
        running.set(false);
        executor.shutdown();
    }


    //Does nothing
    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return getLidarDistance()[0];
    }
}
