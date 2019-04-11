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
   * button.whenPressed(new ElevatorMoveBangBang());
   * <p>
   * Run the command while the button is being held down and interrupt it once
   * the button is released.
   * button.whileHeld(new ElevatorMoveBangBang());
   * <p>
   * Start the command when the button is released and let it run the command
   * until it is finished as determined by it's isFinished method.
   * button.whenReleased(new ElevatorMoveBangBang());
   */
  public static XboxController xbox = new XboxController(0);
  public static Joystick fightStick = new Joystick(1);

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
  public static FightStickDigitalButton fightStickShare = new FightStickDigitalButton(fightStick, 7, FightStickInput.input.share);
  public static FightStickDigitalButton fightStickOption = new FightStickDigitalButton(fightStick, 8, FightStickInput.input.option);
  public static FightStickDigitalButton fightStickL3 = new FightStickDigitalButton(fightStick, 9, FightStickInput.input.L3);
  public static FightStickDigitalButton fightStickR3 = new FightStickDigitalButton(fightStick, 10, FightStickInput.input.R3);
  public static FightStickAxisButton fightStickLT = new FightStickAxisButton(fightStick, 2, FightStickInput.input.lightKick);
  public static FightStickAxisButton fightStickRT = new FightStickAxisButton(fightStick, 3, FightStickInput.input.R2);

  public static FightStickPOVDirection POVUp = new FightStickPOVDirection(FightStickInput.input.POVtop);
  public static FightStickPOVDirection POVDown = new FightStickPOVDirection(FightStickInput.input.POVbot);
  public static FightStickPOVDirection POVcenter = new FightStickPOVDirection(FightStickInput.input.POVcenter);

  public OI() {
    // Only created once, init and execute are called when button is pressed/released

    FieldPaths.genPaths();
    xboxLB.whenPressed(new ToggleGearbox(RobotMap.SLOW));
    xboxRB.whenPressed(new ToggleGearbox(RobotMap.FAST));

    xboxY.whenPressed(new Ball());

    xboxStart.whenPressed(new TogglePogo());

    fightStickLB.whileHeld(new ElevatorMoveBangBang());
    //fightStickRB.whileHeld(new ElevatorMove());

    fightStickX.whenPressed(new ElevatorMove(ElevatorHeight.HATCH_LOW));
    fightStickY.whenPressed(new ElevatorMove(ElevatorHeight.HATCH_MID));
    fightStickRB.whenPressed(new ElevatorMove(ElevatorHeight.HATCH_HIGH));

    fightStickA.whenPressed(new ElevatorMove(ElevatorHeight.CARGO_LOW));
    fightStickB.whenPressed(new ElevatorMove(ElevatorHeight.CARGO_MID));
    fightStickRT.whenActive(new ElevatorMove(ElevatorHeight.CARGO_HIGH));

    //Climb Hab 2
//    fightStickOption.whenPressed(new TimedClimb());
    fightStickShare.whileHeld(new ClimberMove());

  }


}
