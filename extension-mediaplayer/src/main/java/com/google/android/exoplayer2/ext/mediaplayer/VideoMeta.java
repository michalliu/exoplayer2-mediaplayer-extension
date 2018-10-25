package com.google.android.exoplayer2.ext.mediaplayer;

public class VideoMeta {
    public static final int OPEN_ENDED = -1;

    public final String uri;
    public final long duration; // video duration, in milliseconds

    public long startPosition; // The start position at which to provide samples, in milliseconds
    public long endPosition = OPEN_ENDED; // The end position at which to provide samples, in milliseconds

    public VideoMeta(String uri, long durationMillisecond) {
        this.uri = uri;
        this.duration = durationMillisecond;
    }
}
