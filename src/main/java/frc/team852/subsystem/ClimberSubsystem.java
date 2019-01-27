package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ClimberMove;
import frc.team852.lib.utils.SparkMax;

public class ClimberSubsystem extends Subsystem {

    private SparkMax climberMotor = RobotMap.climberMotor;


    public ClimberSubsystem() {
        super();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ClimberMove());
    }

    public void stopMotors() {
        this.climberMotor.stopMotor();
    }

    public double getSpeed() {
        return this.climberMotor.get();
    }

    public void setSpeed(double speed) {
        this.climberMotor.set(speed);
    }

    public double getEncoderPos() {
        return this.climberMotor.getEncoderPosition();
    }

    public void resetEncoders() {
        climberMotor.resetEncoder();
    }

}
