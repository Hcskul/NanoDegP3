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
    }
}
