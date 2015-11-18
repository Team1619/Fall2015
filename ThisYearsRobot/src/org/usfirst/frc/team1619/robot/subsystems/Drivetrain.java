package org.usfirst.frc.team1619.robot.subsystems;

import org.usfirst.frc.team1619.robot.RobotMap;
import org.usfirst.frc.team1619.robot.commands.ManualDriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem
{

	private RobotDrive drive;
	private CANTalon leftMotor1;
	private CANTalon leftMotor2;
	private CANTalon rightMotor1;
	private CANTalon rightMotor2;

	// Singleton Drivetrain object
	private static Drivetrain theSystem = new Drivetrain();
	public static Drivetrain getInstance()
	{
		return theSystem;
	}

	public Drivetrain()
	{
		leftMotor1 =  new CANTalon(RobotMap.LEFT_MOTOR_1);
		leftMotor2 =  new CANTalon(RobotMap.LEFT_MOTOR_2);
		rightMotor1 = new CANTalon(RobotMap.RIGHT_MOTOR_1);
		rightMotor2 = new CANTalon(RobotMap.RIGHT_MOTOR_2);
		
		drive = new RobotDrive(leftMotor1, leftMotor2, rightMotor1, rightMotor2);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
		leftMotor1.enableLimitSwitch(false, false);
		leftMotor2.enableLimitSwitch(false, false);
		rightMotor1.enableLimitSwitch(false, false);
		rightMotor2.enableLimitSwitch(false, false);
		leftMotor1.enableBrakeMode(true);
		leftMotor2.enableBrakeMode(true);
		rightMotor1.enableBrakeMode(true);
		rightMotor2.enableBrakeMode(true);
	}                                                   

	public void initDefaultCommand()
	{
		setDefaultCommand(new ManualDriveCommand());
	}
	
	/**
	 * Arcade drive with only joystick or other input device used.
	 * Y axis is linear value, twist axis is rotate value.
	 * 
	 * @param inputDevice
	 */
	public void drive(GenericHID inputDevice) {
		drive.arcadeDrive(inputDevice.getY(), inputDevice.getTwist());
	}
	
	/**
	 * Arcade drive with linearVal being like the linear input on a joystick,
	 * and rotateVal being the twist value. Both -1.0 to 1.0.
	 * 
	 * @param linearVal
	 * @param rotateVal
	 */
	public void drive(double linearVal, double rotateVal) {
		drive.arcadeDrive(-linearVal, rotateVal);
	}

	public void stop() {
		drive.stopMotor();
	}
}
