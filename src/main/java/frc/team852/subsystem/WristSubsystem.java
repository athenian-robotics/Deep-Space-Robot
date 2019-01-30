package frc.team852.subsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;
import frc.team852.command.WristTo0;
import frc.team852.command.WristTo90;
import frc.team852.lib.utils.SparkMax;

public class WristSubsystem extends Subsystem {

  //TODO create encoders if necessary

  private SparkMax wristMotor = RobotMap.wristMotor;

  public WristSubsystem(){ super(); }

  @Override
  protected void initDefaultCommand() { setDefaultCommand(new WristTo0()); }

  public void stop(){ this.wristMotor.stopMotor(); }

  public double getSpeed() {
    return this.wristMotor.get();
  }


  public void setSpeed(double speed){
    if (speed > 0 && RobotMap.wristUpperLimit.get()){
      wristMotor.set(0);
      System.out.println("Upper limit switch activated.");
    }
    else if (speed < 0 && RobotMap.wristLowerLimit.get()){
      wristMotor.set(0);
      System.out.println("Lower limit switch activated.");
    }
    else {
      wristMotor.set(speed);
    }
  }

}

