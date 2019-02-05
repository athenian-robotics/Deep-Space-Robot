package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialLidar extends SerialPort implements PIDSource {

    private int[] currVal, lastVal, badVal;
    private int numCharsRead;
    private byte[] currByte, lastByte;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private AtomicBoolean running;

    public SerialLidar(int baudRate, Port port, int dataBits, Parity parity, StopBits stopBits) {
        super(baudRate, port, dataBits, parity, stopBits);
        this.startUpLidar();
    }

    private int[] getDist(){
        while(true) {
            currByte = this.read(1);
            System.out.println("byte 0: " + currByte[0]);

            if (lastByte[0] == 0x59 && currByte[0] == 0x59) {
                lastByte[0] = 0x00;
                numCharsRead = 0;
                break;
            } else {
                lastByte = currByte;
                numCharsRead++;
            }
            if (numCharsRead > 30) {
                System.out.println("ERROR: NUM CHARS > 30");
                return badVal;
            }
        }
        byte[] b = new byte[7];
        byte checksum = (byte) 0xB2;
        for(int i = 0; i<=6; i++){
            b[i] = this.read(1)[0];
            System.out.println("byte " + i + ": " + b[i]);
            if(i<=5) checksum = (byte) (b[i] + checksum);
        }
        if(checksum != b[6]){
            System.out.println("ERROR: CHECKSUM!");
            return badVal;
        }
        int[] ans = new int[]{((b[1] << 8) + b[0]), ((b[3] << 8) + b[2])};
        return ans;
    }

    public int[] getLidarDistance(){
        if (currVal == badVal) return lastVal;
        return currVal;
    }

    private void startUpLidar(){
        byte[] setStandardMode = {0x42,0x57,0x02,0x00,0x00,0x00,0x01,0x06,0x00,0x00};
        this.write(setStandardMode, 8);
        //Timer.delay(0.5);
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
                System.out.println("Dist: " + currVal[0] + " | Strength: " + currVal[1]);
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
