package frc.team852;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team852.command.*;
import frc.team852.lib.utils.fightstick.FightStickAxisButton;
import frc.team852.lib.utils.fightstick.FightStickDigitalButton;
import frc.team852.lib.utils.fightstick.FightStickInput;
import frc.team852.lib.utils.fightstick.FightStickPOVDirection;

public class OI {
  /**
   * CREATING BUTTONS
   * One type of button is a joystick button which is any button on a
   * joystick.
   * You create one by telling it which joystick it's on and which button
   * number it is.
   * Joystick stick = new Joystick(port);
   * Button button = new JoystickButton(stick, buttonNumber);
   * <p>
   * There are a few additional built in buttons you can use. Additionally,
   * by subclassing Button you can create custom triggers and bind those to
   * commands the same as any other Button.
   * <p>
   * // TRIGGERING COMMANDS WITH BUTTONS
   * Once you have a button, it's trivial to bind it to a button in one of
   * three ways:
   * <p>
   * Start the command when the button is pressed and let it run the command
   * until it is finished as determined by it's isFinished method.
   * button.whenPressed(new ElevatorMove());
   * <p>
   * Run the command while the button is being held down and interrupt it once
   * the button is released.
   * button.whileHeld(new ElevatorMove());
   * <p>
   * Start the command when the button is released and let it run the command
   * until it is finished as determined by it's isFinished method.
   * button.whenReleased(new ElevatorMove());
   */
  public static Joystick stick1 = new Joystick(0);
  public static Joystick stick2 = new Joystick(1);
  public static XboxController xbox = new XboxController(2);
  public static Joystick fightStick = new Joystick(3);

  // Joystick buttons start at 1
  public static Button xboxA = new JoystickButton(xbox, 1);
  public static Button xboxB = new JoystickButton(xbox, 2);
  public static Button xboxX = new JoystickButton(xbox, 3);
  public static Button xboxY = new JoystickButton(xbox, 4);
  public static Button xboxLB = new JoystickButton(xbox, 5);
  public static Button xboxRB = new JoystickButton(xbox, 6);
  public static Button xboxBack = new JoystickButton(xbox, 7);
  public static Button xboxStart = new JoystickButton(xbox, 8);
  public static Button xboxLS = new JoystickButton(xbox, 9);
  public static Button xboxRS = new JoystickButton(xbox, 10);

  public static FightStickDigitalButton fightStickA = new FightStickDigitalButton(fightStick, 1, FightStickInput.input.medKick);
  public static FightStickDigitalButton fightStickB = new FightStickDigitalButton(fightStick, 2, FightStickInput.input.heavyKick);
  public static FightStickDigitalButton fightStickX = new FightStickDigitalButton(fightStick, 3, FightStickInput.input.medPunch);
  public static FightStickDigitalButton fightStickY = new FightStickDigitalButton(fightStick, 4, FightStickInput.input.heavyPunch);
  public static FightStickDigitalButton fightStickLB = new FightStickDigitalButton(fightStick, 5, FightStickInput.input.lightPunch);
  public static FightStickDigitalButton fightStickRB = new FightStickDigitalButton(fightStick, 6, FightStickInput.input.R1);
  public static FightStickAxisButton fightStickLT = new FightStickAxisButton(fightStick, 2, FightStickInput.input.lightKick);
  public static FightStickAxisButton fightStickRT = new FightStickAxisButton(fightStick, 3, FightStickInput.input.R2);

  public static FightStickPOVDirection POVUp = new FightStickPOVDirection(FightStickInput.input.POVtop);
  public static FightStickPOVDirection POVDown = new FightStickPOVDirection(FightStickInput.input.POVbot);
  public static FightStickPOVDirection POVcenter = new FightStickPOVDirection(FightStickInput.input.POVcenter);

  public OI() {
    // Only created once, init and execute are called when button is pressed/released
    new JoystickButton(stick1, 1).whenPressed(new ToggleGearbox());
    new JoystickButton(stick1, 6).whenPressed(new ChangeDriveMode());
      new JoystickButton(stick2, 6).whenPressed(new ChangeDriveMode());
    //new JoystickButton(stick2, 1).whenPressed(new ToggleGearbox());

    FieldPaths.genPaths();
    xboxA.whenPressed(new DriveDistanceVelocity(4, 1));
    xboxBack.whenPressed(new ToggleGearbox());
    xboxStart.whenPressed(new ToggleGearbox());
    xboxRB.whenPressed(new ToggleGearbox());

    //xboxA.whenPressed(new DriveDistanceVelocity(2, 1));
    //xboxB.whenPressed(new DriveTimedVelocity(1, 2));
    //xboxX.whenPressed(new DriveTimed());
    //xboxY.whenPressed(new DriveTank());

    if(xbox.getPOV() == 90 || xbox.getPOV() == -90)
      new ChangeDriveMode();
    xboxY.whenPressed(new DriveDistance(157.48));
    xboxB.whenPressed(new DriveAngle(90));
    xboxX.whenPressed(new DriveAngle(-90));
    //TODO change target
    fightStickLB.whileHeld(new ElevatorMove());
    fightStickX.whenReleased(new PlaceHatch(0));
    fightStickY.whenReleased(new PlaceHatch(1));
    fightStickRB.whenReleased(new PlaceHatch(2));

  }


}
