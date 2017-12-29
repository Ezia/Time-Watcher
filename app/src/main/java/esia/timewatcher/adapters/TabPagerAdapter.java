package esia.timewatcher.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by esia on 29/12/17.
 */

public class TabPagerAdapter extends PagerAdapter {

	public TabPagerAdapter() {
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return false;
	}
}
