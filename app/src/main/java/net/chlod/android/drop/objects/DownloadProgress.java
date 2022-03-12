package net.chlod.android.drop.objects;

public class DownloadProgress {

    public FormatPair.Part ongoing;
    public float progress = 0;
    public long eta = 0;

    public DownloadProgress() {}

    public DownloadProgress(FormatPair.Part ongoing, float progress, long eta) {
        this.ongoing = ongoing;
        this.progress = progress;
        this.eta = eta;
    }
}
