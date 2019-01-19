package frc.team852.subsystem;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.ElevatorToPosition;

public class ElevatorSubsystem extends Subsystem {

    private SpeedControllerGroup elevatorMotors = new SpeedControllerGroup(RobotMap.elevatorMotorL, RobotMap.elevatorMotorR);
    private double currEncoderValL, currEncoderValR;

    //TODO fix inversion motor & check encoder tick up/down

    public ElevatorSubsystem(){
        super();
        RobotMap.elevatorMotorL.setInverted(true);
        currEncoderValL = RobotMap.elevatorMotorL.getEncoder().getPosition();
        currEncoderValR = RobotMap.elevatorMotorR.getEncoder().getPosition();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ElevatorToPosition(100, 0.75));
    }

    public void setSpeed(double speed){
        this.elevatorMotors.set(speed);
    }

    public void stopMotors(){
        this.elevatorMotors.stopMotor();
    }

    public double getSpeed(){
        return this.elevatorMotors.get();
    }

    public double getEncoderPos(){
        return ((RobotMap.elevatorMotorL.getEncoder().getPosition() - currEncoderValL)+(RobotMap.elevatorMotorR.getEncoder().getPosition() - currEncoderValR))/2;
    }

    public void resetEncoders(){
        currEncoderValL = RobotMap.elevatorMotorL.getEncoder().getPosition();
        currEncoderValR = RobotMap.elevatorMotorR.getEncoder().getPosition();
    }

}
