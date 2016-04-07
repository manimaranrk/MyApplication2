package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.model.MarkerOptions;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.GPSListener;
import com.purpleknot1.Util.GpsStatus;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateVenueMapActivity extends Activity implements LocationListener{

    // Google Map
    private GoogleMap googleMap;
    Button locate_button,submit_button,cancel_button;
    private ProgressDialog pDialog;
    NetworkConnection networkConnection;
    String venueName,venueAddress1,venueAddress2,city=null,state,venuePin,contactName,designation,contactNumber,
            contactEmail,ownerName,ownerNumber,venueClass,seatingCapacity,venueWebsite,pastOccupancy,venueType;

    double latitude,longitude;
    JSONParser jsonParser = new JSONParser();

    GPSListener gpsListener;
    private LocationListener listener;
    private LocationManager manager;
    boolean locate;
    GPSTracker1 gpsTracker1;
    GpsStatus gpsStatus;
    int status=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createvenuemapactivity);

        locate_button = (Button)findViewById(R.id.locate_button) ;
        submit_button = (Button)findViewById(R.id.sumbit_button);

        cancel_button = (Button)findViewById(R.id.mapcancel_button);

        networkConnection = new NetworkConnection(this);

        submit_button.setEnabled(false);

        submit_button.setBackgroundColor(Color.RED);

        locate_button.setEnabled(false);

        locate_button.setBackgroundColor(Color.RED);

        gpsTracker1 = new GPSTracker1(CreateVenueMapActivity.this);

        if(gpsTracker1.canGetLocation()){

            try {

                initilizeMap();

            } catch (Exception e) {
                e.printStackTrace();
            }

            LocationManager locationManager1 = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean enabledGPS1 = locationManager1
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            Criteria criteria1 = new Criteria();
            String bestProvider1 = locationManager1.getBestProvider(criteria1, true);
            Location location1 = locationManager1.getLastKnownLocation(bestProvider1);

            if (location1 != null) {

                onLocationChanged(location1);

            }
            locationManager1.requestLocationUpdates(bestProvider1, 20000, 0, this);


            // \n is for new line
        //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            new CountDownTimer(15000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                    locate_button.setText("Locate "+millisUntilFinished / 1000);

                    submit_button.setEnabled(false);
                    locate_button.setEnabled(false);

                }

                public void onFinish() {

                    locate_button.setText("Locate");
                    locate_button.setEnabled(true);

                    locate_button.setBackgroundColor(Color.parseColor("#6A287E"));
                }
            }.start();

        }else{

            showSettingsAlert();

        }

        Log.e("lati--", latitude + "");
        Log.e("long--", longitude + "");

        locate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit_button.setEnabled(false);
                submit_button.setBackgroundColor(Color.RED);
                googleMap.setMyLocationEnabled(true);

                new CountDownTimer(15000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                        submit_button.setEnabled(false);
                        locate_button.setEnabled(false);
                        submit_button.setText("Submit " + millisUntilFinished / 1000);

                    }

                    public void onFinish() {

                        submit_button.setText("Submit");
                        submit_button.setEnabled(true);
                        submit_button.setBackgroundColor(Color.parseColor("#6A287E"));
                    }
                }.start();

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CreateVenueMapActivity.this, UserActionActivity.class));
                finish();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(CreateVenueMapActivity.this, MandapamTypeActivity.class));
//                finish();

                startActivity(new Intent(CreateVenueMapActivity.this, TargetEntityActivity.class));
                finish();

            }
        });
    }

    public int  showSettingsAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateVenueMapActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intentRedirectionGPSSettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intentRedirectionGPSSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intentRedirectionGPSSettings, 1);

            }
        });

        alertDialog.show();

        return status;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("resultcode______",resultCode+"");

        if(resultCode==0){

            Log.e("refreshhh===","refresh==");

            Intent refresh = new Intent(this, TargetEntityActivity.class);
            startActivity(refresh);
            this.finish();
        }
        else{
            Log.e("refreshhh1111===","refresh==");
        }

    }

    private class MyLocationListener implements LocationListener{
        public void onLocationChanged(Location location) {

            // TODO Auto-generated method stub
            if (location != null){
                //text.setText(("Lat: " + location.getLatitude()
                 //       + "\nLLong: " + location.getLongitude()));
            }
            Toast.makeText(CreateVenueMapActivity.this, ""+location, Toast.LENGTH_SHORT).show();
        }
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (latitude != 0.0 || longitude != 0.0) {

                LatLng latLng = new LatLng(latitude, longitude);
                //    googleMap.addMarker(new MarkerOptions().position(latLng));

                //   googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(13.0900, 78.0000), 14.0f) );

        //        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                //  googleMap.setMyLocationEnabled(true);

                googleMap.getUiSettings().setZoomControlsEnabled(true);
            }
        }catch (Exception e){

            Log.e("error++++----",e.toString());
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initilizeMap();

        Log.e("resuleeee","resuleeeee");

    }

    class createVenue extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateVenueMapActivity.this);
            pDialog.setMessage("Venue Createing...");
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

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", venueName));
                params.add(new BasicNameValuePair("venue_address1", venueAddress1));
                params.add(new BasicNameValuePair("location", venueAddress2));
                params.add(new BasicNameValuePair("city", city));
                params.add(new BasicNameValuePair("state", state));
                params.add(new BasicNameValuePair("pincode", venuePin));
                params.add(new BasicNameValuePair("contact_name", contactName));
                params.add(new BasicNameValuePair("designation", designation));
                params.add(new BasicNameValuePair("contact_number", contactNumber));
                params.add(new BasicNameValuePair("contact_email", contactEmail));
                params.add(new BasicNameValuePair("owner_name", ownerName));
                params.add(new BasicNameValuePair("owner_number", ownerNumber));
                params.add(new BasicNameValuePair("class", venueClass));
                params.add(new BasicNameValuePair("seating_capacity", seatingCapacity));
                params.add(new BasicNameValuePair("website", venueWebsite));
                params.add(new BasicNameValuePair("past_occupancy", pastOccupancy));
                params.add(new BasicNameValuePair("lat", latitude+""));
                params.add(new BasicNameValuePair("lng", longitude+""));
                params.add(new BasicNameValuePair("venue_type", venueType));

                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_create_venue1,
                        "POST", params);

                // check log cat fro response
                //    Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(CreateVenueMapActivity.this,
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

                    Toast.makeText(CreateVenueMapActivity.this,"Venue Created Successfully. ",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateVenueMapActivity.this,MandapamTypeActivity.class));
                    finish();

                }
                else if(success == 2){

                    Toast.makeText(CreateVenueMapActivity.this,"Venue Name Already Exists. ",Toast.LENGTH_SHORT).show();

                }

                else if(success == 0) {

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
                }
                else if(success == 2) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);

            Log.e("gps---","on");

        }
        else{
            Log.e("gps---","off");
        }
    }

}