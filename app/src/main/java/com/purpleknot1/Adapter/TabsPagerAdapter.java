package com.purpleknot1.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.purpleknot1.activity.WeeklyReportFragment;
import com.purpleknot1.activity.MonthlyReportFragment;
import com.purpleknot1.activity.DailyReportFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new DailyReportFragment();
		case 1:
			// Games fragment activity
			return new WeeklyReportFragment();
		case 2:
			// Movies fragment activity
			return new MonthlyReportFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
