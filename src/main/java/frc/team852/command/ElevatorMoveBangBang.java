package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.ElevatorSubsystem;
import frc.team852.subsystem.WristSubsystem;

public class ElevatorMoveBangBang extends Command {

  private ElevatorSubsystem elevator;
  private WristSubsystem wrist;
  private final int moveDist = 5; // TODO set on reception of robot and tuning of PID
  private double elevatorSetpoint, oldElevatorSetpoint, wristSetpoint;

  public ElevatorMoveBangBang() {
    this(Robot.elevatorSubsystem.getHeight());
  }

  public ElevatorMoveBangBang(int setpoint) {
    requires(Robot.elevatorSubsystem);
    requires(Robot.wristSubsystem);
    this.elevator = Robot.elevatorSubsystem;
    this.wrist = Robot.wristSubsystem;
  }


  @Override
  protected void initialize() {
    elevator.disable();
  }

  @Override
  protected void execute() {
    if (elevator.getPIDController().isEnabled())
      elevator.disable();
    // Check if under user control
    if (OI.fightStickLB.get()) {
      if (OI.POVUp.get()) {
        elevator.setSpeed(0.3);
      } else if (OI.POVDown.get()) {
        elevator.setSpeed(-0.1);
      }
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
