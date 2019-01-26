package frc.team852.command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI
import frc.team852.Robot;
import frc.team852.RobotMap

import static frc.team852.OI.stick1;

public class DriveTank extends Command
{

    public DriveTank()
    {
        super();
        requires(Robot.driveSubsystem);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end()
    {

    }
    @Override
    protected void interrupted()
    {

    }

    @Override
    protected void execute()
    {
        double moveForward = stick1.getY();

        if(moveForward > 0)
        {

        }

    }
}
