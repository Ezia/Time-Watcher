package esia.timewatcher.adapters.recycler;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Arrays;

import esia.timewatcher.CustomStopDialogFragment;
import esia.timewatcher.R;
import esia.timewatcher.adapters.spinner.Action;
import esia.timewatcher.adapters.spinner.ActionSpinnerAdapter;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.utils.TimeUtils;

public class RunningHobbyRecyclerViewAdapter
		extends SimpleRecyclerViewAdapter<HobbyData, RunningHobbyRecyclerViewAdapter.RunningHobbyViewHolder> {

	private enum RunningHobbyAction implements Action {
		CANCEL("Cancel"),
		STOP("Stop"),
		STOP_PLUS("Stop...");

		private String name;
		RunningHobbyAction(String name) {
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

	public RunningHobbyRecyclerViewAdapter(Context context) {
		super(context, R.layout.running_hobby_view);
		dataList = DatabaseManager.getInstance().requestRunningHobbies(true);
	}

	public void onDatabaseChange() {
		dataList = DatabaseManager.getInstance().requestRunningHobbies(true);
		notifyDataSetChanged();
	}

	public void updateTimers() {
		if (recyclerView != null) {
			for (HobbyData data : dataList) {
				RunningHobbyViewHolder vh = (RunningHobbyViewHolder)
						recyclerView.findViewHolderForItemId(data.getId());
				if (vh != null) {
					vh.updateTimer(data);
				}
			}
		}
	}

	@Override
	public RunningHobbyViewHolder createViewHolder(View v) {
		return new RunningHobbyViewHolder(v);
	}

	protected class RunningHobbyViewHolder extends SimpleRecyclerViewAdapter.SimpleViewHolder {
		TextView name;
		TextView startDate;
		TextView remainingTime;

		public RunningHobbyViewHolder(View itemView) {
			super(itemView);

			name = itemView.findViewById(R.id.name);
			startDate = itemView.findViewById(R.id.start_date);
			remainingTime = itemView.findViewById(R.id.elapsed_time);

			Spinner actionSpinner = itemView.findViewById(R.id.action_spinner);
			ActionSpinnerAdapter actionAdapter = new ActionSpinnerAdapter(context,
					Arrays.asList(RunningHobbyAction.values()));
			actionAdapter.setButtonClickListener(action -> onActionClick(action));
			actionSpinner.setAdapter(actionAdapter);
			actionSpinner.setSelection(RunningHobbyAction.STOP.ordinal());
		}

		private void onActionClick(Action action) {
			switch (((RunningHobbyAction)action)) {
				case STOP:
					HobbyData data = dataList.get(getAdapterPosition());
					Hobby newHobby = new Hobby(data.getHobby().getStartDate(), new DateTime());
					DatabaseManager.getInstance().updateHobby(data.getId(), newHobby,
							data.getTypeData().getId());
					Toast.makeText(context, "Hobby stopped", Toast.LENGTH_SHORT).show();
					break;
				case CANCEL:
					DatabaseManager.getInstance().deleteHobby(getItemId());
					Toast.makeText(context, "Hobby deleted", Toast.LENGTH_SHORT).show();
					break;
				case STOP_PLUS:
					notifyDialogRequest(CustomStopDialogFragment.newInstance(getItemId()));
					break;
			}
		}

		public void set(Data data) {
			HobbyData hobbyData = (HobbyData)data;
			assert(hobbyData != null);

			name.setBackground(new BitmapDrawable(context.getResources(),
					hobbyData.getTypeData().getType().getIcon()
			));
			name.setText(hobbyData.getTypeData().getType().getName());
			startDate.setText(TimeUtils.toSimpleString(hobbyData.getHobby().getStartDate()));
			updateTimer(hobbyData);
		}

		@Override
		public void fillPopup(PopupMenu popup) {}

		@Override
		public void deleteData() {}

		public void updateTimer(HobbyData data) {
			Period elapsedTime = new Period(data.getHobby().getStartDate(),
					DateTime.now());
			remainingTime.setText("(" + TimeUtils.toSimpleString(elapsedTime) + ")");
		}
	}
}
