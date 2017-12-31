package esia.timewatcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esia.timewatcher.adapters.StoppedHobbyRecyclerViewAdapter;
import esia.timewatcher.database.DatabaseListener;
import esia.timewatcher.database.DatabaseManager;

public class StatsFragment extends Fragment implements DatabaseListener {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseManager.getInstance().addListener(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.stats_fragment, container, false);

		StoppedHobbyRecyclerViewAdapter runningHobbiesAdapter =
				new StoppedHobbyRecyclerViewAdapter(getContext());
		RecyclerView runningHobbiesRecycler = view.findViewById(R.id.stopped_hobbies);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		runningHobbiesRecycler.setLayoutManager(layoutManager);
		runningHobbiesRecycler.setAdapter(runningHobbiesAdapter);
		return view;
	}

	@Override
	public void onDatabaseChange() {
		if (getView() != null) {
			RecyclerView runningHobbiesRecycler = getView().findViewById(R.id.stopped_hobbies);
			((StoppedHobbyRecyclerViewAdapter)runningHobbiesRecycler.getAdapter())
					.updateFromDatabase();
		}
	}
}
