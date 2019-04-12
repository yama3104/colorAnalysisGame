package com.satoshi.coloranalysisgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainResultActivity extends AppCompatActivity implements View.OnClickListener {
    double[] scores;
    int[][] correct_colors, user_colors;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);

        Intent intent = getIntent();
        scores = intent.getDoubleArrayExtra("scores");
        //Bundle bundle = new Bundle();
        correct_colors = (int[][])intent.getSerializableExtra("correct_colors");
        user_colors = (int[][])intent.getSerializableExtra("user_colors");

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TableLayout result_tLayout = findViewById(R.id.result_tableLayout);
        //TextView score_tv = findViewById(R.id.score_tv);
        BootstrapButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);


        int sum=0;
        for(int i=0; i<scores.length; i++) {

            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.main_result_layout,null);
            TextView problemNum_tv = tableRow.findViewById(R.id.row_problemnumber_tv);
            ColorView correct_cv = tableRow.findViewById(R.id.row_correct_color_cv);
            ColorView user_cv = tableRow.findViewById(R.id.row_user_color_cv);
            TextView correct_tv = tableRow.findViewById(R.id.row_correct_color_tv);
            TextView user_tv = tableRow.findViewById(R.id.row_user_color_tv);
            TextView score_tv = tableRow.findViewById(R.id.row_score_tv);

            problemNum_tv.setText(""+(i+1));
            correct_cv.setColor(correct_colors[i][0], correct_colors[i][1], correct_colors[i][2]);
            user_cv.setColor(user_colors[i][0], user_colors[i][1], user_colors[i][2]);
            correct_tv.setText(getString(R.string.rgb_string,correct_colors[i][0], correct_colors[i][1], correct_colors[i][2])+"\n正解の色");
            user_tv.setText(getString(R.string.rgb_string,user_colors[i][0], user_colors[i][1], user_colors[i][2])+"\n調整した色");
            score_tv.setText("誤差：" + String.format("%.2f", scores[i]));
            result_tLayout.addView(tableRow);
            /*sum += i;
            sb.append(i);
            sb.append("\n");*/
        }
        //score_tv.setText(sb.append("\n SUM: ").append(sum));
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
                Intent intent = new Intent(MainResultActivity.this, TitleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                //FLAG_ACTIVITY_CLEAR_TOPは、
                // 遷移先のアクティビティが既に動いていればそのアクティビティより上にある（この場合はB, C, D）アクティビティを消す、という挙動を設定する。
                // これによって、A→B→C→D→Aと遷移した後にbackボタンを押してもDに戻ることはなくなる。

                //FLAG_ACTIVITY_SINGLE_TOPは、
                // 既に動いているアクティビティに遷移するとそのアクティビティを閉じてもう一度作りなおすデフォルトの挙動（multiple mode）から、作りなおさずに再利用する挙動に変更する。
                // これによって、D→Aへの遷移のときのアニメーションが戻る動きになる。
                break;
        }
    }
}
