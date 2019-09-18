package com.example.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity{

    private Button bt_login;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 控件的初始化
        bt_login = (Button) findViewById(R.id.bt_login);
        setListener();
    }
    /**
     * 设置监听
     */
    private void setListener() {

        // 按钮点击事件
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
