package net.chlod.android.drop.interfaces;

import net.chlod.android.drop.objects.DownloadProgress;
import net.chlod.android.drop.objects.DownloadRequest;

public interface DownloadProgressChange extends DownloadCallback {

    public void onProgressChange(DownloadRequest request, DownloadProgress progress);

}
