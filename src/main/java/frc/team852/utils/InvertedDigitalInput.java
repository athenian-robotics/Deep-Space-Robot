package frc.team852.utils;

import edu.wpi.first.wpilibj.DigitalInput;

public class InvertedDigitalInput extends DigitalInput {

    public InvertedDigitalInput(int channel) {
        super(channel);
    }

    @Override
    public boolean get() {
        return !(super.get());
    }
}