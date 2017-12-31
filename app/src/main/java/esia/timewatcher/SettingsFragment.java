package esia.timewatcher;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import esia.timewatcher.adapters.TypeIconSpinnerAdapter;
import esia.timewatcher.adapters.TypeRecyclerViewAdapter;


public class SettingsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.settings_fragment, container, false);

		RecyclerView typeRecyclerView = view.findViewById(R.id.type_recycler_view);
		typeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		typeRecyclerView.setAdapter(new TypeRecyclerViewAdapter(getContext()));

		Spinner typeIconSpinner = view.findViewById(R.id.type_icon_spinner);
		typeIconSpinner.setAdapter(new TypeIconSpinnerAdapter(getContext()));

		return view;
	}
}
