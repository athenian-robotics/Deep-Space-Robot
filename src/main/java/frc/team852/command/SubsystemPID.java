package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.lib.utils.InvertedDigitalInput;

public class SubsystemPID extends Command {

    private boolean usingLimits, hold;
    private InvertedDigitalInput limitLower, limitUpper;
    private PIDController pid;
    private Subsystem subsystem;
    private PIDSource source;


    /** Subsystem PID Command
     * This command will intake a pid controller specified in robotmap and will go to a position (hold or to target)
     *
     * @param target target postion - units based off PIDController input
     * @param pid PIDController object defined in robotmap
     * @param subsystem which subsystem this runs on
     * @param percentTolerance margin of error to stop command
     */

    public SubsystemPID(double target, PIDController pid, Subsystem subsystem, double percentTolerance) {
        this(target, pid, subsystem, percentTolerance, null, null);
    }

    /** Subsystem PID Command
     * This command will intake a pid controller specified in robotmap and will go to a position (hold or to target)
     *
     * @param target target postion - units based off PIDController input
     * @param pid PIDController object defined in robotmap
     * @param subsystem which subsystem this runs on
     * @param percentTolerance margin of error to stop command
     * @param limitLower lower subsystem limit switch
     * @param limitUpper upper subsystem limit switch
     */

    public SubsystemPID(double target, PIDController pid, Subsystem subsystem, double percentTolerance, InvertedDigitalInput limitLower, InvertedDigitalInput limitUpper){
        this(target, pid, subsystem, percentTolerance, limitLower, limitUpper, false, null);
    }

    /** Subsystem PID Command
     * This command will intake a pid controller specified in robotmap and will go to a position (hold or to target)
     *
     * @param target target postion - units based off PIDController input
     * @param pid PIDController object defined in robotmap
     * @param subsystem which subsystem this runs on
     * @param percentTolerance margin of error to stop command
     * @param limitLower lower subsystem limit switch
     * @param limitUpper upper subsystem limit switch
     * @param hold boolean if holding subsystem in place
     * @param source PIDSource to reset setpoint upon initialization
     */

    public SubsystemPID(double target, PIDController pid, Subsystem subsystem, double percentTolerance, InvertedDigitalInput limitLower, InvertedDigitalInput limitUpper, boolean hold, PIDSource source){
        requires(subsystem);
        this.subsystem = subsystem;
        this.usingLimits = (limitLower == null) || (limitUpper == null);
        this.limitLower = limitLower;
        this.limitUpper = limitUpper;
        this.hold = hold;
        this.pid = pid;
        this.pid.setContinuous(false);
        this.pid.setSetpoint(target);
        this.pid.setPercentTolerance(percentTolerance);
        this.source = source;
    }

    @Override
    protected void initialize(){
        pid.reset();
        if(hold) pid.setSetpoint(source.pidGet());
    }

    @Override
    protected boolean isFinished() {
        if(usingLimits){
            if(limitLower.get()){
                System.out.println("ERROR: Lower Limit of " + subsystem.getName() + " subsystem triggered while running PID command!");
                return true;
            }
            else if(limitUpper.get()){
                System.out.println("ERROR: Upper Limit of " + subsystem.getName() + " subsystem triggered while running PID command!");
                return true;
            }
        }
        if(!hold) {
            return pid.onTarget();
        }
        return false;
    }

    @Override
    protected void end() {
        System.out.println(this.getClass() + "was ended");
        pid.setEnabled(false);
    }

    @Override
    protected void interrupted() {
        System.out.println(subsystem.getName() + "should never be interrupted, ending()");
        end();
    }

    //TODO Tune PID Control
    @Override
    protected void execute()
    {
        if(!pid.isEnabled()) pid.setEnabled(true);
    }
}
