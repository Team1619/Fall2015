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
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSSZ");
    private static String sLogFolder = getDateString() + "/";
    private static ArrayList<UGenericLogger> sLoggers = new ArrayList<UGenericLogger>();

    static
    {
        sDateFormat.setTimeZone(TimeZone.getTimeZone("America/Denver"));
    }

    protected ArrayBlockingQueue<String[]> fLoggingQueue;
    private FileWriter fOutput;
    private String fLogName;
    private String fFileExtension;
    private Thread fLogThread;


    // The runnable implementation for each logger
    private final Runnable fLogRunnable = new Runnable()
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
                String output;
                if (fFileExtension.equals(".csv"))
                {
                    output = String.format("[%s]", getTimeString());
                    for (String s : msg)
                    {
                        output = String.format("%s,%s", output, s);
                    }
                }
                else
                {
                    output = String.format("[%s] [%s]", getTimeString(), Thread.currentThread());
                    for (String s : msg)
                    {
                        output = String.format("%s %s", output, s);
                    }
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


    /**
     * Initializes fLogName and fLoggingQueue.
     * Starts fLogRunnable if not already running
     *
     * @param logName The name of the log file
     * @param fileExtension The file extension (should include ".". i.e. ".txt")
     */
    public UGenericLogger(String logName, String fileExtension)
    {
        fLogName = logName;
        fFileExtension = fileExtension;
        fLoggingQueue = new ArrayBlockingQueue<String[]>(QUEUE_SIZE);

        sLoggers.add(this);
        makeLogFolder();
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
     * Safely stops the logger thread and removes it from the list of
     * Loggers. This logger should not be used after this! Set it to null!
     */
    public void closeLogger()
    {
        stopThread();
        sLoggers.remove(this);
    }

    /**
     * Stops all log threads, then resets the date and creates the new
     * folder that corresponds to the directory that the log files are
     * stored in, then calls the nextLog() method on all loggers.
     */
    public static void changeLogs()
    {
        stopLogs();
        sLogFolder = getDateString() + "/";

        if (makeLogFolder())
        {
            cleanUp();
            startLogs();
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
     * Stops all threads which are currently alive, and does not return
     * until all have stopped execution. Should result in clean stop with
     * all remaining log messages written (messages written before the call
     * of this method).
     */
    private static void stopLogs()
    {
        for (UGenericLogger l : sLoggers)
        {
            l.stopThread();
        }
    }

    /**
     * For all log, it stops them if any are running, then defines
     * a new fileWriter for each and starts them.
     */
    private static void startLogs()
    {
        for (UGenericLogger l : sLoggers)
        {
            l.nextLog();
        }
    }

    /**
     * Makes a new folder with path/name as LOG_FOLDER_PATH + sLogFolder, where
     * sLogFolder is the dateString.
     *
     * @return True if folder is created or already existed, and false if it could
     * not be created and doesn't exist. If false is returned, all log threads are
     * stopped and an error message is printed.
     */
    private static boolean makeLogFolder()
    {
        File logDir = new File(LOG_FOLDER_PATH + sLogFolder);
        if (logDir.mkdir() || logDir.exists())
        {
            return true;
        }
        // If folder not successfully created:

        System.err.println("Cannot create log folder " + LOG_FOLDER_PATH + sLogFolder);

        stopLogs();
        return false;
    }

    private String makeFullPath()
    {
        return String.format("%s%s%s%s", LOG_FOLDER_PATH, sLogFolder, fLogName, fFileExtension);
    }

    /**
     * Stops log thread if it is executing. Waits until full clean
     * exit of thread. Closes the loggers fileWriter, and throws
     * RuntimeException if it does not close.
     */
    private void stopThread()
    {
        if (fLogThread != null && fLogThread.isAlive())
        {
            fLoggingQueue.add(new String[]{STOP});

            // Make sure the thread is stopped
            while (true)
            {
                if (!fLogThread.isAlive()) break;
            }
        }

        // Close fileWriter for thread
        if (fOutput != null)
        {
            try
            {
                fOutput.close();
                fOutput = null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * First, defines the fileWriter for the thread to use, then starts
     * the thread if it is not executing. Creates new thread from
     * runnable.
     */
    private void startThread()
    {
        // Define fileWriter if necessary
        if (fOutput == null)
        {
            try
            {
                fOutput = new FileWriter(makeFullPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        if (fLogThread == null || !fLogThread.isAlive())
        {
            fLogThread = new Thread(fLogRunnable);
            fLogThread.start();
        }
    }


    /**
     * Make sure the logger is not running, then send the initial log
     * (if any) to the loggingQueue and start the thread (which should
     * define a new fileWriter).
     */
    protected void nextLog()
    {
        stopThread();

        initLog();

        startThread();
    }

    /**
     * Creates initial print in the log file.
     */
    protected void initLog()
    {
    }
}
