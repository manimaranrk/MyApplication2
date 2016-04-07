package com.purpleknot1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.purpleknot1.activity.R;

public class ViewPagerAdapter1 extends PagerAdapter {
	// Declare Variables
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
	String[] managerMessages;

	Activity activity;

	LayoutInflater inflater;


//	venueId, visitId, userId,currentStatus,
//	postVisitStatus,scheduleDate,lastVisitDate,visitNumber,scheduleStatus,venueNames

	public ViewPagerAdapter1(Context context, String[] venueId, String[] visitId, String[] userId, String[] currentStatus, String[] postVisitStatus,
							 String[] scheduleDate, String[] lastVisitDate, String[] visitNumber, String[] scheduleStatus, String[] venueNames, String[] managerMessages) {
		this.context = context;

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
		this.managerMessages = managerMessages;




	}
 
	@Override
	public int getCount() {
		return venueNames.length;
	}
 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}
 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
 
		// Declare Variables
		TextView txtvenueid;
		final TextView txtvenuename;
		final TextView txtstatus;
		TextView txtpoststatus;
		final TextView txtscheduledate;
		TextView txtlastvisit;
		TextView txtmanagermessage;

		Button updateStatusButton;


		//ImageView imgflag;
 
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.column_viewpager_item1, container,
				false);
 
		// Locate the TextViews in viewpager_item.xml
		//txtvenueid = (TextView) itemView.findViewById(R.id.venueid);
		txtvenuename = (TextView) itemView.findViewById(R.id.venuename);
		txtstatus = (TextView) itemView.findViewById(R.id.status);
		//txtpoststatus = (TextView) itemView.findViewById(R.id.postvisit);
		txtscheduledate = (TextView) itemView.findViewById(R.id.scheduledate);
		//txtlastvisit = (TextView) itemView.findViewById(R.id.lastvisit);
		txtmanagermessage = (TextView) itemView.findViewById(R.id.managermessage);
	//	updateStatusButton=(Button) itemView.findViewById(R.id.update_status_button);
 
		// Capture position and set to the TextViews
		//txtvenueid.setText(venueId[position]);
		txtvenuename.setText(venueNames[position]);
		txtstatus.setText(currentStatus[position]);
		//txtpoststatus.setText(postVisitStatus[position]);
		txtscheduledate.setText(scheduleDate[position]);
		//txtlastvisit.setText(lastVisitDate[position]);
		txtmanagermessage.setText(managerMessages[position]);

//		updateStatusButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				String venueName = txtvenuename.getText().toString();
//				String venueStatus = txtstatus.getText().toString();
//				String venueDate = txtscheduledate.getText().toString();
//
//				Intent intent= new Intent(context,UpdateVenueStatusActivity.class);
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
 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);
 
	}
}