package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.WristSubsystem;

public class WristHold extends Command {
  private final WristSubsystem wrist;
  private double setpoint;
  private boolean wasInterrupted;

  public WristHold() {
    requires(Robot.wristSubsystem);
    this.wrist = Robot.wristSubsystem;
    this.setpoint = wrist.getPosition();
    this.wasInterrupted = false;
  }

  @Override
  protected void initialize() {
    if (wasInterrupted)
      this.setpoint = wrist.getPosition();
    wrist.setSetpoint(this.setpoint);
    wrist.enable();
    wasInterrupted = false;
  }

  @Override
  protected void execute() {
    if (!wrist.getPIDController().isEnabled())
      wrist.enable();
  }

  @Override
  protected void interrupted() {
    this.wasInterrupted = true;
    end();
  }

  @Override
  protected void end() {
    this.wrist.getPIDController().reset();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
