package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.WristSubsystem;

public class WristMove extends Command {
  private WristSubsystem wrist;
  private final int moveDist = 5; // TODO set on reception of robot and tuning of PID
  private double setpoint, oldSetpoint;

  public WristMove() {
    requires(Robot.wristSubsystem);
    this.wrist = Robot.wristSubsystem;
    this.setpoint = wrist.getSetpoint();
  }

  @Override
  protected void initialize() {
    wrist.setSetpoint(this.setpoint);
    wrist.enable();
  }

  @Override
  protected void execute() {
    if (wrist.canMove()) {
      if (OI.POVUp.get())
        setpoint = wrist.canMoveUp() ? setpoint + moveDist : setpoint;
      else if (OI.POVDown.get())
        setpoint = wrist.canMoveDown() ? setpoint - moveDist : setpoint;
      if (oldSetpoint != setpoint) {
        wrist.setSetpoint(setpoint);
        oldSetpoint = setpoint;
      }
    }
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void end() {
    wrist.getPIDController().reset();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}