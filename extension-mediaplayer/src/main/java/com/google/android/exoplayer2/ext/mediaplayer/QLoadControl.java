package com.google.android.exoplayer2.ext.mediaplayer;

import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultAllocator;

/**
 * Created by leoliu on 17/5/3.
 */

public class QLoadControl extends DefaultLoadControl {
    public static final String LOG_TAG = "QLoadControl";

    private static int sBufferSegmentSize = C.DEFAULT_BUFFER_SEGMENT_SIZE;
    private static int sMinBufferMs = DEFAULT_MIN_BUFFER_MS;
    private static int sMaxBufferMs = DEFAULT_MAX_BUFFER_MS;
    private static int sBufferForPlaybackMs = DEFAULT_BUFFER_FOR_PLAYBACK_MS;
    private static int sBufferForPlaybackRebufferMs = DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;

    // 流控参数配置样例
    public static final String DEFAULT_LC_SAMPLE_CONFIG = "65536,15000,30000,2000,5000";

    // 修改流控参数配置
    public static void updateConfig(Config config) {
        if (config == null) {
            Log.v(LOG_TAG, "updateConfig config null");
            return;
        }
        sBufferSegmentSize = config.bufferSegmentSize;
        sMinBufferMs = config.minBufferMs;
        sMaxBufferMs = config.maxBufferMs;
        sBufferForPlaybackMs = config.bufferForPlaybackMs;
        sBufferForPlaybackRebufferMs = config.bufferForPlaybackRebufferMs;
        Log.v(LOG_TAG, "updateConfig with config " + config);
    }

    // 修改流控参数配置，支持字符串配置
    public static void updateConfig(String configStr) {
        Log.v(LOG_TAG, "updateConfig configStr=" + configStr);
        String[] configs = configStr.split(",");
        QLoadControl.Config config = new QLoadControl.Config();
        try {
            if (configs.length > 0) {
                config.bufferSegmentSize = Integer.parseInt(configs[0]);
            }
            if (configs.length > 1) {
                config.minBufferMs = Integer.parseInt(configs[1]);
            }
            if (configs.length > 2) {
                config.maxBufferMs = Integer.parseInt(configs[2]);
            }
            if (configs.length > 3) {
                config.bufferForPlaybackMs = Integer.parseInt(configs[3]);
            }
            if (configs.length > 4) {
                config.bufferForPlaybackRebufferMs = Integer.parseInt(configs[4]);
            }
            QLoadControl.updateConfig(config);
        } catch (Exception e) {
            Log.v(LOG_TAG, "invalid qloadcontrol configStr=" + configStr);
        }

    }

    public QLoadControl() {
        super(new DefaultAllocator(true, sBufferSegmentSize),
                sMinBufferMs,
                sMaxBufferMs,
                sBufferForPlaybackMs,
                sBufferForPlaybackRebufferMs,
                C.LENGTH_UNSET,
                DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS);
    }

    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs, float playbackSpeed) {
        boolean shouldContinueLoading =  super.shouldContinueLoading(bufferedDurationUs, playbackSpeed);
//        PlayerUtils.log(QLog.DEBUG, LOG_TAG, "shouldContinueLoading " + shouldContinueLoading
//                + "[" + bufferedDurationUs + "," + playbackSpeed + "]");
        return shouldContinueLoading;
    }

    @Override
    public boolean shouldStartPlayback(long bufferedDurationUs, float playbackSpeed, boolean rebuffering) {
        boolean shouldStartPlayback = super.shouldStartPlayback(bufferedDurationUs, playbackSpeed, rebuffering);
//        PlayerUtils.log(QLog.DEBUG, LOG_TAG, "shouldStartPlayback " + shouldStartPlayback
//                + "[" + bufferedDurationUs + "," + playbackSpeed + "," + rebuffering + "]");
        return shouldStartPlayback;
    }

    @Override
    protected int calculateTargetBufferSize(Renderer[] renderers, TrackSelectionArray trackSelectionArray) {
        int targetBufferSize =  super.calculateTargetBufferSize(renderers, trackSelectionArray);
//        PlayerUtils.log(QLog.DEBUG, LOG_TAG, "targetBufferSize " + targetBufferSize);
        return targetBufferSize;
    }

    public static class Config {
        private int bufferSegmentSize = C.DEFAULT_BUFFER_SEGMENT_SIZE;
        private int minBufferMs = DEFAULT_MIN_BUFFER_MS;
        private int maxBufferMs = DEFAULT_MAX_BUFFER_MS;
        private int bufferForPlaybackMs = DEFAULT_BUFFER_FOR_PLAYBACK_MS;
        private int bufferForPlaybackRebufferMs = DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;

        @Override
        public String toString() {
            return "QLoadControl.Config[bufferSegmentSize=" + bufferSegmentSize +
                    "," + "minBufferMs=" + minBufferMs +
                    "," + "maxBufferMs=" + maxBufferMs +
                    "," + "bufferForPlaybackMs=" + bufferForPlaybackMs +
                    "," + "bufferForPlaybackRebufferMs=" + bufferForPlaybackRebufferMs
                    +"]";
        }
    }
}