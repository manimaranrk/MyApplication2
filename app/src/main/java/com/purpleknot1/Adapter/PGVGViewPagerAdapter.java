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

import com.purpleknot1.Util.JSONParser;
import com.purpleknot1.Util.NetworkConnection;
import com.purpleknot1.activity.R;
import com.purpleknot1.activity.ScheduledVenueActivity;
import com.purpleknot1.activity.UpdatePGVGStatusActivity;
import com.purpleknot1.activity.UpdateSameStatusActivity;
import com.purpleknot1.activity.UpdateVenueDetailsActivity;
import com.purpleknot1.activity.UpdateVenueStatusActivit;

import java.math.BigDecimal;

public class PGVGViewPagerAdapter extends PagerAdapter implements LocationListener {
	// Declare Variables

	//ViewPagerAdapter adapter;
	Context context;
	String[] pgNameArr;
	String[] pgVisitIDArr;
	String[] pgIdArr;
	String[] pgUserIdArr;
	String[] pgCurrentStatusArr;
	String[] pgPostVisitStatusArr;
	String[] pgLocationArr;
	String[] pgLatiArr;
	String[] pgLongiArr;

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

	public PGVGViewPagerAdapter(Context context, String[] pgNameArr, String[] pgVisitIDArr, String[] pgIdArr,
								String[] pgUserIdArr, String[] pgCurrentStatusArr, String[] pgPostVisitStatusArr,
								String[] pgLocationArr, String[] pgLatiArr, String[] pgLongiArr) {


		this.context = context;

		networkConnection = new NetworkConnection(context);

		activity = (Activity) context;

		this.pgNameArr = pgNameArr;
		this.pgVisitIDArr = pgVisitIDArr;
		this.pgIdArr = pgIdArr;
		this.pgUserIdArr = pgUserIdArr;
		this.pgCurrentStatusArr = pgCurrentStatusArr;
		this.pgPostVisitStatusArr = pgPostVisitStatusArr;
		this.pgLocationArr = pgLocationArr;
		this.pgLatiArr = pgLatiArr;
		this.pgLongiArr = pgLongiArr;

	}
 
	@Override
	public int getCount() {
		return pgNameArr.length;
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

		final TextView pgNameTextview;
		final TextView pgStatusTextview ,pgcurrentTextview ,pgvisitTextview,pgUserTextview,pgLocationTextview,
				pgLatiTextview,pgLongiTextview,pgIdTextview;


		Button updateStatusButton,  updateVenueButton;
 
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.column_viewpager_pgvg, container,
				false);
 
		// Locate the TextViews in viewpager_item.xml
		pgNameTextview = (TextView) itemView.findViewById(R.id.pgname);

		pgStatusTextview = (TextView) itemView.findViewById(R.id.pgstatus);
		pgcurrentTextview = (TextView) itemView.findViewById(R.id.pg_current_textview);
		pgvisitTextview = (TextView) itemView.findViewById(R.id.pg_visit_id_textview);
		pgUserTextview = (TextView) itemView.findViewById(R.id.pg_user_id_textview);

		pgLocationTextview = (TextView) itemView.findViewById(R.id.pg_location_status);
	//	countTextview = (TextView) itemView.findViewById(R.id.count_textview);
		pgLatiTextview = (TextView) itemView.findViewById(R.id.pg_latitude_textview);
		pgLongiTextview = (TextView) itemView.findViewById(R.id.pg_longitude_textview);
		pgIdTextview = (TextView) itemView.findViewById(R.id.pgid_textview);


		updateGpsButton = (Button) itemView.findViewById(R.id.pg_update_gps_button);
		updateStatusButton = (Button) itemView.findViewById(R.id.pg_update_status_button);
	//	sameStatusButton = (Button) itemView.findViewById(R.id.update_samestatus_button);
 
		// Capture position and set to the TextViews
		pgNameTextview.setText(pgNameArr[position]);
		pgvisitTextview.setText(pgVisitIDArr[position]);
		pgIdTextview.setText(pgIdArr[position]);

		pgUserTextview.setText(pgUserIdArr[position]);
		pgStatusTextview.setText(pgPostVisitStatusArr[position]);
		pgcurrentTextview.setText(pgCurrentStatusArr[position]);

		pgLocationTextview.setText(pgLocationArr[position]);


		pgLatiTextview.setText(pgLatiArr[position]+"");
		pgLongiTextview.setText(pgLongiArr[position]+"");

//		pos = position;



	//	locationStatusTextview.setText(locationStatusArray[position]);

		String loc = pgLocationArr[position];




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

	//	countTextview.setText(length+"/"+venueNames.length);


		updateGpsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				venueName = pgNameTextview.getText().toString();

				Intent intent= new Intent(context,UpdateVenueDetailsActivity.class);
				//intent.putExtra("venueName",venueName);

				activity.startActivity(intent);
				activity.finish();


			}
		});


		updateStatusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				String pgName = pgNameTextview.getText().toString();

				String pgStatus = pgStatusTextview.getText().toString();
				String currentStatus = pgcurrentTextview.getText().toString();
				String visitDetails = pgvisitTextview.getText().toString();
				String user = pgUserTextview.getText().toString();

				String location = pgLocationTextview.getText().toString();
				String latitude = pgLatiTextview.getText().toString();
				String longitude = pgLongiTextview.getText().toString();
				String id = pgIdTextview.getText().toString();


				Intent intent = new Intent(context, UpdatePGVGStatusActivity.class);
				intent.putExtra("pgName", pgName);


				activity.startActivity(intent);
				activity.finish();

//				distance = 0.0;
//
//				double venue_latitude = 0.0;
//				double venue_longitude = 0.0;
//				String venueName = txtvenuename.getText().toString();
//				String venueStatus = txtstatus.getText().toString();
//			//	String venueDate = txtscheduledate.getText().toString();
//				String visitId = visitIdTextview.getText().toString();
//				String userId = txt_userId.getText().toString();
//				String venueId = txtvenueid.getText().toString();
//
//				String specialStatus1 = txt_specialStatus1.getText().toString();
//				String specialStatus2 = txt_specialStatus2.getText().toString();
//				String specialStatus3 = txt_specialStatus3.getText().toString();
//				String specialStatus4 = txt_specialStatus4.getText().toString();
//				String specialStatus5 = txt_specialStatus5.getText().toString();
//
//				String locationStatus = locationStatusTextview.getText().toString();
//
//				String latitude = latitudeTextview.getText().toString();
//				String longitude = longitudeTextview.getText().toString();
//
//				Log.e("location status---", locationStatus);
//				Log.e("latitude---", latitude);
//				Log.e("longitude---", longitude);
//
//				int locatStatus = Integer.parseInt(locationStatus);
//
//			//	getLocation();
//
//				if(latitude != "null" && latitude != "" && latitude != null && !latitude.equals("") && !latitude.isEmpty()) {
//
//					venue_latitude = Double.parseDouble(latitude);
//				}
//
//				if(longitude != "null" && longitude != "" && longitude != null && !longitude.equals("") && !longitude.isEmpty()) {
//					venue_longitude = Double.parseDouble(longitude);
//				}
//
//				Location locationA = new Location("point A");
//				locationA.setLatitude(lati);
//				locationA.setLongitude(longi);
//				Location locationB = new Location("point B");
//				locationB.setLatitude(venue_latitude);
//				locationB.setLongitude(venue_longitude);
//				distance += locationA.distanceTo(locationB);
//				double kilometers = distance * 0.001;
//
//
//				Log.e("current_latitude",lati+"");
//				Log.e("current_longitude",longi+"");
//				Log.e("venue_latitude",venue_latitude+"");
//				Log.e("venue_longitude",venue_longitude+"");
//
//				Log.e("distance",distance+"");
//				Log.e("kilometers",kilometers+"");
//
//				BigDecimal bd = new BigDecimal(kilometers);
//				String  val = bd.toPlainString();
//
//				if (lati != 0.0 || longi != 0.0) {
//
//					if (locatStatus == 1) {
//
//						if (distance <= 300) {
//
//							Intent intent = new Intent(context, UpdateVenueStatusActivit.class);
//							intent.putExtra("venueName", venueName);
//							intent.putExtra("visitId", visitId);
//							intent.putExtra("venueId", venueId);
//
//							intent.putExtra("venue_latitude", venue_latitude);
//							intent.putExtra("venue_longitude", venue_longitude);
//
//							intent.putExtra("userId", userId);
//							intent.putExtra("venueStatus", venueStatus);
//							intent.putExtra("locationStatus", locationStatus);
//							//intent.putExtra("venueDate", venueDate);
//							intent.putExtra("specialStatus1", specialStatus1);
//							intent.putExtra("specialStatus2", specialStatus2);
//							intent.putExtra("specialStatus3", specialStatus3);
//							intent.putExtra("specialStatus4", specialStatus4);
//							intent.putExtra("specialStatus5", specialStatus5);
//
//							activity.startActivity(intent);
//							activity.finish();
//						} else {
//
//							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//									context);
//
//							// set title
//							alertDialogBuilder.setTitle("Purpleknot");
//
//							// set dialog message
//							alertDialogBuilder
//									.setMessage("Please Update Status Near the Venue")
//									.setCancelable(false)
//									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog, int id) {
//											// if this button is clicked, close
//											// current activity
//											dialog.cancel();
//
//										}
//									});
//
//
//							// create alert dialog
//							AlertDialog alertDialog = alertDialogBuilder.create();
//
//							// show it
//							alertDialog.show();
//
//
//						}
//
//
//					} else {
//
//						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//								context);
//
//						// set title
//						alertDialogBuilder.setTitle("Purpleknot");
//
//						// set dialog message
//						alertDialogBuilder
//								.setMessage("Please Update the GPS First")
//								.setCancelable(false)
//								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int id) {
//										// if this button is clicked, close
//										// current activity
//										Intent intent = new Intent(context, ScheduledVenueActivity.class);
//										// intent.putExtra("flag",0);
//										activity.startActivity(intent);
//										activity.finish();
//										dialog.cancel();
//
//									}
//								});
//
//
//						// create alert dialog
//						AlertDialog alertDialog = alertDialogBuilder.create();
//
//						// show it
//						alertDialog.show();
//
//					}
//				}
//				else{
//
//					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//							context);
//
//					// set title
//					alertDialogBuilder.setTitle("Purpleknot");
//
//					// set dialog message
//					alertDialogBuilder
//							.setMessage("Switch on Location in Phone Settings")
//							.setCancelable(false)
//							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,int id) {
//									// if this button is clicked, close
//									// current activity
//									Intent intent = new Intent(context, ScheduledVenueActivity.class);
//									// intent.putExtra("flag",0);
//									activity.startActivity(intent);
//									activity.finish();
//									dialog.cancel();
//
//
//								}
//							});
//
//
//
//					AlertDialog alertDialog = alertDialogBuilder.create();
//
//					alertDialog.show();
//
//
//
//				}



			}
		});


 
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
}