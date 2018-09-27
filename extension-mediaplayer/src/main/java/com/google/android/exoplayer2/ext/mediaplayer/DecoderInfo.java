package com.google.android.exoplayer2.ext.mediaplayer;

public class DecoderInfo {
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_UNKNOWN = -1;

    public int decoderType = TYPE_UNKNOWN; //
    public String decoderName = "";

//    public long initializedTimestampMs = 0;
    public long initializationDurationMs = 0;

    DecoderInfo(int decoderType, String decoderName, long initializationDurationMs) {
        this.decoderType = decoderType;
        this.decoderName = decoderName;
        this.initializationDurationMs = initializationDurationMs;
    }

    private String type2Str(int decoderType) {
        switch (decoderType) {
            case TYPE_AUDIO:
                return "adec";
            case TYPE_VIDEO:
                return "vdec";
            default:
                return "unknown";
        }
    }

    @Override
    public String toString() {
        return type2Str(decoderType)
                + ": " + decoderName
                + "," + initializationDurationMs;
    }
}
