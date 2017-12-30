package esia.timewatcher;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import org.joda.time.DateTime;

import esia.timewatcher.adapters.RunningHobbyRecyclerViewAdapter;
import esia.timewatcher.adapters.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseListener;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.Hobby;

public class HomeFragment extends Fragment implements DatabaseListener {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseManager.getInstance().addListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.home_fragment, container, false);

		TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(getContext(), 5);
		Spinner typeSpinner = view.findViewById(R.id.typeSpinner);
		typeSpinner.setAdapter(typeSpinnerAdapter);

		RunningHobbyRecyclerViewAdapter runningHobbiesAdapter =
				new RunningHobbyRecyclerViewAdapter(getContext());
		RecyclerView runningHobbiesRecycler = view.findViewById(R.id.running_hobbies);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		runningHobbiesRecycler.setLayoutManager(layoutManager);
		runningHobbiesRecycler.setAdapter(runningHobbiesAdapter);

		final Handler handler = new Handler();
		Runnable updateRunningList = new Runnable() {
			@Override
			public void run() {
				runningHobbiesAdapter.updateTimers();
				handler.postDelayed(this, 1000);
			}
		};
		this.getActivity().runOnUiThread(updateRunningList);
		handler.post(updateRunningList);

		Button startButton = view.findViewById(R.id.start_button);
		startButton.setOnClickListener((v) -> onStartClick(v));

		return view;
	}

	public void onStartClick(View v) {
		Spinner typeSpinner = getView().findViewById(R.id.typeSpinner);
		long selectedTypeId = typeSpinner.getSelectedItemId();
		Hobby newHobby = new Hobby(new DateTime());
		DatabaseManager.getInstance().createHobby(newHobby, selectedTypeId);
	}


	@Override
	public void onDatabaseChange() {
		RecyclerView runningHobbiesRecycler = getView().findViewById(R.id.running_hobbies);
		RunningHobbyRecyclerViewAdapter adapter = (RunningHobbyRecyclerViewAdapter)
				runningHobbiesRecycler.getAdapter();
		adapter.updateFromDatabase();
	}
}
