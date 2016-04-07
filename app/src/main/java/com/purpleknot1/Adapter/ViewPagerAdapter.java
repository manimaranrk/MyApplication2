package com.purpleknot1.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purpleknot1.activity.R;
import com.purpleknot1.activity.ScheduledVenueActivity;
import com.purpleknot1.activity.UpdateSameStatusActivity;
import com.purpleknot1.activity.UpdateVenueDetailsActivity;
import com.purpleknot1.activity.UpdateVenueStatusActivit;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.activity.ScheduledVenueActivity;
import com.purpleknot1.activity.UpdateVenueStatusActivit;

import java.math.BigDecimal;

public class ViewPagerAdapter extends PagerAdapter implements LocationListener {
	// Declare Variables

	//ViewPagerAdapter adapter;
	Context context;
	String[] venueId;
	String[] visitId;
	String[] userId;
	String[] currentStatus;
	String[] postVisitStatus;
	String[] scheduleDate;
	String[] lastVisitDate;
	String[] visitNumber;
	String[] scheduleStatus;
	String[] venueNames;
	//String[] managerMessages;
	Integer[] venueCount;

	String[] specialStatus1;
	String[] specialStatus2;
	String[] specialStatus3;
	String[] specialStatus4;
	String[] specialStatus5;

	String[] locationStatusArray;

	String[] latitudeArray;
	String[] longitudeArray;

	double distance=0.0;

	double lati,longi;


	private LocationManager locationManager;

	int pos;

	Button updateGpsButton,sameStatusButton;

	NetworkConnection networkConnection;

	String provider;

	String venueName;


	int count;

	double current_latitude,current_longitude;

	Activity activity;

	LayoutInflater inflater;

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();




//	venueId, visitId, userId,currentStatus,
//	postVisitStatus,scheduleDate,lastVisitDate,visitNumber,scheduleStatus,venueNames
 
	public ViewPagerAdapter(Context context, String[] venueId, String[] visitId, String[] userId,
							String[] currentStatus, String[] postVisitStatus,String[] scheduleDate,
							String[] lastVisitDate, String[] visitNumber,String[] scheduleStatus,
							String[] venueNames,Integer[] venueCount,  String[] specialStatus1,
							String[] specialStatus2,String[] specialStatus3,String[] specialStatus4,
							String[] specialStatus5,String[] locationStatusArray,String[] latitudeArray,String[] longitudeArray,double lati,double longi) {


		this.context = context;

		networkConnection = new NetworkConnection(context);

		activity = (Activity) context;

		this.venueId = venueId;
		this.visitId = visitId;
		this.userId = userId;
		this.currentStatus = currentStatus;
		this.postVisitStatus = postVisitStatus;
		this.scheduleDate = scheduleDate;
		this.lastVisitDate = lastVisitDate;
		this.visitNumber = visitNumber;
		this.scheduleStatus = scheduleStatus;
		this.venueNames = venueNames;
		//this.managerMessages = managerMessages;
		this.venueCount = venueCount;

		this.specialStatus1 = specialStatus1;
		this.specialStatus2 = specialStatus2;
		this.specialStatus3 = specialStatus3;
		this.specialStatus4 = specialStatus4;
		this.specialStatus5 = specialStatus5;
		this.locationStatusArray = locationStatusArray;

		this.latitudeArray = latitudeArray;
		this.longitudeArray = longitudeArray;

		this.lati = lati;
		this.longi = longi;






	}
 
	@Override
	public int getCount() {
		return venueNames.length;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}


 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
 
		// Declare Variables

		final TextView txtvenueid;
		final TextView txtvenuename;
		final TextView txtstatus;
		TextView txtpoststatus;
		//final TextView txtscheduledate;
		final TextView visitIdTextview;
		TextView txtlastvisit;
		//TextView txtmanagermessage;
		final TextView locationStatusTextview,latitudeTextview,longitudeTextview;

		final TextView txt_specialStatus1,txt_specialStatus2,txt_specialStatus3,txt_specialStatus4,txt_specialStatus5,txt_userId;

		TextView countTextview;

		Button updateStatusButton,  updateVenueButton;




		//ImageView imgflag;
 
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.column_viewpager_item, container,
				false);
 
		// Locate the TextViews in viewpager_item.xml
		txtvenueid = (TextView) itemView.findViewById(R.id.venueid_textview);
		txtvenuename = (TextView) itemView.findViewById(R.id.venuename);
		txtstatus = (TextView) itemView.findViewById(R.id.status);
		//txtpoststatus = (TextView) itemView.findViewById(R.id.postvisit);
	//	txtscheduledate = (TextView) itemView.findViewById(R.id.scheduledate);

		visitIdTextview = (TextView) itemView.findViewById(R.id.visit_id_textview);

		locationStatusTextview = (TextView) itemView.findViewById(R.id.location_status);

		//txtlastvisit = (TextView) itemView.findViewById(R.id.lastvisit);
		//txtmanagermessage = (TextView) itemView.findViewById(R.id.managermessage);

		countTextview = (TextView) itemView.findViewById(R.id.count_textview);

		txt_specialStatus1 = (TextView) itemView.findViewById(R.id.special_textview);

		txt_specialStatus2 = (TextView) itemView.findViewById(R.id.special_textview1);

		txt_specialStatus3 = (TextView) itemView.findViewById(R.id.special_textview2);

		txt_specialStatus4 = (TextView) itemView.findViewById(R.id.special_textview3);

		txt_specialStatus5 = (TextView) itemView.findViewById(R.id.special_textview4);

		latitudeTextview = (TextView) itemView.findViewById(R.id.latitude_textview);

		longitudeTextview = (TextView) itemView.findViewById(R.id.longitude_textview);

		txt_userId = (TextView) itemView.findViewById(R.id.user_id_textview);


		updateGpsButton = (Button) itemView.findViewById(R.id.update_gps_button);

		updateStatusButton = (Button) itemView.findViewById(R.id.update_status_button);

		sameStatusButton = (Button) itemView.findViewById(R.id.update_samestatus_button);


//		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//
//		Criteria criteria = new Criteria();
//		provider = locationManager.getBestProvider(criteria, false);
//		Location location = locationManager.getLastKnownLocation(provider);
//
//		// Initialize the location fields
//		if (location != null) {
//			System.out.println("Provider " + provider + " has been selected.");
//			onLocationChanged(location);
//		} else {
//			//latituteField.setText("Location not available");
//			//longitudeField.setText("Location not available");
//		}

	//	updateVenueButton = (Button) itemView.findViewById(R.id.update_venue_button);

 
		// Capture position and set to the TextViews
		txtvenueid.setText(venueId[position]);
		txtvenuename.setText(venueNames[position]);
		txtstatus.setText(postVisitStatus[position]);

		visitIdTextview.setText(visitId[position]);

		//txtpoststatus.setText(postVisitStatus[position]);
	//	txtscheduledate.setText(scheduleDate[position]);

		txt_specialStatus1.setText(specialStatus1[position]);
		txt_specialStatus2.setText(specialStatus2[position]);
		txt_specialStatus3.setText(specialStatus3[position]);
		txt_specialStatus4.setText(specialStatus4[position]);
		txt_specialStatus5.setText(specialStatus5[position]);

		latitudeTextview.setText(latitudeArray[position]);
		longitudeTextview.setText(longitudeArray[position]);

		pos = position;

		txt_userId.setText(userId[position]);

		locationStatusTextview.setText(locationStatusArray[position]);

		String loc = locationStatusArray[position];




		if(loc.equals("0")){

			updateGpsButton.setVisibility(View.VISIBLE);

		}
		else{
			updateGpsButton.setVisibility(View.INVISIBLE);

		}

	//	Log.e("location---",locationStatusArray[position]);


		//txtlastvisit.setText(lastVisitDate[position]);
		//txtmanagermessage.setText(managerMessages[position]);

		int length = position+1;

		countTextview.setText(length+"/"+venueNames.length);


		sameStatusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.e("Same status","same status");

//				Intent intent= new Intent(context,UpdateSameStatusActivity.class);
//				intent.putExtra("venueName", venueName);
//				activity.startActivity(intent);
//				activity.finish();

				distance = 0.0;


				double venue_latitude = 0.0;
				double venue_longitude = 0.0;
				String venueName = txtvenuename.getText().toString();
				String venueStatus = txtstatus.getText().toString();
				//	String venueDate = txtscheduledate.getText().toString();
				String visitId = visitIdTextview.getText().toString();
				String userId = txt_userId.getText().toString();
				String venueId = txtvenueid.getText().toString();


				String specialStatus1 = txt_specialStatus1.getText().toString();
				String specialStatus2 = txt_specialStatus2.getText().toString();
				String specialStatus3 = txt_specialStatus3.getText().toString();
				String specialStatus4 = txt_specialStatus4.getText().toString();
				String specialStatus5 = txt_specialStatus5.getText().toString();

				String locationStatus = locationStatusTextview.getText().toString();

				String latitude = latitudeTextview.getText().toString();
				String longitude = longitudeTextview.getText().toString();

				Log.e("location status---", locationStatus);
				Log.e("latitude---", latitude);
				Log.e("longitude---", longitude);

				int locatStatus = Integer.parseInt(locationStatus);

				//	getLocation();







				if(latitude != "null" && latitude != "" && latitude != null && !latitude.equals("") && !latitude.isEmpty()) {

					venue_latitude = Double.parseDouble(latitude);
				}


				if(longitude != "null" && longitude != "" && longitude != null && !longitude.equals("") && !longitude.isEmpty()) {
					venue_longitude = Double.parseDouble(longitude);
				}

				Location locationA = new Location("point A");
				locationA.setLatitude(lati);
				locationA.setLongitude(longi);
				Location locationB = new Location("point B");
				locationB.setLatitude(venue_latitude);
				locationB.setLongitude(venue_longitude);
				distance += locationA.distanceTo(locationB);
				double kilometers = distance * 0.001;


				Log.e("current_latitude",lati+"");
				Log.e("current_longitude",longi+"");
				Log.e("venue_latitude",venue_latitude+"");
				Log.e("venue_longitude",venue_longitude+"");

				Log.e("distance",distance+"");
				Log.e("kilometers",kilometers+"");

				BigDecimal bd = new BigDecimal(kilometers);
				String  val = bd.toPlainString();

				if (lati != 0.0 || longi != 0.0) {


					if (locatStatus == 1) {

						if (distance <= 300) {


							Intent intent = new Intent(context, UpdateSameStatusActivity.class);
							intent.putExtra("venueName", venueName);
							intent.putExtra("visitId", visitId);
							intent.putExtra("venueId", venueId);

							intent.putExtra("venue_latitude", venue_latitude);
							intent.putExtra("venue_longitude", venue_longitude);

							intent.putExtra("userId", userId);
							intent.putExtra("venueStatus", venueStatus);
							intent.putExtra("locationStatus", locationStatus);
							//intent.putExtra("venueDate", venueDate);
							intent.putExtra("specialStatus1", specialStatus1);
							intent.putExtra("specialStatus2", specialStatus2);
							intent.putExtra("specialStatus3", specialStatus3);
							intent.putExtra("specialStatus4", specialStatus4);
							intent.putExtra("specialStatus5", specialStatus5);

							activity.startActivity(intent);
							activity.finish();
						} else {

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);

							// set title
							alertDialogBuilder.setTitle("Purpleknot");

							// set dialog message
							alertDialogBuilder
									.setMessage("Please Update Status Near the Venue")
									.setCancelable(false)
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											dialog.cancel();

										}
									});


							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();

							// show it
							alertDialog.show();


						}


					} else {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set title
						alertDialogBuilder.setTitle("Purpleknot");

						// set dialog message
						alertDialogBuilder
								.setMessage("Please Update the GPS First")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// if this button is clicked, close
										// current activity
										Intent intent = new Intent(context, ScheduledVenueActivity.class);
										// intent.putExtra("flag",0);
										activity.startActivity(intent);
										activity.finish();
										dialog.cancel();

									}
								});


						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					}
				}
				else{

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					// set title
					alertDialogBuilder.setTitle("Purpleknot");

					// set dialog message
					alertDialogBuilder
							.setMessage("Switch on Location in Phone Settings")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									Intent intent = new Intent(context, ScheduledVenueActivity.class);
									// intent.putExtra("flag",0);
									activity.startActivity(intent);
									activity.finish();
									dialog.cancel();


								}
							});



					AlertDialog alertDialog = alertDialogBuilder.create();

					alertDialog.show();



				}






			}
		});


		updateGpsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				venueName = txtvenuename.getText().toString();

				Intent intent= new Intent(context,UpdateVenueDetailsActivity.class);
				intent.putExtra("venueName",venueName);

				activity.startActivity(intent);
				activity.finish();


			}
		});


		updateStatusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				distance = 0.0;

				double venue_latitude = 0.0;
				double venue_longitude = 0.0;
				String venueName = txtvenuename.getText().toString();
				String venueStatus = txtstatus.getText().toString();
			//	String venueDate = txtscheduledate.getText().toString();
				String visitId = visitIdTextview.getText().toString();
				String userId = txt_userId.getText().toString();
				String venueId = txtvenueid.getText().toString();

				String specialStatus1 = txt_specialStatus1.getText().toString();
				String specialStatus2 = txt_specialStatus2.getText().toString();
				String specialStatus3 = txt_specialStatus3.getText().toString();
				String specialStatus4 = txt_specialStatus4.getText().toString();
				String specialStatus5 = txt_specialStatus5.getText().toString();

				String locationStatus = locationStatusTextview.getText().toString();

				String latitude = latitudeTextview.getText().toString();
				String longitude = longitudeTextview.getText().toString();

				Log.e("location status---", locationStatus);
				Log.e("latitude---", latitude);
				Log.e("longitude---", longitude);

				int locatStatus = Integer.parseInt(locationStatus);

			//	getLocation();

				if(latitude != "null" && latitude != "" && latitude != null && !latitude.equals("") && !latitude.isEmpty()) {

					venue_latitude = Double.parseDouble(latitude);
				}

				if(longitude != "null" && longitude != "" && longitude != null && !longitude.equals("") && !longitude.isEmpty()) {
					venue_longitude = Double.parseDouble(longitude);
				}

				Location locationA = new Location("point A");
				locationA.setLatitude(lati);
				locationA.setLongitude(longi);
				Location locationB = new Location("point B");
				locationB.setLatitude(venue_latitude);
				locationB.setLongitude(venue_longitude);
				distance += locationA.distanceTo(locationB);
				double kilometers = distance * 0.001;


				Log.e("current_latitude",lati+"");
				Log.e("current_longitude",longi+"");
				Log.e("venue_latitude",venue_latitude+"");
				Log.e("venue_longitude",venue_longitude+"");

				Log.e("distance",distance+"");
				Log.e("kilometers",kilometers+"");

				BigDecimal bd = new BigDecimal(kilometers);
				String  val = bd.toPlainString();

				if (lati != 0.0 || longi != 0.0) {

					if (locatStatus == 1) {

						if (distance <= 300) {

							Intent intent = new Intent(context, UpdateVenueStatusActivit.class);
							intent.putExtra("venueName", venueName);
							intent.putExtra("visitId", visitId);
							intent.putExtra("venueId", venueId);

							intent.putExtra("venue_latitude", venue_latitude);
							intent.putExtra("venue_longitude", venue_longitude);

							intent.putExtra("userId", userId);
							intent.putExtra("venueStatus", venueStatus);
							intent.putExtra("locationStatus", locationStatus);
							//intent.putExtra("venueDate", venueDate);
							intent.putExtra("specialStatus1", specialStatus1);
							intent.putExtra("specialStatus2", specialStatus2);
							intent.putExtra("specialStatus3", specialStatus3);
							intent.putExtra("specialStatus4", specialStatus4);
							intent.putExtra("specialStatus5", specialStatus5);

							activity.startActivity(intent);
							activity.finish();
						} else {

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									context);

							// set title
							alertDialogBuilder.setTitle("Purpleknot");

							// set dialog message
							alertDialogBuilder
									.setMessage("Please Update Status Near the Venue")
									.setCancelable(false)
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											dialog.cancel();

										}
									});


							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();

							// show it
							alertDialog.show();


						}


					} else {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set title
						alertDialogBuilder.setTitle("Purpleknot");

						// set dialog message
						alertDialogBuilder
								.setMessage("Please Update the GPS First")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// if this button is clicked, close
										// current activity
										Intent intent = new Intent(context, ScheduledVenueActivity.class);
										// intent.putExtra("flag",0);
										activity.startActivity(intent);
										activity.finish();
										dialog.cancel();

									}
								});


						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					}
				}
				else{

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					// set title
					alertDialogBuilder.setTitle("Purpleknot");

					// set dialog message
					alertDialogBuilder
							.setMessage("Switch on Location in Phone Settings")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									// current activity
									Intent intent = new Intent(context, ScheduledVenueActivity.class);
									// intent.putExtra("flag",0);
									activity.startActivity(intent);
									activity.finish();
									dialog.cancel();


								}
							});



					AlertDialog alertDialog = alertDialogBuilder.create();

					alertDialog.show();



				}



			}
		});


//		updateVenueButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//
//				String venueName = txtvenuename.getText().toString();
//				String venueStatus = txtstatus.getText().toString();
//				String venueDate = txtscheduledate.getText().toString();
//
//				Intent intent= new Intent(context,UpdateVenueDetailsActivity.class);
//				intent.putExtra("venueName",venueName);
//				intent.putExtra("venueStatus",venueStatus);
//				intent.putExtra("venueDate", venueDate);
//				activity.startActivity(intent);
//				activity.finish();
//
//			}
//		});
 
		// Locate the ImageView in viewpager_item.xml
	//	imgflag = (ImageView) itemView.findViewById(R.id.flag);
		// Capture position and set to the ImageView
	//	imgflag.setImageResource(flag[position]);
 
		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);
 
		return itemView;
	}

	public void getLocation(){

		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		if(provider!=null && !provider.equals("")){

			// Get the location from the given provider
			Location location = locationManager.getLastKnownLocation(provider);
			locationManager.requestLocationUpdates(provider, 300000, 1, this);

			if(location!=null) {
				onLocationChanged(location);
			}
			else {
				Toast.makeText(context, "Location can't be retrieved", Toast.LENGTH_SHORT).show();
			}

		}else{
			Toast.makeText(context , "No Provider Found", Toast.LENGTH_SHORT).show();
		}
	}
 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);
 
	}

	@Override
	public void onLocationChanged(Location location) {
		current_latitude = (double) (location.getLatitude());
		current_longitude = (double) (location.getLongitude());

		//Log.e("----latitude---",current_latitude+"");
		//Log.e("----longitude---", current_longitude + "");


	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(context, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(context, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}





//	class createVenue extends AsyncTask<String, String, JSONObject> {
//
//		/**
//		 * Before starting background thread Show Progress Dialog
//		 * */
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//
//
//			pDialog = new ProgressDialog(context);
//			pDialog.setMessage("Update Details...");
//
//			pDialog.setCancelable(false);
//			pDialog.show();
//
//		}
//
//		/**
//		 * Creating product
//		 * */
//		protected JSONObject doInBackground(String... args) {
//
//			JSONObject json = null;
//
//			if (networkConnection.isNetworkAvailable()) {
//				Log.e("network1===",networkConnection.isNetworkAvailable()+"");
//
//				List<NameValuePair> params = new ArrayList<NameValuePair>();
//				params.add(new BasicNameValuePair("name", venueName));
//
//				params.add(new BasicNameValuePair("lat", lat+""));
//				params.add(new BasicNameValuePair("lng", lng+""));
//
//
//
//				// getting JSON Object
//				// Note that create product url accepts POST method
//				json = jsonParser.makeHttpRequest(ApplicationConstants.url_update_venue_position,
//						"POST", params);
//
//				// check log cat fro response
//				Log.d("Create Response", json.toString());
//
//				// check for success tag
//
//			}
//			else {
//				pDialog.dismiss();
//				Looper.prepare();
//				Toast.makeText(context,
//						"Network is not Available. Please Check Your Internet Connection ",
//						Toast.LENGTH_SHORT).show();
//				Looper.loop();
//			}
//
//			return json;
//		}
//
//		/**
//		 * After completing background task Dismiss the progress dialog
//		 * **/
//		protected void onPostExecute(JSONObject json) {
//			// dismiss the dialog once done
//			try {
//				if ((pDialog != null) && pDialog.isShowing()) {
//					pDialog.dismiss();
//				}
//
//			} catch (final IllegalArgumentException e) {
//				// Handle or log or ignore
//			} catch (final Exception e) {
//				// Handle or log or ignore
//			} finally {
//				pDialog = null;
//			}
//
//
//			try {
//				int success = json.getInt(ApplicationConstants.TAG_SUCCESS);
//
//				if (success == 1) {
//
//
//
//				//	ViewPagerAdapter.this.notifyDataSetChanged();
//
//					Toast.makeText(context,
//							"GPS Location is Updated for "+venueName,
//							Toast.LENGTH_SHORT).show();
//
////					String loc = locationStatusArray[pos];
////
////
////					if(loc.equals("0")){
////
////						updateGpsButton.setVisibility(View.VISIBLE);
////
////					}
////					else{
////						updateGpsButton.setVisibility(View.INVISIBLE);
////
////					}
//				//	context.startActivity(new Intent(context, com.PurpleKnot.Activity.ScheduledVenueActivity.class));
//
//				//	Intent intent = new Intent(context, com.PurpleKnot.Activity.ScheduledVenueActivity.class);
//
//				//	intent.putExtra("flag",1);
//
//				//	context.startActivity(intent);
//
//					//updateGpsButton.setVisibility(View.INVISIBLE);
//
//				}
//
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//		}
//
//
//
//	}

}