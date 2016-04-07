package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.purpleknot1.Util.ApplicationConstants;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserListActivity extends Activity {

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    JSONArray user = null;

    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    ListView userListview;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        userListview =(ListView)findViewById(R.id.user_listview);

        backButton=(ImageView)findViewById(R.id.userlist_back_imageview);

        networkConnection = new NetworkConnection(this);

        if (networkConnection.isNetworkAvailable()) {

            new SelectUser().execute();
        }
        else{
            // populateScheduleVenueDetails();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserListActivity.this, AdminDashBoardActivity.class));
                finish();

            }
        });

        userListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String userId = Employee.getInstance().getUserId();

             //   String employeeId=Employee.getInstance().getEmployeeId();

                TextView userNameTextview = (TextView) view.findViewById(R.id.username_texiview);

                Log.e("userNameTextview", userNameTextview.getText().toString());

                TextView employeeidTextview = (TextView) view.findViewById(R.id.employeeid_textview);

                Log.e("employeeidTextview",employeeidTextview.getText().toString());

                Employee.getInstance().setUserId(employeeidTextview.getText().toString());

                Intent intent = new Intent(UserListActivity.this,ScheduledVenue1Activity.class);
                intent.putExtra("employeeId",userId);
                intent.putExtra("admin","admin");
                startActivity(intent);



            }
        });


    }

    class SelectUser extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserListActivity.this);
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

                    user = json.getJSONArray(ApplicationConstants.TAG_USERS);

                    for (int i = 0; i < user.length(); i++) {
                        JSONObject c = user.getJSONObject(i);

                        String userName = c.getString(ApplicationConstants.TAG_USERSNAME);
                        String userRole = c.getString(ApplicationConstants.TAG_USERSROLE);
                        String userId = c.getString(ApplicationConstants.TAG_USERID);


                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(ApplicationConstants.TAG_USERSNAME, userName);
                        map.put(ApplicationConstants.TAG_USERSROLE, userRole);
                        map.put(ApplicationConstants.TAG_USERID, userId);


                        oslist.add(map);

                        ListAdapter adapter = new SimpleAdapter(UserListActivity.this, oslist,
                                R.layout.column_usr,
                                new String[]{ApplicationConstants.TAG_USERSNAME, ApplicationConstants.TAG_USERSROLE, ApplicationConstants.TAG_USERID}, new int[]{
                                R.id.username_texiview, R.id.userrole_textview, R.id.employeeid_textview});

                        userListview.setAdapter(adapter);

                    }

                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
