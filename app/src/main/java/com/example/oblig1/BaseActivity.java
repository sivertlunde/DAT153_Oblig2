package com.example.oblig1;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public void goToHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("flag", "homebtn");
        startActivity(intent);
    }

    public void goToSettings(View view){
        Intent intent = new Intent(getApplicationContext(), Preferences.class);
        startActivity(intent);
    }

}