package frc.team852.lib.utils;

import com.revrobotics.CANSparkMax;

public class SparkMax extends CANSparkMax {

    private double lastResetPos, lastVal;

    public SparkMax(int channel, MotorType motorType) {
        super(channel, motorType);
        lastResetPos = 0.0;
        lastVal = 0.0;
    }

    //This method will ensure good readings of encoders
    public double getEncoderPosition(){
        double val = super.getEncoder().getPosition();

        //round to 4 decimal places
        int temp = (int)(val*10000.0);
        val = ((double)temp)/10000.0;

        //if the values are equal (+/- half a rotation), return 0.0000
        if(lastResetPos >= val-0.5 && lastResetPos <= val+0.5) return 0.0000;
        if(val == 0.0) return lastVal;
        else return val;
    }

    public void resetEncoder(){
        lastResetPos = super.getEncoder().getPosition();
    }
}