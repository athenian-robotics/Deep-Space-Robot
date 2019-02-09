package frc.team852.command;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.Drivetrain;

public class ToggleGearbox extends Command {

  private Drivetrain dt;
  private Value gear;

  public ToggleGearbox() {
    requires(Robot.drivetrain);
    this.gear = Robot.drivetrain.getGearing();
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
    System.out.println("Running Toggle Gear");
    if (gear == RobotMap.SLOW) {
      dt.setGearbox(RobotMap.FAST);
      System.out.println("IN LOW GEAR");
    }
    else {
      dt.setGearbox(RobotMap.SLOW);
      System.out.println("IN HIGH GEAR");
    }
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

