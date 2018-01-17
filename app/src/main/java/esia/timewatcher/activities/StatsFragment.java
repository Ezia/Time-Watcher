package esia.timewatcher.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;

import org.joda.time.Duration;

import java.util.ArrayList;

import esia.timewatcher.R;
import esia.timewatcher.adapters.spinner.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.utils.HobbyStatManager;
import esia.timewatcher.utils.StatsUtils;
import esia.timewatcher.utils.TimeUtils;
import esia.timewatcher.view.StatView;

public class StatsFragment extends Fragment {

	private Spinner typeSpinner;
	private TableLayout eventStatsTable;
	private TableLayout hobbyStatsTable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.stats_fragment, container, false);

		typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
		typeSpinner.setAdapter(new TypeSpinnerAdapter(getContext()));

		eventStatsTable = (TableLayout) view.findViewById(R.id.event_stats_table);

		hobbyStatsTable = (TableLayout) view.findViewById(R.id.hobby_stats_table);

		ImageButton statsButton = (ImageButton) view.findViewById(R.id.stats_button);
		statsButton.setOnClickListener(v -> updateStats());

		return view;
	}

	private void updateStats() {
		long typeId = typeSpinner.getSelectedItemId();

//		ArrayList<EventData> eventDataList =
//				DatabaseManager.getInstance().requestEvents(typeId, true);

//		StatView eventDayFreq = new StatView(
//				getContext(),
//				"Times per day",
//				String.valueOf(
//						StatsUtils.computeEventFrequency(eventDataList, Duration.standardDays(1))
//				)
//		);
//
//		StatView eventWeekFreq = new StatView(
//				getContext(),
//				"Times per week",
//				String.valueOf(
//						StatsUtils.computeEventFrequency(eventDataList, Duration.standardDays(7))
//				)
//		);
//
//		eventStatsTable.removeAllViews();
//
//		eventStatsTable.addView(eventDayFreq);
//		eventStatsTable.addView(eventWeekFreq);


		ArrayList<HobbyData> hobbyDataList =
				DatabaseManager.getInstance().requestStoppedHobbies(typeId, true);
		HobbyStatManager hobbyStats = new HobbyStatManager(hobbyDataList);

		StatView hobbyDayFreq = new StatView(getContext());
		hobbyDayFreq.setStatName("Time per day");
		hobbyDayFreq.setStatValue(String.valueOf(
				TimeUtils.toSimpleString(
						hobbyStats.getHobbyDurationFrequency(Duration.standardDays(1))
						.toPeriod().normalizedStandard()
				)
		));


		StatView hobbyWeekFreq = new StatView(getContext());
		hobbyWeekFreq.setStatName("Time per week");
		hobbyWeekFreq.setStatValue(String.valueOf(
				TimeUtils.toSimpleString(
						hobbyStats.getHobbyDurationFrequency(Duration.standardDays(7))
						.toPeriod().normalizedStandard()
				)
		));

		StatView hobbyEarlyStart = new StatView(getContext());
		hobbyEarlyStart.setStatName("Earliest start date");
		hobbyEarlyStart.setStatValue(String.valueOf(
				TimeUtils.toSimpleString(
						hobbyStats.getEarliestStartDate()
				)
		));

		StatView hobbyLateEnd = new StatView(getContext());
		hobbyLateEnd.setStatName("Latest end date");
		hobbyLateEnd.setStatValue(String.valueOf(
				TimeUtils.toSimpleString(
						hobbyStats.getLatestEndDate()
				)
		));

		StatView hobbyTotalDuration = new StatView(getContext());
		hobbyTotalDuration.setStatName("Total time spent");
		hobbyTotalDuration.setStatValue(String.valueOf(
				TimeUtils.toSimpleString(
						hobbyStats.getTotalHobbyDuration()
								.toPeriod().normalizedStandard()
				)
		));

		hobbyStatsTable.removeAllViews();
		hobbyStatsTable.addView(hobbyEarlyStart);
		hobbyStatsTable.addView(hobbyLateEnd);
		hobbyStatsTable.addView(hobbyTotalDuration);
		hobbyStatsTable.addView(hobbyDayFreq);
		hobbyStatsTable.addView(hobbyWeekFreq);
	}
}
