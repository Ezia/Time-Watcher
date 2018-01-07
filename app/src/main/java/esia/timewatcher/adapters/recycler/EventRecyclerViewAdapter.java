package esia.timewatcher.adapters.recycler;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
		super(context, null, R.layout.event_view);
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
		ImageButton modeButton;


		public EventViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.name);
			date = (TextView) itemView.findViewById(R.id.date);

			modeButton = (ImageButton) itemView.findViewById(R.id.more_button);

			modeButton.setOnClickListener(v -> {
				PopupMenu popup = new PopupMenu(context, v);
				popup.inflate(R.menu.deletion_menu);
				popup.setOnMenuItemClickListener(item -> onPopupMenuItemClick(item));
				popup.show();
			});
		}

		private boolean onPopupMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.delete_menu_item:
					DatabaseManager.getInstance().deleteEvent(getItemId());
					Toast.makeText(context, "Hobby deleted", Toast.LENGTH_SHORT).show();
					return true;
			}
			return false;
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
