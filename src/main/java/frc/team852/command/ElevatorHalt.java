package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team852.Robot;
import frc.team852.subsystem.ElevatorSubsystem;

public class ElevatorHalt extends Command {

  public ElevatorHalt(){
    requires(Robot.elevatorSubsystem);
  }


  @Override
  protected void execute(){
  }

  @Override
  protected boolean isFinished() {
    return true;
  }
}
