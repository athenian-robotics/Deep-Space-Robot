package frc.team852.lib.utils.fightstick;



import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class FightStickDigitalButton extends JoystickButton implements FightStickButton {

  private FightStickInput.input button;

  public FightStickDigitalButton(Joystick stick, int buttonNumber, FightStickInput.input button) {
    super(stick, buttonNumber);
    this.button = button;
  }

  @Override
  public FightStickInput.input getButtonInputType() {
    return this.button;
  }
}