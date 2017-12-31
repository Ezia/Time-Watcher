package esia.timewatcher.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.Period;

import java.util.LinkedList;

import esia.timewatcher.R;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.utils.TimeUtils;

public class EventRecyclerViewAdapter
		extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder> {


	private Context context;
	private LinkedList<EventData> itemList;

	public EventRecyclerViewAdapter(Context context) {
		this.context = context;
		itemList = DatabaseManager.getInstance().requestEvents(false);
		setHasStableIds(true);
	}

	public void updateFromDatabase() {
		itemList = DatabaseManager.getInstance().requestEvents(false);
		notifyDataSetChanged();
	}

	@Override
	public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View newView = LayoutInflater.from(context)
				.inflate(R.layout.event_view, parent, false);
		return new EventViewHolder(newView);
	}


	@Override
	public void onBindViewHolder(EventViewHolder holder, int position) {
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

	protected class EventViewHolder extends RecyclerView.ViewHolder {
		TextView name;
		TextView date;


		public EventViewHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			date = itemView.findViewById(R.id.date);
		}

		public void set(EventData data) {
			name.setText(data.getOccupationTypeData().getOccupationType().getName());
			name.setBackground(new BitmapDrawable(context.getResources(),
					data.getOccupationTypeData().getOccupationType().getIcon()
			));
			date.setText(TimeUtils.toString(data.getEvent().getDate()));
		}
	}
}
