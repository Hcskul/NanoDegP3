package com.example.android.quizapplk;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.android.quizapplk.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerListingActivity extends AppCompatActivity {
    // KEYs have to match to the PHP script and to the SQL database on the server
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_POINTS = "points";
    private static final String BASE_URL = "http://192.168.0.169/quizapp/";
    private ArrayList<HashMap<String, String>> playerList;
    private ListView playerListView;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_listing);
        playerListView = (ListView) findViewById(R.id.playerList);
        new FetchPlayersAsyncTask().execute();

    }


    // Updating parsed JSON data into ListView
    private void populatePlayerList() {
        ListAdapter adapter = new SimpleAdapter(
                PlayerListingActivity.this, playerList,
                R.layout.list_item, new String[]{KEY_NAME, KEY_AGE, KEY_POINTS},
                new int[]{R.id.playerName, R.id.playerAge, R.id.playerPoints});
        // updating listview
        playerListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Fetches the list of player from the server
    private class FetchPlayersAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PlayerListingActivity.this);
            pDialog.setMessage(PlayerListingActivity.this.getString(R.string.listResults));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        // background task for the json parsing
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "display_players.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray players;
                if (success == 1) {
                    playerList = new ArrayList<>();
                    players = jsonObject.getJSONArray(KEY_DATA);
                    //Iterates through the response and populate players list
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject playerJson = players.getJSONObject(i);
                        String playerName = playerJson.getString(KEY_NAME);
                        Integer playerAge = playerJson.getInt(KEY_AGE);
                        Integer playerPoints = playerJson.getInt(KEY_POINTS);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_NAME, playerName);
                        map.put(KEY_AGE, playerAge.toString());
                        map.put(KEY_POINTS, playerPoints.toString());
                        playerList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    populatePlayerList();
                }
            });
        }

    }

}