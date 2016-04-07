package com.purpleknot1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.purpleknot1.Util.NetworkConnection;

public class AdminDashBoardActivity extends Activity {

    Button userDetailsButton,scheduleVenueButton;


    NetworkConnection networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        userDetailsButton=(Button)findViewById(R.id.display_user_button);
        scheduleVenueButton=(Button)findViewById(R.id.schedule_venue_button);


        networkConnection = new NetworkConnection(this);


        userDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashBoardActivity.this, UserListActivity.class));
                finish();

            }
        });


        scheduleVenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdminDashBoardActivity.this, ScheduleVenueActivity.class));
                finish();


            }
        });


    }

}
