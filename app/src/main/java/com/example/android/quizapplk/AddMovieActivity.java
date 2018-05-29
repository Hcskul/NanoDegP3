package com.example.android.quizapplk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AddMovieActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_MOVIE_NAME = "movie_name";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_YEAR = "year";
    private static final String BASE_URL = "http://192.168.0.169/movies/";
    private static String STRING_EMPTY = "";
    private EditText movieNameEditText;
    private EditText genreEditText;
    private EditText yearEditText;
    private String movieName;
    private String genre;
    private String year;
    private Button addButton;
    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__screen);
        movieNameEditText = (EditText) findViewById(R.id.nameInput);
        genreEditText = (EditText) findViewById(R.id.nameInput);
        yearEditText = (EditText) findViewById(R.id.ageInput);
        addButton = (Button) findViewById(R.id.SendData);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addMovie();
                } else {
                    Toast.makeText(AddMovieActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    /**
     * Checks whether all files are filled. If so then calls AddMovieAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void addMovie() {
        if (!STRING_EMPTY.equals(movieNameEditText.getText().toString()) &&
                !STRING_EMPTY.equals(genreEditText.getText().toString()) &&
                !STRING_EMPTY.equals(yearEditText.getText().toString())) {

            movieName = movieNameEditText.getText().toString();
            genre = genreEditText.getText().toString();
            year = yearEditText.getText().toString();

            new AddMovieAsyncTask().execute();
        } else {
            Toast.makeText(AddMovieActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }
    }

    /**
     * AsyncTask for adding a movie
     */
    private class AddMovieAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(AddMovieActivity.this);
            pDialog.setMessage("Adding Movie. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_MOVIE_NAME, movieName);
            httpParams.put(KEY_GENRE, genre);
            httpParams.put(KEY_YEAR, year);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_movie.php", "POST", httpParams);
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
                        Toast.makeText(AddMovieActivity.this,
                                "Movie Added", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        finish();

                    } else {
                        Toast.makeText(AddMovieActivity.this,
                                "Some error occurred while adding movie",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}