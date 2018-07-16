package com.google.android.exoplayer2.ext.audio;

import android.os.SystemClock;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.Format;

/**
 * Created by leoliu on 2018/6/19.
 */

public class AudioFrameManager {
    private AudioFrame[] audioFrames = null;
    private AudioLevel[] audioLevels = null;
    /**
     * Generate AudioFrame from decoded stream
     */
    public AudioFrameManager() {}

    /**
     * Push audio pcm data
     * @param audioData pcm data
     * @param audioFormat the audioFormat contains channelCount,sampleRate etc...
     */
    public synchronized void feedAudioData(byte[] audioData, Format audioFormat) {
        int channelCount = audioFormat.channelCount;
        Assertions.checkArgument(channelCount > 0 && channelCount <= 2);
        int bytesPerChannel = audioData.length / channelCount;
        byte[][] data = new byte[channelCount][bytesPerChannel];
        int dataIdx = 0;
        int chanIdx = 0;
        for (int j = 0; j < audioData.length; j += AudioLevel.kSizeOfShort) {
            if (chanIdx > channelCount - 1) {
                chanIdx = 0;
                dataIdx += AudioLevel.kSizeOfShort;
            }
            byte[] channelData = data[chanIdx];
            channelData[dataIdx] = audioData[j];
            channelData[dataIdx+1] = audioData[j+1];
            chanIdx++;
        }

        processAudioFrame(data, audioFormat);
    }

    private synchronized void processAudioFrame(byte[][] data, Format audioFormat) {
        // init audio frame for each channel
        if (audioFrames == null) {
            audioFrames = new AudioFrame[data.length];
        }
        if (audioLevels == null) {
            audioLevels = new AudioLevel[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            String frameTag = "chan" + i;
            if (audioFrames[i] == null) {
                audioFrames[i] = new AudioFrame(frameTag);
            }
            if (audioLevels[i] == null) {
                audioLevels[i] = new AudioLevel();
            }
            int offset = 0;
            while (offset < data[i].length - 1) {
                AudioFrame audioFrame = audioFrames[i];
                int length = data[i].length - offset;
                int bytesWritten = audioFrame.UpdateFrameChunk(SystemClock.uptimeMillis(),
                        data[i], offset, length,
                        audioFormat.sampleRate, (short) 1);
                if (audioFrames[i].available() <= 0) {
                    audioLevels[i].ComputeLevel(audioFrames[i], audioFrames[i].duration());
                    audioFrames[i] = new AudioFrame(frameTag);
                }
                offset += bytesWritten;
            }
        }
    }

    public synchronized double getAudioEnergy() {
        double energy = 0;
        if (audioLevels != null) {
            for (int i = 0; i < audioLevels.length; i++) {
                AudioLevel level = audioLevels[i];
                energy += level.TotalEnergy();
            }
            return energy / audioLevels.length;
        }
        return energy;
    }

    public synchronized double getDuration() {
        double duration = 0;
        if (audioLevels != null) {
            for (int i = 0; i < audioLevels.length; i++) {
                AudioLevel level = audioLevels[i];
                duration += level.TotalDuration();
            }
            return duration / audioLevels.length;
        }
        return duration;
    }
}
