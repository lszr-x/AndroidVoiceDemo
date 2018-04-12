package com.example.lszr.androidvoicedemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author：lszr on 2018/4/9 21:18
 * @email：1085963811@qq.com
 */
public class AudioRecorderActivity extends AppCompatActivity {

    public static final String TAG = "AudioRecordManager";
    private AudioRecord mAudioRecord;
    private DataOutputStream mDataOutputStream;
    private Thread mRecordThread;
    private Boolean isStart = false;
    private static AudioRecorderActivity audioRecorderActivity;
    private int bufferSize;
    private File mAudioRecordFile;
    private AudioTrack mAudioTrack;


    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private String filePathOfMusic;
    private MediaRecorder mMediaRecorder = new MediaRecorder();
    private File mRecorderFile;
    private Boolean flag = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);

        Button btnStartRecord = (Button) findViewById(R.id.btn_start_recorder);
        Button btnStartPlay = (Button) findViewById(R.id.btn_start_play);
        Button btnRecorder = (Button) findViewById(R.id.btn_recoder);
        Button btnStopRecorder = (Button) findViewById(R.id.btn_stop_recorder);
        Button btnPsuseRecorder = (Button) findViewById(R.id.btn_pause_recorder);


        bufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        //参数说明：
        // 音频获取源MIC，
        // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
        // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道，
        // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持，
        // 缓冲区字节大小
        mAudioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        try {
            mAudioRecordFile = new File(
                    Environment.
                            getExternalStorageDirectory().
                            getCanonicalFile()
                            + "/projectTest/testSound.pcm");

            if(!mAudioRecordFile.exists()){
                mAudioRecordFile.createNewFile();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        //暂停MediaRecorder录音
        btnPsuseRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaRecorder.pause();
                flag = true;
            }
        });


        //停止MediaRecorder录音
        btnStopRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorderFile != null && mRecorderFile.exists()) {
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                    mMediaRecorder = null;


                }
            }
        });


        //开始MediaRecorder录音
        btnRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!flag) {

                    if (ContextCompat.checkSelfPermission(AudioRecorderActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(AudioRecorderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AudioRecorderActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);

                    } else {

                        try {
                            mRecorderFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/projectTest/sound.amr");
                            if (!mRecorderFile.exists()) {
                                mRecorderFile.createNewFile();
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mMediaRecorder.setOutputFile(mRecorderFile.getAbsoluteFile());
                        }
                        try {
                            mMediaRecorder.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaRecorder.start();
                    }
                } else {
                    mMediaRecorder.resume();
//                    mMediaRecorder.start();
                }

            }

        });


        //播放系统录音结果
        btnStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
            }
        });

        //系统录音功能（开始）
        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, 1);
            }
        });
    }


    /**
     * 回调录音完毕内容
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                try {
                    Uri uri = data.getData();
                    Log.d("test2", String.valueOf(uri));

                    String filePath = getAudioFilePathFromUri(uri);
                    filePathOfMusic = filePath;
                    mMediaPlayer.setDataSource(filePathOfMusic);
                    mMediaPlayer.prepare();
                    Log.d("test", filePath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据uri来返回对应的系统文件路径
     *
     * @param uri
     * @return
     */
    private String getAudioFilePathFromUri(Uri uri) {

        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        return cursor.getString(index);
    }


    static void startActivity(Context context) {
        Intent intent = new Intent(context, AudioRecorderActivity.class);
        context.startActivity(intent);
    }


    /**
     * 重写销毁方法
     */
    @Override
    protected void onDestroy() {
        if (mRecorderFile != null && mRecorderFile.exists()) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;


        }
        super.onDestroy();


    }
}
