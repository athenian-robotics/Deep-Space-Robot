package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorMove extends Command {
  private final ElevatorSubsystem elevator;
  private int heightVal, oldSetpoint;

  private static final Shuffle sSetpointBound = new Shuffle(ElevatorMove.class, "SetpointBound", 10);

  public ElevatorMove(ElevatorHeight height) {
    requires(Robot.elevatorSubsystem);
    elevator = Robot.elevatorSubsystem;
    heightVal = height.value;
  }

  public ElevatorMove() {
    this(ElevatorHeight.FEED_STATION);
  }

  @Override
  protected void initialize() {
    elevator.getPIDController().reset();
    elevator.enable();
    oldSetpoint = (int) elevator.getSetpoint();
    elevator.setSetpoint(heightVal);
  }

  @Override
  protected void execute() {
    if (!elevator.getPIDController().isEnabled())
      elevator.enable();
    System.out.println(heightVal);
  }

  @Override
  protected boolean isFinished() {
    return false;  // Will be automatically canceled by whenHeld() in OI
  }

  @Override
  protected void interrupted() {
    elevator.setSetpoint(oldSetpoint);
    end();
  }
}
