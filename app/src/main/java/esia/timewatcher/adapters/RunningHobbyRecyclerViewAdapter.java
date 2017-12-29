package esia.timewatcher.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.Date;
import java.util.LinkedList;

import esia.timewatcher.R;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.utils.TimeUtils;

public class RunningHobbyRecyclerViewAdapter
		extends RecyclerView.Adapter<RunningHobbyRecyclerViewAdapter.RunningActivityItem> {

	private Context context;
	private LinkedList<HobbyData> itemList;

	public RunningHobbyRecyclerViewAdapter(Context context) {
		this.context = context;
		itemList = DatabaseManager.getInstance().requestRunningHobbies();
	}

	public void update() {
		itemList = DatabaseManager.getInstance().requestRunningHobbies();
		notifyDataSetChanged();
	}

	@Override
	public RunningActivityItem onCreateViewHolder(ViewGroup parent, int viewType) {
		View newView = View.inflate(context, R.layout.running_hobby_layout, null);
		return new RunningActivityItem(newView);
	}

	@Override
	public void onBindViewHolder(RunningActivityItem holder, int position) {
		holder.set(itemList.get(position));
	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}

	public class RunningActivityItem extends RecyclerView.ViewHolder {
		ImageView icon;
		TextView name;
		TextView startDate;
		TextView remainingTime;

		public RunningActivityItem(View itemView) {
			super(itemView);
			icon = itemView.findViewById(R.id.icon);
			name = itemView.findViewById(R.id.name);
			startDate = itemView.findViewById(R.id.start_date);
			remainingTime = itemView.findViewById(R.id.elapsed_time);
		}

		public void set(HobbyData data) {
			String startDateText = TimeUtils.toString(data.getHobby().getStartDate());

			icon.setImageBitmap(data.getOccupationTypeData().getOccupationType().getIcon());
			name.setText(data.getOccupationTypeData().getOccupationType().getName());
			startDate.setText(startDateText);
			Period elapsedTime = new Period(data.getHobby().getStartDate(),
					DateTime.now());
			remainingTime.setText("(" + TimeUtils.toString(elapsedTime) + ")");
		}
	}
}
