package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ClimberMove;
import frc.team852.lib.utils.SparkMax;

public class ClimberSubsystem extends Subsystem {

    //Set a climber motor
    private SparkMax climberMotor = RobotMap.climberMotor;

    //Constructor
    public ClimberSubsystem() {
        super();
    }

    //Default command is climberMove
    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ClimberMove());
    }


    /*

        Everything from here on should be self-explanatory

     */


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
