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


public class StoppedHobbyRecyclerViewAdapter
		extends RecyclerView.Adapter<StoppedHobbyRecyclerViewAdapter.StoppedHobbyItem>{


	private Context context;
	private LinkedList<HobbyData> itemList;

	public StoppedHobbyRecyclerViewAdapter(Context context) {
		this.context = context;
		itemList = DatabaseManager.getInstance().requestStoppedHobbies(true);
		setHasStableIds(true);
	}

	public void updateFromDatabase() {
		itemList = DatabaseManager.getInstance().requestStoppedHobbies(true);
		notifyDataSetChanged();
	}

	@Override
	public StoppedHobbyItem onCreateViewHolder(ViewGroup parent, int viewType) {
		View newView = LayoutInflater.from(context)
				.inflate(R.layout.stopped_hobby_view, parent, false);
		return new StoppedHobbyItem(newView);
	}


	@Override
	public void onBindViewHolder(StoppedHobbyItem holder, int position) {
		holder.set(itemList.get(position));
	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}

	@Override
	public long getItemId(int position) {
		return itemList.get(position).getId();
	}

	protected class StoppedHobbyItem extends RecyclerView.ViewHolder {
		TextView name;
		TextView startDate;
		TextView stopDate;
		TextView remainingTime;

		public StoppedHobbyItem(View itemView) {
			super(itemView);

			name = itemView.findViewById(R.id.name);
			startDate = itemView.findViewById(R.id.start_date);
			remainingTime = itemView.findViewById(R.id.elapsed_time);
			stopDate = itemView.findViewById(R.id.stop_date);
		}

		public void set(HobbyData data) {
			name.setBackground(new BitmapDrawable(context.getResources(),
					data.getOccupationTypeData().getOccupationType().getIcon()
			));
			name.setText(data.getOccupationTypeData().getOccupationType().getName());
			startDate.setText(TimeUtils.toString(data.getHobby().getStartDate()));
			stopDate.setText(TimeUtils.toString(data.getHobby().getEndDate()));
			Period elapsedTime = new Period(data.getHobby().getStartDate(),
					data.getHobby().getEndDate());
			remainingTime.setText("(" + TimeUtils.toString(elapsedTime) + ")");
		}
	}
}
