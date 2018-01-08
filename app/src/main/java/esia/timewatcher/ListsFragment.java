package esia.timewatcher;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import esia.timewatcher.adapters.recycler.EventRecyclerViewAdapter;
import esia.timewatcher.adapters.recycler.StoppedHobbyRecyclerViewAdapter;

/**
 * Created by esia on 08/01/18.
 */

public class ListsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.lists_fragment, container, false);

		StoppedHobbyRecyclerViewAdapter runningHobbiesAdapter =
				new StoppedHobbyRecyclerViewAdapter(getContext());
		RecyclerView runningHobbiesRecycler = (RecyclerView) view.findViewById(R.id.stopped_hobbies);
		runningHobbiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
		runningHobbiesRecycler.setAdapter(runningHobbiesAdapter);

		EventRecyclerViewAdapter eventAdapter =
				new EventRecyclerViewAdapter(getContext());
		RecyclerView eventRecyclerView = (RecyclerView) view.findViewById(R.id.events);
		eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		eventRecyclerView.setAdapter(eventAdapter);

		return view;
	}
}
