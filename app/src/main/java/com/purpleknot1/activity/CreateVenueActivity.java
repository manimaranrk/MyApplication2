package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;


import com.purpleknot1.Util.ApplicationConstants;

import com.purpleknot1.Util.CreateVenue;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.FPSDBConstants;

import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.purpleknot1.Util.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.os.StrictMode;

public class CreateVenueActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    LocationManager locationManager ;
    String provider;
    double latitude = 0.0,longitude = 0.0;
    double latitude1 = 0.0,longitude1 = 0.0;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


    EditText venueNameEdittext,venueAddress1Edittext,venueAddress2Edittext,pastOccupancyEdittext,venueCityEdittext,venueStateEdittext,venuePinEdittext,venueRegionEdittext;
    EditText contectNameEdittext,contactNumberEdittext,contactEmailEdittext,ownerNameEdittext,ownerNumberEdittext;
    EditText seatingCapacityEdittext,venueWebsiteEdittext,designationEdittext;

    Spinner venueTypeSpinner,venueClassEdittext;

    Spinner stateSpinner,citySpinner;

   // Button captureGpsButton;

    ImageView backImageView;

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    JSONArray stateArray = null;
    JSONArray cityArray = null;

    List<String> stateList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();

    String stateName,cityName ,zoneName;

    String venueName,venueAddress1,venueAddress2,city=null,state,venuePin,contactName,designation,contactNumber,
     contactEmail,ownerName,ownerNumber,venueClass,seatingCapacity,venueWebsite,pastOccupancy,venueType;

    PopupMenu popupMenu;

    SharedPreferences sharedpreferences;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_venue);

        networkConnection = new NetworkConnection(this);

        venueNameEdittext=(EditText)findViewById(R.id.venue_name_edittext);
        venueAddress1Edittext=(EditText)findViewById(R.id.venue_address1_edittext);
        venueAddress2Edittext=(EditText)findViewById(R.id.venue_address2_edittext);
        stateSpinner = (Spinner)findViewById(R.id.venuestate_edittext);
        citySpinner  = (Spinner)findViewById(R.id.venuecity_edittext);

        venuePinEdittext=(EditText)findViewById(R.id.pincode_edittext);
        contectNameEdittext=(EditText)findViewById(R.id.contactname_edittext);
        designationEdittext=(EditText)findViewById(R.id.designation_edittext);
        contactNumberEdittext=(EditText)findViewById(R.id.contactnumber_texiview);
        contactEmailEdittext=(EditText)findViewById(R.id.contactemail_edittext);
        ownerNameEdittext=(EditText)findViewById(R.id.ownername_edittext);
        ownerNumberEdittext=(EditText)findViewById(R.id.ownernumber_edittext);
        venueClassEdittext=(Spinner)findViewById(R.id.venueclass_edittext);
        pastOccupancyEdittext=(EditText)findViewById(R.id.pastoccupancy_edittext);
        seatingCapacityEdittext=(EditText)findViewById(R.id.seatingcapacity_edittext);
        venueWebsiteEdittext=(EditText)findViewById(R.id.website_edittext);
        venueTypeSpinner=(Spinner)findViewById(R.id.venue_type_spinner);
        backImageView=(ImageView)findViewById(R.id.createvenue_back_imageview);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

        List<String> classList = new ArrayList<String>();
        classList.add("Please Select the Class");
        classList.add("A");
        classList.add("B");
        classList.add("C");
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,classList);
        classAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        venueClassEdittext.setAdapter(classAdapter);
        List<String> typeList = new ArrayList<String>();
        typeList.add("Select the Venue Type");
        typeList.add("Mini Hall");
        typeList.add("Marriage Hall");
        typeList.add("Function Hall");
        typeList.add("Banquet Hall");
        typeList.add("Hotel");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,typeList);

        typeAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        venueTypeSpinner.setAdapter(typeAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                stateName = parent.getItemAtPosition(position).toString();
                new SelectCity().execute();
                cityList.clear();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateVenueActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, cityList);

                citySpinner.setAdapter(adapter);
                state = parent.getItemAtPosition(position).toString();
              //  togglePeriodicLocationUpdates();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        venueTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                venueType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        venueClassEdittext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                venueClass = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ownerNumberEdittext.addTextChangedListener(watch);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateVenueActivity.this, MandapamTypeActivity.class);
                startActivity(intent);
               finish();
            }
        });

        new SelectState().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            if (mGoogleApiClient != null) {

                mGoogleApiClient.connect();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            checkPlayServices();

            // Resuming the periodic location updates
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }catch (Exception e){

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
        try {
            if (!mRequestingLocationUpdates) {

                mRequestingLocationUpdates = true;
                startLocationUpdates();
            } else {

                mRequestingLocationUpdates = false;
                stopLocationUpdates();
            }
        }
        catch (Exception e){

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

        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
        catch (Exception e){

        }


    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {

        try {

            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
        catch (Exception e){

        }
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
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            Log.e("loc lat--",latitude+"");
            Log.e("loc lon--",longitude+"");

        } else {

           // lblLocation
           //         .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

            if (arg0.length() !=10) {

              //  Toast.makeText(getApplicationContext(), "Please Enter Valid Owner Number.", Toast.LENGTH_SHORT).show();
                //Your query to fetch Data
            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub

           // output.setText(s);
            if(a != 10){

            }
        }};


    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(CreateVenueActivity.this, view);
        popupMenu.inflate(R.menu.menu_schedule_venue);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_report:
                        Log.e("-logout-", "-logout-");
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
//        new CheckValidUser().execute();
//        finish();
//
//    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateVenueActivity.this);
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


                //  Log.e("userName--",userName);
                // Log.e("passWord--",passWord);
                //  Log.e("tmDevice--",tmDevice);

                String userId = Employee.getInstance().getUserId();


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userId));

                //  params.add(new BasicNameValuePair("device_id", tmDevice));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_update_user_status,
                        "POST", params);

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(CreateVenueActivity.this,
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
                    Intent intent=new Intent(CreateVenueActivity.this, LoginActivity.class);
                    intent.putExtra("exit",exit);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class SelectState extends AsyncTask<String, String, JSONObject>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateVenueActivity.this);
            pDialog.setMessage("Loading State..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {
            JSONObject json = null;
            if (networkConnection.isNetworkAvailable())
            {
                try
                {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_state,
                            "POST", params);

                } catch (Exception e)
                {
                    Log.e("schedule Eror", e.toString());
                }

            } else {
            }

            return json;
        }

        protected void onPostExecute(JSONObject json)
        {
            try
            {
                if ((pDialog != null) && pDialog.isShowing())
                {
                    pDialog.dismiss();
                }
            }
            catch (final IllegalArgumentException e)
            {
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
                pDialog = null;
            }

            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1)
                {
                    stateArray = json.getJSONArray(ApplicationConstants.TAG_EXISTING_STATE);

                    for(int i = 0; i < stateArray.length(); i++ ){
                        JSONObject c = stateArray.getJSONObject(i);
                        String stateName = c.getString(ApplicationConstants.TAG_EXISTING_STATE_NAME);

                        if(stateList.size()==0){
                            stateList.add(0,"Select State");
                        }

                        stateList.add(stateName);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateVenueActivity.this,
                                android.R.layout.simple_spinner_item, stateList);

                        stateSpinner.setAdapter(adapter);
                    }
                }
                else
                {
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    class SelectCity extends AsyncTask<String, String, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateVenueActivity.this);
            pDialog.setMessage("Loading City..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args)
        {

            JSONObject json = null;
            if (networkConnection.isNetworkAvailable())
            {
                try
                {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("state_name", stateName));
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_city_by_state,
                            "POST", params);

                } catch (Exception e)
                {

                    Log.e("schedule Eror", e.toString());
                }

            } else {

            }

            return json;
        }

        protected void onPostExecute(JSONObject json)
        {
            try
            {
                if ((pDialog != null) && pDialog.isShowing())
                {
                    pDialog.dismiss();
                }

            }
            catch (final IllegalArgumentException e)
            {
                // Handle or log or ignore
            }
            catch (final Exception e)
            {
                // Handle or log or ignore
            }
            finally
            {
                pDialog = null;
            }

            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1)
                {
                    cityArray = json.getJSONArray(ApplicationConstants.TAG_CTY);

                    for(int i = 0; i < cityArray.length(); i++ ){
                        JSONObject c = cityArray.getJSONObject(i);
                        String cityName = c.getString(ApplicationConstants.TAG_CTY_NME);

                        if(cityList.size()==0){
                            cityList.add(0,"Select City");
                        }

                        cityList.add(cityName);
                   //     citySpinner.setPrompt("Select City");
//                        String defaultTextForSpinner = "Select City";
//                        String[] stockArr = new String[cityList.size()];
//                        stockArr = cityList.toArray(stockArr);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateVenueActivity.this,
                                android.R.layout.simple_spinner_item, cityList);

                        citySpinner.setAdapter(adapter);

                        // citySpinner.setAdapter(new CustomSpinnerAdapter(ExistingVenueActivity.this, R.layout.spinner_item, stockArr, defaultTextForSpinner));
                    }
                }
                else
                {
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    class createVenue extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateVenueActivity.this);
            pDialog.setMessage("Login...");
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
                Log.e("network1===",networkConnection.isNetworkAvailable()+"");

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
                Toast.makeText(CreateVenueActivity.this,
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

                    Toast.makeText(CreateVenueActivity.this,
                            "Venue Created Successfully. ",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(CreateVenueActivity.this,MandapamTypeActivity.class));
                    finish();

                }
                else if(success == 2){

                    Toast.makeText(CreateVenueActivity.this,
                            "Venue Name Already Exists. ",
                            Toast.LENGTH_SHORT).show();

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

    public void createVenue(){

        ownerNumber=null;

//        JSONObject json = null;

        togglePeriodicLocationUpdates();

        venueName=venueNameEdittext.getText().toString();
        venueAddress1=venueAddress1Edittext.getText().toString();
        venueAddress2=venueAddress2Edittext.getText().toString();

        venuePin=venuePinEdittext.getText().toString();
        contactName=contectNameEdittext.getText().toString();
        designation=designationEdittext.getText().toString();
        contactNumber=contactNumberEdittext.getText().toString();
        contactEmail=contactEmailEdittext.getText().toString();
        ownerName=ownerNameEdittext.getText().toString();
        ownerNumber =ownerNumberEdittext.getText().toString();
        pastOccupancy=pastOccupancyEdittext.getText().toString();
        seatingCapacity=seatingCapacityEdittext.getText().toString();
        venueWebsite=venueWebsiteEdittext.getText().toString();

        venueName = venueName.replaceAll("[^a-zA-Z0-9]", " ");
        venueAddress1 = venueAddress1.replaceAll("[^a-zA-Z0-9]", " ");
        venueAddress2 = venueAddress2.replaceAll("[^a-zA-Z0-9]", " ");

        contactName = contactName.replaceAll("[^a-zA-Z0-9]", " ");
        designation = designation.replaceAll("[^a-zA-Z0-9]", " ");

        ownerName = ownerName.replaceAll("[^a-zA-Z0-9]", " ");

        if(venueName.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Venue Name.", Toast.LENGTH_LONG).show();
        }

        else if(venueAddress1.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Venue Address1.", Toast.LENGTH_LONG).show();
        }

        else if(venueAddress2.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Location.", Toast.LENGTH_LONG).show();
        }

        else if(city=="Select City"){
            Toast.makeText(CreateVenueActivity.this, "Please Select City.", Toast.LENGTH_LONG).show();
        }

        else if(state=="Select State"){
            Toast.makeText(CreateVenueActivity.this, "Please Select  State.", Toast.LENGTH_LONG).show();
        }

        else if(designation.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Designation.", Toast.LENGTH_LONG).show();
        }

        else if(venuePin.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Pincode.", Toast.LENGTH_LONG).show();
        }

        else if(contactName.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Contact Name.", Toast.LENGTH_LONG).show();
        }

        else if(contactNumber.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Contact Number.", Toast.LENGTH_LONG).show();
        }

        else if(contactNumber.length()!=10){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Contact Number.", Toast.LENGTH_LONG).show();
        }

        else if(ownerName.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Owner Name.", Toast.LENGTH_LONG).show();
        }

        else if(venueClass=="Please Select the Class"){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Class.", Toast.LENGTH_LONG).show();
        }

        else if(pastOccupancy.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Past Occupancy.", Toast.LENGTH_LONG).show();
        }

        else if(seatingCapacity.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Seating Capacity.", Toast.LENGTH_LONG).show();
        }

        else if(pastOccupancy.length()==0){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Past Occupancy.", Toast.LENGTH_LONG).show();
        }
        else if(venueType =="Select the Venue Type"){
            Toast.makeText(CreateVenueActivity.this, "Please Enter Venue Type.", Toast.LENGTH_LONG).show();
        }

        else {

            try {

                if(contactEmail.equals("")){
                    contactEmail="nil";
                }

                if(venueWebsite.equals("")){
                    venueWebsite="nil";
                }
                if(ownerNumber.equals("")){
                    ownerNumber="0";
                }

                if (latitude != 0.0 || longitude != 0.0) {

                    if(ownerNumber.equals("0")){

                        CreateVenue.getInstance().setVenueName(venueName);
                        CreateVenue.getInstance().setVenueAddress1(venueAddress1);
                        CreateVenue.getInstance().setVenueAddress2(venueAddress2);
                        CreateVenue.getInstance().setCity(city);
                        CreateVenue.getInstance().setState(state);
                        CreateVenue.getInstance().setDesignation(designation);
                        CreateVenue.getInstance().setVenuePin(venuePin);
                        CreateVenue.getInstance().setContactName(contactName);
                        CreateVenue.getInstance().setContactNumber(contactNumber);
                        CreateVenue.getInstance().setOwnerName(ownerName);
                        CreateVenue.getInstance().setOwnerNumber(ownerNumber);

                        CreateVenue.getInstance().setVenueClass(venueClass);
                        CreateVenue.getInstance().setPastOccupancy(pastOccupancy);
                        CreateVenue.getInstance().setSeatingCapacity(seatingCapacity);
                        CreateVenue.getInstance().setVenueType(venueType);

                        CreateVenue.getInstance().setContactEmail(contactEmail);
                        CreateVenue.getInstance().setVenueWebsite(venueWebsite);

                      //  startActivity(new Intent(CreateVenueActivity.this, CreateVenueMapActivity.class));
                     //   finish();

                        new createVenue().execute();

                    }
                    else if(!ownerNumber.equals("") && ownerNumber.length() != 10) {

                        Toast.makeText(CreateVenueActivity.this, "Please Enter Valid Owner Number.", Toast.LENGTH_LONG).show();

                    }
                    else if(ownerNumber.length() == 10){

                        CreateVenue.getInstance().setVenueName(venueName);
                        CreateVenue.getInstance().setVenueAddress1(venueAddress1);
                        CreateVenue.getInstance().setVenueAddress2(venueAddress2);
                        CreateVenue.getInstance().setCity(city);
                        CreateVenue.getInstance().setState(state);
                        CreateVenue.getInstance().setDesignation(designation);
                        CreateVenue.getInstance().setVenuePin(venuePin);
                        CreateVenue.getInstance().setContactName(contactName);
                        CreateVenue.getInstance().setContactNumber(contactNumber);
                        CreateVenue.getInstance().setOwnerName(ownerName);
                        CreateVenue.getInstance().setOwnerNumber(ownerNumber);

                        CreateVenue.getInstance().setVenueClass(venueClass);
                        CreateVenue.getInstance().setPastOccupancy(pastOccupancy);
                        CreateVenue.getInstance().setSeatingCapacity(seatingCapacity);
                        CreateVenue.getInstance().setVenueType(venueType);

                        CreateVenue.getInstance().setContactEmail(contactEmail);
                        CreateVenue.getInstance().setVenueWebsite(venueWebsite);

                    //    startActivity(new Intent(CreateVenueActivity.this, CreateVenueMapActivity.class));
                    //    finish();

                        new createVenue().execute();
                    }

                }else{

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            CreateVenueActivity.this);

                    alertDialogBuilder.setTitle("Purpleknot");

                    alertDialogBuilder
                            .setMessage("Switch on Location in Phone Settings")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    dialog.cancel();

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            } catch (Exception e) {
                Log.e("LoginActivity_userLogin", e.toString(), e);
            }
        }

    }

    public void createVenueButtonClick(View v)
    {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CreateVenueActivity.this);

        // set title
        alertDialogBuilder.setTitle("Purpleknot");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please ensure Near by the Venue")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        createVenue();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
