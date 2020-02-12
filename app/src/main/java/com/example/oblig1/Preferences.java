package com.example.oblig1;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Preferences extends BaseActivity {

    TextView currentOwner;
    EditText newOwner;
    Button saveBtn;
    SharedPreferences sharedPref;
    Editor editor;

    private void initializeVariables(){
        currentOwner = findViewById(R.id.ownerString);
        newOwner = findViewById(R.id.newOwner);
        saveBtn = findViewById(R.id.savePreferences);
        sharedPref = getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        editor = sharedPref.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        initializeVariables();
        checkOwner();
    }

    private void checkOwner(){
        String owner = sharedPref.getString("Owner", null);
        if (owner != null){
            currentOwner.setText(owner.toUpperCase());
        }
    }

    public void saveOwner(View view){
        if (newOwner.getText().toString().trim().equals("")){
            Toast.makeText(Preferences.this, "Please enter a name in the field", Toast.LENGTH_LONG).show();
        } else {
            editor.putString("Owner", newOwner.getText().toString());
            editor.commit();
            currentOwner.setText(newOwner.getText().toString().toUpperCase());
        }
    }

}
