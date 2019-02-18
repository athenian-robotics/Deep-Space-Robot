package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.WristSubsystem;

public class WristMove extends Command {
  private final WristSubsystem wrist;
  private double setpoint;
  private boolean holdingBall;
  private final int lowerUnsafe = 50, upperUnsafe = 100; // IDK what these values are

  public WristMove() {
    requires(Robot.wristSubsystem);
    this.wrist = Robot.wristSubsystem;
    this.setpoint = wrist.getPosition();
    this.holdingBall = false;
  }

  @Override
  protected void initialize() {
    wrist.getPIDController().reset();
    wrist.setSetpoint(this.setpoint);
    wrist.enable();
  }

  @Override
  protected void execute() {
    if (!wrist.getPIDController().isEnabled())
      wrist.enable();

    if (!holdingBall) {
      int elevatorHeight = Robot.elevatorSubsystem.getHeight();
      if (elevatorHeight >= lowerUnsafe && elevatorHeight <= upperUnsafe)
        setpoint = 30; // Not sure what the encoder ticks are
      else if (elevatorHeight < lowerUnsafe)
        setpoint = 0; // Not sure how this translates to encoder ticks
      else
        setpoint = 90;
    }
    wrist.setSetpoint(setpoint);

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
