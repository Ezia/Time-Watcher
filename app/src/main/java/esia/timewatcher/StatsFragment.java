package esia.timewatcher;

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

import java.util.ArrayList;

import esia.timewatcher.adapters.recycler.EventRecyclerViewAdapter;
import esia.timewatcher.adapters.recycler.StoppedHobbyRecyclerViewAdapter;
import esia.timewatcher.adapters.spinner.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Type;

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

		TypeData typeData = DatabaseManager.getInstance().requestType(typeId);

		computeHobbyStats(typeData);
	}

	private void computeHobbyStats(TypeData typeData) {
	}
}
