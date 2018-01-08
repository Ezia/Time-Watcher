package esia.timewatcher.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;

import esia.timewatcher.HomeFragment;
import esia.timewatcher.ListsFragment;
import esia.timewatcher.SettingsFragment;
import esia.timewatcher.StatsFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return new HomeFragment();
			case 1:
				return new ListsFragment();
			case 2:
				return new StatsFragment();
			case 3:
				return new SettingsFragment();
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return "Home";
			case 1:
				return "Lists";
			case 2:
				return "Stats";
			case 3:
				return "Settings";
			default:
				throw new IllegalArgumentException();
		}
	}
}
