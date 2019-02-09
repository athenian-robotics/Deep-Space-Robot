package frc.team852.lib.utils;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class GyroEncoderPID implements PIDSource {

    private PIDSource source;
    private AHRS gyro;
    private boolean isLeftSide;
    private double gyroStartPoint = 0, error;

    public GyroEncoderPID(PIDSource source, AHRS gyro, boolean isLeftSide){
        this.source = source;
        this.gyro = gyro;
        this.isLeftSide = isLeftSide;
    }
    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return null;
    }

    @Override
    public double pidGet() {
        if(isLeftSide){
            if(gyro.getAngle() > gyroStartPoint){

            }
        }
        return 0;
    }

    public void setGyroStartpoint(double start){
        gyroStartPoint = start;
    }
}
