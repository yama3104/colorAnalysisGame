package com.satoshi.coloranalysisgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ChooseResultActivity extends AppCompatActivity implements View.OnClickListener {

    boolean[] scores;
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_result);

        Intent intent = getIntent();
        scores = intent.getBooleanArrayExtra("scores");

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView score_tv = findViewById(R.id.score_tv);
        BootstrapButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        for(int i=0; i<scores.length; i++) {
            if(scores[i]) str += i+"  ○\n"; else str += i+"  ×\n";
        }

        score_tv.setText(str);
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
            case R.id.back_btn:
                Intent intent = new Intent(ChooseResultActivity.this, TitleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
