package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.utils.PIDControl;
import frc.team852.subsystem.Drivetrain;

public class DriveDistance extends Command {

    private double targetDist, inches;
    private Drivetrain dt = Robot.drivetrain;
    //private DifferentialDrive drive = new DifferentialDrive(RobotMap.leftDrive, RobotMap.rightDrive);
    private PIDControl pid = new PIDControl(0.02,0.00003,0.00000002);
    private PIDController leftPID = new PIDController(0.025,0.000002,0.00000001,
            RobotMap.leftEncoder, RobotMap.leftDrive,0.005);
    private PIDController rightPID = new PIDController(0.025,0.000002,0.00000001,
            RobotMap.leftEncoder,RobotMap.rightDrive, 0.005);


    //1 revolution = 18.84 inches
    //1 revolution = 6.601 high gear, 15 low gear

    public DriveDistance(double inches){
        requires(dt);
        this.inches = inches;
    }

    @Override
    protected void initialize(){

        RobotMap.leftEncoder.reset();
        RobotMap.rightEncoder.reset();

        System.out.println("WARNING: RobotMap.leftEncoder.pidGet() = " + RobotMap.leftEncoder.pidGet());
        System.out.println("WARNING: RobotMap.rightEncoder.pidGet() = " + RobotMap.rightEncoder.pidGet());
        System.out.println("WARNING: targetDist = " + targetDist);
        leftPID.setContinuous(false);
        leftPID.setSetpoint(targetDist);
        leftPID.setAbsoluteTolerance(0.1);
        rightPID.setContinuous(false);
        rightPID.setSetpoint(targetDist);
        rightPID.setAbsoluteTolerance(0.1);

        leftPID.setEnabled(false);
        rightPID.setEnabled(false);

        //pid.setReversed();
    }

    @Override
    protected boolean isFinished() {
        return leftPID.onTarget() && rightPID.onTarget();
    }

    @Override
    protected void end() {
        leftPID.setEnabled(false);
        rightPID.setEnabled(false);
        dt.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void execute() {
//        if(!leftPID.isEnabled())
//            leftPID.setEnabled(true);
//        if(!rightPID.isEnabled())
//            rightPID.setEnabled(true);

        double error = pid.getPID(targetDist, (RobotMap.leftEncoder.pidGet()+RobotMap.rightEncoder.pidGet())/2);
        dt.drive(-error,-error);

        System.out.println("leftPID error = " + leftPID.getError());
        System.out.println("rightPID error = " + rightPID.getError());
        //System.out.println("RobotMap.leftEncoder.pidGet() = " + RobotMap.leftEncoder.pidGet());
        //System.out.println("RobotMap.rightEncoder.pidGet() = " + RobotMap.rightEncoder.pidGet());
        //System.out.println("targetDist = " + targetDist);
    }
}
