package frc.team852.command;

import edu.wpi.first.wpilibj.command.Command;

public class WristBangBang extends Command {
    /** Angles are in degrees unless otherwise specified*/

    public static final double encoderQuarterTurn = 319;  // How many encoder ticks correspond to a quarter turn (90º)

    public static final double slack = 50;  // How much the arm can move without driving the motor and encoder
    public static final double balancePoint = 0;  // The angle at which the center of mass is directly over the axle

    public static final double posUpright = 0;  // Angle of upright position relative to upright position (0 by definition)
    public static final double posForward = 90;  // Angle of forward position relative to upright position

    @Override
    protected void initialize() {

    }

    @Override
    protected boolean isFinished() {
        // TODO change
        return false;
    }

    @Override
    protected void execute() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {

    }
}
