package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class CreatePGActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    EditText pgNameEdittext,pgAgeEdittext,pgFatherEdittext,pgAddressEdittext,pgContactNameEdittext,pgTelNoEdittext,pgMobileEdittext,

    pgEmailEdittext,pgFirmEdittext,pgPartnerEdittext,pgWebsiteEdittext,pgEventsEdittext;

    Spinner pgStateSpinner,pgCitySpinner;

    Button pgSubmitButton;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    NetworkConnection networkConnection;

    JSONArray stateArray = null;
    JSONArray cityArray = null;

    List<String> stateList = new ArrayList<String>();

    String stateName;

    List<String> cityList = new ArrayList<String>();

    String state,city;

    String pgName,pgAge,pgFather,pgAddress,pgContactName,pgTelno,pgMobile,pgEmail,pgFirm,pgPartner,pgWebsite,pgEvents;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private boolean mRequestingLocationUpdates = false;

    private Location mLastLocation;

    double latitude = 0.0,longitude = 0.0;

    ImageView backbutton;

    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_pg);

        networkConnection = new NetworkConnection(this);

        pgNameEdittext=(EditText)findViewById(R.id.pg_name_edittext);
        pgAgeEdittext=(EditText)findViewById(R.id.pg_age_edittext);
        pgFatherEdittext=(EditText)findViewById(R.id.pg_father_edittext);
        pgAddressEdittext=(EditText)findViewById(R.id.pg_address_edittext);
        pgContactNameEdittext=(EditText)findViewById(R.id.pg_contactname_edittext);
        pgTelNoEdittext=(EditText)findViewById(R.id.pg_tel_edittext);
        pgMobileEdittext=(EditText)findViewById(R.id.pg_number_edittext);
        pgEmailEdittext=(EditText)findViewById(R.id.pg_email_edittext);
        pgFirmEdittext=(EditText)findViewById(R.id.pg_firmname_edittext);
        pgPartnerEdittext=(EditText)findViewById(R.id.pg_partner_edittext);
        pgWebsiteEdittext=(EditText)findViewById(R.id.pg_website_edittext);
        pgEventsEdittext=(EditText)findViewById(R.id.pg_events_edittext);
        pgStateSpinner=(Spinner)findViewById(R.id.pg_state_edittext);
        pgCitySpinner=(Spinner)findViewById(R.id.pg_city_edittext);

        backbutton=(ImageView)findViewById(R.id.createpg_back_imageview);

        pgSubmitButton=(Button)findViewById(R.id.createpg_submit_button);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }


        new SelectState().execute();

        pgStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                stateName = parent.getItemAtPosition(position).toString();

                new SelectCity().execute();
                cityList.clear();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreatePGActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, cityList);

                pgCitySpinner.setAdapter(adapter);
                state = parent.getItemAtPosition(position).toString();
                //  togglePeriodicLocationUpdates();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        pgCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                city = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CreatePGActivity.this, PgVgTypeActivity.class));
                finish();

            }
        });

        pgSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPG();

            }
        });

    }

    public void pgmenuButtonClickEvent(View view){

        popupMenu = new PopupMenu(CreatePGActivity.this, view);
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

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreatePGActivity.this);
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
                Toast.makeText(CreatePGActivity.this,
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
                    Intent intent=new Intent(CreatePGActivity.this, LoginActivity.class);
                    intent.putExtra("exit",exit);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
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

    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {

            mRequestingLocationUpdates = true;
            startLocationUpdates();
        } else {

            mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }

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

    public void createPG(){

        pgName = pgNameEdittext.getText().toString();
        pgAge = pgAgeEdittext.getText().toString();
        pgFather = pgFatherEdittext.getText().toString();
        pgAddress = pgAddressEdittext.getText().toString();
        pgContactName=pgContactNameEdittext.getText().toString();
        pgTelno = pgTelNoEdittext.getText().toString();
        pgMobile=pgMobileEdittext.getText().toString();
        pgEmail=pgEmailEdittext.getText().toString();
        pgFirm = pgFirmEdittext.getText().toString();
        pgPartner = pgPartnerEdittext.getText().toString();
        pgWebsite = pgWebsiteEdittext.getText().toString();
        pgEvents = pgEventsEdittext.getText().toString();

        if(pgName.length()==0){
            Toast.makeText(CreatePGActivity.this, "Please Enter PG/VG Name.", Toast.LENGTH_LONG).show();
        }
        else  if(pgAge.length()==0){
            Toast.makeText(CreatePGActivity.this, "Please Enter Age.", Toast.LENGTH_LONG).show();
        }
        else  if(pgFather.length()==0){
            Toast.makeText(CreatePGActivity.this, "Please Enter Father's Name.", Toast.LENGTH_LONG).show();
        }
        else  if(pgAddress.length()==0){
            Toast.makeText(CreatePGActivity.this, "Please Enter PG/VG Address.", Toast.LENGTH_LONG).show();
        }
        else  if(pgContactName.length()==0){
            Toast.makeText(CreatePGActivity.this, "Please Enter Contact Name.", Toast.LENGTH_LONG).show();
        }
        else  if(pgTelno.length()!=10){
            Toast.makeText(CreatePGActivity.this, "Please Enter Tel No.", Toast.LENGTH_LONG).show();
        }

        else  if(pgMobile.length()!=10){
            Toast.makeText(CreatePGActivity.this, "Please Enter Mobile No.", Toast.LENGTH_LONG).show();
        }
//        else  if(pgEmail.length()==0){
//            Toast.makeText(CreatePGActivity.this, "Please Enter Email.", Toast.LENGTH_LONG).show();
//        }

        else  if(pgFirm.length()==0){
            Toast.makeText(CreatePGActivity.this, "Please Enter Frim Name.", Toast.LENGTH_LONG).show();
        }
//        else  if(pgPartner.length()==0){
//            Toast.makeText(CreatePGActivity.this, "Please Enter Entity Proprietor Partnership/Company:.", Toast.LENGTH_LONG).show();
//        }

        else  if(pgEvents.length()==0){
            Toast.makeText(CreatePGActivity.this, "Please Enter No of Events Done.", Toast.LENGTH_LONG).show();
        }
        else{
            new createPg().execute();
        }
    }

    class createPg extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreatePGActivity.this);
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

                String userId = Employee.getInstance().getUserId();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", pgName));
                params.add(new BasicNameValuePair("pgAge", pgAge));
                params.add(new BasicNameValuePair("pgFather", pgFather));
                params.add(new BasicNameValuePair("city", city));
                params.add(new BasicNameValuePair("state", state));
                params.add(new BasicNameValuePair("pgAddress", pgAddress));
                params.add(new BasicNameValuePair("pgContactName", pgContactName));
                params.add(new BasicNameValuePair("pgTelno", pgTelno));
                params.add(new BasicNameValuePair("pgMobile", pgMobile));
                params.add(new BasicNameValuePair("pgEmail", pgEmail));
                params.add(new BasicNameValuePair("pgFirm", pgFirm));
                params.add(new BasicNameValuePair("pgPartner", pgPartner));
                params.add(new BasicNameValuePair("pgWebsite", pgWebsite));
                params.add(new BasicNameValuePair("pgEvents", pgEvents));
                params.add(new BasicNameValuePair("lat", latitude+""));
                params.add(new BasicNameValuePair("lng", longitude+""));
                params.add(new BasicNameValuePair("userId", userId));

                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_create_pgpv,
                        "POST", params);

                // check log cat fro response
                //    Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(CreatePGActivity.this,
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

                    Toast.makeText(CreatePGActivity.this,
                            "Pg/VG Created Successfully. ",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(CreatePGActivity.this, PgVgTypeActivity.class));
                    finish();

                }
                else if(success == 2){

                    Toast.makeText(CreatePGActivity.this,
                            "Pg/VG Already Exists. ",
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

                    Toast.makeText(CreatePGActivity.this,
                            "PG/Vg Name Already Exists. ",
                            Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
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
            pDialog = new ProgressDialog(CreatePGActivity.this);
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreatePGActivity.this,
                                android.R.layout.simple_spinner_item, cityList);

                        pgCitySpinner.setAdapter(adapter);

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

    class SelectState extends AsyncTask<String, String, JSONObject>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreatePGActivity.this);
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreatePGActivity.this,
                                android.R.layout.simple_spinner_item, stateList);

                        pgStateSpinner.setAdapter(adapter);
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

}
