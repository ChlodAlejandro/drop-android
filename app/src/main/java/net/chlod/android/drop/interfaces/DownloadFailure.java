package net.chlod.android.drop.interfaces;

import net.chlod.android.drop.objects.DownloadRequest;

public interface DownloadFailure extends DownloadCallback {

    public void onFailure(DownloadRequest request, Exception cause);

}
