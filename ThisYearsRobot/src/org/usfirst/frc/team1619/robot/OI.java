package org.usfirst.frc.team1619.robot;

import edu.wpi.first.wpilibj.Joystick;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	public final Joystick rightStick;
	
	// Singleton OI object
	private static OI sOI = new OI();
	public static OI getInstance() 
	{
		return sOI;
	}
	
	/**
	 * Assign all operator input (buttons and joysticks)
	 */
    private OI() 
    {
    	rightStick = new Joystick(RobotMap.RIGHT_STICK_ID);
    }
    
    /**
     * Initialize commands for any operator input
     */
    public void init()
    {
    	// To be used to assign commands to any buttons, etc
    }
}

