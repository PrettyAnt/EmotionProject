package com.example.emotionproject.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emotionproject.AppReceiveCallBack;
import com.example.emotionproject.manager.KeyBoardManager;
import com.example.emotionproject.R;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements AppReceiveCallBack {


    private ImageView iv_test;
    private TextView  tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View      v                =  findViewById(R.id.rl_content);
        iv_test = findViewById(R.id.iv_test);
        tv_test = findViewById(R.id.tv_test);
        View      bindKeyBoardView = findViewById(R.id.ll_root_keyboard);
        KeyBoardManager.with(this)
                .bindContentView(v)
                .bindKeyBoardView(bindKeyBoardView)
                .bindAppReceiveCallBack(this)
                .build();
    }
    //按下back键监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://返回键
                finish();
                return false;
            default:
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAppReceiveCallBack(String key, String value) {
        InputStream           open    = null;
        try {
            open = getAssets().open("emoji/" + key);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap                bitmap  = BitmapFactory.decodeStream(open, null, options);
            iv_test.setImageBitmap(bitmap);
            tv_test.setText(value);
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}