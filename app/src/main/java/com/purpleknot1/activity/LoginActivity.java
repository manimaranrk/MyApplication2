package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.*;


import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;

import com.purpleknot1.Util.FPSDBConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.LoginStatus;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.TelephonyInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity{

    public static final String MyPREFERENCES = "PurpleknotLogin" ;
    public static final String userNameKey = "userNameKey";
    public static final String passwordKey = "passwordKey";
    public static final String loginstatusKey = "loginstatusKey";

    TelephonyManager tel;
    NetworkConnection networkConnection;
    String userName,passWord;
    EditText loginUserNameEdittext,loginPasswordEdittext;
    JSONArray user = null;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    TextView forgetPasswordTextview,changePasswordTextview;
    String tmDevice=null,imsiSIM1=null,imsiSIM2=null;
    Boolean isSIM1Ready,isSIM2Ready,isDualSIM;
    SharedPreferences sharedpreferences;
    TelephonyInfo telephonyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginUserNameEdittext=(EditText)findViewById(R.id.login_username_edittext);
        loginPasswordEdittext=(EditText)findViewById(R.id.login_password_edittext);

        forgetPasswordTextview=(TextView)findViewById(R.id.forgetpassword_textview);
        changePasswordTextview=(TextView)findViewById(R.id.changepassword_textview);

        networkConnection = new NetworkConnection(this);

        telephonyInfo = TelephonyInfo.getInstance(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //  tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        tmDevice = getUniqueID();

    //    Log.e("tmDevice", tmDevice);

         imsiSIM1 = telephonyInfo.getImsiSIM1();
         imsiSIM2 = telephonyInfo.getImsiSIM2();

        try {

//            Intent intent = getIntent();
//            if (intent != null) {
//                String exit= intent.getStringExtra("exit");
//
//                if (exit.equals("exit")) {
//                   finish();
//                }
//            }

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String exit = extras.getString("exit");
                if (exit.equals("exit")) {

                    finish();

                }

            }

        }catch (Exception error){

        //    Log.e("login--",error.toString());


        }


        if(imsiSIM1!=null) {
        //    Log.e("imsiSIM1", imsiSIM1);
        }

        if(imsiSIM2!=null) {
        //    Log.e("imsiSIM2", imsiSIM2);
        }


         isSIM1Ready = telephonyInfo.isSIM1Ready();
         isSIM2Ready = telephonyInfo.isSIM2Ready();

         isDualSIM = telephonyInfo.isDualSIM();

        // login();


        if (sharedpreferences.contains(userNameKey)) {
            loginUserNameEdittext.setText(sharedpreferences.getString(userNameKey, ""));
        }
        if (sharedpreferences.contains(passwordKey)) {
            loginPasswordEdittext.setText(sharedpreferences.getString(passwordKey, ""));

        }
        login();


        changePasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
                finish();

            }
        });

        forgetPasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                finish();

            }
        });

        //  forgetPasswordTextview.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        //   changePasswordTextview.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        // httpConnection = new HttpClientWrapper();
    }


    public void login(){
        try {


            if (networkConnection.isNetworkAvailable()) {

                int loginStatus = 0;

                if (sharedpreferences.contains(userNameKey)) {
                    loginUserNameEdittext.setText(sharedpreferences.getString(userNameKey, ""));
                }
                if (sharedpreferences.contains(passwordKey)) {
                    loginPasswordEdittext.setText(sharedpreferences.getString(passwordKey, ""));

                }

                if (sharedpreferences.contains(loginstatusKey)) {
                    //  loginPasswordEdittext.setText(sharedpreferences.getString(passwordKey, ""));

                    loginStatus = sharedpreferences.getInt(loginstatusKey, 0);

                }

                if (loginUserNameEdittext.getText().toString() != null && loginPasswordEdittext.getText().toString() != null) {

                    userName = loginUserNameEdittext.getText().toString();
                    passWord = loginPasswordEdittext.getText().toString();


                    if (userName.length() == 0) {
                        //   Toast.makeText(LoginActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
                    } else if (passWord.length() == 0) {
                        //  Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    } else {

                        //   int loginStatus = LoginStatus.getInstance().getLoginStatus();

                        if (loginStatus == 0) {

                            new CheckValidUser1().execute();

                        }


                    }
                }
            } else {

                Toast.makeText(LoginActivity.this,
                        "Network is not Available. Please Check Your Internet Connection ",
                        Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){

        //    Log.e("login error",e.toString());

        }

    }

    public void logInButtonClickEvent(View view){

        if (networkConnection.isNetworkAvailable()) {

            userName = loginUserNameEdittext.getText().toString();
            passWord = loginPasswordEdittext.getText().toString();

            if(userName.length()==0){
                Toast.makeText(LoginActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
            }
            else if(passWord.length()==0){
                Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
            }
            else {
                try {

//                    if(userName.equalsIgnoreCase("admin") && passWord.equalsIgnoreCase("admin")){
//
//                        startActivity(new Intent(LoginActivity.this, AdminDashBoardActivity.class));
//                        finish();
//
//                    }else {

                        new CheckValidUser().execute();
                 //   }
                }
                catch (Exception e){
            //        Log.e("eeeee==",e.toString());
                }

                //       authenticateUser();

            }
        }
        else{

            Toast.makeText(LoginActivity.this,
                    "Network is not Available. Please Check Your Internet Connection ",
                    Toast.LENGTH_SHORT).show();
        }
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


//    public void forgetPasswordClickEvent(View view){
//
//        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
//        finish();
//
//
//    }



    class CheckValidUser1 extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            try {

                if (networkConnection.isNetworkAvailable()) {
                    Log.e("network1===", networkConnection.isNetworkAvailable() + "");


                    //  Log.e("userName--",userName);
                    // Log.e("passWord--",passWord);
                    //  Log.e("tmDevice--",tmDevice);


                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", userName));
                    params.add(new BasicNameValuePair("password", passWord));
                    params.add(new BasicNameValuePair("imsi1", imsiSIM1));
                    params.add(new BasicNameValuePair("imsi2", imsiSIM2));


                    // getting JSON Object
                    // Note that create product url accepts POST method
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_validate_user,
                            "POST", params);


//                    if (json == null) {
//
//                        pDialog.dismiss();
//                        Looper.prepare();
//                        Toast.makeText(LoginActivity.this,
//                                "Invalid User Name and Password ",
//                                Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }

                    // check log cat fro response
                 //     Log.e("Create Response", json.toString());

                    // check for success tag

                }
//                else {
//                    pDialog.dismiss();
//                    Looper.prepare();
//                    Toast.makeText(LoginActivity.this,
//                            "Network is not Available. Please Check Your Internet Connection ",
//                            Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
            }
            catch (Exception e){

            //    Log.e("error--",e.toString());

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

                if (json == null) {

               //     pDialog.dismiss();
                //    Looper.prepare();
                    Toast.makeText(LoginActivity.this,
                            "Invalid User Name and Password ",
                            Toast.LENGTH_SHORT).show();
              //      Looper.loop();
                }
                else {

                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                    if (success == 1) {

                        user = json.getJSONArray(ApplicationConstants.TAG_USER);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(userNameKey, userName);

                        editor.putString(passwordKey, passWord);

                        editor.commit();

                        for (int i = 0; i < user.length(); i++) {
                            JSONObject c = user.getJSONObject(i);

                            // Storing each json item in variable
                            String userId = c.getString(ApplicationConstants.TAG_USER_ID);
                            String userName = c.getString(ApplicationConstants.TAG_USERNAME);
                            String userRole = c.getString(ApplicationConstants.TAG_USER_DSIGNATION);
                            String cityId = c.getString(ApplicationConstants.TAG_USER_CITY_ID);
                            //     String employeeId = c.getString(ApplicationConstants.TAG_USER_EMPLOYEEID);

                            //      Employee.getInstance().setEmployeeId(employeeId);

                            Employee.getInstance().setUserId(userId);
                            Employee.getInstance().setEmployeeRole(userRole);
                            Employee.getInstance().setCityId(cityId);


                            //   if(userRole.equalsIgnoreCase("Sales Person")) {

                            //    Log.e("username--", userName);
                            //   Log.e("role--", userRole);

//                        int loginStatus = LoginStatus.getInstance().getLoginStatus();
//
//                        Log.e("loginStatus==",loginStatus+"");
//
                            LoginStatus.getInstance().setLoginStatus(1);

//                                startActivity(new Intent(LoginActivity.this, ScheduledVenueActivity.class));
//                                finish();
                            startActivity(new Intent(LoginActivity.this, UserActionActivity.class));
                            finish();

                            //  }
//                        else{
//
//
//                            startActivity(new Intent(LoginActivity.this, AdminDashBoardActivity.class));
//                            finish();
//
//                        }

                        }

                        Log.e("login", "successfully");

                    } else if (success == 0) {

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

                        //    Looper.prepare();

                        Toast.makeText(LoginActivity.this,
                                "Please Enter Valid User Name and Password ",
                                Toast.LENGTH_SHORT).show();
                        loginUserNameEdittext.setText("");
                        loginPasswordEdittext.setText("");


                        //     finish();
                        //    startActivity(new Intent(LoginActivity.this, LoginActivity.class));


                        //    Looper.loop();
                    } else if (success == 2) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    class CheckValidUser extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json = null;

            try {

                if (networkConnection.isNetworkAvailable()) {
                    Log.e("network1===", networkConnection.isNetworkAvailable() + "");


                    //  Log.e("userName--",userName);
                    // Log.e("passWord--",passWord);
                    //  Log.e("tmDevice--",tmDevice);


                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", userName));
                    params.add(new BasicNameValuePair("password", passWord));
                    params.add(new BasicNameValuePair("imsi1", imsiSIM1));
                    params.add(new BasicNameValuePair("imsi2", imsiSIM2));
                    //  params.add(new BasicNameValuePair("device_id", tmDevice));


                    // getting JSON Object
                    // Note that create product url accepts POST method
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_validate_user,
                            "POST", params);

//                    if (json == null) {
//
//                             pDialog.dismiss();
//                              Looper.prepare();
//                        Toast.makeText(LoginActivity.this,
//                                "Invalid User Name and Password ",
//                                Toast.LENGTH_SHORT).show();
//                             Looper.loop();
//                    }



                    // check log cat fro response
                //    Log.e("Create Response", json.toString());

                    // check for success tag

                }
//                else {
//                    pDialog.dismiss();
//                    Looper.prepare();
//                    Toast.makeText(LoginActivity.this,
//                            "Network is not Available. Please Check Your Internet Connection ",
//                            Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                }
            }
            catch (Exception e){

            //    Log.e("error-",e.toString());
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

                if (json == null) {

               //     pDialog.dismiss();
              //      Looper.prepare();
                    Toast.makeText(LoginActivity.this,
                            "Invalid User Name and Password ",
                            Toast.LENGTH_SHORT).show();
               //     Looper.loop();
                }
                else {


                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);

                    if (success == 1) {

                        user = json.getJSONArray(ApplicationConstants.TAG_USER);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(userNameKey, userName);
                        editor.putString(passwordKey, passWord);
                        editor.putInt(loginstatusKey, 0);

                        editor.commit();


                        for (int i = 0; i < user.length(); i++) {
                            JSONObject c = user.getJSONObject(i);

                            // Storing each json item in variable
                            String userId = c.getString(ApplicationConstants.TAG_USER_ID);
                            String userName = c.getString(ApplicationConstants.TAG_USERNAME);
                            String userRole = c.getString(ApplicationConstants.TAG_USER_DSIGNATION);
                            String cityId = c.getString(ApplicationConstants.TAG_USER_CITY_ID);
                            //     String employeeId = c.getString(ApplicationConstants.TAG_USER_EMPLOYEEID);

                            //      Employee.getInstance().setEmployeeId(employeeId);

                            Employee.getInstance().setUserId(userId);
                            Employee.getInstance().setEmployeeRole(userRole);
                            Employee.getInstance().setCityId(cityId);
                            //      if(userRole.equalsIgnoreCase("Sales Person")) {

                            //       Log.e("username--", userName);
                            //       Log.e("role--", userRole);

//                                startActivity(new Intent(LoginActivity.this, ScheduledVenueActivity.class));
//                                finish();
                            startActivity(new Intent(LoginActivity.this, UserActionActivity.class));
                            finish();

                            //      }
//                        else{
//
//
//                            startActivity(new Intent(LoginActivity.this, AdminDashBoardActivity.class));
//                            finish();
//
//                        }

                        }

                        //   Log.e("login", "successfully");

                    } else if (success == 0) {

//                    try {
//                        if ((pDialog != null) && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//
//                    } catch (final IllegalArgumentException e) {
//                        // Handle or log or ignore
//                    } catch (final Exception e) {
//                        // Handle or log or ignore
//                    } finally {
//                        pDialog = null;
//                    }


                        Toast.makeText(LoginActivity.this,
                                "Please Enter Valid User Name and Password ",
                                Toast.LENGTH_SHORT).show();
                        loginUserNameEdittext.setText("");
                        loginPasswordEdittext.setText("");


                        //     finish();
                        //    startActivity(new Intent(LoginActivity.this, LoginActivity.class));


                        //    Looper.loop();
                    } else if (success == 2) {

                    }
                }



            } catch (JSONException e) {

            //    Log.e("json error--",e.toString());

                e.printStackTrace();
            }

        }

    }



//    @Override
//    public void processMessage(Bundle message, ServiceListenerType what) {
//        switch (what) {
//            case LOGIN_USER:
//                userLoginResponse(message);
//                break;
//
//
//            default:
//             //   Util.messageBar(LoginActivity.this, message.getString(FPSDBConstants.RESPONSE_DATA));
//                break;
//        }
//    }

    private void userLoginResponse(Bundle message) {
        try {
            String response = message.getString(FPSDBConstants.RESPONSE_DATA);
            JSONObject json = new JSONObject(response);

            int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
            if (success == 1) {

                user = json.getJSONArray(ApplicationConstants.TAG_USER);

                for (int i = 0; i < user.length(); i++) {
                    JSONObject c = user.getJSONObject(i);

                    String userId = c.getString(ApplicationConstants.TAG_USER_ID);
                    String userName = c.getString(ApplicationConstants.TAG_USERNAME);
                    //   String userRole = c.getString(ApplicationConstants.TAG_USER_ROLE);
                    //   String employeeId = c.getString(ApplicationConstants.TAG_USER_EMPLOYEEID);

                    //   Employee.getInstance().setEmployeeId(employeeId);

                    startActivity(new Intent(LoginActivity.this, ScheduledVenueActivity.class));
                    finish();

                    Log.e("username--", userName);
                    //    Log.e("role--", userRole);
                }
            }

        } catch (Exception e) {
            Log.e("Error", e.toString(), e);
        }
    }

//    private void authenticateUser() {
//        try {
//
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("username", userName));
//            params.add(new BasicNameValuePair("password", passWord));
//
//            if (networkConnection.isNetworkAvailable()) {
//                String url = ApplicationConstants.url_validate_user;
//
//                Log.e("url--",url);
//
//                httpConnection.sendRequest(url, null, ServiceListenerType.LOGIN_USER,
//                        SyncHandler, RequestType.POST, params, this);
//            } else {
//
//            }
//        } catch (Exception e) {
//            Log.e("LoginActivity_userLogin", e.toString(), e);
//        }
//    }
}