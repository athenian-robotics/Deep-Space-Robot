package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.ElevatorSubsystem;
import frc.team852.subsystem.WristSubsystem;

public class WristSetup extends Command {
  private final WristSubsystem wrist;
  private double setpoint;

  public WristSetup() {
    requires(Robot.wristSubsystem);
    this.wrist = Robot.wristSubsystem;
    this.setpoint = wrist.getPosition();
  }

  @Override
  protected void initialize() {
    wrist.getPIDController().reset();
    wrist.setSetpoint(this.setpoint);
    wrist.setPercentTolerance(.1);
    wrist.enable();
  }

  @Override
  protected void execute() {
    if (!wrist.getPIDController().isEnabled())
      wrist.enable();
    wrist.setSetpoint(319);
    Shuffle.put(this, "encoder pos", wrist.getEncoderPos());

  }

  @Override
  protected void interrupted() {
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
