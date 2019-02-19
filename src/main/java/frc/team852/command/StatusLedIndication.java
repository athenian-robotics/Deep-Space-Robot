package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;


public class StatusLedIndication extends Command {


    public StatusLedIndication() {
        requires(Robot.statusLeds);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected void end() {
    }

    @Override
    protected boolean isFinished() {return false;}

    @Override
    protected void interrupted() {
        end();
    }


}
