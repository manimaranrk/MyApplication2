package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserActionActivity extends Activity {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    Button workButton, breakButton, dataViewButton, submitButton;

    ToggleButton tButton;

    TextView currentTimeTextview,dateTimeTextview;

    int toggle;

    String currentDateTime;

    ImageView actionOn,actionCenter,actionOff;

    private ProgressDialog pDialog;

    NetworkConnection networkConnection;

    JSONParser jsonParser = new JSONParser();
    PopupMenu popupMenu;

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_action);

        networkConnection = new NetworkConnection(this);

//        workButton=(Button)findViewById(R.id.work_button);
//        breakButton=(Button)findViewById(R.id.break_button);
//        dataViewButton=(Button)findViewById(R.id.data_view_button);

//        actionOn = (ImageView)findViewById(R.id.action_on);
//        actionCenter = (ImageView)findViewById(R.id.action_neutrol);
//        actionOff = (ImageView)findViewById(R.id.action_off);

        currentTimeTextview = (TextView)findViewById(R.id.currenttime_textview);

        dateTimeTextview = (TextView)findViewById(R.id.datetime_textview);

        tButton = (ToggleButton) findViewById(R.id.toggleButton1);

        submitButton = (Button)findViewById(R.id.submit_button);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String strDate = sdf.format(c.getTime());

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String strDate1 = sdf1.format(c.getTime());

     //   int loginStatus = LoginStatus.getInstance().getLoginStatus();

      //  Log.e("loginStatus==",loginStatus+"");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);




        try {

            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inFormat.parse(strDate1);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);
            Log.e("goal--",goal);

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy"); //Date and time
            String currentDate = sdf2.format(c.getTime());

            currentDateTime = goal+" "+currentDate;

            dateTimeTextview.setText(currentDateTime);

        }
        catch (Exception e){
            Log.e("eeee",e.toString());
        }



        currentTimeTextview.setText(strDate);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toggle==1){
                    startActivity(new Intent(UserActionActivity.this, ReportActivity.class));
                    finish();

                }else{

//                    startActivity(new Intent(UserActionActivity.this, TargetEntityActivity.class));
//                    finish();

                    startActivity(new Intent(UserActionActivity.this, CreateVenueMapActivity.class));
                    finish();
                }

            }
        });


        tButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    toggle=1;

//                    startActivity(new Intent(UserActionActivity.this, TargetEntityActivity.class));
//                    finish();

                } else {
                    toggle=0;
                }

            }
        });

//        workButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(UserActionActivity.this, TargetEntityActivity.class));
//                finish();
//
//
//            }
//        });
//
//        breakButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        dataViewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(UserActionActivity.this, ReportActivity.class));
//                finish();
//
//            }
//        });





    }

    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(UserActionActivity.this, view);
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

                   //     startActivity(new Intent(UserActionActivity.this, LoginActivity.class));

                        finish();


                        //   startActivity(new Intent(ScheduledVenueActivity.this, ReportActivity.class));

                        break;

                }
                return true;
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // Write your code here
//
//        super.onBackPressed();
//
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserActionActivity.this);
//        builder1.setMessage("Do You Want Close the Application.");
//        builder1.setCancelable(true);
//        builder1.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        builder1.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();
//
//
//
//
//    }


    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActionActivity.this);
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
                Log.e("network1===",networkConnection.isNetworkAvailable()+"");


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
                Toast.makeText(UserActionActivity.this,
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


                  //  LoginStatus.getInstance().setLoginStatus(1);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt(loginstatusKey, 1);
                    editor.commit();

                  //  startActivity(new Intent(UserActionActivity.this, LoginActivity.class));

                    String exit="exit";

                    Intent intent=new Intent(UserActionActivity.this, LoginActivity.class);

                    intent.putExtra("exit",exit);

                    startActivity(intent);
                    finish();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


//    @Override
//    public void onBackPressed() {
//
//     //   new CheckValidUser().execute();
//     //   finish();
//
//    }

}