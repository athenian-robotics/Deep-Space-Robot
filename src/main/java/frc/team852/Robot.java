package frc.team852;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team852.command.TrackPosition;
import frc.team852.lib.CVDataStore;
import frc.team852.lib.grpc.CVDataServer;
import frc.team852.lib.utils.AHRS_PID;
import frc.team852.lib.utils.Shuffle;
import frc.team852.subsystem.Drivetrain;

import java.io.IOException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static OI oi;

  //Subsystems
  public static Drivetrain drivetrain;

  //Sensors
  public static AHRS_PID gyro;

  //Data
  public static CVDataServer dataServer;
  public static CVDataStore dataStore;

  //Other
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  public Robot(){
    super();
  }
  public Robot(double period){
    super(period);
  }

  public static Shuffle robotStarted = new Shuffle(Robot.class, "robotStarted", false);
  public static Shuffle robotReady = new Shuffle(Robot.class, "robotReady", false);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    robotStarted.set(true);

    new RobotMap();
    dataServer = new CVDataServer();
    dataStore = new CVDataStore();

    drivetrain = new Drivetrain();

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    try {
      gyro = new AHRS_PID(SerialPort.Port.kUSB);
    } catch (RuntimeException ex ) {
      DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
    }

    oi = new OI(); // Must be defined last

    try {
      dataServer.start();
      SmartDashboard.putString("GRPC_STATUS", "Vision Driver assist available");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      SmartDashboard.putString("GRPC status", "Vision Driver assist unavailable");
    }

    robotReady.set(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    try {
      Scheduler.getInstance().run();
    } catch (Exception e) {
      System.out.println("Exception in robotPeriodic");
      e.printStackTrace();
    }
    // Do stuff like zeroing sensors here (i.e. arm is on limit switch-> zero the encoder)
  }

  /**
   * This is called whenever the Robot enters disabled mode
   * Use this method to reset any subsystem information on disable
   */
  @Override
  public void disabledInit() {
    robotStarted.set(false);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    robotStarted.set(true);
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit() {
    robotStarted.set(true);
    Scheduler.getInstance().add(new TrackPosition());
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // Make sure to cancel any autonomous stuff
    // Might not be needed as sandstorm etc
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
