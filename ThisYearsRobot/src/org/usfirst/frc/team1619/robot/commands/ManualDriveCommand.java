package org.usfirst.frc.team1619.robot.commands;

import org.usfirst.frc.team1619.robot.OI;
import org.usfirst.frc.team1619.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Command which handles the manual (joystick) driving of the robot.
 */
public class ManualDriveCommand extends Command {

	private Drivetrain drivetrain;
	private Joystick joystick;
	
    public ManualDriveCommand() {
        // Use requires() here to declare subsystem dependencies
    	drivetrain = Drivetrain.getInstance();
		requires(drivetrain);

		this.joystick = OI.getInstance().rightStick;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drivetrain.drive(joystick);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
