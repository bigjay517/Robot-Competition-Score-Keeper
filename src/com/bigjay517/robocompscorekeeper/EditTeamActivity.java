package com.bigjay517.robocompscorekeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditTeamActivity extends Activity {

	JSONObject teamdata;

	EditText textTimeMinutes;
	EditText textTimeSeconds;

	TextView textTeamNumber;
	TextView textTrackScore;

	Spinner trackSpinner;
	Spinner touchesSpinner;

	Button buttonSave;
	Button buttonDelete;

	String id;

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	private static final String url_team_details = "http://10.0.2.2/robocomp/get_team_details.php";

	private static final String url_update_team = "http://10.0.2.2/robocomp/update_team.php";

	private static final String url_delete_team = "http://10.0.2.2/robocomp/delete_team.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ROBOCOMP = "teams";
	private static final String TAG_ID = "id";
	private static final String TAG_TIME = "time";
	private static final String TAG_TEAM = "team";
	private static final String TAG_TRACK = "track";
	private static final String TAG_SCORE = "score";
	private static final String TAG_TOUCHES = "touches";
	private static final String TAG_YELLOW_TRACK_TIME = "yellow_track_time";
	private static final String TAG_YELLOW_TRACK_TOUCHES = "yellow_track_touches";
	private static final String TAG_YELLOW_TRACK_SCORE = "yellow_track_score";
	private static final String TAG_BLUE_TRACK_TIME = "blue_track_time";
	private static final String TAG_BLUE_TRACK_TOUCHES = "blue_track_touches";
	private static final String TAG_BLUE_TRACK_SCORE = "blue_track_score";

	int TESTINT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_team);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// Edit Text
		textTeamNumber = (TextView) findViewById(R.id.teamNumberTextView);
		textTimeSeconds = (EditText) findViewById(R.id.timeSecondsEditText);
		textTrackScore = (TextView) findViewById(R.id.trackScoreTextView);
		textTimeMinutes = (EditText) findViewById(R.id.timeMinutesEditText);

		// save button
		buttonSave = (Button) findViewById(R.id.saveTeamButton);
		buttonDelete = (Button) findViewById(R.id.deleteTeamButton);

		touchesSpinner = (Spinner) findViewById(R.id.touchesSpinner);
		trackSpinner = (Spinner) findViewById(R.id.trackSelectSpinner);

		// getting team details from intent
		Intent i = getIntent();

		// getting team id (pid) from intent
		id = i.getStringExtra(TAG_ID);

		// Getting complete team details in background thread
		new GetTeamDetails().execute();

		// save button click event
		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update team
				new SaveTeamDetails().execute();
			}
		});

		// Delete button click event
		buttonDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// deleting team in background thread
				new DeleteTeam().execute();
			}
		});
		
		touchesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				int penalty;
				int timeSeconds = Integer.parseInt(textTimeSeconds.getText().toString());
				int timeMinutes = Integer.parseInt(textTimeMinutes.getText().toString());
				switch (position) {
				case 0:
					penalty = 0;
					break;
				case 1:
					penalty = 2;
					break;
				case 2:
					penalty = 7;
					break;
				case 3:
					penalty = 12;
					break;
				default:
					penalty = 27;
					break;
				}
				int time = timeSeconds+timeMinutes*60;
				int score = time + penalty;
				textTrackScore.setText(Integer.toString(score));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		trackSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					showYellowTrackData();
					break;
				case 1:
					showBlueTrackData();
					break;
				default:
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void showYellowTrackData() {
		// TODO Auto-generated method stub
		try {
			int totalTime = Integer.parseInt(teamdata.getString(TAG_YELLOW_TRACK_TIME));
			int timeMinutes = totalTime/60;
			int timeSeconds = totalTime-(timeMinutes*60);
			textTimeSeconds.setText(Integer.toString(timeSeconds));
			textTimeMinutes.setText(Integer.toString(timeMinutes));
			textTrackScore.setText(teamdata.getString(TAG_YELLOW_TRACK_SCORE));
			touchesSpinner.setSelection(Integer.parseInt(teamdata.getString(TAG_YELLOW_TRACK_TOUCHES)));
			textTeamNumber.setText(teamdata.getString(TAG_TEAM));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showBlueTrackData() {
		
		try {
			int totalTime = Integer.parseInt(teamdata.getString(TAG_BLUE_TRACK_TIME));
			int timeMinutes = totalTime/60;
			int timeSeconds = totalTime-(timeMinutes*60);
			textTimeSeconds.setText(Integer.toString(timeSeconds));
			textTimeMinutes.setText(Integer.toString(timeMinutes));
			textTimeSeconds.setText(teamdata.getString(TAG_BLUE_TRACK_TIME));
			textTrackScore.setText(teamdata.getString(TAG_BLUE_TRACK_SCORE));
			touchesSpinner.setSelection(Integer.parseInt(teamdata.getString(TAG_BLUE_TRACK_TOUCHES)));
			textTeamNumber.setText(teamdata.getString(TAG_TEAM));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_team, menu);
		return true;
	}

	/**
	 * Background Async Task to Get complete team details
	 * */
	class GetTeamDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditTeamActivity.this);
			pDialog.setMessage("Loading team details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... arg0) {

			// updating UI from Background Thread
			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(TAG_ID, id));

				// getting team details by making HTTP request
				// Note that team details url will use GET request
				JSONObject json = jsonParser.makeHttpRequest(url_team_details,
						"GET", params);

				// check your log for json response
				Log.d("Single Product Details", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// successfully received team details
					JSONArray teamObj = json.getJSONArray(TAG_TEAM); // JSON

					// get first team object from JSON Array
					teamdata = teamObj.getJSONObject(0);

					// team with this id found
				} else {
					// team with id not found
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
			// display product data in EditText
			showYellowTrackData();
			// dismiss the dialog once got all details
			pDialog.dismiss();
		}
	}

	/**
	 * Background Async Task to Save product Details
	 * */
	class SaveTeamDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditTeamActivity.this);
			pDialog.setMessage("Saving team ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {

			// getting updated data from EditTexts
			int timeMinutes = Integer.parseInt(textTimeMinutes.getText().toString());
			int timeSeconds = Integer.parseInt(textTimeSeconds.getText().toString());
			int touches = touchesSpinner.getSelectedItemPosition();
			int track = trackSpinner.getSelectedItemPosition();
			int penalty;

			// Determine Penalty
			switch (touches) {
			case 0:
				penalty = 0;
				break;
			case 1:
				penalty = 2;
				break;
			case 2:
				penalty = 7;
				break;
			case 3:
				penalty = 12;
				break;
			default:
				penalty = 27;
				break;
			}

			// Create score and time
			int time = timeSeconds+timeMinutes*60;
			int score = time + penalty;

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_TIME, Integer.toString(time)));
			params.add(new BasicNameValuePair(TAG_TRACK, Integer.toString(track)));
			params.add(new BasicNameValuePair(TAG_SCORE, Integer.toString(score)));
			params.add(new BasicNameValuePair(TAG_TOUCHES, Integer.toString(touches)));
			params.add(new BasicNameValuePair(TAG_ID, id));

			// sending modified data through http request
			// Notice that update product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_team, "POST", params);

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully updated
					Intent i = getIntent();
					// send result code 100 to notify about team update
					setResult(100, i);
					finish();
				} else {
					// failed to update team
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
			// dismiss the dialog once team updated
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task to Delete Product
	 * */
	class DeleteTeam extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditTeamActivity.this);
			pDialog.setMessage("Deleting Team...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting product
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", id));

				// getting team details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(url_delete_team, "POST", params);

				// check your log for json response
				Log.d("Delete Product", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// team successfully deleted
					// notify previous activity by sending code 100
					Intent i = getIntent();
					// send result code 100 to notify about team deletion
					setResult(100, i);
					finish();
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
			// dismiss the dialog once team deleted
			pDialog.dismiss();

		}

	}

}
