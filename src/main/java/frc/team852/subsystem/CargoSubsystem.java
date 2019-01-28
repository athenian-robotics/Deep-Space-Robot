package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;

public class CargoSubsystem extends Subsystem {

    public CargoSubsystem() {
        super();
    }

    @Override
    protected void initDefaultCommand() {
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
    

}
