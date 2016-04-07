//package com.PurpleKnot.Services;
//
//
//import android.app.Activity;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.Toast;
//
//
//import LoginActivity;
//import com.PurpleKnot.Activity.R;
//import ApplicationConstants;
//import AsyncRequest;
//
//import Employee;
//import JSONParser;
//import NetworkConnection;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class BackgroundService extends Service implements AsyncRequest.OnAsyncRequestComplete, LocationListener {
//
//    JSONArray user = null;
//
//    double lat,lng;
//
//    private LocationManager locationManager;
//
//    JSONParser jsonParser = new JSONParser();
//
//
//
//
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//
//    private static final String TAG_SUCCESS = "success";
//
//    Activity activity;
//
//    String provider;
//
//
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        Thread notifyingThread = new Thread(null, serviceTask, "BackgroundService");
//        notifyingThread.start();
//
//    }
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//
//    /*Connection device Thread*/
//    private final Runnable serviceTask = new Runnable() {
//        public void run() {
//            Looper.prepare();
//            while (true) {
//                try {
////                    if (SessionId.getInstance().getSessionId().length() > 0) {
//                        NetworkConnection networkConnection = new NetworkConnection(BackgroundService.this);
//                        if (networkConnection.isNetworkAvailable()){
//                            new ConnectionSenddata().execute("");
//                    }
//                    Integer timeout = Integer.parseInt(getString(R.string.serviceTimeout));
//                    Thread.sleep(timeout);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };
//
//    private class ConnectionSenddata extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... f_url) {
//
//            JSONObject json = null;
//
//            try {
//                        getLocation();
//                        params = getParams();
//                        Log.e("parms", params.toString());
//                        json = jsonParser.makeHttpRequest(ApplicationConstants.url_update_user_status,
//                        "POST", params);
//
//                        Log.d("Create Response", json.toString());
//
//                try {
//                    int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//                    if (success == 1) {
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } catch (Exception e) {
//                Log.e("Error:---- ", e.toString());
//            }
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(String file_url) {
//
//        }
//    }
//
//    @Override
//    public void asyncResponse(String response) {
//
//        try {
//            JSONObject json = new JSONObject(response);
//            int success = json.getInt(TAG_SUCCESS);
//            if (success == 1) {
//
//
//
//
//                Log.e("login", "successfully");
//
//            }
//            else if(success == 0) {
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void getLocation(){
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        provider = locationManager.getBestProvider(criteria, false);
//        if(provider!=null && !provider.equals("")){
//
//            // Get the location from the given provider
//            Location location = locationManager.getLastKnownLocation(provider);
//            locationManager.requestLocationUpdates(provider, 2000, 1, this);
//
//            if(location!=null) {
//                onLocationChanged(location);
//            }
//            else {
//                Toast.makeText(BackgroundService.this, "Location can't be retrieved", Toast.LENGTH_SHORT).show();
//            }
//
//        }else{
//            Toast.makeText(BackgroundService.this , "No Provider Found", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        lat = (double) (location.getLatitude());
//        lng = (double) (location.getLongitude());
//
//        Log.e("latitude---",lat+"");
//        Log.e("longitude---", lng + "");
//
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        Toast.makeText(BackgroundService.this, "Enabled new provider " + provider,
//                Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Toast.makeText(BackgroundService.this, "Disabled provider " + provider,
//                Toast.LENGTH_SHORT).show();
//    }
//
//    private ArrayList<NameValuePair> getParams() {
//
//        String userId = Employee.getInstance().getUserId();
//
//        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("userid", userId));
//        params.add(new BasicNameValuePair("latitude", lat+""));
//        params.add(new BasicNameValuePair("longitude", lng+""));
//        return params;
//    }
//
//
//
//    @Override
//    public void onDestroy() {
//
//    }
//}