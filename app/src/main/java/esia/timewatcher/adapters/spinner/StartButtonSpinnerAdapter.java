package esia.timewatcher.adapters.spinner;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import esia.timewatcher.R;

public class StartButtonSpinnerAdapter implements SpinnerAdapter {

	public enum ActionType {
		HOBBY("Hobby"),
		HOBBY_PLUS("Hobby+"),
		EVENT("Event"),
		EVENT_PLUS("Event+");

		private String toString;
		ActionType(String toString) {
			this.toString = toString;
		}

		@Override
		public String toString() {
			return toString;
		}
	}

	public interface ButtonClickListener {
		void onButtonClick(ActionType action);
	}

	private Context context;
	private ActionType[] actions = {
			ActionType.HOBBY,
			ActionType.HOBBY_PLUS,
			ActionType.EVENT,
			ActionType.EVENT_PLUS
	};
	private ButtonClickListener listener;

	public StartButtonSpinnerAdapter(Context context) {
		this.context = context;
	}

	public void setButtonClickListener(ButtonClickListener listener) {
		this.listener = listener;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.start_button_drop_down_view, null);
		} else {
			newView = convertView;
		}

		TextView text = newView.findViewById(R.id.text);
		text.setText(actions[position].toString());

		return newView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.start_button_view, null);
		} else {
			newView = convertView;
		}

		newView.setOnClickListener(view -> {
			if (listener != null) {
				listener.onButtonClick(actions[position]);
			}
		});

		TextView text = newView.findViewById(R.id.button);
		text.setText(actions[position].toString());

		return newView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {}

	@Override
	public int getCount() {
		return actions.length;
	}

	@Override
	public Object getItem(int position) {
		return actions[position];
	}

	@Override
	public long getItemId(int position) {
		return actions[position].ordinal();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}
}
