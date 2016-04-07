package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateSameStatusActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button submitButton;

    EditText commendEdittext;

    String managerNotAvail,comeTommorow,commend;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    NetworkConnection networkConnection;

    String venueNam;

    String specialStatus1,specialStatus2,specialStatus3,specialStatus4,specialStatus5;

    String venueName, visitId ,venueId, userId ,scheduleVenueStatus;

    double venue_latitude=0.0,venue_longitude=0.0;

    String postVisitStatus,venueDate;

    SharedPreferences sharedpreferences;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    double lati,longi;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_same_status);

        commendEdittext = (EditText)findViewById(R.id.samecomment_edittext);

        submitButton = (Button)findViewById(R.id.same_status_submit);

        back = (ImageView)findViewById(R.id.sameback);


        networkConnection = new NetworkConnection(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            venueName = extras.getString("venueName");
            visitId = extras.getString("visitId");
            venueId = extras.getString("venueId");

            venue_latitude = extras.getDouble("venue_latitude");
            venue_longitude = extras.getDouble("venue_longitude");

            userId = extras.getString("userId");

            postVisitStatus = extras.getString("venueStatus");
            venueDate = extras.getString("venueDate");

            specialStatus1 = extras.getString("specialStatus1");
            specialStatus2 = extras.getString("specialStatus2");
            specialStatus3 = extras.getString("specialStatus3");
            specialStatus4 = extras.getString("specialStatus4");
            specialStatus5 = extras.getString("specialStatus5");

            Log.e("venueName--",venueName);

            Log.e("visitId--",visitId);
            Log.e("venueId--",venueId);
            Log.e("postVisitStatus--",postVisitStatus);
            Log.e("venueName--",venueName);
            Log.e("venueName--",venueName);

        }

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UpdateSameStatusActivity.this, ScheduledVenueActivity.class);
                // intent.putExtra("flag",0);
                startActivity(intent);
                finish();

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commend = commendEdittext.getText().toString();

                commend = commend.replaceAll("[^a-zA-Z0-9]", " ");

                Log.e("commend",commend);

                new createVenue().execute();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }



    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            // btnStartLocationUpdates
            //        .setText(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            //    Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
            //     btnStartLocationUpdates
            //             .setText(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            //   Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("TAG", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        // Toast.makeText(getApplicationContext(), "Location changed!",
        //         Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }



    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            lati = mLastLocation.getLatitude();
            longi = mLastLocation.getLongitude();

            Log.e("loc lat--",lati+"");
            Log.e("loc lon--",longi+"");

            //  lblLocation.setText(latitude + ", " + longitude);

        } else {

            // lblLocation
            //         .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    class createVenue extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pDialog = new ProgressDialog(UpdateSameStatusActivity.this);
            pDialog.setMessage("Update Details...");

            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            if (networkConnection.isNetworkAvailable()) {
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("venueNam", venueName));
                params.add(new BasicNameValuePair("commend", commend));
                params.add(new BasicNameValuePair("visit_id", visitId));
                params.add(new BasicNameValuePair("venue_id", venueId));
                params.add(new BasicNameValuePair("user_id", userId));
                params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));
                // params.add(new BasicNameValuePair("last_visit", currentDateandTime));
                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_no_update,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdateSameStatusActivity.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(JSONObject json) {
            // dismiss the dialog once done
            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                pDialog = null;
            }


            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {

                    Intent intent = new Intent(UpdateSameStatusActivity.this, ScheduledVenueActivity.class);
                    startActivity(intent);
                    finish();
//                    Toast.makeText(UpdateSameStatusActivity.this,
//                            "GPS Location is Updated for "+venueName,
//                            Toast.LENGTH_SHORT).show();



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    }

}
