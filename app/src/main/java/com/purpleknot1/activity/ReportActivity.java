package com.purpleknot1.activity;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


import com.purpleknot1.Adapter.TabsPagerAdapter;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;

public class ReportActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    private ProgressDialog pDialog;

    NetworkConnection networkConnection;

    JSONParser jsonParser = new JSONParser();

    // Tab titles
    private String[] tabs = { "Daily Report", "Weekly Report", "Monthly Report" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        networkConnection = new NetworkConnection(this);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

//    @Override
//    public void onBackPressed() {
//
//    //    new CheckValidUser().execute();
//    //    finish();
//
////        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserActionActivity.this);
////        builder1.setMessage("Do You Want Close the Application.");
////        builder1.setCancelable(true);
////        builder1.setPositiveButton("Yes",
////                new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////
////                        new CheckValidUser().execute();
////
////                        finish();
////
////
////                      //  dialog.cancel();
////                    }
////                });
////        builder1.setNegativeButton("No",
////                new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        dialog.cancel();
////                    }
////                });
////
////        AlertDialog alert11 = builder1.create();
////        alert11.show();
//
//    }

//    class CheckValidUser extends AsyncTask<String, String, JSONObject> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(ReportActivity.this);
//            pDialog.setMessage("Login...");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        /**
//         * Creating product
//         * */
//        protected JSONObject doInBackground(String... args) {
//
//            JSONObject json = null;
//
//            if (networkConnection.isNetworkAvailable()) {
//                Log.e("network1===", networkConnection.isNetworkAvailable() + "");
//
//
//                //  Log.e("userName--",userName);
//                // Log.e("passWord--",passWord);
//                //  Log.e("tmDevice--",tmDevice);
//
//                String userId = Employee.getInstance().getUserId();
//
//
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("userid", userId));
//
//                //  params.add(new BasicNameValuePair("device_id", tmDevice));
//
//
//                // getting JSON Object
//                // Note that create product url accepts POST method
//                json = jsonParser.makeHttpRequest(ApplicationConstants.url_update_user_status,
//                        "POST", params);
//
//                // check log cat fro response
//                Log.d("Create Response", json.toString());
//
//                // check for success tag
//
//            }
//            else {
//                pDialog.dismiss();
//                Looper.prepare();
//                Toast.makeText(ReportActivity.this,
//                        "Network is not Available. Please Check Your Internet Connection ",
//                        Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            return json;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(JSONObject json) {
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
//
//
//            try {
//                int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                if (success == 1) {
//
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }

}
