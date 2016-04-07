package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;


public class UpdateVenueDetailsActivity extends Activity  implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    LocationManager locationManager ;

    NetworkConnection networkConnection;
    String provider;
    double latitude,longitude;
    EditText venueNameEdittext;

    String venueName;

    Button captureGpsButton;

    ImageView backImageView;

    private final int requestCode = 20;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    String venueStatus;

    String venueNam;

    JSONArray venue = null;



    PopupMenu popupMenu;


  //  private static final String TAG_SUCCESS = "success";

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

    // UI elements
   // private TextView lblLocation;
    private Button btnShowLocation, btnStartLocationUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_venuedetail);

        venueNameEdittext=(EditText)findViewById(R.id.update_venue_name_edittext);

        venueNameEdittext.setFocusable(false);

        backImageView=(ImageView)findViewById(R.id.update_venue_back_imageview);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            venueNam = extras.getString("venueName");

            venueNameEdittext.setText(venueNam);

        }

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateVenueDetailsActivity.this, ScheduledVenueActivity.class);
                // intent.putExtra("flag",0);
                startActivity(intent);
                finish();
            }
        });
    }


    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(UpdateVenueDetailsActivity.this, view);
        popupMenu.inflate(R.menu.menu_schedule_venue);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {


                    case R.id.menu_report:

                        Log.e("-logout-","-logout-");

                        new CheckValidUser().execute();

                        break;
                }
                return true;
            }
        });
    }


//    @Override
//    public void onBackPressed() {
//
//    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateVenueDetailsActivity.this);
            pDialog.setMessage("Logout...");
            pDialog.setIndeterminate(true);
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

                String userId = Employee.getInstance().getUserId();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userId));

                //  params.add(new BasicNameValuePair("device_id", tmDevice));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_update_user_status,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdateVenueDetailsActivity.this,
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


                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt(loginstatusKey, 1);
                    editor.commit();

                    //  startActivity(new Intent(UserActionActivity.this, LoginActivity.class));

                    String exit="exit";

                    Intent intent=new Intent(UpdateVenueDetailsActivity.this, LoginActivity.class);

                    intent.putExtra("exit",exit);

                    startActivity(intent);
                    finish();





                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void updateVenueButtonClick(View v) {
        JSONObject json = null;


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                UpdateVenueDetailsActivity.this);

        // set title
        alertDialogBuilder.setTitle("Purpleknot");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please ensure Near by the Venue")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        updateVenueGps();


                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();



    }

    public void updateVenueGps(){

       // getLocation();

        togglePeriodicLocationUpdates();

        venueName = venueNameEdittext.getText().toString();

        if (venueName.length() == 0) {
            Toast.makeText(UpdateVenueDetailsActivity.this, "Please Enter Venue Name.", Toast.LENGTH_LONG).show();
        }

        new createVenue().execute();

    }


    class createVenue extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pDialog = new ProgressDialog(UpdateVenueDetailsActivity.this);
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
                Log.e("network1===",networkConnection.isNetworkAvailable()+"");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", venueName));

                params.add(new BasicNameValuePair("lat", latitude+""));
                params.add(new BasicNameValuePair("lng", longitude+""));



                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_update_venue_position,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdateVenueDetailsActivity.this,
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

                    Toast.makeText(UpdateVenueDetailsActivity.this,
                            "GPS Location is Updated for "+venueName,
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UpdateVenueDetailsActivity.this, ScheduledVenueActivity.class);
                    // intent.putExtra("flag",0);
                    startActivity(intent);
                    finish();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



    }

//    public void getLocation(){
//
//        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        provider = locationManager.getBestProvider(criteria, false);
//        if(provider!=null && !provider.equals("")){
//
//            // Get the location from the given provider
//            Location location = locationManager.getLastKnownLocation(provider);
//            locationManager.requestLocationUpdates(provider, 2000, 1, this);
//
//            if(location!=null)
//                onLocationChanged(location);
//            else
//                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
//
//        }else{
//            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
//        }
//    }




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
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();



         //   lblLocation.setText(latitude + ", " + longitude);

        } else {

            Toast.makeText(getApplicationContext(),
                    "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_LONG)
                    .show();

        //    lblLocation
        //            .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
          //  btnStartLocationUpdates
//                    .setText(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d("update gps", "Periodic location updates started!");

        } else {
            // Changing the button text
       //     btnStartLocationUpdates
       //             .setText(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d("update gps", "Periodic location updates stopped!");
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
        Log.i("update gps", "Connection failed: ConnectionResult.getErrorCode() = "
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

    //    Toast.makeText(getApplicationContext(), "Location changed!",
   //             Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

}
