package com.example.android.quizapplk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.quizapplk.helper.CheckNetworkStatus;
import com.example.android.quizapplk.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // KEYs have to match to the PHP script and to the SQL database on the server
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_POINTS = "points";
    private static final String BASE_URL = "http://192.168.0.169/quizapp/";
    public static int finalScore = 0;
    public static String test;
    public static boolean isShared = false;
    private int success;
    private ProgressDialog pDialog;

    //disable the back button
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), R.string.backPress, Toast.LENGTH_SHORT).show();
    }

    // Set two click Listeners on the Share and See Result Button when the Activity gets called
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button viewAllBtn = (Button) findViewById(R.id.playerListing);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(),
                            PlayerListingActivity.class);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(MainActivity.this, R.string.NetworkStatus, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button addNewBtn = (Button) findViewById(R.id.SendData);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for network connectivity and if was already shared
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext()) && isShared) {
                    Toast.makeText(MainActivity.this, R.string.alreadyShared, Toast.LENGTH_SHORT).show();
                }
                // sets the isShared boolean to true when it is first shared
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext()) && !isShared) {
                    new AddPlayerAsyncTask().execute();
                    isShared = true;
                }
                if (!CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    //Display error message if not connected to internet
                    Toast.makeText(MainActivity.this, R.string.NetworkStatus, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //method to check the results
    public void checkResults(View v) {
        finalScore = 0;
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

        //creates final String for the results and shows it
        String finalMessage = getResources().getString(R.string.finalString, Welcome_Screen.name, finalScore);
        Toast.makeText(this, finalMessage, Toast.LENGTH_SHORT).show();
    }

    //Methode to hear the toccata.mp3

    public void onClick(View v) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.toccata);
        mp.start();
    }

    //method to reset everything in the app
    public void resetButtons(View v) {
        isShared = false;
        Toast.makeText(this, R.string.scoreReset, Toast.LENGTH_SHORT).show();
        Intent resetIntent = new Intent(MainActivity.this, Welcome_Screen.class);
        startActivity(resetIntent);
    }

    //AsyncTask for adding a player
    private class AddPlayerAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(MainActivity.this.getString(R.string.AddPlayer));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // method to call when a new player wants to be set in the database --> calls PHP script with an static ip
        // check if the server IP address is still the same before building the app
        @Override
        protected String doInBackground(String... params) {
            String playerName = Welcome_Screen.name;
            String playerAge = Welcome_Screen.age;
            String points = String.valueOf(finalScore);

            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_NAME, playerName);
            httpParams.put(KEY_AGE, playerAge);
            httpParams.put(KEY_POINTS, points);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "add_player.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // method to call after the other tasks are done
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(MainActivity.this, R.string.AddPlayerSuccess, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.AddPlayerFailure, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}