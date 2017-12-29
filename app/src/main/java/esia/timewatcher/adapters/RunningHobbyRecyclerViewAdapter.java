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
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.utils.TimeUtils;

public class RunningHobbyRecyclerViewAdapter
		extends RecyclerView.Adapter<RunningHobbyRecyclerViewAdapter.RunningHobbyItem> {

	private Context context;
	private RecyclerView recyclerView = null;
	private LinkedList<HobbyData> itemList;

	public RunningHobbyRecyclerViewAdapter(Context context) {
		this.context = context;
		itemList = DatabaseManager.getInstance().requestRunningHobbies();
		setHasStableIds(true);
	}

	public void updateFromDatabase() {
		itemList = DatabaseManager.getInstance().requestRunningHobbies();
		notifyDataSetChanged();
	}

	public void updateTimers() {
		if (recyclerView != null) {
			for (HobbyData data : itemList) {
				RunningHobbyItem vh = (RunningHobbyItem)
						recyclerView.findViewHolderForItemId(data.getId());
				if (vh != null) {
					vh.updateTimer(data);
				}
			}
		}
	}

	@Override
	public RunningHobbyItem onCreateViewHolder(ViewGroup parent, int viewType) {
		View newView = LayoutInflater.from(context)
				.inflate(R.layout.running_hobby_layout, parent, false);
		return new RunningHobbyItem(newView);
	}


	@Override
	public void onBindViewHolder(RunningHobbyItem holder, int position) {
		holder.set(itemList.get(position));
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		this.recyclerView = recyclerView;
	}

	@Override
	public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		this.recyclerView = null;
	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}

	@Override
	public long getItemId(int position) {
		return itemList.get(position).getId();
	}

	protected class RunningHobbyItem extends RecyclerView.ViewHolder {
		TextView name;
		TextView startDate;
		TextView remainingTime;

		public RunningHobbyItem(View itemView) {
			super(itemView);

			name = itemView.findViewById(R.id.name);
			startDate = itemView.findViewById(R.id.start_date);
			remainingTime = itemView.findViewById(R.id.elapsed_time);

			Button stopButton = itemView.findViewById(R.id.stop_button);
			stopButton.setOnClickListener((v) -> {
				v.setEnabled(false);
				onStopClick(getAdapterPosition());
			});
		}

		public void set(HobbyData data) {
			name.setBackground(new BitmapDrawable(context.getResources(),
					data.getOccupationTypeData().getOccupationType().getIcon()
			));
			name.setText(data.getOccupationTypeData().getOccupationType().getName());
			startDate.setText(TimeUtils.toString(data.getHobby().getStartDate()));
			updateTimer(data);
		}

		public void updateTimer(HobbyData data) {
			Period elapsedTime = new Period(data.getHobby().getStartDate(),
					DateTime.now());
			remainingTime.setText("(" + TimeUtils.toString(elapsedTime) + ")");
		}
	}

	public void onStopClick(int position) {
		HobbyData data = itemList.get(position);
		Hobby newHobby = new Hobby(data.getHobby().getStartDate(), new DateTime());
		DatabaseManager.getInstance().updateHobby(data.getId(), newHobby,
				data.getOccupationTypeData().getId());
		updateFromDatabase();
	}
}
