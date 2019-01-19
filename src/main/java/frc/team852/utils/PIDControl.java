package frc.team852.utils;

/**
 *
 * (hopefully) easily implementable PID control.
 * This is expected to be called at a more or less consistent rate.
 * Many thanks to http://brettbeauregard.com/blog/2011/04/improving-the-beginner%E2%80%99s-pid-derivative-kick/
 */
public class PIDControl {

    private double pGain;
    private double iGain;
    private double dGain;

    private double errorSum = 0;
    private double lastError = 0;

    private double iSum = 0;
    private double maxError = 0;
    private double maxI = 0;

    private double upperBound;
    private double lowerBound;

    private boolean reversed = false;
    private boolean hasBoundsSet = false;

    private double lastReadingMilli = -1;
    private double readingTimeout = -1;  // todo: make this reset the I value automatically based on a timeout



    public PIDControl(double p, double i, double d) {

        this.pGain = p;
        this.iGain = i;
        this.dGain = d;

    }

    public PIDControl() {

        this.pGain = 0;
        this.iGain = 0;
        this.dGain = 0;
    }

    // Let's provide the ability to set and change variables. They can maybe be
    // Tweaked with the smartdashboard if we want to easily troubleshoot things.

    /**
     * Reset error for integral sum. Needs to be called at the beginning of every loop or place where we want to start
     * processing sensor data with the PID loop, otherwise the integral gets out of hand.
     */
    public void reset() {

        this.errorSum = 0;

    }

    /**
     * Set the values to be reversed, in case the error changes in the way opposite to what we'd want.
     * Assuming here that all values are positive or negative.
     * Note that this does <b>not</b> allow for reversing while it's running.
     */
    public void setReversed() {

        if (reversed) {

            pGain *= -1;
            iGain *= -1;
            dGain *= -1;

        }

    }

    public void setReadingTimeout(float milliseconds) {

        this.readingTimeout = milliseconds;

    }

    /**
     * Used to prevent help I from winding up too quickly if we hit a limit
     */
    public void setMaxI(double maximum){

        this.maxI = maximum;

        if (this.iGain != 0) {
            this.maxError = this.maxI / this.iGain;
        }



    }

    public void setP(double p) {

        this.pGain = p;
        this.reset();

    }

    public void setI(double i) {

        this.iGain = i;
        this.reset();
    }

    public void setD(double d) {

        this.dGain = d;
        this.reset();
    }


    /**
     * Set a constraint on output, such as when we want to set a value for a motor controller.
     */
    public void setOutputConstraints(double minimum, double maximum) {

        this.lowerBound = minimum;
        this.upperBound = maximum;

    }

    /**
     * Get a PID output based on a passed in error and the existing system state.
     * Should theoretically work w/ all of the existing systems, though testing would
     * be necessary.
     * @param error Error of offset from desired target.
     * @return Adjustment to be made towards the target.
     */

    public double getPID(double error) {

        // TODO Consider implementing set points

        // Reset if the last time we called this was greater than a certain time threshold
        if (this.readingTimeout >= 0 && (System.currentTimeMillis() - this.lastReadingMilli) > this.readingTimeout)
            this.reset();

        double pOut = this.pGain * error;

        // The integral here is pretty simple, just the accumulated sum multiplied by the gain we're passing in.
        // However, we do want to account for if i is already at the max point

        double iOut = this.iGain * this.errorSum;

        if (maxI != 0)
            iOut = constrain(iOut, -this.maxI, this.maxI);

        // The derivative is just the rate of change times the gain. It's negative as it needs to actually decrease the
        // error.
        double dOut = (error - lastError) * -dGain;

        this.lastError = error;

        double output = pOut + iOut + dOut;

        if (this.hasBoundsSet) {
            output = constrain(output, this.lowerBound, this.upperBound);
        }

        if (maxI != 0)
            // Constraining the error here keeps us from going too far over a threshold, since it might
            // Just level out at a certain point.
            this.errorSum = constrain(this.errorSum + error, -this.maxError, this.maxError);

        else
            this.errorSum += error;  // Increase the sum of the error for the integral

        this.lastReadingMilli = System.currentTimeMillis();



        return output;

    }

    public double getPID(double target, double currentReading) {

        return getPID(currentReading - target);
    }


    /**
     * Some values require boundaries, such as motor controllers only having a max input of
     * 1.0.
     */
    private double constrain(double input, double lowerBound, double upperBound) {

        if (input > upperBound)
            return upperBound;
        else if (input < lowerBound)
            return lowerBound;
        else
            return input;

    }

    /**
     * Constrain values based on an existing input. Requires an input to be already instantiated.
     */
    private double constrainOutput(double input) {

        if (this.hasBoundsSet) {

            return this.constrain(input, this.upperBound, this.lowerBound);
        }

        else
            return input;

    }

    public double getpGain() {
        return pGain;
    }

    public double getiGain() {
        return iGain;
    }

    public double getdGain() {
        return dGain;
    }

    public String toString() {

        return String.format("P: %s, I: %s, D: %s", this.pGain, this.iGain, this.dGain);

    }

}