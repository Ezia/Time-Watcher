package esia.timewatcher.adapters.recycler;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;

import esia.timewatcher.CustomStopDialogFragment;
import esia.timewatcher.R;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.structures.Hobby;
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
		ImageButton more_button;

		public StoppedHobbyViewHolder(View itemView) {
			super(itemView);

			name = (TextView) itemView.findViewById(R.id.name);
			startDate = (TextView) itemView.findViewById(R.id.start_date);
			remainingTime = (TextView) itemView.findViewById(R.id.elapsed_time);
			stopDate = (TextView) itemView.findViewById(R.id.stop_date);
			more_button = (ImageButton) itemView.findViewById(R.id.more_button);

			more_button.setOnClickListener(v -> {
				PopupMenu popup = new PopupMenu(context, v);
				popup.inflate(R.menu.deletion_menu);
				popup.setOnMenuItemClickListener(item -> onPopupMenuItemClick(item));
				popup.show();
			});
		}

		private boolean onPopupMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.delete_menu_item:
					DatabaseManager.getInstance().deleteHobby(getItemId());
					Toast.makeText(context, "Hobby deleted", Toast.LENGTH_SHORT).show();
					return true;
			}
			return false;
		}

		public void set(Data data) {
			HobbyData hobbyData = (HobbyData)data;
			assert(hobbyData != null);

			name.setBackground(new BitmapDrawable(context.getResources(),
					hobbyData.getTypeData().getType().getIcon()
			));
			name.setText(hobbyData.getTypeData().getType().getName());
			startDate.setText(TimeUtils.toSimpleString(hobbyData.getHobby().getStartDate()));
			stopDate.setText(TimeUtils.toSimpleString(hobbyData.getHobby().getEndDate()));
			Period elapsedTime = new Period(hobbyData.getHobby().getStartDate(),
					hobbyData.getHobby().getEndDate());
			remainingTime.setText("(" + TimeUtils.toSimpleString(elapsedTime) + ")");
		}
	}
}
