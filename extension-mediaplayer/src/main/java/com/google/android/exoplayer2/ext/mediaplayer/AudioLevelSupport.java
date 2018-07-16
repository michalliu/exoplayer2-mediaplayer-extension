package com.google.android.exoplayer2.ext.mediaplayer;

/**
 * Created by leoliu on 2018/7/16.
 */

public interface AudioLevelSupport {
    /**
     * Control if the audio level should be calculated, If the flag is set to false, {@link #getAudioLevel()}
     * always returns 0.0f. The default value is false to save memory and cpu.
     *
     * @param isNeedAudioLevel should calculate audio level
     */
    void setCalculateAudioLevel(boolean isNeedAudioLevel);

    /**
     *
     * Returns audio level in the time period from the last call to this method, Sry for bad english.
     *
     * Reference:
     * https://w3c.github.io/webrtc-stats/#dom-rtcmediastreamtrackstats-totalaudioenergy
     *
     * This can be used to obtain a root mean square (RMS) value that uses the same units as audioLevel,
     * as defined in [RFC6464].
     *
     * It can be converted to these units using the formula Math.sqrt(totalAudioEnergy/totalSamplesDuration).
     * This calculation can also be performed using the differences between the values of two different getStats() calls,
     * in order to compute the average audio level over any desired time interval.
     * In other words, do Math.sqrt((energy2 - energy1)/(duration2 - duration1)).
     */
    double getAudioLevel();

    /**
     * @return audio duration has been played, differ from total duration of a media file
     */
    double getAudioDuration();

    /**
     * To calculate audio level at any time range use the following equation
     * double audioLevel = Math.sqrt((energy2 - energy1)/(duration2 - duration1))
     * audioLevel is ranging from 0.0f to 1.0f
     * @return accumulated audio energy
     */
    double getAudioEnergy();
}
