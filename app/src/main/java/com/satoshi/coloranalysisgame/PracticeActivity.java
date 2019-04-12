package com.satoshi.coloranalysisgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.xw.repo.BubbleSeekBar;

import java.util.Random;

public class PracticeActivity extends AppCompatActivity implements ColorView.OnClickListener{
    ColorView correct_colorView, user_colorView;

    TextView correct_rgb_tv, user_rgb_tv, score_tv;
    //SeekBar[] rgb_seekBars = new SeekBar[3];
    BubbleSeekBar[] rgb_bseekBars = new BubbleSeekBar[3];
    //final int DRAW_MODE = 2;
    Random rnd;

    int r_correct, g_correct, b_correct;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        correct_colorView = findViewById(R.id.correct_colorView);
        user_colorView = findViewById(R.id.user_colorView);

        Button change_btn = findViewById(R.id.change_btn);
        Button check_btn = findViewById(R.id.check_btn);
        rgb_bseekBars[0] = findViewById(R.id.r_bseekBar);
        rgb_bseekBars[1] = findViewById(R.id.g_bseekBar);
        rgb_bseekBars[2] = findViewById(R.id.b_bseekBar);
        change_btn.setOnClickListener(this);
        check_btn.setOnClickListener(this);
        correct_rgb_tv = findViewById(R.id.correct_rgb_tv);
        user_rgb_tv = findViewById(R.id.user_rgb_tv);
        score_tv = findViewById(R.id.score_tv);

        //colorView.setMode(DRAW_MODE);
        setColorRandomly();

        for(int ii=0; ii<3; ii++) {
            rgb_bseekBars[ii].setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                    super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
                    if(bubbleSeekBar.equals(rgb_bseekBars[0])) {
                        user_colorView.setRed(progress);
                        user_rgb_tv.setText("("+progress+", "+rgb_bseekBars[1].getProgress()+", "+rgb_bseekBars[2].getProgress()+")");
                        calcScore();
                    }
                    if(bubbleSeekBar.equals(rgb_bseekBars[1])) {
                        user_colorView.setGreen(progress);
                        user_rgb_tv.setText("("+rgb_bseekBars[0].getProgress()+", "+progress+", "+rgb_bseekBars[2].getProgress()+")");
                        calcScore();
                    }
                    if(bubbleSeekBar.equals(rgb_bseekBars[2])) {
                        user_colorView.setBlue(progress);
                        user_rgb_tv.setText("("+rgb_bseekBars[0].getProgress()+", "+rgb_bseekBars[1].getProgress()+", "+progress+")");
                        calcScore();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        //ナビゲーションバー（戻るボタン、ホームボタンとか）を隠す
        View decor = getWindow().getDecorView();
        //decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_btn:
                setColorRandomly();
                break;
            case R.id.check_btn:

                break;
        }
    }

    public void setColorRandomly(){
        rnd = new Random(System.currentTimeMillis());
        r_correct = rnd.nextInt(256);
        g_correct = rnd.nextInt(256);
        b_correct = rnd.nextInt(256);
        //timer_tv.setText(r+", "+g+", "+b);
        correct_colorView.setColor(r_correct,g_correct,b_correct);
        correct_rgb_tv.setText("("+r_correct+", "+g_correct+", "+b_correct+")");
    }

    //点数計算と色の記録
    public void calcScore(){
        int r_submit = rgb_bseekBars[0].getProgress();
        int g_submit = rgb_bseekBars[1].getProgress();
        int b_submit = rgb_bseekBars[2].getProgress();
        int distanceOfColor = (r_correct-r_submit)*(r_correct-r_submit)
                +(g_correct-g_submit)*(g_correct-g_submit)
                +(b_correct-b_submit)*(b_correct-b_submit);

        double score = Math.sqrt(distanceOfColor);

        score_tv.setText("誤差：" + String.format("%.2f", score));
    }
}
