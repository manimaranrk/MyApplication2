package com.purpleknot1.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.purpleknot1.Util.ApplicationConstants;

import com.purpleknot1.Util.Employee;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.SelectedDate;
import com.purpleknot1.Util.ApplicationConstants;
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

public class WeeklyReportFragment extends Fragment {

	ListView weeklyReportListview;

	NetworkConnection networkConnection;

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	JSONArray daily_report = null;

	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

	Button weeklyReportbtn;

	ImageView backImageView;

	PopupMenu popupMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_weekly_report, container, false);

		weeklyReportListview=(ListView) rootView.findViewById(R.id.weeklyeport_listview);

		weeklyReportbtn=(Button) rootView.findViewById(R.id.weekly_report_btn);

		backImageView=(ImageView) rootView.findViewById(R.id.weekly_back_imageview);

		networkConnection = new NetworkConnection(getActivity());
		//populateScheduleVenueDetails1();

		backImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), UserActionActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});

		weeklyReportbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				oslist.clear();

				new LoadReport().execute();

			}
		});
		
		return rootView;
	}

//	public void menuButtonClickEvent(View view){
//
////		popupMenu = new PopupMenu(getActivity(), view);
////		popupMenu.inflate(R.menu.menu_schedule_venue);
////		popupMenu.show();
////
////		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
////			@Override
////			public boolean onMenuItemClick(MenuItem item) {
////				switch (item.getItemId()) {
//////                    case R.id.menu_venue:
//////
//////                        startActivity(new Intent(ScheduledVenueActivity.this, CreateVenueActivity.class));
//////
//////                        break;
////
////					case R.id.menu_report:
////
////						Log.e("-logout-", "-logout-");
////
////						new CheckValidUser().execute();
////
////						startActivity(new Intent(getActivity(), LoginActivity.class));
////
////						getActivity().finish();
////
////						//   startActivity(new Intent(ScheduledVenueActivity.this, ReportActivity.class));
////
////						break;
////
////				}
////				return true;
////			}
////		});
//	}


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

	private void populateDailyReport(){





	}



	class LoadReport extends AsyncTask<String, String, JSONObject> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
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
		 */
		protected JSONObject doInBackground(String... args) {

			JSONObject json = null;

			if (networkConnection.isNetworkAvailable()) {
				Log.e("network1===", networkConnection.isNetworkAvailable() + "");

				String userId = Employee.getInstance().getUserId();

			//	String employeeID = Employee.getInstance().getEmployeeId();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("employeeid", userId));


				// getting JSON Object
				// Note that create product url accepts POST method
				json = jsonParser.makeHttpRequest(ApplicationConstants.url_weekly_report,
						"POST", params);

				// check log cat fro response
				Log.d("-- Response", json.toString());

				// check for success tag

			}

			return json;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 **/
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
						String employeeId = c.getString(ApplicationConstants.TAG_USERRID);
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
								new String[] {ApplicationConstants.TAG_VISITID,ApplicationConstants.TAG_VENUENAME,ApplicationConstants.TAG_CURRENTSTATUS, ApplicationConstants.TAG_SCHEDULEDATE }, new int[] {
								R.id.schedulevenue_id_texiview,R.id.schedulevenue_name_textview,R.id.schedulevenue_ststus_textview, R.id.schedulevenue_date_textview});




						weeklyReportListview.setAdapter(adapter);


					}
					SelectedDate.getInstance().setSelectedDate(null);

				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


}
