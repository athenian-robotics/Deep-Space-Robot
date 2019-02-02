package frc.team852.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.WristSubsystem;

public class WristToPosition extends Command
{

    private final WristSubsystem wrist = Robot.wristSubsystem;
    private double currentDistance, targetDistance, move;
    private PIDController pid = new PIDController(0, 0, 0, RobotMap.wristMotor, RobotMap.wristMotor);

    //TODO implement speed
    public WristToPosition(double target) {
        requires(Robot.wristSubsystem);
        this.targetDistance = target;
        this.currentDistance = wrist.getEncoderPos();
        pid.setContinuous(false);
        pid.setSetpoint(target);
        pid.setPercentTolerance(1);
    }

    @Override
    protected void initialize(){
        pid.reset();
    }

    @Override
    protected boolean isFinished(){ return pid.onTarget(); }

    @Override
    protected void end(){
        pid.setEnabled(false);
    }

    @Override
    protected void interrupted(){
        end();
    }

    //TODO tune pid
    protected void execute(){
        if(!pid.isEnabled()) pid.setEnabled(true);
    }


}