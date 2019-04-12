package com.satoshi.coloranalysisgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class ChooseColorActivity extends AppCompatActivity implements View.OnClickListener{

    MyCountDownTimer myCountDownTimer;
    GameStartCountDownTimer gameStartCountDownTimer;
    long countMillis=0;
    boolean timeIsPaused = false;
    Toast t;

    ProgressBar progressBarArray[] = new ProgressBar[3];
    ProgressBar timer_pb;
    ColorView[] colorViewArray = new ColorView[3];
    TextView number_tv, score_tv, timer_tv, game_start_cd_tv;

    Random rnd;
    int r_correct, g_correct, b_correct;
    int correct_index; //正解の色が何番目か
    int problem_num = 0;
    final int finish_problem_num = 2;
    boolean[] scores = new boolean[finish_problem_num];

    int max_correct_answers;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);
        myCountDownTimer = new MyCountDownTimer(10*1000, 10);
        gameStartCountDownTimer = new GameStartCountDownTimer(3*1000,10);

        progressBarArray[0] = findViewById(R.id.r_progressBar);
        progressBarArray[1] = findViewById(R.id.g_progressBar);
        progressBarArray[2] = findViewById(R.id.b_progressBar);
        number_tv = findViewById(R.id.number_tv);
        score_tv = findViewById(R.id.score_tv);
        timer_tv = findViewById(R.id.timer_tv);
        game_start_cd_tv = findViewById(R.id.game_start_cd_tv);
        colorViewArray[0] = findViewById(R.id.colorView_1);
        colorViewArray[1] = findViewById(R.id.colorView_2);
        colorViewArray[2] = findViewById(R.id.colorView_3);
        timer_pb = findViewById(R.id.timer_pb);
        timer_pb.setMax(10*1000);
        for(ColorView cv : colorViewArray) cv.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        max_correct_answers = sharedPreferences.getInt(getString(R.string.sp_max_correct),0);
        score_tv.setText(max_correct_answers + "問連続正解中！");

        problem_init();
        gameStartCountDownTimer.start();
    }

    @Override
    protected void onResume(){
        super.onResume();

        //ナビゲーションバー（戻るボタン、ホームボタンとか）を隠す
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //タイマーを前回の秒数から再開するための処理
        /*if(timeIsPaused) {
            timeIsPaused = false;
            myCountDownTimer = new MyCountDownTimer(countMillis, 10);
            myCountDownTimer.start();
        } else {
            myCountDownTimer.start();
        }*/
    }

    @Override
    protected void onPause(){
        super.onPause();

        timeIsPaused = true;
        if(myCountDownTimer != null) myCountDownTimer.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.colorView_1:
                processingCorrectOrWrong(0);
                break;
            case R.id.colorView_2:
                processingCorrectOrWrong(1);
                break;
            case R.id.colorView_3:
                processingCorrectOrWrong(2);
                break;
        }
    }

    public class MyCountDownTimer extends CountDownTimer {
        private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss.SS", Locale.JAPAN);


        private MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            countMillis = l;
            if(l < 4000) timer_tv.setTextColor(Color.RED); else timer_tv.setTextColor(Color.BLACK);
            timer_tv.setText(dataFormat.format(l));
            timer_pb.setProgress((int)l);
        }

        @Override
        public void onFinish() {
            Log.d("onFinish", "タイマー動いてる");

            processingCorrectOrWrong(colorViewArray.length);
        }
    }

    public class GameStartCountDownTimer extends CountDownTimer {
        private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss.SS", Locale.JAPAN);

        private GameStartCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long l) {
            game_start_cd_tv.setText(""+(int)((l+1000)/1000));
            //String.format("%d",(int)((l+1000)/1000))
        }
        @Override
        public void onFinish() {
            View game_start_cd_view = findViewById(R.id.game_start_cd);
            ViewGroup vg = (ViewGroup) game_start_cd_view.getParent();
            vg.removeView(game_start_cd_view);
            //タイマーを前回の秒数から再開するための処理
            if(timeIsPaused) {
                timeIsPaused = false;
                myCountDownTimer = new MyCountDownTimer(countMillis, 10);
                myCountDownTimer.start();
            } else {
                myCountDownTimer.start();
            }
        }
    }

    public void setColorRandomly(ColorView colorView){
        rnd = new Random();
        r_correct = rnd.nextInt(256);
        g_correct = rnd.nextInt(256);
        b_correct = rnd.nextInt(256);
        //timer_tv.setText(r+", "+g+", "+b);
        colorView.setColor(r_correct,g_correct,b_correct);
    }

    public void problem_init(){
        for(ColorView cv : colorViewArray) setColorRandomly(cv);

        //プログレスバーに値を設定
        rnd = new Random();
        correct_index = rnd.nextInt(3);
        int[] color = colorViewArray[correct_index].getColor();
        for(int i=0; i<3; i++) progressBarArray[i].setProgress(color[i]);
        number_tv.setText("問題：" + (problem_num+1)+"/"+finish_problem_num);
    }

    public void processingCorrectOrWrong(int chosen_index){
        if(myCountDownTimer != null) myCountDownTimer.cancel();
        myCountDownTimer = null;
        String str;
        //Toast.makeText(this, Arrays.toString(colorViewArray[0].getColor()),Toast.LENGTH_SHORT).show();
        if(correct_index==chosen_index) {
            if(problem_num < finish_problem_num) scores[problem_num] = true;
            max_correct_answers++;
            str = "正解！";
        } else {
            if(problem_num < finish_problem_num) scores[problem_num] = false;
            max_correct_answers = 0;
            str = "残念！";
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.sp_max_correct),max_correct_answers);
        editor.apply();

        score_tv.setText(max_correct_answers + "問連続正解中！");
        toast(str);
        problem_num++;

        //以下、出題を続けるかどうか
        if(problem_num == finish_problem_num) {
            Intent i = new Intent(ChooseColorActivity.this, ChooseResultActivity.class);
            i.putExtra("scores", scores);
            startActivity(i);
        }
        if(problem_num < finish_problem_num) {
            problem_init();
            myCountDownTimer = new MyCountDownTimer(10 * 1000, 10);
            myCountDownTimer.start();
        }
    }

    public void toast(String message) {
        if(t != null) {
            t.cancel();
        }
        t = Toast.makeText(this, message, Toast.LENGTH_LONG);
        t.show();
    }
}
