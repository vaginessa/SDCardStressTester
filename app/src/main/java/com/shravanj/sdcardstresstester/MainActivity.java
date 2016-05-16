package com.shravanj.sdcardstresstester;

/**
 * The MainActivity class creates and displays all of the UI elements as well as initiates the stress test
 * @author Shravan Jambukesan
 * Date: 5/14/16
 * Period: 3rd
 */

import android.app.AlertDialog;
import android.os.Process;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v7.app.*;
import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.io.*;


public class MainActivity extends AppCompatActivity
{
    //UI element Data members
    private TextView sdStatus;
    private Button startTest;
    private Button resetTest;
    private Button info;
    public long fileNumber = calculateNumFiles();
    private boolean done = false;
    private boolean x = false;
    //Test timing data members
    private long createStart;
    private long createEnd;
    private long deleteStart;
    private long deleteEnd;


    /**
     * The onCreate method instantiates the UI elements as well as starts the test once the button is pressed
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTest = (Button)findViewById(R.id.startTest);
        resetTest = (Button)findViewById(R.id.resetTest);
        info = (Button)findViewById(R.id.infoButton);
        sdStatus = (TextView)findViewById(R.id.sdStatus);
        displayHelpMessage();
        resetHandler();
        if(checkForSD())
        {
            sdStatus.setText("SD Card Detected. Press run test to start. Before starting for the first time, make sure you view the readme by pressing Help/Info.");
            startTest.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) {

                    sdStatus.setText("Test started");
                    createStart = System.currentTimeMillis();
                    boolean b = true;
                    FileCreator fc = new FileCreator(fileNumber);
                    while(b)
                    {
                        boolean run = fc.getStatus();
                        System.out.println("Files created");
                        if(run)
                        {
                            b = false;
                            x = true;
                        }
                    }
                    if(x)
                    {
                        createEnd = System.currentTimeMillis();
                        deleteFiles();
                    }
                }
            });
        }
        else
        {
            sdStatus.setText("No SD Card found");
        }
    }

    /**
     * The displayHelpMessage helper method creates a new onClicker listener for the Info/Help button
     */
    public void displayHelpMessage()
    {
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                createHelpMessage();
            }
        });
    }

    /**
     * The createHelpMessage method creates and sets the text for the pop up box for the Info/Help viewer
     */
    public void createHelpMessage()
    {
        AlertDialog help = new AlertDialog.Builder(MainActivity.this).create();
        help.setTitle("Info");
        help.setMessage("Running the test will simulate wear by generating files that take up 10% of the available SD card space, time it, delete the files, time the deletion, and finally list the results. This can simulate heavy wear if used multiple times. It is normal for the UI to freeze during the test due to the RAM used to write random blocks to the files.");
        help.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        help.show();
    }

    /**
     * The resetHandler method creates a new onClick listener for the Reset button which deletes any temporary test files (if any are found)
     */
    public void resetHandler()
    {
            resetTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    done = true;
                    deleteFiles();
                    sdStatus.setText("Files deleted (if any). Press Run Stress Test to test again.");
                }
            });
    }

    /**
     * The deleteFiles method sequentially deletes the files and times how long it takes to delete them by
     * updating the delete timing variables using the current system time
     */
    public void deleteFiles()
    {
        deleteStart = System.currentTimeMillis();
        File dir = new File("sdcard/stresstest/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
                //System.out.println("deleted");
            }
            deleteEnd = System.currentTimeMillis();
            sdStatus.setText("Final results: It took " + getFileCreationTime() +  " minute(s) to create " + calculateNumFiles() + " files and " + getFileDeletionTime() + " second(s) to delete them");
        }
        else
        {
            sdStatus.setText("SD card not inserted/mounted, so there are no files to be removed");
        }
    }

    /**
     * The checkForSD method checks to see if there is a SD card inserted and mounted
     * @return true if there is an SD card detected
     * @return false if there is not an SD card detected
     */
    public boolean checkForSD()
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && Environment.isExternalStorageRemovable())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * The getAvailableSDSpace method calculates how much free space is available on the card in MB
     * @return the number of MB available on the SD card
     */
    public long getAvailableSDSpace()
    {
        StatFs sdstat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableSpace = (long) sdstat.getBlockSize() * (long) sdstat.getAvailableBlocks() / (long) Math.pow(1024, 2);
        return availableSpace;
    }

    /**
     * The calculateNumFiles method calculates the number of files to be created by taking the MB of the available
     * SD card space and multiplying it by .10 to get 10% of it
     * @return
     */
    public long calculateNumFiles()
    {
        long spaceToUse = (long) (getAvailableSDSpace() * .10);
        return spaceToUse;
    }

    /**
     * The getFileCreation time gets the time it took to create the files in number of minutes
     * @return the number of minutes it took to get the files created
     */
    public double getFileCreationTime()
    {
        return (createEnd - createStart) / 1000 / 60;
    }

    /**
     * The getFileDeletionTime gets the time it took to delete the files in number of seconds
     * @return the number of seconds it took to get the files deleted
     */
    public double getFileDeletionTime()
    {
        return (deleteEnd - deleteStart) / 1000;
    }

}