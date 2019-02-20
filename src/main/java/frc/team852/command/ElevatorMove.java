package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorMove extends Command {
  private final ElevatorSubsystem elevator;
  private static double elevatorSetpoint;
  private static int positionIndex;

  private static final Shuffle sSetpointBound = new Shuffle(ElevatorMove.class, "SetpointBound", 10);

  public ElevatorMove(ElevatorHeight height) {
    requires(Robot.elevatorSubsystem);
    elevator = Robot.elevatorSubsystem;
    positionIndex = height.value;
    elevator.setSpeed(height.value);
  }

  public ElevatorMove() {
    this(ElevatorHeight.FEED_STATION);
  }

  @Override
  protected void initialize() {
    elevator.getPIDController().reset();
    //if (!elevator.getPIDController().isEnabled())
    elevator.enable();
    elevatorSetpoint = elevator.getSetpoint();
  }

  @Override
  protected void execute() {
    if (!elevator.getPIDController().isEnabled())
      elevator.enable();
  }

  @Override
  protected boolean isFinished() {
    return false;  // Will be automatically canceled by whenHeld() in OI
  }

  @Override
  protected void interrupted() {
    end();
  }
}
