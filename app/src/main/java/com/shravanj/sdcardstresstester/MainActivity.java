package com.shravanj.sdcardstresstester;

import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;



public class MainActivity extends AppCompatActivity
{

    private TextView totalSD;
    private long sdCardSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalSD = (TextView)findViewById(R.id.totalSD);
        sdCardSpace = getTotalSDCardSize();
        if(checkForSD())
        {
           totalSD.setText(Long.toString(sdCardSpace));
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

    public long getTotalSDCardSize()
    {
        File f = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(f.getPath());
        long size = stat.getBlockSizeLong();
        long count = stat.getBlockCountLong();

        return (size * count) / (long) (Math.pow(1024, 2));

    }
}
