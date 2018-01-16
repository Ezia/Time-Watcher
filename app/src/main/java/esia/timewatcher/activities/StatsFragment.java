package esia.timewatcher.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;

import org.joda.time.Duration;

import java.util.ArrayList;

import esia.timewatcher.R;
import esia.timewatcher.adapters.recycler.EventRecyclerViewAdapter;
import esia.timewatcher.adapters.recycler.StoppedHobbyRecyclerViewAdapter;
import esia.timewatcher.adapters.spinner.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Type;
import esia.timewatcher.utils.StatsUtils;
import esia.timewatcher.utils.TimeUtils;
import esia.timewatcher.view.NumericalStatView;

public class StatsFragment extends Fragment {

	private Spinner typeSpinner;
	private TableLayout eventStatsTable;
	private TableLayout hobbyStatstable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.stats_fragment, container, false);

		typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
		typeSpinner.setAdapter(new TypeSpinnerAdapter(getContext()));

		eventStatsTable = (TableLayout) view.findViewById(R.id.event_stats_table);

		hobbyStatstable = (TableLayout) view.findViewById(R.id.hobby_stats_table);

		ImageButton statsButton = (ImageButton) view.findViewById(R.id.stats_button);
		statsButton.setOnClickListener(v -> updateStats());

		return view;
	}

	private void updateStats() {
		long typeId = typeSpinner.getSelectedItemId();

		ArrayList<EventData> eventDataList =
				DatabaseManager.getInstance().requestEvents(typeId, true);

		NumericalStatView eventDayFreq = new NumericalStatView(getContext());
		eventDayFreq.setStatName("Times per day");
		eventDayFreq.setStatValue(String.valueOf(
				StatsUtils.computeEventFrequency(eventDataList, Duration.standardDays(1))
		));

		NumericalStatView eventWeekFreq = new NumericalStatView(getContext());
		eventWeekFreq.setStatName("Times per week");
		eventWeekFreq.setStatValue(String.valueOf(
				StatsUtils.computeEventFrequency(eventDataList, Duration.standardDays(7))
		));

		ArrayList<HobbyData> hobbyDataList =
				DatabaseManager.getInstance().requestStoppedHobbies(typeId, true);

		NumericalStatView hobbyDayFreq = new NumericalStatView(getContext());
		hobbyDayFreq.setStatName("Time per day");
		hobbyDayFreq.setStatValue(String.valueOf(
				TimeUtils.toSimpleString(
				StatsUtils.computeHobbyDurationFrequency(hobbyDataList, Duration.standardDays(1))
						.toPeriod().normalizedStandard()
				)
		));

		NumericalStatView hobbyWeekFreq = new NumericalStatView(getContext());
		hobbyWeekFreq.setStatName("Time per week");
		hobbyWeekFreq.setStatValue(String.valueOf(
				TimeUtils.toSimpleString(
				StatsUtils.computeHobbyDurationFrequency(hobbyDataList, Duration.standardDays(7))
						.toPeriod().normalizedStandard()
				)
		));

		eventStatsTable.removeAllViews();

		eventStatsTable.addView(eventDayFreq);
		eventStatsTable.addView(eventWeekFreq);

		hobbyStatstable.removeAllViews();

		hobbyStatstable.addView(hobbyDayFreq);
		hobbyStatstable.addView(hobbyWeekFreq);
	}
}
