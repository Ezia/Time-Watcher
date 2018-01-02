package esia.timewatcher.adapters.recycler;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import esia.timewatcher.R;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.utils.TimeUtils;

public class EventRecyclerViewAdapter
		extends SimpleRecyclerViewAdapter<EventData, EventRecyclerViewAdapter.EventViewHolder> {

	public EventRecyclerViewAdapter(Context context) {
		super(context, R.layout.event_view);
		dataList = DatabaseManager.getInstance().requestEvents(false);
	}

	@Override
	public void onDatabaseChange() {
		dataList = DatabaseManager.getInstance().requestEvents(false);
		notifyDataSetChanged();
	}

	@Override
	public EventViewHolder createViewHolder(View v) {
		return new EventViewHolder(v);
	}

	protected class EventViewHolder extends SimpleRecyclerViewAdapter.SimpleViewHolder {
		TextView name;
		TextView date;


		public EventViewHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			date = itemView.findViewById(R.id.date);
			itemView.setOnLongClickListener(v -> onLongClick(v));
		}

		@Override
		public void deleteData() {
			DatabaseManager.getInstance().deleteEvent(getItemId());
			Toast.makeText(context, "Hobby deleted", Toast.LENGTH_SHORT).show();
		}

		public void set(Data data) {
			EventData eventData = (EventData)data;
			assert(eventData != null);

			name.setText(eventData.getTypeData().getType().getName());
			name.setBackground(new BitmapDrawable(context.getResources(),
					eventData.getTypeData().getType().getIcon()
			));
			date.setText(TimeUtils.toSimpleString(eventData.getEvent().getDate()));
		}
	}
}
