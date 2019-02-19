package frc.team852.command;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.subsystem.HatchSubsystem;

public class OutputHatch extends Command {

  private HatchSubsystem hatchSubsystem = Robot.hatchSubsystem;
  private Timer timer;
  private CANSparkMax.IdleMode ogIdleMode;

  public OutputHatch() {
    super();
    timer = new Timer();
  }

  @Override
  protected void initialize() {
    requires(Robot.hatchSubsystem);
    requires(Robot.drivetrain);
    ogIdleMode = Robot.drivetrain.getIdleMode();
    Robot.drivetrain.setIdleMode(CANSparkMax.IdleMode.kCoast);
    timer.start();
  }

  @Override
  protected boolean isFinished() {
    return timer.hasPeriodPassed(1); //TODO change timing on reception of robot
  }

  @Override
  protected void end() {
    hatchSubsystem.retractPneumatics();
    Robot.drivetrain.setIdleMode(ogIdleMode);
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void execute() {
    if (hatchSubsystem.getPneumaticState() != DoubleSolenoid.Value.kForward) {
      hatchSubsystem.extendPneumatics();
    }
  }
}
