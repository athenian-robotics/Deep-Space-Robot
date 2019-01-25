package frc.team852;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team852.commands.LimitTester;
import frc.team852.commands.NEOTester;
import frc.team852.subsystems.NEOTesterSub;

public class OI {

    // CREATING BUTTONS
    // One type of button is a joystick button which is any button on a
    // joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ElevatorToPosition());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ElevatorToPosition());

    // Start the command when the button is released and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ElevatorToPosition());

    //public static Joystick leftStick = new Joystick(0);
    //public static Joystick rightStick = new Joystick(1);

    //public static Joystick xbox = new Joystick(2);

    //public static Button xboxA = new JoystickButton(xbox, 1);

    public static Joystick stick1 = new Joystick(0);
    public static Joystick stick2 = new Joystick(1);
    public OI(){
        // Only created once, init and execute are called when button is released
		new JoystickButton(stick1, 6).whenReleased(new LimitTester(7));
    }


}
