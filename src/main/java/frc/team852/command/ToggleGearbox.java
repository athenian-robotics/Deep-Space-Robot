package frc.team852.command;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.Drivetrain;

public class ToggleGearbox extends Command {

  private Drivetrain dt;
  private Value gear;
  private Value state;

  private static final Shuffle sInHighGear = new Shuffle(ToggleGearbox.class, "InHighGear", false);

  public ToggleGearbox(Value state){
    requires(Robot.drivetrain);
    this.gear = Robot.drivetrain.getGearing();
    this.state = state;
  }

  public ToggleGearbox() {
    this(Value.kOff);
  }

  @Override
  protected void initialize() {
    System.out.println("ToggleGear Initialized");
    dt = Robot.drivetrain;
    this.gear = dt.getGearing();
  }

  /**
   * Execute and toggle the gearbox
   */
  @Override
  protected void execute() {
    if(state == Value.kOff) {
      System.out.println("Toggling Drivetrain Gearing");
      if (gear == RobotMap.SLOW) {
        dt.setGearbox(RobotMap.FAST);
        System.out.println("Gearing: HIGH SPEED");
      } else {
        dt.setGearbox(RobotMap.SLOW);
        System.out.println("Gearing: SLOW SPEED");
      }
    }
    else{
      if(state != gear)
        dt.setGearbox(state);
      if(state == RobotMap.FAST)
        System.out.println("Gearing: HIGH SPEED");
      else
        System.out.println("Gearing: SLOW SPEED");
    }

    sInHighGear.set(state == RobotMap.FAST);
  }

  /**
   * Command gets to run once before the scheduler checks if it's finished
   * @return True in order to only run the command once
   */
  @Override
  protected boolean isFinished() {
    return true;
  }

  @Override
  protected void end() {
    System.out.println("Ending gear toggle");
  }
}

