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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.purpleknot1.Adapter.PGVGViewPagerAdapter;
import com.purpleknot1.Adapter.ScheduleVenueArrayAdapter;
import com.purpleknot1.Adapter.ViewPagerAdapter;
import com.purpleknot1.DTO.ScheduleVenueListDto;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.City;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.State;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ScheduledPgvgActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    int flag=0;

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    Spinner stateSpinner,citySpinner,zoneSpinner;

    JSONParser jsonParser = new JSONParser();

    JSONArray pgvg = null;

    //ListView scheduleListview;

    ScheduleVenueArrayAdapter scheduleVenueArrayAdapter;

    ScheduleVenueListDto scheduleVenueListDto;

    ImageView menuImageview,refreshImageview,backImageview;

    TextView venueIdTextview,venueNameTextview,venueStatusTextview,venueDateTextview;

    PopupMenu popupMenu;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

  //  ViewPager viewPager;
    PagerAdapter adapter1;

    ListView pgvgListView;

    JSONArray state = null;
    JSONArray city = null;
    JSONArray zone = null;

    String stateName,cityName ,zoneName;
    List<String> stateList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();
    List<String> zoneList = new ArrayList<String>();

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

    double distance=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pgvg_exists);

        menuImageview=(ImageView)findViewById(R.id.pgvg_schedulemenu_imageview);
      //  viewPager = (ViewPager) findViewById(R.id.pgvgpager);

        pgvgListView = (ListView)findViewById(R.id.pgvg_listview);

        refreshImageview=(ImageView) findViewById(R.id.pgvgrefresh_imageview);

        backImageview = (ImageView) findViewById(R.id.pgvg_schedule_back_imageview);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        backImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ScheduledPgvgActivity.this, PgVgTypeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        if (networkConnection.isNetworkAvailable())
        {

            new SelectSchedulePgVg().execute();

        }
        else
        {

        }

        refreshImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        pgvgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                final TextView pgvg_name = (TextView) view.findViewById(R.id.pgvg_name_textview);
                TextView pgvg_lati = (TextView) view.findViewById(R.id.pgvg_lati_textview);
                TextView pgvg_longi = (TextView) view.findViewById(R.id.pgvg_longi_textview);
                TextView pgvg_userid = (TextView) view.findViewById(R.id.pgvg_userid_textview);
                TextView pgvg_id = (TextView) view.findViewById(R.id.pgvg_id_textview);
                TextView pgvg_current = (TextView) view.findViewById(R.id.pgvg_current_textview);
                TextView pgvg_loccap = (TextView) view.findViewById(R.id.pgvg_loccap_textview);
                TextView pgvg_visit_id = (TextView) view.findViewById(R.id.pgvg_visitid_textview);
                TextView pgvg_status = (TextView) view.findViewById(R.id.pgvg_ststus_textview);

         //       String lati=pgvg_lati.getText().toString();
        //        String longi=pgvg_longi.getText().toString();
                String userId=pgvg_userid.getText().toString();
                String pgvgId= pgvg_id.getText().toString();
                String pgvgCurrent=pgvg_current.getText().toString();
                String pgvgLoccap=pgvg_loccap.getText().toString();
                String pgvgVisitid=pgvg_visit_id.getText().toString();
                String pgvgstatus=pgvg_status.getText().toString();

            if(pgvg_loccap.getText().toString().equals("1")) {

                Double pgvg_latitude = Double.valueOf(pgvg_lati.getText().toString());
                Double pgvg_longitude = Double.valueOf(pgvg_longi.getText().toString());

          //      Log.e("latiiii===",lati);
          //      Log.e("longiiii===",longi);

                distance = 0.0;

                Location locationA = new Location("point A");
                locationA.setLatitude(lati);
                locationA.setLongitude(longi);
                Location locationB = new Location("point B");
                locationB.setLatitude(pgvg_latitude);
                locationB.setLongitude(pgvg_longitude);
                distance += locationA.distanceTo(locationB);
                double kilometers = distance * 0.001;

                Log.e("lati,longi----", lati + "--" + longi);
                Log.e("pgvg_lati,pgvg_long----", pgvg_latitude + "--" + pgvg_longitude);



                    if (distance <= 300) {

                        Intent intent = new Intent(getApplicationContext(), UpdatePGVGStatusActivity.class);
                        intent.putExtra("pgName", pgvg_name.getText().toString());
                        intent.putExtra("pgvgLati", pgvg_latitude);
                        intent.putExtra("pgvgLongi", pgvg_longitude);
                        intent.putExtra("userId", userId);
                        intent.putExtra("pgvgId", pgvgId);
                        intent.putExtra("pgvgCurrent", pgvgCurrent);
                        intent.putExtra("pgvgLoccap", pgvgLoccap);
                        intent.putExtra("pgvgVisitid", pgvgVisitid);
                        intent.putExtra("pgvgstatus", pgvgstatus);

                        startActivity(intent);

                    } else {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduledPgvgActivity.this);
                        alertDialogBuilder.setMessage("Please Update Pg/VG near by only");

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {

                                dialog.dismiss();

//                            Intent intent = new Intent(getApplicationContext(), UserActionActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        Log.e("distance----", "" + distance);
                    }
                }
                else
                {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduledPgvgActivity.this);
                    alertDialogBuilder.setMessage("Please Update Pg/VG near by only");

                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {

                            Intent intent= new Intent(ScheduledPgvgActivity.this,UpdatePGGPSDetailsActivity.class);
                            intent.putExtra("pgname",pgvg_name.getText().toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                    Log.e("distance----", "" + distance);

//                    Intent intent= new Intent(ScheduledPgvgActivity.this,UpdateVenueDetailsActivity.class);
//                    intent.putExtra("venueName",pgvg_name.getText().toString());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();

                }


                //Toast.makeText(getApplicationContext(),
               //        pagetitle.getText().toString(), Toast.LENGTH_SHORT).show();
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

            mRequestingLocationUpdates = true;

            startLocationUpdates();

        } else {

            mRequestingLocationUpdates = false;

            stopLocationUpdates();

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

            Log.e("exception---++++-----",e.toString());

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

            //  lblLocation.setText(latitude + ", " + longitude);

        } else {

            // lblLocation
            //         .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduledPgvgActivity.this);
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
                Toast.makeText(ScheduledPgvgActivity.this,
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

                    Intent intent=new Intent(ScheduledPgvgActivity.this, LoginActivity.class);
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

    class SelectSchedulePgVg extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduledPgvgActivity.this);
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

                    //      String cityId = Employee.getInstance().getCityId();

                    Log.e("userId--", userId);
                    //     Log.e("cityId--", cityId);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("user_id", userId));
                    //            params.add(new BasicNameValuePair("city_name", cityName));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_scheduledpgvg_userid1,
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

                        pgvg = json.getJSONArray(ApplicationConstants.TAG_PGHISTORY);

                        for (int i = 0; i < pgvg.length(); i++) {
                            JSONObject c = pgvg.getJSONObject(i);

                            String pgName = c.getString(ApplicationConstants.TAG_PG_NAME);
                            String pgVisitId = c.getString(ApplicationConstants.TAG_PG_VISIT_ID);
                            String pgId = c.getString(ApplicationConstants.TAG_PG_ID);
                            String pgUserId = c.getString(ApplicationConstants.TAG_PG_USER_ID);
                            String pgCurrentStatus = c.getString(ApplicationConstants.TAG_PG_CURRENT_STATUS);
                            String pgPostVisitedStatus = c.getString(ApplicationConstants.TAG_PG_POST_VISIT_STATUS);
                            String pgLocationCaptured = c.getString(ApplicationConstants.TAG_PG_LOCATION_CAPTURED);
                            String pgLatitude = c.getString(ApplicationConstants.TAG_PG_LATITUDE);
                            String pgLongitude = c.getString(ApplicationConstants.TAG_PG_LONGITUDE);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(ApplicationConstants.TAG_PG_NAME, pgName);
                            map.put(ApplicationConstants.TAG_PG_VISIT_ID, pgVisitId);
                            map.put(ApplicationConstants.TAG_PG_ID, pgId);
                            map.put(ApplicationConstants.TAG_PG_USER_ID, pgUserId);
                            map.put(ApplicationConstants.TAG_PG_CURRENT_STATUS, pgCurrentStatus);
                            map.put(ApplicationConstants.TAG_PG_POST_VISIT_STATUS, pgPostVisitedStatus);
                            map.put(ApplicationConstants.TAG_PG_LOCATION_CAPTURED, pgLocationCaptured);
                            map.put(ApplicationConstants.TAG_PG_LATITUDE, pgLatitude);
                            map.put(ApplicationConstants.TAG_PG_LONGITUDE, pgLongitude);

                            map.put(ApplicationConstants.TAG_PG_COUNT, i+1+"");

                            oslist.add(map);

                         //   Log.e("iiii--", i + 1 + "");
                          //  pgvg_id_texiview

                          //  String i1 = i+1+"";

                            String[] from = new String[] {ApplicationConstants.TAG_PG_COUNT,ApplicationConstants.TAG_PG_VISIT_ID,ApplicationConstants.TAG_PG_NAME,
                                    ApplicationConstants.TAG_PG_POST_VISIT_STATUS,ApplicationConstants.TAG_PG_LATITUDE,ApplicationConstants.TAG_PG_LONGITUDE,
                                    ApplicationConstants.TAG_PG_USER_ID,ApplicationConstants.TAG_PG_CURRENT_STATUS,ApplicationConstants.TAG_PG_LOCATION_CAPTURED,
                                    ApplicationConstants.TAG_PG_ID};

                            int[] to = new int[] { R.id.pgvgid_texiview,R.id.pgvg_visitid_textview,R.id.pgvg_name_textview,R.id.pgvg_ststus_textview,R.id.pgvg_lati_textview,
                                    R.id.pgvg_longi_textview,R.id.pgvg_userid_textview,R.id.pgvg_current_textview,R.id.pgvg_loccap_textview,R.id.pgvg_id_textview };

                            ListAdapter adapter = new SimpleAdapter(ScheduledPgvgActivity.this, oslist,
                                    R.layout.column_pgvg,from,to);

                            pgvgListView.setAdapter(adapter);

                        }

                        togglePeriodicLocationUpdates();

                    } else {
                    }
                }else{

                    finish();
                    //    startActivity(new Intent(ScheduledPgvgActivity.this, MandapamTypeActivity.class));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void pgvgSchMenuButtonClickEvent(View view){

        popupMenu = new PopupMenu(ScheduledPgvgActivity.this, view);
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