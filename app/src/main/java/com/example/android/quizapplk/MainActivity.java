package com.example.android.quizapplk;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.quizapplk.helper.CheckNetworkStatus;

public class MainActivity extends AppCompatActivity {

    private boolean share = false;

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*
    Methode to hear the toccata.mp3
     */
    public void onClick(View v) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.toccata);
        mp.start();
    }

    /*
    method to check the results
    */
    public void checkResults(View v) {
        int finalScore = 0;
        RadioButton right1 = (RadioButton) findViewById(R.id.quiz1Answer2);
        RadioButton right2 = (RadioButton) findViewById(R.id.quiz2Answer2);
        RadioButton right3 = (RadioButton) findViewById(R.id.quiz4Answer2);
        RadioButton right4 = (RadioButton) findViewById(R.id.quiz5Answer1);
        RadioButton right5 = (RadioButton) findViewById(R.id.quiz6Answer3);
        RadioButton right6 = (RadioButton) findViewById(R.id.quiz7Answer1);

        if (right1.isChecked()) {
            finalScore++;
        }
        if (right2.isChecked()) {
            finalScore++;
        }
        if (right3.isChecked()) {
            finalScore++;
        }
        if (right4.isChecked()) {
            finalScore++;
        }
        if (right5.isChecked()) {
            finalScore++;
        }
        if (right6.isChecked()) {
            finalScore++;
        }



        // Checks if the number of guests input is empty
        EditText numberGuests = (EditText) findViewById(R.id.guessGuestsChristmasmarket);
        String guestsString = numberGuests.getText().toString();
        if (guestsString.isEmpty()) {
            Toast.makeText(this, R.string.errorEmptyGuests, Toast.LENGTH_SHORT).show();
            return;
        } else {
            int guestsInt = Integer.parseInt(numberGuests.getText().toString());
            if (guestsInt > 1900000 && guestsInt < 2200000) {
                finalScore++;
            }
        }

        // Checks if the answers at question 8 are right
        CheckBox quiz8Answer1 = (CheckBox) findViewById(R.id.quiz8Answer1);
        boolean isRight1 = quiz8Answer1.isChecked();
        CheckBox quiz8Answer2 = (CheckBox) findViewById(R.id.quiz8Answer2);
        boolean isWrong = quiz8Answer2.isChecked();
        CheckBox quiz8Answer3 = (CheckBox) findViewById(R.id.quiz8Answer3);
        boolean isRight2 = quiz8Answer3.isChecked();
        if (isRight1 && !isWrong && isRight2) {
            finalScore++;
        }

        // Checks if the player wants to share his or her results
        String finalMessage = getResources().getString(R.string.finalString, Welcome_Screen.name, finalScore);

        if (share) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailSubject) + Welcome_Screen.name);
            intent.putExtra(Intent.EXTRA_TEXT, finalMessage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        } else {
            Toast.makeText(this, finalMessage, Toast.LENGTH_SHORT).show();
        }
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
            share = true;
        } else {
            Button shareAndSee = (Button) findViewById(R.id.checkResults);
            shareAndSee.setText(R.string.ShowResults);
            share = false;
        }
    }

    /*
    method to reset everything in the app
     */

    public void resetButtons(View v) {
        EditText nameInput = (EditText) findViewById(R.id.nameInput);
        nameInput.setText("");
        EditText numberGuests = (EditText) findViewById(R.id.guessGuestsChristmasmarket);
        numberGuests.setText("");

        RadioGroup firstQuestion = (RadioGroup) findViewById(R.id.radioGroupFirstQestion);
        firstQuestion.clearCheck();
        RadioGroup secondQuestion = (RadioGroup) findViewById(R.id.radioGroupSecondQestion);
        secondQuestion.clearCheck();
        RadioGroup fourthQuestion = (RadioGroup) findViewById(R.id.radioGroupFourthQestion);
        fourthQuestion.clearCheck();
        RadioGroup fifthQuestion = (RadioGroup) findViewById(R.id.radioGroupFifthQestion);
        fifthQuestion.clearCheck();
        RadioGroup sixthQuestion = (RadioGroup) findViewById(R.id.radioGroupSixthQestion);
        sixthQuestion.clearCheck();
        RadioGroup seventhQuestion = (RadioGroup) findViewById(R.id.radioGroupSeventhQestion);
        seventhQuestion.clearCheck();
        CheckBox eighthQestionAnswerOne = (CheckBox) findViewById(R.id.quiz8Answer1);
        eighthQestionAnswerOne.setChecked(false);
        CheckBox eighthQestionAnswerTwo = (CheckBox) findViewById(R.id.quiz8Answer2);
        eighthQestionAnswerTwo.setChecked(false);
        CheckBox eighthQestionAnswerThree = (CheckBox) findViewById(R.id.quiz8Answer3);
        eighthQestionAnswerThree.setChecked(false);

        CheckBox shareCheckbox = (CheckBox) findViewById(R.id.shareCheckbox);
        shareCheckbox.setChecked(false);

        Toast.makeText(this, R.string.scoreReset, Toast.LENGTH_SHORT).show();


    }

}
