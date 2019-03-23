package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.ClimberSubsystem;

public class ClimberMove extends Command {
    private final ClimberSubsystem climber;

    //In case no speed is given, default to this
    public ClimberMove() {
      requires(Robot.climberSubsystem);

      climber = Robot.climberSubsystem;
    }

    @Override
    protected void initialize(){
    }

    //Called when interrupted
    @Override
    protected void interrupted() {
      end();
    }

    //Reset encoders, reset motors, put everything back to where it was.
    @Override
    protected void end() {
      climber.stopMotors();
    }

    //Never stop, change this later once we are no longer using buttons to start and stop
    @Override
    protected boolean isFinished() {
        return false;
    }

    //Pass a speed through the motors, stop when done
    protected void execute() {

        if (OI.fightStickLT.get()) {
            if (OI.POVUp.get()) {
                climber.setSpeed(0.75);
            } else if (OI.POVDown.get()) {
                climber.setSpeed(-0.75);
            } else {
                climber.setSpeed(0);
            }
        }
    }
}
