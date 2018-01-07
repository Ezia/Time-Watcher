package esia.timewatcher.adapters.recycler;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

	public RunningHobbyRecyclerViewAdapter(Context context, DialogListener dialogListener) {
		super(context, dialogListener, R.layout.running_hobby_view);
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

			name = (TextView) itemView.findViewById(R.id.name);
			startDate = (TextView) itemView.findViewById(R.id.start_date);
			remainingTime = (TextView) itemView.findViewById(R.id.elapsed_time);

			ImageButton moreButton = (ImageButton) itemView.findViewById(R.id.more_button);
			moreButton.setOnClickListener(v -> {
					PopupMenu popup = new PopupMenu(context, v);
					popup.inflate(R.menu.running_hobby_menu);
					popup.setOnMenuItemClickListener(item -> onPopupMenuItemClick(item));
					popup.show();
			});
		}

		private boolean onPopupMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.stop_menu_item:
					HobbyData data = dataList.get(getAdapterPosition());
					Hobby newHobby = new Hobby(data.getHobby().getStartDate(), new DateTime());
					DatabaseManager.getInstance().updateHobby(data.getId(), newHobby,
							data.getTypeData().getId());
					Toast.makeText(context, "Hobby stopped", Toast.LENGTH_SHORT).show();
					return true;
				case R.id.stop_plus_menu_item:
					notifyDialogRequest(CustomStopDialogFragment.newInstance(getItemId()));
					return true;
				case R.id.cancel_menu_item:
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
			updateTimer(hobbyData);
		}

		public void updateTimer(HobbyData data) {
			Period elapsedTime = new Period(data.getHobby().getStartDate(),
					DateTime.now());
			remainingTime.setText("(" + TimeUtils.toSimpleString(elapsedTime) + ")");
		}
	}
}
