package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.purpleknot1.Adapter.ScheduleVenueArrayAdapter;
import com.purpleknot1.Adapter.ViewPagerAdapter;
import com.purpleknot1.DBHandler.PurpleKnotDBHelper;
import com.purpleknot1.DTO.RequestType;
import com.purpleknot1.DTO.ScheduleVenueListDto;
import com.purpleknot1.DTO.ServiceListenerType;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.City;
import com.purpleknot1.Util.Employee;


import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.State;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.purpleknot1.Util.City;
import com.purpleknot1.Util.JSONParser;

public class  ScheduledVenueActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    int flag=0;

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    Spinner stateSpinner,citySpinner,zoneSpinner;

    JSONParser jsonParser = new JSONParser();

    JSONArray venue = null;

    //ListView scheduleListview;

    ScheduleVenueArrayAdapter scheduleVenueArrayAdapter;

    ScheduleVenueListDto scheduleVenueListDto;

    ImageView menuImageview,refreshImageview,backImageview;

    TextView venueIdTextview,venueNameTextview,venueStatusTextview,venueDateTextview;

    PopupMenu popupMenu;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    ViewPager viewPager;
    PagerAdapter adapter1;

    JSONArray state = null;
    JSONArray city = null;
    JSONArray zone = null;

    String stateName,cityName ,zoneName;
    List<String> stateList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();
    List<String> zoneList = new ArrayList<String>();


    ArrayList<String> venueIdList=new ArrayList<String>();
    ArrayList<String> visitIdList=new ArrayList<String>();
    ArrayList<String> userIdList=new ArrayList<String>();
    ArrayList<String> currentStatusList=new ArrayList<String>();
    ArrayList<String> postVisitStatusList=new ArrayList<String>();
    ArrayList<String> scheduleDateList=new ArrayList<String>();
    ArrayList<String> lastVisitDateList=new ArrayList<String>();
    ArrayList<String> visitNumberList=new ArrayList<String>();
    ArrayList<String> scheduleStatusList=new ArrayList<String>();
    ArrayList<String> venueNamesList=new ArrayList<String>();

    ArrayList<String> specialStatus1List=new ArrayList<String>();
    ArrayList<String> specialStatus2List=new ArrayList<String>();
    ArrayList<String> specialStatus3List=new ArrayList<String>();
    ArrayList<String> specialStatus4List=new ArrayList<String>();
    ArrayList<String> specialStatus5List=new ArrayList<String>();
  //  ArrayList<String> managerMessageList=new ArrayList<String>();
    ArrayList<Integer> venueCountList=new ArrayList<Integer>();
    ArrayList<String> locationStatusList=new ArrayList<String>();
    ArrayList<String> latitudeList=new ArrayList<String>();
    ArrayList<String> longitudeList=new ArrayList<String>();


    String venueId[]=new String[9999];
    String visitId[]=new String[9999];
    String userId[]=new String[9999];
    String currentStatus[]=new String[9999];
    String postVisitStatus[]=new String[9999];
    String scheduleDate[]=new String[9999];
    String lastVisitDate[]=new String[9999];
    String visitNumber[]=new String[9999];
    String scheduleStatus[]=new String[9999];
    String venueNames[]=new String[9999];

    String specialStatus1Array[]=new String[9999];
    String specialStatus2Array[]=new String[9999];
    String specialStatus3Array[]=new String[9999];
    String specialStatus4Array[]=new String[9999];
    String specialStatus5Array[]=new String[9999];

    String locationStatusArray[]=new String[9999];

    String latitudeArray[]=new String[9999];
    String longitudeArray[]=new String[9999];


   // String managerMessages[]=new String[9999];
    Integer venueCount[]=new Integer[9999];

    TextView stateTv,cityTv;

    String stateString;

    String selectedState,selectedCity;

    ArrayAdapter<String> stateAdapter;

    ArrayAdapter<String> cityAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.schedule_venue);

      //  scheduleListview=(ListView)findViewById(R.id.scheduled_venue_listview);
        menuImageview=(ImageView)findViewById(R.id.menu_imageview);

        stateSpinner=(Spinner)findViewById(R.id.schedule_venue_state_spinner);
        citySpinner=(Spinner)findViewById(R.id.schedule_venue_city_spinner);

        viewPager = (ViewPager) findViewById(R.id.pager);
       // zoneSpinner=(Spinner)findViewById(R.id.schedule_venue_zone_spinner);
        stateTv = (TextView)findViewById(R.id.statetextview);
        cityTv = (TextView)findViewById(R.id.citytextview);

        refreshImageview=(ImageView) findViewById(R.id.refresh_imageview);

        backImageview = (ImageView) findViewById(R.id.schedule_back_imageview);

        networkConnection = new NetworkConnection(this);



        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        try {
            if (checkPlayServices()) {

                // Building the GoogleApi client
                buildGoogleApiClient();

                createLocationRequest();
            }
        }
        catch (Exception e){

        }

        backImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ScheduledVenueActivity.this, MandapamTypeActivity.class));
                finish();

            }
        });

       // citySpinner.setVisibility(View.INVISIBLE);
      //  cityTv.setVisibility(View.INVISIBLE);




        //togglePeriodicLocationUpdates();


        if (networkConnection.isNetworkAvailable())
        {

          //  new SelectScheduleVenue().execute();

            selectedState = State.getInstance().getState();


            if(selectedState == null) {

                new SelectState().execute();

            }
            else{

           //     Bundle extras = getIntent().getExtras();
           //     flag = extras.getInt("flag");

             //   if(flag == 1) {


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduledVenueActivity.this);
                    alertDialogBuilder.setMessage("If You Want Select New City");

                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            new SelectState().execute();

                            City.getInstance().setCity(null);

                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            List<String> stateList1 = State.getInstance().getStateList();

                            selectedState = State.getInstance().getState();


                            stateAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                                    android.R.layout.simple_spinner_item, stateList1);


//                        List<String> cityList1 = City.getInstance().getCityList();
//
//
//
//                        selectedCity = City.getInstance().getCity();
//
//                        cityAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
//                                android.R.layout.simple_spinner_item, cityList1);


                            // Log.e("positon--", stateAdapter.getPosition(selectedState) + "");

                            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stateSpinner.setAdapter(stateAdapter);
                            if (!selectedState.equals(null)) {
                                int spinnerPosition = stateAdapter.getPosition(selectedState);
                                stateSpinner.setSelection(spinnerPosition);
                            }


//                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        citySpinner.setAdapter(cityAdapter);
//                        if (!selectedCity.equals(null)) {
//                            int spinnerPosition1 = cityAdapter.getPosition(selectedCity);
//                            citySpinner.setSelection(spinnerPosition1);
//                        }


                            //  stateSpinner.setSelection(stateAdapter.getPosition(selectedState));

                            //stateSpinner.setPrompt(selectedState);

                            //    new SelectScheduleVenue1().execute();


                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

              //  }




               // new SelectState().execute();
               // stateAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
              //          android.R.layout.simple_spinner_item, stateList);
               // stateSpinner.setSelection(stateAdapter.getPosition(selectedState));

//                stateSpinner.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        stateSpinner.setSelection(stateAdapter.getPosition(selectedState));
//                    }
//                });




            }

        }
        else
        {

        }


//        if(State.getInstance().getState()!=null) {
//
//            stateName = State.getInstance().getState();
//
//          //  new SelectCity1().execute();
//
//        }

//        if(City.getInstance().getCity()!=null){
//
//            cityName = City.getInstance().getCity();
//            int pos = City.getInstance().getPos();
//
////            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
////                    android.R.layout.simple_spinner_item, cityList);
////
////            int position = adapter.getPosition(cityName);
//
//            citySpinner.setSelection(pos);
//
//
//
//            if (networkConnection.isNetworkAvailable())
//            {
//
//                new SelectScheduleVenue().execute();
//            }
//
//        }

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("state--",parent.getItemAtPosition(position).toString());

                selectedCity = City.getInstance().getCity();

                if(selectedCity ==parent.getItemAtPosition(position).toString()) {

                    if (selectedCity != null) {

                        List<String> cityList1 = City.getInstance().getCityList();

                        cityAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                                android.R.layout.simple_spinner_item, cityList1);

                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter);
                        if (!selectedCity.equals(null)) {
                            int spinnerPosition1 = cityAdapter.getPosition(selectedCity);
                            citySpinner.setSelection(spinnerPosition1);
                        }
                    } else if (selectedCity == null) {

                        if (position != 0) {

                            State.getInstance().setState(null);
                            City.getInstance().setCity(null);

//                    Toast.makeText(parent.getContext(),
//                            "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//                            Toast.LENGTH_SHORT).show();
                            stateName = parent.getItemAtPosition(position).toString();

                            State.getInstance().setState(stateName);

                            new SelectCity().execute();

                            cityList.clear();

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, cityList);

                            citySpinner.setAdapter(adapter);

                            //   citySpinner.setVisibility(View.VISIBLE);
                            //  cityTv.setVisibility(View.VISIBLE);
                            //   stateSpinner.setVisibility(View.INVISIBLE);
                            //   stateTv.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                else{

                    if (position != 0) {

                        State.getInstance().setState(null);
                        City.getInstance().setCity(null);

//                    Toast.makeText(parent.getContext(),
//                            "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//                            Toast.LENGTH_SHORT).show();
                        stateName = parent.getItemAtPosition(position).toString();

                        State.getInstance().setState(stateName);

                        new SelectCity().execute();

                        cityList.clear();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, cityList);

                        citySpinner.setAdapter(adapter);


//===================================================================




                 //       new SelectScheduleVenue().execute();

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




         //       if(position!=0) {

//                Toast.makeText(parent.getContext(),
//                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
                    cityName = parent.getItemAtPosition(position).toString();

                    City.getInstance().setCity(cityName);
                    City.getInstance().setPos(position);

                    //    new SelectZone().execute();

                    //  zoneList.clear();


//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
//                        android.R.layout.simple_spinner_dropdown_item, zoneList);
//
//                zoneSpinner.setAdapter(adapter);


                    new SelectScheduleVenue().execute();
                   // stateSpinner.setVisibility(View.INVISIBLE);
                  //  stateTv.setVisibility(View.INVISIBLE);
        //        }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






//        zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                Toast.makeText(parent.getContext(),
//                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT).show();
//                zoneName = parent.getItemAtPosition(position).toString();
//
//
//
////                new SelectZone().execute();
////
////                zoneList.clear();
////
////
////
////                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
////                        android.R.layout.simple_spinner_dropdown_item, zoneList);
////
////                zoneSpinner.setAdapter(adapter);
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        refreshImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

//        scheduleListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                TextView venueNameTextview = (TextView) view.findViewById(R.id.schedulevenue_name_textview);
//                String venueName = venueNameTextview.getText().toString();
//                Log.e("venueName--------",venueName);
//                Intent intent= new Intent(ScheduledVenueActivity.this,UpdateVenueDetailsActivity.class);
//                intent.putExtra("venueName",venueName);
//                startActivity(intent);
//
//                finish();
//
//                return true;
//            }
//        });

//        scheduleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                TextView venueNameTextview = (TextView) view.findViewById(R.id.schedulevenue_name_textview);
//                TextView venueStatusTextview = (TextView) view.findViewById(R.id.schedulevenue_ststus_textview);
//                TextView venueDateTextview = (TextView) view.findViewById(R.id.schedulevenue_date_textview);
//
//                String venueName = venueNameTextview.getText().toString();
//                String venueStatus = venueStatusTextview.getText().toString();
//                String venueDate = venueDateTextview.getText().toString();
//
//                Intent intent= new Intent(ScheduledVenueActivity.this,UpdateVenueStatusActivity.class);
//                intent.putExtra("venueName",venueName);
//                intent.putExtra("venueStatus",venueStatus);
//                intent.putExtra("venueDate",venueDate);
//                startActivity(intent);
//                finish();
//
//            }
//        });

    }


//    @Override
//    public void onBackPressed() {
//
//        new CheckValidUser().execute();
//        finish();
//
//    }

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
        }
        catch (Exception e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            stopLocationUpdates();
        }
        catch (Exception e){

        }
    }



    /**
     * Method to toggle periodic location updates
     * */
    private void togglePeriodicLocationUpdates() {

        try {
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
        catch (Exception e){

        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        catch (Exception e){

        }
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        }
        catch (Exception e){

        }
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
        try {
            Log.i("TAG", "Connection failed: ConnectionResult.getErrorCode() = "
                    + result.getErrorCode());
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnected(Bundle arg0) {

        try {
            // Once connected with google api, get the location
            displayLocation();

            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {

        try {
            mGoogleApiClient.connect();
        }
        catch (Exception e){

        }


    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            // Assign the new location
            mLastLocation = location;

            // Toast.makeText(getApplicationContext(), "Location changed!",
            //         Toast.LENGTH_SHORT).show();

            // Displaying the new location on UI
            displayLocation();
        }
        catch (Exception e){

        }
    }



    private void displayLocation() {

        try {

            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                lati = mLastLocation.getLatitude();
                longi = mLastLocation.getLongitude();

                Log.e("loc lat--", lati + "");
                Log.e("loc lon--", longi + "");

                //  lblLocation.setText(latitude + ", " + longitude);

            } else {

                // lblLocation
                //         .setText("(Couldn't get the location. Make sure location is enabled on the device)");
            }
        }
        catch (Exception e){

        }
    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduledVenueActivity.this);
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

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(ScheduledVenueActivity.this,
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

                    Intent intent=new Intent(ScheduledVenueActivity.this, LoginActivity.class);

                    intent.putExtra("exit",exit);

                    startActivity(intent);
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public static String[] clean(final String[] v) {
        List<String> list = new ArrayList<String>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);
    }
    public static Integer[] clean(final Integer[] v) {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new Integer[list.size()]);
    }


    class SelectZone extends AsyncTask<String, String, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduledVenueActivity.this);
            pDialog.setMessage("Loading List..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
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
                    params.add(new BasicNameValuePair("city_name", cityName));
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_zone_by_city,
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

                    zone = json.getJSONArray(ApplicationConstants.TAG_ZONE);

                    for(int i = 0; i < zone.length(); i++ ){
                        JSONObject c = zone.getJSONObject(i);
                        String zoneName = c.getString(ApplicationConstants.TAG_ZONE_NAME);

                        if(zoneList.size()==0){
                            zoneList.add(0,"Select Zone");
                        }

                        zoneList.add(zoneName);
                        zoneSpinner.setPrompt("Select Zone");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                                android.R.layout.simple_spinner_item, zoneList);

                        zoneSpinner.setAdapter(adapter);

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

    class SelectCity extends AsyncTask<String, String, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduledVenueActivity.this);
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
                    city = json.getJSONArray(ApplicationConstants.TAG_CTY);

                    for(int i = 0; i < city.length(); i++ ){
                        JSONObject c = city.getJSONObject(i);
                        String cityName = c.getString(ApplicationConstants.TAG_CTY_NME);

                        if(cityList.size()==0){
                            cityList.add(0,"Select City");
                        }

                        cityList.add(cityName);

                        City.getInstance().setCityList(cityList);

//                        String defaultTextForSpinner = "Select City";
//                        String[] stockArr = new String[cityList.size()];
//                        stockArr = cityList.toArray(stockArr);

                     //   cityAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                     //           android.R.layout.simple_spinner_item, cityList);

                        cityAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                                R.layout.citylist, R.id.citylisttext, cityList);

                        citySpinner.setAdapter(cityAdapter);

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
            pDialog = new ProgressDialog(ScheduledVenueActivity.this);
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
                    state = json.getJSONArray(ApplicationConstants.TAG_EXISTING_STATE);

                    for(int i = 0; i < state.length(); i++ ){
                        JSONObject c = state.getJSONObject(i);
                        String stateName = c.getString(ApplicationConstants.TAG_EXISTING_STATE_NAME);

                        if(stateList.size()==0){

                        stateList.add(0,"Select State");

                        }

                        stateList.add(stateName);

                        State.getInstance().setStateList(stateList);

                      //  stateAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                      //          android.R.layout.simple_spinner_item, stateList);

                        stateAdapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
                                R.layout.statelist, R.id.statelisttext, stateList);

//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduledVenueActivity.this,
//                                android.R.layout.simple_spinner_item, stateList);

                        stateSpinner.setAdapter(stateAdapter);
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



    class SelectScheduleVenue extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduledVenueActivity.this);
            pDialog.setMessage("Loading List..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;
            if (networkConnection.isNetworkAvailable()) {

                try {

                    String userId = Employee.getInstance().getUserId();

                    String cityId = Employee.getInstance().getCityId();

                    Log.e("userId--", userId);
                    Log.e("cityId--", cityId);



                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("user_id", userId));
                    params.add(new BasicNameValuePair("city_name", cityName));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_scheduledvenue_userid1,
                            "POST", params);

                 //   Log.e("json---",json.toString());

                }
                catch (Exception e){

                    Log.e("schedule Eror",e.toString());
                }

            }
            else {

            }

            return json;
        }

        protected void onPostExecute(JSONObject json) {

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

                if (json != null){

                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {


                    viewPager.setAdapter(null);

                    venueIdList.clear();
                    visitIdList.clear();
                    userIdList.clear();
                    currentStatusList.clear();
                    postVisitStatusList.clear();
                    scheduleDateList.clear();
                    lastVisitDateList.clear();
                    visitNumberList.clear();
                    scheduleStatusList.clear();
                    venueNamesList.clear();
                    //   managerMessageList.clear();
                    venueCountList.clear();

                    specialStatus1List.clear();
                    specialStatus2List.clear();
                    specialStatus3List.clear();
                    specialStatus4List.clear();
                    specialStatus5List.clear();
                    locationStatusList.clear();
                    latitudeList.clear();
                    longitudeList.clear();

                    clean(venueId);
                    clean(visitId);
                    clean(userId);
                    clean(currentStatus);
                    clean(postVisitStatus);
                    clean(scheduleDate);
                    clean(lastVisitDate);
                    clean(visitNumber);
                    clean(scheduleStatus);
                    clean(venueNames);
                    //  clean(managerMessages);
                    clean(venueCount);
                    clean(specialStatus1Array);
                    clean(specialStatus2Array);
                    clean(specialStatus3Array);
                    clean(specialStatus4Array);
                    clean(specialStatus5Array);
                    clean(locationStatusArray);

                    clean(latitudeArray);
                    clean(longitudeArray);

                    venue = json.getJSONArray(ApplicationConstants.TAG_VENUEHISTORE);

                    for (int i = 0; i < venue.length(); i++) {
                        JSONObject c = venue.getJSONObject(i);

                        String venueid = c.getString(ApplicationConstants.TAG_VENUEID);
                        String visitid = c.getString(ApplicationConstants.TAG_VISITID);
                        String venuename = c.getString(ApplicationConstants.TAG_VENUENAME);
                        String userid = c.getString(ApplicationConstants.TAG_USERRID);
                        String currentstatus = c.getString(ApplicationConstants.TAG_CURRENTSTATUS);
                        String postvisitedStatus = c.getString(ApplicationConstants.TAG_POSTVISITSTATUS);
                        String scheduledate = c.getString(ApplicationConstants.TAG_SCHEDULEDATE);
                        String lastvisitedDate = c.getString(ApplicationConstants.TAG_LASTVISITDATE);
                        String visitnumber = c.getString(ApplicationConstants.TAG_VISITNUMBER);
                        String schedulestatus = c.getString(ApplicationConstants.TAG_SCHEDULESTATUS);

                        String specialstatus1 = c.getString(ApplicationConstants.TAG_VENUE_SPECIAL_STATUS1);
                        String specialstatus2 = c.getString(ApplicationConstants.TAG_VENUE_SPECIAL_STATUS2);
                        String specialstatus3 = c.getString(ApplicationConstants.TAG_VENUE_SPECIAL_STATUS3);
                        String specialstatus4 = c.getString(ApplicationConstants.TAG_VENUE_SPECIAL_STATUS4);
                        String specialstatus5 = c.getString(ApplicationConstants.TAG_VENUE_SPECIAL_STATUS5);
                        String locationCaptured = c.getString(ApplicationConstants.TAG_VENUE_LOCATION_CAPTURED);
                        String latitude = c.getString(ApplicationConstants.TAG_VENUE_LATITUDE);
                        String longitude = c.getString(ApplicationConstants.TAG_VENUE_LONGITUDE);

                        //     String managerMessage = c.getString(ApplicationConstants.TAG_MANAGERMESSAGE);

                        venueIdList.add(venueid);
                        visitIdList.add(visitid);
                        userIdList.add(userid);
                        currentStatusList.add(currentstatus);
                        postVisitStatusList.add(postvisitedStatus);
                        scheduleDateList.add(scheduledate);
                        lastVisitDateList.add(lastvisitedDate);
                        visitNumberList.add(visitnumber);
                        scheduleStatusList.add(schedulestatus);
                        venueNamesList.add(venuename);
                        //   managerMessageList.add(managerMessage);
                        venueCountList.add(i);
                        specialStatus1List.add(specialstatus1);
                        specialStatus2List.add(specialstatus2);
                        specialStatus3List.add(specialstatus3);
                        specialStatus4List.add(specialstatus4);
                        specialStatus5List.add(specialstatus5);
                        locationStatusList.add(locationCaptured);

                        latitudeList.add(latitude);
                        longitudeList.add(longitude);

                        venueId = venueIdList.toArray(new String[venueIdList.size()]);
                        visitId = visitIdList.toArray(new String[visitIdList.size()]);
                        userId = userIdList.toArray(new String[userIdList.size()]);
                        currentStatus = currentStatusList.toArray(new String[currentStatusList.size()]);
                        postVisitStatus = postVisitStatusList.toArray(new String[postVisitStatusList.size()]);
                        scheduleDate = scheduleDateList.toArray(new String[scheduleDateList.size()]);
                        lastVisitDate = lastVisitDateList.toArray(new String[lastVisitDateList.size()]);
                        visitNumber = visitNumberList.toArray(new String[visitNumberList.size()]);
                        scheduleStatus = scheduleStatusList.toArray(new String[scheduleStatusList.size()]);
                        venueNames = venueNamesList.toArray(new String[venueNamesList.size()]);
                        //   managerMessages = managerMessageList.toArray(new String[managerMessageList.size()]);
                        venueCount = venueCountList.toArray(new Integer[venueCountList.size()]);

                        specialStatus1Array = specialStatus1List.toArray(new String[specialStatus1List.size()]);
                        specialStatus2Array = specialStatus2List.toArray(new String[specialStatus2List.size()]);
                        specialStatus3Array = specialStatus3List.toArray(new String[specialStatus3List.size()]);
                        specialStatus4Array = specialStatus4List.toArray(new String[specialStatus4List.size()]);
                        specialStatus5Array = specialStatus5List.toArray(new String[specialStatus5List.size()]);

                        locationStatusArray = locationStatusList.toArray(new String[locationStatusList.size()]);
                        latitudeArray = latitudeList.toArray(new String[latitudeList.size()]);
                        longitudeArray = longitudeList.toArray(new String[longitudeList.size()]);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(ApplicationConstants.TAG_VENUEID, venueid);
                        map.put(ApplicationConstants.TAG_VISITID, visitid);
                        map.put(ApplicationConstants.TAG_VENUENAME, venuename);
                        map.put(ApplicationConstants.TAG_USERRID, userid);
                        map.put(ApplicationConstants.TAG_CURRENTSTATUS, currentstatus);
                        map.put(ApplicationConstants.TAG_POSTVISITSTATUS, postvisitedStatus);
                        map.put(ApplicationConstants.TAG_SCHEDULEDATE, scheduledate);
                        map.put(ApplicationConstants.TAG_LASTVISITDATE, lastvisitedDate);
                        map.put(ApplicationConstants.TAG_VISITNUMBER, visitnumber);
                        map.put(ApplicationConstants.TAG_SCHEDULESTATUS, schedulestatus);
                        map.put("count", String.valueOf(i + 1));

                        oslist.add(map);

                        Log.e("iiii--", i + 1 + "");

                        int count = venueId.length;

                        adapter1 = new ViewPagerAdapter(ScheduledVenueActivity.this, venueId, visitId, userId, currentStatus,
                                postVisitStatus, scheduleDate, lastVisitDate, visitNumber, scheduleStatus, venueNames, venueCount,
                                specialStatus1Array, specialStatus2Array, specialStatus3Array, specialStatus4Array, specialStatus5Array,
                                locationStatusArray, latitudeArray, longitudeArray, lati, longi);
                        adapter1.notifyDataSetChanged();


                        viewPager.setAdapter(adapter1);


                    }

                    togglePeriodicLocationUpdates();

                } else {
                }
            }else{

                    finish();
                    startActivity(new Intent(ScheduledVenueActivity.this, MandapamTypeActivity.class));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(ScheduledVenueActivity.this, view);
        popupMenu.inflate(R.menu.menu_schedule_venue);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_report:

                        new CheckValidUser().execute();

                        break;

                }
                return true;
            }
        });
    }
}