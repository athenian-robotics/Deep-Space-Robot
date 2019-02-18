package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorMove extends Command {
  private final ElevatorSubsystem elevator;
  private double elevatorSetpoint, oldSetpoint;
  private int elevatorMinHeight, elevatorMaxHeight;
  private final boolean oneOff;

  public ElevatorMove(double setpoint) {
    requires(Robot.elevatorSubsystem);
    elevator = Robot.elevatorSubsystem;
    elevator.setSetpoint(setpoint);
    oneOff = true;
  }

  public ElevatorMove() {
    requires(Robot.elevatorSubsystem);
    elevator = Robot.elevatorSubsystem;
    elevatorSetpoint = elevator.getSetpoint();
    oneOff = false;
  }

  @Override
  protected void initialize() {
    elevator.getPIDController().reset();
    if (!elevator.getPIDController().isEnabled())
      elevator.enable();
    elevatorSetpoint = elevator.getSetpoint();
  }

  @Override
  protected void execute() {
    if (!elevator.getPIDController().isEnabled())
      elevator.enable();
    double setpointMoveDist = 5;
    if (OI.POVUp.get()) {
      elevatorSetpoint = oldSetpoint + setpointMoveDist;
    } else if (OI.POVDown.get())
      elevatorSetpoint = oldSetpoint - setpointMoveDist;
    if (elevatorSetpoint != oldSetpoint) {
      elevator.setSetpoint(elevatorSetpoint);
      oldSetpoint = elevatorSetpoint;
    }
  }

  @Override
  protected boolean isFinished() {
    return oneOff;
  }
}
