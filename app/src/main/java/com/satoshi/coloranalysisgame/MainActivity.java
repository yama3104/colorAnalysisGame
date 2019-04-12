package com.satoshi.coloranalysisgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.api.view.BootstrapTextView;
import com.xw.repo.BubbleSeekBar;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ColorView colorView;
    MyCountDownTimer myCountDownTimer;
    GameStartCountDownTimer gameStartCountDownTimer;
    long countMillis=0;
    boolean timeIsPaused = false;
    int dispWidth, dispHeight;

    ProgressBar timer_pb;
    TextView number_tv, score_tv, game_start_cd_tv;
    SeekBar[] rgb_seekBars = new SeekBar[3];
    BubbleSeekBar[] rgb_bseekBars = new BubbleSeekBar[3];
    Random rnd;

    //final int DRAW_MODE = 1;
    int r_correct, g_correct, b_correct, r_submit, g_submit, b_submit;
    int problem_num = 0;
    final int finish_problem_num = 10;
    double[] scores = new double[finish_problem_num];
    int[][] correct_colors = new int[finish_problem_num][3];
    int[][] user_colors = new int[finish_problem_num][3];

    //コンストラクタ
    //public MainActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();
        colorView = findViewById(R.id.colorView);
        myCountDownTimer = new MyCountDownTimer(10*1000, 10);
        gameStartCountDownTimer = new GameStartCountDownTimer(3*1000,10);

        dispWidth = getDispSize(this).x;
        dispHeight = getDispSize(this).y;

        rgb_bseekBars[0] = findViewById(R.id.r_bseekBar);
        rgb_bseekBars[1] = findViewById(R.id.g_bseekBar);
        rgb_bseekBars[2] = findViewById(R.id.b_bseekBar);
        rgb_seekBars[0] = findViewById(R.id.r_seekBar);
        rgb_seekBars[1] = findViewById(R.id.g_seekBar);
        rgb_seekBars[2] = findViewById(R.id.b_seekBar);
        number_tv = findViewById(R.id.number_tv);
        score_tv = findViewById(R.id.score_tv);
        game_start_cd_tv = findViewById(R.id.game_start_cd_tv);
        timer_pb = findViewById(R.id.timer_pb);
        timer_pb.setMax(10*1000);

        //colorView.setColor(r,g,b);

        for(int ii=0; ii<3; ii++) {
            rgb_bseekBars[ii].setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                    super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
                }
            });
        }

        /*for(int i=0; i<3; i++) {
            rgb_seekBars[i].setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                            if(seekBar.equals(rgb_seekBars[0])) r_progress_tv.setText("R:"+progress);
                            if(seekBar.equals(rgb_seekBars[1])) g_progress_tv.setText("G:"+progress);
                            if(seekBar.equals(rgb_seekBars[2])) b_progress_tv.setText("B:"+progress);

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
        }*/

        //colorView.setMode(DRAW_MODE);
        setColorRandomly();
        //int problem_num_1 = problem_num+1;
        number_tv.setText("No." + (problem_num+1));
        //myCountDownTimer.start();
    }

    @Override
    protected void onResume(){
        super.onResume();

        //ナビゲーションバー（戻るボタン、ホームボタンとか）を隠す
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        gameStartCountDownTimer.start();
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
            case R.id.change_btn:
                Log.d("button", "button");
                setColorRandomly();
                break;

            case R.id.submit_btn:
                calcScore();
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
            timer_pb.setProgress((int)l);
        }
        @Override
        public void onFinish() {
            Log.d("onFinish", "タイマー動いてる");

            if(myCountDownTimer != null) myCountDownTimer.cancel();
            myCountDownTimer = null;
            calcScore();
            problem_num++;
            if(problem_num == finish_problem_num) {
                Intent i = new Intent(MainActivity.this, MainResultActivity.class);
                Bundle bundle1 = new Bundle();
                Bundle bundle2 = new Bundle();
                bundle1.putSerializable("correct_colors",correct_colors);
                bundle2.putSerializable("user_colors",user_colors);
                i.putExtra("scores",scores);
                i.putExtras(bundle1);
                i.putExtras(bundle2);
                startActivity(i);
            } else {
                setColorRandomly();
                number_tv.setText("No." + (problem_num+1));
                myCountDownTimer = new MyCountDownTimer(10 * 1000, 10);
                myCountDownTimer.start();
            }
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

    public Point getDispSize(Context context){
        WindowManager winMan = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if(winMan == null) return new Point(0,0);
        Display disp = winMan.getDefaultDisplay();

        Point point = new Point();
        disp.getSize(point);

        return point;
    }

    public void setColorRandomly(){
        rnd = new Random(System.currentTimeMillis());
        r_correct = rnd.nextInt(256);
        g_correct = rnd.nextInt(256);
        b_correct = rnd.nextInt(256);
        //timer_tv.setText(r+", "+g+", "+b);
        colorView.setColor(r_correct,g_correct,b_correct);
    }

    //点数計算と色の記録
    public void calcScore(){
        r_submit = rgb_bseekBars[0].getProgress();
        g_submit = rgb_bseekBars[1].getProgress();
        b_submit = rgb_bseekBars[2].getProgress();
        int distanceOfColor = (r_correct-r_submit)*(r_correct-r_submit)
                +(g_correct-g_submit)*(g_correct-g_submit)
                +(b_correct-b_submit)*(b_correct-b_submit);
        int MAX_SCORE = 255*255*3;
        //int score = (distanceOfColor==0)? MAX_SCORE : MAX_SCORE/distanceOfColor;
        double score = Math.sqrt(distanceOfColor);
        if(problem_num < finish_problem_num) {
            scores[problem_num] = score;
            correct_colors[problem_num][0] = r_correct;
            correct_colors[problem_num][1] = g_correct;
            correct_colors[problem_num][2] = b_correct;
            user_colors[problem_num][0] = r_submit;
            user_colors[problem_num][1] = g_submit;
            user_colors[problem_num][2] = b_submit;
        }
        score_tv.setText("誤差：" + String.format("%.2f", score));
    }

}
