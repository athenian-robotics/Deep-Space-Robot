package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.CargoIntakeMove;

public class CargoSubsystem extends Subsystem {
	private final WPI_TalonSRX cargoMotor;

    public CargoSubsystem() {
        super();
        this.cargoMotor = RobotMap.cargoMotor;
    }

    @Override
    protected void initDefaultCommand() {new CargoIntakeMove(0);}

    public void stopMotors() {
        cargoMotor.stopMotor();
    }

    public double getSpeed() {
        return cargoMotor.get();
    }

    public void setSpeed(double speed) {
    	if (speed != 0)
    		cargoMotor.set(speed);
    	else
    		cargoMotor.stopMotor();
    }
    

}
