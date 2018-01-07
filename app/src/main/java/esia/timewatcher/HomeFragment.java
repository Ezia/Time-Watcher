package esia.timewatcher;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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

	private Spinner typeSpinner;
	private RecyclerView runningHobbiesRecycler;
	private ImageButton startButton;

	private enum  StartAction implements Action {
		HOBBY("New hobby"),
		HOBBY_PLUS("New hobby..."),
		EVENT("New event"),
		EVENT_PLUS("New Event...");

		private final String name;

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
		// view

		View view = inflater.inflate(R.layout.home_fragment, container, false);

		// type spinner

		TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(getContext());
		typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
		typeSpinner.setAdapter(typeSpinnerAdapter);

		// running hobbies recycler view

		RunningHobbyRecyclerViewAdapter runningHobbiesAdapter =
				new RunningHobbyRecyclerViewAdapter(getContext());
		runningHobbiesAdapter.setDialogListener(this);
		runningHobbiesRecycler = (RecyclerView) view.findViewById(R.id.running_hobbies);
		runningHobbiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
		runningHobbiesRecycler.setAdapter(runningHobbiesAdapter);

		// timer update on running hobbies

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

		// start button

		startButton = (ImageButton) view.findViewById(R.id.start_button);
		startButton.setOnClickListener(v -> {
			PopupMenu popup = new PopupMenu(getContext(), v);
			popup.inflate(R.menu.start_menu);
			popup.setOnMenuItemClickListener(item -> onPopupMenuItemClick(item));
			popup.show();
		});

		return view;
	}

	private boolean onPopupMenuItemClick(MenuItem item) {
		long selectedTypeId = typeSpinner.getSelectedItemId();
		switch (item.getItemId()) {
			case R.id.start_hobby_menu_item:
				CustomStartDialogFragment.newHobbyInstance(selectedTypeId)
						.show(getFragmentManager(), "dialog");
				return true;
			case R.id.start_event_menu_item:
				CustomStartDialogFragment.newEventInstance(selectedTypeId)
						.show(getFragmentManager(), "dialog");
				return true;
		}
		return false;
	}

	@Override
	public void onDialogRequest(DialogFragment dialog) {
		dialog.show(getFragmentManager(), "dialog");
	}
}
