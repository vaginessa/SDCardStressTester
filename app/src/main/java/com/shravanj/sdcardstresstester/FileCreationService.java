package com.shravanj.sdcardstresstester;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FileCreationService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.shravanj.sdcardstresstester.action.FOO";
    private static final String ACTION_BAZ = "com.shravanj.sdcardstresstester.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.shravanj.sdcardstresstester.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.shravanj.sdcardstresstester.extra.PARAM2";

    private boolean done = false;
    private long fileNumber = calculateNumFiles();

    public FileCreationService()
    {
        super("FileCreationService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FileCreationService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FileCreationService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFoo(param1, param2);
//            } else if (ACTION_BAZ.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionBaz(param1, param2);
//            }
//
            createFiles();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
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

    public long getAvailableSDSpace()
    {
        StatFs sdstat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long availableSpace = (long) sdstat.getBlockSize() * (long) sdstat.getAvailableBlocks() / (long) Math.pow(1024, 2);
        return availableSpace;
    }

    public long calculateNumFiles()
    {
        long spaceToUse = (long) (getAvailableSDSpace() * .10);
        return spaceToUse;
    }
}
