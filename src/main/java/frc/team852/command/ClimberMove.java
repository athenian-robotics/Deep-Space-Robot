package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.ClimberSubsystem;

public class ClimberMove extends Command {
  private final ClimberSubsystem climber;

  /**
   * Control the climber with the POV stick on the fightstick
   */
  //In case no speed is given, default to this
  public ClimberMove() {
    requires(Robot.climberSubsystem);
    climber = Robot.climberSubsystem;
  }

  @Override
  protected void initialize() {
    climber.resetEncoder();
  }

  //Called when interrupted
  @Override
  protected void interrupted() {
    end();
  }

  //Reset encoders, reset motors, put everything back to where it was.
  @Override
  protected void end() {
    climber.resetEncoder();
    climber.stopMotors();
  }

  //Never stop, change this later once we are no longer using buttons to start and stop
  @Override
  protected boolean isFinished() {
    return false;
  }

  //Pass a speed through the motors, stop when done
  protected void execute() {
    double speed = 0.0;
    if (OI.fightStickPOVUp.get())
      speed = 0.3;
    else if (OI.fightStickPOVDown.get())
      speed = -0.3;
    climber.setSpeed(speed);
  }
}
