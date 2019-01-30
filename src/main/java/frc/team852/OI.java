package frc.team852;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team852.command.SubsystemPID;
import frc.team852.command.ToggleGearbox;

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
   * button.whenPressed(new ElevatorToPosition());
   * <p>
   * Run the command while the button is being held down and interrupt it once
   * the button is released.
   * button.whileHeld(new ElevatorToPosition());
   * <p>
   * Start the command when the button is released and let it run the command
   * until it is finished as determined by it's isFinished method.
   * button.whenReleased(new ElevatorToPosition());
   */
  public static Joystick stick1 = new Joystick(0);
  public static Joystick stick2 = new Joystick(1);
  public static XboxController xbox = new XboxController(2);

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

  public OI() {
    // Only created once, init and execute are called when button is pressed/released
    new JoystickButton(stick1, 1).whenPressed(new ToggleGearbox());
    new JoystickButton(stick2, 1).whenPressed(new ToggleGearbox());

    //TODO change target
    xboxA.whenPressed(new SubsystemPID(1000, RobotMap.elevatorPIDPosition, Robot.elevatorSubsystem, 2, RobotMap.elevatorLowerLimit, RobotMap.elevatorUpperLimit));


  }


}
