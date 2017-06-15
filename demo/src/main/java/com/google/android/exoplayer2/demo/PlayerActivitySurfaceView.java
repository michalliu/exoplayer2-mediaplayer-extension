package com.google.android.exoplayer2.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ext.mediaplayer.ExoMediaPlayer;
import com.google.android.exoplayer2.ext.mediaplayer.MediaPlayerInterface;

public class PlayerActivitySurfaceView extends Activity implements
        SurfaceHolder.Callback,
        SeekBar.OnSeekBarChangeListener,
        ExoMediaPlayer.OnPreparedListener,
        ExoMediaPlayer.OnSeekCompleteListener,
        ExoMediaPlayer.OnBufferingUpdateListener,
        View.OnClickListener {

    public static final String TAG = "PlayerActivity";

    private SeekBar mSeekBar;
    private SurfaceHolder mSurfaceHolder;
    private ExoMediaPlayer mMediaPlayer;
    private ImageButton mPlayPauseButton;
    private boolean mJustCreatePlayer;
    private TextView mDebugTextView;
    private TextView mVideoDurationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_surfaceview);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.main_surface);
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mPlayPauseButton = (ImageButton) findViewById(R.id.playpausebtn);
        mPlayPauseButton.setOnClickListener(this);
        mPlayPauseButton.setEnabled(false);
        mDebugTextView = (TextView) findViewById(R.id.debugtextview);
        mVideoDurationView = (TextView) findViewById(R.id.videoDuration);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
        mPlayPauseButton.setEnabled(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged[" + width + "," + height + "]");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        destroyPlayer();    }

    @Override
    public void onPrepared(MediaPlayerInterface mp) {
        Log.d(TAG, "onPrepared");
        mMediaPlayer.start();
        mSeekBar.setMax((int) mMediaPlayer.getDuration());
        mVideoDurationView.setText(String.valueOf(mMediaPlayer.getDuration()));
        updatePlayStatus();
    }

    @Override
    public void onBufferingUpdate(MediaPlayerInterface mp, int percent) {
        Log.d(TAG, "onBufferingUpdate " + percent);
    }

    @Override
    public void onSeekComplete(MediaPlayerInterface mp) {
        Log.d(TAG, "onSeekComplete");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mDebugTextView.setText(String.valueOf(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mDebugTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress=seekBar.getProgress();
        Log.d(TAG, "seekTo " + progress);
        mMediaPlayer.seekTo(progress);
        mDebugTextView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playpausebtn) {
            if (mMediaPlayer == null) {
                initPlayer();
            }
            if (mJustCreatePlayer) {
                String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                try {
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.prepareAsync();
                    mJustCreatePlayer = false;
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
            }
            updatePlayStatus();
        }
    }

    private void initPlayer() {
        mMediaPlayer = new ExoMediaPlayer(getApplicationContext());
        mMediaPlayer.setDisplay(mSurfaceHolder);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mJustCreatePlayer = true;
    }

    private void destroyPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mJustCreatePlayer = false;
            mMediaPlayer = null;
        }
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mPlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }
}
