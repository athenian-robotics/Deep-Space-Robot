package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.ElevatorSubsystem;
import frc.team852.subsystem.WristSubsystem;

public class ElevatorMove extends Command {

  private ElevatorSubsystem elevator;
  private WristSubsystem wrist;
  private final int moveDist = 5; // TODO set on reception of robot and tuning of PID
  private double elevatorSetpoint, oldElevatorSetpoint, wristSetpoint;

  public ElevatorMove() {
    this(Robot.elevatorSubsystem.getHeight());
  }

  public ElevatorMove(int setpoint) {
    requires(Robot.elevatorSubsystem);
    requires(Robot.wristSubsystem);
    this.elevator = Robot.elevatorSubsystem;
    this.wrist = Robot.wristSubsystem;
    this.elevatorSetpoint = setpoint;
    this.wristSetpoint = wrist.getSafeSetpoint(elevator.getHeight());
  }


  @Override
  protected void initialize() {
    elevator.setSetpoint(this.elevatorSetpoint);
    elevator.enable();
    wrist.setSetpoint(this.wristSetpoint);
    wrist.enable();
  }

  @Override
  protected void execute() {
    if (!elevator.getPIDController().isEnabled())
      elevator.enable();
    // Check if under user control
    if (OI.fightStickLB.get()) {
      if (OI.POVUp.get()) {
        elevatorSetpoint = elevator.canMoveUp() ? elevatorSetpoint + moveDist : elevatorSetpoint;
      } else if (OI.POVDown.get()) {
        elevatorSetpoint = elevator.canMoveDown() ? elevatorSetpoint - moveDist : elevatorSetpoint;
      }
      if (oldElevatorSetpoint != elevatorSetpoint) {
        elevator.setSetpoint(elevatorSetpoint);
        oldElevatorSetpoint = elevatorSetpoint;
      }
    }
    // Move the wrist to a safe position
    wrist.safeMove(elevator.getHeight());
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void end() {
    elevator.getPIDController().reset();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
