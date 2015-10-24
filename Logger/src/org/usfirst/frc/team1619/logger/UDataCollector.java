package org.usfirst.frc.team1619.logger;

/**
 * Created by DanielHathcock on 10/23/15.
 * Project: Logger
 * Package: org.usfirst.frc.team1619.logger
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
        super("UACRRobotDataLog-" + logName);

        fHeaders = headers;

        nextLog();
    }

    /**
     * Prints the headers at the top of each column
     */
    @Override
    protected void initLog()
    {
        printCSV(fHeaders);
    }

    /**
     * Prints the specified values separated by commas into the columns under each header.
     */
    public void log(String... values)
    {
        printCSV(values);
    }

    /**
     * @param values are printed to the file with a comma between each one
     */
    private void printCSV(String[] values)
    {
        String[] message = new String[values.length];
        int k = 0;
        for (String s : values)
        {
            message[k++] = s + ",";
        }
        fLoggingQueue.add(message);
    }
}