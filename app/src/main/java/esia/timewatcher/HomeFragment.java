package esia.timewatcher;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.Arrays;

import esia.timewatcher.adapters.recycler.RunningHobbyRecyclerViewAdapter;
import esia.timewatcher.adapters.recycler.SimpleRecyclerViewAdapter;
import esia.timewatcher.adapters.spinner.Action;
import esia.timewatcher.adapters.spinner.ActionSpinnerAdapter;
import esia.timewatcher.adapters.spinner.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;

public class HomeFragment extends Fragment implements SimpleRecyclerViewAdapter.DialogListener {

	@Override
	public void onDialogRequest(DialogFragment dialog) {
		dialog.show(getFragmentManager(), "dialog");
	}

	private enum  StartAction implements Action {
		HOBBY("New hobby"),
		HOBBY_PLUS("New hobby..."),
		EVENT("New event"),
		EVENT_PLUS("New Event...");

		private String name;

		StartAction(String name) {
			this.name = name;
		}

		@Override
		public long getId() {
			return ordinal();
		}

		@Override
		public String getName() {
			return name;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.home_fragment, container, false);

		TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(getContext());
		Spinner typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
		typeSpinner.setAdapter(typeSpinnerAdapter);

		RunningHobbyRecyclerViewAdapter runningHobbiesAdapter =
				new RunningHobbyRecyclerViewAdapter(getContext());
		RecyclerView runningHobbiesRecycler = (RecyclerView) view.findViewById(R.id.running_hobbies);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		runningHobbiesRecycler.setLayoutManager(layoutManager);
		runningHobbiesRecycler.setAdapter(runningHobbiesAdapter);
		runningHobbiesAdapter.setDialogListener(this);

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

		Spinner startSpinner = (Spinner) view.findViewById(R.id.start_spinner);
		ActionSpinnerAdapter startSpinnerAdapter = new ActionSpinnerAdapter(getContext(),
				Arrays.asList(StartAction.values()));
		startSpinnerAdapter.setButtonClickListener(action -> onStartClick(action));
		startSpinner.setAdapter(startSpinnerAdapter);

		return view;
	}

	public void onStartClick(Action action) {
		Spinner typeSpinner = (Spinner) getView().findViewById(R.id.type_spinner);
		long selectedTypeId = typeSpinner.getSelectedItemId();

		switch (((StartAction)action)) {
			case EVENT:
				Event event = new Event(new DateTime());
				DatabaseManager.getInstance().createEvent(event, selectedTypeId);
				Toast.makeText(getContext(), "Event created", Toast.LENGTH_SHORT).show();
				break;
			case HOBBY:
				Hobby newHobby = new Hobby(new DateTime());
				DatabaseManager.getInstance().createHobby(newHobby, selectedTypeId);
				Toast.makeText(getContext(), "Hobby created", Toast.LENGTH_SHORT).show();
				break;
			case EVENT_PLUS:
				CustomStartDialogFragment.newEventInstance(selectedTypeId)
						.show(getFragmentManager(), "dialog");
				break;
			case HOBBY_PLUS:
				CustomStartDialogFragment.newHobbyInstance(selectedTypeId)
						.show(getFragmentManager(), "dialog");
				break;
		}
	}
}
