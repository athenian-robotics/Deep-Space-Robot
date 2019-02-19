package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class SlateWipe extends Command {


  @Override
  protected void execute(){
    Scheduler.getInstance().removeAll();
  }

  @Override
  protected boolean isFinished() {
    return true;
  }
}
