package com.satoshi.coloranalysisgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnSuccessListener;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_LEADERBOARD_UI = 9004;
    final int RC_SIGN_IN = 1773;
    GoogleSignInAccount mSignedInAccount = null;
    private String mPlayerId;

    Spinner rgb_spn, choose_spn;
    String[] rgb_spinnerItems = new String[19]; //2~20
    String[] choose_spinnerItems = new String[26]; //5~30

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button choose_showLB_btn = findViewById(R.id.choose_showLB_btn);
        choose_showLB_btn.setOnClickListener(this);

        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        for(int i=0; i<19; i++) rgb_spinnerItems[i] = ""+(i+2);
        for(int i=0; i<26; i++) choose_spinnerItems[i] = ""+(i+5);

        rgb_spn = findViewById(R.id.rgb_spn);
        ArrayAdapter<String> adapter_rgb = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rgb_spinnerItems);
        adapter_rgb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SharedPreferences sharedPreferences = getSharedPreferences("SettingData", Context.MODE_PRIVATE);
        rgb_spn.setAdapter(adapter_rgb);
        rgb_spn.setSelection(sharedPreferences.getInt(getString(R.string.sp_rgb_problemnumber), 7)-2);
        rgb_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = getSharedPreferences("SettingData", Context.MODE_PRIVATE).edit();
                editor.putInt(getString(R.string.sp_rgb_problemnumber), i+2);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        choose_spn = findViewById(R.id.choose_spn);
        ArrayAdapter<String> adapter_choose = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choose_spinnerItems);
        adapter_choose.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose_spn.setAdapter(adapter_choose);
        choose_spn.setSelection(sharedPreferences.getInt(getString(R.string.sp_choose_problemnumber), 15)-5);
        choose_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = getSharedPreferences("SettingData", Context.MODE_PRIVATE).edit();
                editor.putInt(getString(R.string.sp_choose_problemnumber), i+5);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_showLB_btn:
                if(!showchooseLeaderboard()) startSignInIntent();
                break;
        }
    }

    private boolean showchooseLeaderboard() {
        if(GoogleSignIn.getLastSignedInAccount(this) == null) {
            return false;
        } else {
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .getAllLeaderboardsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
            return true;
        }
    }

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
                onConnected(signedInAccount);
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = "LogIn Failed...";
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d("", "onConnected(): connected to Google APIs");
        if (mSignedInAccount != googleSignInAccount) {

            mSignedInAccount = googleSignInAccount;
            //mLeaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);

            // get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();
                            //switchToMainScreen();
                        }
                    });
            //.addOnFailureListener(createFailureListener("There was a problem getting the player id!"));

            Log.d("StartActivity", "ConnectionSucces!!!");
        }
    }
}
