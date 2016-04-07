package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.purpleknot1.Adapter.StatusAdapter;
import com.purpleknot1.Adapter.StatusArrayAdapter;
import com.purpleknot1.DTO.ImageAttachmentDto;
import com.purpleknot1.DTO.ImageDto;
import com.purpleknot1.DTO.StatusDto;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.purpleknot1.DTO.StatusDto;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

public class UpdateVenueStatusActivit extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    String imageFilePath;
    String imageFilename;
    byte[] imageBytes;

    double distance=0.0;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String dat,tim;

    ListView relativeLayout;

    JSONArray stage = null;

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    NetworkConnection networkConnection;

    EditText venueNameEdittext,venuecommendEdittext;

    String venueNme;

    File storageDirectory;

    Spinner scheduleVenueStatusSpinner;

    CheckBox notInterestCheckbox,interventionCheckbox,introductoryCheckbox,friendlyCheckbon;

    String venueName, visitId ,venueId, userId ,scheduleVenueStatus;

    String imageBase64Bytes;

    HashSet<String> hashSet = new HashSet();

    ArrayList<String> ar = new ArrayList<String>();

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    ArrayList<HashMap<String, String>> venueStatusList = new ArrayList<HashMap<String, String>>();

    ImageDto imageDto=new ImageDto();

    ArrayList<String> alist = new ArrayList<>();

    ImageAttachmentDto imageAttachmentDto=new ImageAttachmentDto();
    String venueStatus,venueDate;

    boolean isChecked;

    String currentDateandTime;

    String venueCommend;

    TextView venueStatusTextview,stageTextview;

    ImageView backImageview;

    Spinner stateSpinner,citySpinner;

    ToggleButton stageToggle;

    StatusArrayAdapter statusArrayAdapter;
    private ArrayList<StatusDto> m_parts = new ArrayList<StatusDto>();

    StatusDto statusDto = new StatusDto();

    int imageCount;

    String postVisitStatus;

    StatusAdapter statusAdapter;

    String stageNumber;

    LinearLayout linear;

    Button updateSameStatusButton,nextButton;

    ArrayList<String> statuslist = new ArrayList<String>();

    String specialStatus1,specialStatus2,specialStatus3,specialStatus4,specialStatus5;

    ToggleButton stage3toggleButton=null;

    String status3=null;

    int flag1=0;

    int statusId;

    PopupMenu popupMenu;

    private int myear;
    private int mmonth;
    private int mday;

    private int myear1;
    private int mmonth1;
    private int mday1;

    static final int DATE_DIALOG_ID = 999;

    double venue_latitude=0.0,venue_longitude=0.0;



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

    //Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_venue_statu);

        //scheduleVenueStatusSpinner=(Spinner)findViewById(R.id.schedulevenuestatus_spinner);

        venueNameEdittext=(EditText)findViewById(R.id.schedulevenue_name_edittext);

        stageTextview = (TextView)findViewById(R.id.stage_textview);

        linear = (LinearLayout)findViewById(R.id.owner_toggle);

       // updateSameStatusButton = (Button)findViewById(R.id.same_status_button);

        nextButton = (Button)findViewById(R.id.next_button);

        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);






       // venuecommendEdittext=(EditText)findViewById(R.id.venuecommend_edittext);

       // notInterestCheckbox=(CheckBox)findViewById(R.id.notinterest_checkbox);

       // interventionCheckbox = (CheckBox)findViewById(R.id.intervention_checkbox);

       // introductoryCheckbox = (CheckBox)findViewById(R.id.introductory_checkbox);

        //friendlyCheckbon = (CheckBox)findViewById(R.id.friendly_checkbox);

       // venueStatusTextview=(TextView)findViewById(R.id.venuestatus_textview);
        relativeLayout = (ListView)findViewById(R.id.relative);

        backImageview=(ImageView)findViewById(R.id.updatestatus_back_imageview);

      //  stateSpinner = (Spinner)findViewById(R.id.update_venue_state_spinner);

      //  citySpinner =  (Spinner)findViewById(R.id.update_venue_city_spinner);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }


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

            venueNameEdittext.setText(venueName);

            new DisplayStatus().execute();

        }



        backImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UpdateVenueStatusActivit.this, ScheduledVenueActivity.class);
               // intent.putExtra("flag",0);
                startActivity(intent);
                finish();

            }
        });


//        updateSameStatusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance = 0.0;

                Location locationA = new Location("point A");
                locationA.setLatitude(lati);
                locationA.setLongitude(longi);
                Location locationB = new Location("point B");
                locationB.setLatitude(venue_latitude);
                locationB.setLongitude(venue_longitude);
                distance += locationA.distanceTo(locationB);
                double kilometers = distance * 0.001;


                Log.e("current_latitude", lati + "");
                Log.e("current_longitude", longi + "");
                Log.e("venue_latitude", venue_latitude + "");
                Log.e("venue_longitude", venue_longitude + "");

                Log.e("distance", distance + "");
                Log.e("kilometers", kilometers + "");

                BigDecimal bd = new BigDecimal(kilometers);
                String val = bd.toPlainString();




                    if (distance <= 300) {

                        if (networkConnection.isNetworkAvailable()) {


                            Log.e("statusId--", statusId + "");


                            if (statusId == 1 || statusId == 2 || statusId == 3 || statusId == 4 || statusId == 5 ||
                                    statusId == 6 || statusId == 7 || statusId == 8 || statusId == 9 || statusId == 10 ||
                                    statusId == 11 || statusId == 12 || statusId == 13) {

                                if (stageNumber.equals("1")) {

                                    if (statuslist.size() != 0) {

                                        Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage1StatusActivity.class);
                                        intent.putExtra("venueName", venueNameEdittext.getText().toString());
                                        intent.putExtra("visitId", visitId);
                                        intent.putExtra("venueId", venueId);
                                        intent.putExtra("userId", userId);

                                        intent.putExtra("venue_latitude", venue_latitude);
                                        intent.putExtra("venue_longitude", venue_longitude);

                                        intent.putExtra("displayStatus", oslist);
                                        intent.putExtra("specialStatus1", specialStatus1);
                                        intent.putExtra("specialStatus2", specialStatus2);
                                        intent.putExtra("specialStatus3", specialStatus3);
                                        intent.putExtra("specialStatus4", specialStatus4);
                                        intent.putExtra("specialStatus5", specialStatus5);


                                        intent.putExtra("venueStatusList", statuslist);
                                        intent.putExtra("postVisitStatus", postVisitStatus);
                                        intent.putExtra("stageNumber", stageNumber);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        // finish();
                                    } else {

                                        Log.e("button---", "else");

                                        Toast.makeText(UpdateVenueStatusActivit.this,
                                                "Please Select the Option.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } else if (stageNumber.equals("2")) {

                                    if (statuslist.size() != 0) {
                                        for (HashMap<String, String> venue : venueStatusList) {

                                            if (venue.containsValue("Template of Agreement Provided") || venue.containsValue("Agreement Accepted") || venue.containsValue("Details Collected for Agreement")) {

                                                Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage2StatusActivity.class);
                                                intent.putExtra("visitId", visitId);
                                                intent.putExtra("venueId", venueId);
                                                intent.putExtra("userId", userId);

                                                intent.putExtra("venue_latitude", venue_latitude);
                                                intent.putExtra("venue_longitude", venue_longitude);

                                                intent.putExtra("venueName", venueNameEdittext.getText().toString());
                                                intent.putExtra("venueStatus", venueStatus);
                                                intent.putExtra("StatusList", statuslist);
                                                intent.putExtra("venueStatusList", venueStatusList);
                                                intent.putExtra("postVisitStatus", postVisitStatus);
                                                intent.putExtra("stageNumber", stageNumber);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();

                                            } else if (venue.containsValue("Owner Met")) {

                                                Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage2Status1Activity.class);
                                                intent.putExtra("visitId", visitId);
                                                intent.putExtra("venueId", venueId);
                                                intent.putExtra("userId", userId);

                                                intent.putExtra("venue_latitude", venue_latitude);
                                                intent.putExtra("venue_longitude", venue_longitude);

                                                intent.putExtra("venueName", venueNameEdittext.getText().toString());
                                                intent.putExtra("venueStatus", venueStatus);
                                                intent.putExtra("StatusList", statuslist);
                                                intent.putExtra("venueStatusList", venueStatusList);
                                                intent.putExtra("postVisitStatus", postVisitStatus);
                                                intent.putExtra("stageNumber", stageNumber);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();

                                            } else {

                                                Intent intent = new Intent(UpdateVenueStatusActivit.this, AgreementDetailsActivity.class);
                                                intent.putExtra("venueName", venueNameEdittext.getText().toString());
                                                intent.putExtra("visitId", visitId);
                                                intent.putExtra("venueId", venueId);
                                                intent.putExtra("userId", userId);

                                                intent.putExtra("venue_latitude", venue_latitude);
                                                intent.putExtra("venue_longitude", venue_longitude);

                                                // intent.putExtra("venueStatus", venueStatus);
                                                intent.putExtra("StatusList", statuslist);
                                                intent.putExtra("venueStatusList", venueStatusList);
                                                intent.putExtra("stageNumber", stageNumber);
                                                intent.putExtra("postVisitStatus", postVisitStatus);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();

                                                // finish();

                                            }

                                        }
                                    } else {

                                        Log.e("button---", "else");

                                        Toast.makeText(UpdateVenueStatusActivit.this,
                                                "Please Select the Option.",
                                                Toast.LENGTH_SHORT).show();


                                    }


//                    Intent intent = new Intent(UpdateVenueStatusActivit.this, AgreementDetailsActivity.class);
////
//                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                    intent.putExtra("venueStatusList", venueStatusList);
//                    intent.putExtra("postVisitStatus", postVisitStatus);
//                    startActivity(intent);
//                    finish();

                                } else if (stageNumber.equals("3")) {

                                    if (status3 != null) {


                                        if (dat != null) {
                                            Log.e("status3--", status3);

                                            Log.e("dat111--", dat);

                                            Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage3StatusActivity.class);
                                            intent.putExtra("visitId", visitId);
                                            intent.putExtra("venueId", venueId);
                                            intent.putExtra("userId", userId);

                                            intent.putExtra("venue_latitude", venue_latitude);
                                            intent.putExtra("venue_longitude", venue_longitude);

                                            intent.putExtra("venueName", venueNameEdittext.getText().toString());
                                            intent.putExtra("venueStatus", status3);
                                            intent.putExtra("dat", dat);
                                            intent.putExtra("postVisitStatus", postVisitStatus);
                                            intent.putExtra("stageNumber", stageNumber);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();


                                        } else {
                                            Toast.makeText(UpdateVenueStatusActivit.this,
                                                    "Please Select the Date.",
                                                    Toast.LENGTH_SHORT).show();
                                        }


                                    } else {
                                        Log.e("button---", "else");

                                        Toast.makeText(UpdateVenueStatusActivit.this,
                                                "Please Select the Option.",
                                                Toast.LENGTH_SHORT).show();


                                    }


                                }

//                else if (stageNumber.equals("3")) {
//
//                    Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage2StatusActivity.class);
//                    intent.putExtra("visitId",visitId);
//                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                    intent.putExtra("venueStatusList", venueStatusList);
//                    intent.putExtra("postVisitStatus", postVisitStatus);
//                    intent.putExtra("stageNumber", stageNumber);
//                    startActivity(intent);
//                    finish();
//
//
//
//
//                }


                                //------------------------------------------------------------------------------
//                                                    Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage1StatusActivity.class);
//                                                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                                                    intent.putExtra("venueStatus", tv.getText().toString());
//                                                    intent.putExtra("postVisitStatus", postVisitStatus);
//                                                    intent.putExtra("stageNumber", stageNumber);
//                                                    startActivity(intent);
//                                                    finish();
                                //-------------------------------------------------------------------------------

//                                                } else if (stageNumber.equals("2")) {
//
//                                                    Intent intent = new Intent(UpdateVenueStatusActivit.this, AgreementDetailsActivity.class);
//
//                                                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                                                    intent.putExtra("venueStatus", tv.getText().toString());
//                                                    intent.putExtra("postVisitStatus", postVisitStatus);
//                                                    startActivity(intent);
//                                                    finish();
//
//                                                }


                                //----------------------------------------------------

//                    Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage1StatusActivity.class);
//                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                    intent.putExtra("venueStatusList", venueStatusList);
//                    intent.putExtra("postVisitStatus", postVisitStatus);
//                    intent.putExtra("stageNumber", stageNumber);
//                    startActivity(intent);
//                    finish();


                            } else {
                                Intent intent = new Intent(UpdateVenueStatusActivit.this, ScheduledVenueActivity.class);
                                startActivity(intent);

                            }


                        } else {

                            Toast.makeText(UpdateVenueStatusActivit.this,
                                    "Network is not Available. Please Check Your Internet Connection ",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                    else{

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                UpdateVenueStatusActivit.this);

                        // set title
                        alertDialogBuilder.setTitle("Purpleknot");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Please Update Status Near the Venue")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();

                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


                    }

                }

        });






//        relativeLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                TextView venueNameTextview = (TextView) view.findViewById(R.id.status_texiview);
//
//                Log.e("text--",venueNameTextview.getText().toString());
//
//
//                RelativeLayout rLayout = (RelativeLayout) view;
//
//                ToggleButton tgl = (ToggleButton) rLayout.getChildAt(1);
//
//                stageToggle = (ToggleButton) view.findViewById(R.id.stage_toggle);
//
//                String strStatus = "";
//
//                if(tgl.isChecked()){
//
//                  //  tgl.setChecked(false);
//
//                  //  strStatus = "Off";
//                    Log.e("toggle--","on");
//
//
//                }else{
//
//                    Log.e("toggle--","off");
//                  //  tgl.setChecked(true);
//
//                  //  strStatus = "On";
//
//
//
//                }
//
//                if(stageToggle.isChecked()){
//
//                    Log.e("toggle--","on");
//                }
//                else{
//                    Log.e("toggle--","off");
//                }
//
//            }
//        });

      //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
     //   currentDateandTime = sdf.format(new Date());

//        Log.e("currentDateandTime",currentDateandTime);
//
//        notInterestCheckbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                isChecked = notInterestCheckbox.isChecked();
//
//                if (isChecked) {
//
//                    venueStatusTextview.setVisibility(View.GONE);
//                    scheduleVenueStatusSpinner.setVisibility(View.GONE);
//                } else {
//
//                    venueStatusTextview.setVisibility(View.VISIBLE);
//                    scheduleVenueStatusSpinner.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });

//        notInterestCheckbox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
//        interventionCheckbox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
//        introductoryCheckbox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
//        friendlyCheckbon.setOnCheckedChangeListener(new myCheckBoxChnageClicker());

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





    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(UpdateVenueStatusActivit.this, view);
        popupMenu.inflate(R.menu.menu_schedule_venue);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.menu_venue:
//
//                        startActivity(new Intent(ScheduledVenueActivity.this, CreateVenueActivity.class));
//
//                        break;

                    case R.id.menu_report:

                        Log.e("-logout-", "-logout-");

                        new CheckValidUser().execute();

                    //    startActivity(new Intent(UpdateVenueStatusActivit.this, LoginActivity.class));

                    //    finish();

                        //   startActivity(new Intent(ScheduledVenueActivity.this, ReportActivity.class));

                        break;

                }
                return true;
            }
        });
    }





    class DisplayStatus extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateVenueStatusActivit.this);
            pDialog.setMessage("Display Status...");
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
                params.add(new BasicNameValuePair("venue_name", venueName));

                json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_stage_venue2,
                        "POST", params);

                Log.d("Create Response", json.toString());

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdateVenueStatusActivit.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return json;
        }

        protected void onPostExecute(JSONObject json) {

            try {
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } catch (final IllegalArgumentException e) {

            } catch (final Exception e) {

            } finally {
                pDialog = null;
            }


            try {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                if (success == 1) {

                    stage = json.getJSONArray(ApplicationConstants.TAG_STAGE);


                    for (int i = 0; i < stage.length(); i++) {
                        JSONObject c = stage.getJSONObject(i);

                        ScrollView scrl=new ScrollView(UpdateVenueStatusActivit.this);
                        final LinearLayout ll=new LinearLayout(UpdateVenueStatusActivit.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        scrl.addView(ll);



                        statusId = c.getInt(ApplicationConstants.TAG_STATUS_ID);
                        stageNumber = c.getString(ApplicationConstants.TAG_STAGE_NUMBER);

                        String statusDescription = c.getString(ApplicationConstants.TAG_STATUS_DESCRIPTION);

                        statusDto.setStatusName(statusDescription);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(ApplicationConstants.TAG_STATUS_DESCRIPTION, statusDescription);

                        oslist.add(map);

                        alist.add(statusDescription);


                        m_parts.add(statusDto);

                        stageTextview.setText("Stage " + stageNumber);

                        if(stageNumber.equals("3")){

                            SimpleAdapter adapter = new SimpleAdapter(UpdateVenueStatusActivit.this, oslist, R.layout.column_status1,
                                    new String[]{ApplicationConstants.TAG_STATUS_DESCRIPTION}, new int[]{R.id.status_texiview}) {
                                @Override
                                public View getView(int pos, View convertView, ViewGroup parent) {
                                    View v = convertView;
                                    if (v == null) {
                                        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        v = vi.inflate(R.layout.column_status1, null);
                                    }
                                    TextView tv = (TextView) v.findViewById(R.id.status_texiview);
                                    tv.setText(oslist.get(pos).get(ApplicationConstants.TAG_STATUS_DESCRIPTION));

                                    final Button dateButton=(Button) v.findViewById(R.id.stage3_date_button);
                                   // final Button time=(Button) v.findViewById(R.id.time_button);


                                    stage3toggleButton = (ToggleButton) v.findViewById(R.id.stage3_toggle);




                                    Log.e("toggle check --",stage3toggleButton.isChecked()+"");

//                                    Toast.makeText(getApplicationContext(),
//                                            String.valueOf(stage3toggleButton.isChecked()), Toast.LENGTH_SHORT).show();

//                                    Button submit = new Button(UpdateVenueStatusActivit.this);
//
//                                    submit.setText("Submit");
//
//                                    LinearLayout.LayoutParams layoutParams = new  LinearLayout.LayoutParams(70, 70);
//                                    layoutParams.setMargins(5, 3, 0, 0); // left, top, right, bottom
//                                    submit.setLayoutParams(layoutParams);
//
//                                    linear.addView(submit);
//
//
//                                    submit.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//
//
//
//
//
//                                        }
//                                    });



                                    dateButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        //    showDialog(DATE_DIALOG_ID);

                                       //     dat = myear1 + "-" + (mmonth1 + 1) + "-" + mday1;

                                        //    dateButton.setText(myear1 + "-" + (mmonth1 + 1) + "-" + mday1);

                                            final Calendar c = Calendar.getInstance();
                                            mYear = c.get(Calendar.YEAR);
                                            mMonth = c.get(Calendar.MONTH);
                                            mDay = c.get(Calendar.DAY_OF_MONTH);





                                            // Launch Date Picker Dialog
                                            DatePickerDialog dpd = new DatePickerDialog(UpdateVenueStatusActivit.this,
                                                    new DatePickerDialog.OnDateSetListener() {






                                                        @Override
                                                        public void onDateSet(DatePicker view, int year,
                                                                              int monthOfYear, int dayOfMonth) {

//                                    Toast.makeText(getApplicationContext(),
//                                            "Date : " + dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year,
//                                            Toast.LENGTH_SHORT).show();
                                                            //    dateButton.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                            dateButton.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                                            dat = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;


                                                            // Display Selected date in textbox
                                                            //     txtDate.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);

                                                        }
                                                    }, mYear, mMonth, mDay);
                                            dpd.show();

                                        }
                                    });

//                                    time.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                            final Calendar c = Calendar.getInstance();
//                                            mHour = c.get(Calendar.HOUR_OF_DAY);
//                                            mMinute = c.get(Calendar.MINUTE);
//
//                                            // Launch Time Picker Dialog
//                                            TimePickerDialog tpd = new TimePickerDialog(UpdateVenueStatusActivit.this,
//                                                    new TimePickerDialog.OnTimeSetListener() {
//
//                                                        @Override
//                                                        public void onTimeSet(TimePicker view, int hourOfDay,
//                                                                              int minute) {
//                                                            // Display Selected time in textbox
//                                                            //  txtTime.setText(hourOfDay + ":" + minute);
////                                    Toast.makeText(getApplicationContext(),
////                                            "Time : " + hourOfDay + ":" + minute,
////                                            Toast.LENGTH_SHORT).show();
//                                                            tim = hourOfDay + ":" + minute + ":00";
//
//                                                            time.setText(hourOfDay + ":" + minute + ":00");
//                                                        }
//                                                    }, mHour, mMinute, false);
//                                            tpd.show();
//
//
//                                        }
//                                    });
                                    stage3toggleButton.setChecked(false);


                                    Log.e("toggle check111 --", stage3toggleButton.isChecked() + "");

                                    stage3toggleButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {



                                            Log.e("button toggle--", stage3toggleButton.isChecked() + "");

//                                            Toast.makeText(getApplicationContext(),
//                                                    String.valueOf("button--" + stage3toggleButton.isChecked()), Toast.LENGTH_SHORT).show();

                                            //  stage3toggleButton.setChecked(false);
//                                            if(flag1==1) {
//
//                                                stage3toggleButton.setChecked(true);
//                                            }
//                                            else{
//                                                stage3toggleButton.setChecked(false);
//                                            }

                                            try {

                                                if (flag1 == 0) {

                                                    LinearLayout rl = (LinearLayout) v.getParent();

                                                    TextView tv = (TextView) rl.findViewById(R.id.status_texiview);

                                                    //    Button datbutton = (Button) rl.findViewById(R.id.stage3_date_button);

                                                    Log.e("Textttttt--", tv.getText().toString());


                                                    // flag1=1;

                                                    HashMap<String, String> map = new HashMap<String, String>();

                                                    map.put(ApplicationConstants.TAG_STATUS_DESCRIPTION, tv.getText().toString());

                                                    venueStatusList.add(map);


                                                    statuslist.add(tv.getText().toString());

                                                    status3 = tv.getText().toString();

                                                    Log.e("status3", status3);

                                                    //     Log.e("Text11--", datbutton.getText().toString());


//                                                if (stageNumber.equals("1")) {


                                                    //----------------------------------------------------------------------------------
//                                                    Intent intent = new Intent(UpdateVenueStatusActivit.this, StatusActivity1.class);
//                                                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                                                    intent.putExtra("venueStatus", tv.getText().toString());
//                                                    intent.putExtra("date", dat);
//                                                    intent.putExtra("stageNumber", stageNumber);
//                                                    intent.putExtra("postVisitStatus", postVisitStatus);
//                                                    startActivity(intent);
//                                                    finish();
                                                    //------------------------------------------------------------------------------------


//                                                } else if (stageNumber.equals("2")) {
//
//                                                    Intent intent = new Intent(UpdateVenueStatusActivit.this, AgreementDetailsActivity.class);
//
//                                                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                                                    intent.putExtra("venueStatus", tv.getText().toString());
//                                                    intent.putExtra("postVisitStatus", postVisitStatus);
//                                                    startActivity(intent);
//                                                    finish();
//
//                                                }
                                                    flag1 = 1;

//
                                                    Log.e("toggle--", "on");
                                                }
                                                else
                                                {


                                                    LinearLayout rl = (LinearLayout) v.getParent();

                                                    TextView tv = (TextView) rl.findViewById(R.id.status_texiview);
                                                    Log.e("Text--", tv.getText().toString());

                                                    //Button datbutton = (Button) rl.findViewById(R.id.stage3_date_button);

                                                    Log.e("Text--", tv.getText().toString());

                                                    HashMap<String, String> map = new HashMap<String, String>();

                                                    // map.put(ApplicationConstants.TAG_STATUS_DESCRIPTION, tv.getText().toString());

                                                    map.remove(tv.getText().toString());

                                                    venueStatusList.remove(map);

                                                    status3 = null;
                                                    statuslist.remove(tv.getText().toString());

                                                    for (int i = 0; i < statuslist.size(); i++) {

                                                        Log.e("array---", statuslist.get(i).toString());
                                                    }
                                                    flag1 = 0;
                                                }
                                            }
                                            catch (Exception e)
                                            {

                                                Log.e("exx--",e.toString());

                                            }
//                                                //   flag1=0;
//
//                                                //status3 = null;
//
//
//                                                //Log.e("Text11--", datbutton.getText().toString());
//
//
////
////                                                Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage1StatusActivity.class);
////
////                                                intent.putExtra("venueName", venueNameEdittext.getText().toString());
////                                                intent.putExtra("venueStatus", tv.getText().toString());
////                                                intent.putExtra("postVisitStatus", postVisitStatus);
////
////                                                startActivity(new Intent(UpdateVenueStatusActivit.this, Stage1StatusActivity.class));
////                                                finish();
////
//                                                Log.e("toggle--", "off");
//
//                                            }


                                        }
                                    });




                                    return v;


                                }

                            };


//                        ListAdapter adapter1 = new SimpleAdapter(UpdateVenueStatusActivit.this, oslist,
//                                R.layout.column_status,
//                                new String[] {ApplicationConstants.TAG_STATUS_DESCRIPTION }, new int[] {
//                                R.id.status_texiview});

                            //       relativeLayout.setAdapter(statusAdapter);

                            relativeLayout.setAdapter(adapter);




                        }
                        else if(stageNumber.equals("4")){


                            stageTextview.setText("No Stage");
                            nextButton.setText("Venue is Completed ");

                        }

                        else {

                            //       statusAdapter= new StatusAdapter(UpdateVenueStatusActivit.this, R.layout.column_status, m_parts);

                            SimpleAdapter adapter = new SimpleAdapter(UpdateVenueStatusActivit.this, oslist, R.layout.column_status,
                                    new String[]{ApplicationConstants.TAG_STATUS_DESCRIPTION}, new int[]{R.id.status_texiview}) {
                                @Override
                                public View getView(int pos, View convertView, ViewGroup parent) {
                                    View v = convertView;
                                    if (v == null) {
                                        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        v = vi.inflate(R.layout.column_status, null);
                                    }
                                    TextView tv = (TextView) v.findViewById(R.id.status_texiview);
                                    tv.setText(oslist.get(pos).get(ApplicationConstants.TAG_STATUS_DESCRIPTION));

                                    final ToggleButton toggleButton = (ToggleButton) v.findViewById(R.id.stage_toggle);


                                    toggleButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (toggleButton.isChecked()) {
                                                LinearLayout rl = (LinearLayout) v.getParent();
                                                TextView tv = (TextView) rl.findViewById(R.id.status_texiview);
                                                Log.e("Text--", tv.getText().toString());

                                                HashMap<String, String> map = new HashMap<String, String>();

                                                map.put(ApplicationConstants.TAG_STATUS_DESCRIPTION, tv.getText().toString());

                                                venueStatusList.add(map);


                                                statuslist.add(tv.getText().toString());

                                               // if (stageNumber.equals("1")) {

                                                    //------------------------------------------------------------------------------
//                                                    Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage1StatusActivity.class);
//                                                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                                                    intent.putExtra("venueStatus", tv.getText().toString());
//                                                    intent.putExtra("postVisitStatus", postVisitStatus);
//                                                    intent.putExtra("stageNumber", stageNumber);
//                                                    startActivity(intent);
//                                                    finish();
                                                    //-------------------------------------------------------------------------------

//                                                } else if (stageNumber.equals("2")) {
//
//                                                    Intent intent = new Intent(UpdateVenueStatusActivit.this, AgreementDetailsActivity.class);
//
//                                                    intent.putExtra("venueName", venueNameEdittext.getText().toString());
//                                                    intent.putExtra("venueStatus", tv.getText().toString());
//                                                    intent.putExtra("postVisitStatus", postVisitStatus);
//                                                    startActivity(intent);
//                                                    finish();
//
//                                                }



                                                Log.e("toggle--", "yes");
                                            }
                                            else{

                                                LinearLayout rl = (LinearLayout) v.getParent();
                                                TextView tv = (TextView) rl.findViewById(R.id.status_texiview);
                                                Log.e("Text--", tv.getText().toString());

                                                HashMap<String, String> map = new HashMap<String, String>();

                                               // map.put(ApplicationConstants.TAG_STATUS_DESCRIPTION, tv.getText().toString());

                                                map.remove(tv.getText().toString());

                                                venueStatusList.remove(map);


                                                statuslist.remove(tv.getText().toString());

                                                for(int i=0;i<statuslist.size();i++){

                                                    Log.e("array---", statuslist.get(i).toString());
                                                }

                                               // venueStatusList.add(map);

//                                                if(venueStatusList.size() != 0) {
//
//                                                    for (int i = 1; i < venueStatusList.size() - 1; i++) {
//
//                                                        Log.e("array---", venueStatusList.get(i).get(ApplicationConstants.TAG_STATUS_DESCRIPTION));
//
//                                                    }
//                                                }


                                            }
//                                            else {
//
//
//                                                //-------------------------------------------------------
//                                                LinearLayout rl = (LinearLayout) v.getParent();
//                                                TextView tv = (TextView) rl.findViewById(R.id.status_texiview);
//                                                Log.e("Text--", tv.getText().toString());
////                                                Intent intent = new Intent(UpdateVenueStatusActivit.this, Stage1StatusActivity.class);
////                                                intent.putExtra("venueName", venueNameEdittext.getText().toString());
////                                                intent.putExtra("venueStatus", tv.getText().toString());
////                                                intent.putExtra("postVisitStatus", postVisitStatus);
////                                                startActivity(intent);
////                                                finish();
//                                                //----------------------------------------------------------
//
//                                                Log.e("toggle--", "off");
//
//                                            }


                                        }
                                    });


                                    return v;
                                }


                            };

                            relativeLayout.setAdapter(adapter);

                        }

                    }
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                DatePickerDialog _date =   new DatePickerDialog(this, datePickerListener, myear,mmonth,
                        mday){

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        if (year < myear)
                            view.updateDate(myear, mmonth, mday);

                        if (monthOfYear < mmonth && year == myear)
                            view.updateDate(myear, mmonth, mday);

                        if (dayOfMonth < mday && year == myear && monthOfYear == mmonth)
                            view.updateDate(myear, mmonth, mday);

                    }
                };

                return _date;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myear1 = selectedYear;
            mmonth1 = selectedMonth;
            mday1 = selectedDay;



         //   dateButton.setText(myear1 + "-" + (mmonth1 + 1) + "-" + mday1);
            dat = myear1 + "-" + (mmonth1 + 1) + "-" + mday1;

            // set selected date into textview
//            tvDisplayDate.setText(new StringBuilder().append(mmonth1 + 1)
//                    .append("-").append(mday1).append("-").append(myear1)
//                    .append(" "));

        }
    };

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdateVenueStatusActivit.this);
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
                Toast.makeText(UpdateVenueStatusActivit.this,
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

                    Intent intent=new Intent(UpdateVenueStatusActivit.this, LoginActivity.class);

                    intent.putExtra("exit",exit);

                    startActivity(intent);
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
