package com.example.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    final private static String PREFS_NAME = "PreferencesFile";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        boolean havelist = preferences.getBoolean("usermadealist",false);
        Intent intent =  new Intent(SplashActivity.this, MainActivity.class);
        if(!havelist){
            intent = new Intent(SplashActivity.this, MainMenuActivity.class);
        }
        new Thread(new StartActivity(intent)).start();
    }
    private class StartActivity implements Runnable{// to be able to sleep on ui thread
        Intent intent;
        StartActivity(Intent intent){
            this.intent = intent;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(3000);//sleep/wait to show logo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
