package org.usfirst.frc.team1619.logger;

/**
 * Created by DanielHathcock on 10/27/15.
 * Project: Logger
 * Package: org.usfirst.frc.team1619.logger
 */
public class MainTester
{

    // Unit (? not quite unit) tests for my logger:
    public static void main(String[] args)
    {
        ULogger logger = new ULogger();
        UDataCollector dc = new UDataCollector("Voltage", "1", "2", "3");
        for (int i = 0; i < 15; i++)
        {
            dc.log(Integer.toString(i), Integer.toString(i + 4), Integer.toString(i - 4));
        }
        logger.error("Major error in ", new Integer(7).toString(), " places!!");
        logger.error("Lolol, jk. Only in ", "6", " places!!!!!");
        logger.warning("This is a warning");
        logger.info("And here is info about the warning");
        logger.debug("And this is how to fix the warning");

        UGenericLogger.changeLogs();

        logger.error("Heyey, did this work?");
        logger.closeLogger();
        dc.closeLogger();
    }
}
