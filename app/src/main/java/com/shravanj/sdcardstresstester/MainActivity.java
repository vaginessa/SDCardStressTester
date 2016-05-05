package com.shravanj.sdcardstresstester;

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
    long sdAvailableSpace = getAvailableSDSpace();
    long fileNumber = calculateNumFiles();
    private boolean done = false;


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
                public void onClick(View v) {

                    sdStatus.setText("Test started");
                    testInitiated = true;
                    spinner.setVisibility(View.VISIBLE);
                    timer.start();
                    boolean b = true;
                    while(b)
                    {
                        stopTestHandler();
                        boolean run = createFiles();
                        System.out.println("Files created");
                        if(run)
                        {
                            b = false;
                        }
                    }
                    if(done)
                    {
                        timer.stop();
                        sdStatus.setText("Writing files complete.");
                    }
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
                    done = true;
                    deleteFiles();
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

    public boolean createFiles()
    {
        try
        {
            String str = "";
            File testDir = new File("/sdcard/stresstest");
            testDir.mkdirs();
            for(long x = 1; x <= fileNumber; x++)
            {
                str = "TestFile_" + (long) (x) + ".dat";
                File test = new File(testDir, str);
                //h.postDelayed(r, 3000);
                RandomAccessFile testFile = new RandomAccessFile(test, "rw");
                testFile.setLength(1024 * 1024);
                System.out.println(str);
                testFile.close();
                //h.postDelayed(r, 3000);
                if(x == fileNumber)
                {
                    done = true;
                }
            }
        }
        catch(IOException io)
        {
            //errorMessage();
            System.out.println(io);
        }
        return done;
    }


    public void deleteFiles()
    {
        File dir = new File("sdcard/stresstest/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
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

    public long getAvailableSDSpace()
    {
        StatFs sdstat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableSpace = (long) sdstat.getBlockSize() * (long) sdstat.getAvailableBlocks() / (long) Math.pow(1024, 2);
        return availableSpace;
    }
    
    public long calculateNumFiles()
    {
       long spaceToUse = (long) (getAvailableSDSpace() * .010);
        return spaceToUse;
    }

    public void errorMessage()
    {
        AlertDialog help = new AlertDialog.Builder(MainActivity.this).create();
        help.setTitle("Error");
        help.setMessage("An error occurred while generating the files");
        help.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        help.show();
    }

    /*Runnable r = new Runnable()
    {
        @Override
        public void run() {
            createFiles();
        }
    };

    Handler h = new Handler();*/

    /*static class FileCreationThread implements Runnable
    {
        boolean tmp = createFiles();
        @Override
        public void run()
        {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        }
    }*/

    /*class FileGeneration extends AsyncTask<Boolean, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Boolean... b)
        {
            return false;
        }

        @Override
        protected void onPostExecute()
        {

        }

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected void onProgressUpdate()
        {

        }

    }*/

}
