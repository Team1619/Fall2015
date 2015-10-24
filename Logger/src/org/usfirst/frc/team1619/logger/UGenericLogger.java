package org.usfirst.frc.team1619.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by DanielHathcock on 10/23/15.
 * Project: Logger
 * Package: org.usfirst.frc.team1619.logger
 */
public abstract class UGenericLogger
{

    private static final int MAX_LOGS = 50;
    private static final int QUEUE_SIZE = 32;
    private static final String LOG_FOLDER_PATH = UProperties.getProperty("LOG_FOLDER_PATH", String.class);
    private static final String STOP = "STOP";
    private static final SimpleDateFormat sDateFormat;
    private static String sLogFolder = getDateString() + "/";
    private static ArrayList<UGenericLogger> sLoggers = new ArrayList<UGenericLogger>();

    /**
     * Specifies the format for the date and assigns it a time zone.
     */
    static
    {
        sDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSSZ");
        sDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
    }

    private Runnable fLogRunnable = new Runnable()
    {

        @Override
        public void run()
        {
            boolean notDone = true;
            while (notDone)
            {
                try
                {
                    String[] msg = fLoggingQueue.take();
                    if (STOP.equals(msg[0]))
                        notDone = false;
                    else
                    {
                        writeMsg(msg);
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Attempt to write msg to file for this logger's fOutput
         *
         * @param msg Message to be written.
         */
        private void writeMsg(String[] msg)
        {
            try
            {
                String output = String.format("[%s], [%s],", getTimeString(), Thread.currentThread());
                for (String s : msg)
                {
                    output = String.format("%s%s", output, s);
                }
                fOutput.append(output).append('\n');
                fOutput.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };

    protected ArrayBlockingQueue<String[]> fLoggingQueue;
    private FileWriter fOutput;
    private String fLogName;
    private Thread fLogThread;


    /**
     * Initializes fLogName and fLoggingQueue.
     * Starts fLogRunnable if not already running
     *
     * @param logName The name of the log file
     */
    public UGenericLogger(String logName)
    {
        fLogName = logName;
        fLoggingQueue = new ArrayBlockingQueue<String[]>(QUEUE_SIZE);

        sLoggers.add(this);
    }

    /**
     * @return String form of the current date formatted as specified in sDateFormat.
     * "yyyy-MM-dd_HH:mm:ss.SSSZ"
     */
    private static String getDateString()
    {
        return sDateFormat.format(new Date());
    }

    private static String getTimeString()
    {
        return getDateString().substring(11);
    }

    /**
     * Recursive delete that deletes either the file argument,
     * or all the files in the directory argument, plus the
     * directory itself.
     *
     * @param folder (can either be a single file, or a directory)
     */
    private static boolean deleteFile(File folder)
    {
        if (folder.isDirectory())
        {
            File[] files = folder.listFiles();
            assert files != null;
            for (File file : files)
            {
                deleteFile(file);
            }
        }

        return folder.delete();
    }

    /**
     * Stops all log threads, then resets the date and creates the new folder that corresponds
     * to the directory that the log files are stored in, then calls
     * the nextLog() method on all loggers.
     */
    public static void changeLogs()
    {
        stopLogs();
        sLogFolder = getDateString();
        for (UGenericLogger l : sLoggers)
            l.nextLog();
        cleanUp();
        startLogs();
    }

    /**
     * Stops all threads which are currently alive, and does not return
     * until all have stopped execution. Should result in clean stop with
     * all remaining log messages written (messages written before the call
     * of this method).
     */
    public static void stopLogs()
    {
        for (UGenericLogger l : sLoggers)
        {
            l.stopThread();
        }
    }

    /**
     * For all log threads not executing, recreates and restarts them.
     */
    public static void startLogs()
    {
        for (UGenericLogger l : sLoggers)
        {
            l.startThread();
        }
    }

    /**
     * Accesses all of the directories stored under the LOG_FOLDER_PATH
     * directory, and sorts them by date, then deletes the oldest ones
     * until only the newest MAX_LOGS remain.
     */
    private static void cleanUp()
    {
        File logFolder = new File(LOG_FOLDER_PATH);
        if (logFolder.isDirectory())
        {
            File[] logPaths = logFolder.listFiles();
            assert logPaths != null;
            Arrays.sort(logPaths);

            for (int i = 0; i < (logPaths.length - MAX_LOGS); i++)
                deleteFile(logPaths[i]);
        }
    }

    /**
     * Stops log thread if it is executing. Waits until full clean
     * exit of thread.
     */
    private void stopThread()
    {
        if (fLogThread.isAlive())
        {
            fLoggingQueue.add(new String[] {STOP});
        }

        //Make sure the thread is stopped
        while (true)
        {
            if (!fLogThread.isAlive()) break;
        }
    }

    /**
     * Starts the thread if it is not executing. Creates new thread from
     * runnable.
     */
    private void startThread()
    {
        if (!fLogThread.isAlive())
        {
            fLogThread = new Thread(fLogRunnable);
            fLogThread.start();
        }
    }

    /**
     * If the directory for the current date doesn't exist,
     * create the directory (named with the date string).
     * Then creates a file and a fileWriter for the name of
     * the log this method is called on.
     */
    protected void nextLog()
    {
        stopThread();

        try
        {
            if (fOutput != null)
            {
                fOutput.close();
                fOutput = null;
            }

            File logDir = new File(LOG_FOLDER_PATH + sLogFolder);
            if (logDir.mkdir() || logDir.exists())
            {
                fOutput = new FileWriter(LOG_FOLDER_PATH + sLogFolder + fLogName);
                initLog();
                startThread();
            }
            else
            {
                System.err.println("Cannot create log folder " + LOG_FOLDER_PATH + sLogFolder);
                fOutput = null;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates initial print in the log file.
     */
    protected void initLog() {}
}
