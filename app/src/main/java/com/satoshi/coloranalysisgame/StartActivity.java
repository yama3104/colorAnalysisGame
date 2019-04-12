package com.satoshi.coloranalysisgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView title_tv = findViewById(R.id.title_tv);
        String str = "<font color=\"#57CA85\">色彩</font><font color=\"#292b2c\">マスター</font>";
        title_tv.setText(Html.fromHtml(str));

        TextView tap_tv = findViewById(R.id.tap_tv);
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1200);
        animation.setRepeatCount( Animation.INFINITE );
        animation.setRepeatMode( Animation.REVERSE );
        tap_tv.startAnimation(animation);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //ナビゲーションバー（戻るボタン、ホームボタンとか）を隠す
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        Intent i = new Intent(StartActivity.this, TitleActivity.class);
        startActivity(i);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
