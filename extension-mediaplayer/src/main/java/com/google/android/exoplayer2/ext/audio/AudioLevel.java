package com.google.android.exoplayer2.ext.audio;

import com.google.android.exoplayer2.ext.util.AudioUtil;

/**
 * Created by leoliu on 2018/6/19.
 */

// calculate audio level according to webrtc implementation
// https://chromium.googlesource.com/external/webrtc/+/lkgr/audio/audio_level.cc
// https://chromium.googlesource.com/external/webrtc/+/lkgr/audio/audio_level.h
public class AudioLevel {
    private static final short kUpdateFrequency = 1;
    static final short kSizeOfShort = 2;
    //https://chromium.googlesource.com/external/webrtc/+/lkgr/common_audio/signal_processing/include/signal_processing_library.h
    private static final short WEBRTC_SPL_WORD16_MAX = Short.MAX_VALUE;

    private short abs_max_ = 0;
    private short count_ = 0;
    private short current_level_full_range_ = 0;

    private double total_energy_ = 0.0f;
    private double total_duration_ = 0.0f;

    public AudioLevel() {}

    // Called on "API thread(s)" from APIs like VoEBase::CreateChannel(),
    // VoEBase::StopSend()
    short LevelFullRange() {
        return current_level_full_range_;
    }

    void Clear() {
        abs_max_ = 0;
        count_ = 0;
        current_level_full_range_ = 0;
    }

    // See the description for "totalAudioEnergy" in the WebRTC stats spec
    // (https://w3c.github.io/webrtc-stats/#dom-rtcmediastreamtrackstats-totalaudioenergy)
    double TotalEnergy() {
        return total_energy_;
    }
    double TotalDuration() {
        return total_duration_;
    }

    // Called on a native capture audio thread (platform dependent) from the
    // AudioTransport::RecordedDataIsAvailable() callback.
    // In Chrome, this method is called on the AudioInputDevice thread.
    void ComputeLevel(AudioFrame audioFrame, double duration) {
        // Check speech level (works for 2 channels as well)
        short abs_value = audioFrame.muted() ? 0 : WebRtcSpl_MaxAbsValueW16J(audioFrame.data(),
                audioFrame.samples_per_channel_ * audioFrame.num_channels_);
        if (abs_value > abs_max_) {
            abs_max_ = abs_value;
        }
        // Update level approximately 10 times per second
        if (count_++ == kUpdateFrequency) {
            current_level_full_range_ = abs_max_;
            count_ = 0;

            // Decay the absolute maximum (divide by 4)
            abs_max_ >>= 2;
        }

        // See the description for "totalAudioEnergy" in the WebRTC stats spec
        // (https://w3c.github.io/webrtc-stats/#dom-rtcmediastreamtrackstats-totalaudioenergy)
        // for an explanation of these formulas. In short, we need a value that can
        // be used to compute RMS audio levels over different time intervals, by
        // taking the difference between the results from two getStats calls. To do
        // this, the value needs to be of units "squared sample value * time".
        double additional_energy = (double) current_level_full_range_ / Short.MAX_VALUE;
        additional_energy *= additional_energy;
        total_energy_ += additional_energy * duration;
        total_duration_ += duration;
    }

    // Maximum absolute value of word16 vector. C version for generic platforms.
    // https://github.com/ReadyTalk/webrtc/blob/master/webrtc/common_audio/signal_processing/min_max_operations.c
    static short WebRtcSpl_MaxAbsValueW16J(byte[] data, int length) {
        int i=0, j=0, absolute = 0, maximum = 0;
        if (data == null || length <= 0) {
            return -1;
        }
        for (i = 0; i < length; i++) {
            j = i * kSizeOfShort;
            absolute = Math.abs(AudioUtil.byteToShort(data[j], data[j+1]));
            if (absolute > maximum) {
                maximum = absolute;
            }
        }
        if (maximum > WEBRTC_SPL_WORD16_MAX) {
            maximum = WEBRTC_SPL_WORD16_MAX;
        }
        return (short) maximum;
    }
}
