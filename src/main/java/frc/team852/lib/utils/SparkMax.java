package frc.team852.lib.utils;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

public class SparkMax extends CANSparkMax {

    private double resetOffset;
    private CANEncoder enc;

    public SparkMax(int channel, MotorType motorType) {
        super(channel, motorType);
        resetOffset = 0.0;
        enc = getEncoder();
    }

    /**
     * @return Readings from the encoder
     */
    public double getEncoderPosition() {
        double val = enc.getPosition();
        return val - resetOffset;


    }

    /**
     * Compensate for hardware drift
     */
    public void resetEncoder() {
        resetOffset += getEncoderPosition();
    }
}