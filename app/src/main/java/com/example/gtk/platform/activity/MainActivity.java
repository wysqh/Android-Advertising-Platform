package com.example.gtk.platform.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gtk.platform.R;
import com.example.gtk.platform.model.UserInfo;

public class MainActivity extends Activity {
    private TextView userInfoView;
    private ImageView gameView;
    private ImageView labtopView;
    private ImageView estateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.sign_up);
        gameView = (ImageView)findViewById(R.id.game_view);
        labtopView = (ImageView)findViewById(R.id.labtop_view);
        estateView = (ImageView)findViewById(R.id.estate_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        gameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        if (UserInfo.bLogin) {
            userInfoView = (TextView)findViewById(R.id.user_info);
            button.setVisibility(View.GONE);
            userInfoView.setText("您好: " + UserInfo.userName);
        }
    }
}
