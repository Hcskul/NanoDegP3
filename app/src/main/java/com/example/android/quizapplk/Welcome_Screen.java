package com.example.android.quizapplk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizapplk.helper.CheckNetworkStatus;

public class Welcome_Screen extends AppCompatActivity {
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
                    Intent i = new Intent(getApplicationContext(),
                            AddMovieActivity.class);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(Welcome_Screen.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
