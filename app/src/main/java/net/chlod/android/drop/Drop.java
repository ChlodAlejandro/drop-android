package net.chlod.android.drop;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Drop extends Application {

    public static Context APPLICATION_CONTEXT;

    public static final String TAG = "Drop";

    public static String NOTIFICATION_ID_UPDATES = "drop.n_updates";
    public static String NOTIFICATION_ID_DL_ONGOING = "drop.n_dl_ongoing";
    public static String NOTIFICATION_ID_DL_COMPLETED = "drop.n_dl_completed";
    public static String NOTIFICATION_ID_DL_FAILED = "drop.n_dl_failed";
    public static String NOTIFICATION_ID_OTHER = "drop.n_other";

    public static File DATA_DIRECTORY;
    public static File DATA_WORKSPACE_DIRECTORY;
    public static File DOWNLOAD_DIRECTORY;
    public static File DOWNLOAD_OUTPUT_DIRECTORY;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onCreate() {
        super.onCreate();

        APPLICATION_CONTEXT = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationsUpdates = new NotificationChannel(
                NOTIFICATION_ID_UPDATES, getString(R.string.notificationChannelName_updates), NotificationManager.IMPORTANCE_DEFAULT
            );
            final NotificationChannel notificationsOngoing = new NotificationChannel(
                NOTIFICATION_ID_DL_ONGOING, getString(R.string.notificationChannelName_ongoing), NotificationManager.IMPORTANCE_DEFAULT
            );
            final NotificationChannel notificationsCompleted = new NotificationChannel(
                NOTIFICATION_ID_DL_COMPLETED, getString(R.string.notificationChannelName_completed), NotificationManager.IMPORTANCE_HIGH
            );
            final NotificationChannel notificationsFailed = new NotificationChannel(
                NOTIFICATION_ID_DL_FAILED, getString(R.string.notificationChannelName_failed), NotificationManager.IMPORTANCE_HIGH
            );
            final NotificationChannel notificationsOthers = new NotificationChannel(
                NOTIFICATION_ID_OTHER, getString(R.string.notificationChannelName_other), NotificationManager.IMPORTANCE_LOW
            );

            notificationsUpdates.setDescription(getString(R.string.notificationChannelDescription_updates));
            notificationsOngoing.setDescription(getString(R.string.notificationChannelDescription_ongoing));
            notificationsCompleted.setDescription(getString(R.string.notificationChannelDescription_completed));
            notificationsFailed.setDescription(getString(R.string.notificationChannelDescription_interrupted));
            notificationsOthers.setDescription(getString(R.string.notificationChannelDescription_other));

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;

            notificationManager.createNotificationChannels(new ArrayList<NotificationChannel>() {{
                add(notificationsUpdates);
                add(notificationsOngoing);
                add(notificationsCompleted);
                add(notificationsFailed);
                add(notificationsOthers);
            }});
        }

        DATA_DIRECTORY = getFilesDir();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            DOWNLOAD_DIRECTORY = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        else
            DOWNLOAD_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        DATA_WORKSPACE_DIRECTORY = new File(DATA_DIRECTORY, "work");
        workspaceCleanup(); // TODO remove and create cleanup for individual VDTs
        if (!DATA_DIRECTORY.exists() || !DATA_WORKSPACE_DIRECTORY.exists())
            DATA_WORKSPACE_DIRECTORY.mkdirs();

        DOWNLOAD_OUTPUT_DIRECTORY = new File(DOWNLOAD_DIRECTORY, "drop");
        if (!DOWNLOAD_DIRECTORY.exists() || !DOWNLOAD_OUTPUT_DIRECTORY.exists())
            DOWNLOAD_OUTPUT_DIRECTORY.mkdirs();
    }

    public static void workspaceCleanup() {
        try {
            FileUtils.deleteDirectory(DATA_WORKSPACE_DIRECTORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
