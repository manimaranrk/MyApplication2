package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ForgetPasswordActivity extends Activity {

    String userName,mobileNumber,emailId;

    EditText userNameEdittext,mobileNumberEdittext,emailIdEdittext;

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    String deviceId;

    JSONArray user = null;

    String tmDevice=null, tmSerial=null, androidId=null;

    ImageView forgetBackImageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        userNameEdittext=(EditText)findViewById(R.id.forgetpassword_username_edittext);
        mobileNumberEdittext=(EditText)findViewById(R.id.forgetpassword_mobilenum_edittext);
       // emailIdEdittext=(EditText)findViewById(R.id.forgetpassword_email_edittext);

        forgetBackImageview=(ImageView)findViewById(R.id.forget_back_imageview);

        networkConnection = new NetworkConnection(this);

        tmDevice = getUniqueID();

        Log.e("deviceId--",tmDevice);

        forgetBackImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });


      //  emailId=emailIdEdittext.getText().toString();



    }

    public String getUniqueID(){
        String myAndroidDeviceId = "";
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null){
            myAndroidDeviceId = mTelephony.getDeviceId();
        }else{
            myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return myAndroidDeviceId;
    }

    public void forgetButtonClickEvent(View view){

        userName=userNameEdittext.getText().toString();
        mobileNumber=mobileNumberEdittext.getText().toString();

        if (networkConnection.isNetworkAvailable()) {


            if(userName.equalsIgnoreCase("")){
                Toast.makeText(ForgetPasswordActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
            }
            else if(mobileNumber.equalsIgnoreCase("")){
                Toast.makeText(ForgetPasswordActivity.this, "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
            }
//            else if(emailId.length()==0){
//                Toast.makeText(ForgetPasswordActivity.this, "Please Enter Email Id", Toast.LENGTH_LONG).show();
//            }
            else {

                new CheckValidUserDetails().execute();

                //       authenticateUser();

            }
        }
        else{

            Toast.makeText(ForgetPasswordActivity.this,
                    "Network is not Available. Please Check Your Internet Connection ",
                    Toast.LENGTH_SHORT).show();
        }



    }

    class CheckValidUserDetails extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgetPasswordActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;
            if (networkConnection.isNetworkAvailable()) {
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");



                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", userName));
                params.add(new BasicNameValuePair("mobilenumber", mobileNumber));
             //   params.add(new BasicNameValuePair("deviceid", tmDevice));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_validate_user_details,
                        "POST", params);

                // check log cat fro response
           //     Log.d("Create Response", json.toString());

                // check for success tag
//                try {
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                    if (success == 1) {
//
//                        Intent intent=new Intent(ForgetPasswordActivity.this, UpdatePasswordActivity.class);
//
//                        intent.putExtra("username",userName);
//
//                        startActivity(intent);
//                        finish();
//
////                        user = json.getJSONArray(ApplicationConstants.TAG_USR);
////
////
////                        for (int i = 0; i < user.length(); i++) {
////                            JSONObject c = user.getJSONObject(i);
////
////                            // Storing each json item in variable
////                            String userId = c.getString(ApplicationConstants.TAG_USR_ID);
////                            String userName = c.getString(ApplicationConstants.TAG_USR_NME);
////                            String userDesignation = c.getString(ApplicationConstants.TAG_USR_DESIGNATION);
////                            String userImei = c.getString(ApplicationConstants.TAG_USR_IMEI);
////                            String userMobile = c.getString(ApplicationConstants.TAG_USR_MOBILE);
////
////                            //startActivity(new Intent(ForgetPasswordActivity.this, UpdatePasswordActivity.class));
////
////                            Intent intent=new Intent(ForgetPasswordActivity.this, UpdatePasswordActivity.class);
////
////                            intent.putExtra("username",userName);
////
////                            startActivity(intent);
////                            finish();
////                        }
//
////                        $user = array();
////                        $user["id"] = $row["f_user_id"];
////                        $user["username"] = $row["f_username"];
////                        $user["designation"] = $row["f_designation"];
////                        $user["employee_id"] = $row["employee_id"];
////
////                        $user["fullname"] = $row["f_fullname"];
////                        $user["imei"] = $row["f_imei"];
////                        $user["mobile_no"] = $row["f_mobile_number"];
////                        $user["city_id"] = $row["f_city_id"];
////                        $user["manager_1"] = $row["f_manager1"];
////                        $user["manager_2"] = $row["f_manager2"];
////                        $user["manager_3"] = $row["f_manager3"];
////                        $user["manager_4"] = $row["f_manager4"];
////                        $user["accessgrant"] = $row["f_accessgrant"];
////                        $user["mobile_app_access"] = $row["f_mobile_app_access"];
//
//
//
//
//
//                    }
//                    else if(success == 0) {
//
//                        pDialog.dismiss();
//                        Looper.prepare();
//                        Toast.makeText(ForgetPasswordActivity.this,
//                                "Please Enter Valid User Name,Mobile Number",
//                                Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }
//                    else if(success == 2) {
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            else {
//                pDialog.dismiss();
//                Looper.prepare();
//                Toast.makeText(ForgetPasswordActivity.this,
//                        "Network is not Available. Please Check Your Internet Connection ",
//                        Toast.LENGTH_SHORT).show();
//                Looper.loop();
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

                    Intent intent=new Intent(ForgetPasswordActivity.this, UpdatePasswordActivity.class);

                    intent.putExtra("username",userName);

                    startActivity(intent);
                    finish();

//                        user = json.getJSONArray(ApplicationConstants.TAG_USR);
//
//
//                        for (int i = 0; i < user.length(); i++) {
//                            JSONObject c = user.getJSONObject(i);
//
//                            // Storing each json item in variable
//                            String userId = c.getString(ApplicationConstants.TAG_USR_ID);
//                            String userName = c.getString(ApplicationConstants.TAG_USR_NME);
//                            String userDesignation = c.getString(ApplicationConstants.TAG_USR_DESIGNATION);
//                            String userImei = c.getString(ApplicationConstants.TAG_USR_IMEI);
//                            String userMobile = c.getString(ApplicationConstants.TAG_USR_MOBILE);
//
//                            //startActivity(new Intent(ForgetPasswordActivity.this, UpdatePasswordActivity.class));
//
//                            Intent intent=new Intent(ForgetPasswordActivity.this, UpdatePasswordActivity.class);
//
//                            intent.putExtra("username",userName);
//
//                            startActivity(intent);
//                            finish();
//                        }

//                        $user = array();
//                        $user["id"] = $row["f_user_id"];
//                        $user["username"] = $row["f_username"];
//                        $user["designation"] = $row["f_designation"];
//                        $user["employee_id"] = $row["employee_id"];
//
//                        $user["fullname"] = $row["f_fullname"];
//                        $user["imei"] = $row["f_imei"];
//                        $user["mobile_no"] = $row["f_mobile_number"];
//                        $user["city_id"] = $row["f_city_id"];
//                        $user["manager_1"] = $row["f_manager1"];
//                        $user["manager_2"] = $row["f_manager2"];
//                        $user["manager_3"] = $row["f_manager3"];
//                        $user["manager_4"] = $row["f_manager4"];
//                        $user["accessgrant"] = $row["f_accessgrant"];
//                        $user["mobile_app_access"] = $row["f_mobile_app_access"];





                }
                else if(success == 0) {


                 ///   Looper.prepare();
                    Toast.makeText(ForgetPasswordActivity.this,
                            "Please Enter Valid User Name,Mobile Number",
                            Toast.LENGTH_SHORT).show();
                 //   Looper.loop();
                }
                else if(success == 2) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }








    }


}
