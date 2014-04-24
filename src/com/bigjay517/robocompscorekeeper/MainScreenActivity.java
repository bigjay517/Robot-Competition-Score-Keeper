package com.bigjay517.robocompscorekeeper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends Activity {
	
	Button btnViewTeams;
	Button btnNewTeam;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		 
        // Buttons
        btnViewTeams = (Button) findViewById(R.id.btnViewTeams);
        btnNewTeam = (Button) findViewById(R.id.btnCreateTeam);
 
        // view products click event
        btnViewTeams.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AllTeamsActivity.class);
                startActivity(i);
 
            }
        });
 
        // view products click event
        btnNewTeam.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), NewTeamActivity.class);
                startActivity(i);
 
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}

}
