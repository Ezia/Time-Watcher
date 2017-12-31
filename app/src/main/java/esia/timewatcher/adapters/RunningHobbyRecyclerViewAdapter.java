package esia.timewatcher.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.LinkedList;

import esia.timewatcher.R;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.utils.TimeUtils;

public class RunningHobbyRecyclerViewAdapter
		extends SimpleRecyclerViewAdapter<HobbyData, RunningHobbyRecyclerViewAdapter.RunningHobbyViewHolder> {

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
		Button stopButton;

		public RunningHobbyViewHolder(View itemView) {
			super(itemView);

			name = itemView.findViewById(R.id.name);
			startDate = itemView.findViewById(R.id.start_date);
			remainingTime = itemView.findViewById(R.id.elapsed_time);
			stopButton = itemView.findViewById(R.id.stop_button);
		}

		public void set(Data data) {
			HobbyData hobbyData = (HobbyData)data;
			assert(hobbyData != null);

			name.setBackground(new BitmapDrawable(context.getResources(),
					hobbyData.getOccupationTypeData().getOccupationType().getIcon()
			));
			name.setText(hobbyData.getOccupationTypeData().getOccupationType().getName());
			startDate.setText(TimeUtils.toString(hobbyData.getHobby().getStartDate()));
			updateTimer(hobbyData);
			stopButton.setOnClickListener((v) -> onStopClick());
		}

		public void onStopClick() {
			stopButton.setOnClickListener(null);
			HobbyData data = dataList.get(getAdapterPosition());
			Hobby newHobby = new Hobby(data.getHobby().getStartDate(), new DateTime());
			DatabaseManager.getInstance().updateHobby(data.getId(), newHobby,
					data.getOccupationTypeData().getId());
		}

		public void updateTimer(HobbyData data) {
			Period elapsedTime = new Period(data.getHobby().getStartDate(),
					DateTime.now());
			remainingTime.setText("(" + TimeUtils.toString(elapsedTime) + ")");
		}
	}
}
