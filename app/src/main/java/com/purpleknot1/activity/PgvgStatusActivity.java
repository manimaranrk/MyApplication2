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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PgvgStatusActivity extends Activity {

    public static final String MyPREFERENCES = "PurpleknotLogin" ;

    public static final String loginstatusKey = "loginstatusKey";

    EditText commentEdittext;

    String pgName,userID,pgvgId,pgvgCurrent,pgvgLoccap,pgvgComment,pgvgVisitid,pgvgstatus;

    double lati=0.0,longi=0.0;

    Button submit;

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    ImageView back;

    PopupMenu popupMenu;

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgvg_status);

        commentEdittext= (EditText)findViewById(R.id.pgvgcomment_edittext);

        submit=(Button)findViewById(R.id.submit_button);

        back =(ImageView)findViewById(R.id.pgvg_status_back_imageview);

        networkConnection = new NetworkConnection(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            pgName = extras.getString("pgName");
            lati= extras.getDouble("lati");
            longi=extras.getDouble("longi");
            userID = extras.getString("userID");
            pgvgId = extras.getString("pgvgId");
            pgvgCurrent = extras.getString("pgvgCurrent");
            pgvgLoccap = extras.getString("pgvgLoccap");
            pgvgVisitid = extras.getString("pgvgVisitid");
            pgvgstatus = extras.getString("pgvgstatus");


            Log.e("pgName--",pgName);
            Log.e("userID--",userID);
            Log.e("pgvgId--",pgvgId);
            Log.e("pgvgCurrent--",pgvgCurrent);
            Log.e("pgvgLoccap--",pgvgLoccap);
            Log.e("pgvgVisitid--",pgvgVisitid);
            Log.e("pgvgstatus--",pgvgstatus);

            Log.e("latit--",lati+"");
            Log.e("longit--",longi+"");



          //  pgvg_latitude =Double.parseDouble(latit);


           // pgvg_longitude = Double.parseDouble(longit);
           // Log.e("lati,longi##----", pgvg_latitude + "--" + pgvg_longitude);




        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ScheduledPgvgActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pgvgComment= commentEdittext.getText().toString();


                if (networkConnection.isNetworkAvailable()) {

                    new UpdateVenueStatus1().execute();

                }


            }
        });


    }

    public void menuButtonClickEvent(View view){

        popupMenu = new PopupMenu(PgvgStatusActivity.this, view);
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

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PgvgStatusActivity.this);
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
                Toast.makeText(PgvgStatusActivity.this,
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

                    Intent intent=new Intent(PgvgStatusActivity.this, LoginActivity.class);
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

    class UpdateVenueStatus1 extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PgvgStatusActivity.this);
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

                    params.add(new BasicNameValuePair("name", pgName));
                    params.add(new BasicNameValuePair("userID", userID));
                    params.add(new BasicNameValuePair("pgvgId", pgvgId));
                    params.add(new BasicNameValuePair("pgvgstatus", pgvgstatus));
                    params.add(new BasicNameValuePair("pgvgCurrent", pgvgCurrent));
                    params.add(new BasicNameValuePair("pgvgVisitid", pgvgVisitid));
                    params.add(new BasicNameValuePair("pgvgComment", pgvgComment));
                    params.add(new BasicNameValuePair("lati", lati+""));
                    params.add(new BasicNameValuePair("longi", longi+""));


                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_pgvg_status,
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
                Toast.makeText(PgvgStatusActivity.this,
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

//                        startActivity(new Intent(Stage3StatusActivity.this, ScheduledVenueActivity.class));
//                        finish();
                    if (networkConnection.isNetworkAvailable()) {


                        try {

                        Intent intent=new Intent(PgvgStatusActivity.this, ScheduledPgvgActivity.class);
                       // startActivity(new Intent(PgvgStatusActivity.this, ScheduledPgvgActivity.class));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                          //  new GetVisitId().execute();
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

}
