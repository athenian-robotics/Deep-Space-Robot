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
  private double elevatorSetpoint, oldElevatorSetpoint, wristSetpoint, oldWristSetpoint;
  private final int elevatorLowerSafeDist = RobotMap.elevatorLowerSafeDist, elevatorUpperSafeDist = RobotMap.elevatorUpperSafeDist;
  private final double wristBottom = RobotMap.wristBottom, wristTop = RobotMap.wristTop, wristSafe = RobotMap.wristSafe;

  public ElevatorMove() {
    requires(Robot.elevatorSubsystem);
    requires(Robot.wristSubsystem);
    this.elevator = Robot.elevatorSubsystem;
    this.wrist = Robot.wristSubsystem;
    this.elevatorSetpoint = elevator.getPosition();
    this.wristSetpoint = wrist.getPosition();
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
    if (OI.POVUp.get()) {
      elevatorSetpoint = elevator.canMoveUp() ? elevatorSetpoint + moveDist : elevatorSetpoint;
    } else if (OI.POVDown.get()) {
      elevatorSetpoint = elevator.canMoveDown() ? elevatorSetpoint - moveDist : elevatorSetpoint;
    }
    if (oldElevatorSetpoint != elevatorSetpoint) {
      elevator.setSetpoint(elevatorSetpoint);
      oldElevatorSetpoint = elevatorSetpoint;
    }
    if (elevator.getHeight() <= elevatorLowerSafeDist) {
      wrist.setSetpoint(wristBottom); // This should put the wrist at level
    } else if (elevator.getHeight() <= elevatorUpperSafeDist) {
      wrist.setSetpoint(wristSafe); // This should put the back of wrist outside of the frame
    } else {
      wrist.setSetpoint(wristTop); // This should leave the wrist ready to deploy the hatch
    }
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
