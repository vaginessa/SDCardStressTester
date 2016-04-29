package com.shravanj.sdcardstresstester;

import android.app.AlertDialog;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v7.app.*;
import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.io.*;
import java.util.*;




public class MainActivity extends AppCompatActivity
{

    private TextView sdStatus;
    private Button startTest;
    private Button cancelTest;
    private Button info;
    private ProgressBar spinner;
    private Chronometer timer;
    private boolean testInitiated;
    private final int NUM_FILES_TO_GENERATE = 1000;
    private int fileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTest = (Button)findViewById(R.id.startTest);
        cancelTest = (Button)findViewById(R.id.cancelTest);
        info = (Button)findViewById(R.id.infoButton);
        timer = (Chronometer)findViewById(R.id.timer);
        sdStatus = (TextView)findViewById(R.id.sdStatus);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        displayHelpMessage();
        spinner.setVisibility(View.GONE);
        if(checkForSD())
        {
            final long sdAvailableSpace = getTotalSDCardSize();
           sdStatus.setText("SD Card Detected. Press run test to start.");
            startTest.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    sdStatus.setText("Test started. SD avail: " + sdAvailableSpace);
                    testInitiated = true;
                    stopTestHandler();
                    spinner.setVisibility(View.VISIBLE);
                    timer.start();

                }
            });
        }
        else
        {
            sdStatus.setText("No SD Card found");
        }



    }

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

    public void createHelpMessage()
    {
        AlertDialog help = new AlertDialog.Builder(MainActivity.this).create();
        help.setTitle("Info");
        help.setMessage("Running the test will simulate wear by generating files that take up 10% of the available SD card space, time it, delete the files, time the deletion, and finally list the results. This can simulate heavy wear if used multiple times.");
        help.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        help.show();
    }

    public void stopTestHandler()
    {
        if(testInitiated)
        {
            cancelTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinner.setVisibility(View.GONE);
                    resetTimer();
                    sdStatus.setText("Test canceled");
                }
            });
        }
    }

    public void resetTimer()
    {
        long stopped = timer.getBase() - SystemClock.elapsedRealtime();
        timer.setBase(SystemClock.elapsedRealtime());
        timer.stop();
        stopped = 0;
    }

    public void createFiles()
    {

    }

    public void deleteFiles()
    {

    }

    public boolean checkForSD()
    {
        if(android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public long getTotalSDCardSize()
    {
        StatFs sdstat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableSpace = (long) sdstat.getAvailableBlocks() * (long) sdstat.getBlockCount() / (long) Math.pow(1024, 2);
        return availableSpace;
    }



}
