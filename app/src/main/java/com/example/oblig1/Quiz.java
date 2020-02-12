package com.example.oblig1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz extends BaseActivity {

    private int score;
    private int total;
    private TextView scoreText;
    private EditText input;
    private ImageView quizImage;
    private Image currentImage;
    private int currentId;
    private ImageRepository repo;

    private void initializeVariables(){
        score = 0;
        total = 0;
        scoreText = findViewById(R.id.quizScore);
        input = findViewById(R.id.userInput);
        quizImage = findViewById(R.id.quizImage);
        repo = new ImageRepository(getApplication());
        currentId = -1;
    }

    private void checkAnswer(){
        if (input.getText().toString().equalsIgnoreCase(currentImage.getName())) {
            score++;
            Toast.makeText(Quiz.this, "Correct!", Toast.LENGTH_SHORT).show();
            imageFromDb();
        } else {
            Toast.makeText(Quiz.this, "Incorrect! The correct answer is " + currentImage.getName(), Toast.LENGTH_SHORT).show();
        }
        updateScore();
    }

    private void updateScore(){
        total++;
        scoreText.setText(score + "/" + total);
        input.getText().clear();
    }

    private void exitAndToast(){
        finish();
        Toast.makeText(getApplicationContext(),"You finished with a score of " + score +" out of " + total,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        exitAndToast();
    }

    private void imageFromDb(){

        class GetImage extends AsyncTask<Void, Void, Image> {

            @Override
            protected Image doInBackground(Void... voids) {
                Image image = repo.getImageDao().getRandomImage(currentId);
                if (image == null){
                    return repo.getImageDao().getRandomImage(-1);
                }
                return image;
            }

            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);
                quizImage.setImageBitmap(image.getBitmap());
                currentImage = image;
                currentId = image.getImageId();
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        initializeVariables();
        imageFromDb();
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO || (actionId == EditorInfo.IME_ACTION_DONE) || (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    //Perform your Actions here.
                    checkAnswer();
                }
                return handled;
            }
        });
    }

}
