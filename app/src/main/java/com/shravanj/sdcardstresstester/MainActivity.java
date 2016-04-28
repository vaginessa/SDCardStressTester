package com.shravanj.sdcardstresstester;

import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
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
        stopTestHandler();
        spinner.setVisibility(View.GONE);
        if(checkForSD())
        {
           sdStatus.setText("SD Card Detected. Press run test to start.");
            startTest.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    sdStatus.setText("Test started");
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

            }
        });
    }

    public void stopTestHandler()
    {
        cancelTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                spinner.setVisibility(View.GONE);
                resetTimer();
                sdStatus.setText("Test canceled");
            }
        });
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



}
