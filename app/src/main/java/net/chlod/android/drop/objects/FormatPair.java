package net.chlod.android.drop.objects;

import androidx.annotation.Nullable;

import net.chlod.ytdl_android.mapper.VideoFormat;

public class FormatPair {

    @Nullable
    public final VideoFormat video;
    @Nullable
    public final VideoFormat audio;

    public FormatPair(@Nullable VideoFormat video, @Nullable VideoFormat audio) {
        this.video = video;
        this.audio = audio;
    }

    public enum Part {
        AUDIO,
        VIDEO
    }

}
