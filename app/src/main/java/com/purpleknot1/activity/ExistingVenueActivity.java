package com.purpleknot1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.purpleknot1.Util.ApplicationConstants;
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
import java.util.List;

public class ExistingVenueActivity extends Activity {


    Spinner stateSpinner,citySpinner,zoneSpinner;

    NetworkConnection networkConnection;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    JSONArray state = null;
    JSONArray city = null;
    List<String> stateList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();

    String stateName,cityName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_venue);

        stateSpinner=(Spinner)findViewById(R.id.existing_venue_state_spinner);
        citySpinner=(Spinner)findViewById(R.id.existing_venue_city_spinner);
        zoneSpinner=(Spinner)findViewById(R.id.existing_venue_zone_spinner);

        networkConnection = new NetworkConnection(this);


        if (networkConnection.isNetworkAvailable())
        {

            new SelectState().execute();
        }
        else
        {

        }

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                cityName = parent.getItemAtPosition(position).toString();

            //    new SelectCity().execute();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                stateName = parent.getItemAtPosition(position).toString();

                new SelectCity().execute();

                cityList.clear();



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExistingVenueActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, cityList);

                citySpinner.setAdapter(adapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }




    class SelectState extends AsyncTask<String, String, JSONObject>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(ExistingVenueActivity.this);
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
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_state,
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

                    state = json.getJSONArray(ApplicationConstants.TAG_EXISTING_STATE);

                    for(int i = 0; i < state.length(); i++ ){
                        JSONObject c = state.getJSONObject(i);
                        String stateName = c.getString(ApplicationConstants.TAG_EXISTING_STATE_NAME);

                        if(stateList.size()==0){
                            stateList.add(0,"Select State");
                        }
                      //  else {
                            stateList.add(stateName);


//                        Set<String> hs = new HashSet<>();
//                        hs.addAll(stateList);
//                        stateList.clear();
//                        stateList.addAll(hs);

                            stateSpinner.setPrompt("Select State");

                            String defaultTextForSpinner = "Select State";
                            String[] stockArr = new String[stateList.size()];
                            stockArr = stateList.toArray(stockArr);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExistingVenueActivity.this,
                                    android.R.layout.simple_spinner_item, stateList);

                            stateSpinner.setAdapter(adapter);
                        }
                       // stateSpinner.setAdapter(new CustomSpinnerAdapter(ExistingVenueActivity.this, R.layout.spinner_item, stockArr, defaultTextForSpinner));
        //            }


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
            pDialog = new ProgressDialog(ExistingVenueActivity.this);
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
                    params.add(new BasicNameValuePair("state_name", stateName));
                    json = jsonParser.makeHttpRequest(ApplicationConstants.url_select_city_by_state,
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

                    for(int i = 0; i < city.length(); i++ ){
                        JSONObject c = city.getJSONObject(i);
                        String cityName = c.getString(ApplicationConstants.TAG_CTY_NME);
                        cityList.add(cityName);
                        citySpinner.setPrompt("Select City");
//                        String defaultTextForSpinner = "Select City";
//                        String[] stockArr = new String[cityList.size()];
//                        stockArr = cityList.toArray(stockArr);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExistingVenueActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, cityList);

                        citySpinner.setAdapter(adapter);

                       // citySpinner.setAdapter(new CustomSpinnerAdapter(ExistingVenueActivity.this, R.layout.spinner_item, stockArr, defaultTextForSpinner));
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
