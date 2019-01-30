package frc.team852.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;

public class HatchSubsystem extends Subsystem {

  @Override
  protected void initDefaultCommand() {
  }

  public void extendPneumatics(){
    RobotMap.hatchPancakePneumatics.set(DoubleSolenoid.Value.kForward);
  }

  public void retractPneumatics(){
    RobotMap.hatchPancakePneumatics.set(DoubleSolenoid.Value.kReverse);
  }

  public DoubleSolenoid.Value getPneumaticState(){
    return RobotMap.hatchPancakePneumatics.get();
  }
}
