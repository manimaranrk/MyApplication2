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


public class Stage1StatusActivity extends Activity {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    private ProgressDialog pDialog;
    NetworkConnection networkConnection;

    JSONParser jsonParser = new JSONParser();

    byte[] imageBytes;
    String imageFilePath;
    ToggleButton toggleButton;

    private int mYear, mMonth, mDay, mHour, mMinute;

    Button dateButton,timeButton;

    String dat,tim;

    ToggleButton interestedToggle,awareToggle,warmToggle,demoToggle,interventionToggle;

    //Button captureButton;
    Button submitButton;

    int imageCount;

    String venueName;

    String venueNme, userId ,venueStatus,postVisitStatus,visitId,venueId,venueCmd,stageNumber;

    EditText commentEdittext;

    String currentDateandTime;

    int spcialStatus1 = 0,spcialStatus2 = 0,spcialStatus3 = 0,spcialStatus4 = 0,spcialStatus5 = 0;

    String scheduleDate;

    LinearLayout interestedLinear,awareLinear,warmLinear,demoLinear,interventionLinear;

    ImageView interestedOn,interestedNetrol,interestedOff,awareOn,awareNetrol,awareOff,warmOn,warmNetrol,
            warmOff,demoOn,demoNetrol,demoOff,interventionOn,interventionNetrol,interventionOff;

    //Button agreementButton;

    ArrayList<HashMap<String, String>> venueStatusList;

    ArrayList<HashMap<String, String>> displayStatus;

    ArrayList<String> statuslist = new ArrayList<String>();

    String specialStatus1,specialStatus2,specialStatus3,specialStatus4,specialStatus5;

    String special1Status,special2Status,special3Status,special4Status,special5Status;

    TextView interestedTextview,awareTextview,warmTextview,demoTextview,interventionTextview;

    int flag1=0,flag2=0;

    LinearLayout linear1,l1,l2,l3,l4,l5;

    ImageView backButton;

    String visitID = null;

    JSONArray venueid = null;

    boolean processClick=true;

    PopupMenu popupMenu;

    SharedPreferences sharedpreferences;

    double latitude = 0.0,longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stage1_status);

      //  toggleButton = (ToggleButton)findViewById(R.id.status_toggle);




        //dateButton = (Button)findViewById(R.id.date_button);
        //timeButton = (Button)findViewById(R.id.time_Button);
        interestedTextview = (TextView)findViewById(R.id.interested_textview);
        awareTextview = (TextView)findViewById(R.id.aware_textview);
        warmTextview = (TextView)findViewById(R.id.warmly_textview);
        demoTextview = (TextView)findViewById(R.id.demo_textview);
        interventionTextview =(TextView)findViewById(R.id.intervention_textview);


        interestedToggle = (ToggleButton)findViewById(R.id.interested_toggle);
        awareToggle = (ToggleButton)findViewById(R.id.aware_toggle);
        warmToggle = (ToggleButton)findViewById(R.id.warm_toggle);
        demoToggle = (ToggleButton)findViewById(R.id.demo_toggle);
        interventionToggle = (ToggleButton)findViewById(R.id.intervention_toggle);

        backButton =(ImageView)findViewById(R.id.stage1back_imageview);

        linear1 = (LinearLayout)findViewById(R.id.linear_toggle);

        l1 = (LinearLayout) linear1.findViewById(R.id.interested_linear);

        l2 = (LinearLayout) linear1.findViewById(R.id.interested_linear1);

        l3 = (LinearLayout) linear1.findViewById(R.id.interested_linear3);
        l4 = (LinearLayout) linear1.findViewById(R.id.interested_linear4);
        l5 = (LinearLayout) linear1.findViewById(R.id.interested_linear5);

       // agreementButton = (Button)findViewById(R.id.agreement_button);

        commentEdittext = (EditText)findViewById(R.id.samecomment_edittext);

     //   captureButton = (Button)findViewById(R.id.capture_image_button);

        submitButton = (Button)findViewById(R.id.submit_button);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentDateandTime = sdf.format(new Date());

        Log.e("currentDateandTime",currentDateandTime);

      //  agreementButton.setVisibility(View.INVISIBLE);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //ArrayList<HashMap<String, String>> arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arraylist");

            venueNme = extras.getString("venueName");
            visitId = extras.getString("visitId");
            userId = extras.getString("userId");
            venueId = extras.getString("venueId");

            latitude=extras.getDouble("venue_latitude");
            longitude=extras.getDouble("venue_longitude");

          //  venueStatus = extras.getString("venueStatus");
            displayStatus= (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("displayStatus");

            statuslist = extras.getStringArrayList("venueStatusList");

            special1Status = extras.getString("specialStatus1");
            special2Status = extras.getString("specialStatus2");
            special3Status = extras.getString("specialStatus3");
            special4Status = extras.getString("specialStatus4");
            special5Status = extras.getString("specialStatus5");

            ArrayList<String> resList = new ArrayList<String>();
            String searchString = "Demo Given";
            String searchString1 = "Initiated";
            String searchString2 = "Manager Met";

            for (HashMap<String, String> curVal : displayStatus){

                if(displayStatus.size()==1) {

//                    if (curVal.containsValue(searchString) && !curVal.containsValue(searchString1) && !curVal.containsValue(searchString2)) {

                    if (curVal.containsValue(searchString)) {
                        // resList.add(curVal);
                        flag1 = 1;
                    }
                    if (curVal.containsValue(searchString2)) {
                        // resList.add(curVal);
                        flag1 = 1;
                    }
                }
            }

            try {


                if (special1Status.equals("1") && flag1 == 1) {

                    linear1.removeView(l1);
                    //  linear1.removeView(l2);
                    // linear1.removeView(l3);
                    //  linear1.removeView(l4);
                    //  linear1.removeView(l5);

                    interestedTextview.setVisibility(View.INVISIBLE);
                    interestedToggle.setVisibility(View.INVISIBLE);
                }

                if (special2Status.equals("1") && flag1 == 1) {

                    //  linear1.removeView(l1);
                    linear1.removeView(l2);
                    // linear1.removeView(l3);
                    //  linear1.removeView(l4);
                    //  linear1.removeView(l5);

                    awareTextview.setVisibility(View.INVISIBLE);
                    awareToggle.setVisibility(View.INVISIBLE);
                }

                if (special3Status.equals("1") && flag1 == 1) {

                    //   linear1.removeView(l1);
                    //  linear1.removeView(l2);
                    linear1.removeView(l3);
                    //  linear1.removeView(l4);
                    //  linear1.removeView(l5);

                    warmTextview.setVisibility(View.INVISIBLE);
                    warmToggle.setVisibility(View.INVISIBLE);
                }

                if (special4Status.equals("1") && flag1 == 1 || flag2 == 1) {

                    // linear1.removeView(l1);
                    //  linear1.removeView(l2);
                    // linear1.removeView(l3);
                    linear1.removeView(l4);
                    //  linear1.removeView(l5);

                    demoTextview.setVisibility(View.INVISIBLE);
                    demoToggle.setVisibility(View.INVISIBLE);
                }

//            if(flag1 == 1 ){
//
//                linear1.removeView(l4);
//                //  linear1.removeView(l5);
//
//                demoTextview.setVisibility(View.INVISIBLE);
//                demoToggle.setVisibility(View.INVISIBLE);
//
//            }


                if (special5Status.equals("1") && flag1 == 1) {

                    //  linear1.removeView(l1);
                    //  linear1.removeView(l2);
                    //  linear1.removeView(l3);
                    //  linear1.removeView(l4);
                    linear1.removeView(l5);

                    interventionTextview.setVisibility(View.INVISIBLE);
                    interventionToggle.setVisibility(View.INVISIBLE);
                }

                for (String curVal : statuslist) {
                    if (curVal.contains(searchString)) {
                        // resList.add(curVal);

                        interestedTextview.setVisibility(View.INVISIBLE);
                        interestedToggle.setVisibility(View.INVISIBLE);

                        awareTextview.setVisibility(View.INVISIBLE);
                        awareToggle.setVisibility(View.INVISIBLE);

                        warmTextview.setVisibility(View.INVISIBLE);
                        warmToggle.setVisibility(View.INVISIBLE);

                        demoTextview.setVisibility(View.INVISIBLE);
                        demoToggle.setVisibility(View.INVISIBLE);


                        linear1.removeView(l1);
                        linear1.removeView(l2);
                        linear1.removeView(l3);
                        linear1.removeView(l4);


                    }
                }
            }catch (Exception e){

                Log.e("eeee",e.toString());
            }

            postVisitStatus = extras.getString("postVisitStatus");
            stageNumber = extras.getString("stageNumber") ;

//            if(stageNumber.equals("2")){
//
//                agreementButton.setVisibility(View.VISIBLE);
//
//            }

            // Log.e("post visit",venueStatus);
            //   Log.e("post venueDate",venueDate);





//            if(venueStatus!="null") {
//
//                scheduleVenueStatusSpinner.setSelection(getIndex(scheduleVenueStatusSpinner, venueStatus));
//            }
        }


//        agreementButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Stage1StatusActivity.this, AgreementDetailsActivity.class);
//                intent.putExtra("venueName", venueNme);
//                intent.putExtra("venueStatus", venueStatus);
//                intent.putExtra("postVisitStatus", postVisitStatus);
//                startActivity(intent);
//                finish();
//
//            }
//        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Stage1StatusActivity.this, ScheduledVenueActivity.class);

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


                        //  submitButton.setEnabled(false);

//                for(int i = 0; i < statuslist.size(); i++ ) {
//
//                    //   for(int i = venueStatusList.size()-1; i > 0 ; i--){
//
//                    Log.e("qqqqq--", statuslist.get(i).toString());
//                    venueStatus = venueStatusList.get(i).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//
//
//
//
//                }


                        //    Log.e("venueStatus--",venueStatus);

                        if (interestedToggle.getVisibility() == View.VISIBLE && interestedToggle.isChecked()) {

                            spcialStatus1 = 1;

                        } else if (interestedToggle.getVisibility() == View.VISIBLE && !interestedToggle.isChecked()) {
                            spcialStatus1 = 0;
                        } else if (interestedToggle.getVisibility() == View.INVISIBLE) {
                            spcialStatus1 = Integer.parseInt(special1Status);
                        }


                        if (awareToggle.getVisibility() == View.VISIBLE && awareToggle.isChecked()) {

                            spcialStatus2 = 1;

                        } else if (awareToggle.getVisibility() == View.VISIBLE && !awareToggle.isChecked()) {
                            spcialStatus2 = 0;
                        } else if (awareToggle.getVisibility() == View.INVISIBLE) {
                            spcialStatus2 = Integer.parseInt(special2Status);
                        }


                        if (warmToggle.getVisibility() == View.VISIBLE && warmToggle.isChecked()) {

                            spcialStatus3 = 1;

                        } else if (warmToggle.getVisibility() == View.VISIBLE && !warmToggle.isChecked()) {
                            spcialStatus3 = 0;
                        } else if (warmToggle.getVisibility() == View.INVISIBLE) {
                            spcialStatus3 = Integer.parseInt(special3Status);
                        }


                        if (demoToggle.getVisibility() == View.VISIBLE && demoToggle.isChecked()) {

                            spcialStatus4 = 1;

                        } else if (demoToggle.getVisibility() == View.VISIBLE && !demoToggle.isChecked()) {
                            spcialStatus4 = 0;
                        } else if (demoToggle.getVisibility() == View.INVISIBLE) {
                            spcialStatus4 = Integer.parseInt(special4Status);
                        }


                        if (interventionToggle.getVisibility() == View.VISIBLE && interventionToggle.isChecked()) {

                            spcialStatus5 = 1;

                        } else if (interventionToggle.getVisibility() == View.VISIBLE && !interventionToggle.isChecked()) {
                            spcialStatus5 = 0;
                        } else if (interventionToggle.getVisibility() == View.INVISIBLE) {
                            spcialStatus5 = Integer.parseInt(special5Status);
                        }

                        //scheduleDate = dat+" "+tim;

                        String venueImagePath;
                        Log.e("venueName))))", venueNme);
                        Log.e("postVisitStatus))))", postVisitStatus);


                        // if (Image.getInstance().getImagePath().size() != 0) {

                        //   for (String imagePath : Image.getInstance().getImagePath()) {

                        //      Log.e("imagePathhhhh --", imagePath);

                        //      if (imagePath != null) {

                        //        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                        //      venueCmd = commentEdittext.getText().toString();
                        //     String imageString = encodeTobase64(myBitmap);
                        //     new UpdateVenueStatus().execute(imageString);
                        // }
                        //     }
                        // } else {
                        venueCmd = commentEdittext.getText().toString();

                        venueCmd = venueCmd.replaceAll("[^a-zA-Z0-9]", " ");

                        Log.e("venueCmd1",venueCmd);

                        new UpdateVenueStatus1().execute();
                        //  }
                        //}


                    }

                    processClick = false;
                }else{

                    Toast.makeText(Stage1StatusActivity.this,
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
//                DatePickerDialog dpd = new DatePickerDialog(Stage1StatusActivity.this,
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
//                TimePickerDialog tpd = new TimePickerDialog(Stage1StatusActivity.this,
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

        popupMenu = new PopupMenu(Stage1StatusActivity.this, view);
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

                        Log.e("-logout-","-logout-");

                        new CheckValidUser().execute();

              //          startActivity(new Intent(Stage1StatusActivity.this, LoginActivity.class));

                    //    finish();

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

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Stage1StatusActivity.this);
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
                Toast.makeText(Stage1StatusActivity.this,
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

                    Intent intent=new Intent(Stage1StatusActivity.this, LoginActivity.class);

                    intent.putExtra("exit",exit);

                    startActivity(intent);
                    finish();


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
            pDialog = new ProgressDialog(Stage1StatusActivity.this);
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


                if(!statuslist.isEmpty()) {


                    for(int i = 0 ; i < statuslist.size() ; i++){

                        if(statuslist.size() == 1) {
                            Log.e("--" + 1 + "--", statuslist.get(0));

                            params.add(new BasicNameValuePair("venue_status", statuslist.get(0)));

                            params.add(new BasicNameValuePair("visit_status3", ""));


                            params.add(new BasicNameValuePair("visit_status2", ""));

                            params.add(new BasicNameValuePair("visit_status1", ""));
                        }
                        else if(statuslist.size() == 2) {
                            Log.e("--" + 2 + "--", statuslist.get(0));
                            Log.e("--" + 2 + "--", statuslist.get(1));


                            params.add(new BasicNameValuePair("venue_status", statuslist.get(1)));

                            params.add(new BasicNameValuePair("visit_status3", statuslist.get(0)));


                            params.add(new BasicNameValuePair("visit_status2", ""));

                            params.add(new BasicNameValuePair("visit_status1", ""));

                        }
                        else if(statuslist.size() == 3) {
                            Log.e("--" + 3 + "--", statuslist.get(0));
                            Log.e("--" + 3 + "--", statuslist.get(1));
                            Log.e("--" + 3 + "--", statuslist.get(2));


                            params.add(new BasicNameValuePair("venue_status", statuslist.get(2)));

                            params.add(new BasicNameValuePair("visit_status3", statuslist.get(1)));


                            params.add(new BasicNameValuePair("visit_status2", statuslist.get(0)));

                            params.add(new BasicNameValuePair("visit_status1", ""));


                        }
                        else if(statuslist.size() == 4) {
                            Log.e("--" + 4 + "--", statuslist.get(0));
                            Log.e("--" + 4 + "--", statuslist.get(1));
                            Log.e("--" + 4 + "--", statuslist.get(2));
                            Log.e("--" + 4 + "--", statuslist.get(3));

                            params.add(new BasicNameValuePair("venue_status", statuslist.get(3)));

                            params.add(new BasicNameValuePair("visit_status3", statuslist.get(2)));


                            params.add(new BasicNameValuePair("visit_status2", statuslist.get(1)));

                            params.add(new BasicNameValuePair("visit_status1", statuslist.get(0)));
                        }

                    }


//                    for (int i = 0; i < venueStatusList.size(); i++) {
//
//                        if(venueStatusList.size() ==1){
//                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus1));
//
//                            params.add(new BasicNameValuePair("visit_status3", ""));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", ""));
//
//                            params.add(new BasicNameValuePair("visit_status1", ""));
//
//                        }
//                        else if(venueStatusList.size() ==2){
//
//                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                            visitStatus2=venueStatusList.get(1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus1));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", ""));
//
//                            params.add(new BasicNameValuePair("visit_status1", ""));
//
//
//                        }
//                        else if(venueStatusList.size() ==3){
//
//                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                            visitStatus2=venueStatusList.get(1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                            visitStatus3=venueStatusList.get(2).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus3));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus1));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status1", ""));
//
//                        }
//                        else if(venueStatusList.size() ==4){
//
//                            visitStatus1=venueStatusList.get(0).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                            visitStatus2=venueStatusList.get(1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                            visitStatus3=venueStatusList.get(2).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                            visitStatus4=venueStatusList.get(3).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus4));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus3));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus1));
//
//
//
//                        }
//
//                    }

//
//                    for(Map<String, String> map : venueStatusList)
//                    {
//                        String tagName = map.get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                        Log.e("tagName--",tagName);
//
//                        if(venueStatusList.size()==1){
//
//                            visitStatus1 = tagName;
//
//                        }
//
//
//
//                    }


//                    for (int i = venueStatusList.size()-1 ; i > 0; i--) {
//
//
//
//                   //     Log.e("IIIIIIIII---"+i+"-----",venueStatusList.get(i).get(ApplicationConstants.TAG_STATUS_DESCRIPTION));
//
//                        if (i == 1) {
//                            visitStatus1 = venueStatusList.get(1).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                        }
////                    else{
////                        visitStatus4="";
////                    }
//
//                        else if (i == 2) {
//                            visitStatus2 = venueStatusList.get(2).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                        }
////                    else{
////                        visitStatus1="";
////                    }
//
//
//                        else if (i == 3) {
//                            visitStatus3 = venueStatusList.get(3).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                        }
////                    else{
////                        visitStatus2=null;
////                    }
//
//                        else if (i == 4) {
//                            visitStatus4 = venueStatusList.get(4).get(ApplicationConstants.TAG_STATUS_DESCRIPTION);
//                        }
////                    else{
////                        visitStatus3="";
////                    }
//
//
//                    }
                }

                    try {

                     //   Log.e(i + "", venueStatusList.get(i).get(ApplicationConstants.TAG_STATUS_DESCRIPTION));




                        params.add(new BasicNameValuePair("name", venueNme));
                        params.add(new BasicNameValuePair("visit_id", visitId));
                        params.add(new BasicNameValuePair("venue_id", venueId));
                        params.add(new BasicNameValuePair("user_id", userId));
                        params.add(new BasicNameValuePair("stage_number", stageNumber));




                        params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));
                      //  params.add(new BasicNameValuePair("last_visit", currentDateandTime));
                        params.add(new BasicNameValuePair("comment", venueCmd));

//                        if(visitStatus4 == null && visitStatus3 == null && visitStatus2 ==null && visitStatus1 != null ){
//                            params.add(new BasicNameValuePair("venue_status", visitStatus1));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus3));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus4));
//
//                        }
//
//                        else if(visitStatus4 != null && visitStatus3 == null && visitStatus2 ==null && visitStatus1 == null){
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus4));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus1));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus3));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus2));
//
//                        }
//
//                        else if(visitStatus4 == null && visitStatus3 != null && visitStatus2 ==null && visitStatus1 == null){
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus3));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus4));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus1));
//
//                        }
//
//                        else if(visitStatus4 == null && visitStatus3 == null && visitStatus2 !=null && visitStatus1 == null){
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus1));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus4));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus3));
//
//                        }
//
//                        else if(visitStatus4 == null && visitStatus3 == null && visitStatus2 !=null && visitStatus1 != null){
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus1));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus4));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus3));
//
//                        }
//
//                        else if(visitStatus4 == null && visitStatus3 != null && visitStatus2 !=null && visitStatus1 != null){
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus3));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus2));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus1));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus4));
//
//                        }
//
//                        else if(visitStatus4 != null && visitStatus3 != null && visitStatus2 !=null && visitStatus1 != null){
//
//                            params.add(new BasicNameValuePair("venue_status", visitStatus4));
//
//                            params.add(new BasicNameValuePair("visit_status3", visitStatus3));
//
//
//                            params.add(new BasicNameValuePair("visit_status2", visitStatus2));
//
//                            params.add(new BasicNameValuePair("visit_status1", visitStatus1));
//
//                        }






                        params.add(new BasicNameValuePair("spcialStatus1", spcialStatus1 + ""));
                        params.add(new BasicNameValuePair("spcialStatus2", spcialStatus2 + ""));
                        params.add(new BasicNameValuePair("spcialStatus3", spcialStatus3 + ""));
                        params.add(new BasicNameValuePair("spcialStatus4", spcialStatus4 + ""));
                        params.add(new BasicNameValuePair("spcialStatus5", spcialStatus5 + ""));


                     //   params.add(new BasicNameValuePair("scheduleDate", scheduleDate));


                        json = jsonParser.makeHttpRequest(ApplicationConstants.url_stage1_status_upload,
                                "POST", params);

                    //    Log.e("stage 1 json ",json.toString());
                    }
                    catch (Exception e){

                        Log.e("eeeeee--", e.toString());

                    }
              //  }



            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(Stage1StatusActivity.this,
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

                    if (networkConnection.isNetworkAvailable()) {


                        try {


                        new GetVisitId().execute();

                        }
                        catch (Exception e){

                            Log.e("visit error",e.toString());

                        }

                    }



//                    startActivity(new Intent(Stage1StatusActivity.this, ScheduledVenueActivity.class));
//                    finish();
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
            pDialog = new ProgressDialog(Stage1StatusActivity.this);
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

                //    Log.d("Create Response", json.toString());

                }
                catch (Exception e){

                    Log.e("eeeeee--", e.toString());

                }
                //  }



            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(Stage1StatusActivity.this,
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


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Stage1StatusActivity.this);
                        alertDialogBuilder.setMessage("You want update Next Status");

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(Stage1StatusActivity.this, UpdateVenueStatusActivit.class);


                                intent.putExtra("venueName", venueNme);

                                intent.putExtra("venue_latitude", latitude);
                                intent.putExtra("venue_longitude", longitude);

                                intent.putExtra("visitId", visitID);
                                intent.putExtra("venueId", venueId);
                                intent.putExtra("userId", userId);

//                            special1Status = extras.getString("specialStatus1");
//                            special2Status = extras.getString("specialStatus2");
//                            special3Status = extras.getString("specialStatus3");
//                            special4Status = extras.getString("specialStatus4");
//                            special5Status = extras.getString("specialStatus5");

                                intent.putExtra("specialStatus1", special1Status);
                                intent.putExtra("specialStatus2", special2Status);
                                intent.putExtra("specialStatus3", special3Status);
                                intent.putExtra("specialStatus4", special4Status);
                                intent.putExtra("specialStatus5", special5Status);

                                intent.putExtra("venueStatus", statuslist.get(0));
                                intent.putExtra("stageNumber", stageNumber);

                                startActivity(intent);


                                finish();


                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Stage1StatusActivity.this, ScheduledVenueActivity.class);

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
                        Intent intent = new Intent(Stage1StatusActivity.this, ScheduledVenueActivity.class);

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
