package org.usfirst.frc.team1619.logger;

/**
 * Created by DanielHathcock on 10/6/15.
 * Project: Logger
 * Package: org.usfirst.frc.team1619.logger
 */
public class ULogger extends UGenericLogger
{

    public ULogger()
    {
        super("UACRRobot" + UProperties.getLoggingLevel() + "Log");
        nextLog();
    }

    /**
     * Logs an error message to the log file.
     *
     * @param message The error message
     */
    public void error(String ... message)
    {
        log("ERROR", message);
    }

    /**
     * Logs a warning to the log file. The logging level must be greater than or equal to WARNING
     *
     * @param message The warning message
     */
    public void warning(String ... message)
    {
        if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.WARNING) >= 0)
        {
            log("WARNING", message);
        }
    }

    /**
     * Logs the info message to the log file. The logging level must be greater than or equal to INFO
     *
     * @param message The info message
     */
    public void info(String ... message)
    {
        if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.INFO) >= 0)
        {
            log("INFO", message);
        }
    }

    /**
     * Logs the debug message to the log file. The logging level must be greater than or equal to DEBUG
     *
     * @param message The debug message
     */
    public void debug(String ... message)
    {
        if (UProperties.getLoggingLevel().compareTo(ULoggingLevels.DEBUG) >= 0)
        {
            log("DEBUG", message);
        }
    }

    /**
     * Adds the message to the loggingQueue to be written
     *
     * @param level   The text version of the logging level
     * @param message The log message
     */
    protected void log(String level, String[] message)
    {
        message[0] = String.format("[%s], %s",level, message[0]);
        fLoggingQueue.add(message);
    }
}
