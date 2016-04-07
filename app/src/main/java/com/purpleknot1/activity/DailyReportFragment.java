package com.purpleknot1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.purpleknot1.Adapter.DailyReportArrayAdapter;
import com.purpleknot1.DTO.ScheduleVenueListDto;
import com.purpleknot1.Util.ApplicationConstants;

import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.SelectedDate;
import com.purpleknot1.Adapter.DailyReportArrayAdapter;
import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.SelectedDate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;


public class DailyReportFragment extends Fragment implements OnRefreshListener{

	SwipeRefreshLayout swipeLayout;

	NetworkConnection networkConnection;

	DailyReportArrayAdapter dailyReportArrayAdapter;
	ScheduleVenueListDto scheduleVenueListDto;
	ListView dailyReportListview;
	ImageView calenderViewButton,backImageview;
	View rootView;
	private PopupWindow pwindo;

	String dateString;
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	JSONArray daily_report = null;



	//ArrayList<HashMap<String, String>> reportList =null;

	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();


	HashMap<String, String> map = new HashMap<String, String>();

	String selectedDate=null;
//	CalendarViewActivity calendarView;
	//int day,mon,yer;

	PopupMenu popupMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_daily_report, container, false);
		TextView t = (TextView) rootView.findViewById(R.id.dailyreport_date_textview);
		dailyReportListview=(ListView) rootView.findViewById(R.id.dailyreport_listview);
		calenderViewButton=(ImageView) rootView.findViewById(R.id.calenderview);

		backImageview=(ImageView) rootView.findViewById(R.id.daily_back_imageview);

		networkConnection = new NetworkConnection(getActivity());

		scheduleVenueListDto=new ScheduleVenueListDto();

		selectedDate= SelectedDate.getInstance().getSelectedDate();

		if(selectedDate!=null){

			populateDailyReport(selectedDate);

			//populateScheduleVenueDetails(selectedDate);
		}
		else{
			Log.e("date--","null");
		}

		backImageview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), UserActionActivity.class);
				startActivity(intent);
				getActivity().finish();

			}
		});

		calenderViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), CalendarViewActivity.class);
				startActivity(intent);
				getActivity().finish();

			}
		});

		return rootView;
	}

	@Override
	public void onRefresh() {
	//	new LoadReport().execute();
	}

	private void populateDailyReport(String selectedDate){

		Log.e("selectedDate-==",selectedDate);

		//String string = "004-034556";
		String[] parts = selectedDate.split("-");
		String yer = parts[0]; // 004
		String mon = parts[1]; // 034556
		String dat = parts[2];

		int year = Integer.parseInt(yer);
		int month = Integer.parseInt(mon);
		int date = Integer.parseInt(dat);



		if(month<=9){
			String datString=null;
			if(date<=9){
				 datString="0"+date;
			}
			else{
				datString=""+date;
			}

			dateString=year+"-0"+month+"-"+datString;


		}else{
			String datString=null;
			if(date<=9){
				datString="0"+date;
			}
			else{
				datString=""+date;
			}
			dateString=year+"-"+month+"-"+datString;
		}

//		adapter = new SimpleAdapter(getActivity(), null,R.layout.column_daily_report,new String[] {}, new int[] {R.id.daily_report_id_texiview,
//				R.id.daily_report_venue_name_textview,R.id.daily_report_venue_status_textview,
//				R.id.daily_report_venue_date_textview});
//
//		adapter.notifyDataSetChanged();

		oslist.clear();

		new LoadReport().execute();

	}


	class CheckValidUser extends AsyncTask<String, String, JSONObject> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
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
				Toast.makeText(getActivity(),
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


				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}


	class LoadReport extends AsyncTask<String, String, JSONObject> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Report ...");
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
				String userId = Employee.getInstance().getUserId();

				Log.e("userId--", userId);

				//String employeeID = Employee.getInstance().getEmployeeId();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("employeeid", userId));
				params.add(new BasicNameValuePair("scheduledate", dateString));


				// getting JSON Object
				// Note that create product url accepts POST method
				json = jsonParser.makeHttpRequest(ApplicationConstants.url_daily_report,
						"POST", params);

				// check log cat fro response
				Log.d("Create Response", json.toString());

				// check for success tag

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

					daily_report = json.getJSONArray(ApplicationConstants.TAG_DAILY_REPORT);

					for (int i = 0; i < daily_report.length(); i++) {
						JSONObject c = daily_report.getJSONObject(i);

						String venueId = c.getString(ApplicationConstants.TAG_VENUEID);
						String visitId = c.getString(ApplicationConstants.TAG_VISITID);
						String venueName = c.getString(ApplicationConstants.TAG_VENUENAME);
						String employeeId=c.getString(ApplicationConstants.TAG_USERRID);
						String currentStatus = c.getString(ApplicationConstants.TAG_CURRENTSTATUS);
						String postVisitedStatus = c.getString(ApplicationConstants.TAG_POSTVISITSTATUS);
						String scheduleDate = c.getString(ApplicationConstants.TAG_SCHEDULEDATE);
						String lastVisitedDate = c.getString(ApplicationConstants.TAG_LASTVISITDATE);
						String visitNumber = c.getString(ApplicationConstants.TAG_VISITNUMBER);
						String scheduleStatus = c.getString(ApplicationConstants.TAG_SCHEDULESTATUS);

						HashMap<String, String> map = new HashMap<String, String>();

						map.put(ApplicationConstants.TAG_VENUEID, venueId);
						map.put(ApplicationConstants.TAG_VISITID, visitId);
						map.put(ApplicationConstants.TAG_VENUENAME, venueName);
						map.put(ApplicationConstants.TAG_USERRID, employeeId);
						map.put(ApplicationConstants.TAG_CURRENTSTATUS, currentStatus);
						map.put(ApplicationConstants.TAG_POSTVISITSTATUS, postVisitedStatus);
						map.put(ApplicationConstants.TAG_SCHEDULEDATE, scheduleDate);
						map.put(ApplicationConstants.TAG_LASTVISITDATE, lastVisitedDate);
						map.put(ApplicationConstants.TAG_VISITNUMBER, visitNumber);
						map.put(ApplicationConstants.TAG_SCHEDULESTATUS, scheduleStatus);

						oslist.add(map);

						ListAdapter adapter = new SimpleAdapter(getActivity(), oslist,
								R.layout.column_schedule,
								new String[] {ApplicationConstants.TAG_VISITID,ApplicationConstants.TAG_VENUENAME,ApplicationConstants.TAG_CURRENTSTATUS, ApplicationConstants.TAG_LASTVISITDATE }, new int[] {
								R.id.schedulevenue_id_texiview,R.id.schedulevenue_name_textview,R.id.schedulevenue_ststus_textview, R.id.schedulevenue_date_textview});

						dailyReportListview.setAdapter(adapter);

					}
					SelectedDate.getInstance().setSelectedDate(null);

				} else {
				}


			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}


	private void populateScheduleVenueDetails(String selectedDate){

	}
}
