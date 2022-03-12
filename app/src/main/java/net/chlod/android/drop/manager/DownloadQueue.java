package net.chlod.android.drop.manager;

import android.content.Context;

import androidx.annotation.Nullable;

import net.chlod.android.drop.enums.DownloadRequestState;
import net.chlod.android.drop.enums.OutputFormat;
import net.chlod.android.drop.enums.RequestOptionType;
import net.chlod.android.drop.interfaces.DownloadCallback;
import net.chlod.android.drop.interfaces.DownloadCompletion;
import net.chlod.android.drop.interfaces.DownloadConvertProgressChange;
import net.chlod.android.drop.interfaces.DownloadFailure;
import net.chlod.android.drop.interfaces.DownloadProgressChange;
import net.chlod.android.drop.interfaces.DownloadQueued;
import net.chlod.android.drop.interfaces.DownloadStarted;
import net.chlod.android.drop.networking.VideoDownloadTask;
import net.chlod.android.drop.objects.DownloadProgress;
import net.chlod.android.drop.objects.DownloadRequest;
import net.chlod.android.drop.objects.FormatPair;
import net.chlod.ytdl_android.YoutubeDLResponse;
import net.chlod.ytdl_android.mapper.VideoInfo;
import net.chlod.ytdl_android.objects.VideoInfoCollection;
import net.chlod.ytdl_android_ffmpeg.objects.FFmpegProgressUpdate;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadQueue {
    private static DownloadQueue instance;

    public static List<DownloadQueued> queueCallbacks = new ArrayList<>();
    public static List<DownloadStarted> startCallbacks = new ArrayList<>();
    public static List<DownloadProgressChange> downloadProgressCallbacks = new ArrayList<>();
    public static List<DownloadConvertProgressChange> convertProgressCallbacks = new ArrayList<>();
    public static List<DownloadCompletion> completionCallbacks = new ArrayList<>();
    public static List<DownloadFailure> failureCallbacks = new ArrayList<>();

    public static void init(WeakReference<Context> applicationContext) {
        instance = new DownloadQueue(applicationContext);
    }
    public static void assertInit() throws Exception {
        if (instance == null)
            throw new Exception("DownloadQueue has not been initialized.");
    }

    public static void triggerCallbacks(DownloadRequestState callbackType, DownloadRequest request, Object extra) {
        switch (callbackType) {
            case QUEUED:
                for (DownloadQueued callback : queueCallbacks)
                    callback.onQueued(request);
                break;
            case STARTING:
                for (DownloadStarted callback : startCallbacks)
                    callback.onStart(request);
                break;
            case DOWNLOADING:
                for (DownloadProgressChange callback : downloadProgressCallbacks)
                    callback.onProgressChange(request, (DownloadProgress) extra);
                break;
            case CONVERTING:
                for (DownloadConvertProgressChange callback : convertProgressCallbacks)
                    callback.onProgressChange(request, (FFmpegProgressUpdate) extra);
                break;
            case FINISHED:
                for (DownloadCompletion callback : completionCallbacks)
                    callback.onCompleted(request, (YoutubeDLResponse) extra);
                break;
            case INTERRUPTED:
            case FAILED:
                for (DownloadFailure callback : failureCallbacks)
                    callback.onFailure(request, (Exception) extra);
                break;
        }
    }

    public static void triggerCallbacks(DownloadRequestState callbackType, DownloadRequest request) {
        triggerCallbacks(callbackType, request, null);
    }

    public static DownloadQueue i() {
        return instance;
    }

    private static List<VideoDownloadTask> queue = new ArrayList<>();
    // make nThreads configurable in settings soon.
    private ExecutorService downloadService = Executors.newFixedThreadPool(1);
    private WeakReference<Context> context;

    private DownloadQueue(WeakReference<Context> applicationContext) {
        this.context = applicationContext;
    }

    public boolean add(DownloadRequest downloadRequest, File targetFile, OutputFormat targetFormat) {
        VideoDownloadTask vdt = new VideoDownloadTask(downloadRequest, targetFile, targetFormat);
        boolean res = queue.add(vdt);
        downloadService.submit(vdt);
        requestSave();
        return res;
    }

    public boolean add(VideoInfoCollection videoInfoCollection,
                       OutputFormat targetFormat,
                       @Nullable List<FormatPair> selectedFormats,
                       File targetDirectory,
                       Map<RequestOptionType, String> requestOptions) {
        boolean res = true;

        if (selectedFormats == null) {
            List<FormatPair> bestFormats = new ArrayList<>();
            for (int j = 0; j < videoInfoCollection.infoList.size(); j++)
                bestFormats.add(null);
            selectedFormats = bestFormats;
        }

        if (videoInfoCollection.infoList.size() != selectedFormats.size())
            throw new IllegalArgumentException("List of formats do not match list of video info objects.");

        for (int i = 0; i < videoInfoCollection.infoList.size(); i++) {
            VideoInfo vi = videoInfoCollection.infoList.get(i);
            FormatPair selectedFormat = selectedFormats.get(i);

            File targetSubdirectory =  vi.playlistName != null ? new File(targetDirectory,
                    vi.playlistName.replaceAll(
                            "/[^A-Za-z0-9\\-,.()]/gi", "_")) : targetDirectory;

            if (!targetSubdirectory.exists()) {
                //noinspection ResultOfMethodCallIgnored
                targetSubdirectory.mkdirs();
            }

            DownloadRequest request = new DownloadRequest(
                vi.webpageUrl, vi, selectedFormat, requestOptions
            );
            res = res && add(
                request,
                new File(
                    targetSubdirectory,
                    vi.getFulltitle().replaceAll("\\.", "_") + "." + targetFormat.name().toLowerCase()),
                targetFormat);
        }
        return res;
    }

    public void restartDownload(UUID vdtId) {
        for (VideoDownloadTask vtd : queue) {
            if (vtd.id.equals(vdtId)) {
                if (vtd.getState() == DownloadRequestState.INTERRUPTED
                    || vtd.getState() == DownloadRequestState.FAILED)
                    downloadService.submit(vtd);
                break;
            }
        }
    }

    public void forceRestartDownload(UUID vdtId) {
        for (VideoDownloadTask vtd : queue) {
            if (vtd.id.equals(vdtId)) {
                downloadService.submit(vtd);
                break;
            }
        }
    }

    public void requestSave() {
        DownloadQueuePersistence.i().requestQueueSave();
    }

    public void registerCallback(DownloadCallback callback) {
        if (callback instanceof  DownloadQueued)
            queueCallbacks.add((DownloadQueued) callback);
        else if (callback instanceof  DownloadProgressChange)
            downloadProgressCallbacks.add((DownloadProgressChange) callback);
        else if (callback instanceof DownloadConvertProgressChange)
            convertProgressCallbacks.add((DownloadConvertProgressChange) callback);
        else if (callback instanceof  DownloadCompletion)
            completionCallbacks.add((DownloadCompletion) callback);
        else if (callback instanceof  DownloadFailure)
            failureCallbacks.add((DownloadFailure) callback);
        else
            throw new IllegalArgumentException("callback is not a valid callback type");
    }

}
