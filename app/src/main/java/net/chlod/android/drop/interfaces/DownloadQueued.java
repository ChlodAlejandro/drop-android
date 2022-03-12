package net.chlod.android.drop.interfaces;

import net.chlod.android.drop.objects.DownloadRequest;

public interface DownloadQueued extends DownloadCallback {

    public void onQueued(DownloadRequest request);

}
