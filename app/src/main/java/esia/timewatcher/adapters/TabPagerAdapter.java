package esia.timewatcher.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import esia.timewatcher.HomeFragment;
import esia.timewatcher.SettingsFragment;
import esia.timewatcher.StatsFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return new SettingsFragment();
			case 1:
				return new HomeFragment();
			case 2:
				return new StatsFragment();
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return "Settings";
			case 1:
				return "Home";
			case 2:
				return "Stats";
			default:
				throw new IllegalArgumentException();
		}
	}
}
