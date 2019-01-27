package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;

public class ClimberMove extends Command {
    final double speed;

    public ClimberMove(double speed) {
        super();
        requires(Robot.climberSubsystem);
        this.speed = speed;
    }

    public ClimberMove() {
        this(1);
    }

    protected void interrupted() {
        end();
    }

    protected void end() {
        RobotMap.climberMotor.resetEncoder();
        RobotMap.climberSubsystem.stopMotors();
    }


    protected boolean isFinished() {
        return false;
    }

    protected void execute() {
        RobotMap.climberMotor.set(speed);
    }
}
