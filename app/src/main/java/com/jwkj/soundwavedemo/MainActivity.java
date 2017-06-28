package com.jwkj.soundwavedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jwkj.soundwave.SoundWaveManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isSuccess = SoundWaveManager.init(this);//初始化声波配置
        Log.e("hdltag", "onCreate(MainActivity.java:15):isSuccess = " + isSuccess);
    }
}
