package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorMove extends Command {

  private ElevatorSubsystem elevator;
  private final int moveDist = 5; // TODO set on reception of robot and tuning of PID
  private double elevatorSetpoint, oldElevatorSetpoint, wristSetpoint;

  public ElevatorMove() {
    this(Robot.elevatorSubsystem.getHeight());
  }

  public ElevatorMove(int setpoint) {
    requires(Robot.elevatorSubsystem);
    this.elevator = Robot.elevatorSubsystem;
    this.elevatorSetpoint = setpoint;
  }


  @Override
  protected void initialize() {
    elevator.setSetpoint(this.elevatorSetpoint);
    elevator.enable();
  }

  @Override
  protected void execute() {
//    if (!elevator.getPIDController().isEnabled())
//      elevator.enable();
//    // Check if under user control
////    if (OI.fightStickLB.get()) {
////      if (OI.fightStickPOVUp.get()) {
////        elevatorSetpoint = elevator.canMoveUp() ? elevatorSetpoint + moveDist : elevatorSetpoint;
////      } else if (OI.fightStickPOVDown.get()) {
////        elevatorSetpoint = elevator.canMoveDown() ? elevatorSetpoint - moveDist : elevatorSetpoint;
////      }
////      if (oldElevatorSetpoint != elevatorSetpoint) {
////        elevator.setSetpoint(elevatorSetpoint);
////        oldElevatorSetpoint = elevatorSetpoint;
////      }
//    }
    // Move the wrist to a safe position
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
