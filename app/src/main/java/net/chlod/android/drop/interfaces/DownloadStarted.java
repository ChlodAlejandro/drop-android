package net.chlod.android.drop.interfaces;

import net.chlod.android.drop.objects.DownloadRequest;

public interface DownloadStarted extends DownloadCallback {

    public void onStart(DownloadRequest request);

}
