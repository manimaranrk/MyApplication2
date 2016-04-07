package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChangePasswordActivity extends Activity {

    EditText userNameEdittext,oldPasswordEdittext,newPasswordEdittext1,newPasswordEdittext2;

    Button createPasswordButton;

    String userName,oldPassword,newPassword1,newPassword2;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    NetworkConnection networkConnection;

    ImageView backButtonImageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        networkConnection = new NetworkConnection(this);

        userNameEdittext=(EditText)findViewById(R.id.createpassword_usernme_edittext);
        oldPasswordEdittext=(EditText)findViewById(R.id.createpassword_oldpassword_edittext1);
        newPasswordEdittext1=(EditText)findViewById(R.id.createpassword_newpassword_edittext1);
        newPasswordEdittext2=(EditText)findViewById(R.id.createpassword_newpassword_edittext2);

        backButtonImageview=(ImageView)findViewById(R.id.change_back_imageview);

        createPasswordButton=(Button)findViewById(R.id.create_submit_button);


        backButtonImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                finish();

            }
        });

        createPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                userName=userNameEdittext.getText().toString();
                oldPassword=oldPasswordEdittext.getText().toString();
                newPassword1=newPasswordEdittext1.getText().toString();
                newPassword2=newPasswordEdittext2.getText().toString();

                if(newPassword1.equalsIgnoreCase(newPassword2))
                {
                    new changePassword().execute();

                }
                else
                {

                    newPasswordEdittext1.setText("");
                    newPasswordEdittext2.setText("");

                    Toast.makeText(ChangePasswordActivity.this,
                            "Please Enter New Password",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    class changePassword extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePasswordActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject json=null;

            if (networkConnection.isNetworkAvailable()) {
                Log.e("network1===", networkConnection.isNetworkAvailable() + "");



                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", userName));
                params.add(new BasicNameValuePair("oldpassword", oldPassword));
                params.add(new BasicNameValuePair("newpassword", newPassword1));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_change_password,
                        "POST", params);

                // check log cat fro response
        //        Log.d("Create Response", json.toString());

                // check for success tag
//                try {
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                    if (success == 1) {
//
//
//
//
//                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
//                        finish();
//                        pDialog.dismiss();
//                        Looper.prepare();
//                        Toast.makeText(ChangePasswordActivity.this,
//                                "Password Created Succesfully",
//                                Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//
//                        Log.e("login", "successfully");
//
//                    }
//                    else if(success == 0) {
//
//                        pDialog.dismiss();
//                        Looper.prepare();
//                        Toast.makeText(ChangePasswordActivity.this,
//                                "Enter Valid User Name and Password ",
//                                Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }
//                    else if(success == 2) {
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(ChangePasswordActivity.this,
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



                    Toast.makeText(ChangePasswordActivity.this,
                            "Password Created Succesfully",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                    finish();
                   // pDialog.dismiss();
                  //  Looper.prepare();

                  //  Looper.loop();

                    Log.e("login", "successfully");

                }
                else if(success == 0) {

                  //  pDialog.dismiss();
                //    Looper.prepare();
                    Toast.makeText(ChangePasswordActivity.this,
                            "Enter Valid User Name and Password ",
                            Toast.LENGTH_SHORT).show();

                userNameEdittext.setText("");
                oldPasswordEdittext.setText("");
                newPasswordEdittext1.setText("");
                newPasswordEdittext2.setText("");
                  //  Looper.loop();
                }
                else if(success == 2) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }







        }

    }
}
