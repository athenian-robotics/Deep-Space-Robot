package frc.team852.lib.utils.fightstick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.team852.OI;

public class FightStickAxisButton extends Trigger implements FightStickButton {

  private int axis;
  private FightStickInput.input button;
  private Joystick fightStick;

  public FightStickAxisButton(Joystick fightStick, int axis, FightStickInput.input button) {
    this.axis = axis;
    this.button = button;
  }


  @Override
  public boolean get() {
    return fightStick.getRawAxis(this.axis) > 0.5;
  }


  @Override
  public FightStickInput.input getButtonInputType() {
    return this.button;
  }
}
