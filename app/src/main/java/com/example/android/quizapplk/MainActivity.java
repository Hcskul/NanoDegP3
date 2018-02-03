package com.example.android.quizapplk;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onClick(View v) {
        Button one = (Button) this.findViewById(R.id.playButton);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.toccata);
        one.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                mp.start();
            }
        });

        mp.start();
    }


}
