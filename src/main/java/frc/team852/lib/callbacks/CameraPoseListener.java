package frc.team852.lib.callbacks;

import frc.team852.DeepSpaceRobot.CameraPose;

public abstract class CameraPoseListener extends GenericListener<CameraPose> {
    public CameraPoseListener() {
        super(CameraPoseListener.class);
    }
}
