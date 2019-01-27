package frc.team852.lib.utilities;

/**
 * output = P × error + I × ∑error + D × error/t
 */

// TODO PID Filter
// TODO FeedForward fGain

public class PIDController {

    private double pGain;
    private double iGain;
    private double dGain;
    private double fGain;

    private double maxI = 0;
    private double maxError = 0;

    // sum of past errors, the integral
    private double errorSum = 0;
    private double lastError = 0;

    private boolean hasBoundSet = false;
    private double upperBound;
    private double lowerBound;

    private boolean firstRun = true;
    private boolean reversed = false;

    private double lastReadingMilli = -1;
    private double readingTimeout = -1;

    private double output;

    /**
     * @param p The coefficient for the Proportional term
     * @param i The coefficient for the Integral term
     * @param d The coefficient for the Derivative term
     */
    public PIDController(double p, double i, double d) {
        this(p, i, d, 0);
    }


    /**
     * @param gainz PID(f) gains ordered PID (F is optional)
     */
    public PIDController(double[] gainz) {
        try {
            if (gainz.length == 3) {
                this.pGain = gainz[0];
                this.iGain = gainz[1];
                this.pGain = gainz[2];
                this.fGain = 0;
            } else if (gainz.length == 4) {
                this.pGain = gainz[0];
                this.iGain = gainz[1];
                this.pGain = gainz[2];
                this.fGain = gainz[3];
            } else
                throw new Exception("ERROR: invalid number of gains");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.pGain = 0;
            this.iGain = 0;
            this.dGain = 0;
            this.fGain = 0;
        }
        checkSigns();
        upperBound = 1;
        lowerBound = -1;
    }

    /**
     * @param p The coefficient for the Proportional term
     * @param i The coefficient for the Integral term
     * @param d The coefficient for the Derivative term
     * @param f The coefficient for the Feed Forward term (usually 0 here)
     */

    public PIDController(double p, double i, double d, double f) {
        this.pGain = p;
        this.iGain = i;
        this.dGain = d;
        this.fGain = f;
        checkSigns();
        upperBound = 1;
        lowerBound = -1;
    }

    /**
     * <p>Default constructor</p>
     * <p>Ain't gonna move</p>
     */
    public PIDController() {
        this(0, 0, 0, 0);
    }

    /**
     * @param gainz Array of PID(f) gains
     * @return
     */
    public PIDController setGainz(double[] gainz) {
        try {
            if (gainz.length == 3) {
                this.pGain = gainz[0];
                this.iGain = gainz[1];
                this.pGain = gainz[2];
                this.fGain = 0;
            } else if (gainz.length == 4) {
                this.pGain = gainz[0];
                this.iGain = gainz[1];
                this.pGain = gainz[2];
                this.fGain = gainz[3];
            } else
                throw new Exception("ERROR: invalid number of gains");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    /**
     * Set a threshold for what is considered a timeout
     */
    public void setReadingTimeout(float milliseconds) {
        this.readingTimeout = milliseconds;
    }

    /**
     * Cap the integral term to avoid windup
     *
     * @param maximum The largest allowable integral term
     */
    public void setMaxI(double maximum) {
        this.maxI = maximum;
        if (this.iGain != 0) this.maxError = this.maxI / this.iGain;
    }

    /**
     * <p>Set a constraint on output, don't want motor controllers to go bonkers</p>
     * <p>No options to set constraints in constructor (Most every thing defaults to -1 {@literal <->} 1) but nice to have</p>
     *
     * @param minimum The minimum value this PID controller can output
     * @param maximum The maximum value this PID controller can output
     * @return This {@code this} for the edge case where this needs to be called
     */
    public PIDController setOutputConstraints(double minimum, double maximum) {
        this.lowerBound = minimum;
        this.upperBound = maximum;
        this.hasBoundSet = true;
        return this;
    }

    /**
     * <p>Resets errorSum and resets the read time</p>
     */
    public void reset() {
        this.errorSum = 0;
        this.lastReadingMilli = System.currentTimeMillis();
    }

    /**
     * <p>To operate correctly, all PID parameters require the same sign</p>
     * <p>This should align with the {@literal reversed} value</p>
     */
    private void checkSigns() {
        if (reversed) {
            if (pGain > 0) pGain *= -1;
            if (iGain > 0) iGain *= -1;
            if (dGain > 0) dGain *= -1;
            if (fGain > 0) fGain *= -1;
        } else {  // all values should be above zero
            if (pGain < 0) pGain *= -1;
            if (iGain < 0) iGain *= -1;
            if (dGain < 0) dGain *= -1;
            if (fGain < 0) fGain *= -1;
        }
    }


    /**
     * @param input
     * @param lowerBound
     * @param upperBound
     * @return bounded input
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
     * @param pv
     * @param target
     * @return PID output to minimize the error between {@code pv} and {@code target}
     */
    public double getPID(double pv, double target) {

        double error = target - pv;

        // Reset if the last time we called this was greater than a certain time threshold
        if (this.readingTimeout > 0 && (System.currentTimeMillis() - this.lastReadingMilli) / 1000d > this.readingTimeout)
            this.reset();

        // delta time
        double dt = (System.currentTimeMillis() - this.lastReadingMilli) / 1000d;

        // rough F output, only depend on target location
        // todo probably doesnt work :(
        double fOut = this.fGain * target;

        // proportion
        double pOut = this.pGain * error;

        // accumulated sum multiplied the gain being passed in
        // also check if integral maxed out
        double iOut = this.iGain * this.errorSum;
        if (maxI != 0) iOut = constrain(iOut, -this.maxI, this.maxI);

        // derivative, rate of change times the gain, it's negative as it needs to decrease the error
        // set negative dGain in tuning
        double dOut = ((error - lastError) / dt) * dGain;

        //reset error
        this.lastError = error;

        // final values
        output = pOut + iOut + dOut + fOut;

        // check for bound
        if (this.hasBoundSet)
            output = constrain(output, this.lowerBound, this.upperBound);

        // updating constraints
        if (maxI != 0)
            // set boundary so it doesn't go bonkers
            this.errorSum = constrain(this.errorSum + error, -this.maxError, this.maxError) * dt;
        else
            // increase sum for integral
            this.errorSum += error * dt;

        this.lastReadingMilli = System.currentTimeMillis();

        return output;
    }


    /**
     * @return P gain
     */
    public double getP() {
        return pGain;
    }

    /**
     * @return I gain
     */
    public double getI() {
        return iGain;
    }

    /**
     * @return D gain
     */
    public double getD() {
        return dGain;
    }

    /**
     * @param p Proportional gain
     */
    public PIDController setP(double p) {
        this.pGain = p;
        checkSigns();
        this.reset();
        return this;
    }

    /**
     * @param i Integral gain
     */
    public PIDController setI(double i) {
        this.iGain = i;
        checkSigns();
        this.reset();
        return this;
    }

    /**
     * @param d derivative gain
     */
    public PIDController setD(double d) {
        this.dGain = d;
        checkSigns();
        this.reset();
        return this;
    }

    /**
     * <p>Feed forward param, predict rough future output before pid responds</p>
     * <p>Target speed and target acceleration multiplied by velocity and acceleration</p>
     * <p>only use in rough output value, never use in position based control modes</p>
     *
     * @param f feed forward gain
     */
    public PIDController setF(double f) {
        fGain = f;
        this.reset();
        return this;
    }

    /**
     * @return A {@code String} to print them GAINZ all pretty
     */
    public String toString() {
        if (this.fGain == 0)
            return String.format("P: %f, I: %f, D: %f", this.pGain, this.iGain, this.dGain);
        else
            return String.format("P: %f, I: %f, D: %f, F: %f", this.pGain, this.iGain, this.dGain, this.fGain);
    }

}
