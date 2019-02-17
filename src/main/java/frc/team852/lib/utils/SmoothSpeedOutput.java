package frc.team852.lib.utils;

import edu.wpi.first.wpilibj.PIDOutput;

public class SmoothSpeedOutput implements PIDOutput {
    public static final double timeout = 1;

    private final PIDOutput pidOut;
    private final double maxAcceleration;
    private final double maxDeceleration;
    private final boolean logging;

    private long lastTime;
    private double value;

    public SmoothSpeedOutput(PIDOutput output, double maxAcceleration, double maxDeceleration, boolean logging) {
        pidOut = output;
        this.maxAcceleration = maxAcceleration;
        this.maxDeceleration = Math.max(maxAcceleration, maxDeceleration);
        this.logging = logging;
    }

    public SmoothSpeedOutput(PIDOutput output, double maxAcceleration, boolean logging) {
        this(output, maxAcceleration, maxAcceleration, logging);
    }

    @Override
    public void pidWrite(double target) {
        long currTime = System.currentTimeMillis();
        double deltaTime = (currTime - lastTime) / 1000d;
        lastTime = currTime;

        if (deltaTime > timeout) {
            value = 0;
            return;
        }

        double error = target - value;

        value += Math.copySign(
                Math.max(
                        -Math.abs(maxDeceleration * deltaTime),
                        Math.min(
                                Math.abs(maxAcceleration * deltaTime),
                                Math.copySign(error, target * error)
                        )),
                error);

        if (logging) {
            Shuffle.put(this, "target", target);
            Shuffle.put(this, "value", value);
            Shuffle.put(this, "deltaTime", deltaTime);
        }

        pidOut.pidWrite(value);
    }
}