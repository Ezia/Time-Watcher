package esia.timewatcher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import esia.timewatcher.adapters.recycler.EventRecyclerViewAdapter;
import esia.timewatcher.adapters.recycler.StoppedHobbyRecyclerViewAdapter;
import esia.timewatcher.adapters.spinner.TypeSpinnerAdapter;

public class StatsFragment extends Fragment {

	private Spinner typeSpinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.stats_fragment, container, false);

		typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
		typeSpinner.setAdapter(new TypeSpinnerAdapter(getContext()));

		return view;
	}
}
