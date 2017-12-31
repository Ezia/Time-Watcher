package esia.timewatcher.adapters.recycler;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.TextView;

import org.joda.time.Period;

import esia.timewatcher.R;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.utils.TimeUtils;


public class StoppedHobbyRecyclerViewAdapter
		extends SimpleRecyclerViewAdapter<HobbyData, StoppedHobbyRecyclerViewAdapter.StoppedHobbyViewHolder> {

	public StoppedHobbyRecyclerViewAdapter(Context context) {
		super(context, R.layout.stopped_hobby_view);
		dataList = DatabaseManager.getInstance().requestStoppedHobbies(true);
	}

	public void onDatabaseChange() {
		dataList = DatabaseManager.getInstance().requestStoppedHobbies(true);
		notifyDataSetChanged();
	}

	@Override
	public StoppedHobbyViewHolder createViewHolder(View v) {
		return new StoppedHobbyViewHolder(v);
	}

	protected class StoppedHobbyViewHolder extends SimpleRecyclerViewAdapter.SimpleViewHolder {
		TextView name;
		TextView startDate;
		TextView stopDate;
		TextView remainingTime;

		public StoppedHobbyViewHolder(View itemView) {
			super(itemView);

			name = itemView.findViewById(R.id.name);
			startDate = itemView.findViewById(R.id.start_date);
			remainingTime = itemView.findViewById(R.id.elapsed_time);
			stopDate = itemView.findViewById(R.id.stop_date);
		}

		public void set(Data data) {
			HobbyData hobbyData = (HobbyData)data;
			assert(hobbyData != null);

			name.setBackground(new BitmapDrawable(context.getResources(),
					hobbyData.getTypeData().getType().getIcon()
			));
			name.setText(hobbyData.getTypeData().getType().getName());
			startDate.setText(TimeUtils.toString(hobbyData.getHobby().getStartDate()));
			stopDate.setText(TimeUtils.toString(hobbyData.getHobby().getEndDate()));
			Period elapsedTime = new Period(hobbyData.getHobby().getStartDate(),
					hobbyData.getHobby().getEndDate());
			remainingTime.setText("(" + TimeUtils.toString(elapsedTime) + ")");
		}
	}
}
