package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.purpleknot1.Adapter.CustomSpinnerAdapter;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Adapter.CustomSpinnerAdapter;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.view.View.OnClickListener;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;

public class ScheduleVenueActivity extends Activity
{

    Button btnSubmitPopup,dateButton,timeButton,closeButton;
    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    CustomSpinnerAdapter customSpinnerAdapter;

    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParser1 = new JSONParser();

    public TextView venueNameTextview;

    JSONArray city = null;
    JSONArray location = null;
    JSONArray venue = null;

    Spinner cityListview,locationListview;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    String cityName,locationName;

    ArrayAdapter<String> dataAdapter;

    ListView scheduleVenueListview;

    List<String> cityList = new ArrayList<String>();
    List<String> locationList = new ArrayList<String>();
    List<String> userList = new ArrayList<String>();

    SimpleAdapter adapter;

    private PopupWindow pwindo;

    private int mYear, mMonth, mDay, mHour, mMinute;

    JSONArray user = null;

    Spinner userSpinner;

    String venueid;

    String userNme;

    String dateTime;

    String dat,tim;

    ImageView backButton;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedulevenue_list);

        cityListview=(Spinner)findViewById(R.id.city_spinner);
        locationListview=(Spinner)findViewById(R.id.location_spinner);
        scheduleVenueListview=(ListView)findViewById(R.id.schedule_venue_listview);

        backButton = (ImageView)findViewById(R.id.venuelist_back_imageview);

        networkConnection = new NetworkConnection(this);

        if (networkConnection.isNetworkAvailable())
        {

            new SelectCity().execute();
        }
        else
        {
            // populateScheduleVenueDetails();
        }

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleVenueActivity.this, AdminDashBoardActivity.class));
                finish();
            }
        });


        scheduleVenueListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView venueName=(TextView) view.findViewById(R.id.schedule_venue_name_textview);
                TextView venueId=(TextView) view.findViewById(R.id.schedule_venue_id_textview);

                Toast.makeText(parent.getContext(),
                        "venueName : " + venueName.getText().toString()+" -- venueId-- "+venueId.getText().toString(),
                        Toast.LENGTH_SHORT).show();

                initiatePopupWindow(venueName.getText().toString(),venueId.getText().toString());

//                oslist.remove(venueName.getText().toString());
//                adapter.notifyDataSetChanged();



            }
        });


        locationListview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                oslist.clear();

                adapter = new SimpleAdapter(ScheduleVenueActivity.this, oslist,
                        R.layout.column_venue_schedule,
                        new String[] {ApplicationConstants.TAG_VENUENAME,ApplicationConstants.TAG_SCHEDUL_VENUE_ID, ApplicationConstants.TAG_SCHEDUL_VENUE_CURRENT_STATUS }, new int[] {
                        R.id.schedule_venue_name_textview,R.id.schedule_venue_id_textview,R.id.current_status_textview});


                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();

                locationName = parent.getItemAtPosition(position).toString();

                new LoadVenue().execute();

//                String selectedItem = parent.getItemAtPosition(position).toString();
//
//                oslist.remove(selectedItem);
//                adapter.notifyDataSetChanged();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        cityListview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                locationList.clear();
//                if(position!=0)
//                {
                    dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinnertext, locationList);
                    dataAdapter.clear();
                    dataAdapter.notifyDataSetChanged();

                    Toast.makeText(parent.getContext(),
                            "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();
                    cityName = parent.getItemAtPosition(position).toString();

                    new SelectLocation().execute();
               // }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }


    class SelectUser extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduleVenueActivity.this);
            pDialog.setMessage("Loading List..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;
            if (networkConnection.isNetworkAvailable()) {

                try {
                    //    String userId = Employee.getInstance().getUserId();
                    //   String employeeID = Employee.getInstance().getEmployeeId();

                    //     Log.e("userId1--", userId);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    // params.add(new BasicNameValuePair("employee_id", employeeID));

                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_user,
                            "POST", params);

                    Log.e("json---", json.toString());

                } catch (Exception e) {

                    Log.e("schedule Eror", e.toString());
                }

            } else {

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
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1) {



                    userList.clear();
                    userSpinner.setAdapter(new ArrayAdapter<String>(ScheduleVenueActivity.this,android.R.layout.simple_dropdown_item_1line,userList));

                  //  customSpinnerAdapter.notifyDataSetChanged();




                    user = json.getJSONArray(ApplicationConstants.TAG_USERS);

                    for (int i = 0; i < user.length(); i++) {
                        JSONObject c = user.getJSONObject(i);

                        String userName = c.getString(ApplicationConstants.TAG_USERSNAME);
                        String userRole = c.getString(ApplicationConstants.TAG_USERSROLE);
                        String userId = c.getString(ApplicationConstants.TAG_USERID);


//                        HashMap<String, String> map = new HashMap<String, String>();
//
//                        map.put(ApplicationConstants.TAG_USERSNAME, userName);
//                        map.put(ApplicationConstants.TAG_USERSROLE, userRole);
//                        map.put(ApplicationConstants.TAG_USERID, userId);


                      //  oslist.add(map);

                        userList.add(userName);

                        String defaultTextForSpinner = "Select User";
                        String[] stockArr = new String[userList.size()];
                        stockArr = userList.toArray(stockArr);

                        customSpinnerAdapter=new CustomSpinnerAdapter(ScheduleVenueActivity.this, R.layout.spinner_item, stockArr, defaultTextForSpinner);

//                        customSpinnerAdapter.clear();

                        customSpinnerAdapter.notifyDataSetChanged();

                        userSpinner.setAdapter(customSpinnerAdapter);

                    }

                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initiatePopupWindow(String venueName,String venueId) {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) ScheduleVenueActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 300, 370, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            userSpinner = (Spinner) layout.findViewById(R.id.user_spinner);

            TextView venueNameTextview = (TextView) layout.findViewById(R.id.schedule_venuename_txt);
            TextView venueIdTextview = (TextView) layout.findViewById(R.id.schedule_venueid_txt);

            venueNameTextview.setText(venueName);
            venueIdTextview.setText(venueId);

            venueid = venueId;

            dateButton = (Button) layout.findViewById(R.id.date_button);

            timeButton = (Button) layout.findViewById(R.id.time_button);

            closeButton = (Button) layout.findViewById(R.id.close_button);

            btnSubmitPopup = (Button) layout.findViewById(R.id.btn_submit_popup);
            btnSubmitPopup.setOnClickListener(cancel_button_click_listener);

            closeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            });


            dateButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(ScheduleVenueActivity.this,
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

            timeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog tpd = new TimePickerDialog(ScheduleVenueActivity.this,
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







            new SelectUser().execute();






        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnClickListener cancel_button_click_listener = new OnClickListener() {
        public void onClick(View v) {

            Log.e("dat--", dat);
            Log.e("tim--",tim);

            userNme=userSpinner.getSelectedItem().toString();
            dateTime=dat+" "+tim;

            new CreateSchedule().execute();





          //  userSpinner.setAdapter(customSpinnerAdapter);

//            locationList.clear();
////                if(position!=0)
////                {
//            dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
//                    R.layout.spinnertext, locationList);
//            dataAdapter.clear();
//            dataAdapter.notifyDataSetChanged();

            pwindo.dismiss();

        }
    };

    class CreateSchedule extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduleVenueActivity.this);
            pDialog.setMessage("Create Schedule...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            JSONObject json1=null;
            if (networkConnection.isNetworkAvailable()) {
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");



                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("venue_id", venueid));
                params.add(new BasicNameValuePair("user_name", userNme));
                params.add(new BasicNameValuePair("schedule_date", dateTime));


                // getting JSON Object
                // Note that create product url accepts POST method
                json1 = jsonParser1.makeHttpRequest(ApplicationConstants.url_schedule_venue_by_admin,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response1--", json1.toString());

                // check for success tag
                try {
                    int success = json1.getInt(ApplicationConstants.TAG_SUCCESS);

                    if (success == 1) {


                        pDialog.dismiss();
                        Looper.prepare();
                        Toast.makeText(ScheduleVenueActivity.this,
                                "Venue is Scheduled ",
                                Toast.LENGTH_SHORT).show();
                        Looper.loop();



                    }
                    else if(success == 0) {


                    }
                    else if(success == 2) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(ScheduleVenueActivity.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            //new LoadVenue().execute();
        }

    }


    class LoadVenue extends AsyncTask<String, String, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduleVenueActivity.this);
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
                    params.add(new BasicNameValuePair("location", locationName));
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_venue_by_location,
                            "POST", params);

                }
                catch (Exception e)
                {

                    Log.e("schedule Eror", e.toString());
                }

            }
            else
            {

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

            try
            {

                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1)
                {


                    venue = json.getJSONArray(ApplicationConstants.TAG_SCHEDUL_VENUE);
                    for (int i = 0; i < venue.length(); i++)
                    {

                        JSONObject c = venue.getJSONObject(i);
                        String venueName = c.getString(ApplicationConstants.TAG_SCHEDUL_VENUE_NAME);
                        String venueId = c.getString(ApplicationConstants.TAG_SCHEDUL_VENUE_ID);
                        String currentStatus = c.getString(ApplicationConstants.TAG_SCHEDUL_VENUE_CURRENT_STATUS);
                        String scheduleStatus = c.getString(ApplicationConstants.TAG_SCHEDUL_VENUE_SCHEDULE_STATUS);

                        if(scheduleStatus.equalsIgnoreCase("0")) {

                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put(ApplicationConstants.TAG_SCHEDUL_VENUE_NAME, venueName);
                            map.put(ApplicationConstants.TAG_SCHEDUL_VENUE_ID, venueId);
                            map.put(ApplicationConstants.TAG_SCHEDUL_VENUE_CURRENT_STATUS, currentStatus);
                            map.put(ApplicationConstants.TAG_SCHEDUL_VENUE_SCHEDULE_STATUS, scheduleStatus);

                            oslist.add(map);

                            adapter = new SimpleAdapter(ScheduleVenueActivity.this, oslist,
                                    R.layout.column_venue_schedule,
                                    new String[]{ApplicationConstants.TAG_VENUENAME, ApplicationConstants.TAG_SCHEDUL_VENUE_ID, ApplicationConstants.TAG_SCHEDUL_VENUE_CURRENT_STATUS}, new int[]{
                                    R.id.schedule_venue_name_textview, R.id.schedule_venue_id_textview, R.id.current_status_textview});





                            scheduleVenueListview.setAdapter(adapter);
                        }

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

    class SelectLocation extends AsyncTask<String, String, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduleVenueActivity.this);
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
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_location,
                            "POST", params);

                }
                catch (Exception e)
                {

                    Log.e("schedule Eror", e.toString());
                }

            }
            else
            {

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

            try
            {
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1)
                {


                    location = json.getJSONArray(ApplicationConstants.TAG_LOCATION);

                    for (int i = 0; i < location.length(); i++)
                    {
                        JSONObject c = location.getJSONObject(i);
                        String loctionName = c.getString(ApplicationConstants.TAG_LOCATION_NAME);
                        locationList.add(loctionName);
//                        dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
//                                  R.layout.spinnertext, locationList);
//                        locationListview.setAdapter(dataAdapter);

                        String defaultTextForSpinner = "Select Location";
                        String[] stockArr = new String[locationList.size()];
                        stockArr = locationList.toArray(stockArr);

                        locationListview.setAdapter(new CustomSpinnerAdapter(ScheduleVenueActivity.this, R.layout.spinner_item, stockArr, defaultTextForSpinner));


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
            pDialog = new ProgressDialog(ScheduleVenueActivity.this);
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
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_city,
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
                    for (int i = 0; i < city.length(); i++)
                    {
                        JSONObject c = city.getJSONObject(i);

                        String cityName = c.getString(ApplicationConstants.TAG_CTY_NAME);

                        cityList.add(cityName);

                        String defaultTextForSpinner = "Select City";
                        String[] stockArr = new String[cityList.size()];
                        stockArr = cityList.toArray(stockArr);

                        cityListview.setAdapter(new CustomSpinnerAdapter(ScheduleVenueActivity.this, R.layout.spinner_item, stockArr, defaultTextForSpinner));

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
