package esia.timewatcher.adapters.spinner;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.LinkedList;

import esia.timewatcher.R;

public class ActionSpinnerAdapter implements SpinnerAdapter {

	public interface ButtonClickListener {
		void onButtonClick(Action action);
	}

	private Context context;
	private LinkedList<Action> actions;
	private ButtonClickListener listener;

	public ActionSpinnerAdapter(Context context, Collection<? extends Action> actions) {
		this.context = context;
		this.actions = new LinkedList<>(actions);
	}

	public void setButtonClickListener(ButtonClickListener listener) {
		this.listener = listener;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.action_drop_down_view, null);
		} else {
			newView = convertView;
		}

		TextView text = (TextView) newView.findViewById(R.id.text);
		text.setText(actions.get(position).getName());

		return newView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.action_view, null);
		} else {
			newView = convertView;
		}

		newView.setOnClickListener(view -> {
			if (listener != null) {
				listener.onButtonClick(actions.get(position));
			}
		});

		TextView text = (TextView) newView.findViewById(R.id.button);
		text.setText(actions.get(position).getName());

		return newView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {}

	@Override
	public int getCount() {
		return actions.size();
	}

	@Override
	public Object getItem(int position) {
		return actions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return actions.get(position).getId();
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
