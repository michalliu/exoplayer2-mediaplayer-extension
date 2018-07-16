package com.google.android.exoplayer2.ext.audio;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.C;

import java.util.Arrays;

/**
 * Created by leoliu on 2018/6/19.
 */
/* This class holds up to 60 ms of super-wideband (44.1 kHz) stereo audio. It
 * allows for adding and subtracting frames while keeping track of the resulting
 * states.
 *
 * {@link https://chromium.googlesource.com/external/webrtc/+/lkgr/api/audio/audio_frame.cc}
 *
 * Notes
 * - This is a de-facto api, not designed for external use. The AudioFrame class
 *   is in need of overhaul or even replacement, and anyone depending on it
 *   should be prepared for that.
 * - The total number of samples is samples_per_channel_ * num_channels_.
 * - Stereo data is interleaved starting with the left channel.
 */
public class AudioFrame {

    // Stereo, 44.1 kHz, 60 ms (2 * 44.1 * 60)
    private static int kMaxDataSizeSamples = 5292;
    private static short kSizeOfShort = 2;
    private static int kMaxDataSizeBytes = kMaxDataSizeSamples * kSizeOfShort;

    // RTP timestamp of the first sample in the AudioFrame.
    private long timestamp_ = C.TIME_UNSET;
    short samples_per_channel_ = 0;
    short num_channels_ = 0;
    private int sample_rate_hz_ = 0;
    private boolean muted_ = true;

    private int offset_ = 0;
    private String tag_ = "";

    private boolean isDebug_ = true;

    AudioFrame(String frameTag) {
        tag_ = frameTag;
    }

    private byte[] data_ = new byte[kMaxDataSizeBytes];

    // Resets all members to their default state.
    void UpdateFrame(long timestamp,
                     byte[] data,
                     short samples_per_channel,
                     int sample_rate_hz,
                     short num_channels) {
        timestamp_ = timestamp;
        samples_per_channel_ = samples_per_channel;
        sample_rate_hz_ = sample_rate_hz;
        num_channels_ = num_channels;

        int length = samples_per_channel * num_channels;
        Assertions.checkArgument(length < kMaxDataSizeSamples);
        if (data != null) {
            System.arraycopy(data, 0, data_, 0, length * kSizeOfShort);
            muted_ = false;
        } else {
            muted_ = true;
        }
    }

    // same as UpdateFrame but ensures 60ms stereo or mono in each frame
    // returns bytes are written to audio frame
    // this method can be called multiple times, returns 0 indicate the frame data is full
    int UpdateFrameChunk(long timestamp,
                     byte[] data,
                     int offset,
                     int length,
                     int sample_rate_hz,
                     short num_channels) {
        if (timestamp_ == C.TIME_UNSET) {
            timestamp_ = timestamp;
        }
        Assertions.checkArgument(sample_rate_hz > 0);
        Assertions.checkArgument(sample_rate_hz_ == 0 || sample_rate_hz_ == sample_rate_hz);
        sample_rate_hz_ = sample_rate_hz;
        Assertions.checkArgument(num_channels > 0);
        Assertions.checkArgument(num_channels_== 0 || num_channels_ == num_channels);
        num_channels_ = num_channels;
        int bytesRemain = kMaxDataSizeBytes - offset_;
        int bytesWritten = Math.min(length, bytesRemain);
        if (bytesWritten > 0) {
            if (data != null) {
                System.arraycopy(data, offset, data_, offset_, bytesWritten);
                muted_ = false;
                Assertions.checkArgument(bytesRemain % kSizeOfShort == 0);
                samples_per_channel_ += bytesWritten / kSizeOfShort / num_channels_;
                offset_ += bytesWritten;
            }
        }
        // avoid double write when bytesRemain and bytesWritten both were zero
        Assertions.checkArgument(bytesRemain > 0);
        if (bytesRemain - bytesWritten == 0) {
            onFrameAvailable();
        }
        return bytesWritten;
    }

    // frame is filled
    private void onFrameAvailable() {

    }

    // duration in seconds
    // https://w3c.github.io/webrtc-stats/#dom-rtcaudiohandlerstats-totalaudioenergy
    double duration() {
        return (double) samples_per_channel_ / sample_rate_hz_;
    }

    int available() {
        return kMaxDataSizeBytes - offset_;
    }

    // Same as Reset(), but leaves mute state unchanged. Muting a frame requires
    // the buffer to be zeroed on the next call to mutable_data(). Callers
    // intending to write to the buffer immediately after Reset() can instead use
    // ResetWithoutMuting() to skip this wasteful zeroing.
    void ResetWithoutMuting() {
        timestamp_ = C.TIME_UNSET;
        samples_per_channel_ = 0;
        sample_rate_hz_ = 0;
        num_channels_ = 0;
        offset_ = 0;
    }

    void CopyFrom(AudioFrame src) {
        if (src.equals(this)) return;
        timestamp_ = src.timestamp_;
        muted_ = src.muted();
        samples_per_channel_ = src.samples_per_channel_;
        sample_rate_hz_ = src.sample_rate_hz_;
        num_channels_ = src.num_channels_;
        offset_ = src.offset_;

        int length = samples_per_channel_ * num_channels_;
        Assertions.checkArgument(length < kMaxDataSizeSamples);
        if (!src.muted()) {
            System.arraycopy(src.data(), 0, data_, 0, length * kSizeOfShort);
            muted_ = false;
        }
    }

    // data() returns a zeroed static buffer if the frame is muted.
    // mutable_frame() always returns a non-static buffer; the first call to
    // mutable_frame() zeros the non-static buffer and marks the frame unmuted.
    byte[] data() {
        return muted_ ? empty_data() : data_;
    }

    // TODO(henrik.lundin) Can we skip zeroing the buffer?
    // See https://bugs.chromium.org/p/webrtc/issues/detail?id=5647.
    byte[] mutable_data() {
        if (muted_) {
            Arrays.fill(data_, 0, kMaxDataSizeBytes - 1, (byte) 0);
            muted_ = false;
        }
        return data_;
    }

    // Prefer to mute frames using AudioFrameOperations::Mute.
    void Mute() {
        muted_ = true;
    }

    // Frame is muted by default.
    boolean muted() {
        return muted_;
    }

    // A permamently zeroed out buffer to represent muted frames. This is a
    // header-only class, so the only way to avoid creating a separate empty
    // buffer per translation unit is to wrap a static in an inline function.
    static byte[] empty_data() {
        return new byte[kMaxDataSizeBytes];
    }
}
