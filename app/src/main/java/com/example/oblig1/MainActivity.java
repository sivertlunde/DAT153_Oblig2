package com.example.oblig1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class MainActivity extends BaseActivity {

    private String m_Text = "";
    private ImageRepository repo;
    public static Boolean dbIsEmpty = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo = new ImageRepository(getApplication());
        checkDb();
        checkPreferences();
    }

    public MainActivity(){}

    public void startQuiz(View view){
        Intent intent = new Intent(this, Quiz.class);
        if (!dbIsEmpty) {
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "The database is empty. Add some images to start the quiz.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDatabase(View view){
        Intent intent = new Intent(this, Database.class);
        if (!dbIsEmpty) {
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "The database is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addPhoto(View view){
        Intent intent = new Intent(this, AddPhoto.class);
        startActivity(intent);
    }

    private void checkPreferences(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        Editor editor = sharedPref.edit();
//        editor.remove("Owner");
//        editor.commit();
        String owner = sharedPref.getString("Owner", null);
        if (owner == null){
            inputName(editor);
        } else {
            String checkFlag = getIntent().getStringExtra("flag");
            if (!"homebtn".equalsIgnoreCase(checkFlag)){
                Toast.makeText(MainActivity.this, "Welcome, " +owner, Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("ResourceType")
    private void inputName(Editor editor){
        final Editor e2 = editor;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Owner");
        builder.setMessage("Please enter your name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setId(999);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                e2.putString("Owner", m_Text);
                e2.commit();
            }
        });
        builder.show();
    }

    public void checkDb(){

        class GetImage extends AsyncTask<Void, Void, Image> {

            @Override
            protected Image doInBackground(Void... voids) {
                Image image = repo.getImageDao().getRandomImage(-1);
                return image;
            }

            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);
                if (image == null){
                    dbIsEmpty = true;
                } else {
                    dbIsEmpty = false;
                }
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }


}
