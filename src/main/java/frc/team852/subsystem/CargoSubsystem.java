package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.CargoIntakeMove;

public class CargoSubsystem extends Subsystem {

    public CargoSubsystem() {
        super();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new CargoIntakeMove());
    }

    public void stopMotors() {
        RobotMap.cargoMotor.stopMotor();
    }

    public double getSpeed() {
        return RobotMap.cargoMotor.get();
    }

    public void setSpeed(double speed) {
        RobotMap.cargoMotor.set(speed);
    }

    public double getEncoderPos() {
        return RobotMap.cargoMotor.getEncoderPosition();
    }

    public void resetEncoders() {
        RobotMap.cargoMotor.resetEncoder();
    }

}
