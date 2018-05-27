package com.example.android.quizapplk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizapplk.helper.CheckNetworkStatus;

public class Welcome_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__screen);

        // Find the View that shows the numbers category
        TextView numbers = (TextView) findViewById(R.id.proceedingButton);

        // Set a click listener on that View
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(Welcome_Screen.this, MainActivity.class);
                startActivity(numbersIntent);
            }
        });

        Button viewAllBtn = (Button) findViewById(R.id.TestConnection);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(),
                            MovieListingActivity.class);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(Welcome_Screen.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

        Button addNewBtn = (Button) findViewById(R.id.SendData);

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(),
                            AddMovieActivity.class);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(Welcome_Screen.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}
