package net.chlod.android.drop.interfaces;

import net.chlod.android.drop.objects.DownloadProgress;
import net.chlod.android.drop.objects.DownloadRequest;
import net.chlod.ytdl_android_ffmpeg.objects.FFmpegProgressUpdate;

public interface DownloadConvertProgressChange extends DownloadCallback {

    public void onProgressChange(DownloadRequest request, FFmpegProgressUpdate progress);

}
