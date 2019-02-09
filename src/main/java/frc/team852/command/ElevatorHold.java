package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorHold extends Command {

  private final ElevatorSubsystem elevator;
  private double setpoint;
  private boolean wasInterruped;

  /**
   * Set the elevator to hold position
   */
  public ElevatorHold() {
    this(Robot.elevatorSubsystem.getPosition());

  }

  public ElevatorHold(double setpoint) {
    requires(Robot.elevatorSubsystem);
    this.elevator = Robot.elevatorSubsystem;
    this.setpoint = setpoint;
    this.wasInterruped = false;
  }

  @Override
  protected void initialize() {
    if (wasInterruped)
      this.setpoint = elevator.getPosition();
    elevator.setSetpoint(this.setpoint);
    elevator.enable(); // Turn the PID on
    wasInterruped = false;
  }

  @Override
  protected void execute() {
    if (!elevator.getPIDController().isEnabled())
      elevator.enable();
  }

  @Override
  protected void interrupted() {
    this.wasInterruped = true;
    end();
  }

  @Override
  protected void end() {
    this.elevator.getPIDController().reset();
  }


  @Override
  protected boolean isFinished() {
    return false;
  }
}
