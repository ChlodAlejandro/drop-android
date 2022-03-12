package net.chlod.android.drop.objects;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import net.chlod.android.drop.Drop;
import net.chlod.android.drop.enums.RequestOptionType;
import net.chlod.android.drop.networking.VideoDownloadTask;
import net.chlod.android.drop.utility.RequestOptionUtilities;
import net.chlod.ytdl_android.YoutubeDLRequest;
import net.chlod.ytdl_android.mapper.VideoFormat;
import net.chlod.ytdl_android.mapper.VideoInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DownloadRequest {

    public final UUID id;

    public final String videoUrl;
    public final VideoInfo videoInfo;
    public final FormatPair selectedFormats;
    public final Map<RequestOptionType, String> requestOptions;

    private VideoDownloadTask downloadTask;

    public DownloadRequest(
            String videoUrl,
            VideoInfo videoInfo,
            @Nullable FormatPair selectedPair,
            Map<RequestOptionType, String> requestOptions) {
        id = UUID.randomUUID();

        if (selectedPair == null || (selectedPair.audio == null && selectedPair.video == null)) {
            List<VideoFormat> probableFormats = videoInfo.requestedFormats;
            VideoFormat detectedAudioFormat = null;
            VideoFormat detectedVideoFormat = null;

            for (VideoFormat format : probableFormats) {
                if (!format.acodec.equals("none") &&
                        (detectedAudioFormat == null || detectedAudioFormat.abr < format.abr)) {
                    detectedAudioFormat = format;
                }
                if (!format.vcodec.equals("none") &&
                        (detectedVideoFormat == null || detectedVideoFormat.filesize < format.filesize)) {
                    detectedVideoFormat = format;
                }
            }

            if (detectedAudioFormat == null && detectedVideoFormat == null) {
                // In the very unlikely event that YouTube-DL does not provide
                // a default format, we're going to have to guess.

                for (VideoFormat format : videoInfo.formats) {
                    if (!format.acodec.equals("none") &&
                            (detectedAudioFormat == null || detectedAudioFormat.abr < format.abr)) {
                        detectedAudioFormat = format;
                    }
                    if (!format.vcodec.equals("none") &&
                            (detectedVideoFormat == null || detectedVideoFormat.filesize < format.filesize)) {
                        detectedVideoFormat = format;
                    }
                }
            }

            this.selectedFormats = new FormatPair(detectedVideoFormat, detectedAudioFormat);
        } else {
            this.selectedFormats = selectedPair;
        }

        System.out.println(selectedFormats.audio);
        System.out.println(selectedFormats.video);

        System.out.println("=====================");

        boolean audioFound = false;
        boolean videoFound = false;
        for (VideoFormat f : videoInfo.formats) {
            if (selectedFormats.audio == null || f.toString().equals(selectedFormats.audio.toString()))
                audioFound = true;
            if (selectedFormats.video == null || f.toString().equals(selectedFormats.video.toString()))
                videoFound = true;
        }

        if (!audioFound || !videoFound) {
            throw new IllegalArgumentException("One of the selected formats do not belong to the video in this request.");
        }

        this.videoUrl = videoUrl;
        this.videoInfo = videoInfo;

        this.requestOptions = requestOptions == null ? new HashMap<RequestOptionType, String>() : requestOptions;
    }

    @Nullable
    public YoutubeDLRequest generateRequest(FormatPair.Part part) {
        VideoFormat format = part == FormatPair.Part.AUDIO ? selectedFormats.audio : selectedFormats.video;
        if (format == null)
            return null;

        YoutubeDLRequest request = new YoutubeDLRequest(videoUrl);
        requestOptions.put(RequestOptionType.FORMAT_CODE, format.formatId);
        Log.i(Drop.TAG + " - " + id, new Gson().toJson(requestOptions));
        for (Map.Entry<RequestOptionType, String> option : requestOptions.entrySet()) {
            RequestOptionUtilities.setRequestOption(request, option.getKey(), option.getValue());
        }

        return request;
    }

    public void setTask(VideoDownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    public VideoDownloadTask getTask() {
        return downloadTask;
    }

}
