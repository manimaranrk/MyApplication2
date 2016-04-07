package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.purpleknot1.Adapter.ScheduleVenueArrayAdapter;
import com.purpleknot1.Adapter.ViewPagerAdapter1;
import com.purpleknot1.DTO.ScheduleVenueListDto;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduledVenue1Activity extends Activity {

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

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
    ArrayList<String> managerMessageList=new ArrayList<String>();

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
    String managerMessages[]=new String[9999];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.schedule_venue1);

      //  scheduleListview=(ListView)findViewById(R.id.scheduled_venue_listview);
        menuImageview=(ImageView)findViewById(R.id.menu_imageview);
   //     venueIdTextview=(TextView)findViewById(R.id.venueid_txt);
      //  venueNameTextview=(TextView)findViewById(R.id.venuename_txt);
     //   venueStatusTextview=(TextView)findViewById(R.id.venuestatus_txt);
     //   venueDateTextview=(TextView)findViewById(R.id.venuedata_txt);
        refreshImageview=(ImageView) findViewById(R.id.refresh_imageview);

        backImageview = (ImageView) findViewById(R.id.schedule_back_imageview);

        backImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ScheduledVenue1Activity.this, UserListActivity.class));
                finish();

            }
        });

        networkConnection = new NetworkConnection(this);

        if (networkConnection.isNetworkAvailable()) {

            new SelectScheduleVenue().execute();
        }
        else{
           // populateScheduleVenueDetails();
        }

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

    class SelectScheduleVenue extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScheduledVenue1Activity.this);
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

                    Log.e("userId--", userId);

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("user_id", userId));
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_scheduledvenue,
                            "POST", params);

                    Log.e("json---",json.toString());

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
                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
                if (success == 1) {

                    venue = json.getJSONArray(ApplicationConstants.TAG_VENUEHISTORE);

                    for (int i = 0; i < venue.length(); i++) {
                        JSONObject c = venue.getJSONObject(i);

                        String venueid = c.getString(ApplicationConstants.TAG_VENUEID);
                        String visitid = c.getString(ApplicationConstants.TAG_VISITID);
                        String venuename = c.getString(ApplicationConstants.TAG_VENUENAME);
                        String userid=c.getString(ApplicationConstants.TAG_USERRID);
                        String currentstatus = c.getString(ApplicationConstants.TAG_CURRENTSTATUS);
                        String postvisitedStatus = c.getString(ApplicationConstants.TAG_POSTVISITSTATUS);
                        String scheduledate = c.getString(ApplicationConstants.TAG_SCHEDULEDATE);
                        String lastvisitedDate = c.getString(ApplicationConstants.TAG_LASTVISITDATE);
                        String visitnumber = c.getString(ApplicationConstants.TAG_VISITNUMBER);
                        String schedulestatus = c.getString(ApplicationConstants.TAG_SCHEDULESTATUS);
                        String managerMessage = c.getString(ApplicationConstants.TAG_MANAGERMESSAGE);

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
                       managerMessageList.add(managerMessage);

                        venueId = venueIdList.toArray(new String[venueIdList.size()]);
                        visitId = visitIdList.toArray(new String[visitIdList.size()]);
                        userId = userIdList.toArray(new String[userIdList.size()]);
                        currentStatus=currentStatusList.toArray(new String[currentStatusList.size()]);
                        postVisitStatus=postVisitStatusList.toArray(new String[postVisitStatusList.size()]);
                        scheduleDate=scheduleDateList.toArray(new String[scheduleDateList.size()]);
                        lastVisitDate=lastVisitDateList.toArray(new String[lastVisitDateList.size()]);
                        visitNumber=visitNumberList.toArray(new String[visitNumberList.size()]);
                        scheduleStatus=scheduleStatusList.toArray(new String[scheduleStatusList.size()]);
                        venueNames = venueNamesList.toArray(new String[venueNamesList.size()]);
                        managerMessages = managerMessageList.toArray(new String[managerMessageList.size()]);

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
                        map.put("count", String.valueOf(i+1));

                        oslist.add(map);

                        Log.e("iiii--",i+1+"");

                        String count=String.valueOf(i+1);

//                        ListAdapter adapter = new SimpleAdapter(ScheduledVenueActivity.this, oslist,
//                                R.layout.column_schedule,
//                                new String[] {"count",ApplicationConstants.TAG_VENUENAME,ApplicationConstants.TAG_CURRENTSTATUS, ApplicationConstants.TAG_SCHEDULEDATE }, new int[] {
//                                R.id.schedulevenue_id_texiview,R.id.schedulevenue_name_textview,R.id.schedulevenue_ststus_textview, R.id.schedulevenue_date_textview});

                   //     scheduleListview.setAdapter(adapter);

                        viewPager = (ViewPager) findViewById(R.id.pager);
                        // Pass results to ViewPagerAdapter Class
                        adapter1 = new ViewPagerAdapter1(ScheduledVenue1Activity.this, venueId, visitId, userId,currentStatus,
                                postVisitStatus,scheduleDate,lastVisitDate,visitNumber,scheduleStatus,venueNames,managerMessages );
                        // Binds the Adapter to the ViewPager
                        viewPager.setAdapter(adapter1);

//                        Bundle extras = getIntent().getExtras();
//                        if(extras !=null) {
//                            String employeeid = extras.getString("employeeId");
//                            Employee.getInstance().setEmployeeId(employeeid);
//                        }

                    }

                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(ScheduledVenue1Activity.this, view);
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

                        startActivity(new Intent(ScheduledVenue1Activity.this, ReportActivity.class));

                        break;

                }
                return true;
            }
        });
    }

//    private void populateScheduleVenueDetails(){
//
//        String employeeId=EmployeeId.getInstance().getEmployeeId();
//        scheduleVenueArrayAdapter=new ScheduleVenueArrayAdapter(this,R.layout.column_schedule);
//        List<ScheduleVenueListDto> scheduleVenueListDtos =
//                PurpleKnotDBHelper.getInstance(ScheduledVenueActivity.this).selectMandapamByEmployeeId(employeeId);
//
//        for (int i = 0; i < scheduleVenueListDtos.size(); i++) {
//            scheduleVenueListDto = scheduleVenueListDtos.get(i);
//            Log.e("mandapamListDto",scheduleVenueListDto.getVenueId()+"");
//            scheduleVenueArrayAdapter.add(scheduleVenueListDto);
//            scheduleListview.setAdapter(scheduleVenueArrayAdapter);
//            scheduleVenueArrayAdapter.notifyDataSetChanged();
//        }
//    }
}