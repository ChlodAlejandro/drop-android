package net.chlod.android.drop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import net.chlod.android.drop.enums.OutputFormat;
import net.chlod.android.drop.interfaces.DownloadCompletion;
import net.chlod.android.drop.interfaces.DownloadConvertProgressChange;
import net.chlod.android.drop.interfaces.DownloadFailure;
import net.chlod.android.drop.interfaces.DownloadProgressChange;
import net.chlod.android.drop.interfaces.DownloadQueued;
import net.chlod.android.drop.interfaces.DownloadStarted;
import net.chlod.android.drop.manager.DownloadQueue;
import net.chlod.android.drop.objects.DownloadProgress;
import net.chlod.android.drop.objects.DownloadRequest;
import net.chlod.ytdl_android.YoutubeDL;
import net.chlod.ytdl_android.YoutubeDLException;
import net.chlod.ytdl_android.YoutubeDLResponse;
import net.chlod.ytdl_android.objects.VideoInfoCollection;
import net.chlod.ytdl_android_ffmpeg.FFmpeg;
import net.chlod.ytdl_android_ffmpeg.FFmpegException;
import net.chlod.ytdl_android_ffmpeg.objects.FFmpegProgressUpdate;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * THIS CLASS WILL BE REPLACED WITH SplashScreen!!!!
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            YoutubeDL.getInstance().init(getApplication());
            FFmpeg.getInstance().init(getApplication());
            DownloadQueue.init(new WeakReference<>(Drop.APPLICATION_CONTEXT));
        } catch (YoutubeDLException | FFmpegException e) {
            Log.e("Drop", "failed to initialize youtubedl-android", e);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForMonehs(Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        doTheHarlemShake();
    }

    public File[] listFiles(File dir) {
        return dir.listFiles();
    }

    public void askForMonehs(String... permissions) {
        ArrayList<String> toGrant = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                toGrant.add(permission);
            }
        }

        if (toGrant.size() > 0)
            ActivityCompat.requestPermissions(this, toGrant.toArray(new String[0]),7);

        ExecutorService updateExecutor = Executors.newSingleThreadExecutor();
        updateExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("Drop", YoutubeDL.getInstance().updateYoutubeDL(getApplication()).name());
                } catch (YoutubeDLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void doTheHarlemShake() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String testVideoURL = "https://www.youtube.com/watch?v=QKQSNODlMXo";
                String testPlaylistURL = "https://www.youtube.com/playlist?list=PLwjeCZFBHNJp_UvUl6N_XiLiuB95kjXr4";

                VideoInfoCollection videoInfo = null;
                VideoInfoCollection playlistInfo = null;
                try {
                    videoInfo = YoutubeDL.getInstance().getInfo(testVideoURL);
                    playlistInfo = YoutubeDL.getInstance().getInfo(testPlaylistURL);
                } catch (YoutubeDLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (videoInfo == null || playlistInfo == null) {
                    Log.e(Drop.TAG, "Can't continue. Issue occurred when grabbing video info.");
                    return;
                }

                DownloadQueue.queueCallbacks.add(new DownloadQueued() {
                    public void onQueued(DownloadRequest request) {
                        Log.i(Drop.TAG, " ===== [ QUEUED ] =====");
                        Log.i(Drop.TAG, request.id.toString());
                    }
                });
                DownloadQueue.startCallbacks.add(new DownloadStarted() {
                    public void onStart(DownloadRequest request) {
                        Log.i(Drop.TAG, " ===== [ STARTED ] =====");
                        Log.i(Drop.TAG, request.id.toString());
                    }
                });
                DownloadQueue.downloadProgressCallbacks.add(new DownloadProgressChange() {
                    public void onProgressChange(DownloadRequest request, DownloadProgress progress) {
                        Log.i(Drop.TAG, " ===== [ PROGRESS ] =====");
                        Log.i(Drop.TAG, request.id.toString());
                        Log.i(Drop.TAG, new Gson().toJson(progress));
                    }
                });
                DownloadQueue.convertProgressCallbacks.add(new DownloadConvertProgressChange() {
                    public void onProgressChange(DownloadRequest request, FFmpegProgressUpdate progress) {
                        Log.i(Drop.TAG, " ===== [ CONVERT ] =====");
                        Log.i(Drop.TAG, request.id.toString());
                        Log.i(Drop.TAG, new Gson().toJson(progress));
                    }
                });
                DownloadQueue.completionCallbacks.add(new DownloadCompletion() {
                    public void onCompleted(DownloadRequest request, YoutubeDLResponse response) {
                        Log.i(Drop.TAG, " ===== [ COMPLETE ] =====");
                        Log.i(Drop.TAG, request.id.toString());
                        Log.i(Drop.TAG, String.valueOf(response.getExitCode()));
                        Log.i(Drop.TAG, response.getOut());
                    }
                });
                DownloadQueue.failureCallbacks.add(new DownloadFailure() {
                    public void onFailure(DownloadRequest request, Exception cause) {
                        Log.e(Drop.TAG, " ===== [ FUCK ] =====");
                        Log.e(Drop.TAG, request.id.toString());
                        cause.printStackTrace();
                    }
                });

                assert videoInfo != null;
                assert playlistInfo != null;

                 // DownloadQueue.i().add(videoInfo, OutputFormat.MP4, null, Drop.DOWNLOAD_OUTPUT_DIRECTORY, null);
                DownloadQueue.i().add(playlistInfo, OutputFormat.MP4, null, Drop.DOWNLOAD_OUTPUT_DIRECTORY, null);
                // DownloadQueue.i().add(playlistInfo, "mp3", "bestaudio", Drop.DOWNLOAD_OUTPUT_DIRECTORY, null);
            }
        }).start();
    }

    public void tester() {
        Log.i(Drop.TAG, new Gson().toJson(listFiles(Drop.DATA_WORKSPACE_DIRECTORY)));
        Log.i(Drop.TAG, new Gson().toJson(listFiles(Drop.DOWNLOAD_OUTPUT_DIRECTORY)));
    }
}
