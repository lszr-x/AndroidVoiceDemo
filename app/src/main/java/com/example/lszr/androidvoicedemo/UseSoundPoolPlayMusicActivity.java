package com.example.lszr.androidvoicedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

/**
 * @author：lszr on 2018/4/8 21:47
 * @email：1085963811@qq.com
 */
public class UseSoundPoolPlayMusicActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3;

    //创建一个SoundPool对象

    private SoundPool.Builder mSoundPool;

    private SoundPool sp;

    //定义一个HashMap用于存放音频流的ID

    HashMap<Integer, Integer> musicId = new HashMap<Integer, Integer>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_use_sound_pool);

        btn1 = (Button) findViewById(R.id.btn_1);

        btn2 = (Button) findViewById(R.id.btn_2);

        btn3 = (Button) findViewById(R.id.btn_3);

        //初始化soundPool,设置可容纳12个音频流，音频流的质量为5，

        mSoundPool = new SoundPool.Builder();
        mSoundPool.setMaxStreams(4);


        sp=mSoundPool.build();




        //通过load方法加载指定音频流，并将返回的音频ID放入musicId中

        musicId.put(1, sp.load(this, R.raw.first, 0));

        musicId.put(2, sp.load(this, R.raw.second, 0));

        musicId.put(3, sp.load(this, R.raw.third, 0));



        View.OnClickListener listener=new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.btn_1:

                        //播放指定的音频流

                        sp.play(musicId.get(1), 1, 1, 1, 1, 1);

                        break;

                    case R.id.btn_2:

                        sp.play(musicId.get(2), 1, 1, 0, 1, 1);

                        break;

                    case R.id.btn_3:

                        sp.play(musicId.get(3), 1, 1, 0, 1, 1);

                        break;

                    default:

                        break;
                }
            }



        };

        btn1.setOnClickListener(listener);

        btn2.setOnClickListener(listener);

        btn3.setOnClickListener(listener);

    }

    static void startActivity(Context context){
        Intent intent=new Intent(context,UseSoundPoolPlayMusicActivity.class);
        context.startActivity(intent);
    }


}
