package frc.team852.command;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.subsystem.HatchSubsystem;

public class OutputHatch extends Command {

  private HatchSubsystem hatchSubsystem = Robot.hatchSubsystem;
  private double time;
  private CANSparkMax.IdleMode ogIdleMode;

  public OutputHatch() {
    super();
    time = Timer.getFPGATimestamp();
  }

  @Override
  protected void initialize() {
    requires(Robot.hatchSubsystem);
    requires(Robot.drivetrain);
    ogIdleMode = Robot.drivetrain.getIdleMode();
    Robot.drivetrain.setIdleMode(CANSparkMax.IdleMode.kCoast);
  }

  @Override
  protected boolean isFinished() {
    return time + 0.5 <= Timer.getFPGATimestamp(); //TODO change timing on reception of robot
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
      time = Timer.getFPGATimestamp();
    }
  }
}
