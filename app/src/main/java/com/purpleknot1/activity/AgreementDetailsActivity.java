package com.purpleknot1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.InputFilterMinMax;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AgreementDetailsActivity extends Activity {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    private ProgressDialog pDialog;
    NetworkConnection networkConnection;



    private int mYear, mMonth, mDay, mHour, mMinute;

    Button dateButton,timeButton;

    String dat,tim;



    Button submitButton,commitedRevenueButton,revenueShareButton,amountButton,percentageButton;

    boolean processClick=true;

    String venueName;

    String venueNme,venueStts,postVisitStatus,venueCmd;

    EditText mandapamNameEdittext,addressEdittext,ownerNameEdittext,fathersNameEdittext,ageEdittext;
  //  EditText emailidEdittext;

    String scheduleDate;

    String stageNumber,userId,visitId,venueId;

    String mandapamName,address,ownerName,fatherName,age,revenueModel = null,revenueModel1 = null,emailId;

    ToggleButton ownerMetToggle,liveDemoToggle;

    JSONParser jsonParser = new JSONParser();

    EditText amountEdittext,percentageEdittext;

    int liveStatus,ownerMet;

    ArrayList<HashMap<String, String>> venueStatusList;

    TextView ownerMetTextview,liveDemoTextview;

    ArrayList<String> statuslist = new ArrayList<String>();
    String currentStatus,visitStatus;

    ImageView backButton;

    String visitID = null;
    int liveDemo;

    JSONArray venueid = null;
    JSONArray livedemo = null;

    LinearLayout toggleLinear,liveDemoLinear,ownermetLinear;

    LinearLayout datetimeLinear,spacelinear1;

    PopupMenu popupMenu;

   // RelativeLayout agreementRelative;

    private int myear;
    private int mmonth;
    private int mday;

    private int myear1;
    private int mmonth1;
    private int mday1;

    static final int DATE_DIALOG_ID = 999;

    SharedPreferences sharedpreferences;

    double latitude = 0.0,longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.agrement_details);

        dateButton = (Button)findViewById(R.id.agreement_date_button);
        timeButton = (Button)findViewById(R.id.agrement_time_Button);

        mandapamNameEdittext = (EditText)findViewById(R.id.mandapam_name_edittext);
        addressEdittext = (EditText)findViewById(R.id.address_edittext);
        ownerNameEdittext = (EditText)findViewById(R.id.owner_name_edittext);
        fathersNameEdittext = (EditText)findViewById(R.id.father_name_edittext);
        ageEdittext  = (EditText)findViewById(R.id.age_edittext);

        backButton = (ImageView)findViewById(R.id.agreementback_imageview);

        spacelinear1 = (LinearLayout)findViewById(R.id.agrementspacelinear1);

        datetimeLinear  = (LinearLayout)findViewById(R.id.datetime_linear);


        toggleLinear = (LinearLayout)findViewById(R.id.aggrement_toggle_linear);


        ownermetLinear = (LinearLayout)toggleLinear.findViewById(R.id.ownermet_linear);
        liveDemoLinear = (LinearLayout)toggleLinear.findViewById(R.id.live_demo_linear);

        commitedRevenueButton = (Button)findViewById(R.id.commited_revenue_button);
        revenueShareButton = (Button)findViewById(R.id.revenue_share_button);
        amountEdittext = (EditText)findViewById(R.id.amount_button);
        percentageEdittext = (EditText)findViewById(R.id.percentage_button);
       // emailidEdittext = (EditText)findViewById(R.id.emailid_edittext);

        ownerMetToggle = (ToggleButton)findViewById(R.id.ownermet_toggle);
        liveDemoToggle = (ToggleButton)findViewById(R.id.live_demo_toggle);

        ownerMetTextview = (TextView)findViewById(R.id.owner_textview);
        liveDemoTextview = (TextView)findViewById(R.id.live_demo_textview);


        submitButton = (Button)findViewById(R.id.submit_button);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mandapamNameEdittext.setEnabled(false);


        amountEdittext.setVisibility(View.INVISIBLE);

        percentageEdittext.setVisibility(View.INVISIBLE);

        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);



        int flag1 = 0,flag2 = 0;




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            venueNme = extras.getString("venueName");
            venueStatusList= (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("venueStatusList");
            venueStts = extras.getString("venueStatus");
            postVisitStatus = extras.getString("postVisitStatus");
            stageNumber = extras.getString("stageNumber") ;

            latitude=extras.getDouble("venue_latitude");
            longitude=extras.getDouble("venue_longitude");

            userId = extras.getString("userId");

           // statuslist = extras.getStringArrayList("StatusList");
            statuslist = extras.getStringArrayList("StatusList");

            visitId = extras.getString("visitId");
            venueId = extras.getString("venueId");

         //   postVisitStatus = extras.getString("postVisitStatus");


            mandapamNameEdittext.setText(venueNme);





            for(HashMap<String, String> venue: venueStatusList) {


                // int retval=venueStatusList.indexOf("Owner Met");

                if (venue.containsValue("Owner Met")) {

                    if (venue.containsValue("Owner Met")) {

                        ownerMetToggle.setVisibility(View.INVISIBLE);
                        ownerMetTextview.setVisibility(View.INVISIBLE);
                        toggleLinear.removeView(ownermetLinear);
                       // agreementRelative.removeView(ownermetLinear);
                        flag1=0;

                    }
                    else
                    {

                        ownerMetToggle.setVisibility(View.VISIBLE);
                        ownerMetTextview.setVisibility(View.VISIBLE);
                        toggleLinear.addView(ownermetLinear);

                        flag1 = 1;
                    }


                }

                else if (venue.containsValue("Commercial Negotiation")) {

                    if (venue.containsValue("Commercial Negotiation") && !venue.containsValue("Owner Met")) {

                     //   liveDemoToggle.setVisibility(View.INVISIBLE);
                      //  liveDemoTextview.setVisibility(View.INVISIBLE);

                        dateButton.setVisibility(View.INVISIBLE);
                        timeButton.setVisibility(View.INVISIBLE);

                      //  toggleLinear.removeView(liveDemoLinear);
                        //agreementRelative.removeView(ownermetLinear);
                        ownerMetToggle.setVisibility(View.INVISIBLE);
                        ownerMetTextview.setVisibility(View.INVISIBLE);
                       // agreementRelative.removeView(ownermetLinear);
                        toggleLinear.removeView(ownermetLinear);
                        flag2=0;
                    }
                    else
                    {
                     //   liveDemoToggle.setVisibility(View.VISIBLE);
                    //    liveDemoTextview.setVisibility(View.VISIBLE);
                     //   toggleLinear.addView(liveDemoLinear);
                        flag2=1;
                    }


                }

            }



            if(!statuslist.isEmpty()) {


                for (int i = 0; i < statuslist.size(); i++) {

                    if (statuslist.size() == 1) {
                        Log.e("--" + 1 + "--", statuslist.get(0));
                        currentStatus = statuslist.get(0);


                    } else if (statuslist.size() == 2) {
                        Log.e("--" + 2 + "--", statuslist.get(0));
                        Log.e("--" + 2 + "--", statuslist.get(1));

                        currentStatus = statuslist.get(1);

                        visitStatus =  statuslist.get(0);



                    }

                    else if (statuslist.size() == 3) {
                        Log.e("--" + 2 + "--", statuslist.get(0));
                        Log.e("--" + 2 + "--", statuslist.get(1));
                        Log.e("--" + 2 + "--", statuslist.get(2));

                        currentStatus = statuslist.get(2);

                        visitStatus =  statuslist.get(1);



                    }



//                    } else if (statuslist.size() == 3) {
//                        Log.e("--" + 3 + "--", statuslist.get(0));
//                        Log.e("--" + 3 + "--", statuslist.get(1));
//                        Log.e("--" + 3 + "--", statuslist.get(2));
//
//
//
//
//
//                    } else if (statuslist.size() == 4) {
//                        Log.e("--" + 4 + "--", statuslist.get(0));
//                        Log.e("--" + 4 + "--", statuslist.get(1));
//                        Log.e("--" + 4 + "--", statuslist.get(2));
//                        Log.e("--" + 4 + "--", statuslist.get(3));
//
//                    }

                }
            }



        }


        new GetLiveDemo().execute();








        liveDemoToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (liveDemoToggle.isChecked()) {

                    dateButton.setVisibility(View.VISIBLE);
                    timeButton.setVisibility(View.VISIBLE);

                    //datetimeLinear.setVisibility(View.VISIBLE);

                }
                else{

                    dateButton.setVisibility(View.INVISIBLE);
                    timeButton.setVisibility(View.INVISIBLE);
                  //  datetimeLinear.setVisibility(View.INVISIBLE);
                }

            }
        });

       // percentageEdittext.addTextChangedListener(watch);

        ageEdittext.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "150")});

        percentageEdittext.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "101")});





        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AgreementDetailsActivity.this, ScheduledVenueActivity.class);

                startActivity(intent);
                finish();
            }
        });


        commitedRevenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                amountEdittext.setVisibility(View.VISIBLE);


                percentageEdittext.setVisibility(View.INVISIBLE);
                percentageEdittext.setText("");



                //revenueModel= commitedRevenueButton.getText().toString();

            }
        });


        revenueShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountEdittext.setVisibility(View.INVISIBLE);
                amountEdittext.setText("");

                percentageEdittext.setVisibility(View.VISIBLE);
              //  revenueModel= revenueShareButton.getText().toString();

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           //     try{


                if (networkConnection.isNetworkAvailable()) {


                    if (processClick) {


                        submitButton.setEnabled(false);
                        submitButton.setClickable(false);


                        if (amountEdittext.getText().toString() != null && amountEdittext.getVisibility() == View.VISIBLE) {

                            if (amountEdittext.getText().toString() != null) {

                                revenueModel = amountEdittext.getText().toString();

                            }

//                        else{
//
//                            Toast.makeText(AgreementDetailsActivity.this,
//                                    "Please Enter the Revenue Model ",
//                                    Toast.LENGTH_SHORT).show();
//
//                        }

                        } else if (percentageEdittext.getText().toString() != null && percentageEdittext.getVisibility() == View.VISIBLE) {

                            if (percentageEdittext.getText().toString() != null) {

                                // revenueModel = percentageEdittext.getText().toString();
                                revenueModel1 = percentageEdittext.getText().toString();

                            }

                        }
//                    else if(percentageEdittext.getText().toString() != null) {
//
//
//                        revenueModel = percentageEdittext.getText().toString();
//
//                    }


                        if (ownerMetToggle.isChecked()) {

                            ownerMet = 1;

                        } else {
//                    Toast.makeText(AgreementDetailsActivity.this,
//                            "Please Owner Met ",
//                            Toast.LENGTH_SHORT).show();
                        }

                        if (liveDemoToggle.isChecked()) {

                            liveStatus = 1;

                        } else {
//                    Toast.makeText(AgreementDetailsActivity.this,
//                            "Please Live Demo Provided ",
//                            Toast.LENGTH_SHORT).show();

                        }


                        scheduleDate = dat + " " + tim;

                        Log.e("scheduleDate--", scheduleDate);

                        mandapamName = mandapamNameEdittext.getText().toString();
                        address = addressEdittext.getText().toString();
                        ownerName = ownerNameEdittext.getText().toString();
                        fatherName = fathersNameEdittext.getText().toString();
                        age = ageEdittext.getText().toString();
                        //      emailId = emailidEdittext.getText().toString();

                        mandapamName = mandapamName.replaceAll("[^a-zA-Z0-9]", " ");
                        address = address.replaceAll("[^a-zA-Z0-9]", " ");
                        ownerName = ownerName.replaceAll("[^a-zA-Z0-9]", " ");
                        fatherName = fatherName.replaceAll("[^a-zA-Z0-9]", " ");

                        if (mandapamName.length() == 0) {
                            Toast.makeText(AgreementDetailsActivity.this, "Please Enter Mandapam Name.", Toast.LENGTH_LONG).show();
                        } else if (address.length() == 0) {
                            Toast.makeText(AgreementDetailsActivity.this, "Please Enter Address.", Toast.LENGTH_LONG).show();
                        } else if (ownerName.length() == 0) {
                            Toast.makeText(AgreementDetailsActivity.this, "Please Enter Owner Name.", Toast.LENGTH_LONG).show();
                        } else if (fatherName.length() == 0) {
                            Toast.makeText(AgreementDetailsActivity.this, "Please Enter Father Name.", Toast.LENGTH_LONG).show();
                        } else if (age.length() == 0) {
                            Toast.makeText(AgreementDetailsActivity.this, "Please Enter  age.", Toast.LENGTH_LONG).show();
                        }
//                    else if (emailId.length() == 0) {
//                        Toast.makeText(AgreementDetailsActivity.this, "Please Enter Email Id.", Toast.LENGTH_LONG).show();
//                    }
//                        else if (revenueModel.length() == 0) {
//                            Toast.makeText(AgreementDetailsActivity.this, "Please Enter Revenue Amount or Percentage.", Toast.LENGTH_LONG).show();
//                        }
                        else if (scheduleDate.length() == 0) {
                            Toast.makeText(AgreementDetailsActivity.this, "Please Enter Date and Time.", Toast.LENGTH_LONG).show();
                        } else {

                            String amount=amountEdittext.getText().toString();

                            String percentage=percentageEdittext.getText().toString();

                            if (amount != null && !amount.equals("")) {

                                new UploadAgreementDetails().execute();

                            }
                            else if (percentage!=null && !percentage.equals("")) {

                                new UploadAgreementDetails1().execute();

                            }

                        }

                    }
                    processClick = false;
                } else {

                    Toast.makeText(AgreementDetailsActivity.this,
                            "Network is not Available. Please Check Your Internet Connection ",
                            Toast.LENGTH_SHORT).show();

                }


      //      }
//                catch (Exception e){
//
//                    Log.e("eeee",e.toString());
//
//                }

            }
        });





        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                // Launch Date Picker Dialog
//                DatePickerDialog dpd = new DatePickerDialog(AgreementDetailsActivity.this,
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

            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(AgreementDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // Display Selected time in textbox
                                //  txtTime.setText(hourOfDay + ":" + minute);
//                                    Toast.makeText(getApplicationContext(),
//                                            "Time : " + hourOfDay + ":" + minute,
//                                            Toast.LENGTH_SHORT).show();
                                tim = hourOfDay + ":" + minute + ":00";

                                timeButton.setText(hourOfDay + ":" + minute + ":00");
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }
        });






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


//                	Log.e("year--", year+"");
//                	Log.e("monthOfYear--", monthOfYear+"");
//                	Log.e("dayOfMonth--", dayOfMonth+"");
//
//                	Log.e("myear--", myear+"");
//                	Log.e("mmonth--", mmonth+"");
//                	Log.e("mday--", mday+"");


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

            dateButton.setText(myear1 + "-" + (mmonth1 + 1) + "-" + mday1);
            dat = myear1 + "-" + (mmonth1 + 1) + "-" + mday1;

            // set selected date into textview
//            tvDisplayDate.setText(new StringBuilder().append(mmonth1 + 1)
//                    .append("-").append(mday1).append("-").append(myear1)
//                    .append(" "));

        }
    };


    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(AgreementDetailsActivity.this, view);
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

              //          startActivity(new Intent(AgreementDetailsActivity.this, LoginActivity.class));

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
            pDialog = new ProgressDialog(AgreementDetailsActivity.this);
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
                Toast.makeText(AgreementDetailsActivity.this,
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

                    Intent intent=new Intent(AgreementDetailsActivity.this, LoginActivity.class);

                    intent.putExtra("exit",exit);

                    startActivity(intent);
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



//    TextWatcher watch = new TextWatcher(){
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//
////            try {
////                Log.d("Percentage", "input: " + s);
////                if(Integer.parseInt(s.toString())>100){
////                //    s.replace(0, s.length(), "100");
////                }
////                else{
////
////                    Toast.makeText(AgreementDetailsActivity.this,
////                            "Percentage Limit is 0 to 100 ",
////                            Toast.LENGTH_SHORT).show();
////
////                }
////
////
////            }
////            catch(NumberFormatException nfe){
////
////                Log.e("NumberFormatException--",nfe.toString());
////
////            }
//
//
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//                                      int arg3) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int a, int b, int c) {
//            // TODO Auto-generated method stub
//
////                output.setText(s);
////                if(a == 9){
////                    Toast.makeText(getApplicationContext(), "Maximum Limit Reached", Toast.LENGTH_SHORT).show();
////                }
//        }};

    class UploadAgreementDetails1 extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AgreementDetailsActivity.this);
            pDialog.setMessage("Upload Agreement Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            if (networkConnection.isNetworkAvailable()) {



                for (int i = 0; i < statuslist.size(); i++) {

                    if (statuslist.size() == 1) {
                        Log.e("--" + 1 + "--", statuslist.get(0));
//                        params.add(new BasicNameValuePair("venue_status", statuslist.get(0)));
//                        params.add(new BasicNameValuePair("visit_status1", ""));
                        params.add(new BasicNameValuePair("venue_status", "Details Collected for Agreement"));
                        params.add(new BasicNameValuePair("visit_status1", "Commercial Negotiation"));


                    } else if (statuslist.size() == 2) {
                        Log.e("--" + 2 + "--", statuslist.get(0));
                        Log.e("--" + 2 + "--", statuslist.get(1));

//                        params.add(new BasicNameValuePair("venue_status", statuslist.get(1)));
//                        params.add(new BasicNameValuePair("visit_status1", statuslist.get(0)));

                        params.add(new BasicNameValuePair("venue_status", "Details Collected for Agreement"));
                        params.add(new BasicNameValuePair("visit_status1", "Commercial Negotiation"));



                    }
                    else if (statuslist.size() == 3) {
                        Log.e("--" + 3 + "--", statuslist.get(0));
                        Log.e("--" + 3 + "--", statuslist.get(1));
                        Log.e("--" + 3 + "--", statuslist.get(2));

                        params.add(new BasicNameValuePair("venue_status", statuslist.get(2)));
                        params.add(new BasicNameValuePair("visit_status1", statuslist.get(1)));

                    }
                }

                params.add(new BasicNameValuePair("name", mandapamName));
                params.add(new BasicNameValuePair("user_id", userId));
                params.add(new BasicNameValuePair("visit_id", visitId));
                params.add(new BasicNameValuePair("stage_number", stageNumber));
                params.add(new BasicNameValuePair("venue_id", venueId));
                params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));
                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("owner_name", ownerName));
                params.add(new BasicNameValuePair("father_name", fatherName));
                params.add(new BasicNameValuePair("age", age));
                params.add(new BasicNameValuePair("revenue_percent", revenueModel1));
                params.add(new BasicNameValuePair("revenue_status", "1"));
                params.add(new BasicNameValuePair("schedule_date", scheduleDate));

                json = jsonParser.makeHttpRequest(ApplicationConstants.url_create_venue_agreement1,
                        "POST", params);

                //    Log.e("json---", json.toString());



            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(AgreementDetailsActivity.this,
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

//                    startActivity(new Intent(AgreementDetailsActivity.this, ScheduledVenueActivity.class));
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


    class UploadAgreementDetails extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AgreementDetailsActivity.this);
            pDialog.setMessage("Upload Agreement Details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            if (networkConnection.isNetworkAvailable()) {



                for (int i = 0; i < statuslist.size(); i++) {

                    if (statuslist.size() == 1) {
                        Log.e("--" + 1 + "--", statuslist.get(0));
//                        params.add(new BasicNameValuePair("venue_status", statuslist.get(0)));
//                        params.add(new BasicNameValuePair("visit_status1", ""));
                        params.add(new BasicNameValuePair("venue_status", "Details Collected for Agreement"));
                        params.add(new BasicNameValuePair("visit_status1", "Commercial Negotiation"));


                    } else if (statuslist.size() == 2) {
                        Log.e("--" + 2 + "--", statuslist.get(0));
                        Log.e("--" + 2 + "--", statuslist.get(1));

//                        params.add(new BasicNameValuePair("venue_status", statuslist.get(1)));
//                        params.add(new BasicNameValuePair("visit_status1", statuslist.get(0)));

                        params.add(new BasicNameValuePair("venue_status", "Details Collected for Agreement"));
                        params.add(new BasicNameValuePair("visit_status1", "Commercial Negotiation"));



                    }
                    else if (statuslist.size() == 3) {
                        Log.e("--" + 3 + "--", statuslist.get(0));
                        Log.e("--" + 3 + "--", statuslist.get(1));
                        Log.e("--" + 3 + "--", statuslist.get(2));

                        params.add(new BasicNameValuePair("venue_status", statuslist.get(2)));
                        params.add(new BasicNameValuePair("visit_status1", statuslist.get(1)));



                    }

                }




                params.add(new BasicNameValuePair("name", mandapamName));
                params.add(new BasicNameValuePair("user_id", userId));
                params.add(new BasicNameValuePair("visit_id", visitId));
                params.add(new BasicNameValuePair("venue_id", venueId));
                params.add(new BasicNameValuePair("stage_number", stageNumber));



              //  params.add(new BasicNameValuePair("venue_status", currentStatus));
              //  params.add(new BasicNameValuePair("visit_status1", visitStatus));




                params.add(new BasicNameValuePair("postvisit_Status", postVisitStatus));

                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("owner_name", ownerName));
                params.add(new BasicNameValuePair("father_name", fatherName));
                params.add(new BasicNameValuePair("age", age));


               // if(revenueModel.length() != 0) {
                params.add(new BasicNameValuePair("revenue_amount", revenueModel));
                params.add(new BasicNameValuePair("revenue_status", "0"));
               // }
              //  if(revenueModel1.length() != 0) {

               //     params.add(new BasicNameValuePair("revenue_model1", revenueModel1));
              //  }
            //    params.add(new BasicNameValuePair("email_id", emailId));
                params.add(new BasicNameValuePair("schedule_date", scheduleDate));

             //   params.add(new BasicNameValuePair("owner_met", ownerMet+""));
             //   params.add(new BasicNameValuePair("live_status", liveStatus+""));

                json = jsonParser.makeHttpRequest(ApplicationConstants.url_create_agreement_detail,
                        "POST", params);

            //    Log.e("json---", json.toString());



            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(AgreementDetailsActivity.this,
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

//                    startActivity(new Intent(AgreementDetailsActivity.this, ScheduledVenueActivity.class));
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
            pDialog = new ProgressDialog(AgreementDetailsActivity.this);
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
                Toast.makeText(AgreementDetailsActivity.this,
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


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AgreementDetailsActivity.this);
                        alertDialogBuilder.setMessage("You want update Next Status");

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(AgreementDetailsActivity.this, UpdateVenueStatusActivit.class);


                                intent.putExtra("venueName", venueNme);
                                intent.putExtra("venueId", venueId);
                                intent.putExtra("visitId", visitID);
                                intent.putExtra("userId", userId);
                                intent.putExtra("venue_latitude", latitude);
                                intent.putExtra("venue_longitude", longitude);

                                intent.putExtra("venueStatus", statuslist.get(0));
                                intent.putExtra("stageNumber", stageNumber);

                                startActivity(intent);


                                finish();


                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AgreementDetailsActivity.this, ScheduledVenueActivity.class);

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

                        Intent intent = new Intent(AgreementDetailsActivity.this, ScheduledVenueActivity.class);

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


    class GetLiveDemo extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AgreementDetailsActivity.this);
            pDialog.setMessage("Get Live Demo Staatus...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            List<NameValuePair> params = new ArrayList<NameValuePair>();



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


                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_get_live_demo,
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
                Toast.makeText(AgreementDetailsActivity.this,
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

                    livedemo = json.getJSONArray(ApplicationConstants.TAG_LIVE_DEMO_STATUS);

//                        startActivity(new Intent(Stage3StatusActivity.this, ScheduledVenueActivity.class));
//                        finish();

                    for(int i = 0; i < livedemo.length(); i++ ){
                        JSONObject c = livedemo.getJSONObject(i);
                        liveDemo = c.getInt(ApplicationConstants.TAG_LIVE_DEMO);

                        if(liveDemo == 1){

                            liveDemoToggle.setVisibility(View.INVISIBLE);
                            liveDemoTextview.setVisibility(View.INVISIBLE);

                        }



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
//
//
//
//
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
//
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


    private String currentDateFormat(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }





}
