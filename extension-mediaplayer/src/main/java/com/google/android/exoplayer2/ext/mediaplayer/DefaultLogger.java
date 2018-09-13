package com.google.android.exoplayer2.ext.mediaplayer;

import android.util.Log;

/**
 * Created by michal on 9/23/15.
 */
public class DefaultLogger implements ILogger {
    public static final String TAG = "OskPlayer";
    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @Override
    public int v(String tag, String msg) {
        return Log.v(TAG + "/" + tag, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    @Override
    public int v(String tag, String msg, Throwable tr) {
        return Log.v(TAG + "/" + tag, msg, tr);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @Override
    public int d(String tag, String msg) {
        return Log.d(TAG + "/" + tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    @Override
    public int d(String tag, String msg, Throwable tr) {
        return Log.d(TAG + "/" + tag, msg, tr);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @Override
    public int i(String tag, String msg) {
        return Log.i(TAG + "/" + tag, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    @Override
    public int i(String tag, String msg, Throwable tr) {
        return Log.i(TAG + "/" + tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @Override
    public int w(String tag, String msg) {
        return Log.w(TAG + "/" + tag, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    @Override
    public int w(String tag, String msg, Throwable tr) {
        return Log.w(TAG + "/" + tag,msg,tr);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    @Override
    public int w(String tag, Throwable tr) {
        return Log.w(TAG + "/" + tag,tr);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    @Override
    public int e(String tag, String msg) {
        return Log.e(TAG + "/" + tag,msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    @Override
    public int e(String tag, String msg, Throwable tr) {
        return Log.e(TAG + "/" + tag,msg,tr);
    }
}
