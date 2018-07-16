package com.google.android.exoplayer2.ext.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by leoliu on 2018/6/13.
 */

public class AudioUtil {
    public static final double PCM_EACH_MAX_16BIT = 32767 * 32767;

    //For Little Endian
    //byteToShort(audioData[j], audioData[j+1])
    public static short byteToShort(byte low, byte high) {
        return (short)(((high & 0xFF) << 8) | (low & 0xFF));
    }

    private static short byteToShort2(byte firstByte, byte secondByte) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(firstByte);
        bb.put(secondByte);
        return bb.getShort(0);
    }

    public static double mean(double[] values, int count) {
        count = Math.min(count, values.length);
        double sum = 0;
        for (int i=0; i<count; i++) {
            sum += values[i];
        }
        return sum / count;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a) {
            sb.append(String.format("%02x ", b));
        }
        return sb.toString();
    }

    public static double min(double[] values, int count) {
        count = Math.min(count, values.length);
        double min = 0;
        for (int i=0; i<count; i++) {
            min = Math.min(min, values[i]);
        }
        return min;
    }
}
