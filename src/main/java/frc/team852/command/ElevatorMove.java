package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.lib.utils.fightstick.FightStickInput;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorMove extends Command {

  private ElevatorSubsystem elevator;
  private final int moveDist = 5; // TODO set on reception of robot and tuning of PID
  private double setpoint, oldSetpoint;

  public ElevatorMove() {
    requires(Robot.elevatorSubsystem);
    this.elevator = Robot.elevatorSubsystem;
    this.setpoint = elevator.getPosition();
  }


  @Override
  protected void initialize() {
    elevator.setSetpoint(this.setpoint);
    elevator.enable();
  }

  @Override
  protected void execute() {
    if (OI.POVUp.get()) {
      setpoint = elevator.canMoveUp() ? setpoint + moveDist : setpoint;
    } else if (OI.POVDown.get()) {
      setpoint = elevator.canMoveDown() ? setpoint - moveDist : setpoint;
    }
    if (oldSetpoint != setpoint) {
      elevator.setSetpoint(setpoint);
      oldSetpoint = setpoint;
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
