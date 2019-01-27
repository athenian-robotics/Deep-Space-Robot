package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.WristTo0;
import frc.team852.command.WristTo90;
import frc.team852.lib.utils.SparkMax;

public class WristSubsystem extends Subsystem{

    private SparkMax wristMotor = RobotMap.wristMotor;

    public WristSubsystem(){
        super();
    }

    public void setSpeed(double speed){
        this.wristMotor.set(speed);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new WristTo0());
    }

    public void stopMotors(){
        this.wristMotor.stopMotor();
    }

}
