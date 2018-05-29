package com.example.android.quizapplk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.quizapplk.helper.CheckNetworkStatus;
import com.example.android.quizapplk.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Welcome_Screen extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_POINTS = "points";
    private static final String BASE_URL = "http://192.168.0.169/quizapp/";
    private static String STRING_EMPTY = "";
    private EditText playerNameEditText;
    private EditText ageEditText;
    private EditText scoreEditText;
    private String playerName;
    private String playerAge;
    private String points;
    private int success;
    private ProgressDialog pDialog;

    public static String name;
    public static String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__screen);

        Button quiz = (Button) findViewById(R.id.proceedingButton);
        // Set a click listener on that View
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameInput = (EditText) findViewById(R.id.nameInput);
                EditText ageInput = (EditText) findViewById(R.id.ageInput);
                name = nameInput.getText().toString();
                age = ageInput.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(Welcome_Screen.this, R.string.errorEmptyName, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (age.isEmpty()) {
                    Toast.makeText(Welcome_Screen.this, R.string.errorEmptyAge, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent numbersIntent = new Intent(Welcome_Screen.this, MainActivity.class);
                    startActivity(numbersIntent);
                }
            }
        });

        Button viewAllBtn = (Button) findViewById(R.id.TestConnection);
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
                    Toast.makeText(Welcome_Screen.this, "Unable to connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button addNewBtn = (Button) findViewById(R.id.SendData);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addPlayer();
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(Welcome_Screen.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Checks whether all files are filled. If so then calls AddPlayerAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    public void addPlayer() {
        playerNameEditText = (EditText) findViewById(R.id.nameInput);
        ageEditText = (EditText) findViewById(R.id.ageInput);
        scoreEditText = (EditText) findViewById(R.id.ageInput);

        if (!STRING_EMPTY.equals(playerNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(ageEditText.getText().toString()) &&
                !STRING_EMPTY.equals(scoreEditText.getText().toString())) {

            playerName = playerNameEditText.getText().toString();
            playerAge = ageEditText.getText().toString();
            points = scoreEditText.getText().toString();

            new AddPlayerAsyncTask().execute();
        } else {
            Toast.makeText(Welcome_Screen.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }
    }

    /**
     * AsyncTask for adding a player
     */
    private class AddPlayerAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(Welcome_Screen.this);
            pDialog.setMessage("Adding Player. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
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

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(Welcome_Screen.this,
                                "Player Added", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about player update
                        setResult(20, i);

                    } else {
                        Toast.makeText(Welcome_Screen.this,
                                "Some error occurred while adding the player",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}
