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
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.purpleknot1.Adapter.StatusAdapter;
import com.purpleknot1.Adapter.StatusArrayAdapter;
import com.purpleknot1.DTO.ImageAttachmentDto;
import com.purpleknot1.DTO.ImageDto;
import com.purpleknot1.DTO.StatusDto;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

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

public class UpdatePGVGStatusActivity extends Activity implements ConnectionCallbacks,
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

    EditText pgvgNameEdittext,venuecommendEdittext;

    String venueNme;

    File storageDirectory;

    Spinner scheduleVenueStatusSpinner;

    CheckBox notInterestCheckbox,interventionCheckbox,introductoryCheckbox,friendlyCheckbon;

    String pgName, visitId ,venueId, userId ,scheduleVenueStatus;

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

    double pgvg_latitude=0.0,pgvg_longitude=0.0;

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

    String latit,longit,pgvgId,pgvgCurrent,pgvgLoccap,userID,pgvgVisitid,pgvgstatus;

    String pgvgStatus;

    //Button dateButton;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_pgvg_status);

        pgvgNameEdittext=(EditText)findViewById(R.id.schedulepg_name_edittext);

        stageTextview = (TextView)findViewById(R.id.pgstage_textview);

        linear = (LinearLayout)findViewById(R.id.owner_toggle);
        nextButton = (Button)findViewById(R.id.pg_next_button);

        backButton=(ImageView)findViewById(R.id.updatepgstatus_back_imageview);

        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);

        relativeLayout = (ListView)findViewById(R.id.pgrelative);
        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (checkPlayServices()) {

            buildGoogleApiClient();
            createLocationRequest();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
         //   pgvgVisitid
            pgName = extras.getString("pgName");
            pgvg_latitude= extras.getDouble("pgvgLati");
            pgvg_longitude=extras.getDouble("pgvgLongi");
            userID = extras.getString("userId");
            pgvgId = extras.getString("pgvgId");
            pgvgCurrent = extras.getString("pgvgCurrent");
            pgvgLoccap = extras.getString("pgvgLoccap");
            pgvgVisitid = extras.getString("pgvgVisitid");
            pgvgstatus = extras.getString("pgvgstatus");


      //      pgvg_latitude =Double.parseDouble(latit);


          //  pgvg_longitude = Double.parseDouble(longit);
            Log.e("lati,longi##----",pgvg_latitude+"--"+pgvg_longitude);

            pgvgNameEdittext.setText(pgName);

            new DisplayStatus().execute();

        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ScheduledPgvgActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nextBtn = nextButton.getText().toString();

                if (nextBtn == "Submit") {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdatePGVGStatusActivity.this);
                    alertDialogBuilder.setMessage("All the Status are Updated");

                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {

                            Intent intent = new Intent(getApplicationContext(), UserActionActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

//                            Intent intent = new Intent(getApplicationContext(), UserActionActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();



                }
                else{

                distance = 0.0;

                Location locationA = new Location("point A");
                locationA.setLatitude(lati);
                locationA.setLongitude(longi);
                Location locationB = new Location("point B");
                locationB.setLatitude(pgvg_latitude);
                locationB.setLongitude(pgvg_longitude);
                distance += locationA.distanceTo(locationB);
                double kilometers = distance * 0.001;



                Log.e("lati,longi**----", pgvg_latitude + "--" + pgvg_longitude);

                Log.e("statuslist----", statuslist.size() + "");

                if (statusId == 1 || statusId == 2 || statusId == 3 || statusId == 4 || statusId == 5 ||
                        statusId == 6 || statusId == 7) {

                    Log.e("statusId----", "statusId");

                    if (distance <= 300) {

                        Log.e("distance----", "" + distance);

                        if (networkConnection.isNetworkAvailable()) {

                            if (statuslist.size() != 0) {

                                Log.e("yes----", pgvgStatus);

                                Log.e("lati,longi----", lati + "--" + longi);

                                Intent intent = new Intent(getApplicationContext(), PgvgStatusActivity.class);
                                intent.putExtra("pgName", pgName);
                                intent.putExtra("lati", lati);
                                intent.putExtra("longi", longi);
                                intent.putExtra("userID", userID);
                                intent.putExtra("pgvgId", pgvgId);
                                intent.putExtra("pgvgCurrent", pgvgCurrent);
                                intent.putExtra("pgvgLoccap", pgvgLoccap);
                                intent.putExtra("pgvgVisitid", pgvgVisitid);
                                intent.putExtra("pgvgstatus", pgvgStatus);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                finish();

                            } else {

                                Log.e("noo----", "Option");

                                Toast.makeText(UpdatePGVGStatusActivity.this,
                                        "Please Select the Option.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.e("networkConnection----", "not ");
                        }
                    } else {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdatePGVGStatusActivity.this);
                        alertDialogBuilder.setMessage("Please Update Pg/VG near by only");

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {

                                dialog.dismiss();
//                                Intent intent = new Intent(getApplicationContext(), UserActionActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                finish();

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        Log.e("distance----", "" + distance);
                    }
                }

                }




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

    protected void startLocationUpdates() {

        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
        catch (Exception e){

        }

    }

    protected void stopLocationUpdates() {

        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("TAG", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

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


        } else {

        }
    }

    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(UpdatePGVGStatusActivity.this, view);
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





    class DisplayStatus extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdatePGVGStatusActivity.this);
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
                params.add(new BasicNameValuePair("pg_name", pgName));

                json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_pg_status,
                        "POST", params);

                Log.d("Create Response", json.toString());

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdatePGVGStatusActivity.this,
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



                    stage = json.getJSONArray(ApplicationConstants.TAG_PG_STAGE);


                    for (int i = 0; i < stage.length(); i++) {
                        JSONObject c = stage.getJSONObject(i);

                        ScrollView scrl=new ScrollView(UpdatePGVGStatusActivity.this);
                        final LinearLayout ll=new LinearLayout(UpdatePGVGStatusActivity.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        scrl.addView(ll);



                        statusId = c.getInt(ApplicationConstants.TAG_PG_STATUS_id);
                        stageNumber = c.getString(ApplicationConstants.TAG_PG_STAGE_NUMBER);

                        String statusDescription = c.getString(ApplicationConstants.TAG_PG_STATUS_DESCRIPTION);

                        statusDto.setStatusName(statusDescription);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(ApplicationConstants.TAG_PG_STATUS_DESCRIPTION, statusDescription);

                        oslist.add(map);

                        alist.add(statusDescription);


                        m_parts.add(statusDto);

                        Log.e("Descrrrrr====*****", statusId+"");

                //        stageTextview.setText("Stage " + stageNumber);

                       // if(stageNumber.equals('1'))
                      //  {
                        if(statusId ==8){

                            stageTextview.setText("No Stage");
                            nextButton.setText("Submit");

                        }else

                            {

                                //       statusAdapter= new StatusAdapter(UpdateVenueStatusActivit.this, R.layout.column_status, m_parts);

                                SimpleAdapter adapter = new SimpleAdapter(UpdatePGVGStatusActivity.this, oslist, R.layout.column_pg_status,
                                        new String[]{ApplicationConstants.TAG_PG_STATUS_DESCRIPTION}, new int[]{R.id.pgstatus_texiview}) {
                                    @Override
                                    public View getView(int pos, View convertView, ViewGroup parent) {
                                        View v = convertView;
                                        if (v == null) {
                                            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            v = vi.inflate(R.layout.column_pg_status, null);
                                        }
                                        TextView tv = (TextView) v.findViewById(R.id.pgstatus_texiview);
                                        tv.setText(oslist.get(pos).get(ApplicationConstants.TAG_PG_STATUS_DESCRIPTION));


                                        final ToggleButton toggleButton = (ToggleButton) v.findViewById(R.id.pg_stage_toggle);

                                        toggleButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (toggleButton.isChecked()) {
                                                    LinearLayout rl = (LinearLayout) v.getParent();
                                                    TextView tv = (TextView) rl.findViewById(R.id.pgstatus_texiview);
                                                    Log.e("Text--", tv.getText().toString());
                                                    HashMap<String, String> map = new HashMap<String, String>();
                                                    map.put(ApplicationConstants.TAG_PG_STATUS_DESCRIPTION, tv.getText().toString());
                                                    venueStatusList.add(map);
                                                    statuslist.add(tv.getText().toString());

                                                    pgvgStatus = tv.getText().toString();

                                                    Log.e("pgvgStatusgbnfgf--", "---yes---" + pgvgStatus);

                                                    //stageTextview

                                                } else {

                                                    LinearLayout rl = (LinearLayout) v.getParent();
                                                    TextView tv = (TextView) rl.findViewById(R.id.pgstatus_texiview);
                                                    Log.e("Text--", tv.getText().toString());
                                                    HashMap<String, String> map = new HashMap<String, String>();

                                                    map.remove(tv.getText().toString());
                                                    venueStatusList.remove(map);
                                                    statuslist.remove(tv.getText().toString());
                                                    for (int i = 0; i < statuslist.size(); i++) {
                                                        Log.e("array---", statuslist.get(i).toString());
                                                    }
                                                }
                                            }
                                        });
                                        return v;
                                    }
                                };
                                relativeLayout.setAdapter(adapter);
                            }
                      //  }
                      //  else{

                       //     nextButton.setText("Submit");

                      //  }

                    }
                }
                else if(success == 0) {

                    try {
                        if ((pDialog != null) && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                    } catch (final Exception e) {
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
            pDialog = new ProgressDialog(UpdatePGVGStatusActivity.this);
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
                Toast.makeText(UpdatePGVGStatusActivity.this,
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

                    Intent intent=new Intent(UpdatePGVGStatusActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
