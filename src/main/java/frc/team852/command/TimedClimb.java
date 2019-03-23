package frc.team852.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team852.OI;
import frc.team852.Robot;
import frc.team852.subsystem.ClimberSubsystem;
import frc.team852.subsystem.Drivetrain;

public class TimedClimb extends Command {

    private double startTime;
    private ClimberSubsystem climber;
    private Drivetrain dt;
    private boolean isFinished, timeStarted;

    public TimedClimb(){
        requires(Robot.drivetrain);
        requires(Robot.climberSubsystem);
        this.climber = Robot.climberSubsystem;
        this.dt = Robot.drivetrain;
    }

    @Override
    public void initialize(){
        timeStarted = false;
        isFinished = (!OI.fightStickL3.get() || !OI.fightStickR3.get());
        if (isFinished) cancel();
    }

    @Override
    protected boolean isFinished() {
        return isFinished;
    }

    @Override
    protected void end(){
        dt.stop();
        climber.stopMotors();
        climber.retractPogo();
    }

    @Override
    protected void interrupted(){
        end();
    }

    @Override
    protected void execute(){
        double currentTime = Timer.getFPGATimestamp();
        if(!OI.fightStickLT.get()) {
            if (Robot.gyro.getRoll() < 7.5 && !timeStarted) {
                climber.setSpeed(0.75);
                dt.drive(0.2, 0.2);
                System.out.println("CAM's going to 7.5 degree roll val");
            } else {
                if (!timeStarted) {
                    startTime = Timer.getFPGATimestamp();
                    timeStarted = true;
                }

                if (currentTime <= startTime + 0.8) {
                    climber.setSpeed(0.6);
                    dt.drive(0.2, 0.2);
                    System.out.println("TimedClimb at 2nd stage!");
                } else if (currentTime <= startTime + 1.3) {
                    climber.setSpeed(0.4);
                    dt.drive(0.2, 0.2);
                    System.out.println("TimedClimb at 3rd stage!");
                } else if (currentTime <= startTime + 1.8) {
                    dt.stop();
                    climber.stopMotors();
                    climber.extendPogo();
                    System.out.println("TimedClimb at 4th stage!");
                } else if (currentTime <= startTime + 2.3) {
                    dt.drive(0.3, 0.3);
                    System.out.println("TimedClimb at 5th stage!");
                } else if (currentTime <= startTime + 2.6) {
                    dt.drive(0.4, 0.4);
                    climber.retractPogo();
                    System.out.println("TimedClimb at 6th stage!");
                } else if (currentTime <= startTime + 3.0) {
                    dt.stop();
                    System.out.println("TimedClimb at 7th stage!");
                    isFinished = true;
                } else {
                    isFinished = true;
                }
            }
        }
        else{
            end();
            isFinished = true;
        }
    }
}
