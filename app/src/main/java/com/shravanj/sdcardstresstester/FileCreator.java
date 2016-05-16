package com.shravanj.sdcardstresstester;

/**
 * The FileCreator class creates 1 MB files with random blocks written. Based on the amount of
 * free space available on the SD card, a varying number of files will be created to take up 10%
 * of the free space on the card.
 * @author Shravan Jambukesan
 * Date: 5/14/16
 * Period: 3rd
 */

import java.io.*;


public class FileCreator
{
    //Data member for keeping track of whether or not the file creation has finished
    public boolean done = false;

    /**
     * The FileCreator constructor calls the createFiles method and passes it the number of files to
     * create
     * @param numFiles the number of files to create
     */
    FileCreator(long numFiles)
    {
        long filesToCreate = numFiles;
        createFiles(filesToCreate);
    }

    /**
     * The createFiles method creates a set number of files with 1 MB of random data written to them into
     * the temporary directory /sdcard/stresstest/
     * @param numberOfFiles the number of files to be generated
     */
    public void createFiles(long numberOfFiles)
    {
        long fileNumber = numberOfFiles;
        try
        {
            String str = "";
            File testDir = new File("/sdcard/stresstest");
            testDir.mkdirs();
            for(long x = 1; x <= fileNumber; x++)
            {
                str = "TestFile_" + (long) (x) + ".dat";
                File test = new File(testDir, str);
                RandomAccessFile testFile = new RandomAccessFile(test, "rw");
                testFile.setLength(1024 * 1024);
                //System.out.println(str);
                testFile.close();
                if(x == fileNumber)
                {
                    done = true;
                }
            }
        }
        catch(IOException io)
        {
            System.out.println(io);
        }
    }

    /**
     * The getStatus method checks whether or not the files have been created
     * @return the status of the test; true if finished, false if not
     */
    public boolean getStatus()
    {
        return done;
    }
}
