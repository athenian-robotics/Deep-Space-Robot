package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;


public class CargoIntakeMove extends Command {

    final double speed;


    public CargoIntakeMove(double speed) {
        super();
        //Allows for values to be passed through
        this.speed = speed;
    }

    public CargoIntakeMove() {
        //Sets the default speed if none is given
        this(1);
    }

    @Override
    protected void initialize() {
        requires(Robot.cargoSubsystem);
    }

    @Override
    protected boolean isFinished() {
        //We can change this later if we need to but this basically never stops
        return false;
    }

    @Override
    protected void interrupted() {
        //when interrupted then end
        end();
    }

    @Override
    protected void end() {
        //Stop motors
        RobotMap.cargoSubsystem.stopMotors();
    }

    @Override
    protected void execute() {
        //Sets the speed to a given value when button is pressed
        RobotMap.cargoSubsystem.setSpeed(speed);
    }
}
