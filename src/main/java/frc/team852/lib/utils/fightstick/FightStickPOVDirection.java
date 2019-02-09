package frc.team852.lib.utils.fightstick;


import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.team852.OI;

public class FightStickPOVDirection extends Trigger {
  private FightStickInput.input direction;

  public FightStickPOVDirection(FightStickInput.input input) {
    this.direction = input;
  }

  @Override
  public boolean get() {
    return FightStickInput.getJoystickEnumValue(OI.fightStick.getPOV()) == this.direction;
  }
}
