package org.usfirst.frc.team1619.logger;

/**
 * Created by DanielHathcock on 10/23/15.
 * Project: Logger
 * Package: org.usfirst.frc.team1619.logger
 *
 * This the the subclass of UGenericLogger which is meant to log numerical data in a
 * comma-separated-value format. This can then be easily displayed or analyzed.
 *
 * All columns have headers, and all rows have a single unique time stamp at which all
 * of the data in that row was recorded.
 *
 * This is meant to be used for values from the Power Distribution Panel, like voltage
 * and current draw values for all motors.
 */
public class UDataCollector extends UGenericLogger
{

    private String[] fHeaders;

    /**
     * @param logName is the name of the file that the data will be written to
     * @param headers are printed at the top of each column of data
     */
    public UDataCollector(String logName, String... headers)
    {
        super("UACRRobotDataLog-" + logName, ".csv");

        fHeaders = headers;

        nextLog();
    }

    /**
     * Prints the headers at the top of each column
     */
    @Override
    protected void initLog()
    {
        log(fHeaders);
    }

    /**
     * Prints the specified values separated by commas into the columns under each header.
     */
    public void log(String... values)
    {
        fLoggingQueue.add(values);
    }

}