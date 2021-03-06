package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Stage2StatusActivity extends Activity {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    private ProgressDialog pDialog;
    NetworkConnection networkConnection;

    JSONParser jsonParser = new JSONParser();

    byte[] imageBytes;
    String imageFilePath;
    ToggleButton toggleButton;

    private int mYear, mMonth, mDay, mHour, mMinute;

  //  Button dateButton,timeButton;

    String dat,tim;

    ToggleButton interestedToggle,awareToggle,warmToggle,demoToggle,interventionToggle;

    ToggleButton liveDemoToggle,ownerMetToggle;

    //Button captureButton;
    Button submitButton;

    int imageCount;

    String venueName;

    String venueNme,venueStts,userId,venueId,visitId,venueStatus,postVisitStatus,venueCmd,stageNumber;

    EditText commentEdittext;

  //  String currentDateandTime;

    int spcialStatus1 = 0,spcialStatus2 = 0,spcialStatus3 = 0,spcialStatus4 = 0,spcialStatus5 = 0;

  //  String scheduleDate;

    LinearLayout interestedLinear,awareLinear,warmLinear,demoLinear,interventionLinear;

    ImageView interestedOn,interestedNetrol,interestedOff,awareOn,awareNetrol,awareOff,warmOn,warmNetrol,
            warmOff,demoOn,demoNetrol,demoOff,interventionOn,interventionNetrol,interventionOff;

 //   Button agreementButton;

    ArrayList<HashMap<String, String>> venueStatusList;

    int flag1=0;

    TextView ownerMetTextview,liveDemoTextview;

    HashMap<String, String> venueHashmap;

    ArrayList<String> statuslist = new ArrayList<String>();

    ImageView backButton;

    String visitID = null;

    JSONArray venueid = null;

    boolean processClick=true;

    PopupMenu popupMenu;

    SharedPreferences sharedpreferences;

    double latitude = 0.0,longitude = 0.0;

   // String  venueStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stage2status);

      //  toggleButton = (ToggleButton)findViewById(R.id.status_toggle);

        liveDemoToggle = (ToggleButton)findViewById(R.id.live_demo_toggle);
        ownerMetToggle = (ToggleButton)findViewById(R.id.owner_toggle);

     //   dateButton = (Button)findViewById(R.id.date_button);
    //    timeButton = (Button)findViewById(R.id.time_Button);

        ownerMetTextview = (TextView)findViewById(R.id.ownermet_textview);
        liveDemoTextview = (TextView)findViewById(R.id.livedemo_textview);
    //    agreementButton = (Button)findViewById(R.id.agreement_button);

        commentEdittext = (EditText)findViewById(R.id.samecomment_edittext);

        backButton = (ImageView)findViewById(R.id.stage2back_imageview);

     //   captureButton = (Button)findViewById(R.id.capture_image_button);

        submitButton = (Button)findViewById(R.id.submit_button);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

   //     agreementButton.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //ArrayList<HashMap<String, String>> arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arraylist");

            venueNme = extras.getString("venueName");
          //  venueStatus = extras.getString("venueStatus");
            venueStatusList= (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("venueStatusList");

            venueStts = extras.getString("venueStatus");

            statuslist = extras.getStringArrayList("StatusList");

            latitude=extras.getDouble("venue_latitude");
            longitude=extras.getDouble("venue_longitude");


            if(statuslist.size()==2){
                flag1=1;

            }
            if(statuslist.size()==0){

                flag1=2;
            }

            for(HashMap<String, String> venue: venueStatusList) {


                // int retval=venueStatusList.indexOf("Owner Met");

                venueHashmap = venue;

                if (venue.containsValue("Owner Met")) {

                    if (venue.containsValue("Owner Met")) {

                    ownerMetToggle.setVisibility(View.INVISIBLE);
                    ownerMetTextview.setVisibility(View.INVISIBLE);

                        flag1=3;

                    }
                    else
                    {

                    ownerMetToggle.setVisibility(View.VISIBLE);
                    ownerMetTextview.setVisibility(View.VISIBLE);
                    }
                }

                else if (venue.containsValue("Commercial Negotiation")) {

                    if (venue.containsValue("Commercial Negotiation")) {
                        liveDemoToggle.setVisibility(View.INVISIBLE);
                        liveDemoTextview.setVisibility(View.INVISIBLE);
                        flag1=4;
                    }
                    else
                    {
                        liveDemoToggle.setVisibility(View.VISIBLE);
                        liveDemoTextview.setVisibility(View.VISIBLE);
                    }
                }

                else if (venue.containsValue("Template of Agreement Provided")) {

                    if (venue.containsValue("Template of Agreement Provided")) {
                      //  liveDemoToggle.setVisibility(View.INVISIBLE);
                       // liveDemoTextview.setVisibility(View.INVISIBLE);
                        flag1=5;
                  //      agreementButton.setVisibility(View.INVISIBLE);
                    }

                }

                else if (venue.containsValue("Agreement Accepted")) {

                    if (venue.containsValue("Agreement Accepted")) {
                        //  liveDemoToggle.setVisibility(View.INVISIBLE);
                        // liveDemoTextview.setVisibility(View.INVISIBLE);
                        flag1=6;
                 //       agreementButton.setVisibility(View.INVISIBLE);
                    }
                }
            }

            if(!statuslist.isEmpty()) {

            }

            userId = extras.getString("userId");
            visitId = extras.getString("visitId");
            venueId = extras.getString("venueId");

            postVisitStatus = extras.getString("postVisitStatus");
            stageNumber = extras.getString("stageNumber") ;

            if(flag1<=5 || flag1<=6 ){

         //       agreementButton.setVisibility(View.INVISIBLE);

                ownerMetToggle.setVisibility(View.INVISIBLE);
                ownerMetTextview.setVisibility(View.INVISIBLE);

                liveDemoToggle.setVisibility(View.INVISIBLE);
                liveDemoTextview.setVisibility(View.INVISIBLE);

            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Stage2StatusActivity.this, ScheduledVenueActivity.class);

                startActivity(intent);
                finish();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkConnection.isNetworkAvailable()) {
                    if (processClick) {

                        submitButton.setEnabled(false);
                        submitButton.setClickable(false);

                        for (int i = 0; i < venueStatusList.size(); i++) {

                            Log.e("qqqqq--", venueStatusList.get(i).toString());
                            venueStatus = venueStatusList.get(i).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
                        }
                        String venueImagePath;
                        Log.e("venueName))))", venueNme);
                        //    Log.e("postVisitStatus))))", postVisitStatus);

                        venueCmd = commentEdittext.getText().toString();

                        venueCmd = venueCmd.replaceAll("[^a-zA-Z0-9]", " ");

                        new UpdateVenueStatus1().execute();
                    }
                    processClick = false;
                }else{

                    Toast.makeText(Stage2StatusActivity.this,
                            "Network is not Available. Please Check Your Internet Connection ",
                            Toast.LENGTH_SHORT).show();
                }
            }
            });

//        captureButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 0);
//
//
//            }
//        });


//        dateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                // Launch Date Picker Dialog
//                DatePickerDialog dpd = new DatePickerDialog(Stage2StatusActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
////                                    Toast.makeText(getApplicationContext(),
////                                            "Date : " + dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year,
////                                            Toast.LENGTH_SHORT).show();
//                                //    dateButton.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                dateButton.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                                dat = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                                // Display Selected date in textbox
//                                //     txtDate.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                dpd.show();
//            }
//        });

//        timeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                mHour = c.get(Calendar.HOUR_OF_DAY);
//                mMinute = c.get(Calendar.MINUTE);
//
//                // Launch Time Picker Dialog
//                TimePickerDialog tpd = new TimePickerDialog(Stage2StatusActivity.this,
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//                                // Display Selected time in textbox
//                                //  txtTime.setText(hourOfDay + ":" + minute);
////                                    Toast.makeText(getApplicationContext(),
////                                            "Time : " + hourOfDay + ":" + minute,
////                                            Toast.LENGTH_SHORT).show();
//                                tim = hourOfDay + ":" + minute + ":00";
//
//                                timeButton.setText(hourOfDay + ":" + minute + ":00");
//                            }
//                        }, mHour, mMinute, false);
//                tpd.show();
//            }
//        });
    }

    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(Stage2StatusActivity.this, view);
        popupMenu.inflate(R.menu.menu_stage2status);
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

                        new CheckValidUser(Stage2StatusActivity.this).execute();



          //              startActivity(new Intent(Stage2StatusActivity.this, LoginActivity.class));

//                        finish();

                        //   startActivity(new Intent(ScheduledVenueActivity.this, ReportActivity.class));

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

        Activity mActivity;
        public CheckValidUser(Activity activity)
        {
            super();
            this.mActivity=activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Stage2StatusActivity.this);
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
            //    Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(Stage2StatusActivity.this,
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
            String exit="exit";
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


                    this.mActivity.finish();
                    //  startActivity(new Intent(UserActionActivity.this, LoginActivity.class));



                   // Intent intent=new Intent(Stage2StatusActivity.this, LoginActivity.class);

                  //  intent.putExtra("exit", exit);

                  //  startActivity(intent);

                   // android.os.Process.killProcess(android.os.Process.myPid());

                   // Stage2StatusActivity.this.finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }


    class UpdateVenueStatus1 extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Stage2StatusActivity.this);
            pDialog.setMessage("Upload the Vevue Status...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String visitStatus1=null,visitStatus2=null,visitStatus3=null,visitStatus4=null;

            if (networkConnection.isNetworkAvailable()) {

                if(statuslist !=null) {



                        for (int i = 0; i < statuslist.size(); i++) {

                            if (statuslist.size() == 1) {
                                Log.e("--" + 1 + "--", statuslist.get(0));
                                params.add(new BasicNameValuePair("venue_status", statuslist.get(0)));
                                params.add(new BasicNameValuePair("visit_status1", ""));

                            } else if (statuslist.size() == 2) {
                                Log.e("--" + 2 + "--", statuslist.get(0));
                                Log.e("--" + 2 + "--", statuslist.get(1));

                                params.add(new BasicNameValuePair("venue_status", statuslist.get(1)));
                                params.add(new BasicNameValuePair("visit_status1", statuslist.get(0)));



                            }

                        }
                }

                    try {

                        params.add(new BasicNameValuePair("name", venueNme));
                        params.add(new BasicNameValuePair("visit_id", visitId));
                        params.add(new BasicNameValuePair("venue_id", venueId));
                        params.add(new BasicNameValuePair("stage_number", stageNumber));
                        params.add(new BasicNameValuePair("user_id", userId));
                        params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));
                       // params.add(new BasicNameValuePair("last_visit", currentDateandTime));
                        params.add(new BasicNameValuePair("comment", venueCmd));

                        json = jsonParser.makeHttpRequest(ApplicationConstants.url_upload1_stage2,
                                "POST", params);
                    }
                    catch (Exception e){

                        Log.e("eeeeee--", e.toString());

                    }
            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(Stage2StatusActivity.this,
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

                    if(statuslist !=null) {


                        for (int i = 0; i < statuslist.size(); i++) {

                            if (statuslist.size() == 1) {
                                Log.e("--" + 1 + "--", statuslist.get(0));
                                venueStatus = statuslist.get(0);

                             //   params.add(new BasicNameValuePair("venue_status", statuslist.get(0)));
                            //    params.add(new BasicNameValuePair("visit_status1", ""));

                            } else if (statuslist.size() == 2) {
                                Log.e("--" + 2 + "--", statuslist.get(0));
                                Log.e("--" + 2 + "--", statuslist.get(1));

                                venueStatus = statuslist.get(1);

                             //   params.add(new BasicNameValuePair("venue_status", statuslist.get(1)));
                              //  params.add(new BasicNameValuePair("visit_status1", statuslist.get(0)));


                            }

                        }
                    }

//                    startActivity(new Intent(Stage2StatusActivity.this, ScheduledVenueActivity.class));
//                    finish();
                    if (networkConnection.isNetworkAvailable()) {


                        try {

                            new GetVisitId().execute();
                        }
                        catch (Exception e){

                            Log.e("login error",e.toString());

                        }

                    }



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class GetVisitId extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Stage2StatusActivity.this);
            pDialog.setMessage("Upload the Vevue Status...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            String visitStatus1=null,visitStatus2=null,visitStatus3=null,visitStatus4=null;

            if (networkConnection.isNetworkAvailable()) {


                try {

//                        venueNme = extras.getString("venueName");
//                        userId = extras.getString("userId");
//                        visitId = extras.getString("visitId");
//                        postVisitStatus = extras.getString("postVisitStatus");
//                        stageNumber = extras.getString("stageNumber");
//                        dat = extras.getString("dat");
//                        venueStatus = extras.getString("venueStatus");

                    params.add(new BasicNameValuePair("venue_name", venueNme));


                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_get_visit_id,
                            "POST", params);

                 //   Log.d("Create Response", json.toString());

                }
                catch (Exception e){

                    Log.e("eeeeee--", e.toString());

                }
                //  }



            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(Stage2StatusActivity.this,
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

                    venueid = json.getJSONArray(ApplicationConstants.TAG_VENUE_ID);

//                        startActivity(new Intent(Stage3StatusActivity.this, ScheduledVenueActivity.class));
//                        finish();

                    for(int i = 0; i < venueid.length(); i++ ){
                        JSONObject c = venueid.getJSONObject(i);
                        visitID = c.getString(ApplicationConstants.TAG_VISIT_ID);



                    }

                    if(visitID !=null && !visitID.isEmpty()) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Stage2StatusActivity.this);
                        alertDialogBuilder.setMessage("You want update Next Status");

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(Stage2StatusActivity.this, UpdateVenueStatusActivit.class);


                                intent.putExtra("venueName", venueNme);

                                intent.putExtra("visitId", visitID);
                                intent.putExtra("venueId", venueId);
                                intent.putExtra("userId", userId);
                                intent.putExtra("venue_latitude", latitude);
                                intent.putExtra("venue_longitude", longitude);

                                intent.putExtra("venueStatus", venueStatus);
                                intent.putExtra("stageNumber", stageNumber);

                                startActivity(intent);


                                finish();


                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Stage2StatusActivity.this, ScheduledVenueActivity.class);

                                intent.putExtra("venueName", venueNme);

                                startActivity(intent);


                                finish();

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                    else{
                        Intent intent = new Intent(Stage2StatusActivity.this, ScheduledVenueActivity.class);

                        intent.putExtra("venueName", venueNme);

                        startActivity(intent);


                        finish();

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

//    class UpdateVenueStatus extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(Stage1StatusActivity.this);
//            pDialog.setMessage("Upload the Image...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        /**
//         * Creating product
//         * */
//        protected String doInBackground(String... args) {
//
//            JSONObject json = null;
//            if (networkConnection.isNetworkAvailable()) {
//                Log.e("network1===", networkConnection.isNetworkAvailable() + "");
//
//                String imageString=args[0];
//
//                for(int i=venueStatusList.size()-1;i>0;i--) {
//
//
//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("name", venueNme));
//                    params.add(new BasicNameValuePair("venue_status", venueStatusList.get(i).get(ApplicationConstants.TAG_STATUS_DESCRIPTION)));
//                    params.add(new BasicNameValuePair("user_image", imageString));
//                    params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));
//                    params.add(new BasicNameValuePair("last_visit", currentDateandTime));
//                    params.add(new BasicNameValuePair("comment", venueCmd));
//                    params.add(new BasicNameValuePair("visit_status1", venueStatusList.get(i).get(ApplicationConstants.TAG_STATUS_DESCRIPTION)));
//                    params.add(new BasicNameValuePair("visit_status2", venueStatusList.get(i-1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION)));
//                    params.add(new BasicNameValuePair("visit_status3", venueStatusList.get(i-2).get(ApplicationConstants.TAG_STATUS_DESCRIPTION)));
//
//
//
//
//
//                    params.add(new BasicNameValuePair("spcialStatus1", spcialStatus1 + ""));
//                    params.add(new BasicNameValuePair("spcialStatus2", spcialStatus2 + ""));
//                    params.add(new BasicNameValuePair("spcialStatus3", spcialStatus3 + ""));
//                    params.add(new BasicNameValuePair("spcialStatus4", spcialStatus4 + ""));
//                    params.add(new BasicNameValuePair("spcialStatus5", spcialStatus5 + ""));
//
//                    params.add(new BasicNameValuePair("scheduleDate", scheduleDate));
//
//
//                    Log.e("parms post--", venueStatus);
//
//                    // getting JSON Object
//                    // Note that create product url accepts POST method
//                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_upload_image,
//                            "POST", params);
//
//                    // check log cat fro response
//                    Log.d("Create Response", json.toString());
//                }
//
//                // check for success tag
//                try {
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                    if (success == 1) {
//
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatusServerSync(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatusServerSync(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
//
//                        startActivity(new Intent(Stage1StatusActivity.this, ScheduledVenueActivity.class));
//                        finish();
//
//                    }
//
////                    else{
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatus(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatus(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
////
////                        startActivity(new Intent(UpdateVenueStatusActivity.this, ScheduledVenueActivity.class));
////                        finish();
////
////                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else {
//                pDialog.dismiss();
//                Looper.prepare();
//                Toast.makeText(Stage1StatusActivity.this,
//                        "Network is not Available. Please Check Your Internet Connection ",
//                        Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog once done
//            try {
//                if ((pDialog != null) && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//
//            } catch (final IllegalArgumentException e) {
//                // Handle or log or ignore
//            } catch (final Exception e) {
//                // Handle or log or ignore
//            } finally {
//                pDialog = null;
//            }
//        }
//    }

//    class UpdateVenueStatus extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(Stage1StatusActivity.this);
//            pDialog.setMessage("Upload the Image...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        /**
//         * Creating product
//         * */
//        protected String doInBackground(String... args) {
//
//            JSONObject json = null;
//            if (networkConnection.isNetworkAvailable()) {
//                Log.e("network1===", networkConnection.isNetworkAvailable() + "");
//
//                String imageString=args[0];
//
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("name", venueNme));
//                params.add(new BasicNameValuePair("venue_status", venueStts));
//                params.add(new BasicNameValuePair("user_image", imageString));
//                params.add(new BasicNameValuePair("postvisit_Status", venueStatus));
//                params.add(new BasicNameValuePair("last_visit", currentDateandTime));
//                params.add(new BasicNameValuePair("comment", venueCommend));
//
//                Log.e("parms post--",venueStatus);
//
//                // getting JSON Object
//                // Note that create product url accepts POST method
//                json = jsonParser.makeHttpRequest(ApplicationConstants.url_upload_image,
//                        "POST", params);
//
//                // check log cat fro response
//                Log.d("Create Response", json.toString());
//
//                // check for success tag
//                try {
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                    if (success == 1) {
//
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatusServerSync(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatusServerSync(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
//
//                        startActivity(new Intent(Stage1StatusActivity.this, ScheduledVenueActivity.class));
//                        finish();
//
//                    }
//
////                    else{
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueStatus(venueNme, scheduleVenueStatus);
////
////                        String venueId=PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                getVenueIdbyName(venueNme);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).
////                                updateVenueScheduleStatus(venueId);
////
////                        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).deleteImagePath(imageFilePath);
////
////                        startActivity(new Intent(UpdateVenueStatusActivity.this, ScheduledVenueActivity.class));
////                        finish();
////
////                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else {
//                pDialog.dismiss();
//                Looper.prepare();
//                Toast.makeText(Stage1StatusActivity.this,
//                        "Network is not Available. Please Check Your Internet Connection ",
//                        Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog once done
//            try {
//                if ((pDialog != null) && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//
//            } catch (final IllegalArgumentException e) {
//                // Handle or log or ignore
//            } catch (final Exception e) {
//                // Handle or log or ignore
//            } finally {
//                pDialog = null;
//            }
//        }
//    }


//    public static String encodeTobase64(Bitmap image)
//    {
//        Bitmap immagex=image;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
//
//        Log.e("LOOK", imageEncoded);
//        return imageEncoded;
//    }




//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if(data.getExtras().get("data")!=null) {
//
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//            String partFilename = currentDateFormat();
//            File outputFile = storeCameraPhotoInSDCard(bitmap, partFilename);
//
//            imageCount = Image.getInstance().getImageCount();
//            Log.e("imagecount", imageCount + "");
//
//            Log.e("outputFile====", outputFile.toString());
//
//            imageFilePath = outputFile.toString();
//
//            try {
//                int imagecount = Image.getInstance().getImageCount();
//                Log.e("array--", imagecount + "");
//
//                for (int i = imageCount; i <= imagecount; i++) {
//
//                    Image.getInstance().getImagePath().add(i, outputFile.toString());
////----------------------------
//
////                for(String imagePath : Image.getInstance().getImagePath()){
////
////                    Log.e("imagePath--",imagePath);
////
////                }
//
//
////-----------------------------
//
//                }
//                imageCount++;
//                Image.getInstance().setImageCount(imageCount);
//                Log.e("imageCount", imageCount + "");
//
//
//            } catch (Exception e) {
//                Log.e("eeee", e.toString());
//            }
//
//
//            //  ImageAttachment.getInstance().setImageBaseBytes(imageFilePath);
//
//            // display the image from SD Card to ImageView Control
////        imageFilename = "Site_" + partFilename + ".jpg";
////        //     Bitmap mBitmap = getImageFileFromSDCard(imageFilename);
////
////
//            imageBytes = getBytes(outputFile);
////
////
////
////        PurpleKnotDBHelper.getInstance(UpdateVenueStatusActivity.this).storeImage(venueNameEdittext.getText().toString(),
////                imageFilePath);
//
//
//            ImageView image = new ImageView(this);
//
//            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
//            image.setLayoutParams(layoutParams);
//
//            image.setImageBitmap(bmp);
//
//            //  image.setImageResource(R.drawable.YOUR_IMAGE_ID);
//
//            AlertDialog.Builder builder =
//                    new AlertDialog.Builder(this).
//                            setMessage("Cuptured image").
//                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).
//                            setView(image);
//            builder.create().show();
//        }
//    }

//    private byte[] getBytes(File filePath) {
//
//        Bitmap bm = BitmapFactory.decodeFile(filePath.toString());
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//        byte[] imageByte = baos.toByteArray();
//
//        return imageByte;
//    }
//
//
//    private File storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
//
//        File filepath= Environment.getExternalStorageDirectory();
//
//
//        File dir = new File(filepath.getAbsolutePath()+"/Purpleknot/Purpleknot Images/");
//
//        dir.mkdirs();
//
//        File outputFile = new File(dir, venueName + currentDate + ".jpg");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return outputFile;
//    }
//
//
    private String currentDateFormat(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }





}
