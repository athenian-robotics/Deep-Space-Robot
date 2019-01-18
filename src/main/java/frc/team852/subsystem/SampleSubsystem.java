package frc.team852.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team852.command.SampleCommand;

public class SampleSubsystem extends Subsystem {
    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new SampleCommand());
    }
}
