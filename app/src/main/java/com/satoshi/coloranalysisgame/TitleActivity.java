package com.satoshi.coloranalysisgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_title);

        AdView mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Button rgb_btn = findViewById(R.id.rgb_btn);
        Button color_btn = findViewById(R.id.color_btn);
        Button practice_btn = findViewById(R.id.practice_btn);
        Button title_btn = findViewById(R.id.title_btn);
        rgb_btn.setOnClickListener(this);
        color_btn.setOnClickListener(this);
        practice_btn.setOnClickListener(this);
        title_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //ナビゲーションバー（戻るボタン、ホームボタンとか）を隠す
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rgb_btn:
                Intent intent = new Intent(TitleActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.color_btn:
                Intent intent2 = new Intent(TitleActivity.this, ChooseColorActivity.class);
                startActivity(intent2);
                break;
            case R.id.practice_btn:
                Intent intent1 = new Intent(TitleActivity.this, PracticeActivity.class);
                startActivity(intent1);
                break;
            case R.id.title_btn:
                Intent intent3 = new Intent(TitleActivity.this, StartActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent3);
                break;
        }
    }
}
