package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdatePasswordActivity extends Activity {

    EditText newPasswordEdittext1,newPasswordEdittext2;

    String password1,password2;

    String userName;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    NetworkConnection networkConnection;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);

        networkConnection = new NetworkConnection(this);

        newPasswordEdittext1=(EditText)findViewById(R.id.changepassword_password_edittext);
        newPasswordEdittext2=(EditText)findViewById(R.id.changepassword_password_edittext1);

        backButton=(ImageView)findViewById(R.id.update_back_imageview);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdatePasswordActivity.this, ForgetPasswordActivity.class));
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            userName = extras.getString("username");
        }

    }

    public void changeButtonClickEvent(View view){

        password1 = newPasswordEdittext1.getText().toString();
        password2 = newPasswordEdittext2.getText().toString();

        if(password1.equalsIgnoreCase(password2)){


            new changePassword().execute();




        }
        else{

            newPasswordEdittext1.setText("");
            newPasswordEdittext2.setText("");
            Toast.makeText(UpdatePasswordActivity.this,
                    "Enter Password Correctly. ",
                    Toast.LENGTH_SHORT).show();
        }
    }


    class changePassword extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpdatePasswordActivity.this);
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
                params.add(new BasicNameValuePair("password", password1));


                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser.makeHttpRequest(ApplicationConstants.url_create_password,
                        "POST", params);

                // check log cat fro response
            //    Log.d("Create Response", json.toString());

                // check for success tag

            }
            else {
                pDialog.dismiss();
                Looper.prepare();
                Toast.makeText(UpdatePasswordActivity.this,
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


                    pDialog.dismiss();
                    Looper.prepare();
                    Toast.makeText(UpdatePasswordActivity.this,
                            "Password Created Succesfully",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();

                    startActivity(new Intent(UpdatePasswordActivity.this, LoginActivity.class));
                    finish();

                    Log.e("login", "successfully");

                }
                else if(success == 0) {

                    pDialog.dismiss();
                    Looper.prepare();
                    Toast.makeText(UpdatePasswordActivity.this,
                            "Forget Password is Failed ",
                            Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                else if(success == 2) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
