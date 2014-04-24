package com.bigjay517.robocompscorekeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AllTeamsActivity extends ListActivity {
	
	private ProgressDialog pDialog;
	
	JSONParser jParser = new JSONParser();
	
	ArrayList<HashMap<String, String>> teamsList;
	
	private static String url_all_teams = "http://141.215.221.140/robocomp/get_all_teams.php";
	
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ROBOCOMP = "teams";
    private static final String TAG_ID = "id";
    private static final String TAG_TEAM = "team";
    
    // products JSONArray
    JSONArray teams = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_teams);
		
        // Hashmap for ListView
        teamsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllTeams().execute();
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.id)).getText().toString();
 
                // Starting new intent
                //Intent in = new Intent(getApplicationContext(), EditTeamActivity.class);  //REMOVE
                // sending pid to next activity
                //in.putExtra(TAG_ID, id);  //REMOVE
 
                // starting new activity and expecting some response back
                //startActivityForResult(in, 100);  //REMOVE
            }
        });
	}
	
	// Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_teams, menu);
		return true;
	}
	
	/**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllTeams extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllTeamsActivity.this);
            pDialog.setMessage("Loading teams. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_teams, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    teams = json.getJSONArray(TAG_ROBOCOMP);
 
                    // looping through All Products
                    for (int i = 0; i < teams.length(); i++) {
                        JSONObject c = teams.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_TEAM);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_TEAM, name);
 
                        // adding HashList to ArrayList
                        teamsList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(), NewTeamActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllTeamsActivity.this, teamsList,
                            R.layout.list_item, new String[] { TAG_ID, TAG_TEAM},
                            new int[] { R.id.id, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}
