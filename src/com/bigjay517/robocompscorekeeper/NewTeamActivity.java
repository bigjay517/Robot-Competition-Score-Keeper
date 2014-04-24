package com.bigjay517.robocompscorekeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewTeamActivity extends Activity {

	 // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText inputTeam;
    EditText inputTimeMinutes;
    EditText inputTimeSeconds;
    Spinner inputTrack;
    Spinner inputTouches;
    
    // url to create new product
    private static String url_create_team = "http://localhost/robocomp/create_team.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_team);
		
		// Edit Text
        inputTeam = (EditText) findViewById(R.id.inputTeam);
        inputTimeMinutes = (EditText) findViewById(R.id.inputTimeMinutes);
        inputTimeSeconds = (EditText) findViewById(R.id.inputTimeSeconds);
 
        // Spinner
        inputTrack = (Spinner) findViewById(R.id.inputTrack);
        inputTouches = (Spinner) findViewById(R.id.inputTouches);
        
        // Create button
        Button btnCreateTeam = (Button) findViewById(R.id.btnCreateTeam);
 
        // button click event
        btnCreateTeam.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewTeam().execute();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_team, menu);
		return true;
	}
	
	/**
     * Background Async Task to Create new product
     * */
    class CreateNewTeam extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewTeamActivity.this);
            pDialog.setMessage("Creating Team..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = inputTeam.getText().toString();
            String price = inputTimeMinutes.getText().toString();
            String description = inputTimeSeconds.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("description", description));
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_team, "POST", params);
 
            // check log cat from response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), AllTeamsActivity.class);
                    startActivity(i);
 
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }

}
