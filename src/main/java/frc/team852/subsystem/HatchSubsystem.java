package frc.team852.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.RobotMap;

public class HatchSubsystem extends Subsystem {
  private final DoubleSolenoid pneumatics;
  public HatchSubsystem(){
    setName("HatchSystem");
    pneumatics = RobotMap.hatchPancakePneumatics;
  }

  @Override
  protected void initDefaultCommand() {
  }

  public void extendPneumatics(){
    pneumatics.set(DoubleSolenoid.Value.kForward);
  }

  public void retractPneumatics(){
    pneumatics.set(DoubleSolenoid.Value.kReverse);
  }

  public DoubleSolenoid.Value getPneumaticState(){
    return pneumatics.get();
  }
}
