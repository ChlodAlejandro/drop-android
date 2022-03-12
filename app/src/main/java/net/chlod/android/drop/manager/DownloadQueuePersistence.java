package net.chlod.android.drop.manager;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;

import net.chlod.android.drop.Drop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadQueuePersistence {
    private static final DownloadQueuePersistence instance = new DownloadQueuePersistence();

    public static DownloadQueuePersistence i() {
        return instance;
    }

    private File queueStorageFile;
    private ExecutorService saveExecutor;

    private DownloadQueuePersistence() {
        queueStorageFile = new File(Drop.DATA_DIRECTORY, "queue.jstore");
        Log.i(Drop.TAG, queueStorageFile.getAbsolutePath());
        saveExecutor = Executors.newSingleThreadExecutor();
    }

    private void saveQueue() throws IOException {
        if (!queueStorageFile.exists())
            if (!queueStorageFile.createNewFile())
                throw new IOException("Failed to create queue storage file.");

        FileOutputStream os = new FileOutputStream(queueStorageFile, false);
        String content = new Gson().toJson(DownloadQueue.i());
        byte[] contentBytes = content.getBytes();
        os.write(contentBytes, 0, contentBytes.length);
        os.close();
    }

    public void requestQueueSave(final Context context) {
        saveExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    DownloadQueuePersistence.i().saveQueue();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (context != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
                        builder.setMessage("An exception has occurred!\n\n" + e.toString())
                                .setNeutralButton("OK", null);
                        builder.create();
                    }
                }
            }
        });
    }

    public void requestQueueSave() {
        requestQueueSave(null);
    }

}
