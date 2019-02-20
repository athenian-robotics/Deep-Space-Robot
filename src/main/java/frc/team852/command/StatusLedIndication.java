package frc.team852.command;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.LedStrip;


public class StatusLedIndication extends Command {

    private DigitalInput inHabRange = RobotMap.inHabRange;
    private DigitalInput inStationRange = RobotMap.inStationRange;
    private LedStrip statusLeds;
    private static final Shuffle sIsBlueTeam = new Shuffle(StatusLedIndication.class, "isBlueTeam", false);


    public StatusLedIndication() {
        requires(Robot.statusLeds);
        statusLeds = Robot.statusLeds;
    }

    @Override
    protected void execute() {
        if(inHabRange.get()){
            statusLeds.setColor(LedStrip.LedColors.GOLD_STROBE);
        }
        else if(inStationRange.get()){
            statusLeds.setColor(LedStrip.LedColors.WHITE_STROBE);
        }
        else{
            if(sIsBlueTeam.getB())
              statusLeds.setColor(LedStrip.LedColors.BLUE);
            else
              statusLeds.setColor(LedStrip.LedColors.RED);
        }
    }

    @Override
    protected void end() {
        statusLeds.setColor(LedStrip.LedColors.RAINBOW);
    }

    @Override
    protected boolean isFinished() {return false;}

    @Override
    protected void interrupted() {
        end();
    }


}
