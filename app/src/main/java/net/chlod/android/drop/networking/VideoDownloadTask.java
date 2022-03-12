package net.chlod.android.drop.networking;

import android.util.Log;

import com.google.gson.Gson;

import net.chlod.android.drop.Drop;
import net.chlod.android.drop.enums.DownloadRequestState;
import net.chlod.android.drop.enums.OutputFormat;
import net.chlod.android.drop.enums.RequestOptionType;
import net.chlod.android.drop.exceptions.BadFormatCombinationException;
import net.chlod.android.drop.manager.DownloadQueue;
import net.chlod.android.drop.objects.DownloadProgress;
import net.chlod.android.drop.objects.DownloadRequest;
import net.chlod.android.drop.objects.FormatPair;
import net.chlod.android.drop.utility.RequestOptionUtilities;
import net.chlod.ytdl_android.DownloadProgressCallback;
import net.chlod.ytdl_android.YoutubeDL;
import net.chlod.ytdl_android.YoutubeDLException;
import net.chlod.ytdl_android.YoutubeDLRequest;
import net.chlod.ytdl_android.YoutubeDLResponse;
import net.chlod.ytdl_android_ffmpeg.FFmpeg;
import net.chlod.ytdl_android_ffmpeg.FFmpegException;
import net.chlod.ytdl_android_ffmpeg.FFmpegProgressCallback;
import net.chlod.ytdl_android_ffmpeg.FFmpegResponse;
import net.chlod.ytdl_android_ffmpeg.objects.FFmpegProgressUpdate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class VideoDownloadTask implements Runnable {

    public static boolean isAudioOnlyFormat(OutputFormat format) {
        return format == OutputFormat.MP3 || format == OutputFormat.WAV;
    }

    private DownloadRequestState state;

    public final UUID id;

    private final DownloadRequest request;
    private final OutputFormat targetFormat;

    private final File targetFile;

    private volatile DownloadProgress progress = new DownloadProgress();
    public volatile Exception exception = null;

    public VideoDownloadTask(DownloadRequest request, File targetFile, OutputFormat targetFormat) {
        id = UUID.randomUUID();

        this.targetFormat = targetFormat;

        this.targetFile = targetFile;
        this.request = request;
        request.setTask(this);

        state = DownloadRequestState.QUEUED;
        DownloadQueue.triggerCallbacks(DownloadRequestState.QUEUED, request);
    }

    @Override
    public void run() {
        Log.i(Drop.TAG + " - " + request.id, "Starting download of " + request.videoInfo.title);

        String targetExtension = FilenameUtils.getExtension(targetFile.getName());
        File temporaryPath = new File(Drop.DATA_WORKSPACE_DIRECTORY, request.id.toString());

        if (temporaryPath.exists()) {
            try {
                FileUtils.deleteDirectory(temporaryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //noinspection ResultOfMethodCallIgnored
        temporaryPath.mkdirs();

        boolean audioHasAudio =
                request.selectedFormats.audio != null && !request.selectedFormats.audio.acodec.equals("none");
        boolean audioHasVideo =
                request.selectedFormats.audio != null && !request.selectedFormats.audio.vcodec.equals("none");
        boolean videoHasAudio =
                request.selectedFormats.video != null && !request.selectedFormats.video.acodec.equals("none");
        boolean videoHasVideo =
                request.selectedFormats.video != null && !request.selectedFormats.video.vcodec.equals("none");

        state = DownloadRequestState.STARTING;
        DownloadQueue.triggerCallbacks(DownloadRequestState.STARTING, request);
        Log.i(Drop.TAG + " - " + request.id, "Begin " + request.videoInfo.title);

        File videoPartFile = new File(temporaryPath, request.id + ".video");
        File audioPartFile = new File(temporaryPath, request.id + ".audio");
        File temporaryTargetFile = new File(temporaryPath, request.id + "." + targetFormat.name().toLowerCase());

        YoutubeDLResponse videoResponse = null;
        YoutubeDLResponse audioResponse = null;

        if (request.selectedFormats.video != null)
            videoResponse = executeDownloadRequest(request, FormatPair.Part.VIDEO, videoPartFile);
        if (request.selectedFormats.audio != null)
            audioResponse = executeDownloadRequest(request, FormatPair.Part.AUDIO, audioPartFile);

        state = DownloadRequestState.CONVERTING;
        // TODO make callbacks

        if (isAudioOnlyFormat(targetFormat)) { // no audio but audio only
            if (!audioHasAudio && !videoHasAudio) {
                failWithException(
                    new BadFormatCombinationException("No audio on both tracks for audio-only format."));
            } else if (audioHasAudio) {
                extractStreamIntoFile(FormatPair.Part.AUDIO, audioPartFile, temporaryTargetFile);
            } else { // video has audio
                extractStreamIntoFile(FormatPair.Part.AUDIO, videoPartFile, temporaryTargetFile);
            }
        } else {
            if (!audioHasVideo && !videoHasVideo) { // all outputs are audio
                if (audioHasAudio) {
                    extractStreamIntoFile(FormatPair.Part.AUDIO, audioPartFile, temporaryTargetFile);
                } else if (videoHasAudio) { // video has audio
                    extractStreamIntoFile(FormatPair.Part.AUDIO, videoPartFile, temporaryTargetFile);
                } else {
                    failWithException(
                        new BadFormatCombinationException("No streams on both tracks."));
                }
            } else if (videoHasVideo) {
                if (audioHasAudio) {
                    mergeMediaFiles(audioPartFile, videoPartFile, temporaryTargetFile);
                } else if (videoHasAudio) { // video has audio
                    mergeMediaFiles(videoPartFile, videoPartFile, temporaryTargetFile);
                } else {
                    extractStreamIntoFile(FormatPair.Part.VIDEO, videoPartFile, temporaryTargetFile);
                }
            } else {
                if (audioHasAudio) {
                    mergeMediaFiles(audioPartFile, audioPartFile, temporaryTargetFile);
                } else if (videoHasAudio) { // video has audio
                    mergeMediaFiles(videoPartFile, audioPartFile, temporaryTargetFile);
                } else {
                    extractStreamIntoFile(FormatPair.Part.VIDEO, audioPartFile, temporaryTargetFile);
                }
            }
        }

        if (targetFile.exists()) {
            Log.w(Drop.TAG, "Download target exists already! OVERWRITING!!!");
            //noinspection ResultOfMethodCallIgnored
            targetFile.delete();
        }
        try {
            FileUtils.moveFile(temporaryTargetFile, targetFile);
        } catch (IOException e) {
            failWithException(e);
        }

        state = DownloadRequestState.FINISHED;
        DownloadQueue.triggerCallbacks(DownloadRequestState.FINISHED, request);
        Log.e(Drop.TAG + " - " + request.id, "done " + request.videoInfo.title);

        try {
            cleanup();
        } catch (IOException e) {
            Log.w(Drop.TAG, "Failed to clean up: " + request.id);
            e.printStackTrace();
        }
    }

    public YoutubeDLResponse executeDownloadRequest(final DownloadRequest request, FormatPair.Part part, File targetFile) {
        progress.ongoing = part;
        YoutubeDLResponse response = null;
        try {
            YoutubeDLRequest ytdlRequest = request.generateRequest(part);
            if (ytdlRequest == null) return null;

            RequestOptionUtilities.setRequestOption(ytdlRequest, RequestOptionType.OUTPUT,
                targetFile.getAbsolutePath().replace("\"", "\\\""));
            response = YoutubeDL.getInstance().execute(ytdlRequest,
                new DownloadProgressCallback() {
                    @Override
                    public void onProgressUpdate(final float _progress, final long etaInSeconds) {
                        state = DownloadRequestState.DOWNLOADING;
                        progress.progress = _progress;
                        progress.eta = etaInSeconds;
                        DownloadQueue.triggerCallbacks(DownloadRequestState.DOWNLOADING, request, progress);
                        Log.i(Drop.TAG + " - " + request.id, _progress + "% " + request.videoInfo.title);
                    }
                });
        } catch (InterruptedException e) {
            interruptWithException(e);
        } catch (YoutubeDLException e) {
            failWithException(e);
        } catch (Throwable t) {
            failWithException(new Exception(t));
        }
        return response;
    }

    public FFmpegResponse mergeMediaFiles(File audioFile, File videoFile, File targetFile) {
        FFmpegResponse response = null;
        try {
            Log.i(Drop.TAG, "beginning conversion");
            response = FFmpeg.getInstance().execute(new String[]{
                    "-v",
                    "repeat+verbose",
                    "-y",
                    "-i",
                    audioFile.getAbsolutePath().replace("\"", "\\\""),
                    "-i",
                    videoFile.getAbsolutePath().replace("\"", "\\\""),
                    "-map",
                    "1:v",
                    "-map",
                    "0:a",
                    "-c:v",
                    "copy",
                    "-c:a",
                    "aac",
                    targetFile.getAbsolutePath().replace("\"", "\\\"")
            }, new FFmpegProgressCallback() {
                @Override
                public void onProgressUpdate(FFmpegProgressUpdate update) {
                    DownloadQueue.triggerCallbacks(DownloadRequestState.CONVERTING, request, update);
                    Log.i(Drop.TAG + " - " + request.id, new Gson().toJson(update));
                }
            });
        } catch (InterruptedException e) {
            failWithException(e);
        } catch (FFmpegException e) {
            failWithException(e);
        }
        return response;
    }

    public FFmpegResponse convertMediaFiles(File sourceFile, File targetFile) {
        FFmpegResponse response = null;
        try {
            Log.i(Drop.TAG, "beginning conversion");
            response = FFmpeg.getInstance().execute(new String[]{
                    "-v",
                    "repeat+verbose",
                    "-y",
                    "-i",
                    sourceFile.getAbsolutePath().replace("\"", "\\\""),
                    targetFile.getAbsolutePath().replace("\"", "\\\"")
            }, new FFmpegProgressCallback() {
                @Override
                public void onProgressUpdate(FFmpegProgressUpdate update) {
                    DownloadQueue.triggerCallbacks(DownloadRequestState.CONVERTING, request, update);
                    Log.i(Drop.TAG + " - " + request.id, new Gson().toJson(update));
                }
            });
        } catch (InterruptedException e) {
            failWithException(e);
        } catch (FFmpegException e) {
            failWithException(e);
        }
        return response;
    }

    public FFmpegResponse extractStreamIntoFile(FormatPair.Part part, File sourceFile, File targetFile) {
        FFmpegResponse response = null;
        try {
            Log.i(Drop.TAG, "beginning conversion");
            response = FFmpeg.getInstance().execute(new String[]{
                    "-v",
                    "repeat+verbose",
                    "-y",
                    "-i",
                    sourceFile.getAbsolutePath().replace("\"", "\\\""),
                    "-map ",
                    (part == FormatPair.Part.AUDIO ?
                        "0:a" :
                        "0:v"),
                    (part == FormatPair.Part.AUDIO ?
                            "-c:a" :
                            "-c:v"),
                    (part == FormatPair.Part.AUDIO ?
                            "aac" :
                            "copy"),
                    targetFile.getAbsolutePath().replace("\"", "\\\"")
            }, new FFmpegProgressCallback() {
                @Override
                public void onProgressUpdate(FFmpegProgressUpdate update) {
                    DownloadQueue.triggerCallbacks(DownloadRequestState.CONVERTING, request, update);
                    Log.i(Drop.TAG + " - " + request.id, new Gson().toJson(update));
                }
            });
        } catch (InterruptedException e) {
            failWithException(e);
        } catch (FFmpegException e) {
            failWithException(e);
        }
        return response;
    }

    public void failWithException(Exception e) {
        state = DownloadRequestState.FAILED;
        exception = e;
        DownloadQueue.triggerCallbacks(DownloadRequestState.FAILED, request, e);
        Log.e(Drop.TAG + " - " + request.id, "FAILED " + request.videoInfo.title);
        e.printStackTrace();
    }

    public void interruptWithException(Exception e) {
        state = DownloadRequestState.INTERRUPTED;
        exception = e;
        DownloadQueue.triggerCallbacks(DownloadRequestState.INTERRUPTED, request, e);
        Log.e(Drop.TAG + " - " + request.id, "INTERRUPTED " + request.videoInfo.title);
        e.printStackTrace();
    }

    public void cleanup() throws IOException {
        File temporaryPath = new File(Drop.DATA_WORKSPACE_DIRECTORY, request.id.toString());
        FileUtils.deleteDirectory(temporaryPath);
    }

    public DownloadProgress getProgress() {
        return progress;
    }

    public DownloadRequestState getState() {
        return state;
    }

}
