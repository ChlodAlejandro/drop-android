package net.chlod.android.drop.interfaces;

import net.chlod.android.drop.objects.DownloadRequest;
import net.chlod.ytdl_android.YoutubeDLResponse;

public interface DownloadCompletion extends DownloadCallback {

    public void onCompleted(DownloadRequest request, YoutubeDLResponse response);

}
