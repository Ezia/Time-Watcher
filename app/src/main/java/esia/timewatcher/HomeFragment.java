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
import android.widget.Toast;

import org.joda.time.DateTime;

import esia.timewatcher.adapters.RunningHobbyRecyclerViewAdapter;
import esia.timewatcher.adapters.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseListener;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.Event;
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

		TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(getContext());
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
		startButton.setOnLongClickListener((v) -> onStartLongClick(v));

		return view;
	}

	public void onStartClick(View v) {
		Spinner typeSpinner = getView().findViewById(R.id.typeSpinner);
		long selectedTypeId = typeSpinner.getSelectedItemId();
		Hobby newHobby = new Hobby(new DateTime());
		DatabaseManager.getInstance().createHobby(newHobby, selectedTypeId);
		Toast toast = Toast.makeText(getContext(), "Hobby created", Toast.LENGTH_SHORT);
		toast.show();
	}

	public boolean onStartLongClick(View v) {
		Spinner typeSpinner = getView().findViewById(R.id.typeSpinner);
		long selectedTypeId = typeSpinner.getSelectedItemId();
		Event newEvent = new Event(new DateTime());
		DatabaseManager.getInstance().createEvent(newEvent, selectedTypeId);
		Toast toast = Toast.makeText(getContext(), "Event created", Toast.LENGTH_SHORT);
		toast.show();
		return true;
	}

	@Override
	public void onDatabaseChange() {
		if (getView() != null) {
			RecyclerView runningHobbiesRecycler = getView().findViewById(R.id.running_hobbies);
			RunningHobbyRecyclerViewAdapter adapter = (RunningHobbyRecyclerViewAdapter)
					runningHobbiesRecycler.getAdapter();
			adapter.updateFromDatabase();
		}
	}
}
