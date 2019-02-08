package frc.team852.command;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.CargoSubsystem;


public class CargoIntakeMove extends Command {
	private final CargoSubsystem cargoSubsystem;
    final double speed;


    public CargoIntakeMove(double speed) {
        super();
        //Allows for values to be passed through
        this.speed = speed;
        this.cargoSubsystem = Robot.cargoSubsystem;
    }

    public CargoIntakeMove() {
        //Sets the default speed if none is given
        this(1);
    }

    @Override
    protected void initialize() {
        requires(cargoSubsystem);
    }

    @Override
    protected boolean isFinished() {
        //TODO limit switch, not critical (hard stop) but should be added. May pop ball without.
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
        cargoSubsystem.stopMotors();
    }

    @Override
    protected void execute() {
        //Sets the speed to a given value when button is pressed
        cargoSubsystem.setSpeed(speed);
    }
}
