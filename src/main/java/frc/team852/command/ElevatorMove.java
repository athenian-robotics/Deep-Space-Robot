package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorMove extends Command {
  private final ElevatorSubsystem elevator;
  private static double elevatorSetpoint;
  private static int positionIndex;

  private static final double posHatchLow = 10;
  private static final double posHatchMid = 80;
  private static final double posHatchHigh = 150;
  private static final double[] posArray = {posHatchLow, posHatchMid, posHatchHigh};

  private static final Shuffle sSetpointBound = new Shuffle(ElevatorMove.class, "SetpointBound", 10);

  public ElevatorMove(int pos) {
    requires(Robot.elevatorSubsystem);
    elevator = Robot.elevatorSubsystem;
    positionIndex = pos;
    setPosition();
  }

  public ElevatorMove() {
    this(0);
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
    elevator.enable();
    /*if (!elevator.getPIDController().isEnabled())
      elevator.enable();

    if (OI.POVUp.get()) {
      positionIndex++;
    }
    else if (OI.POVDown.get()) {
      positionIndex--;
    }

    setPosition();*/
  }

  @Override
  protected boolean isFinished() {
    return false;  // Will be automatically canceled by whenHeld() in OI
  }

  @Override
  protected void interrupted(){
    end();
  }

  private void setPosition() {
    positionIndex = Math.max(0, Math.min(posArray.length - 1, positionIndex));
    elevatorSetpoint = posArray[positionIndex];
    elevator.setSetpoint(elevatorSetpoint);
  }
}
