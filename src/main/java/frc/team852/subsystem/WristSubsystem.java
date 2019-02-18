package frc.team852.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import frc.team852.Robot;
import frc.team852.RobotMap;
import frc.team852.command.WristMove;
import frc.team852.command.WristSetup;
import frc.team852.lib.utils.Shuffle;

import static frc.team852.OI.xbox;

public class WristSubsystem extends PIDSubsystem {
	public static final Shuffle sGravityConstant= new Shuffle(WristSubsystem.class, "GravityConstant", 0);
	private final WPI_TalonSRX motor;
	private final Encoder encoder;
	private DigitalInput lowerLimit, upperLimit;
	private final int elevatorLowerSafeDist = 10, elevatorUpperSafeDist = 30; // IDK what these values really are TODO fix on on reception of robot
	private final double wristBottom = 0, place = 90, wristSafe = 30; // IDK what these values really are TODO fix on on reception of robot
	
	public WristSubsystem() {
		super("Wrist", 0, 0, 0); // TODO Tune
		this.motor = RobotMap.wristMotor;
		this.encoder = RobotMap.wristEncoder;
		this.lowerLimit = RobotMap.wristLowerLimit;
		this.upperLimit = RobotMap.wristUpperLimit;
		this.motor.setNeutralMode(NeutralMode.Brake);
//    this.setInputRange(ENCODER_RANGE_MIN, ENCODER_RANGE_MAX);
	}
	
	public void stopMotors() {
		this.motor.stopMotor();
	}
	
	public double getSpeed() {
		return this.motor.get();
	}
	
	/**
	 * Manually set the speed while paying attention to limit switches in the mechanism
	 *
	 * @param speed
	 */
	public void setSpeed(double speed) {
		if (speed < 0 && upperLimit.get()) {
			motor.set(0);
			System.out.println("[!!] Wrist on upper limit.");
			encoder.reset();
		} else if (speed > 0 && lowerLimit.get()) {
			motor.set(0);
			System.out.println("[!!] Wrist on lower limit.");
		} else {
			motor.set(speed);
		}
	}
	
	/**
	 * Used in conjunction with a command controlling the elevator to keep the back of the plate from breaking
	 *
	 * @param elevatorHeight
	 */
	public void safeMove(int elevatorHeight) {
		if (!getPIDController().isEnabled())
			enable();
		
		if (elevatorHeight <= elevatorLowerSafeDist)
			setSetpoint(wristBottom);
		else if (elevatorHeight <= elevatorUpperSafeDist)
			setSetpoint(wristSafe);
		else
			setSetpoint(place);
	}
	
	public double getSafeSetpoint(int elevatorHeight) {
		if (elevatorHeight <= elevatorLowerSafeDist)
			return wristBottom;
		else if (elevatorHeight <= elevatorUpperSafeDist)
			return wristSafe;
		else
			return place;
	}
	
	public boolean canMoveUp() {
		return upperLimit.get();
	}
	
	public boolean canMoveDown() {
		return lowerLimit.get();
	}
	
	public boolean canMove() {
		int elevatorHeight = Robot.elevatorLidar.getLidarDistance();
		return !(elevatorHeight <= elevatorUpperSafeDist);
	}
	
	
	@Override
	protected void usePIDOutput(double output) {
		Shuffle.put(this, "speed", output);
		setSpeed(output < 0 ? output * sGravityConstant.get() : output);
	}
	
	public double getEncoderPos() {
		return encoder.pidGet();
	}
	
	public void resetEncoders() {
		encoder.reset();
	}
	
	@Override
	protected double returnPIDInput() {
		return encoder.get();
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new WristSetup());
	}
	
	
}

