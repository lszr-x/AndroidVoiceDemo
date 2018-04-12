package com.example.lszr.androidvoicedemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

/**
 * @author：lszr on 2018/3/29 19:45
 * @email：1085963811@qq.com
 */
public class playMusicActivity extends AppCompatActivity implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener {

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private MediaPlayer mMediaPlayerNetwork = new MediaPlayer();
    private Button mBtnStart;
    private Button mBtnSuspend;
    private Button mBtnStop;
    private Button mBtnNetworkStart;
    private Button mBtnNetworkPause;
    private Button mBtnNetworkStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_music_activity);
        //绑定控件
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnSuspend = (Button) findViewById(R.id.btn_suspend);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnNetworkStart = (Button) findViewById(R.id.btn_network_start);
        mBtnNetworkPause = (Button) findViewById(R.id.btn_network_suspend);
        mBtnNetworkStop = (Button) findViewById(R.id.btn_network_stop);
        playLocalMusicListener();
        playNetworkMusicListener();
        if (ContextCompat.checkSelfPermission(playMusicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            initPlay();
            initNetworkPlay();
        } else {
            ActivityCompat.requestPermissions(playMusicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


    }

    /**
     * 初始化本地播放
     */
    private void initPlay() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");

            mMediaPlayer.setDataSource(file.getPath());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化网络音频播放
     */
    public void initNetworkPlay() {
        mMediaPlayerNetwork.setOnCompletionListener(this);
        mMediaPlayerNetwork.setOnErrorListener(this);
        mMediaPlayerNetwork.setOnBufferingUpdateListener(this);
        mMediaPlayerNetwork.setOnPreparedListener(this);
        Uri mUri = Uri.parse("http://music.163.com/song/media/outer/url?id=317151.mp3");
        try {
            mMediaPlayerNetwork.setDataSource(this, mUri);
            mMediaPlayerNetwork.prepareAsync();
            mBtnNetworkStart.setEnabled(false);
            mBtnNetworkPause.setEnabled(false);
            mBtnNetworkStop.setEnabled(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 启动Activity
     *
     * @param context
     */
    public static void startPlayMusicActivity(Context context) {
        Intent intent = new Intent(context, playMusicActivity.class);
        context.startActivity(intent);
    }

    /**
     * 绑定播放本地音频点击监听
     */
    public void playLocalMusicListener() {
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
            }
        });
        mBtnSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.pause();
            }
        });
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 绑定播放网络音频监听
     */
    public void playNetworkMusicListener() {
        mBtnNetworkStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayerNetwork.start();
                mBtnNetworkPause.setEnabled(true);
                mBtnNetworkStop.setEnabled(true);
            }

        });

        mBtnNetworkPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayerNetwork.isPlaying()) {
                    mBtnNetworkPause.setEnabled(false);

                    mMediaPlayerNetwork.pause();
                }
            }
        });

        mBtnNetworkStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayerNetwork.stop();
                mBtnNetworkStart.setEnabled(false);
                mBtnNetworkPause.setEnabled(false);
                mBtnNetworkStop.setEnabled(false);
                mMediaPlayerNetwork.prepareAsync();

            }
        });
    }

    /**
     * 销毁对象
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    /**
     * 播放完音频后调用的方法
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mBtnNetworkStart.setEnabled(false);
        mBtnNetworkPause.setEnabled(false);
        mBtnNetworkStop.setEnabled(false);
        mMediaPlayerNetwork.prepareAsync();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        return false;
    }

    /**
     * 缓冲中调用的方法
     *
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }


    /**
     * 播放远程音频时缓存完成后的操作
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mBtnNetworkStart.setEnabled(true);
        mBtnNetworkPause.setEnabled(false);
        mBtnNetworkStop.setEnabled(false);

    }
}
