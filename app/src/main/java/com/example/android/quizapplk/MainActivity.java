package com.example.android.quizapplk;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    /*
    Methode to hear the toccata.mp3
     */
    public void onClick(View v) {
        Button one = (Button) findViewById(R.id.playButton);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.toccata);
        one.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mp.start();
            }
        });
    }


    public void checkResults(View v) {
        int finalScore = 0;
        RadioButton right1 = (RadioButton) findViewById(R.id.quiz1Answer2);
        RadioButton right2 = (RadioButton) findViewById(R.id.quiz2Answer2);
        RadioButton right3 = (RadioButton) findViewById(R.id.quiz4Answer2);
        RadioButton right4 = (RadioButton) findViewById(R.id.quiz5Answer1);
        RadioButton right5 = (RadioButton) findViewById(R.id.quiz6Answer3);
        RadioButton right6 = (RadioButton) findViewById(R.id.quiz7Answer1);

        if (right1.isChecked()) {
            finalScore += 1;
        }
        if (right2.isChecked()) {
            finalScore += 1;
        }
        if (right3.isChecked()) {
            finalScore += 1;
        }
        if (right4.isChecked()) {
            finalScore += 1;
        }
        if (right5.isChecked()) {
            finalScore += 1;
        }
        if (right6.isChecked()) {
            finalScore += 1;
        }

        // Checks if the name input is empty
        EditText nameInput = (EditText) findViewById(R.id.nameInput);
        String name = nameInput.getText().toString();
        if (name.matches("")) {
            Toast.makeText(this, R.string.errorEmptyName, Toast.LENGTH_SHORT).show();
            return;
        }

        // Checks if the number of guests input is empty
        EditText numberGuests = (EditText) findViewById(R.id.guessGuestsChristmasmarket);
        String guestsString = numberGuests.getText().toString();

        if (guestsString.matches("")) {
            Toast.makeText(this, R.string.errorEmptyGuests, Toast.LENGTH_SHORT).show();
            return;
        } else {
            int GuestsInt = Integer.parseInt(numberGuests.getText().toString());
            if (GuestsInt > 1900000) {
                if (GuestsInt < 2200000) {
                    finalScore += 1;
                }
            }
        }

        String finalMessage = getString(R.string.final1) + Integer.toString(finalScore) + getString(R.string.final2);
        Toast.makeText(this, finalMessage, Toast.LENGTH_SHORT).show();

    }



    /*
    methode to change the behavior to check/share the results
     */

    public void shareResults(View v) {
        CheckBox statusShare = (CheckBox) findViewById(R.id.shareCheckbox);
        boolean shareCheckbox = statusShare.isChecked();

        if (shareCheckbox) {
            Button shareAndSee = (Button) findViewById(R.id.checkResults);
            shareAndSee.setText(R.string.ShareResults);
        } else {
            Button shareAndSee = (Button) findViewById(R.id.checkResults);
            shareAndSee.setText(R.string.ShowResults);
        }
    }

}
